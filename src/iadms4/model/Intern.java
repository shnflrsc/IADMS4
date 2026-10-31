package iadms4.model;

import java.time.LocalDate;

/*
 * MODEL. A Student who was approved and deployed to a Position.
 * It keeps the position, the hours recorded so far and the internship status.
 */
public class Intern extends Student {
    private final Position position;
    private int recordedHours;
    private InternStatus status;

    // Creates an intern from the Student details plus a position, recordedHours and a status.
    public Intern(int id, String name, Gender gender, LocalDate birthdate, String studentId, String section,
            Position position, int recordedHours, InternStatus status) {
        super(id, name, gender, birthdate, studentId, section);
        this.position = position;
        this.recordedHours = recordedHours;
        this.status = status;
    }

    // Returns the position the intern is deployed to.
    public Position getPosition() {
        return position;
    }

    // Returns the total hours logged so far.
    public int getRecordedHours() {
        return recordedHours;
    }

    // Changes the total hours logged to the given recordedHours.
    public void setRecordedHours(int recordedHours) {
        this.recordedHours = recordedHours;
    }

    // Returns the internship status.
    public InternStatus getStatus() {
        return status;
    }

    // Changes the internship status to the given status.
    public void setStatus(InternStatus status) {
        this.status = status;
    }
}
