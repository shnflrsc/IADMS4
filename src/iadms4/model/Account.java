package iadms4.model;

/*
 * MODEL. The login email and password of one Person.
 * The person is final, so when a Student becomes an Intern a new Account is created with the same login.
 */
public class Account {
    private final Person person;
    private String email;
    private String password;

    // Creates an account for the given person with an email and a password.
    public Account(Person person, String email, String password) {
        this.person = person;
        this.email = email;
        this.password = password;
    }

    // Returns the person who owns this account.
    public Person getPerson() {
        return person;
    }

    // Returns the login email.
    public String getEmail() {
        return email;
    }

    // Changes the login email to the given email.
    public void setEmail(String email) {
        this.email = email;
    }

    // Returns the password.
    public String getPassword() {
        return password;
    }

    // Changes the password to the given password.
    public void setPassword(String password) {
        this.password = password;
    }
}
