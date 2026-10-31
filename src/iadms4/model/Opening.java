package iadms4.model;

/*
 * MODEL. A Position that has been published so students can be endorsed to it.
 * OPEN means endorsements are accepted and CLOSED means they are not.
 */
public class Opening {
    private final int id;
    private final Position position;
    private OpeningStatus status;

    // Creates an opening with the given id, position and status.
    public Opening(int id, Position position, OpeningStatus status) {
        this.id = id;
        this.position = position;
        this.status = status;
    }

    // Returns the opening id.
    public int getId() {
        return id;
    }

    // Returns the position being offered.
    public Position getPosition() {
        return position;
    }

    // Returns OPEN or CLOSED.
    public OpeningStatus getStatus() {
        return status;
    }

    // Changes the status to the given status.
    public void setStatus(OpeningStatus status) {
        this.status = status;
    }
}
