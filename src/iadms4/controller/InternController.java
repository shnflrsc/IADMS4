package iadms4.controller;

import java.util.List;

import iadms4.model.DailyLog;
import iadms4.model.Evaluation;
import iadms4.model.Intern;
import iadms4.model.Session;
import iadms4.service.EvaluationService;
import iadms4.service.InternService;

/*
 * CONTROLLER. intern dashboard stuff
 */
public class InternController {
    private final InternService interns;
    private final EvaluationService evaluations;

    // makes the controller, takes interns, evaluations
    public InternController(InternService interns, EvaluationService evaluations) {
        this.interns = interns;
        this.evaluations = evaluations;
    }

    // gets the logged in intern from Session, cast is safe bc only interns get here
    public Intern me() {
        return (Intern) Session.getInstance().getPerson();
    }

    // gets all the daily logs of the logged in intern
    public List<DailyLog> myLogs() {
        return interns.logsOf(me());
    }

    // my evaluation, null if none yet
    public Evaluation myEvaluation() {
        return evaluations.forIntern(me());
    }

    // log hours, gives back hours left
    public int logHours(int hours, String description) {
        return interns.logHours(me(), hours, description);
    }
}
