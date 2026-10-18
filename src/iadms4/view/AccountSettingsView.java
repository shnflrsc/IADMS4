package iadms4.view;

import iadms4.controller.AccountController;

/**
 * VIEW. Account settings shared by every dashboard: change your own password or login email.
 */
public class AccountSettingsView {
    private final Console console;
    private final AccountController controller;

    public AccountSettingsView(Console console, AccountController controller) {
        this.console = console;
        this.controller = controller;
    }

    public void show() {
        new Menu(console, "Account Settings", "Back")
                .add("Change password", this::changePassword)
                .add("Change email", this::changeEmail)
                .run();
    }

    private void changePassword() {
        String current = console.readRequired("Current password");
        String next = console.readRequired("New password (min 4 chars)");
        String confirm = console.readRequired("Confirm new password");
        controller.changePassword(current, next, confirm);
        console.println("  Password changed.");
    }

    private void changeEmail() {
        console.println("  Current email: " + controller.email());
        String password = console.readRequired("Password");
        String email = console.readRequired("New email");
        controller.changeEmail(password, email);
        console.println("  Email changed.");
    }
}
