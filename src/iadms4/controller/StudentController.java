package iadms4.controller;

import java.util.List;

import iadms4.model.Application;
import iadms4.model.Session;
import iadms4.model.Student;
import iadms4.service.ApplicationService;
import iadms4.service.PersonService;

/**
 * CONTROLLER. Read-only actions of the Student dashboard: own profile and own applications.
 */
public class StudentController {
    private final ApplicationService applications;
    private final PersonService persons;

    public StudentController(ApplicationService applications, PersonService persons) {
        this.applications = applications;
        this.persons = persons;
    }

    public Student me() {
        return (Student) Session.getInstance().getPerson();
    }

    public String email() {
        return persons.emailOf(me());
    }

    public List<Application> myApplications() {
        return applications.forStudent(me().getId());
    }
}
