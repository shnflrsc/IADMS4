package iadms4.view;

import java.util.List;

import iadms4.controller.StudentController;
import iadms4.model.Student;

/**
 * VIEW. The Student dashboard (read-only).
 */
public class StudentDashboardView {
    private final Console console;
    private final StudentController controller;
    private final AccountSettingsView settings;

    public StudentDashboardView(Console console, StudentController controller, AccountSettingsView settings) {
        this.console = console;
        this.controller = controller;
        this.settings = settings;
    }

    public void show() {
        Student me = controller.me();
        new Menu(console, "Student Dashboard - " + me.getName(), "Log out")
                .add("View my profile", this::profile)
                .add("View my applications", this::applications)
                .add("Account settings", settings::show)
                .run();
    }

    private void profile() {
        Student me = controller.me();
        console.println("  Name:        " + me.getName());
        console.println("  Student No.: " + me.getStudentId());
        console.println("  Section:     " + me.getSection());
        console.println("  Email:       " + controller.email());
        console.println("  Birthdate:   " + me.getBirthdate());
    }

    private void applications() {
        List<String[]> rows = controller.myApplications().stream().map(a -> new String[] {
                String.valueOf(a.getId()), a.getOpening().getPosition().getCompany().getName(),
                a.getOpening().getPosition().getTitle(), a.getStatus().toString() }).toList();
        console.table(new String[] { "ID", "Company", "Position", "Status" }, rows);
    }
}
