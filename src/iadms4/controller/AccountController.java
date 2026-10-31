package iadms4.controller;

import iadms4.model.Session;
import iadms4.service.AuthService;
import iadms4.service.PersonService;

/*
 * CONTROLLER. account settings stuff, same for all account types
 */
public class AccountController {
    private final AuthService auth;
    private final PersonService persons;

    // makes the controller, takes auth, persons
    public AccountController(AuthService auth, PersonService persons) {
        this.auth = auth;
        this.persons = persons;
    }

    // email of whoever is logged in
    public String email() {
        return persons.emailOf(Session.getInstance().getPerson());
    }

    // changes the password of whoever is logged in, service does the checks
    public void changePassword(String current, String newPassword, String confirm) {
        auth.changePassword(current, newPassword, confirm);
    }

    // changes the logged in user's email, password is needed to confirm its them
    public void changeEmail(String password, String newEmail) {
        auth.changeEmail(password, newEmail);
    }
}
