package iadms4.controller;

import java.util.List;

import iadms4.model.Application;
import iadms4.model.Session;
import iadms4.model.Student;
import iadms4.service.ApplicationService;
import iadms4.service.PersonService;

/*
 * CONTROLLER. Handles the read-only actions of the Student dashboard.
 *
 * Responsibilities: gives the student access to their own profile, email and applications.
 * The student is the logged-in person taken from Session.
 */
public class StudentController {
    // Service used to list the student's applications.
    private final ApplicationService applications;
    // Service used to look up the student's email.
    private final PersonService persons;

    /*
     * Creates the controller.
     *
     * It takes applications (the application service) and persons (the person service).
     */
    public StudentController(ApplicationService applications, PersonService persons) {
        this.applications = applications;
        this.persons = persons;
    }

    /*
     * Gets the logged-in student.
     *
     * It returns the current person cast to Student.
     */
    public Student me() {
        return (Student) Session.getInstance().getPerson();
    }

    /*
     * Gets the student's login email.
     *
     * It returns the email.
     */
    public String email() {
        return persons.emailOf(me());
    }

    /*
     * Lists the logged-in student's own applications.
     *
     * It returns the student's applications.
     */
    public List<Application> myApplications() {
        return applications.forStudent(me().getId());
    }
}
