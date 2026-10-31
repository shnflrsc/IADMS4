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

    // gets id (int)
    public int getId() {
        return id;
    }

    // gets intern (Intern)
    public Intern getIntern() {
        return intern;
    }

    // gets evaluator (CompanyRepresentative)
    public CompanyRepresentative getEvaluator() {
        return evaluator;
    }

    // gets rating (int), 1 to 5
    public int getRating() {
        return rating;
    }

    // gets comments (String)
    public String getComments() {
        return comments;
    }

    // gets date (LocalDate)
    public LocalDate getDate() {
        return date;
    }
}
