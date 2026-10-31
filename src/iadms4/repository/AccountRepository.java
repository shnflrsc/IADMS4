package iadms4.repository;

import java.util.ArrayList;
import java.util.List;

import iadms4.model.Account;

/*
 * REPOSITORY. Stores Accounts.
 * It does not extend Repository because accounts are identified by email instead of a number.
 * Emails are compared without capital letters or outer spaces.
 */
public class AccountRepository {
    private final List<Account> accounts = new ArrayList<>();

    // Returns the given email trimmed and in lower case, which is the form used for comparing.
    private String normalize(String email) {
        return email.trim().toLowerCase();
    }

    // Stores the given account and returns it. An account with the same email is replaced in the same place.
    public Account save(Account account) {
        for (int i = 0; i < accounts.size(); i++) {
            if (normalize(accounts.get(i).getEmail()).equals(normalize(account.getEmail()))) {
                accounts.set(i, account);
                return account;
            }
        }
        accounts.add(account);
        return account;
    }

    // Removes the account that uses the same email as the given account.
    public void remove(Account account) {
        Account stored = findByEmail(account.getEmail());
        if (stored != null) {
            accounts.remove(stored);
        }
    }

    // Returns the account with the given email, or null if there is none.
    public Account findByEmail(String email) {
        for (Account account : accounts) {
            if (normalize(account.getEmail()).equals(normalize(email))) {
                return account;
            }
        }
        return null;
    }

    // Returns the account owned by the person with the given id, or null if there is none.
    public Account findByPersonId(int personId) {
        for (Account account : accounts) {
            if (account.getPerson().getId() == personId) {
                return account;
            }
        }
        return null;
    }

    // Returns true if an account already uses the given email.
    public boolean existsByEmail(String email) {
        return findByEmail(email) != null;
    }

    // Returns true if there are no accounts yet (the very first run of the program).
    public boolean isEmpty() {
        return accounts.isEmpty();
    }
}
