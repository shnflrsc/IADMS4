package iadms4.model;

/*
 * MODEL. A coordinator's endorsement of one Student to one Opening.
 * It starts as PENDING, then the company approves or rejects it, or a coordinator withdraws it.
 */
public class Application {
    private final int id;
    private final Student student;
    private final Opening opening;
    private final Coordinator coordinator;
    private ApplicationStatus status;

    // Creates an application with the given id, student, opening, coordinator and status.
    public Application(int id, Student student, Opening opening, Coordinator coordinator, ApplicationStatus status) {
        this.id = id;
        this.student = student;
        this.opening = opening;
        this.coordinator = coordinator;
        this.status = status;
    }

    // Returns the application id.
    public int getId() {
        return id;
    }

    // Returns the student who was endorsed.
    public Student getStudent() {
        return student;
    }

    // Returns the opening the student was endorsed to.
    public Opening getOpening() {
        return opening;
    }

    // Returns the coordinator who made the endorsement.
    public Coordinator getCoordinator() {
        return coordinator;
    }

    // Returns the current status.
    public ApplicationStatus getStatus() {
        return status;
    }

    // Changes the status to the given status.
    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
}
