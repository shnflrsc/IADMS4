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
 * Controllers have no business rules; they only connect views to services.
 */
public class AuthController {
    private final AuthService auth;
    private final PersonService persons;

    // Creates the controller using the given services.
    public AuthController(AuthService auth, PersonService persons) {
        this.auth = auth;
        this.persons = persons;
    }

    // Returns true if no account exists yet.
    public boolean needsSetup() {
        return auth.needsSetup();
    }

    // Returns true if someone is logged in.
    public boolean isLoggedIn() {
        return Session.getInstance().isLoggedIn();
    }

    // Returns the person who is logged in, or null if nobody is.
    public Person currentPerson() {
        return Session.getInstance().getPerson();
    }

    // Creates the first coordinator from the given details and logs them in.
    public void setup(String name, Gender gender, LocalDate birthdate, String employeeId, String email,
            String password) {
        Account account = persons.registerCoordinator(name, gender, birthdate, employeeId, email, password);
        auth.startSession(account);
    }

    // Logs a user in with the given email and password.
    public void login(String email, String password) {
        auth.login(email, password);
    }

    // Logs the current user out.
    public void logout() {
        auth.logout();
    }
}
