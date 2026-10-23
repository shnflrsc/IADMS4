package iadms4.view;

import java.time.LocalDate;
import java.util.List;

import iadms4.controller.CoordinatorController;
import iadms4.model.Application;
import iadms4.model.ApplicationStatus;
import iadms4.model.Company;
import iadms4.model.Gender;
import iadms4.model.InternStatus;
import iadms4.model.Person;
import iadms4.model.Intern;
import iadms4.model.Opening;
import iadms4.model.Student;

/*
 * VIEW. The Coordinator dashboard.
 *
 * Responsibilities: builds the coordinator's menu. Each private method is one menu action ("drill"):
 * it asks the user for input through Console, calls CoordinatorController, then prints
 * the result. The view never talks to a service or repository directly.
 */
public class CoordinatorDashboardView {
    // Used for printing and reading.
    private final Console console;
    // Controller that handles the coordinator's requests.
    private final CoordinatorController controller;
    // Screen reused for the "Account settings" menu item.
    private final AccountSettingsView settings;

    /*
     * Creates the view.
     *
     * It takes console (the console used for input and output), controller (the coordinator controller) and
     * settings (the shared account settings screen).
     */
    public CoordinatorDashboardView(Console console, CoordinatorController controller,
            AccountSettingsView settings) {
        this.console = console;
        this.controller = controller;
        this.settings = settings;
    }

    /*
     * Shows the coordinator menu until the coordinator logs out. The items are grouped by topic.
     *
     * It takes coordinatorName (the name of the logged-in coordinator, shown in the menu title).
     */
    public void show(String coordinatorName) {
        new Menu(console, "Coordinator Dashboard - " + coordinatorName, "Log out")
                // Students & interns
                .add("View students", this::viewStudents)
                .add("Search students & interns", this::searchStudents)
                .add("Register student", this::registerStudent)
                .add("View interns (with status)", this::viewInterns)
                .add("Filter interns by status", this::filterInterns)
                .add("View intern evaluations", this::viewEvaluations)
                // Openings & applications
                .add("View openings", this::viewOpenings)
                .add("Endorse student to an opening", this::endorse)
                .add("View applications", this::viewApplications)
                .add("Filter applications by status", this::filterApplications)
                .add("Withdraw a pending application", this::withdraw)
                // Companies
                .add("View companies", this::viewCompanies)
                .add("Add company", this::addCompany)
                .add("Rename a company", this::renameCompany)
                .add("Delete a company", this::deleteCompany)
                // Users & accounts
                .add("View company representatives", this::viewRepresentatives)
                .add("View coordinators", this::viewCoordinators)
                .add("Add company representative account", this::addRepresentative)
                .add("Add coordinator account", this::addCoordinator)
                .add("Edit a person's details", this::editPerson)
                .add("Delete a person", this::deletePerson)
                .add("Reset a user's password", this::resetPassword)
                // Reports & settings
                .add("System summary report", this::report)
                .add("Account settings", settings::show)
                .run();
    }

    // Prints a table of all students and interns.
    private void viewStudents() {
        printStudents(controller.students());
    }

    /*
     * Prints a table of the given students. Interns show the company they are deployed to.
     *
     * It takes students (the students to print).
     */
    private void printStudents(List<Student> students) {
        List<String[]> rows = students.stream().map(s -> new String[] {
                String.valueOf(s.getId()), s.getStudentId(), s.getName(), s.getSection(), controller.emailOf(s),
                s instanceof Intern i ? "Intern @ " + i.getPosition().getCompany().getName() : "Student" }).toList();
        console.table(new String[] { "ID", "Student No.", "Name", "Section", "Email", "Status" }, rows);
    }

    // Asks for a keyword and prints the students and interns that match it.
    private void searchStudents() {
        String keyword = console.readLine("Search (name, student no., section or email; blank = all)");
        printStudents(controller.searchStudents(keyword));
    }

    // Asks for an intern status (or All) and prints the interns that match it.
    private void filterInterns() {
        InternStatus status = console.chooseFilter("Status", InternStatus.values());
        printInterns(controller.interns(status));
    }

    // Asks for an application status (or All) and prints the applications that match it.
    private void filterApplications() {
        ApplicationStatus status = console.chooseFilter("Status", ApplicationStatus.values());
        printApplications(controller.applications(status));
    }

    // Lets the user pick a pending application and withdraws it.
    private void withdraw() {
        Application application = console.choose("Pending application to withdraw", controller.pendingApplications(),
                Formatters::application);
        if (application == null) {
            return;
        }
        controller.withdraw(application);
        console.println("  Application withdrawn. The student can be endorsed again.");
    }

    /*
     * Lets the user pick a person and change their details. A blank answer keeps the old value;
     * the section is only asked for students.
     */
    private void editPerson() {
        Person person = console.choose("Person to edit", controller.everyone(),
                p -> Formatters.kind(p) + ": " + p.getName());
        if (person == null) {
            return;
        }
        String name = console.readLine("Full name (blank = keep \"" + person.getName() + "\")");
        String change = console.readLine("Change gender? currently " + person.getGender() + " (y/N)");
        Gender gender = change.equalsIgnoreCase("y") ? console.readGender() : person.getGender();
        LocalDate birthdate = console.readOptionalDate("Birthdate (now " + person.getBirthdate() + ")");
        String section = person instanceof Student s
                ? console.readLine("Section (blank = keep \"" + s.getSection() + "\")") : null;
        controller.updatePerson(person, name.isEmpty() ? person.getName() : name, gender,
                birthdate == null ? person.getBirthdate() : birthdate,
                section == null || section.isEmpty() ? null : section);
        console.println("  Details updated.");
    }

    // Lets the user pick a person and deletes them after the word DELETE is typed to confirm.
    private void deletePerson() {
        Person person = console.choose("Person to delete", controller.everyone(),
                p -> Formatters.kind(p) + ": " + p.getName());
        if (person == null) {
            return;
        }
        if (!console.readLine("Type DELETE to confirm deleting " + person.getName()).equals("DELETE")) {
            console.println("  Cancelled.");
            return;
        }
        controller.deletePerson(person);
        console.println("  Deleted.");
    }

    // Lets the user pick a person and sets a new password for them.
    private void resetPassword() {
        Person person = console.choose("Person", controller.everyone(), p -> Formatters.kind(p) + ": " + p.getName());
        if (person == null) {
            return;
        }
        controller.resetPassword(person, console.readRequired("New password (min 4 chars)"));
        console.println("  Password reset for " + person.getName() + ".");
    }

    // Lets the user pick a company and rename it.
    private void renameCompany() {
        Company company = console.choose("Company", controller.companies(), c -> c.getName());
        if (company == null) {
            return;
        }
        controller.renameCompany(company, console.readRequired("New name"));
        console.println("  Company renamed.");
    }

    // Lets the user pick a company and deletes it.
    private void deleteCompany() {
        Company company = console.choose("Company to delete", controller.companies(), c -> c.getName());
        if (company == null) {
            return;
        }
        controller.deleteCompany(company);
        console.println("  Company deleted.");
    }

    // Prints the system summary report followed by the per-company table.
    private void report() {
        Reports.print(console, "System summary", controller.report(), true);
        console.println("");
        Reports.printCompanies(console, controller.reportPerCompany());
    }

    // Prints a table of all interns.
    private void viewInterns() {
        printInterns(controller.interns());
    }

    /*
     * Prints a table of the given interns.
     *
     * It takes interns (the interns to print).
     */
    private void printInterns(List<Intern> interns) {
        List<String[]> rows = interns.stream().map(i -> new String[] {
                String.valueOf(i.getId()), i.getStudentId(), i.getName(), i.getPosition().getCompany().getName(),
                i.getPosition().getTitle(), i.getRecordedHours() + "/" + i.getPosition().getRequiredHours(),
                i.getStatus().toString(), controller.emailOf(i) }).toList();
        console.table(new String[] { "ID", "Student No.", "Name", "Company", "Position", "Hours", "Status", "Email" },
                rows);
    }

    // Prints a table of all company representatives.
    private void viewRepresentatives() {
        List<String[]> rows = controller.representatives().stream().map(r -> new String[] {
                String.valueOf(r.getId()), r.getName(), r.getCompany().getName(), controller.emailOf(r) }).toList();
        console.table(new String[] { "ID", "Name", "Company", "Email" }, rows);
    }

    // Prints a table of all coordinators.
    private void viewCoordinators() {
        List<String[]> rows = controller.coordinators().stream().map(c -> new String[] {
                String.valueOf(c.getId()), c.getEmployeeId(), c.getName(), controller.emailOf(c) }).toList();
        console.table(new String[] { "ID", "Employee ID", "Name", "Email" }, rows);
    }

    // Asks for the details of a new student and registers the student with a login account.
    private void registerStudent() {
        String name = console.readRequired("Full name");
        Gender gender = console.readGender();
        LocalDate birthdate = console.readDate("Birthdate");
        String studentId = console.readRequired("Student number");
        String section = console.readLine("Section");
        String email = console.readRequired("Email (login)");
        String password = console.readRequired("Initial password");
        controller.registerStudent(name, gender, birthdate, studentId, section, email, password);
        console.println("  Student registered.");
    }

    // Prints a table of the openings of every company.
    private void viewOpenings() {
        List<String[]> rows = controller.allOpenings().stream().map(o -> new String[] {
                String.valueOf(o.getId()), o.getPosition().getCompany().getName(), o.getPosition().getTitle(),
                String.valueOf(o.getPosition().getRequiredHours()), o.getStatus().toString() }).toList();
        console.table(new String[] { "ID", "Company", "Position", "Hours", "Status" }, rows);
    }

    /*
     * Lets the user pick a student and an opening, then asks the controller to endorse the student.
     * This is a good example of a drill: the view only collects input, and the rules (already an
     * intern? opening closed? duplicate?) are checked in ApplicationService.
     */
    private void endorse() {
        Student student = console.choose("Student", controller.students(),
                s -> s.getStudentId() + " " + s.getName() + " (" + Formatters.kind(s) + ")");
        if (student == null) {
            return;
        }
        Opening opening = console.choose("Opening", controller.openOpenings(), Formatters::opening);
        if (opening == null) {
            return;
        }
        controller.endorse(student, opening);
        console.println("  " + student.getName() + " endorsed to " + opening.getPosition().getTitle() + ".");
    }

    // Prints a table of every application.
    private void viewApplications() {
        printApplications(controller.applications());
    }

    /*
     * Prints a table of the given applications.
     *
     * It takes applications (the applications to print).
     */
    private void printApplications(List<Application> applications) {
        List<String[]> rows = applications.stream().map(a -> new String[] {
                String.valueOf(a.getId()), a.getStudent().getName(), a.getOpening().getPosition().getCompany().getName(),
                a.getOpening().getPosition().getTitle(), a.getCoordinator().getName(), a.getStatus().toString() })
                .toList();
        console.table(new String[] { "ID", "Student", "Company", "Position", "Endorsed by", "Status" }, rows);
    }

    // Prints a table of every intern evaluation.
    private void viewEvaluations() {
        List<String[]> rows = controller.evaluations().stream().map(e -> new String[] {
                String.valueOf(e.getId()), e.getIntern().getName(),
                e.getIntern().getPosition().getCompany().getName(), e.getIntern().getPosition().getTitle(),
                e.getRating() + "/5", e.getEvaluator().getName(), e.getDate().toString(), e.getComments() })
                .toList();
        console.table(new String[] { "ID", "Intern", "Company", "Position", "Rating", "Evaluator", "Date", "Comments" },
                rows);
    }

    // Prints a table of all companies.
    private void viewCompanies() {
        List<String[]> rows = controller.companies().stream()
                .map(c -> new String[] { String.valueOf(c.getId()), c.getName() }).toList();
        console.table(new String[] { "ID", "Company" }, rows);
    }

    // Asks for a name and adds a new company.
    private void addCompany() {
        controller.addCompany(console.readRequired("Company name"));
        console.println("  Company added.");
    }

    // Lets the user pick a company, then creates a representative account for it.
    private void addRepresentative() {
        var company = console.choose("Company", controller.companies(), c -> c.getName());
        if (company == null) {
            console.println("  Add a company first.");
            return;
        }
        String name = console.readRequired("Full name");
        Gender gender = console.readGender();
        LocalDate birthdate = console.readDate("Birthdate");
        String email = console.readRequired("Email (login)");
        String password = console.readRequired("Initial password");
        controller.addRepresentative(name, gender, birthdate, company, email, password);
        console.println("  Representative account created for " + company.getName() + ".");
    }

    // Asks for the details of a new coordinator and creates the account.
    private void addCoordinator() {
        String name = console.readRequired("Full name");
        Gender gender = console.readGender();
        LocalDate birthdate = console.readDate("Birthdate");
        String employeeId = console.readRequired("Employee ID");
        String email = console.readRequired("Email (login)");
        String password = console.readRequired("Initial password");
        controller.addCoordinator(name, gender, birthdate, employeeId, email, password);
        console.println("  Coordinator account created.");
    }
}
