package iadms4.view;

import java.util.ArrayList;
import java.util.List;

import iadms4.controller.CompanyRepController;
import iadms4.model.Application;
import iadms4.model.DailyLog;
import iadms4.model.Evaluation;
import iadms4.model.Intern;
import iadms4.model.InternStatus;
import iadms4.model.Opening;
import iadms4.model.Position;

/*
 * VIEW. company rep dashboard
 * each private method = 1 menu item. ask input, call controller, print
 * dont call services from here
 */
public class CompanyRepDashboardView extends Menu {
    private final CompanyRepController controller;
    private final AccountSettingsView settings;

    // makes the screen, takes console, controller, settings
    public CompanyRepDashboardView(Console console, CompanyRepController controller, AccountSettingsView settings) {
        super(console);
        this.controller = controller;
        this.settings = settings;
    }

    // rep menu, stays here til logout
    public void show(String repName) {
        String[] items = { "View positions", "Add position", "Edit a position", "Delete a position", "View openings",
                "Create opening for a position", "Open / close an opening", "Delete an opening", "View applications",
                "Filter applications by status", "Approve an application", "Deny an application", "Monitor interns",
                "Filter interns by status", "Change intern status (incl. mark COMPLETED)", "View intern daily logs",
                "Evaluate a completed intern", "View evaluations", "Company summary report", "Account settings" };
        String title = "Company Representative - " + repName + " (" + controller.company().getName() + ")";
        run(title, items, "Log out");
    }

    // what each number does
    @Override
    protected void runItem(int choice) {
        switch (choice) {
            case 1: viewPositions(); break;
            case 2: addPosition(); break;
            case 3: editPosition(); break;
            case 4: deletePosition(); break;
            case 5: viewOpenings(); break;
            case 6: createOpening(); break;
            case 7: toggleOpening(); break;
            case 8: deleteOpening(); break;
            case 9: printApplications(controller.applications()); break;
            case 10: printApplications(controller.applications(console.chooseApplicationStatus())); break;
            case 11: approve(); break;
            case 12: deny(); break;
            case 13: printInterns(controller.interns()); break;
            case 14: printInterns(controller.interns(console.chooseInternStatus())); break;
            case 15: changeInternStatus(); break;
            case 16: viewDailyLogs(); break;
            case 17: evaluate(); break;
            case 18: viewEvaluations(); break;
            case 19: report(); break;
            case 20: settings.show(); break;
        }
    }

    // positions table
    private void viewPositions() {
        List<String[]> rows = new ArrayList<>();
        for (Position p : controller.positions()) {
            rows.add(new String[] { String.valueOf(p.getId()), p.getTitle(), String.valueOf(p.getRequiredHours()) });
        }
        console.table(new String[] { "ID", "Title", "Required hours" }, rows);
    }

    // add position screen
    private void addPosition() {
        String title = console.readRequired("Position title");
        int hours = console.readInt("Required hours", 1, 10000);
        controller.addPosition(title, hours);
        console.println("  Position added.");
    }

    // edit position screen. blank = keep old value
    private void editPosition() {
        List<Position> positions = controller.positions();
        int index = console.choose("Position to edit", Formatters.positions(positions));
        if (index == -1) {
            return;
        }
        Position position = positions.get(index);
        String title = console.readLine("New title (blank = keep \"" + position.getTitle() + "\")");
        String rawHours = console.readLine("New required hours (blank = keep " + position.getRequiredHours() + ")");
        if (title.isEmpty()) {
            title = position.getTitle();
        }
        int hours = position.getRequiredHours();
        if (!rawHours.isEmpty()) {
            hours = parseHours(rawHours);
        }
        controller.editPosition(position, title, hours);
        console.println("  Position updated.");
    }

    // text to int, error if its not a number
    private int parseHours(String raw) {
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Required hours must be a number.");
        }
    }

    // delete position screen
    private void deletePosition() {
        List<Position> positions = controller.positions();
        int index = console.choose("Position to delete", Formatters.positions(positions));
        if (index == -1) {
            return;
        }
        controller.deletePosition(positions.get(index));
        console.println("  Position deleted.");
    }

    // openings table
    private void viewOpenings() {
        List<String[]> rows = new ArrayList<>();
        for (Opening o : controller.openings()) {
            rows.add(new String[] { String.valueOf(o.getId()), o.getPosition().getTitle(),
                    String.valueOf(o.getPosition().getRequiredHours()), o.getStatus().toString() });
        }
        console.table(new String[] { "ID", "Position", "Hours", "Status" }, rows);
    }

    // create opening screen
    private void createOpening() {
        List<Position> positions = controller.positions();
        int index = console.choose("Position", Formatters.positionTitles(positions));
        if (index == -1) {
            return;
        }
        controller.createOpening(positions.get(index));
        console.println("  Opening created.");
    }

    // open/close screen
    private void toggleOpening() {
        List<Opening> openings = controller.openings();
        int index = console.choose("Opening", Formatters.openings(openings));
        if (index == -1) {
            return;
        }
        Opening opening = openings.get(index);
        controller.toggleOpening(opening);
        console.println("  Opening is now " + opening.getStatus() + ".");
    }

    // delete opening screen
    private void deleteOpening() {
        List<Opening> openings = controller.openings();
        int index = console.choose("Opening to delete", Formatters.openings(openings));
        if (index == -1) {
            return;
        }
        controller.deleteOpening(openings.get(index));
        console.println("  Opening deleted.");
    }

    // applications table
    private void printApplications(List<Application> applications) {
        List<String[]> rows = new ArrayList<>();
        for (Application a : applications) {
            rows.add(new String[] { String.valueOf(a.getId()), a.getStudent().getName(), a.getStudent().getSection(),
                    a.getOpening().getPosition().getTitle(), a.getCoordinator().getName(), a.getStatus().toString() });
        }
        console.table(new String[] { "ID", "Student", "Section", "Position", "Endorsed by", "Status" }, rows);
    }

    // approve screen
    private void approve() {
        List<Application> pending = controller.pendingApplications();
        int index = console.choose("Pending application", Formatters.applications(pending));
        if (index == -1) {
            return;
        }
        Intern intern = controller.approve(pending.get(index));
        console.println("  Approved. " + intern.getName() + " is now an intern for "
                + intern.getPosition().getTitle() + ". The opening is now closed.");
    }

    // deny screen
    private void deny() {
        List<Application> pending = controller.pendingApplications();
        int index = console.choose("Pending application", Formatters.applications(pending));
        if (index == -1) {
            return;
        }
        controller.deny(pending.get(index));
        console.println("  Application denied.");
    }

    // interns table
    private void printInterns(List<Intern> interns) {
        List<String[]> rows = new ArrayList<>();
        for (Intern i : interns) {
            rows.add(new String[] { String.valueOf(i.getId()), i.getStudentId(), i.getName(), i.getPosition().getTitle(),
                    i.getRecordedHours() + "/" + i.getPosition().getRequiredHours(), i.getStatus().toString() });
        }
        console.table(new String[] { "ID", "Student No.", "Name", "Position", "Hours", "Status" }, rows);
    }

    // change intern status screen
    private void changeInternStatus() {
        List<Intern> interns = controller.interns();
        int index = console.choose("Intern", Formatters.interns(interns));
        if (index == -1) {
            return;
        }
        int statusIndex = console.choose("New status", Formatters.internStatuses());
        if (statusIndex == -1) {
            return;
        }
        InternStatus status = InternStatus.values()[statusIndex];
        controller.changeInternStatus(interns.get(index), status);
        console.println("  Intern status set to " + status + ".");
    }

    // pick intern, show logs
    private void viewDailyLogs() {
        List<Intern> interns = controller.interns();
        int index = console.choose("Intern", Formatters.interns(interns));
        if (index == -1) {
            return;
        }
        Intern intern = interns.get(index);
        List<String[]> rows = new ArrayList<>();
        for (DailyLog log : controller.dailyLogs(intern)) {
            rows.add(new String[] { String.valueOf(log.getId()), log.getDate().toString(),
                    String.valueOf(log.getHours()), log.getDescription() });
        }
        console.println("  Daily logs of " + intern.getName());
        console.table(new String[] { "ID", "Date", "Hours", "Description" }, rows);
    }

    // evaluate screen
    private void evaluate() {
        List<Intern> interns = controller.internsAwaitingEvaluation();
        int index = console.choose("Completed intern awaiting evaluation", Formatters.interns(interns));
        if (index == -1) {
            return;
        }
        Intern intern = interns.get(index);
        int rating = console.readInt("Rating (1-5)", 1, 5);
        String comments = console.readRequired("Comments");
        controller.evaluate(intern, rating, comments);
        console.println("  Evaluation saved for " + intern.getName() + ".");
    }

    // evaluations table
    private void viewEvaluations() {
        List<String[]> rows = new ArrayList<>();
        for (Evaluation e : controller.evaluations()) {
            rows.add(new String[] { String.valueOf(e.getId()), e.getIntern().getName(),
                    e.getIntern().getPosition().getTitle(), e.getRating() + "/5", e.getEvaluator().getName(),
                    e.getDate().toString(), e.getComments() });
        }
        console.table(new String[] { "ID", "Intern", "Position", "Rating", "Evaluator", "Date", "Comments" }, rows);
    }

    // company report
    private void report() {
        Reports.print(console, "Summary for " + controller.company().getName(), controller.report(), false);
    }
}
