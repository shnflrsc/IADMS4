package iadms4.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import iadms4.model.Account;

/**
 * REPOSITORY. Stores Accounts keyed by lower-cased email, so login is a simple lookup.
 */
public class AccountRepository {
    private final Map<String, Account> accounts = new LinkedHashMap<>();

    private static String key(String email) {
        return email.trim().toLowerCase();
    }

    /** Inserts or replaces the account with the same email. */
    public Account save(Account account) {
        accounts.put(key(account.getEmail()), account);
        return account;
    }

    /** Removes the account stored under this email (used when an email changes or a person is deleted). */
    public void remove(Account account) {
        accounts.remove(key(account.getEmail()));
    }

    public Optional<Account> findByEmail(String email) {
        return Optional.ofNullable(accounts.get(key(email)));
    }

    public Optional<Account> findByPersonId(int personId) {
        return accounts.values().stream().filter(a -> a.getPerson().getId() == personId).findFirst();
    }

    public boolean existsByEmail(String email) {
        return accounts.containsKey(key(email));
    }

    public boolean isEmpty() {
        return accounts.isEmpty();
    }

    public List<Account> findAll() {
        return new ArrayList<>(accounts.values());
    }
}
