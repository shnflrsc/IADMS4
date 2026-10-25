package iadms4.service;

import java.util.List;

import iadms4.model.Application;
import iadms4.model.ApplicationStatus;
import iadms4.model.Company;
import iadms4.model.Coordinator;
import iadms4.model.Intern;
import iadms4.model.Opening;
import iadms4.model.OpeningStatus;
import iadms4.model.Person;
import iadms4.model.Student;
import iadms4.repository.ApplicationRepository;
import iadms4.repository.PersonRepository;

/*
 * SERVICE. The endorsement workflow of the system.
 *
 * Responsibilities: endorse = a coordinator endorses a student; approve = the representative accepts
 * (the student becomes an Intern); reject = the representative declines; withdraw = a coordinator
 * withdraws a pending endorsement. Rejected or withdrawn students can be endorsed again.
 * Business rules (no duplicates, the opening must be open, ...) live here.
 */
public class ApplicationService {
    // Storage of applications.
    private final ApplicationRepository applications;
    // Used to re-read the current version of a student.
    private final PersonRepository persons;
    // Used to promote a student to an intern.
    private final PersonService personService;

    /*
     * Creates the service.
     *
     * It takes applications (the application repository), persons (the person repository) and personService
     * (the service that knows how to promote a student).
     */
    public ApplicationService(ApplicationRepository applications, PersonRepository persons,
            PersonService personService) {
        this.applications = applications;
        this.persons = persons;
        this.personService = personService;
    }

    /*
     * A coordinator endorses a student to an opening. The new application starts as PENDING.
     *
     * It takes coordinator (the coordinator making the endorsement), student (the student being endorsed)
     * and opening (the opening the student is endorsed to). It returns the saved application. It throws
     * IllegalStateException if the student is already an intern, the opening is closed, or the student
     * already has a pending application for it.
     */
    public Application endorse(Coordinator coordinator, Student student, Opening opening) {
        // Re-read the student from the repository: they may have been promoted to an Intern since the
        // Student object was listed, and an Intern must not be endorsed again.
        Person current = persons.findById(student.getId()).orElse(student);
        if (current instanceof Intern) {
            throw new IllegalStateException(student.getName() + " is already deployed as an intern.");
        }
        if (opening.getStatus() != OpeningStatus.OPEN) {
            throw new IllegalStateException("That opening is closed.");
        }
        boolean duplicate = applications.findByStudentId(student.getId()).stream()
                .anyMatch(a -> a.getOpening().getId() == opening.getId() && a.getStatus() == ApplicationStatus.PENDING);
        if (duplicate) {
            throw new IllegalStateException("This student already has a pending application for that opening.");
        }
        return applications.save(new Application(applications.nextId(), student, opening, coordinator,
                ApplicationStatus.PENDING));
    }

    /*
     * Approves the application and promotes the student to an intern. The student's other pending
     * applications are rejected automatically, and the opening is closed because it has been filled.
     *
     * It takes application (the application to approve). It returns the new Intern who replaced the
     * Student. It throws IllegalStateException if the application is not pending, the opening is closed, or
     * the student is already an intern.
     */
    public Intern approve(Application application) {
        // Step 1: guard rules - must still be pending and the opening must still be open.
        requirePending(application);
        if (application.getOpening().getStatus() != OpeningStatus.OPEN) {
            throw new IllegalStateException("The opening is closed; cannot approve.");
        }
        Person current = persons.findById(application.getStudent().getId()).orElse(application.getStudent());
        if (current instanceof Intern) {
            throw new IllegalStateException("The student is already deployed as an intern.");
        }
        // Step 2: mark approved, then swap the Student for an Intern (same id, account re-pointed).
        application.setStatus(ApplicationStatus.APPROVED);
        Intern intern = personService.promoteToIntern((Student) current, application.getOpening().getPosition());
        // Step 3: the student is now deployed, so their other pending applications are rejected.
        for (Application other : applications.findByStudentId(intern.getId())) {
            if (other.getStatus() == ApplicationStatus.PENDING) {
                other.setStatus(ApplicationStatus.REJECTED);
            }
        }
        // Step 4: the opening has been filled, so it is closed automatically.
        application.getOpening().setStatus(OpeningStatus.CLOSED);
        return intern;
    }

    /*
     * The company representative declines a pending application.
     *
     * It takes application (the application to reject). It throws IllegalStateException if the application
     * is no longer pending.
     */
    public void reject(Application application) {
        requirePending(application);
        application.setStatus(ApplicationStatus.REJECTED);
    }

    /*
     * A coordinator withdraws a student from a PENDING application. The student may be endorsed again later.
     *
     * It takes application (the application to withdraw). It throws IllegalStateException if the
     * application is no longer pending.
     */
    public void withdraw(Application application) {
        requirePending(application);
        application.setStatus(ApplicationStatus.WITHDRAWN);
    }

    /*
     * Makes sure an application is still PENDING before it is changed.
     *
     * It takes application (the application to check). It throws IllegalStateException if the application
     * already has a final status.
     */
    private void requirePending(Application application) {
        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new IllegalStateException("Application is already " + application.getStatus() + ".");
        }
    }

    /*
     * Lists every application.
     *
     * It returns all applications.
     */
    public List<Application> all() {
        return applications.findAll();
    }

    /*
     * Lists all applications with the given status.
     *
     * It takes status (the status to keep, or null for no filter). It returns the matching applications.
     */
    public List<Application> all(ApplicationStatus status) {
        return applications.findAll().stream().filter(a -> status == null || a.getStatus() == status).toList();
    }

    /*
     * Lists the applications made to a company.
     *
     * It takes company (the company). It returns that company's applications.
     */
    public List<Application> forCompany(Company company) {
        return applications.findByCompany(company);
    }

    /*
     * Lists the applications made to a company, filtered by status.
     *
     * It takes company (the company) and status (the status to keep, or null for no filter). It returns the
     * matching applications.
     */
    public List<Application> forCompany(Company company, ApplicationStatus status) {
        return applications.findByCompany(company).stream()
                .filter(a -> status == null || a.getStatus() == status).toList();
    }

    /*
     * Lists the applications of one student.
     *
     * It takes studentId (the id of the student). It returns that student's applications.
     */
    public List<Application> forStudent(int studentId) {
        return applications.findByStudentId(studentId);
    }

    /*
     * Lists a company's applications that still wait for a decision.
     *
     * It takes company (the company). It returns that company's PENDING applications.
     */
    public List<Application> pendingForCompany(Company company) {
        return applications.findByCompany(company).stream()
                .filter(a -> a.getStatus() == ApplicationStatus.PENDING).toList();
    }
}
