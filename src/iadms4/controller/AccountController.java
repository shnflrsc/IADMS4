package iadms4.controller;

import iadms4.model.Session;
import iadms4.service.AuthService;
import iadms4.service.PersonService;

/*
 * CONTROLLER. Handles the account settings shared by every account type.
 */
public class AccountController {
    private final AuthService auth;
    private final PersonService persons;

    // Creates the controller using the given services.
    public AccountController(AuthService auth, PersonService persons) {
        this.auth = auth;
        this.persons = persons;
    }

    // Returns the email of the logged-in user.
    public String email() {
        return persons.emailOf(Session.getInstance().getPerson());
    }

    // Changes the logged-in user's password.
    public void changePassword(String current, String newPassword, String confirm) {
        auth.changePassword(current, newPassword, confirm);
    }

    // Changes the logged-in user's email after checking their password.
    public void changeEmail(String password, String newEmail) {
        auth.changeEmail(password, newEmail);
    }
}
