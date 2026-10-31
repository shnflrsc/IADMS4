package iadms4.model;

/*
 * MODEL. coordinator endorses a student to an opening, that's an application
 * starts PENDING then gets approved, rejected or withdrawn
 */
public class Application {
    private final int id;
    private final Student student;
    private final Opening opening;
    private final Coordinator coordinator;
    private ApplicationStatus status;

    // makes an Application, takes id, student, opening, coordinator, status
    public Application(int id, Student student, Opening opening, Coordinator coordinator, ApplicationStatus status) {
        this.id = id;
        this.student = student;
        this.opening = opening;
        this.coordinator = coordinator;
        this.status = status;
    }

    // gets the unique id of this application (int)
    public int getId() {
        return id;
    }

    // gets the student who got endorsed (Student)
    public Student getStudent() {
        return student;
    }

    // gets the opening the student was endorsed to (Opening)
    public Opening getOpening() {
        return opening;
    }

    // gets the coordinator who made the endorsement (Coordinator)
    public Coordinator getCoordinator() {
        return coordinator;
    }

    // gets where the application is right now (ApplicationStatus)
    public ApplicationStatus getStatus() {
        return status;
    }

    // sets new status, takes an ApplicationStatus
    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
}
