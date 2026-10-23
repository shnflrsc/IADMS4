package iadms4.controller;

import java.util.List;
import java.util.Optional;

import iadms4.model.DailyLog;
import iadms4.model.Evaluation;
import iadms4.model.Intern;
import iadms4.model.Session;
import iadms4.service.EvaluationService;
import iadms4.service.InternService;

/*
 * CONTROLLER. Handles the actions of the Intern dashboard.
 *
 * Responsibilities: lets the intern log hours and read their own logs and evaluation.
 * The intern is the logged-in person taken from Session.
 */
public class InternController {
    // Service for logging hours and reading logs.
    private final InternService interns;
    // Service for reading the intern's evaluation.
    private final EvaluationService evaluations;

    /*
     * Creates the controller.
     *
     * It takes interns (the intern service) and evaluations (the evaluation service).
     */
    public InternController(InternService interns, EvaluationService evaluations) {
        this.interns = interns;
        this.evaluations = evaluations;
    }

    /*
     * Gets the logged-in intern.
     *
     * It returns the current person cast to Intern.
     */
    public Intern me() {
        return (Intern) Session.getInstance().getPerson();
    }

    /*
     * Lists the intern's own daily logs (read-only: logs can never be edited).
     *
     * It returns the intern's logs.
     */
    public List<DailyLog> myLogs() {
        return interns.logsOf(me());
    }

    /*
     * Gets the evaluation the company representative wrote about this intern, if any.
     *
     * It returns an Optional with the evaluation, or an empty Optional if none exists yet.
     */
    public Optional<Evaluation> myEvaluation() {
        return evaluations.forIntern(me());
    }

    /*
     * Logs hours with a daily log description.
     *
     * It takes hours (the hours worked) and description (what the intern did). It returns the hours
     * remaining after logging.
     */
    public int logHours(int hours, String description) {
        return interns.logHours(me(), hours, description);
    }
}
