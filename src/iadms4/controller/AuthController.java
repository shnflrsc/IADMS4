package iadms4.controller;

import java.time.LocalDate;

import iadms4.model.Account;
import iadms4.model.Gender;
import iadms4.model.Person;
import iadms4.model.Session;
import iadms4.service.AuthService;
import iadms4.service.PersonService;

/*
 * CONTROLLER. login, logout, first time setup
 * no rules here, it just passes things to the services
 */
public class AuthController {
    private final AuthService auth;
    private final PersonService persons;

    // makes the controller, takes auth, persons
    public AuthController(AuthService auth, PersonService persons) {
        this.auth = auth;
        this.persons = persons;
    }

    // true if theres no accounts yet
    public boolean needsSetup() {
        return auth.needsSetup();
    }

    // true if someone is logged in
    public boolean isLoggedIn() {
        return Session.getInstance().isLoggedIn();
    }

    // who is logged in, null if no one
    public Person currentPerson() {
        return Session.getInstance().getPerson();
    }

    // makes the very first coordinator and logs them in
    public void setup(String name, Gender gender, LocalDate birthdate, String employeeId, String email,
            String password) {
        Account account = persons.registerCoordinator(name, gender, birthdate, employeeId, email, password);
        auth.startSession(account);
    }

    // login
    public void login(String email, String password) {
        auth.login(email, password);
    }

    // logout
    public void logout() {
        auth.logout();
    }
}
