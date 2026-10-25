package iadms4.view;

import java.util.List;

import iadms4.controller.CompanyRepController;
import iadms4.model.Application;
import iadms4.model.ApplicationStatus;
import iadms4.model.Intern;
import iadms4.model.InternStatus;
import iadms4.model.Opening;
import iadms4.model.Position;

/*
 * VIEW. The Company Representative dashboard.
 *
 * Responsibilities: builds the representative's menu. Each private method is one menu action:
 * it asks for input, calls CompanyRepController, and prints the result. The view never talks to a
 * service or repository directly.
 */
public class CompanyRepDashboardView {
    // Used for printing and reading.
    private final Console console;
    // Controller that handles the representative's requests.
    private final CompanyRepController controller;
    // Screen reused for the "Account settings" menu item.
    private final AccountSettingsView settings;

    /*
     * Creates the view.
     *
     * It takes console (the console used for input and output), controller (the company representative
     * controller) and settings (the shared account settings screen).
     */
    public CompanyRepDashboardView(Console console, CompanyRepController controller, AccountSettingsView settings) {
        this.console = console;
        this.controller = controller;
        this.settings = settings;
    }

    /*
     * Shows the representative menu until the representative logs out.
     *
     * It takes repName (the name of the logged-in representative, shown in the menu title).
     */
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

    // Prints a table of the company's positions.
    private void viewPositions() {
        List<String[]> rows = controller.positions().stream().map(p -> new String[] {
                String.valueOf(p.getId()), p.getTitle(), String.valueOf(p.getRequiredHours()) }).toList();
        console.table(new String[] { "ID", "Title", "Required hours" }, rows);
    }

    // Asks for a title and required hours, then adds a new position.
    private void addPosition() {
        String title = console.readRequired("Position title");
        int hours = console.readInt("Required hours", 1, 10000);
        controller.addPosition(title, hours);
        console.println("  Position added.");
    }

    // Lets the user pick a position and change its title and hours. A blank answer keeps the old value.
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

    /*
     * Converts the typed hours into a number.
     *
     * It takes raw (the text typed by the user). It returns the number of hours. It throws
     * IllegalArgumentException if the text is not a whole number.
     */
    private int parseHours(String raw) {
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Required hours must be a number.");
        }
    }

    // Lets the user pick a position and deletes it.
    private void deletePosition() {
        Position position = console.choose("Position to delete", controller.positions(), Formatters::position);
        if (position == null) {
            return;
        }
        controller.deletePosition(position);
        console.println("  Position deleted.");
    }

    // Lets the user pick an opening and deletes it.
    private void deleteOpening() {
        Opening opening = console.choose("Opening to delete", controller.openings(), Formatters::opening);
        if (opening == null) {
            return;
        }
        controller.deleteOpening(opening);
        console.println("  Opening deleted.");
    }

    // Asks for a status (or All) and prints the company's applications that match it.
    private void filterApplications() {
        ApplicationStatus status = console.chooseFilter("Status", ApplicationStatus.values());
        List<String[]> rows = controller.applications(status).stream().map(a -> new String[] {
                String.valueOf(a.getId()), a.getStudent().getName(), a.getStudent().getSection(),
                a.getOpening().getPosition().getTitle(), a.getCoordinator().getName(), a.getStatus().toString() })
                .toList();
        console.table(new String[] { "ID", "Student", "Section", "Position", "Endorsed by", "Status" }, rows);
    }

    // Asks for a status (or All) and prints the company's interns that match it.
    private void filterInterns() {
        InternStatus status = console.chooseFilter("Status", InternStatus.values());
        List<String[]> rows = controller.interns(status).stream().map(i -> new String[] {
                String.valueOf(i.getId()), i.getStudentId(), i.getName(), i.getPosition().getTitle(),
                i.getRecordedHours() + "/" + i.getPosition().getRequiredHours(), i.getStatus().toString() })
                .toList();
        console.table(new String[] { "ID", "Student No.", "Name", "Position", "Hours", "Status" }, rows);
    }

    // Prints the summary report of the representative's company.
    private void report() {
        Reports.print(console, "Summary for " + controller.company().getName(), controller.report(), false);
    }

    // Prints a table of the company's openings.
    private void viewOpenings() {
        List<String[]> rows = controller.openings().stream().map(o -> new String[] {
                String.valueOf(o.getId()), o.getPosition().getTitle(),
                String.valueOf(o.getPosition().getRequiredHours()), o.getStatus().toString() }).toList();
        console.table(new String[] { "ID", "Position", "Hours", "Status" }, rows);
    }

    // Lets the user pick a position and publishes it as a new opening.
    private void createOpening() {
        Position position = console.choose("Position", controller.positions(), p -> p.getTitle());
        if (position == null) {
            return;
        }
        controller.createOpening(position);
        console.println("  Opening created.");
    }

    // Lets the user pick an opening and switches it between OPEN and CLOSED.
    private void toggleOpening() {
        Opening opening = console.choose("Opening", controller.openings(), Formatters::opening);
        if (opening == null) {
            return;
        }
        controller.toggleOpening(opening);
        console.println("  Opening is now " + opening.getStatus() + ".");
    }

    // Prints a table of all applications made to the company.
    private void viewApplications() {
        List<String[]> rows = controller.applications().stream().map(a -> new String[] {
                String.valueOf(a.getId()), a.getStudent().getName(), a.getStudent().getSection(),
                a.getOpening().getPosition().getTitle(), a.getCoordinator().getName(), a.getStatus().toString() })
                .toList();
        console.table(new String[] { "ID", "Student", "Section", "Position", "Endorsed by", "Status" }, rows);
    }

    // Lets the user pick a pending application and approves it, turning the student into an intern.
    private void approve() {
        Application application = console.choose("Pending application", controller.pendingApplications(),
                Formatters::application);
        if (application == null) {
            return;
        }
        Intern intern = controller.approve(application);
        console.println("  Approved. " + intern.getName() + " is now an intern for "
                + intern.getPosition().getTitle() + ". The opening is now closed.");
    }

    // Lets the user pick a pending application and denies it.
    private void deny() {
        Application application = console.choose("Pending application", controller.pendingApplications(),
                Formatters::application);
        if (application == null) {
            return;
        }
        controller.deny(application);
        console.println("  Application denied.");
    }

    // Prints a table of every intern of the company with their hours and status.
    private void monitorInterns() {
        List<String[]> rows = controller.interns().stream().map(i -> new String[] {
                String.valueOf(i.getId()), i.getStudentId(), i.getName(), i.getPosition().getTitle(),
                i.getRecordedHours() + "/" + i.getPosition().getRequiredHours(), i.getStatus().toString() })
                .toList();
        console.table(new String[] { "ID", "Student No.", "Name", "Position", "Hours", "Status" }, rows);
    }

    // Lets the user pick an intern and prints that intern's daily logs.
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

    // Lets the user pick a completed intern, then asks for a rating and comments and saves the evaluation.
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

    // Prints a table of the evaluations of the company's interns.
    private void viewEvaluations() {
        List<String[]> rows = controller.evaluations().stream().map(e -> new String[] {
                String.valueOf(e.getId()), e.getIntern().getName(), e.getIntern().getPosition().getTitle(),
                e.getRating() + "/5", e.getEvaluator().getName(), e.getDate().toString(), e.getComments() })
                .toList();
        console.table(new String[] { "ID", "Intern", "Position", "Rating", "Evaluator", "Date", "Comments" }, rows);
    }

    // Lets the user pick an intern and a new status (including COMPLETED), then applies it.
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
