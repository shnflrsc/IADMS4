package iadms4.controller;

import java.time.LocalDate;

import iadms4.model.Account;
import iadms4.model.Gender;
import iadms4.model.Person;
import iadms4.model.Session;
import iadms4.service.AuthService;
import iadms4.service.PersonService;

/**
 * CONTROLLER. Receives login / logout / first-time-setup requests from AuthView and forwards them to
 * the services. Controllers contain no business rules; they only connect views to services.
 */
public class AuthController {
    private final AuthService auth;
    private final PersonService persons;

    public AuthController(AuthService auth, PersonService persons) {
        this.auth = auth;
        this.persons = persons;
    }

    public boolean needsSetup() {
        return auth.needsSetup();
    }

    public boolean isLoggedIn() {
        return Session.getInstance().isLoggedIn();
    }

    public Person currentPerson() {
        return Session.getInstance().getPerson();
    }

    /** First-boot setup: creates the initial coordinator account. */
    public void setup(String name, Gender gender, LocalDate birthdate, String employeeId, String email,
            String password) {
        Account account = persons.registerCoordinator(name, gender, birthdate, employeeId, email, password);
        auth.startSession(account);
    }

    public void login(String email, String password) {
        auth.login(email, password);
    }

    public void logout() {
        auth.logout();
    }
}
