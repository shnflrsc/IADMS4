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
 * CONTROLLER. everything the coordinator dashboard does
 * basically 1 method per menu item, this class is long sorry
 */
public class CoordinatorController {
    private final PersonService persons;
    private final CompanyService companies;
    private final OpeningService openings;
    private final ApplicationService applications;
    private final EvaluationService evaluations;
    private final AuthService auth;
    private final ReportService reports;

    // constructor, just saves the services
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

    // the coordinator thats logged in
    private Coordinator me() {
        return (Coordinator) Session.getInstance().getPerson();
    }

    // all students (interns included)
    public List<Student> students() {
        return persons.students();
    }

    // search students n interns
    public List<Student> searchStudents(String keyword) {
        return persons.searchStudents(keyword);
    }

    // register student + account
    public void registerStudent(String name, Gender gender, LocalDate birthdate, String studentId, String section,
            String email, String password) {
        persons.registerStudent(name, gender, birthdate, studentId, section, email, password);
    }

    // all interns
    public List<Intern> interns() {
        return persons.interns();
    }

    // interns by status, null = all
    public List<Intern> interns(InternStatus status) {
        return persons.interns(status);
    }

    // all evaluations
    public List<Evaluation> evaluations() {
        return evaluations.all();
    }

    // all openings
    public List<Opening> allOpenings() {
        return openings.allOpenings();
    }

    // open openings only (yes "open openings" lol)
    public List<Opening> openOpenings() {
        return openings.openOpenings();
    }

    // endorse student to opening
    public void endorse(Student student, Opening opening) {
        applications.endorse(me(), student, opening);
    }

    // all applications
    public List<Application> applications() {
        return applications.all();
    }

    // applications by status, null = all
    public List<Application> applications(ApplicationStatus status) {
        return applications.all(status);
    }

    // pending ones
    public List<Application> pendingApplications() {
        return applications.all(ApplicationStatus.PENDING);
    }

    // withdraw it
    public void withdraw(Application application) {
        applications.withdraw(application);
    }

    // all companies
    public List<Company> companies() {
        return companies.all();
    }

    // add company
    public void addCompany(String name) {
        companies.create(name);
    }

    // rename company
    public void renameCompany(Company company, String name) {
        companies.rename(company, name);
    }

    // delete company
    public void deleteCompany(Company company) {
        companies.delete(company);
    }

    // all reps
    public List<CompanyRepresentative> representatives() {
        return persons.representatives();
    }

    // all coordinators
    public List<Coordinator> coordinators() {
        return persons.coordinators();
    }

    // everyone w/ a login. students first, then reps, then coordinators
    public List<Person> everyone() {
        List<Person> all = new ArrayList<>();
        all.addAll(persons.students());
        all.addAll(persons.representatives());
        all.addAll(persons.coordinators());
        return all;
    }

    // add rep account
    public void addRepresentative(String name, Gender gender, LocalDate birthdate, Company company, String email,
            String password) {
        persons.registerRepresentative(name, gender, birthdate, company, email, password);
    }

    // add coordinator account
    public void addCoordinator(String name, Gender gender, LocalDate birthdate, String employeeId, String email,
            String password) {
        persons.registerCoordinator(name, gender, birthdate, employeeId, email, password);
    }

    // edit person, section is for students only
    public void updatePerson(Person person, String name, Gender gender, LocalDate birthdate, String section) {
        persons.updateDetails(person, name, gender, birthdate, section);
    }

    // delete person, me() is whoever is deleting
    public void deletePerson(Person person) {
        persons.delete(me(), person);
    }

    // reset someones password
    public void resetPassword(Person person, String newPassword) {
        auth.resetPassword(person, newPassword);
    }

    // their email or "-" if none
    public String emailOf(Person person) {
        return persons.emailOf(person);
    }

    // report for the whole system
    public Summary report() {
        return reports.system();
    }

    // one report per company, same order as companies() dont mess this up
    public List<Summary> reportPerCompany() {
        return reports.perCompany();
    }
}
