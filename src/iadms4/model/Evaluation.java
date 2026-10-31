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

    // constructor
    public Evaluation(int id, Intern intern, CompanyRepresentative evaluator, int rating, String comments,
            LocalDate date) {
        this.id = id;
        this.intern = intern;
        this.evaluator = evaluator;
        this.rating = rating;
        this.comments = comments;
        this.date = date;
    }

    // getter
    public int getId() {
        return id;
    }

    // getter
    public Intern getIntern() {
        return intern;
    }

    // getter
    public CompanyRepresentative getEvaluator() {
        return evaluator;
    }

    // getter, 1 to 5
    public int getRating() {
        return rating;
    }

    // getter
    public String getComments() {
        return comments;
    }

    // getter
    public LocalDate getDate() {
        return date;
    }
}
