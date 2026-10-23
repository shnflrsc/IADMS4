package iadms4.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import iadms4.model.Account;

/*
 * REPOSITORY. Stores Accounts keyed by lower-cased email, so login is a simple lookup.
 *
 * Responsibilities: save, remove and search accounts. It does not extend Repository because accounts
 * are identified by email instead of a numeric id. Lower-casing the key makes emails case-insensitive.
 */
public class AccountRepository {
    // Accounts stored by their normalized (trimmed, lower-case) email.
    private final Map<String, Account> accounts = new LinkedHashMap<>();

    /*
     * Converts an email into the form used as the map key.
     *
     * It takes email (the raw email text). It returns the email with spaces trimmed and letters in lower
     * case.
     */
    private static String key(String email) {
        return email.trim().toLowerCase();
    }

    /*
     * Inserts or replaces the account with the same email.
     *
     * It takes account (the account to store). It returns the same account that was stored.
     */
    public Account save(Account account) {
        accounts.put(key(account.getEmail()), account);
        return account;
    }

    /*
     * Removes the account stored under its email (used when an email changes or a person is deleted).
     *
     * It takes account (the account to remove).
     */
    public void remove(Account account) {
        accounts.remove(key(account.getEmail()));
    }

    /*
     * Finds an account by email, ignoring capital letters and surrounding spaces.
     *
     * It takes email (the email to search for). It returns an Optional with the account, or an empty
     * Optional if there is none.
     */
    public Optional<Account> findByEmail(String email) {
        return Optional.ofNullable(accounts.get(key(email)));
    }

    /*
     * Finds the account that belongs to a person.
     *
     * It takes personId (the id of the owner). It returns an Optional with the account, or an empty
     * Optional if the person has none.
     */
    public Optional<Account> findByPersonId(int personId) {
        return accounts.values().stream().filter(a -> a.getPerson().getId() == personId).findFirst();
    }

    /*
     * Checks whether an email is already registered.
     *
     * It takes email (the email to check). It returns true if an account already uses that email.
     */
    public boolean existsByEmail(String email) {
        return accounts.containsKey(key(email));
    }

    /*
     * Checks whether no account exists yet (true on the very first run of the program).
     *
     * It returns true if there are no accounts.
     */
    public boolean isEmpty() {
        return accounts.isEmpty();
    }

    /*
     * Gets every account.
     *
     * It returns a new list containing all accounts.
     */
    public List<Account> findAll() {
        return new ArrayList<>(accounts.values());
    }
}
