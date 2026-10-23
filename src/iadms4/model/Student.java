package iadms4.model;

import java.time.LocalDate;

/*
 * MODEL. A student who wants to take an internship. It extends Person and adds a student number
 * and a section.
 *
 * Responsibilities: stores the student's school details. Coordinators register students and endorse
 * them to Openings. When a company representative approves an endorsement, the Student is replaced by an
 * Intern (same id, same account) - see PersonService.promoteToIntern.
 */
public class Student extends Person {
    // School-issued student number. It never changes, so it is final.
    private final String studentId;
    // Class section of the student, for example "BSIT 2D-G2".
    private String section;

    /*
     * Creates a student.
     *
     * It takes id (the unique person id), name (the full name), gender (the gender), birthdate (the date of
     * birth), studentId (the school-issued student number) and section (the class section).
     */
    public Student(int id, String name, Gender gender, LocalDate birthdate, String studentId, String section) {
        super(id, name, gender, birthdate);
        this.studentId = studentId;
        this.section = section;
    }

    /*
     * Gets the student number.
     *
     * It returns the school-issued student number.
     */
    public String getStudentId() {
        return studentId;
    }

    /*
     * Gets the class section.
     *
     * It returns the section of this student.
     */
    public String getSection() {
        return section;
    }

    /*
     * Changes the class section.
     *
     * It takes section (the new section).
     */
    public void setSection(String section) {
        this.section = section;
    }
}
