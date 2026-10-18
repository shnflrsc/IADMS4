package iadms4.model;

/**
 * MODEL. A Position that is published for applications (OPEN) or no longer accepting them (CLOSED).
 * Coordinators endorse students to Openings.
 */
public class Opening {
    private final int id;
    private final Position position;
    private OpeningStatus status;

    public Opening(int id, Position position, OpeningStatus status) {
        this.id = id;
        this.position = position;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public Position getPosition() {
        return position;
    }

    public OpeningStatus getStatus() {
        return status;
    }

    public void setStatus(OpeningStatus status) {
        this.status = status;
    }
}
