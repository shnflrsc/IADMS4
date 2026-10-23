package iadms4.view;

import iadms4.controller.AccountController;

/*
 * VIEW. The account settings screen shared by every dashboard.
 *
 * Responsibilities: lets the logged-in user change their own password or login email by sending the
 * typed values to AccountController.
 */
public class AccountSettingsView {
    // Used for printing and reading.
    private final Console console;
    // Controller that handles the changes.
    private final AccountController controller;

    /*
     * Creates the view.
     *
     * It takes console (the console used for input and output) and controller (the account controller).
     */
    public AccountSettingsView(Console console, AccountController controller) {
        this.console = console;
        this.controller = controller;
    }

    // Shows the account settings menu until the user goes back.
    public void show() {
        new Menu(console, "Account Settings", "Back")
                .add("Change password", this::changePassword)
                .add("Change email", this::changeEmail)
                .run();
    }

    // Asks for the current and new passwords, then asks the controller to change the password.
    private void changePassword() {
        String current = console.readRequired("Current password");
        String next = console.readRequired("New password (min 4 chars)");
        String confirm = console.readRequired("Confirm new password");
        controller.changePassword(current, next, confirm);
        console.println("  Password changed.");
    }

    // Shows the current email, asks for the password and a new email, then asks the controller to change it.
    private void changeEmail() {
        console.println("  Current email: " + controller.email());
        String password = console.readRequired("Password");
        String email = console.readRequired("New email");
        controller.changeEmail(password, email);
        console.println("  Email changed.");
    }
}
