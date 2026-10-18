package iadms4.model;

import java.time.LocalDate;

/**
 * MODEL. A student. Coordinators register students and endorse them to Openings.
 * When a company representative approves an endorsement, the Student is replaced by an Intern
 * (same id, same account) - see PersonService.promoteToIntern.
 */
public class Student extends Person {
    private final String studentId;
    private String section;
    
    public Student(int id, String name, Gender gender, LocalDate birthdate, String studentId, String section) {
        super(id, name, gender, birthdate);
        this.studentId = studentId;
        this.section = section;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }
}
