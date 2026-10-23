package iadms4.model;

/*
 * MODEL. The login credentials (email and password) of one Person.
 *
 * Responsibilities: links an email and password to the Person who owns them. The type of that Person
 * decides which dashboard is shown after login. The person is final, so when a Student becomes an Intern
 * a NEW Account object is created with the same email and password.
 */
public class Account {
    // The owner of this account.
    private final Person person;
    // Email used to log in.
    private String email;
    // Password used to log in.
    private String password;

    /*
     * Creates an account.
     *
     * It takes person (the person who owns the account), email (the login email) and password (the login
     * password).
     */
    public Account(Person person, String email, String password) {
        this.person = person;
        this.email = email;
        this.password = password;
    }

    /*
     * Gets the owner of the account.
     *
     * It returns the person who owns this account.
     */
    public Person getPerson() {
        return person;
    }

    /*
     * Gets the login email.
     *
     * It returns the email.
     */
    public String getEmail() {
        return email;
    }

    /*
     * Changes the login email.
     *
     * It takes email (the new email).
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /*
     * Gets the password.
     *
     * It returns the password.
     */
    public String getPassword() {
        return password;
    }

    /*
     * Changes the password.
     *
     * It takes password (the new password).
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
