package iadms4.repository;

import java.util.ArrayList;
import java.util.List;

import iadms4.model.Account;

/*
 * REPOSITORY. stores accounts
 * doesn't extend Repository because accounts use email not an int id
 * emails ignore capital letters and spaces
 */
public class AccountRepository {
    private final List<Account> accounts = new ArrayList<>();

    // trim and lowercase so emails compare properly
    private String normalize(String email) {
        return email.trim().toLowerCase();
    }

    // save account. same email means replace it, don't add a new one
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

    // finds the account with the same email and removes it from the list
    public void remove(Account account) {
        Account stored = findByEmail(account.getEmail());
        if (stored != null) {
            accounts.remove(stored);
        }
    }

    // find by email, null if none
    public Account findByEmail(String email) {
        for (Account account : accounts) {
            if (normalize(account.getEmail()).equals(normalize(email))) {
                return account;
            }
        }
        return null;
    }

    // find by person id, null if none
    public Account findByPersonId(int personId) {
        for (Account account : accounts) {
            if (account.getPerson().getId() == personId) {
                return account;
            }
        }
        return null;
    }

    // checks if the email is taken, returns true if it is
    public boolean existsByEmail(String email) {
        return findByEmail(email) != null;
    }

    // true on the very first run
    public boolean isEmpty() {
        return accounts.isEmpty();
    }
}
