package iadms4.model;

/*
 * MODEL. One summary report: a plain bundle of numbers built by ReportService.
 * The numbers cover either the whole system or one company.
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

    // Creates a report from all of its numbers.
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

    // Returns the number of students who are not interns yet.
    public int getStudents() {
        return students;
    }

    // Returns the total number of interns.
    public int getInterns() {
        return interns;
    }

    // Returns the number of ACTIVE interns.
    public int getActive() {
        return active;
    }

    // Returns the number of DEACTIVATED interns.
    public int getDeactivated() {
        return deactivated;
    }

    // Returns the number of COMPLETED interns.
    public int getCompleted() {
        return completed;
    }

    // Returns the number of OPEN openings.
    public int getOpenOpenings() {
        return openOpenings;
    }

    // Returns the number of CLOSED openings.
    public int getClosedOpenings() {
        return closedOpenings;
    }

    // Returns the number of PENDING applications.
    public int getPending() {
        return pending;
    }

    // Returns the number of APPROVED applications.
    public int getApproved() {
        return approved;
    }

    // Returns the number of REJECTED applications.
    public int getRejected() {
        return rejected;
    }

    // Returns the number of WITHDRAWN applications.
    public int getWithdrawn() {
        return withdrawn;
    }

    // Returns the total hours logged by the interns.
    public int getRecordedHours() {
        return recordedHours;
    }

    // Returns the total hours the interns must complete.
    public int getRequiredHours() {
        return requiredHours;
    }

    // Returns the number of evaluations.
    public int getEvaluations() {
        return evaluations;
    }

    // Returns the average rating of the evaluations, or 0 when there are none.
    public double getAverageRating() {
        return averageRating;
    }

    // Returns the percentage of required hours already recorded, or 0 when no hours are required.
    public int getProgressPercent() {
        if (requiredHours == 0) {
            return 0;
        }
        return recordedHours * 100 / requiredHours;
    }
}
