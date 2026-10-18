package iadms4.model;

import java.time.LocalDate;

/**
 * MODEL. A Student who was approved and deployed to a Position.
 * Tracks the hours recorded so far and the InternStatus. Once recordedHours reaches the position
 * requiredHours the company representative marks the intern COMPLETED, which unlocks the evaluation.
 */
public class Intern extends Student {
    private final Position position;
    private int recordedHours;
    private InternStatus status;
    
    public Intern(int id, String name, Gender gender, LocalDate birthdate, String studentId, String section, Position position,
            int recordedHours, InternStatus status) {
        super(id, name, gender, birthdate, studentId, section);
        this.position = position;
        this.recordedHours = recordedHours;
        this.status = status;
    }

    public Position getPosition() {
        return position;
    }

    public int getRecordedHours() {
        return recordedHours;
    }

    public void setRecordedHours(int recordedHours) {
        this.recordedHours = recordedHours;
    }

    public InternStatus getStatus() {
        return status;
    }

    public void setStatus(InternStatus status) {
        this.status = status;
    }
}
