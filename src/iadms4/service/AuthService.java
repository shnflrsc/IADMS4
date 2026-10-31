package iadms4.service;

import iadms4.model.Account;
import iadms4.model.Person;
import iadms4.model.Session;
import iadms4.repository.AccountRepository;

/*
 * SERVICE. login, logout, passwords, emails
 * breaks a rule = throws exception w/ a message for the user
 */
public class AuthService {
    private final AccountRepository accounts;

    // constructor
    public AuthService(AccountRepository accounts) {
        this.accounts = accounts;
    }

    // true if no accounts yet, need first coordinator
    public boolean needsSetup() {
        return accounts.isEmpty();
    }

    // login. same error for both so u cant guess emails
    public void login(String email, String password) {
        Account account = accounts.findByEmail(email);
        if (account == null || !account.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid email or password.");
        }
        Session.getInstance().login(account);
    }

    // login w/o password, only for after first setup
    public void startSession(Account account) {
        Session.getInstance().login(account);
    }

    // change password, needs current one + confirm
    public void changePassword(String current, String newPassword, String confirm) {
        Account account = Session.getInstance().getAccount();
        if (!account.getPassword().equals(current)) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }
        requireValidPassword(newPassword);
        if (!newPassword.equals(confirm)) {
            throw new IllegalArgumentException("New password and confirmation do not match.");
        }
        account.setPassword(newPassword);
    }

    // change email, needs password, email cant be taken
    public void changeEmail(String password, String newEmail) {
        Account account = Session.getInstance().getAccount();
        if (!account.getPassword().equals(password)) {
            throw new IllegalArgumentException("Password is incorrect.");
        }
        if (newEmail == null || newEmail.isBlank() || !newEmail.contains("@")) {
            throw new IllegalArgumentException("A valid email is required.");
        }
        if (accounts.existsByEmail(newEmail) && !account.getEmail().equalsIgnoreCase(newEmail.trim())) {
            throw new IllegalArgumentException("An account with that email already exists.");
        }
        accounts.remove(account);
        account.setEmail(newEmail.trim());
        accounts.save(account);
    }

    // coordinator resets someones password
    public void resetPassword(Person person, String newPassword) {
        requireValidPassword(newPassword);
        Account account = accounts.findByPersonId(person.getId());
        if (account == null) {
            throw new IllegalStateException("That person has no account.");
        }
        account.setPassword(newPassword);
    }

    // min 4 chars
    private void requireValidPassword(String password) {
        if (password == null || password.length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters.");
        }
    }

    // logout
    public void logout() {
        Session.getInstance().logout();
    }
}
