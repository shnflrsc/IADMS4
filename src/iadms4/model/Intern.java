package iadms4.model;

import java.time.LocalDate;

/*
 * MODEL. student who got approved and deployed
 * adds position, hours, status
 */
public class Intern extends Student {
    private final Position position;
    private int recordedHours;
    private InternStatus status;

    // makes an Intern. student stuff + position, recordedHours, status
    public Intern(int id, String name, Gender gender, LocalDate birthdate, String studentId, String section,
            Position position, int recordedHours, InternStatus status) {
        super(id, name, gender, birthdate, studentId, section);
        this.position = position;
        this.recordedHours = recordedHours;
        this.status = status;
    }

    // gets the position this intern is deployed to (Position)
    public Position getPosition() {
        return position;
    }

    // gets the total hours logged so far (int)
    public int getRecordedHours() {
        return recordedHours;
    }

    // sets new recorded hours, takes an int
    public void setRecordedHours(int recordedHours) {
        this.recordedHours = recordedHours;
    }

    // gets the current internship status (InternStatus)
    public InternStatus getStatus() {
        return status;
    }

    // sets new status, takes an InternStatus
    public void setStatus(InternStatus status) {
        this.status = status;
    }
}
