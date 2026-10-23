package iadms4.service;

import iadms4.model.Account;
import iadms4.model.Person;
import iadms4.model.Session;
import iadms4.repository.AccountRepository;

/*
 * SERVICE. Business rules for logging in and out and for account credentials.
 *
 * Responsibilities: checks the email and password against AccountRepository and, on success,
 * stores the account in the Session singleton. It also handles password changes, email changes,
 * and password resets done by a coordinator. Rule violations are reported by throwing exceptions
 * with a user-friendly message.
 */
public class AuthService {
    // Where the accounts are stored.
    private final AccountRepository accounts;

    /*
     * Creates the service.
     *
     * It takes accounts (the repository that stores accounts).
     */
    public AuthService(AccountRepository accounts) {
        this.accounts = accounts;
    }

    /*
     * Tells whether the system is brand new and still needs its first Coordinator account.
     *
     * It returns true if no account exists yet.
     */
    public boolean needsSetup() {
        return accounts.isEmpty();
    }

    /*
     * Logs a user in. The same error message is used for an unknown email and a wrong password, so we do
     * not reveal which emails exist.
     *
     * It takes email (the email typed by the user) and password (the password typed by the user). It
     * returns the account that was logged in. It throws IllegalArgumentException if the email or password
     * is wrong.
     */
    public Account login(String email, String password) {
        // Same message for unknown email and wrong password, so we do not reveal which emails exist.
        Account account = accounts.findByEmail(email == null ? "" : email)
                .filter(a -> a.getPassword().equals(password))
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        Session.getInstance().login(account);
        return account;
    }

    /*
     * Starts a session for an account without asking for a password (used right after the first-time
     * setup creates the first coordinator).
     *
     * It takes account (the account to log in).
     */
    public void startSession(Account account) {
        Session.getInstance().login(account);
    }

    /*
     * The logged-in user changes their own password. They must know the current one.
     *
     * It takes current (the current password), newPassword (the new password) and confirm (the new password
     * typed a second time). It throws IllegalArgumentException if the current password is wrong, the new
     * one is too short, or the confirmation does not match.
     */
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

    /*
     * The logged-in user changes their login email. The password is required and the new email must be unused.
     *
     * It takes password (the user's current password) and newEmail (the new email). It throws
     * IllegalArgumentException if the password is wrong, the email is invalid, or it is already taken.
     */
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

    /*
     * A coordinator sets a new password for another person (password reset).
     *
     * It takes person (the person whose password is reset) and newPassword (the new password). It throws
     * IllegalArgumentException if the new password is too short. It throws IllegalStateException if the
     * person has no account.
     */
    public void resetPassword(Person person, String newPassword) {
        requireValidPassword(newPassword);
        Account account = accounts.findByPersonId(person.getId())
                .orElseThrow(() -> new IllegalStateException("That person has no account."));
        account.setPassword(newPassword);
    }

    /*
     * Checks that a password is acceptable (at least 4 characters).
     *
     * It takes password (the password to check). It throws IllegalArgumentException if the password is null
     * or too short.
     */
    private void requireValidPassword(String password) {
        if (password == null || password.length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters.");
        }
    }

    // Logs the current user out by clearing the session.
    public void logout() {
        Session.getInstance().logout();
    }
}
