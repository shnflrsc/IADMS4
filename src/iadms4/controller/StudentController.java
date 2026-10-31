package iadms4.controller;

import java.util.List;

import iadms4.model.Application;
import iadms4.model.Session;
import iadms4.model.Student;
import iadms4.service.ApplicationService;
import iadms4.service.PersonService;

/*
 * CONTROLLER. student dashboard stuff, read only
 */
public class StudentController {
    private final ApplicationService applications;
    private final PersonService persons;

    // makes the controller, takes applications, persons
    public StudentController(ApplicationService applications, PersonService persons) {
        this.applications = applications;
        this.persons = persons;
    }

    // gets the logged in student from Session, cast is safe because only students get here
    public Student me() {
        return (Student) Session.getInstance().getPerson();
    }

    // gets the login email of the logged in student
    public String email() {
        return persons.emailOf(me());
    }

    // gets the applications of the logged in student
    public List<Application> myApplications() {
        return applications.forStudent(me().getId());
    }
}
