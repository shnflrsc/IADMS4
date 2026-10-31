package iadms4.view;

import iadms4.controller.AccountController;

/*
 * VIEW. The account settings screen shared by every dashboard.
 * It lets the logged-in user change their own password or login email.
 */
public class AccountSettingsView extends Menu {
    private final AccountController controller;

    // Creates the view using the given console and controller.
    public AccountSettingsView(Console console, AccountController controller) {
        super(console);
        this.controller = controller;
    }

    // Shows the account settings menu until the user goes back.
    public void show() {
        String[] items = { "Change password", "Change email" };
        run("Account Settings", items, "Back");
    }

    // Runs the menu item with the given number.
    @Override
    protected void runItem(int choice) {
        switch (choice) {
            case 1: changePassword(); break;
            case 2: changeEmail(); break;
        }
    }

    // Asks for the current and new passwords, then changes the password.
    private void changePassword() {
        String current = console.readRequired("Current password");
        String next = console.readRequired("New password (min 4 chars)");
        String confirm = console.readRequired("Confirm new password");
        controller.changePassword(current, next, confirm);
        console.println("  Password changed.");
    }

    // Shows the current email, asks for the password and a new email, then changes the email.
    private void changeEmail() {
        console.println("  Current email: " + controller.email());
        String password = console.readRequired("Password");
        String email = console.readRequired("New email");
        controller.changeEmail(password, email);
        console.println("  Email changed.");
    }
}
