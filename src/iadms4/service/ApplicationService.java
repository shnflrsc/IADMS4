package iadms4.service;

import java.util.ArrayList;
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
 * A coordinator endorses a student, then the representative approves (the student becomes an Intern)
 * or rejects, or a coordinator withdraws. Rejected or withdrawn students can be endorsed again.
 */
public class ApplicationService {
    private final ApplicationRepository applications;
    private final PersonRepository persons;
    private final PersonService personService;

    // Creates the service using the given repositories and personService, which promotes students.
    public ApplicationService(ApplicationRepository applications, PersonRepository persons,
            PersonService personService) {
        this.applications = applications;
        this.persons = persons;
        this.personService = personService;
    }

    // Saves a new PENDING application of the student to the opening, endorsed by the coordinator.
    public void endorse(Coordinator coordinator, Student student, Opening opening) {
        // Read the student again because they may have been promoted to an Intern since being listed.
        Person current = persons.findById(student.getId());
        if (current instanceof Intern) {
            throw new IllegalStateException(student.getName() + " is already deployed as an intern.");
        }
        if (opening.getStatus() != OpeningStatus.OPEN) {
            throw new IllegalStateException("That opening is closed.");
        }
        for (Application application : applications.findByStudentId(student.getId())) {
            if (application.getOpening().getId() == opening.getId()
                    && application.getStatus() == ApplicationStatus.PENDING) {
                throw new IllegalStateException("This student already has a pending application for that opening.");
            }
        }
        applications.save(new Application(applications.nextId(), student, opening, coordinator,
                ApplicationStatus.PENDING));
    }

    // Approves the application, turns the student into an Intern and returns the new intern.
    public Intern approve(Application application) {
        // Step 1: the application must still be pending and the opening must still be open.
        requirePending(application);
        if (application.getOpening().getStatus() != OpeningStatus.OPEN) {
            throw new IllegalStateException("The opening is closed; cannot approve.");
        }
        Person current = persons.findById(application.getStudent().getId());
        if (current instanceof Intern) {
            throw new IllegalStateException("The student is already deployed as an intern.");
        }
        // Step 2: mark it approved, then swap the Student for an Intern.
        application.setStatus(ApplicationStatus.APPROVED);
        Intern intern = personService.promoteToIntern((Student) current, application.getOpening().getPosition());
        // Step 3: the student is now deployed, so their other pending applications are rejected.
        for (Application other : applications.findByStudentId(intern.getId())) {
            if (other.getStatus() == ApplicationStatus.PENDING) {
                other.setStatus(ApplicationStatus.REJECTED);
            }
        }
        // Step 4: the opening has been filled, so it is closed.
        application.getOpening().setStatus(OpeningStatus.CLOSED);
        return intern;
    }

    // Rejects the given pending application.
    public void reject(Application application) {
        requirePending(application);
        application.setStatus(ApplicationStatus.REJECTED);
    }

    // Withdraws the given pending application. The student may be endorsed again later.
    public void withdraw(Application application) {
        requirePending(application);
        application.setStatus(ApplicationStatus.WITHDRAWN);
    }

    // Throws an exception if the given application is no longer PENDING.
    private void requirePending(Application application) {
        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new IllegalStateException("Application is already " + application.getStatus() + ".");
        }
    }

    // Returns the items of the given list that have the given status, or all of them if status is null.
    private List<Application> withStatus(List<Application> list, ApplicationStatus status) {
        List<Application> matches = new ArrayList<>();
        for (Application application : list) {
            if (status == null || application.getStatus() == status) {
                matches.add(application);
            }
        }
        return matches;
    }

    // Returns every application.
    public List<Application> all() {
        return applications.findAll();
    }

    // Returns the applications with the given status, or all if status is null.
    public List<Application> all(ApplicationStatus status) {
        return withStatus(applications.findAll(), status);
    }

    // Returns the applications made to the given company.
    public List<Application> forCompany(Company company) {
        return applications.findByCompany(company);
    }

    // Returns the applications made to the given company with the given status, or all if status is null.
    public List<Application> forCompany(Company company, ApplicationStatus status) {
        return withStatus(applications.findByCompany(company), status);
    }

    // Returns the applications of the student with the given id.
    public List<Application> forStudent(int studentId) {
        return applications.findByStudentId(studentId);
    }

    // Returns the PENDING applications of the given company.
    public List<Application> pendingForCompany(Company company) {
        return withStatus(applications.findByCompany(company), ApplicationStatus.PENDING);
    }
}
