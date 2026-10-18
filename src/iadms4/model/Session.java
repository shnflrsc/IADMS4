package iadms4.model;

/**
 * MODEL. Remembers who is currently logged in. There is only ever ONE session, so this is a
 * singleton: always use Session.getInstance(). Controllers read the logged-in person from here.
 */
public final class Session {
    private static final Session INSTANCE = new Session();

    private Account account;

    private Session() {
    }

    public static Session getInstance() {
        return INSTANCE;
    }

    public Account getAccount() {
        return account;
    }

    public Person getPerson() {
        return account == null ? null : account.getPerson();
    }

    public boolean isLoggedIn() {
        return account != null;
    }

    public void login(Account account) {
        this.account = account;
    }

    public void logout() {
        this.account = null;
    }
}
