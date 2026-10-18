package iadms4.model;

import java.time.LocalDate;

/**
 * MODEL. A representative review of a COMPLETED intern: rating 1-5 plus comments. Created once per
 * intern and never edited. Visible to company representatives and coordinators.
 */
public class Evaluation {
    private final int id;
    private final Intern intern;
    private final CompanyRepresentative evaluator;
    private final int rating;
    private final String comments;
    private final LocalDate date;

    public Evaluation(int id, Intern intern, CompanyRepresentative evaluator, int rating, String comments,
            LocalDate date) {
        this.id = id;
        this.intern = intern;
        this.evaluator = evaluator;
        this.rating = rating;
        this.comments = comments;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public Intern getIntern() {
        return intern;
    }

    public CompanyRepresentative getEvaluator() {
        return evaluator;
    }

    public int getRating() {
        return rating;
    }

    public String getComments() {
        return comments;
    }

    public LocalDate getDate() {
        return date;
    }
}
