package iadms4.model;

/*
 * MODEL. email + password of one person
 * person is final so student -> intern needs a NEW account w/ same login
 */
public class Account {
    private final Person person;
    private String email;
    private String password;

    // makes an Account, takes person, email, password
    public Account(Person person, String email, String password) {
        this.person = person;
        this.email = email;
        this.password = password;
    }

    // owner of the account
    public Person getPerson() {
        return person;
    }

    // gets email (String)
    public String getEmail() {
        return email;
    }

    // sets new email, takes a String
    public void setEmail(String email) {
        this.email = email;
    }

    // gets password (String)
    public String getPassword() {
        return password;
    }

    // sets new password, takes a String
    public void setPassword(String password) {
        this.password = password;
    }
}
