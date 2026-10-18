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

/**
 * CONTROLLER. Everything the Coordinator dashboard can do. Each public method matches one menu
 * action in CoordinatorDashboardView and delegates to a service. The logged-in coordinator comes
 * from Session.
 */
public class CoordinatorController {
    private final PersonService persons;
    private final CompanyService companies;
    private final OpeningService openings;
    private final ApplicationService applications;
    private final EvaluationService evaluations;
    private final AuthService auth;
    private final ReportService reports;

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

    public List<Evaluation> evaluations() {
        return evaluations.all();
    }

    private Coordinator me() {
        return (Coordinator) Session.getInstance().getPerson();
    }

    public List<Student> students() {
        return persons.students();
    }

    /** Students and interns matching the keyword (blank = everyone). */
    public List<Student> searchStudents(String keyword) {
        return persons.searchStudents(keyword);
    }

    /** Everyone with a login: students, interns, representatives and coordinators. */
    public List<Person> everyone() {
        List<Person> all = new ArrayList<>();
        all.addAll(persons.students());
        all.addAll(persons.representatives());
        all.addAll(persons.coordinators());
        return all;
    }

    public List<Intern> interns(InternStatus status) {
        return persons.interns(status);
    }

    public List<Application> applications(ApplicationStatus status) {
        return applications.all(status);
    }

    public List<Application> pendingApplications() {
        return applications.all(ApplicationStatus.PENDING);
    }

    public void withdraw(Application application) {
        applications.withdraw(application);
    }

    public void updatePerson(Person person, String name, Gender gender, LocalDate birthdate, String section) {
        persons.updateDetails(person, name, gender, birthdate, section);
    }

    public void deletePerson(Person person) {
        persons.delete(me(), person);
    }

    public void resetPassword(Person person, String newPassword) {
        auth.resetPassword(person, newPassword);
    }

    public void renameCompany(Company company, String name) {
        companies.rename(company, name);
    }

    public void deleteCompany(Company company) {
        companies.delete(company);
    }

    public ReportService.Summary report() {
        return reports.system();
    }

    public List<ReportService.CompanyLine> reportPerCompany() {
        return reports.perCompany();
    }

    public List<Opening> openOpenings() {
        return openings.openOpenings();
    }

    public List<Opening> allOpenings() {
        return openings.allOpenings();
    }

    public List<Application> applications() {
        return applications.all();
    }

    public List<Company> companies() {
        return companies.all();
    }

    public String emailOf(Person person) {
        return persons.emailOf(person);
    }

    public List<Intern> interns() {
        return persons.interns();
    }

    public List<CompanyRepresentative> representatives() {
        return persons.representatives();
    }

    public List<Coordinator> coordinators() {
        return persons.coordinators();
    }

    public void registerStudent(String name, Gender gender, LocalDate birthdate, String studentId, String section,
            String email, String password) {
        persons.registerStudent(name, gender, birthdate, studentId, section, email, password);
    }

    public Application endorse(Student student, Opening opening) {
        return applications.endorse(me(), student, opening);
    }

    public Company addCompany(String name) {
        return companies.create(name);
    }

    public void addRepresentative(String name, Gender gender, LocalDate birthdate, Company company, String email,
            String password) {
        persons.registerRepresentative(name, gender, birthdate, company, email, password);
    }

    public void addCoordinator(String name, Gender gender, LocalDate birthdate, String employeeId, String email,
            String password) {
        persons.registerCoordinator(name, gender, birthdate, employeeId, email, password);
    }
}
