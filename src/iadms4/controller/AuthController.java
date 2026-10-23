package iadms4.controller;

import java.time.LocalDate;

import iadms4.model.Account;
import iadms4.model.Gender;
import iadms4.model.Person;
import iadms4.model.Session;
import iadms4.service.AuthService;
import iadms4.service.PersonService;

/*
 * CONTROLLER. Handles login, logout and first-time setup.
 *
 * Responsibilities: receives the requests from AuthView and forwards them to the services.
 * Controllers contain no business rules; they only connect views to services.
 */
public class AuthController {
    // Service that checks credentials and manages the session.
    private final AuthService auth;
    // Service that creates the first coordinator.
    private final PersonService persons;

    /*
     * Creates the controller.
     *
     * It takes auth (the authentication service) and persons (the person service).
     */
    public AuthController(AuthService auth, PersonService persons) {
        this.auth = auth;
        this.persons = persons;
    }

    /*
     * Asks whether the first-time setup is still needed.
     *
     * It returns true if no account exists yet.
     */
    public boolean needsSetup() {
        return auth.needsSetup();
    }

    /*
     * Asks whether someone is logged in.
     *
     * It returns true if there is an active session.
     */
    public boolean isLoggedIn() {
        return Session.getInstance().isLoggedIn();
    }

    /*
     * Gets the person who is logged in.
     *
     * It returns the current person, or null if nobody is logged in.
     */
    public Person currentPerson() {
        return Session.getInstance().getPerson();
    }

    /*
     * First-boot setup: creates the initial coordinator account and logs it in.
     *
     * It takes name (the full name), gender (the gender), birthdate (the date of birth), employeeId (the
     * employee number), email (the login email) and password (the login password).
     */
    public void setup(String name, Gender gender, LocalDate birthdate, String employeeId, String email,
            String password) {
        Account account = persons.registerCoordinator(name, gender, birthdate, employeeId, email, password);
        auth.startSession(account);
    }

    /*
     * Logs a user in.
     *
     * It takes email (the typed email) and password (the typed password).
     */
    public void login(String email, String password) {
        auth.login(email, password);
    }

    // Logs the current user out.
    public void logout() {
        auth.logout();
    }
}
