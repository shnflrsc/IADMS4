package iadms4.model;

/*
 * MODEL. just a bunch of numbers for reports
 * ReportService fills this in
 */
public class Summary {
    private final int students;
    private final int interns;
    private final int active;
    private final int deactivated;
    private final int completed;
    private final int openOpenings;
    private final int closedOpenings;
    private final int pending;
    private final int approved;
    private final int rejected;
    private final int withdrawn;
    private final int recordedHours;
    private final int requiredHours;
    private final int evaluations;
    private final double averageRating;

    // constructor, yes its 15 params, sorry
    public Summary(int students, int interns, int active, int deactivated, int completed, int openOpenings,
            int closedOpenings, int pending, int approved, int rejected, int withdrawn, int recordedHours,
            int requiredHours, int evaluations, double averageRating) {
        this.students = students;
        this.interns = interns;
        this.active = active;
        this.deactivated = deactivated;
        this.completed = completed;
        this.openOpenings = openOpenings;
        this.closedOpenings = closedOpenings;
        this.pending = pending;
        this.approved = approved;
        this.rejected = rejected;
        this.withdrawn = withdrawn;
        this.recordedHours = recordedHours;
        this.requiredHours = requiredHours;
        this.evaluations = evaluations;
        this.averageRating = averageRating;
    }

    // students not interns yet
    public int getStudents() {
        return students;
    }

    // total interns
    public int getInterns() {
        return interns;
    }

    // ACTIVE count
    public int getActive() {
        return active;
    }

    // DEACTIVATED count
    public int getDeactivated() {
        return deactivated;
    }

    // COMPLETED count
    public int getCompleted() {
        return completed;
    }

    // OPEN count
    public int getOpenOpenings() {
        return openOpenings;
    }

    // CLOSED count
    public int getClosedOpenings() {
        return closedOpenings;
    }

    // PENDING count
    public int getPending() {
        return pending;
    }

    // APPROVED count
    public int getApproved() {
        return approved;
    }

    // REJECTED count
    public int getRejected() {
        return rejected;
    }

    // WITHDRAWN count
    public int getWithdrawn() {
        return withdrawn;
    }

    // total hours logged
    public int getRecordedHours() {
        return recordedHours;
    }

    // total hours required
    public int getRequiredHours() {
        return requiredHours;
    }

    // evaluation count
    public int getEvaluations() {
        return evaluations;
    }

    // avg rating, 0 if none
    public double getAverageRating() {
        return averageRating;
    }

    // progress %, 0 if no required hours so we dont divide by 0
    public int getProgressPercent() {
        if (requiredHours == 0) {
            return 0;
        }
        return recordedHours * 100 / requiredHours;
    }
}
