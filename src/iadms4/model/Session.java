package iadms4.model;

/*
 * MODEL. remembers who is logged in
 * singleton, only 1 session ever. use Session.getInstance()
 */
public class Session {
    private static final Session INSTANCE = new Session();

    // account thats logged in right now, null means nobody is
    private Account account;

    // private so nobody makes a 2nd session (singleton)
    private Session() {
    }

    // gets the one and only session, everyone has to use this
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

    // saves the account that just logged in
    public void login(Account account) {
        this.account = account;
    }

    // clears the account so nobody is logged in anymore
    public void logout() {
        account = null;
    }
}
