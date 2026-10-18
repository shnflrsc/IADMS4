package iadms4.model;

/**
 * MODEL. A coordinator endorsement of a Student to an Opening. Students never apply themselves.
 * Starts PENDING; the company representative then APPROVES (student becomes an Intern) or REJECTS.
 */
public class Application {
    private final int id;
    private final Student student;
    private final Opening opening;
    private final Coordinator coordinator;
    private ApplicationStatus status;

    public Application(int id, Student student, Opening opening, Coordinator coordinator, ApplicationStatus status) {
        this.id = id;
        this.student = student;
        this.opening = opening;
        this.coordinator = coordinator;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public Opening getOpening() {
        return opening;
    }

    public Coordinator getCoordinator() {
        return coordinator;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
}
