package iadms4.view;

import java.util.ArrayList;
import java.util.List;

import iadms4.controller.InternController;
import iadms4.model.DailyLog;
import iadms4.model.Evaluation;
import iadms4.model.Intern;

/*
 * VIEW. intern dashboard
 */
public class InternDashboardView extends Menu {
    private final InternController controller;
    private final AccountSettingsView settings;

    // makes the screen, takes console, controller, settings
    public InternDashboardView(Console console, InternController controller, AccountSettingsView settings) {
        super(console);
        this.controller = controller;
        this.settings = settings;
    }

    // intern menu, stays here til logout
    public void show() {
        String[] items = { "View deployment details", "Log hours", "View my daily logs", "View my evaluation",
                "Account settings" };
        run("Intern Dashboard - " + controller.me().getName(), items, "Log out");
    }

    // runs whatever the intern picked, numbers match the items array
    @Override
    protected void runItem(int choice) {
        switch (choice) {
            case 1: deployment(); break;
            case 2: logHours(); break;
            case 3: viewLogs(); break;
            case 4: viewEvaluation(); break;
            case 5: settings.show(); break;
        }
    }

    // prints the intern's company, position, hours left and status
    private void deployment() {
        Intern me = controller.me();
        int required = me.getPosition().getRequiredHours();
        console.println("  Company:   " + me.getPosition().getCompany().getName());
        console.println("  Position:  " + me.getPosition().getTitle());
        console.println("  Hours:     " + me.getRecordedHours() + " / " + required);
        console.println("  Remaining: " + (required - me.getRecordedHours()));
        console.println("  Status:    " + me.getStatus());
    }

    // asks for hours and what they did, then shows hours left
    private void logHours() {
        int hours = console.readInt("Hours to log", 1, 24);
        String description = console.readRequired("Daily log (what did you do today?)");
        int remaining = controller.logHours(hours, description);
        if (remaining == 0) {
            console.println("  Logged. Required hours reached - your company representative will mark you COMPLETED.");
        } else {
            console.println("  Logged. " + remaining + " hour(s) remaining.");
        }
    }

    // logs table. no edit or delete on logs, by design
    private void viewLogs() {
        List<String[]> rows = new ArrayList<>();
        for (DailyLog log : controller.myLogs()) {
            rows.add(new String[] { String.valueOf(log.getId()), log.getDate().toString(),
                    String.valueOf(log.getHours()), log.getDescription() });
        }
        console.table(new String[] { "ID", "Date", "Hours", "Description" }, rows);
    }

    // prints the evaluation, or a message if theres none yet
    private void viewEvaluation() {
        Evaluation evaluation = controller.myEvaluation();
        if (evaluation == null) {
            console.println("  No evaluation yet. It appears here after your internship is COMPLETED and evaluated.");
            return;
        }
        console.println("  Rating:      " + evaluation.getRating() + "/5");
        console.println("  Comments:    " + evaluation.getComments());
        console.println("  Evaluated by " + evaluation.getEvaluator().getName() + " on " + evaluation.getDate());
    }
}
