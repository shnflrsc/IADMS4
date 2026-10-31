package iadms4.controller;

import java.util.List;

import iadms4.model.Application;
import iadms4.model.Session;
import iadms4.model.Student;
import iadms4.service.ApplicationService;
import iadms4.service.PersonService;

/*
 * CONTROLLER. Handles the read-only actions of the Student dashboard.
 */
public class StudentController {
    private final ApplicationService applications;
    private final PersonService persons;

    // Creates the controller using the given services.
    public StudentController(ApplicationService applications, PersonService persons) {
        this.applications = applications;
        this.persons = persons;
    }

    // Returns the logged-in student.
    public Student me() {
        return (Student) Session.getInstance().getPerson();
    }

    // Returns the student's login email.
    public String email() {
        return persons.emailOf(me());
    }

    // Returns the logged-in student's own applications.
    public List<Application> myApplications() {
        return applications.forStudent(me().getId());
    }
}
