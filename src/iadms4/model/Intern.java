package iadms4.model;

import java.time.LocalDate;

/*
 * MODEL. A Student who was approved and deployed to a Position.
 *
 * Responsibilities: keeps the position, the hours recorded so far and the InternStatus.
 * Once recordedHours reaches the position's required hours, the company representative marks the intern
 * COMPLETED, which unlocks the evaluation. Intern extends Student, so an Intern "is a" Student.
 */
public class Intern extends Student {
    // The position the intern is deployed to. It does not change.
    private final Position position;
    // Total number of hours logged so far.
    private int recordedHours;
    // Current state of the internship.
    private InternStatus status;

    /*
     * Creates an intern.
     *
     * It takes id (the unique person id), name (the full name), gender (the gender), birthdate (the date of
     * birth), studentId (the school-issued student number), section (the class section), position (the
     * position the intern is deployed to), recordedHours (the hours already logged) and status (the current
     * internship status).
     */
    public Intern(int id, String name, Gender gender, LocalDate birthdate, String studentId, String section, Position position,
            int recordedHours, InternStatus status) {
        super(id, name, gender, birthdate, studentId, section);
        this.position = position;
        this.recordedHours = recordedHours;
        this.status = status;
    }

    /*
     * Gets the position of the intern.
     *
     * It returns the position the intern is deployed to.
     */
    public Position getPosition() {
        return position;
    }

    /*
     * Gets the total hours logged.
     *
     * It returns the recorded hours.
     */
    public int getRecordedHours() {
        return recordedHours;
    }

    /*
     * Sets the total hours logged.
     *
     * It takes recordedHours (the new total of recorded hours).
     */
    public void setRecordedHours(int recordedHours) {
        this.recordedHours = recordedHours;
    }

    /*
     * Gets the internship status.
     *
     * It returns the current status.
     */
    public InternStatus getStatus() {
        return status;
    }

    /*
     * Changes the internship status.
     *
     * It takes status (the new status).
     */
    public void setStatus(InternStatus status) {
        this.status = status;
    }
}
