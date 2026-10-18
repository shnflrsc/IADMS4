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

/**
 * SERVICE. Creates people and their accounts (coordinator, student, representative) and applies the
 * rules: required fields, unique email, unique student number. Also promotes a Student to an Intern,
 * edits and deletes people, and searches students.
 */
public class PersonService {
    private final PersonRepository persons;
    private final AccountRepository accounts;
    private final ApplicationRepository applications;
    private final EvaluationRepository evaluations;

    public PersonService(PersonRepository persons, AccountRepository accounts, ApplicationRepository applications,
            EvaluationRepository evaluations) {
        this.persons = persons;
        this.accounts = accounts;
        this.applications = applications;
        this.evaluations = evaluations;
    }

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

    /** Replaces a Student with an Intern (same id) deployed to the position, re-pointing the account. */
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

    /** Edits the shared details of any person; section only applies to students and interns. */
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

    /**
     * Deletes a person and their account. Blocked when other records depend on them: interns,
     * students with application history, representatives who wrote evaluations, coordinators who
     * endorsed students, yourself, and the last coordinator.
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

    /** Students and interns whose name, student number, section or email contains the keyword. */
    public List<Student> searchStudents(String keyword) {
        String k = keyword == null ? "" : keyword.trim().toLowerCase();
        return persons.findStudents().stream().filter(s -> k.isEmpty()
                || s.getName().toLowerCase().contains(k) || s.getStudentId().toLowerCase().contains(k)
                || s.getSection().toLowerCase().contains(k) || emailOf(s).toLowerCase().contains(k)).toList();
    }

    /** All interns with the given status; a null status means no filter. */
    public List<Intern> interns(InternStatus status) {
        return persons.findInterns().stream().filter(i -> status == null || i.getStatus() == status).toList();
    }

    public String emailOf(Person person) {
        return accounts.findByPersonId(person.getId()).map(Account::getEmail).orElse("-");
    }

    public List<Student> students() {
        return persons.findStudents();
    }

    public List<Intern> interns() {
        return persons.findInterns();
    }

    public List<Coordinator> coordinators() {
        return persons.findCoordinators();
    }

    public List<CompanyRepresentative> representatives() {
        return persons.findRepresentatives();
    }

    public List<CompanyRepresentative> representatives(Company company) {
        return persons.findRepresentatives(company);
    }
}
