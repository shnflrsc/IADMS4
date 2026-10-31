package iadms4.controller;

import java.util.List;

import iadms4.model.DailyLog;
import iadms4.model.Evaluation;
import iadms4.model.Intern;
import iadms4.model.Session;
import iadms4.service.EvaluationService;
import iadms4.service.InternService;

/*
 * CONTROLLER. Handles the actions of the Intern dashboard.
 */
public class InternController {
    private final InternService interns;
    private final EvaluationService evaluations;

    // Creates the controller using the given services.
    public InternController(InternService interns, EvaluationService evaluations) {
        this.interns = interns;
        this.evaluations = evaluations;
    }

    // Returns the logged-in intern.
    public Intern me() {
        return (Intern) Session.getInstance().getPerson();
    }

    // Returns the intern's own daily logs.
    public List<DailyLog> myLogs() {
        return interns.logsOf(me());
    }

    // Returns the intern's evaluation, or null if there is none yet.
    public Evaluation myEvaluation() {
        return evaluations.forIntern(me());
    }

    // Logs hours with a description and returns the hours remaining.
    public int logHours(int hours, String description) {
        return interns.logHours(me(), hours, description);
    }
}
