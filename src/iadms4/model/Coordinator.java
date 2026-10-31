package iadms4.model;

import java.time.LocalDate;

/*
 * MODEL. College faculty member who manages the internship program.
 * It adds an employee number to Person.
 */
public class Coordinator extends Person {
    private final String employeeId;

    // Creates a coordinator from the Person details plus an employeeId.
    public Coordinator(int id, String name, Gender gender, LocalDate birthdate, String employeeId) {
        super(id, name, gender, birthdate);
        this.employeeId = employeeId;
    }

    // Returns the school-issued employee number.
    public String getEmployeeId() {
        return employeeId;
    }
}
