package iadms4.view;

import java.util.List;

import iadms4.controller.CompanyRepController;
import iadms4.model.Application;
import iadms4.model.ApplicationStatus;
import iadms4.model.Intern;
import iadms4.model.InternStatus;
import iadms4.model.Opening;
import iadms4.model.Position;

/**
 * VIEW. The Company Representative dashboard. Each private method is one menu action ("drill"):
 * ask for input, call CompanyRepController, print the result.
 */
public class CompanyRepDashboardView {
    private final Console console;
    private final CompanyRepController controller;
    private final AccountSettingsView settings;

    public CompanyRepDashboardView(Console console, CompanyRepController controller, AccountSettingsView settings) {
        this.console = console;
        this.controller = controller;
        this.settings = settings;
    }

    public void show(String repName) {
        new Menu(console, "Company Representative - " + repName + " (" + controller.company().getName() + ")",
                "Log out")
                .add("View positions", this::viewPositions)
                .add("Add position", this::addPosition)
                .add("Edit a position", this::editPosition)
                .add("Delete a position", this::deletePosition)
                .add("View openings", this::viewOpenings)
                .add("Create opening for a position", this::createOpening)
                .add("Open / close an opening", this::toggleOpening)
                .add("Delete an opening", this::deleteOpening)
                .add("View applications", this::viewApplications)
                .add("Filter applications by status", this::filterApplications)
                .add("Approve an application", this::approve)
                .add("Deny an application", this::deny)
                .add("Monitor interns", this::monitorInterns)
                .add("Filter interns by status", this::filterInterns)
                .add("Change intern status (incl. mark COMPLETED)", this::changeInternStatus)
                .add("View intern daily logs", this::viewDailyLogs)
                .add("Evaluate a completed intern", this::evaluate)
                .add("View evaluations", this::viewEvaluations)
                .add("Company summary report", this::report)
                .add("Account settings", settings::show)
                .run();
    }

    private void viewPositions() {
        List<String[]> rows = controller.positions().stream().map(p -> new String[] {
                String.valueOf(p.getId()), p.getTitle(), String.valueOf(p.getRequiredHours()) }).toList();
        console.table(new String[] { "ID", "Title", "Required hours" }, rows);
    }

    private void addPosition() {
        String title = console.readRequired("Position title");
        int hours = console.readInt("Required hours", 1, 10000);
        controller.addPosition(title, hours);
        console.println("  Position added.");
    }

    private void editPosition() {
        Position position = console.choose("Position to edit", controller.positions(), Formatters::position);
        if (position == null) {
            return;
        }
        String title = console.readLine("New title (blank = keep \"" + position.getTitle() + "\")");
        String hours = console.readLine("New required hours (blank = keep " + position.getRequiredHours() + ")");
        controller.editPosition(position, title.isEmpty() ? position.getTitle() : title,
                hours.isEmpty() ? position.getRequiredHours() : parseHours(hours));
        console.println("  Position updated.");
    }

    private int parseHours(String raw) {
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Required hours must be a number.");
        }
    }

    private void deletePosition() {
        Position position = console.choose("Position to delete", controller.positions(), Formatters::position);
        if (position == null) {
            return;
        }
        controller.deletePosition(position);
        console.println("  Position deleted.");
    }

    private void deleteOpening() {
        Opening opening = console.choose("Opening to delete", controller.openings(), Formatters::opening);
        if (opening == null) {
            return;
        }
        controller.deleteOpening(opening);
        console.println("  Opening deleted.");
    }

    private void filterApplications() {
        ApplicationStatus status = console.chooseFilter("Status", ApplicationStatus.values());
        List<String[]> rows = controller.applications(status).stream().map(a -> new String[] {
                String.valueOf(a.getId()), a.getStudent().getName(), a.getStudent().getSection(),
                a.getOpening().getPosition().getTitle(), a.getCoordinator().getName(), a.getStatus().toString() })
                .toList();
        console.table(new String[] { "ID", "Student", "Section", "Position", "Endorsed by", "Status" }, rows);
    }

    private void filterInterns() {
        InternStatus status = console.chooseFilter("Status", InternStatus.values());
        List<String[]> rows = controller.interns(status).stream().map(i -> new String[] {
                String.valueOf(i.getId()), i.getStudentId(), i.getName(), i.getPosition().getTitle(),
                i.getRecordedHours() + "/" + i.getPosition().getRequiredHours(), i.getStatus().toString() })
                .toList();
        console.table(new String[] { "ID", "Student No.", "Name", "Position", "Hours", "Status" }, rows);
    }

    private void report() {
        Reports.print(console, "Summary for " + controller.company().getName(), controller.report(), false);
    }

    private void viewOpenings() {
        List<String[]> rows = controller.openings().stream().map(o -> new String[] {
                String.valueOf(o.getId()), o.getPosition().getTitle(),
                String.valueOf(o.getPosition().getRequiredHours()), o.getStatus().toString() }).toList();
        console.table(new String[] { "ID", "Position", "Hours", "Status" }, rows);
    }

    private void createOpening() {
        Position position = console.choose("Position", controller.positions(), p -> p.getTitle());
        if (position == null) {
            return;
        }
        controller.createOpening(position);
        console.println("  Opening created.");
    }

    private void toggleOpening() {
        Opening opening = console.choose("Opening", controller.openings(), Formatters::opening);
        if (opening == null) {
            return;
        }
        controller.toggleOpening(opening);
        console.println("  Opening is now " + opening.getStatus() + ".");
    }

    private void viewApplications() {
        List<String[]> rows = controller.applications().stream().map(a -> new String[] {
                String.valueOf(a.getId()), a.getStudent().getName(), a.getStudent().getSection(),
                a.getOpening().getPosition().getTitle(), a.getCoordinator().getName(), a.getStatus().toString() })
                .toList();
        console.table(new String[] { "ID", "Student", "Section", "Position", "Endorsed by", "Status" }, rows);
    }

    private void approve() {
        Application application = console.choose("Pending application", controller.pendingApplications(),
                Formatters::application);
        if (application == null) {
            return;
        }
        Intern intern = controller.approve(application);
        console.println("  Approved. " + intern.getName() + " is now an intern for "
                + intern.getPosition().getTitle() + ".");
    }

    private void deny() {
        Application application = console.choose("Pending application", controller.pendingApplications(),
                Formatters::application);
        if (application == null) {
            return;
        }
        controller.deny(application);
        console.println("  Application denied.");
    }

    private void monitorInterns() {
        List<String[]> rows = controller.interns().stream().map(i -> new String[] {
                String.valueOf(i.getId()), i.getStudentId(), i.getName(), i.getPosition().getTitle(),
                i.getRecordedHours() + "/" + i.getPosition().getRequiredHours(), i.getStatus().toString() })
                .toList();
        console.table(new String[] { "ID", "Student No.", "Name", "Position", "Hours", "Status" }, rows);
    }

    private void viewDailyLogs() {
        Intern intern = console.choose("Intern", controller.interns(), Formatters::intern);
        if (intern == null) {
            return;
        }
        List<String[]> rows = controller.dailyLogs(intern).stream().map(l -> new String[] {
                String.valueOf(l.getId()), l.getDate().toString(), String.valueOf(l.getHours()),
                l.getDescription() }).toList();
        console.println("  Daily logs of " + intern.getName());
        console.table(new String[] { "ID", "Date", "Hours", "Description" }, rows);
    }

    private void evaluate() {
        Intern intern = console.choose("Completed intern awaiting evaluation", controller.internsAwaitingEvaluation(),
                Formatters::intern);
        if (intern == null) {
            return;
        }
        int rating = console.readInt("Rating (1-5)", 1, 5);
        String comments = console.readRequired("Comments");
        controller.evaluate(intern, rating, comments);
        console.println("  Evaluation saved for " + intern.getName() + ".");
    }

    private void viewEvaluations() {
        List<String[]> rows = controller.evaluations().stream().map(e -> new String[] {
                String.valueOf(e.getId()), e.getIntern().getName(), e.getIntern().getPosition().getTitle(),
                e.getRating() + "/5", e.getEvaluator().getName(), e.getDate().toString(), e.getComments() })
                .toList();
        console.table(new String[] { "ID", "Intern", "Position", "Rating", "Evaluator", "Date", "Comments" }, rows);
    }

    private void changeInternStatus() {
        Intern intern = console.choose("Intern", controller.interns(), Formatters::intern);
        if (intern == null) {
            return;
        }
        InternStatus status = console.choose("New status", List.of(InternStatus.values()), InternStatus::toString);
        if (status == null) {
            return;
        }
        controller.changeInternStatus(intern, status);
        console.println("  Intern status set to " + status + ".");
    }
}
