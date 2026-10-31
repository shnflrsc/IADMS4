package iadms4.model;

import java.time.LocalDate;

/*
 * MODEL. faculty who runs the internship program
 * just Person + employee number
 */
public class Coordinator extends Person {
    private final String employeeId;

    // makes a Coordinator, takes id, name, gender, birthdate, employeeId
    public Coordinator(int id, String name, Gender gender, LocalDate birthdate, String employeeId) {
        super(id, name, gender, birthdate);
        this.employeeId = employeeId;
    }

    // gets employee id (String)
    public String getEmployeeId() {
        return employeeId;
    }
}
