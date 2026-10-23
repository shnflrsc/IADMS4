package iadms4.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import iadms4.model.Application;
import iadms4.model.ApplicationStatus;
import iadms4.model.InternStatus;
import iadms4.model.Company;
import iadms4.model.CompanyRepresentative;
import iadms4.model.Coordinator;
import iadms4.model.Evaluation;
import iadms4.model.Gender;
import iadms4.model.Intern;
import iadms4.model.Opening;
import iadms4.model.Person;
import iadms4.model.Session;
import iadms4.model.Student;
import iadms4.service.ApplicationService;
import iadms4.service.AuthService;
import iadms4.service.ReportService;
import iadms4.service.CompanyService;
import iadms4.service.EvaluationService;
import iadms4.service.OpeningService;
import iadms4.service.PersonService;

/*
 * CONTROLLER. Handles everything the Coordinator dashboard can do.
 *
 * Responsibilities: each public method matches one menu action in CoordinatorDashboardView and delegates
 * to a service. The logged-in coordinator comes from Session.
 */
public class CoordinatorController {
    // Service for people and accounts.
    private final PersonService persons;
    // Service for companies.
    private final CompanyService companies;
    // Service for positions and openings.
    private final OpeningService openings;
    // Service for applications.
    private final ApplicationService applications;
    // Service for evaluations.
    private final EvaluationService evaluations;
    // Service for password resets.
    private final AuthService auth;
    // Service for reports.
    private final ReportService reports;

    /*
     * Creates the controller.
     *
     * It takes persons (the person service), companies (the company service), openings (the opening
     * service), applications (the application service), evaluations (the evaluation service), auth (the
     * authentication service) and reports (the report service).
     */
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

    /*
     * Lists every evaluation in the system.
     *
     * It returns all evaluations.
     */
    public List<Evaluation> evaluations() {
        return evaluations.all();
    }

    /*
     * Gets the logged-in coordinator.
     *
     * It returns the current person cast to Coordinator.
     */
    private Coordinator me() {
        return (Coordinator) Session.getInstance().getPerson();
    }

    /*
     * Lists all students (interns included).
     *
     * It returns the students.
     */
    public List<Student> students() {
        return persons.students();
    }

    /*
     * Searches students and interns by keyword.
     *
     * It takes keyword (the text to look for (blank = everyone)). It returns the matching students and
     * interns.
     */
    public List<Student> searchStudents(String keyword) {
        return persons.searchStudents(keyword);
    }

    /*
     * Lists everyone with a login: students, interns, representatives and coordinators.
     *
     * It returns all people.
     */
    public List<Person> everyone() {
        List<Person> all = new ArrayList<>();
        all.addAll(persons.students());
        all.addAll(persons.representatives());
        all.addAll(persons.coordinators());
        return all;
    }

    /*
     * Lists interns with a given status.
     *
     * It takes status (the status to keep, or null for all). It returns the matching interns.
     */
    public List<Intern> interns(InternStatus status) {
        return persons.interns(status);
    }

    /*
     * Lists applications with a given status.
     *
     * It takes status (the status to keep, or null for all). It returns the matching applications.
     */
    public List<Application> applications(ApplicationStatus status) {
        return applications.all(status);
    }

    /*
     * Lists the applications that are still PENDING.
     *
     * It returns the pending applications.
     */
    public List<Application> pendingApplications() {
        return applications.all(ApplicationStatus.PENDING);
    }

    /*
     * Withdraws a pending application.
     *
     * It takes application (the application to withdraw).
     */
    public void withdraw(Application application) {
        applications.withdraw(application);
    }

    /*
     * Edits a person's details.
     *
     * It takes person (the person to edit), name (the new name), gender (the new gender), birthdate (the
     * new birthdate) and section (the new section (students only), or null).
     */
    public void updatePerson(Person person, String name, Gender gender, LocalDate birthdate, String section) {
        persons.updateDetails(person, name, gender, birthdate, section);
    }

    /*
     * Deletes a person on behalf of the logged-in coordinator.
     *
     * It takes person (the person to delete).
     */
    public void deletePerson(Person person) {
        persons.delete(me(), person);
    }

    /*
     * Resets a person's password.
     *
     * It takes person (the person whose password is reset) and newPassword (the new password).
     */
    public void resetPassword(Person person, String newPassword) {
        auth.resetPassword(person, newPassword);
    }

    /*
     * Renames a company.
     *
     * It takes company (the company) and name (the new name).
     */
    public void renameCompany(Company company, String name) {
        companies.rename(company, name);
    }

    /*
     * Deletes a company.
     *
     * It takes company (the company to delete).
     */
    public void deleteCompany(Company company) {
        companies.delete(company);
    }

    /*
     * Builds the whole-system summary report.
     *
     * It returns the report numbers.
     */
    public ReportService.Summary report() {
        return reports.system();
    }

    /*
     * Builds one report line per company.
     *
     * It returns the per-company report lines.
     */
    public List<ReportService.CompanyLine> reportPerCompany() {
        return reports.perCompany();
    }

    /*
     * Lists the openings that accept endorsements.
     *
     * It returns the OPEN openings.
     */
    public List<Opening> openOpenings() {
        return openings.openOpenings();
    }

    /*
     * Lists every opening.
     *
     * It returns all openings.
     */
    public List<Opening> allOpenings() {
        return openings.allOpenings();
    }

    /*
     * Lists every application.
     *
     * It returns all applications.
     */
    public List<Application> applications() {
        return applications.all();
    }

    /*
     * Lists every company.
     *
     * It returns all companies.
     */
    public List<Company> companies() {
        return companies.all();
    }

    /*
     * Gets the login email of a person.
     *
     * It takes person (the person). It returns the email, or "-" if there is none.
     */
    public String emailOf(Person person) {
        return persons.emailOf(person);
    }

    /*
     * Lists all interns.
     *
     * It returns the interns.
     */
    public List<Intern> interns() {
        return persons.interns();
    }

    /*
     * Lists all company representatives.
     *
     * It returns the representatives.
     */
    public List<CompanyRepresentative> representatives() {
        return persons.representatives();
    }

    /*
     * Lists all coordinators.
     *
     * It returns the coordinators.
     */
    public List<Coordinator> coordinators() {
        return persons.coordinators();
    }

    /*
     * Registers a new student with a login account.
     *
     * It takes name (the full name), gender (the gender), birthdate (the date of birth), studentId (the
     * student number), section (the class section), email (the login email) and password (the login
     * password).
     */
    public void registerStudent(String name, Gender gender, LocalDate birthdate, String studentId, String section,
            String email, String password) {
        persons.registerStudent(name, gender, birthdate, studentId, section, email, password);
    }

    /*
     * Endorses a student to an opening on behalf of the logged-in coordinator.
     *
     * It takes student (the student to endorse) and opening (the opening to endorse to). It returns the new
     * pending application.
     */
    public Application endorse(Student student, Opening opening) {
        return applications.endorse(me(), student, opening);
    }

    /*
     * Adds a new company.
     *
     * It takes name (the company name). It returns the new company.
     */
    public Company addCompany(String name) {
        return companies.create(name);
    }

    /*
     * Adds a company representative account.
     *
     * It takes name (the full name), gender (the gender), birthdate (the date of birth), company (the
     * company the person represents), email (the login email) and password (the login password).
     */
    public void addRepresentative(String name, Gender gender, LocalDate birthdate, Company company, String email,
            String password) {
        persons.registerRepresentative(name, gender, birthdate, company, email, password);
    }

    /*
     * Adds a coordinator account.
     *
     * It takes name (the full name), gender (the gender), birthdate (the date of birth), employeeId (the
     * employee number), email (the login email) and password (the login password).
     */
    public void addCoordinator(String name, Gender gender, LocalDate birthdate, String employeeId, String email,
            String password) {
        persons.registerCoordinator(name, gender, birthdate, employeeId, email, password);
    }
}
