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

    // makes a Summary, takes all 15 numbers. yes 15, sorry
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

    // how many students haven't been deployed as interns yet
    public int getStudents() {
        return students;
    }

    // how many interns there are in total
    public int getInterns() {
        return interns;
    }

    // how many interns are ACTIVE right now
    public int getActive() {
        return active;
    }

    // how many interns are DEACTIVATED right now
    public int getDeactivated() {
        return deactivated;
    }

    // how many interns already COMPLETED their internship
    public int getCompleted() {
        return completed;
    }

    // how many openings are still OPEN
    public int getOpenOpenings() {
        return openOpenings;
    }

    // how many openings are CLOSED already
    public int getClosedOpenings() {
        return closedOpenings;
    }

    // how many applications are still PENDING
    public int getPending() {
        return pending;
    }

    // how many applications got APPROVED
    public int getApproved() {
        return approved;
    }

    // how many applications got REJECTED
    public int getRejected() {
        return rejected;
    }

    // how many applications got WITHDRAWN
    public int getWithdrawn() {
        return withdrawn;
    }

    // total hours logged by all the interns counted here
    public int getRecordedHours() {
        return recordedHours;
    }

    // total hours all the interns counted here need to finish
    public int getRequiredHours() {
        return requiredHours;
    }

    // how many evaluations were written so far
    public int getEvaluations() {
        return evaluations;
    }

    // average rating of the evaluations, stays 0 if there's none
    public double getAverageRating() {
        return averageRating;
    }

    // progress percentage, 0 if no required hours so we don't divide by 0
    public int getProgressPercent() {
        if (requiredHours == 0) {
            return 0;
        }
        return recordedHours * 100 / requiredHours;
    }
}
