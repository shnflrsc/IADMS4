package iadms4.model;

/*
 * MODEL. a published position, students get endorsed to this
 * OPEN = accepting, CLOSED = not
 */
public class Opening {
    private final int id;
    private final Position position;
    private OpeningStatus status;

    // makes an Opening, takes id, position, status
    public Opening(int id, Position position, OpeningStatus status) {
        this.id = id;
        this.position = position;
        this.status = status;
    }

    // gets the unique id of this opening (int)
    public int getId() {
        return id;
    }

    // gets the position being offered here (Position)
    public Position getPosition() {
        return position;
    }

    // gets if its OPEN or CLOSED right now (OpeningStatus)
    public OpeningStatus getStatus() {
        return status;
    }

    // sets new status, takes an OpeningStatus
    public void setStatus(OpeningStatus status) {
        this.status = status;
    }
}
