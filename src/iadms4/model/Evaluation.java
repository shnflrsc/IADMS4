package iadms4.model;

import java.time.LocalDate;

/*
 * MODEL. rep's review of a COMPLETED intern
 * once per intern, never edited
 */
public class Evaluation {
    private final int id;
    private final Intern intern;
    private final CompanyRepresentative evaluator;
    private final int rating;
    private final String comments;
    private final LocalDate date;

    // makes an Evaluation, takes id, intern, evaluator, rating, comments, date
    public Evaluation(int id, Intern intern, CompanyRepresentative evaluator, int rating, String comments,
            LocalDate date) {
        this.id = id;
        this.intern = intern;
        this.evaluator = evaluator;
        this.rating = rating;
        this.comments = comments;
        this.date = date;
    }

    // gets the unique id of this evaluation (int)
    public int getId() {
        return id;
    }

    // gets the intern who got evaluated (Intern)
    public Intern getIntern() {
        return intern;
    }

    // gets the rep who wrote this evaluation (CompanyRepresentative)
    public CompanyRepresentative getEvaluator() {
        return evaluator;
    }

    // gets rating (int), 1 to 5
    public int getRating() {
        return rating;
    }

    // gets the written feedback from the rep (String)
    public String getComments() {
        return comments;
    }

    // gets the day the evaluation was written (LocalDate)
    public LocalDate getDate() {
        return date;
    }
}
