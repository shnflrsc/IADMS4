package iadms4.service;

import java.time.LocalDate;
import java.util.List;

import iadms4.model.Account;
import iadms4.model.Company;
import iadms4.model.CompanyRepresentative;
import iadms4.model.Coordinator;
import iadms4.model.Gender;
import iadms4.model.Intern;
import iadms4.model.InternStatus;
import iadms4.model.Person;
import iadms4.model.Position;
import iadms4.model.Student;
import iadms4.repository.AccountRepository;
import iadms4.repository.ApplicationRepository;
import iadms4.repository.EvaluationRepository;
import iadms4.repository.PersonRepository;

/*
 * SERVICE. Business rules for people and their accounts.
 *
 * Responsibilities: creates people together with their accounts (coordinator, student, representative)
 * and applies the rules: required fields, unique email, unique student number. It also promotes a Student
 * to an Intern, edits and deletes people, and searches students.
 */
public class PersonService {
    // Storage of people.
    private final PersonRepository persons;
    // Storage of accounts.
    private final AccountRepository accounts;
    // Used to protect people who have application history.
    private final ApplicationRepository applications;
    // Used to protect representatives who wrote evaluations.
    private final EvaluationRepository evaluations;

    /*
     * Creates the service.
     *
     * It takes persons (the person repository), accounts (the account repository), applications (the
     * application repository) and evaluations (the evaluation repository).
     */
    public PersonService(PersonRepository persons, AccountRepository accounts, ApplicationRepository applications,
            EvaluationRepository evaluations) {
        this.persons = persons;
        this.accounts = accounts;
        this.applications = applications;
        this.evaluations = evaluations;
    }

    /*
     * Checks the details shared by every new account.
     *
     * It takes name (the full name), email (the login email) and password (the login password). It throws
     * IllegalArgumentException if the name is blank, the email is invalid or already used, or the password
     * is shorter than 4 characters.
     */
    private void validate(String name, String email, String password) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is required.");
        }
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException("A valid email is required.");
        }
        if (password == null || password.length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters.");
        }
        if (accounts.existsByEmail(email)) {
            throw new IllegalArgumentException("An account with that email already exists.");
        }
    }

    /*
     * Creates a coordinator together with a login account.
     *
     * It takes name (the full name), gender (the gender), birthdate (the date of birth), employeeId (the
     * school-issued employee number), email (the login email) and password (the login password). It returns
     * the new account (its person is the new coordinator). It throws IllegalArgumentException if any field
     * is invalid.
     */
    public Account registerCoordinator(String name, Gender gender, LocalDate birthdate, String employeeId,
            String email, String password) {
        validate(name, email, password);
        if (employeeId == null || employeeId.isBlank()) {
            throw new IllegalArgumentException("Employee ID is required.");
        }
        Coordinator c = new Coordinator(persons.nextId(), name.trim(), gender, birthdate, employeeId.trim());
        persons.save(c);
        return accounts.save(new Account(c, email.trim(), password));
    }

    /*
     * Creates a student together with a login account.
     *
     * It takes name (the full name), gender (the gender), birthdate (the date of birth), studentId (the
     * school-issued student number (must be unique)), section (the class section), email (the login email)
     * and password (the login password). It returns the new account (its person is the new student). It
     * throws IllegalArgumentException if any field is invalid or the student number is taken.
     */
    public Account registerStudent(String name, Gender gender, LocalDate birthdate, String studentId,
            String section, String email, String password) {
        validate(name, email, password);
        if (studentId == null || studentId.isBlank()) {
            throw new IllegalArgumentException("Student ID is required.");
        }
        if (persons.findStudents().stream().anyMatch(s -> s.getStudentId().equalsIgnoreCase(studentId.trim()))) {
            throw new IllegalArgumentException("A student with that student ID already exists.");
        }
        Student s = new Student(persons.nextId(), name.trim(), gender, birthdate, studentId.trim(),
                section == null ? "" : section.trim());
        persons.save(s);
        return accounts.save(new Account(s, email.trim(), password));
    }

    /*
     * Creates a company representative together with a login account.
     *
     * It takes name (the full name), gender (the gender), birthdate (the date of birth), company (the
     * company the person represents), email (the login email) and password (the login password). It returns
     * the new account (its person is the new representative). It throws IllegalArgumentException if any
     * field is invalid or no company is given.
     */
    public Account registerRepresentative(String name, Gender gender, LocalDate birthdate, Company company,
            String email, String password) {
        validate(name, email, password);
        if (company == null) {
            throw new IllegalArgumentException("A company is required.");
        }
        CompanyRepresentative r = new CompanyRepresentative(persons.nextId(), name.trim(), gender, birthdate, company);
        persons.save(r);
        return accounts.save(new Account(r, email.trim(), password));
    }

    /*
     * Replaces a Student with an Intern (same id) deployed to the position, re-pointing the account.
     *
     * It takes student (the student being promoted) and position (the position the new intern is deployed
     * to). It returns the new Intern.
     */
    public Intern promoteToIntern(Student student, Position position) {
        Intern intern = new Intern(student.getId(), student.getName(), student.getGender(), student.getBirthdate(),
                student.getStudentId(), student.getSection(), position, 0, InternStatus.ACTIVE);
        // Same id, so saving REPLACES the Student entry in the repository with the Intern.
        persons.save(intern);
        // Account.person is final, so build a new Account for the Intern with the same login details.
        accounts.findByPersonId(student.getId())
                .ifPresent(old -> accounts.save(new Account(intern, old.getEmail(), old.getPassword())));
        return intern;
    }

    /*
     * Edits the shared details of any person. The section only applies to students and interns.
     *
     * It takes person (the person to edit), name (the new name), gender (the new gender), birthdate (the
     * new birthdate) and section (the new section, or null to leave it unchanged). It throws
     * IllegalArgumentException if the name is blank.
     */
    public void updateDetails(Person person, String name, Gender gender, LocalDate birthdate, String section) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is required.");
        }
        person.setName(name.trim());
        person.setGender(gender);
        person.setBirthdate(birthdate);
        if (person instanceof Student student && section != null) {
            student.setSection(section.trim());
        }
    }

    /*
     * Deletes a person and their account. Blocked when other records depend on them: interns,
     * students with application history, representatives who wrote evaluations, coordinators who
     * endorsed students, yourself, and the last coordinator.
     *
     * It takes actor (the coordinator doing the deletion) and target (the person to delete). It throws
     * IllegalStateException if one of the protection rules above applies.
     */
    public void delete(Coordinator actor, Person target) {
        if (target.getId() == actor.getId()) {
            throw new IllegalStateException("You cannot delete your own account.");
        }
        if (target instanceof Intern) {
            throw new IllegalStateException("Interns cannot be deleted.");
        }
        if (target instanceof Student && !applications.findByStudentId(target.getId()).isEmpty()) {
            throw new IllegalStateException("This student has application history and cannot be deleted.");
        }
        if (target instanceof CompanyRepresentative
                && evaluations.findAll().stream().anyMatch(e -> e.getEvaluator().getId() == target.getId())) {
            throw new IllegalStateException("This representative wrote evaluations and cannot be deleted.");
        }
        if (target instanceof Coordinator) {
            if (applications.findAll().stream().anyMatch(a -> a.getCoordinator().getId() == target.getId())) {
                throw new IllegalStateException("This coordinator endorsed students and cannot be deleted.");
            }
            if (persons.findCoordinators().size() <= 1) {
                throw new IllegalStateException("The last coordinator cannot be deleted.");
            }
        }
        accounts.findByPersonId(target.getId()).ifPresent(accounts::remove);
        persons.remove(target.getId());
    }

    /*
     * Searches students and interns by keyword.
     *
     * It takes keyword (text to look for in the name, student number, section or email (blank = everyone)).
     * It returns the students whose details contain the keyword.
     */
    public List<Student> searchStudents(String keyword) {
        String k = keyword == null ? "" : keyword.trim().toLowerCase();
        return persons.findStudents().stream().filter(s -> k.isEmpty()
                || s.getName().toLowerCase().contains(k) || s.getStudentId().toLowerCase().contains(k)
                || s.getSection().toLowerCase().contains(k) || emailOf(s).toLowerCase().contains(k)).toList();
    }

    /*
     * Lists all interns with the given status.
     *
     * It takes status (the status to keep, or null for no filter). It returns the matching interns.
     */
    public List<Intern> interns(InternStatus status) {
        return persons.findInterns().stream().filter(i -> status == null || i.getStatus() == status).toList();
    }

    /*
     * Gets the login email of a person.
     *
     * It takes person (the person). It returns the email, or "-" if the person has no account.
     */
    public String emailOf(Person person) {
        return accounts.findByPersonId(person.getId()).map(Account::getEmail).orElse("-");
    }

    /*
     * Lists all students (interns included).
     *
     * It returns the students.
     */
    public List<Student> students() {
        return persons.findStudents();
    }

    /*
     * Lists all interns.
     *
     * It returns the interns.
     */
    public List<Intern> interns() {
        return persons.findInterns();
    }

    /*
     * Lists all coordinators.
     *
     * It returns the coordinators.
     */
    public List<Coordinator> coordinators() {
        return persons.findCoordinators();
    }

    /*
     * Lists all company representatives.
     *
     * It returns the representatives of every company.
     */
    public List<CompanyRepresentative> representatives() {
        return persons.findRepresentatives();
    }

    /*
     * Lists the representatives of one company.
     *
     * It takes company (the company). It returns the representatives who work for it.
     */
    public List<CompanyRepresentative> representatives(Company company) {
        return persons.findRepresentatives(company);
    }
}
