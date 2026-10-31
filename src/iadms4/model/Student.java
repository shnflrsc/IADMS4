package iadms4.model;

import java.time.LocalDate;

/*
 * MODEL. student who wants an internship
 * gets replaced by an Intern w/ same id when approved
 */
public class Student extends Person {
    private final String studentId;
    private String section;

    // makes a Student, takes id, name, gender, birthdate, studentId, section
    public Student(int id, String name, Gender gender, LocalDate birthdate, String studentId, String section) {
        super(id, name, gender, birthdate);
        this.studentId = studentId;
        this.section = section;
    }

    // gets student id (String)
    public String getStudentId() {
        return studentId;
    }

    // gets section (String)
    public String getSection() {
        return section;
    }

    // sets new section, takes a String
    public void setSection(String section) {
        this.section = section;
    }
}
