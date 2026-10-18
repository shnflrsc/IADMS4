package iadms4.controller;

import iadms4.model.Session;
import iadms4.service.AuthService;
import iadms4.service.PersonService;

/**
 * CONTROLLER. Account settings shared by every account type: change your own password or email.
 * The logged-in account comes from Session.
 */
public class AccountController {
    private final AuthService auth;
    private final PersonService persons;

    public AccountController(AuthService auth, PersonService persons) {
        this.auth = auth;
        this.persons = persons;
    }

    public String email() {
        return persons.emailOf(Session.getInstance().getPerson());
    }

    public void changePassword(String current, String newPassword, String confirm) {
        auth.changePassword(current, newPassword, confirm);
    }

    public void changeEmail(String password, String newEmail) {
        auth.changeEmail(password, newEmail);
    }
}
