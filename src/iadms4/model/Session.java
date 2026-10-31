package iadms4.model;

/*
 * MODEL. remembers who is logged in
 * singleton, only 1 session ever. use Session.getInstance()
 */
public class Session {
    private static final Session INSTANCE = new Session();

    // null = nobody logged in
    private Account account;

    // private so nobody makes a 2nd session (singleton)
    private Session() {
    }

    // the one session
    public static Session getInstance() {
        return INSTANCE;
    }

    // logged in account, null if none
    public Account getAccount() {
        return account;
    }

    // person of the logged in account, null if none
    public Person getPerson() {
        if (account == null) {
            return null;
        }
        return account.getPerson();
    }

    // true if someone is logged in
    public boolean isLoggedIn() {
        return account != null;
    }

    // start session
    public void login(Account account) {
        this.account = account;
    }

    // end session
    public void logout() {
        account = null;
    }
}
