package iadms4.model;

/*
 * MODEL. email + password of one person
 * person is final so student -> intern needs a NEW account w/ same login
 */
public class Account {
    private final Person person;
    private String email;
    private String password;

    // constructor
    public Account(Person person, String email, String password) {
        this.person = person;
        this.email = email;
        this.password = password;
    }

    // owner of the account
    public Person getPerson() {
        return person;
    }

    // getter
    public String getEmail() {
        return email;
    }

    // setter
    public void setEmail(String email) {
        this.email = email;
    }

    // getter
    public String getPassword() {
        return password;
    }

    // setter
    public void setPassword(String password) {
        this.password = password;
    }
}
