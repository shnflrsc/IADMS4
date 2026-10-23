package iadms4.view;

import java.util.List;

import iadms4.controller.InternController;
import iadms4.model.Intern;

/*
 * VIEW. The Intern dashboard.
 *
 * Responsibilities: builds the intern's menu so the intern can view deployment details, log hours,
 * and read their own daily logs and evaluation. Input is passed to InternController.
 */
public class InternDashboardView {
    // Used for printing and reading.
    private final Console console;
    // Controller that handles the intern's requests.
    private final InternController controller;
    // Screen reused for the "Account settings" menu item.
    private final AccountSettingsView settings;

    /*
     * Creates the view.
     *
     * It takes console (the console used for input and output), controller (the intern controller) and
     * settings (the shared account settings screen).
     */
    public InternDashboardView(Console console, InternController controller, AccountSettingsView settings) {
        this.console = console;
        this.controller = controller;
        this.settings = settings;
    }

    // Shows the intern menu until the intern logs out.
    public void show() {
        new Menu(console, "Intern Dashboard - " + controller.me().getName(), "Log out")
                .add("View deployment details", this::deployment)
                .add("Log hours", this::logHours)
                .add("View my daily logs", this::viewLogs)
                .add("View my evaluation", this::viewEvaluation)
                .add("Account settings", settings::show)
                .run();
    }

    // Prints the company, position, hours progress and status of the intern.
    private void deployment() {
        Intern me = controller.me();
        int required = me.getPosition().getRequiredHours();
        console.println("  Company:   " + me.getPosition().getCompany().getName());
        console.println("  Position:  " + me.getPosition().getTitle());
        console.println("  Hours:     " + me.getRecordedHours() + " / " + required);
        console.println("  Remaining: " + (required - me.getRecordedHours()));
        console.println("  Status:    " + me.getStatus());
    }

    // Asks for the hours and a description, logs them, and reports how many hours remain.
    private void logHours() {
        int hours = console.readInt("Hours to log", 1, 24);
        String description = console.readRequired("Daily log (what did you do today?)");
        int remaining = controller.logHours(hours, description);
        console.println(remaining == 0
                ? "  Logged. Required hours reached - your company representative will mark you COMPLETED."
                : "  Logged. " + remaining + " hour(s) remaining.");
    }

    // Prints a table of the intern's daily logs.
    // Read-only on purpose: daily logs can never be edited or deleted once saved.
    private void viewLogs() {
        List<String[]> rows = controller.myLogs().stream().map(l -> new String[] {
                String.valueOf(l.getId()), l.getDate().toString(), String.valueOf(l.getHours()),
                l.getDescription() }).toList();
        console.table(new String[] { "ID", "Date", "Hours", "Description" }, rows);
    }

    // Prints the intern's evaluation, or a message explaining that there is none yet.
    private void viewEvaluation() {
        controller.myEvaluation().ifPresentOrElse(e -> {
            console.println("  Rating:      " + e.getRating() + "/5");
            console.println("  Comments:    " + e.getComments());
            console.println("  Evaluated by " + e.getEvaluator().getName() + " on " + e.getDate());
        }, () -> console.println("  No evaluation yet. It appears here after your internship is COMPLETED and evaluated."));
    }
}
