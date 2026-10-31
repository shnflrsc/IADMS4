package iadms4.model;

import java.time.LocalDate;

/*
 * MODEL. A student who wants to take an internship. It adds a student number and a section to Person.
 * When an endorsement is approved, the Student is replaced by an Intern with the same id.
 */
public class Student extends Person {
    private final String studentId;
    private String section;

    // Creates a student from the Person details plus a studentId and a section.
    public Student(int id, String name, Gender gender, LocalDate birthdate, String studentId, String section) {
        super(id, name, gender, birthdate);
        this.studentId = studentId;
        this.section = section;
    }

    // Returns the school-issued student number.
    public String getStudentId() {
        return studentId;
    }

    // Returns the class section.
    public String getSection() {
        return section;
    }

    // Changes the class section to the given section.
    public void setSection(String section) {
        this.section = section;
    }
}
