package iadms4.model;

import java.time.LocalDate;

/*
 * MODEL. College faculty member who manages the internship program. It extends Person.
 *
 * Responsibilities: stores the employee id. Coordinators register students, endorse them to openings,
 * create company, representative and coordinator accounts, and view evaluations.
 */
public class Coordinator extends Person {
    // Employee number issued by the school.
    private final String employeeId;

    /*
     * Creates a coordinator.
     *
     * It takes id (the unique person id), name (the full name), gender (the gender), birthdate (the date of
     * birth) and employeeId (the school-issued employee number).
     */
    public Coordinator(int id, String name, Gender gender, LocalDate birthdate, String employeeId) {
        super(id, name, gender, birthdate);
        this.employeeId = employeeId;
    }

    /*
     * Gets the employee number.
     *
     * It returns the employee id.
     */
    public String getEmployeeId() {
        return employeeId;
    }
}
