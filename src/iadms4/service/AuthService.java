package iadms4.service;

import iadms4.model.Account;
import iadms4.model.Person;
import iadms4.model.Session;
import iadms4.repository.AccountRepository;

/*
 * SERVICE. Business rules for logging in and out and for account credentials.
 * Broken rules are reported by throwing an exception with a user-friendly message.
 */
public class AuthService {
    private final AccountRepository accounts;

    // Creates the service using the given account repository.
    public AuthService(AccountRepository accounts) {
        this.accounts = accounts;
    }

    // Returns true if no account exists yet, so the first coordinator still has to be created.
    public boolean needsSetup() {
        return accounts.isEmpty();
    }

    // Logs in with the given email and password. Both mistakes give the same message, so emails stay private.
    public void login(String email, String password) {
        Account account = accounts.findByEmail(email);
        if (account == null || !account.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid email or password.");
        }
        Session.getInstance().login(account);
    }

    // Starts a session for the given account without asking for a password (used after first-time setup).
    public void startSession(Account account) {
        Session.getInstance().login(account);
    }

    // Changes the logged-in user's password. They must give the current one and confirm the new one.
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

    // Changes the logged-in user's email to newEmail. They must give their password and the email must be unused.
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

    // Lets a coordinator set newPassword for the given person.
    public void resetPassword(Person person, String newPassword) {
        requireValidPassword(newPassword);
        Account account = accounts.findByPersonId(person.getId());
        if (account == null) {
            throw new IllegalStateException("That person has no account.");
        }
        account.setPassword(newPassword);
    }

    // Throws an exception if the given password is shorter than 4 characters.
    private void requireValidPassword(String password) {
        if (password == null || password.length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters.");
        }
    }

    // Logs the current user out.
    public void logout() {
        Session.getInstance().logout();
    }
}
