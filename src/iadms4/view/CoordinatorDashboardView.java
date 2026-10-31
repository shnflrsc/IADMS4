package iadms4.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import iadms4.controller.CoordinatorController;
import iadms4.model.Application;
import iadms4.model.Company;
import iadms4.model.CompanyRepresentative;
import iadms4.model.Coordinator;
import iadms4.model.Evaluation;
import iadms4.model.Gender;
import iadms4.model.Intern;
import iadms4.model.Opening;
import iadms4.model.Person;
import iadms4.model.Student;

/*
 * VIEW. coordinator dashboard
 * each private method = 1 menu item aka a "drill"
 * ask input, call controller, print. dont call services from here
 */
public class CoordinatorDashboardView extends Menu {
    private final CoordinatorController controller;
    private final AccountSettingsView settings;

    // makes the screen, takes console, controller, settings
    public CoordinatorDashboardView(Console console, CoordinatorController controller,
            AccountSettingsView settings) {
        super(console);
        this.controller = controller;
        this.settings = settings;
    }

    // coordinator menu, grouped by topic bc it was a mess before
    public void show(String coordinatorName) {
        String[] items = {
                // items 1-6 are about students n interns
                "View students", "Search students & interns", "Register student", "View interns (with status)",
                "Filter interns by status", "View intern evaluations",
                // items 7-11 are about openings n applications
                "View openings", "Endorse student to an opening", "View applications",
                "Filter applications by status", "Withdraw a pending application",
                // items 12-15 are about companies
                "View companies", "Add company", "Rename a company", "Delete a company",
                // items 16-22 are about users n accounts
                "View company representatives", "View coordinators", "Add company representative account",
                "Add coordinator account", "Edit a person's details", "Delete a person", "Reset a user's password",
                // items 23-24 are the report and settings
                "System summary report", "Account settings" };
        run("Coordinator Dashboard - " + coordinatorName, items, "Log out");
    }

    // runs whatever the coordinator picked, numbers match the items array
    @Override
    protected void runItem(int choice) {
        switch (choice) {
            case 1: printStudents(controller.students()); break;
            case 2: searchStudents(); break;
            case 3: registerStudent(); break;
            case 4: printInterns(controller.interns()); break;
            case 5: printInterns(controller.interns(console.chooseInternStatus())); break;
            case 6: viewEvaluations(); break;
            case 7: viewOpenings(); break;
            case 8: endorse(); break;
            case 9: printApplications(controller.applications()); break;
            case 10: printApplications(controller.applications(console.chooseApplicationStatus())); break;
            case 11: withdraw(); break;
            case 12: viewCompanies(); break;
            case 13: addCompany(); break;
            case 14: renameCompany(); break;
            case 15: deleteCompany(); break;
            case 16: viewRepresentatives(); break;
            case 17: viewCoordinators(); break;
            case 18: addRepresentative(); break;
            case 19: addCoordinator(); break;
            case 20: editPerson(); break;
            case 21: deletePerson(); break;
            case 22: resetPassword(); break;
            case 23: report(); break;
            case 24: settings.show(); break;
        }
    }

    // students table, interns show their company
    private void printStudents(List<Student> students) {
        List<String[]> rows = new ArrayList<>();
        for (Student s : students) {
            String status = "Student";
            if (s instanceof Intern) {
                status = "Intern @ " + ((Intern) s).getPosition().getCompany().getName();
            }
            rows.add(new String[] { String.valueOf(s.getId()), s.getStudentId(), s.getName(), s.getSection(),
                    controller.emailOf(s), status });
        }
        console.table(new String[] { "ID", "Student No.", "Name", "Section", "Email", "Status" }, rows);
    }

    // asks for a keyword and prints the students and interns that match
    private void searchStudents() {
        String keyword = console.readLine("Search (name, student no., section or email; blank = all)");
        printStudents(controller.searchStudents(keyword));
    }

    // asks for all the student details then registers them w/ an account
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

    // prints a table of the interns u pass in, w/ company and email
    private void printInterns(List<Intern> interns) {
        List<String[]> rows = new ArrayList<>();
        for (Intern i : interns) {
            rows.add(new String[] { String.valueOf(i.getId()), i.getStudentId(), i.getName(),
                    i.getPosition().getCompany().getName(), i.getPosition().getTitle(),
                    i.getRecordedHours() + "/" + i.getPosition().getRequiredHours(), i.getStatus().toString(),
                    controller.emailOf(i) });
        }
        console.table(new String[] { "ID", "Student No.", "Name", "Company", "Position", "Hours", "Status", "Email" },
                rows);
    }

    // prints a table of every evaluation in the system
    private void viewEvaluations() {
        List<String[]> rows = new ArrayList<>();
        for (Evaluation e : controller.evaluations()) {
            rows.add(new String[] { String.valueOf(e.getId()), e.getIntern().getName(),
                    e.getIntern().getPosition().getCompany().getName(), e.getIntern().getPosition().getTitle(),
                    e.getRating() + "/5", e.getEvaluator().getName(), e.getDate().toString(), e.getComments() });
        }
        console.table(new String[] { "ID", "Intern", "Company", "Position", "Rating", "Evaluator", "Date", "Comments" },
                rows);
    }

    // prints a table of the openings of every company
    private void viewOpenings() {
        List<String[]> rows = new ArrayList<>();
        for (Opening o : controller.allOpenings()) {
            rows.add(new String[] { String.valueOf(o.getId()), o.getPosition().getCompany().getName(),
                    o.getPosition().getTitle(), String.valueOf(o.getPosition().getRequiredHours()),
                    o.getStatus().toString() });
        }
        console.table(new String[] { "ID", "Company", "Position", "Hours", "Status" }, rows);
    }

    // endorse screen, rules are in ApplicationService not here
    private void endorse() {
        List<Student> students = controller.students();
        int studentIndex = console.choose("Student", Formatters.students(students));
        if (studentIndex == -1) {
            return;
        }
        List<Opening> openings = controller.openOpenings();
        int openingIndex = console.choose("Opening", Formatters.openings(openings));
        if (openingIndex == -1) {
            return;
        }
        Student student = students.get(studentIndex);
        Opening opening = openings.get(openingIndex);
        controller.endorse(student, opening);
        console.println("  " + student.getName() + " endorsed to " + opening.getPosition().getTitle() + ".");
    }

    // prints a table of whatever applications u pass in
    private void printApplications(List<Application> applications) {
        List<String[]> rows = new ArrayList<>();
        for (Application a : applications) {
            rows.add(new String[] { String.valueOf(a.getId()), a.getStudent().getName(),
                    a.getOpening().getPosition().getCompany().getName(), a.getOpening().getPosition().getTitle(),
                    a.getCoordinator().getName(), a.getStatus().toString() });
        }
        console.table(new String[] { "ID", "Student", "Company", "Position", "Endorsed by", "Status" }, rows);
    }

    // coordinator picks a pending application and it gets withdrawn
    private void withdraw() {
        List<Application> pending = controller.pendingApplications();
        int index = console.choose("Pending application to withdraw", Formatters.applications(pending));
        if (index == -1) {
            return;
        }
        controller.withdraw(pending.get(index));
        console.println("  Application withdrawn. The student can be endorsed again.");
    }

    // prints a table of all the companies
    private void viewCompanies() {
        List<String[]> rows = new ArrayList<>();
        for (Company c : controller.companies()) {
            rows.add(new String[] { String.valueOf(c.getId()), c.getName() });
        }
        console.table(new String[] { "ID", "Company" }, rows);
    }

    // asks for a company name and adds it
    private void addCompany() {
        controller.addCompany(console.readRequired("Company name"));
        console.println("  Company added.");
    }

    // coordinator picks a company and types the new name
    private void renameCompany() {
        List<Company> companies = controller.companies();
        int index = console.choose("Company", Formatters.companies(companies));
        if (index == -1) {
            return;
        }
        controller.renameCompany(companies.get(index), console.readRequired("New name"));
        console.println("  Company renamed.");
    }

    // coordinator picks a company and it gets deleted
    private void deleteCompany() {
        List<Company> companies = controller.companies();
        int index = console.choose("Company to delete", Formatters.companies(companies));
        if (index == -1) {
            return;
        }
        controller.deleteCompany(companies.get(index));
        console.println("  Company deleted.");
    }

    // prints a table of all the company reps
    private void viewRepresentatives() {
        List<String[]> rows = new ArrayList<>();
        for (CompanyRepresentative r : controller.representatives()) {
            rows.add(new String[] { String.valueOf(r.getId()), r.getName(), r.getCompany().getName(),
                    controller.emailOf(r) });
        }
        console.table(new String[] { "ID", "Name", "Company", "Email" }, rows);
    }

    // prints a table of all the coordinators
    private void viewCoordinators() {
        List<String[]> rows = new ArrayList<>();
        for (Coordinator c : controller.coordinators()) {
            rows.add(new String[] { String.valueOf(c.getId()), c.getEmployeeId(), c.getName(), controller.emailOf(c) });
        }
        console.table(new String[] { "ID", "Employee ID", "Name", "Email" }, rows);
    }

    // picks the company first then asks for the rep's details
    private void addRepresentative() {
        List<Company> companies = controller.companies();
        int index = console.choose("Company", Formatters.companies(companies));
        if (index == -1) {
            console.println("  Add a company first.");
            return;
        }
        Company company = companies.get(index);
        String name = console.readRequired("Full name");
        Gender gender = console.readGender();
        LocalDate birthdate = console.readDate("Birthdate");
        String email = console.readRequired("Email (login)");
        String password = console.readRequired("Initial password");
        controller.addRepresentative(name, gender, birthdate, company, email, password);
        console.println("  Representative account created for " + company.getName() + ".");
    }

    // asks for the new coordinator's details and makes the account
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

    // edit person screen. blank = keep old value
    private void editPerson() {
        List<Person> people = controller.everyone();
        int index = console.choose("Person to edit", Formatters.people(people));
        if (index == -1) {
            return;
        }
        Person person = people.get(index);
        String name = console.readLine("Full name (blank = keep \"" + person.getName() + "\")");
        if (name.isEmpty()) {
            name = person.getName();
        }
        Gender gender = person.getGender();
        String change = console.readLine("Change gender? currently " + person.getGender() + " (y/N)");
        if (change.equalsIgnoreCase("y")) {
            gender = console.readGender();
        }
        LocalDate birthdate = console.readOptionalDate("Birthdate (now " + person.getBirthdate() + ")");
        if (birthdate == null) {
            birthdate = person.getBirthdate();
        }
        // only students have section. null = service wont change it
        String section = null;
        if (person instanceof Student) {
            section = console.readLine("Section (blank = keep \"" + ((Student) person).getSection() + "\")");
            if (section.isEmpty()) {
                section = null;
            }
        }
        controller.updatePerson(person, name, gender, birthdate, section);
        console.println("  Details updated.");
    }

    // delete person screen, must type DELETE
    private void deletePerson() {
        List<Person> people = controller.everyone();
        int index = console.choose("Person to delete", Formatters.people(people));
        if (index == -1) {
            return;
        }
        Person person = people.get(index);
        if (!console.readLine("Type DELETE to confirm deleting " + person.getName()).equals("DELETE")) {
            console.println("  Cancelled.");
            return;
        }
        controller.deletePerson(person);
        console.println("  Deleted.");
    }

    // coordinator picks a person and types their new password
    private void resetPassword() {
        List<Person> people = controller.everyone();
        int index = console.choose("Person", Formatters.people(people));
        if (index == -1) {
            return;
        }
        Person person = people.get(index);
        controller.resetPassword(person, console.readRequired("New password (min 4 chars)"));
        console.println("  Password reset for " + person.getName() + ".");
    }

    // system report then per company table
    private void report() {
        Reports.print(console, "System summary", controller.report(), true);
        console.println("");
        Reports.printCompanies(console, controller.companies(), controller.reportPerCompany());
    }
}
