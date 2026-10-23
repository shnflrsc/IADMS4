package iadms4.model;

/*
 * MODEL. A coordinator's endorsement of one Student to one Opening.
 *
 * Responsibilities: records who was endorsed, to what, by which coordinator, and the current
 * ApplicationStatus. Students never apply by themselves. An application starts PENDING; the
 * company representative then APPROVES it (the student becomes an Intern) or REJECTS it.
 */
public class Application {
    // Unique id of the application.
    private final int id;
    // The student who was endorsed.
    private final Student student;
    // The opening the student was endorsed to.
    private final Opening opening;
    // The coordinator who made the endorsement.
    private final Coordinator coordinator;
    // Where the application is in its lifecycle.
    private ApplicationStatus status;

    /*
     * Creates an application.
     *
     * It takes id (the unique id), student (the endorsed student), opening (the opening endorsed to),
     * coordinator (the coordinator who endorsed the student) and status (the starting status (normally
     * PENDING)).
     */
    public Application(int id, Student student, Opening opening, Coordinator coordinator, ApplicationStatus status) {
        this.id = id;
        this.student = student;
        this.opening = opening;
        this.coordinator = coordinator;
        this.status = status;
    }

    /*
     * Gets the id.
     *
     * It returns the application id.
     */
    public int getId() {
        return id;
    }

    /*
     * Gets the endorsed student.
     *
     * It returns the student.
     */
    public Student getStudent() {
        return student;
    }

    /*
     * Gets the opening.
     *
     * It returns the opening the student was endorsed to.
     */
    public Opening getOpening() {
        return opening;
    }

    /*
     * Gets the endorsing coordinator.
     *
     * It returns the coordinator who made the endorsement.
     */
    public Coordinator getCoordinator() {
        return coordinator;
    }

    /*
     * Gets the status.
     *
     * It returns the current application status.
     */
    public ApplicationStatus getStatus() {
        return status;
    }

    /*
     * Changes the status, for example when the company approves or rejects.
     *
     * It takes status (the new status).
     */
    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
}
