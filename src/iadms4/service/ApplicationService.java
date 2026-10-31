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
 * SERVICE. the endorsement flow
 * endorse then approve (student becomes intern) or reject or withdraw
 * rejected/withdrawn students can be endorsed again
 */
public class ApplicationService {
    private final ApplicationRepository applications;
    private final PersonRepository persons;
    private final PersonService personService;

    // makes the service, takes applications, persons, personService
    public ApplicationService(ApplicationRepository applications, PersonRepository persons,
            PersonService personService) {
        this.applications = applications;
        this.persons = persons;
        this.personService = personService;
    }

    // coordinator endorses student, saved as PENDING
    public void endorse(Coordinator coordinator, Student student, Opening opening) {
        // re-read because they might be an intern already by now
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

    // approve. student becomes intern. this one does a lot
    public Intern approve(Application application) {
        // step 1: still pending? still open?
        requirePending(application);
        if (application.getOpening().getStatus() != OpeningStatus.OPEN) {
            throw new IllegalStateException("The opening is closed; cannot approve.");
        }
        Person current = persons.findById(application.getStudent().getId());
        if (current instanceof Intern) {
            throw new IllegalStateException("The student is already deployed as an intern.");
        }
        // step 2: approve, swap student for intern
        application.setStatus(ApplicationStatus.APPROVED);
        Intern intern = personService.promoteToIntern((Student) current, application.getOpening().getPosition());
        // step 3: reject their other pending applications
        for (Application other : applications.findByStudentId(intern.getId())) {
            if (other.getStatus() == ApplicationStatus.PENDING) {
                other.setStatus(ApplicationStatus.REJECTED);
            }
        }
        // step 4: opening is filled so close it
        application.getOpening().setStatus(OpeningStatus.CLOSED);
        return intern;
    }

    // representative rejects a pending application, student can be endorsed again
    public void reject(Application application) {
        requirePending(application);
        application.setStatus(ApplicationStatus.REJECTED);
    }

    // withdraw, they can get endorsed again later
    public void withdraw(Application application) {
        requirePending(application);
        application.setStatus(ApplicationStatus.WITHDRAWN);
    }

    // throws an error if the application isn't PENDING anymore
    private void requirePending(Application application) {
        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new IllegalStateException("Application is already " + application.getStatus() + ".");
        }
    }

    // filter by status, null means all
    private List<Application> withStatus(List<Application> list, ApplicationStatus status) {
        List<Application> matches = new ArrayList<>();
        for (Application application : list) {
            if (status == null || application.getStatus() == status) {
                matches.add(application);
            }
        }
        return matches;
    }

    // gets every application in the system
    public List<Application> all() {
        return applications.findAll();
    }

    // applications by status, null means all
    public List<Application> all(ApplicationStatus status) {
        return withStatus(applications.findAll(), status);
    }

    // gets all the applications sent to this company
    public List<Application> forCompany(Company company) {
        return applications.findByCompany(company);
    }

    // applications of company by status, null means all
    public List<Application> forCompany(Company company, ApplicationStatus status) {
        return withStatus(applications.findByCompany(company), status);
    }

    // gets all the applications of the student with this id
    public List<Application> forStudent(int studentId) {
        return applications.findByStudentId(studentId);
    }

    // gets the applications this company still has to decide on
    public List<Application> pendingForCompany(Company company) {
        return withStatus(applications.findByCompany(company), ApplicationStatus.PENDING);
    }
}
