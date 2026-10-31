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

    // gets the Person who owns this account, type decides the dashboard
    public Person getPerson() {
        return person;
    }

    // gets the email used to log in (String)
    public String getEmail() {
        return email;
    }

    // sets new email, takes a String
    public void setEmail(String email) {
        this.email = email;
    }

    // gets the password, plain text for now (String)
    public String getPassword() {
        return password;
    }

    // sets new password, takes a String
    public void setPassword(String password) {
        this.password = password;
    }
}
