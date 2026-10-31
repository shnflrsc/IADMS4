package iadms4.view;

import iadms4.controller.AccountController;

/*
 * VIEW. account settings, all dashboards share this
 * change password or email
 */
public class AccountSettingsView extends Menu {
    private final AccountController controller;

    // makes the screen, takes console, controller
    public AccountSettingsView(Console console, AccountController controller) {
        super(console);
        this.controller = controller;
    }

    // shows the settings menu, back goes to the dashboard
    public void show() {
        String[] items = { "Change password", "Change email" };
        run("Account Settings", items, "Back");
    }

    // runs whatever the user picked in the settings menu
    @Override
    protected void runItem(int choice) {
        switch (choice) {
            case 1: changePassword(); break;
            case 2: changeEmail(); break;
        }
    }

    // asks for current and new password then changes it
    private void changePassword() {
        String current = console.readRequired("Current password");
        String next = console.readRequired("New password (min 4 chars)");
        String confirm = console.readRequired("Confirm new password");
        controller.changePassword(current, next, confirm);
        console.println("  Password changed.");
    }

    // shows current email, asks password and new email, then changes it
    private void changeEmail() {
        console.println("  Current email: " + controller.email());
        String password = console.readRequired("Password");
        String email = console.readRequired("New email");
        controller.changeEmail(password, email);
        console.println("  Email changed.");
    }
}
