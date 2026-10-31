package iadms4.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import iadms4.model.Application;
import iadms4.model.ApplicationStatus;
import iadms4.model.Company;
import iadms4.model.CompanyRepresentative;
import iadms4.model.Coordinator;
import iadms4.model.Evaluation;
import iadms4.model.Gender;
import iadms4.model.Intern;
import iadms4.model.InternStatus;
import iadms4.model.Opening;
import iadms4.model.Person;
import iadms4.model.Session;
import iadms4.model.Student;
import iadms4.model.Summary;
import iadms4.service.ApplicationService;
import iadms4.service.AuthService;
import iadms4.service.CompanyService;
import iadms4.service.EvaluationService;
import iadms4.service.OpeningService;
import iadms4.service.PersonService;
import iadms4.service.ReportService;

/*
 * CONTROLLER. Handles everything the Coordinator dashboard can do.
 * Each public method matches one menu action and passes the request to a service.
 */
public class CoordinatorController {
    private final PersonService persons;
    private final CompanyService companies;
    private final OpeningService openings;
    private final ApplicationService applications;
    private final EvaluationService evaluations;
    private final AuthService auth;
    private final ReportService reports;

    // Creates the controller using the given services.
    public CoordinatorController(PersonService persons, CompanyService companies, OpeningService openings,
            ApplicationService applications, EvaluationService evaluations, AuthService auth,
            ReportService reports) {
        this.persons = persons;
        this.companies = companies;
        this.openings = openings;
        this.applications = applications;
        this.evaluations = evaluations;
        this.auth = auth;
        this.reports = reports;
    }

    // Returns the logged-in coordinator.
    private Coordinator me() {
        return (Coordinator) Session.getInstance().getPerson();
    }

    // Returns all students, including interns.
    public List<Student> students() {
        return persons.students();
    }

    // Returns the students and interns that match the given keyword.
    public List<Student> searchStudents(String keyword) {
        return persons.searchStudents(keyword);
    }

    // Registers a new student with a login account.
    public void registerStudent(String name, Gender gender, LocalDate birthdate, String studentId, String section,
            String email, String password) {
        persons.registerStudent(name, gender, birthdate, studentId, section, email, password);
    }

    // Returns all interns.
    public List<Intern> interns() {
        return persons.interns();
    }

    // Returns the interns with the given status, or all if status is null.
    public List<Intern> interns(InternStatus status) {
        return persons.interns(status);
    }

    // Returns every evaluation.
    public List<Evaluation> evaluations() {
        return evaluations.all();
    }

    // Returns every opening.
    public List<Opening> allOpenings() {
        return openings.allOpenings();
    }

    // Returns the openings that accept endorsements.
    public List<Opening> openOpenings() {
        return openings.openOpenings();
    }

    // Endorses the given student to the given opening.
    public void endorse(Student student, Opening opening) {
        applications.endorse(me(), student, opening);
    }

    // Returns every application.
    public List<Application> applications() {
        return applications.all();
    }

    // Returns the applications with the given status, or all if status is null.
    public List<Application> applications(ApplicationStatus status) {
        return applications.all(status);
    }

    // Returns the PENDING applications.
    public List<Application> pendingApplications() {
        return applications.all(ApplicationStatus.PENDING);
    }

    // Withdraws the given pending application.
    public void withdraw(Application application) {
        applications.withdraw(application);
    }

    // Returns every company.
    public List<Company> companies() {
        return companies.all();
    }

    // Adds a company with the given name.
    public void addCompany(String name) {
        companies.create(name);
    }

    // Renames the given company.
    public void renameCompany(Company company, String name) {
        companies.rename(company, name);
    }

    // Deletes the given company.
    public void deleteCompany(Company company) {
        companies.delete(company);
    }

    // Returns all company representatives.
    public List<CompanyRepresentative> representatives() {
        return persons.representatives();
    }

    // Returns all coordinators.
    public List<Coordinator> coordinators() {
        return persons.coordinators();
    }

    // Returns everyone with a login: students and interns, then representatives, then coordinators.
    public List<Person> everyone() {
        List<Person> all = new ArrayList<>();
        all.addAll(persons.students());
        all.addAll(persons.representatives());
        all.addAll(persons.coordinators());
        return all;
    }

    // Adds a company representative account for the given company.
    public void addRepresentative(String name, Gender gender, LocalDate birthdate, Company company, String email,
            String password) {
        persons.registerRepresentative(name, gender, birthdate, company, email, password);
    }

    // Adds a coordinator account.
    public void addCoordinator(String name, Gender gender, LocalDate birthdate, String employeeId, String email,
            String password) {
        persons.registerCoordinator(name, gender, birthdate, employeeId, email, password);
    }

    // Edits the details of the given person. The section is only used for students.
    public void updatePerson(Person person, String name, Gender gender, LocalDate birthdate, String section) {
        persons.updateDetails(person, name, gender, birthdate, section);
    }

    // Deletes the given person on behalf of the logged-in coordinator.
    public void deletePerson(Person person) {
        persons.delete(me(), person);
    }

    // Sets a new password for the given person.
    public void resetPassword(Person person, String newPassword) {
        auth.resetPassword(person, newPassword);
    }

    // Returns the login email of the given person, or "-" if there is none.
    public String emailOf(Person person) {
        return persons.emailOf(person);
    }

    // Returns the whole-system summary report.
    public Summary report() {
        return reports.system();
    }

    // Returns one summary report per company, in the same order as companies().
    public List<Summary> reportPerCompany() {
        return reports.perCompany();
    }
}
