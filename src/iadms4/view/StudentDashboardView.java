package iadms4.view;

import java.util.List;

import iadms4.controller.StudentController;
import iadms4.model.Student;

/*
 * VIEW. The Student dashboard (read-only).
 *
 * Responsibilities: builds the student's menu and prints the student's profile and applications.
 * A student can only look at information; every change is done by a coordinator.
 */
public class StudentDashboardView {
    // Used for printing and reading.
    private final Console console;
    // Controller that provides the student's data.
    private final StudentController controller;
    // Screen reused for the "Account settings" menu item.
    private final AccountSettingsView settings;

    /*
     * Creates the view.
     *
     * It takes console (the console used for input and output), controller (the student controller) and
     * settings (the shared account settings screen).
     */
    public StudentDashboardView(Console console, StudentController controller, AccountSettingsView settings) {
        this.console = console;
        this.controller = controller;
        this.settings = settings;
    }

    // Shows the student menu until the student logs out.
    public void show() {
        Student me = controller.me();
        new Menu(console, "Student Dashboard - " + me.getName(), "Log out")
                .add("View my profile", this::profile)
                .add("View my applications", this::applications)
                .add("Account settings", settings::show)
                .run();
    }

    // Prints the student's name, student number, section, email and birthdate.
    private void profile() {
        Student me = controller.me();
        console.println("  Name:        " + me.getName());
        console.println("  Student No.: " + me.getStudentId());
        console.println("  Section:     " + me.getSection());
        console.println("  Email:       " + controller.email());
        console.println("  Birthdate:   " + me.getBirthdate());
    }

    // Prints a table of the student's applications with company, position and status.
    private void applications() {
        List<String[]> rows = controller.myApplications().stream().map(a -> new String[] {
                String.valueOf(a.getId()), a.getOpening().getPosition().getCompany().getName(),
                a.getOpening().getPosition().getTitle(), a.getStatus().toString() }).toList();
        console.table(new String[] { "ID", "Company", "Position", "Status" }, rows);
    }
}
