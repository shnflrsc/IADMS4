package iadms4.model;

import java.time.LocalDate;

/*
 * MODEL. A representative's review of a COMPLETED Intern.
 * It is created once per intern and never edited.
 */
public class Evaluation {
    private final int id;
    private final Intern intern;
    private final CompanyRepresentative evaluator;
    private final int rating;
    private final String comments;
    private final LocalDate date;

    // Creates an evaluation with the given id, intern, evaluator, rating, comments and date.
    public Evaluation(int id, Intern intern, CompanyRepresentative evaluator, int rating, String comments,
            LocalDate date) {
        this.id = id;
        this.intern = intern;
        this.evaluator = evaluator;
        this.rating = rating;
        this.comments = comments;
        this.date = date;
    }

    // Returns the evaluation id.
    public int getId() {
        return id;
    }

    // Returns the intern who was evaluated.
    public Intern getIntern() {
        return intern;
    }

    // Returns the representative who wrote the evaluation.
    public CompanyRepresentative getEvaluator() {
        return evaluator;
    }

    // Returns the score from 1 to 5.
    public int getRating() {
        return rating;
    }

    // Returns the written feedback.
    public String getComments() {
        return comments;
    }

    // Returns the date the evaluation was written.
    public LocalDate getDate() {
        return date;
    }
}
