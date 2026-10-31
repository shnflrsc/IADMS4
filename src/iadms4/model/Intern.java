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

    // constructor, student stuff + position, hours, status
    public Intern(int id, String name, Gender gender, LocalDate birthdate, String studentId, String section,
            Position position, int recordedHours, InternStatus status) {
        super(id, name, gender, birthdate, studentId, section);
        this.position = position;
        this.recordedHours = recordedHours;
        this.status = status;
    }

    // getter
    public Position getPosition() {
        return position;
    }

    // getter
    public int getRecordedHours() {
        return recordedHours;
    }

    // setter
    public void setRecordedHours(int recordedHours) {
        this.recordedHours = recordedHours;
    }

    // getter
    public InternStatus getStatus() {
        return status;
    }

    // setter
    public void setStatus(InternStatus status) {
        this.status = status;
    }
}
