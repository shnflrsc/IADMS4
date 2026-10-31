package iadms4.view;

import iadms4.controller.AccountController;

/*
 * VIEW. account settings, all dashboards share this
 * change password or email
 */
public class AccountSettingsView extends Menu {
    private final AccountController controller;

    // constructor
    public AccountSettingsView(Console console, AccountController controller) {
        super(console);
        this.controller = controller;
    }

    // settings menu
    public void show() {
        String[] items = { "Change password", "Change email" };
        run("Account Settings", items, "Back");
    }

    // what each number does
    @Override
    protected void runItem(int choice) {
        switch (choice) {
            case 1: changePassword(); break;
            case 2: changeEmail(); break;
        }
    }

    // change password screen
    private void changePassword() {
        String current = console.readRequired("Current password");
        String next = console.readRequired("New password (min 4 chars)");
        String confirm = console.readRequired("Confirm new password");
        controller.changePassword(current, next, confirm);
        console.println("  Password changed.");
    }

    // change email screen
    private void changeEmail() {
        console.println("  Current email: " + controller.email());
        String password = console.readRequired("Password");
        String email = console.readRequired("New email");
        controller.changeEmail(password, email);
        console.println("  Email changed.");
    }
}
