package iadms4.model;

/*
 * MODEL. A Position that has been published so students can be endorsed to it.
 *
 * Responsibilities: pairs a Position with an OpeningStatus. OPEN means endorsements are accepted
 * and CLOSED means they are not. Coordinators endorse students to Openings, never directly to Positions.
 */
public class Opening {
    // Unique id of the opening.
    private final int id;
    // The position being offered.
    private final Position position;
    // Whether the opening is currently accepting endorsements.
    private OpeningStatus status;

    /*
     * Creates an opening.
     *
     * It takes id (the unique id), position (the position being published) and status (OPEN or CLOSED).
     */
    public Opening(int id, Position position, OpeningStatus status) {
        this.id = id;
        this.position = position;
        this.status = status;
    }

    /*
     * Gets the id.
     *
     * It returns the opening id.
     */
    public int getId() {
        return id;
    }

    /*
     * Gets the published position.
     *
     * It returns the position of this opening.
     */
    public Position getPosition() {
        return position;
    }

    /*
     * Gets the status.
     *
     * It returns OPEN or CLOSED.
     */
    public OpeningStatus getStatus() {
        return status;
    }

    /*
     * Opens or closes the opening.
     *
     * It takes status (the new status).
     */
    public void setStatus(OpeningStatus status) {
        this.status = status;
    }
}
