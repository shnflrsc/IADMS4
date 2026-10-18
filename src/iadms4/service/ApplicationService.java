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

/**
 * SERVICE. The endorsement workflow. endorse = a coordinator endorses a student; approve = the
 * representative accepts (student becomes an Intern); reject = the representative declines;
 * withdraw = a coordinator withdraws a pending endorsement. Rejected or withdrawn students can be endorsed again.
 * Business rules (no duplicates, opening must be open, ...) live here.
 */
public class ApplicationService {
    private final ApplicationRepository applications;
    private final PersonRepository persons;
    private final PersonService personService;

    public ApplicationService(ApplicationRepository applications, PersonRepository persons,
            PersonService personService) {
        this.applications = applications;
        this.persons = persons;
        this.personService = personService;
    }

    /** A coordinator endorses a student to an opening. */
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

    /** Approves the application and promotes the student to an intern. */
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
        return intern;
    }

    public void reject(Application application) {
        requirePending(application);
        application.setStatus(ApplicationStatus.REJECTED);
    }

    /** A coordinator withdraws a student from a PENDING application. The student may be endorsed again later. */
    public void withdraw(Application application) {
        requirePending(application);
        application.setStatus(ApplicationStatus.WITHDRAWN);
    }

    private void requirePending(Application application) {
        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new IllegalStateException("Application is already " + application.getStatus() + ".");
        }
    }

    public List<Application> all() {
        return applications.findAll();
    }

    /** All applications with the given status; a null status means no filter. */
    public List<Application> all(ApplicationStatus status) {
        return applications.findAll().stream().filter(a -> status == null || a.getStatus() == status).toList();
    }

    public List<Application> forCompany(Company company) {
        return applications.findByCompany(company);
    }

    public List<Application> forCompany(Company company, ApplicationStatus status) {
        return applications.findByCompany(company).stream()
                .filter(a -> status == null || a.getStatus() == status).toList();
    }

    public List<Application> forStudent(int studentId) {
        return applications.findByStudentId(studentId);
    }

    public List<Application> pendingForCompany(Company company) {
        return applications.findByCompany(company).stream()
                .filter(a -> a.getStatus() == ApplicationStatus.PENDING).toList();
    }
}
