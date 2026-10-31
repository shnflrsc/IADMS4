package iadms4.model;

import java.time.LocalDate;

/*
 * MODEL. faculty who runs the internship program
 * just Person and employee number
 */
public class Coordinator extends Person {
    private final String employeeId;

    // makes a Coordinator, takes id, name, gender, birthdate, employeeId
    public Coordinator(int id, String name, Gender gender, LocalDate birthdate, String employeeId) {
        super(id, name, gender, birthdate);
        this.employeeId = employeeId;
    }

    // gets the employee number from the school (String)
    public String getEmployeeId() {
        return employeeId;
    }
}
