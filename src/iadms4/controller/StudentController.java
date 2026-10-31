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

    // the student thats logged in
    public Student me() {
        return (Student) Session.getInstance().getPerson();
    }

    // my email
    public String email() {
        return persons.emailOf(me());
    }

    // my applications
    public List<Application> myApplications() {
        return applications.forStudent(me().getId());
    }
}
