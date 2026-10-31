package iadms4.model;

/*
 * MODEL. Remembers which account is currently logged in.
 * There is only ever one session, so this class is a singleton: the constructor is private and
 * every other class calls Session.getInstance().
 */
public class Session {
    private static final Session INSTANCE = new Session();

    // The logged-in account, or null when nobody is logged in.
    private Account account;

    // Private so that no other class can create a second Session.
    private Session() {
    }

    // Returns the one and only Session object.
    public static Session getInstance() {
        return INSTANCE;
    }

    // Returns the logged-in account, or null if nobody is logged in.
    public Account getAccount() {
        return account;
    }

    // Returns the owner of the logged-in account, or null if nobody is logged in.
    public Person getPerson() {
        if (account == null) {
            return null;
        }
        return account.getPerson();
    }

    // Returns true if someone is logged in.
    public boolean isLoggedIn() {
        return account != null;
    }

    // Starts a session for the given account.
    public void login(Account account) {
        this.account = account;
    }

    // Ends the session so that nobody is logged in.
    public void logout() {
        account = null;
    }
}
