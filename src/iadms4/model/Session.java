package iadms4.model;

/*
 * MODEL. Remembers which account is currently logged in.
 *
 * Responsibilities: stores the logged-in Account. There is only ever ONE session, so this class
 * uses the singleton pattern: the constructor is private and everyone must call Session.getInstance().
 * Controllers read the logged-in person from here.
 */
public final class Session {
    // The one and only Session object, created when the class is loaded.
    private static final Session INSTANCE = new Session();

    // The account that is logged in, or null when nobody is.
    private Account account;

    // Private so that no other class can create a second Session.
    private Session() {
    }

    /*
     * Gets the single shared Session.
     *
     * It returns the only Session object.
     */
    public static Session getInstance() {
        return INSTANCE;
    }

    /*
     * Gets the logged-in account.
     *
     * It returns the account, or null if nobody is logged in.
     */
    public Account getAccount() {
        return account;
    }

    /*
     * Gets the logged-in person.
     *
     * It returns the owner of the logged-in account, or null if nobody is logged in.
     */
    public Person getPerson() {
        return account == null ? null : account.getPerson();
    }

    /*
     * Checks whether someone is logged in.
     *
     * It returns true if an account is logged in, otherwise false.
     */
    public boolean isLoggedIn() {
        return account != null;
    }

    /*
     * Starts a session for the given account.
     *
     * It takes account (the account that just logged in).
     */
    public void login(Account account) {
        this.account = account;
    }

    // Ends the session so that nobody is logged in.
    public void logout() {
        this.account = null;
    }
}
