package iadms4.service;

import iadms4.model.Account;
import iadms4.model.Person;
import iadms4.model.Session;
import iadms4.repository.AccountRepository;

/**
 * SERVICE. Login and logout rules, plus password/email changes and coordinator password resets. Checks email and password against AccountRepository and, on
 * success, stores the account in the Session singleton.
 */
public class AuthService {
    private final AccountRepository accounts;

    public AuthService(AccountRepository accounts) {
        this.accounts = accounts;
    }

    public boolean needsSetup() {
        return accounts.isEmpty();
    }

    public Account login(String email, String password) {
        // Same message for unknown email and wrong password, so we do not reveal which emails exist.
        Account account = accounts.findByEmail(email == null ? "" : email)
                .filter(a -> a.getPassword().equals(password))
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        Session.getInstance().login(account);
        return account;
    }

    public void startSession(Account account) {
        Session.getInstance().login(account);
    }

    /** The logged-in user changes their own password (must know the current one). */
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

    /** The logged-in user changes their login email (password required, email must be unused). */
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
        // The map is keyed by email, so take the account out, change it, and put it back.
        accounts.remove(account);
        account.setEmail(newEmail.trim());
        accounts.save(account);
    }

    /** A coordinator sets a new password for another person (password reset). */
    public void resetPassword(Person person, String newPassword) {
        requireValidPassword(newPassword);
        Account account = accounts.findByPersonId(person.getId())
                .orElseThrow(() -> new IllegalStateException("That person has no account."));
        account.setPassword(newPassword);
    }

    private void requireValidPassword(String password) {
        if (password == null || password.length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters.");
        }
    }

    public void logout() {
        Session.getInstance().logout();
    }
}
