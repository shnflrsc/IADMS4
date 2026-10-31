package iadms4.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import iadms4.model.Account;
import iadms4.model.Application;
import iadms4.model.Company;
import iadms4.model.CompanyRepresentative;
import iadms4.model.Coordinator;
import iadms4.model.Evaluation;
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
 * It creates people with their accounts, promotes students to interns, edits and deletes people,
 * and searches students.
 */
public class PersonService {
    private final PersonRepository persons;
    private final AccountRepository accounts;
    private final ApplicationRepository applications;
    private final EvaluationRepository evaluations;

    // Creates the service using the given repositories.
    public PersonService(PersonRepository persons, AccountRepository accounts, ApplicationRepository applications,
            EvaluationRepository evaluations) {
        this.persons = persons;
        this.accounts = accounts;
        this.applications = applications;
        this.evaluations = evaluations;
    }

    // Checks the name, email and password shared by every new account and throws an exception if one is invalid.
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

    // Creates a coordinator with a login account and returns the account.
    public Account registerCoordinator(String name, Gender gender, LocalDate birthdate, String employeeId,
            String email, String password) {
        validate(name, email, password);
        if (employeeId == null || employeeId.isBlank()) {
            throw new IllegalArgumentException("Employee ID is required.");
        }
        Coordinator coordinator = new Coordinator(persons.nextId(), name.trim(), gender, birthdate, employeeId.trim());
        persons.save(coordinator);
        return accounts.save(new Account(coordinator, email.trim(), password));
    }

    // Creates a student with a login account. The studentId must be unique.
    public void registerStudent(String name, Gender gender, LocalDate birthdate, String studentId, String section,
            String email, String password) {
        validate(name, email, password);
        if (studentId == null || studentId.isBlank()) {
            throw new IllegalArgumentException("Student ID is required.");
        }
        for (Student other : persons.findStudents()) {
            if (other.getStudentId().equalsIgnoreCase(studentId.trim())) {
                throw new IllegalArgumentException("A student with that student ID already exists.");
            }
        }
        String cleanSection = "";
        if (section != null) {
            cleanSection = section.trim();
        }
        Student student = new Student(persons.nextId(), name.trim(), gender, birthdate, studentId.trim(), cleanSection);
        persons.save(student);
        accounts.save(new Account(student, email.trim(), password));
    }

    // Creates a company representative for the given company with a login account.
    public void registerRepresentative(String name, Gender gender, LocalDate birthdate, Company company,
            String email, String password) {
        validate(name, email, password);
        if (company == null) {
            throw new IllegalArgumentException("A company is required.");
        }
        CompanyRepresentative representative = new CompanyRepresentative(persons.nextId(), name.trim(), gender,
                birthdate, company);
        persons.save(representative);
        accounts.save(new Account(representative, email.trim(), password));
    }

    // Replaces the given student with an Intern deployed to the position, and returns the new intern.
    public Intern promoteToIntern(Student student, Position position) {
        Intern intern = new Intern(student.getId(), student.getName(), student.getGender(), student.getBirthdate(),
                student.getStudentId(), student.getSection(), position, 0, InternStatus.ACTIVE);
        // The id is the same, so saving replaces the Student in the repository.
        persons.save(intern);
        // Account.person is final, so a new Account is saved with the same email and password.
        Account old = accounts.findByPersonId(student.getId());
        if (old != null) {
            accounts.save(new Account(intern, old.getEmail(), old.getPassword()));
        }
        return intern;
    }

    // Edits the given person. The section only applies to students and interns; null leaves it unchanged.
    public void updateDetails(Person person, String name, Gender gender, LocalDate birthdate, String section) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is required.");
        }
        person.setName(name.trim());
        person.setGender(gender);
        person.setBirthdate(birthdate);
        if (person instanceof Student && section != null) {
            ((Student) person).setSection(section.trim());
        }
    }

    // Deletes target and their account for the coordinator actor, unless other records depend on them.
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
        if (target instanceof CompanyRepresentative && wroteEvaluations(target)) {
            throw new IllegalStateException("This representative wrote evaluations and cannot be deleted.");
        }
        if (target instanceof Coordinator) {
            if (endorsedStudents(target)) {
                throw new IllegalStateException("This coordinator endorsed students and cannot be deleted.");
            }
            if (persons.findCoordinators().size() <= 1) {
                throw new IllegalStateException("The last coordinator cannot be deleted.");
            }
        }
        Account account = accounts.findByPersonId(target.getId());
        if (account != null) {
            accounts.remove(account);
        }
        persons.remove(target.getId());
    }

    // Returns true if the given person wrote at least one evaluation.
    private boolean wroteEvaluations(Person person) {
        for (Evaluation evaluation : evaluations.findAll()) {
            if (evaluation.getEvaluator().getId() == person.getId()) {
                return true;
            }
        }
        return false;
    }

    // Returns true if the given person endorsed at least one student.
    private boolean endorsedStudents(Person person) {
        for (Application application : applications.findAll()) {
            if (application.getCoordinator().getId() == person.getId()) {
                return true;
            }
        }
        return false;
    }

    // Returns the students whose name, student number, section or email contains keyword (blank = everyone).
    public List<Student> searchStudents(String keyword) {
        String text = keyword.trim().toLowerCase();
        List<Student> matches = new ArrayList<>();
        for (Student student : persons.findStudents()) {
            if (student.getName().toLowerCase().contains(text)
                    || student.getStudentId().toLowerCase().contains(text)
                    || student.getSection().toLowerCase().contains(text)
                    || emailOf(student).toLowerCase().contains(text)) {
                matches.add(student);
            }
        }
        return matches;
    }

    // Returns the interns with the given status, or all interns if status is null.
    public List<Intern> interns(InternStatus status) {
        List<Intern> matches = new ArrayList<>();
        for (Intern intern : persons.findInterns()) {
            if (status == null || intern.getStatus() == status) {
                matches.add(intern);
            }
        }
        return matches;
    }

    // Returns the login email of the given person, or "-" if they have no account.
    public String emailOf(Person person) {
        Account account = accounts.findByPersonId(person.getId());
        if (account == null) {
            return "-";
        }
        return account.getEmail();
    }

    // Returns all students, including interns.
    public List<Student> students() {
        return persons.findStudents();
    }

    // Returns all interns.
    public List<Intern> interns() {
        return persons.findInterns();
    }

    // Returns all coordinators.
    public List<Coordinator> coordinators() {
        return persons.findCoordinators();
    }

    // Returns the representatives of every company.
    public List<CompanyRepresentative> representatives() {
        return persons.findRepresentatives();
    }
}
