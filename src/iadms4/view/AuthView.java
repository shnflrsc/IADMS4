package iadms4.view;

import java.time.LocalDate;

import iadms4.controller.AuthController;
import iadms4.model.Gender;

/*
 * VIEW. stuff before login: first time setup and login screen
 */
public class AuthView {
    private final Console console;
    private final AuthController controller;

    // makes the screen, takes console, controller
    public AuthView(Console console, AuthController controller) {
        this.console = console;
        this.controller = controller;
    }

    // loops until first coordinator is made
    public void setup() {
        console.header("IADMS - First Time Setup");
        console.println("  No accounts exist yet. Create the first Coordinator account.");
        while (controller.needsSetup()) {
            String name = console.readRequired("Full name");
            Gender gender = console.readGender();
            LocalDate birthdate = console.readDate("Birthdate");
            String employeeId = console.readRequired("Employee ID");
            String email = console.readRequired("Email (used to log in)");
            String password = console.readRequired("Password (min 4 chars)");
            try {
                controller.setup(name, gender, birthdate, employeeId, email, password);
                console.println("  Coordinator account created. You are now logged in.");
            } catch (IllegalArgumentException e) {
                console.println("  ! " + e.getMessage());
            }
        }
    }

    // login screen. false means user picked exit
    public boolean login() {
        console.header("IADMS - Internship Application & Deployment Monitoring");
        console.println("  [1] Log in");
        console.println("  [0] Exit");
        if (console.readInt("Select", 0, 1) == 0) {
            return false;
        }
        String email = console.readLine("Email");
        String password = console.readLine("Password");
        try {
            controller.login(email, password);
        } catch (IllegalArgumentException e) {
            console.println("  ! " + e.getMessage());
            console.pause();
        }
        return true;
    }
}
