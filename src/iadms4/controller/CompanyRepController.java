package iadms4.controller;

import java.util.List;

import iadms4.model.Application;
import iadms4.model.ApplicationStatus;
import iadms4.model.Company;
import iadms4.model.CompanyRepresentative;
import iadms4.model.DailyLog;
import iadms4.model.Evaluation;
import iadms4.model.Intern;
import iadms4.model.InternStatus;
import iadms4.model.Opening;
import iadms4.model.OpeningStatus;
import iadms4.model.Position;
import iadms4.model.Session;
import iadms4.model.Summary;
import iadms4.service.ApplicationService;
import iadms4.service.EvaluationService;
import iadms4.service.InternService;
import iadms4.service.OpeningService;
import iadms4.service.ReportService;

/*
 * CONTROLLER. everything the company rep dashboard does
 * everything is limited to the rep's own company
 */
public class CompanyRepController {
    private final OpeningService openings;
    private final ApplicationService applications;
    private final InternService interns;
    private final EvaluationService evaluations;
    private final ReportService reports;

    // makes the controller, takes openings, applications, interns, evaluations, reports
    public CompanyRepController(OpeningService openings, ApplicationService applications, InternService interns,
            EvaluationService evaluations, ReportService reports) {
        this.openings = openings;
        this.applications = applications;
        this.interns = interns;
        this.evaluations = evaluations;
        this.reports = reports;
    }

    // the rep thats logged in
    private CompanyRepresentative me() {
        return (CompanyRepresentative) Session.getInstance().getPerson();
    }

    // the logged in rep's company
    public Company company() {
        return me().getCompany();
    }

    // positions of this company
    public List<Position> positions() {
        return openings.positionsOf(company());
    }

    // openings of this company
    public List<Opening> openings() {
        return openings.openingsOf(company());
    }

    // all applications for this company
    public List<Application> applications() {
        return applications.forCompany(company());
    }

    // company applications by status, null = all
    public List<Application> applications(ApplicationStatus status) {
        return applications.forCompany(company(), status);
    }

    // pending ones only
    public List<Application> pendingApplications() {
        return applications.pendingForCompany(company());
    }

    // all interns of this company
    public List<Intern> interns() {
        return interns.internsOf(company(), null);
    }

    // company interns by status, null = all
    public List<Intern> interns(InternStatus status) {
        return interns.internsOf(company(), status);
    }

    // daily logs of this intern
    public List<DailyLog> dailyLogs(Intern intern) {
        return interns.logsOf(intern);
    }

    // completed interns w/ no evaluation yet
    public List<Intern> internsAwaitingEvaluation() {
        return evaluations.awaitingEvaluation(company());
    }

    // evaluations for this company
    public List<Evaluation> evaluations() {
        return evaluations.forCompany(company());
    }

    // company report
    public Summary report() {
        return reports.forCompany(company());
    }

    // add position
    public void addPosition(String title, int requiredHours) {
        openings.createPosition(company(), title, requiredHours);
    }

    // edit position
    public void editPosition(Position position, String title, int requiredHours) {
        openings.updatePosition(company(), position, title, requiredHours);
    }

    // delete position
    public void deletePosition(Position position) {
        openings.deletePosition(company(), position);
    }

    // turns a position into an opening
    public void createOpening(Position position) {
        openings.createOpening(position);
    }

    // delete opening
    public void deleteOpening(Opening opening) {
        openings.deleteOpening(company(), opening);
    }

    // OPEN <-> CLOSED
    public void toggleOpening(Opening opening) {
        if (opening.getStatus() == OpeningStatus.OPEN) {
            openings.setStatus(opening, OpeningStatus.CLOSED);
        } else {
            openings.setStatus(opening, OpeningStatus.OPEN);
        }
    }

    // approve, gives back the new intern
    public Intern approve(Application application) {
        return applications.approve(application);
    }

    // deny
    public void deny(Application application) {
        applications.reject(application);
    }

    // change intern status
    public void changeInternStatus(Intern intern, InternStatus status) {
        interns.changeStatus(intern, status);
    }

    // saves evaluation, logged in rep is the evaluator
    public void evaluate(Intern intern, int rating, String comments) {
        evaluations.evaluate(me(), intern, rating, comments);
    }
}
