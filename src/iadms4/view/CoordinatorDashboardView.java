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

/**
 * VIEW. The Coordinator dashboard. Each private method is one menu action ("drill"): ask the user
 * for input through Console, call CoordinatorController, then print the result.
 */
public class CoordinatorDashboardView {
    private final Console console;
    private final CoordinatorController controller;
    private final AccountSettingsView settings;

    public CoordinatorDashboardView(Console console, CoordinatorController controller,
            AccountSettingsView settings) {
        this.console = console;
        this.controller = controller;
        this.settings = settings;
    }

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

    private void viewStudents() {
        printStudents(controller.students());
    }

    private void printStudents(List<Student> students) {
        List<String[]> rows = students.stream().map(s -> new String[] {
                String.valueOf(s.getId()), s.getStudentId(), s.getName(), s.getSection(), controller.emailOf(s),
                s instanceof Intern i ? "Intern @ " + i.getPosition().getCompany().getName() : "Student" }).toList();
        console.table(new String[] { "ID", "Student No.", "Name", "Section", "Email", "Status" }, rows);
    }

    private void searchStudents() {
        String keyword = console.readLine("Search (name, student no., section or email; blank = all)");
        printStudents(controller.searchStudents(keyword));
    }

    private void filterInterns() {
        InternStatus status = console.chooseFilter("Status", InternStatus.values());
        printInterns(controller.interns(status));
    }

    private void filterApplications() {
        ApplicationStatus status = console.chooseFilter("Status", ApplicationStatus.values());
        printApplications(controller.applications(status));
    }

    private void withdraw() {
        Application application = console.choose("Pending application to withdraw", controller.pendingApplications(),
                Formatters::application);
        if (application == null) {
            return;
        }
        controller.withdraw(application);
        console.println("  Application withdrawn. The student can be endorsed again.");
    }

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

    private void resetPassword() {
        Person person = console.choose("Person", controller.everyone(), p -> Formatters.kind(p) + ": " + p.getName());
        if (person == null) {
            return;
        }
        controller.resetPassword(person, console.readRequired("New password (min 4 chars)"));
        console.println("  Password reset for " + person.getName() + ".");
    }

    private void renameCompany() {
        Company company = console.choose("Company", controller.companies(), c -> c.getName());
        if (company == null) {
            return;
        }
        controller.renameCompany(company, console.readRequired("New name"));
        console.println("  Company renamed.");
    }

    private void deleteCompany() {
        Company company = console.choose("Company to delete", controller.companies(), c -> c.getName());
        if (company == null) {
            return;
        }
        controller.deleteCompany(company);
        console.println("  Company deleted.");
    }

    private void report() {
        Reports.print(console, "System summary", controller.report(), true);
        console.println("");
        Reports.printCompanies(console, controller.reportPerCompany());
    }

    private void viewInterns() {
        printInterns(controller.interns());
    }

    private void printInterns(List<Intern> interns) {
        List<String[]> rows = interns.stream().map(i -> new String[] {
                String.valueOf(i.getId()), i.getStudentId(), i.getName(), i.getPosition().getCompany().getName(),
                i.getPosition().getTitle(), i.getRecordedHours() + "/" + i.getPosition().getRequiredHours(),
                i.getStatus().toString(), controller.emailOf(i) }).toList();
        console.table(new String[] { "ID", "Student No.", "Name", "Company", "Position", "Hours", "Status", "Email" },
                rows);
    }

    private void viewRepresentatives() {
        List<String[]> rows = controller.representatives().stream().map(r -> new String[] {
                String.valueOf(r.getId()), r.getName(), r.getCompany().getName(), controller.emailOf(r) }).toList();
        console.table(new String[] { "ID", "Name", "Company", "Email" }, rows);
    }

    private void viewCoordinators() {
        List<String[]> rows = controller.coordinators().stream().map(c -> new String[] {
                String.valueOf(c.getId()), c.getEmployeeId(), c.getName(), controller.emailOf(c) }).toList();
        console.table(new String[] { "ID", "Employee ID", "Name", "Email" }, rows);
    }

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

    private void viewOpenings() {
        List<String[]> rows = controller.allOpenings().stream().map(o -> new String[] {
                String.valueOf(o.getId()), o.getPosition().getCompany().getName(), o.getPosition().getTitle(),
                String.valueOf(o.getPosition().getRequiredHours()), o.getStatus().toString() }).toList();
        console.table(new String[] { "ID", "Company", "Position", "Hours", "Status" }, rows);
    }

    // Example of a drill: pick a student, pick an opening, then ask the controller to endorse.
    // The rules (already an intern? opening closed? duplicate?) are checked in ApplicationService.
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

    private void viewApplications() {
        printApplications(controller.applications());
    }

    private void printApplications(List<Application> applications) {
        List<String[]> rows = applications.stream().map(a -> new String[] {
                String.valueOf(a.getId()), a.getStudent().getName(), a.getOpening().getPosition().getCompany().getName(),
                a.getOpening().getPosition().getTitle(), a.getCoordinator().getName(), a.getStatus().toString() })
                .toList();
        console.table(new String[] { "ID", "Student", "Company", "Position", "Endorsed by", "Status" }, rows);
    }

    private void viewEvaluations() {
        List<String[]> rows = controller.evaluations().stream().map(e -> new String[] {
                String.valueOf(e.getId()), e.getIntern().getName(),
                e.getIntern().getPosition().getCompany().getName(), e.getIntern().getPosition().getTitle(),
                e.getRating() + "/5", e.getEvaluator().getName(), e.getDate().toString(), e.getComments() })
                .toList();
        console.table(new String[] { "ID", "Intern", "Company", "Position", "Rating", "Evaluator", "Date", "Comments" },
                rows);
    }

    private void viewCompanies() {
        List<String[]> rows = controller.companies().stream()
                .map(c -> new String[] { String.valueOf(c.getId()), c.getName() }).toList();
        console.table(new String[] { "ID", "Company" }, rows);
    }

    private void addCompany() {
        controller.addCompany(console.readRequired("Company name"));
        console.println("  Company added.");
    }

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
