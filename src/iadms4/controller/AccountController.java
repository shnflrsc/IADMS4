package iadms4.controller;

import iadms4.model.Session;
import iadms4.service.AuthService;
import iadms4.service.PersonService;

/*
 * CONTROLLER. Handles the account settings shared by every account type.
 *
 * Responsibilities: lets the logged-in user read their own email and change their own password or email.
 * It only forwards the requests to the services; the logged-in account comes from Session.
 */
public class AccountController {
    // Service that changes passwords and emails.
    private final AuthService auth;
    // Service used to look up the current email.
    private final PersonService persons;

    /*
     * Creates the controller.
     *
     * It takes auth (the authentication service) and persons (the person service).
     */
    public AccountController(AuthService auth, PersonService persons) {
        this.auth = auth;
        this.persons = persons;
    }

    /*
     * Gets the email of the logged-in user.
     *
     * It returns the current login email.
     */
    public String email() {
        return persons.emailOf(Session.getInstance().getPerson());
    }

    /*
     * Changes the logged-in user's password.
     *
     * It takes current (the current password), newPassword (the new password) and confirm (the new password
     * typed again).
     */
    public void changePassword(String current, String newPassword, String confirm) {
        auth.changePassword(current, newPassword, confirm);
    }

    /*
     * Changes the logged-in user's email.
     *
     * It takes password (the current password, to confirm the identity) and newEmail (the new email).
     */
    public void changeEmail(String password, String newEmail) {
        auth.changeEmail(password, newEmail);
    }
}
