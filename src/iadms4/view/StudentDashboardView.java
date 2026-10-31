package iadms4.view;

import java.util.ArrayList;
import java.util.List;

import iadms4.controller.StudentController;
import iadms4.model.Application;
import iadms4.model.Student;

/*
 * VIEW. student dashboard, read only
 * coordinators do all the changes
 */
public class StudentDashboardView extends Menu {
    private final StudentController controller;
    private final AccountSettingsView settings;

    // makes the screen, takes console, controller, settings
    public StudentDashboardView(Console console, StudentController controller, AccountSettingsView settings) {
        super(console);
        this.controller = controller;
        this.settings = settings;
    }

    // student menu, stays here until logout
    public void show() {
        String[] items = { "View my profile", "View my applications", "Account settings" };
        run("Student Dashboard - " + controller.me().getName(), items, "Log out");
    }

    // runs whatever the student picked, numbers match the items array
    @Override
    protected void runItem(int choice) {
        switch (choice) {
            case 1: profile(); break;
            case 2: applications(); break;
            case 3: settings.show(); break;
        }
    }

    // prints the student's name, number, section, email and birthdate
    private void profile() {
        Student me = controller.me();
        console.println("  Name:        " + me.getName());
        console.println("  Student No.: " + me.getStudentId());
        console.println("  Section:     " + me.getSection());
        console.println("  Email:       " + controller.email());
        console.println("  Birthdate:   " + me.getBirthdate());
    }

    // prints a table of the student's own applications
    private void applications() {
        List<String[]> rows = new ArrayList<>();
        for (Application a : controller.myApplications()) {
            rows.add(new String[] { String.valueOf(a.getId()), a.getOpening().getPosition().getCompany().getName(),
                    a.getOpening().getPosition().getTitle(), a.getStatus().toString() });
        }
        console.table(new String[] { "ID", "Company", "Position", "Status" }, rows);
    }
}
