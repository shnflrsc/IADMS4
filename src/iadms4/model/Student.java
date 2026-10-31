package iadms4.model;

import java.time.LocalDate;

/*
 * MODEL. student who wants an internship
 * gets replaced by an Intern w/ same id when approved
 */
public class Student extends Person {
    private final String studentId;
    private String section;

    // constructor
    public Student(int id, String name, Gender gender, LocalDate birthdate, String studentId, String section) {
        super(id, name, gender, birthdate);
        this.studentId = studentId;
        this.section = section;
    }

    // getter
    public String getStudentId() {
        return studentId;
    }

    // getter
    public String getSection() {
        return section;
    }

    // setter
    public void setSection(String section) {
        this.section = section;
    }
}
