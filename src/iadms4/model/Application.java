package iadms4.model;

/*
 * MODEL. coordinator endorses a student to an opening, thats an application
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

    // gets id (int)
    public int getId() {
        return id;
    }

    // endorsed student
    public Student getStudent() {
        return student;
    }

    // which opening
    public Opening getOpening() {
        return opening;
    }

    // who endorsed
    public Coordinator getCoordinator() {
        return coordinator;
    }

    // gets status (ApplicationStatus)
    public ApplicationStatus getStatus() {
        return status;
    }

    // sets new status, takes an ApplicationStatus
    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
}
