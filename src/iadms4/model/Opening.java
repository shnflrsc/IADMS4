package iadms4.model;

/*
 * MODEL. a published position, students get endorsed to this
 * OPEN = accepting, CLOSED = not
 */
public class Opening {
    private final int id;
    private final Position position;
    private OpeningStatus status;

    // constructor
    public Opening(int id, Position position, OpeningStatus status) {
        this.id = id;
        this.position = position;
        this.status = status;
    }

    // getter
    public int getId() {
        return id;
    }

    // getter
    public Position getPosition() {
        return position;
    }

    // OPEN or CLOSED
    public OpeningStatus getStatus() {
        return status;
    }

    // setter
    public void setStatus(OpeningStatus status) {
        this.status = status;
    }
}
