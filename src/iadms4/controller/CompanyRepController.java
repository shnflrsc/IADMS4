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
 * CONTROLLER. everything the company representative dashboard does
 * everything is limited to the representative's own company
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

    // gets the logged in representative from Session, cast is safe because only representatives get this dashboard
    private CompanyRepresentative me() {
        return (CompanyRepresentative) Session.getInstance().getPerson();
    }

    // gets the logged in representative's company, used to limit everything to their company
    public Company company() {
        return me().getCompany();
    }

    // gets the list of positions this company offers
    public List<Position> positions() {
        return openings.positionsOf(company());
    }

    // gets the list of openings this company published
    public List<Opening> openings() {
        return openings.openingsOf(company());
    }

    // gets every application sent to this company, any status
    public List<Application> applications() {
        return applications.forCompany(company());
    }

    // company applications by status, null means all
    public List<Application> applications(ApplicationStatus status) {
        return applications.forCompany(company(), status);
    }

    // gets only the applications still waiting for this company's decision
    public List<Application> pendingApplications() {
        return applications.pendingForCompany(company());
    }

    // gets every intern deployed to this company, any status
    public List<Intern> interns() {
        return interns.internsOf(company(), null);
    }

    // company interns by status, null means all
    public List<Intern> interns(InternStatus status) {
        return interns.internsOf(company(), status);
    }

    // gets all the daily logs written by the intern you pass in
    public List<DailyLog> dailyLogs(Intern intern) {
        return interns.logsOf(intern);
    }

    // completed interns with no evaluation yet
    public List<Intern> internsAwaitingEvaluation() {
        return evaluations.awaitingEvaluation(company());
    }

    // gets the evaluations written for interns of this company
    public List<Evaluation> evaluations() {
        return evaluations.forCompany(company());
    }

    // builds the summary report for this company only
    public Summary report() {
        return reports.forCompany(company());
    }

    // adds a new position to this company using the title and hours
    public void addPosition(String title, int requiredHours) {
        openings.createPosition(company(), title, requiredHours);
    }

    // changes the title and hours of one of this company's positions
    public void editPosition(Position position, String title, int requiredHours) {
        openings.updatePosition(company(), position, title, requiredHours);
    }

    // deletes one of this company's positions if nothing is using it
    public void deletePosition(Position position) {
        openings.deletePosition(company(), position);
    }

    // turns a position into an opening
    public void createOpening(Position position) {
        openings.createOpening(position);
    }

    // deletes one of this company's openings if nobody was endorsed to it
    public void deleteOpening(Opening opening) {
        openings.deleteOpening(company(), opening);
    }

    // flips the opening, OPEN becomes CLOSED and CLOSED becomes OPEN
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

    // rejects the application you pass in, student stays a student
    public void deny(Application application) {
        applications.reject(application);
    }

    // sets the intern to the new status, service blocks the invalid ones
    public void changeInternStatus(Intern intern, InternStatus status) {
        interns.changeStatus(intern, status);
    }

    // saves evaluation, logged in representative is the evaluator
    public void evaluate(Intern intern, int rating, String comments) {
        evaluations.evaluate(me(), intern, rating, comments);
    }
}
