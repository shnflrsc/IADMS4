package iadms4.view;

import java.util.List;

import iadms4.controller.InternController;
import iadms4.model.Intern;

/**
 * VIEW. The Intern dashboard: view deployment details, log hours, and read your own daily logs and evaluation.
 */
public class InternDashboardView {
    private final Console console;
    private final InternController controller;
    private final AccountSettingsView settings;

    public InternDashboardView(Console console, InternController controller, AccountSettingsView settings) {
        this.console = console;
        this.controller = controller;
        this.settings = settings;
    }

    public void show() {
        new Menu(console, "Intern Dashboard - " + controller.me().getName(), "Log out")
                .add("View deployment details", this::deployment)
                .add("Log hours", this::logHours)
                .add("View my daily logs", this::viewLogs)
                .add("View my evaluation", this::viewEvaluation)
                .add("Account settings", settings::show)
                .run();
    }

    private void deployment() {
        Intern me = controller.me();
        int required = me.getPosition().getRequiredHours();
        console.println("  Company:   " + me.getPosition().getCompany().getName());
        console.println("  Position:  " + me.getPosition().getTitle());
        console.println("  Hours:     " + me.getRecordedHours() + " / " + required);
        console.println("  Remaining: " + (required - me.getRecordedHours()));
        console.println("  Status:    " + me.getStatus());
    }

    private void logHours() {
        int hours = console.readInt("Hours to log", 1, 24);
        String description = console.readRequired("Daily log (what did you do today?)");
        int remaining = controller.logHours(hours, description);
        console.println(remaining == 0
                ? "  Logged. Required hours reached - your company representative will mark you COMPLETED."
                : "  Logged. " + remaining + " hour(s) remaining.");
    }

    // Read-only on purpose: daily logs can never be edited or deleted once saved.
    private void viewLogs() {
        List<String[]> rows = controller.myLogs().stream().map(l -> new String[] {
                String.valueOf(l.getId()), l.getDate().toString(), String.valueOf(l.getHours()),
                l.getDescription() }).toList();
        console.table(new String[] { "ID", "Date", "Hours", "Description" }, rows);
    }

    private void viewEvaluation() {
        controller.myEvaluation().ifPresentOrElse(e -> {
            console.println("  Rating:      " + e.getRating() + "/5");
            console.println("  Comments:    " + e.getComments());
            console.println("  Evaluated by " + e.getEvaluator().getName() + " on " + e.getDate());
        }, () -> console.println("  No evaluation yet. It appears here after your internship is COMPLETED and evaluated."));
    }
}
