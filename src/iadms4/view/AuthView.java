package iadms4.view;

import java.time.LocalDate;

import iadms4.controller.AuthController;
import iadms4.model.Gender;

/*
 * VIEW. The screens shown before anyone is logged in.
 *
 * Responsibilities: the first-time setup screen (creates the first Coordinator) and the login screen.
 * It reads what the user types and passes it to AuthController.
 */
public class AuthView {
    // Used for printing and reading.
    private final Console console;
    // Controller that handles setup and login requests.
    private final AuthController controller;

    /*
     * Creates the view.
     *
     * It takes console (the console used for input and output) and controller (the authentication
     * controller).
     */
    public AuthView(Console console, AuthController controller) {
        this.console = console;
        this.controller = controller;
    }

    // Repeats until the first coordinator account has been created.
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

    /*
     * Shows the login screen and tries to log the user in.
     *
     * It returns false when the user chooses to exit the application, otherwise true.
     */
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
