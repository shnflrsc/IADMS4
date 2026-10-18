package iadms4.model;

/**
 * MODEL. Login credentials (email + password) that point to the Person who owns the account.
 * The type of the Person decides which dashboard is shown. The person is final, so when a Student
 * becomes an Intern a NEW Account object is created with the same email and password.
 */
public class Account {
    private final Person person;
    private String email;
    private String password;
    
    public Account(Person person, String email, String password) {
        this.person = person;
        this.email = email;
        this.password = password;
    }

    public Person getPerson() {
        return person;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
