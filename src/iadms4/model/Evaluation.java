package iadms4.model;

import java.time.LocalDate;

/*
 * MODEL. A representative's review of a COMPLETED Intern.
 *
 * Responsibilities: stores the rating (1-5), the written comments, who evaluated and when. It is created
 * once per intern and never edited. Visible to company representatives and coordinators.
 */
public class Evaluation {
    // Unique id of the evaluation.
    private final int id;
    // The intern being evaluated.
    private final Intern intern;
    // The company representative who wrote the evaluation.
    private final CompanyRepresentative evaluator;
    // Score from 1 (lowest) to 5 (highest).
    private final int rating;
    // Written feedback.
    private final String comments;
    // Date the evaluation was written.
    private final LocalDate date;

    /*
     * Creates an evaluation.
     *
     * It takes id (the unique id), intern (the intern being evaluated), evaluator (the representative who
     * wrote it), rating (the score from 1 to 5), comments (the written feedback) and date (the date it was
     * written).
     */
    public Evaluation(int id, Intern intern, CompanyRepresentative evaluator, int rating, String comments,
            LocalDate date) {
        this.id = id;
        this.intern = intern;
        this.evaluator = evaluator;
        this.rating = rating;
        this.comments = comments;
        this.date = date;
    }

    /*
     * Gets the id.
     *
     * It returns the evaluation id.
     */
    public int getId() {
        return id;
    }

    /*
     * Gets the intern.
     *
     * It returns the evaluated intern.
     */
    public Intern getIntern() {
        return intern;
    }

    /*
     * Gets the evaluator.
     *
     * It returns the representative who wrote the evaluation.
     */
    public CompanyRepresentative getEvaluator() {
        return evaluator;
    }

    /*
     * Gets the rating.
     *
     * It returns the score from 1 to 5.
     */
    public int getRating() {
        return rating;
    }

    /*
     * Gets the comments.
     *
     * It returns the written feedback.
     */
    public String getComments() {
        return comments;
    }

    /*
     * Gets the date.
     *
     * It returns the date the evaluation was written.
     */
    public LocalDate getDate() {
        return date;
    }
}
