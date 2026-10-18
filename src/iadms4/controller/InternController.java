package iadms4.controller;

import java.util.List;
import java.util.Optional;

import iadms4.model.DailyLog;
import iadms4.model.Evaluation;
import iadms4.model.Intern;
import iadms4.model.Session;
import iadms4.service.EvaluationService;
import iadms4.service.InternService;

/**
 * CONTROLLER. Actions of the Intern dashboard. The intern is the logged-in person from Session.
 */
public class InternController {
    private final InternService interns;
    private final EvaluationService evaluations;

    public InternController(InternService interns, EvaluationService evaluations) {
        this.interns = interns;
        this.evaluations = evaluations;
    }

    public Intern me() {
        return (Intern) Session.getInstance().getPerson();
    }

    /** The intern's own daily logs (read-only: logs can never be edited). */
    public List<DailyLog> myLogs() {
        return interns.logsOf(me());
    }

    /** The evaluation the company representative wrote about this intern, if any. */
    public Optional<Evaluation> myEvaluation() {
        return evaluations.forIntern(me());
    }

    /** Logs hours with a daily log description. Returns hours remaining after logging. */
    public int logHours(int hours, String description) {
        return interns.logHours(me(), hours, description);
    }
}
