package iadms4.model;

import java.time.LocalDate;

/**
 * MODEL. College faculty who manages internships: registers students, endorses them to openings,
 * provisions company, representative and coordinator accounts, and views evaluations.
 */
public class Coordinator extends Person {
    private final String employeeId;

    public Coordinator(int id, String name, Gender gender, LocalDate birthdate, String employeeId) {
        super(id, name, gender, birthdate);
        this.employeeId = employeeId;
    }

    public String getEmployeeId() {
        return employeeId;
    }
}
