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
import iadms4.service.ApplicationService;
import iadms4.service.EvaluationService;
import iadms4.service.InternService;
import iadms4.service.OpeningService;
import iadms4.service.ReportService;

/*
 * CONTROLLER. Handles everything the Company Representative dashboard can do.
 *
 * Responsibilities: forwards each request to the right service. Every query is scoped to the
 * representative's own company (see company()), so a representative never sees another company's data.
 */
public class CompanyRepController {
    // Service for positions and openings.
    private final OpeningService openings;
    // Service for applications.
    private final ApplicationService applications;
    // Service for interns and their logs.
    private final InternService interns;
    // Service for evaluations.
    private final EvaluationService evaluations;
    // Service for reports.
    private final ReportService reports;

    /*
     * Creates the controller.
     *
     * It takes openings (the opening service), applications (the application service), interns (the intern
     * service), evaluations (the evaluation service) and reports (the report service).
     */
    public CompanyRepController(OpeningService openings, ApplicationService applications, InternService interns,
            EvaluationService evaluations, ReportService reports) {
        this.openings = openings;
        this.applications = applications;
        this.interns = interns;
        this.evaluations = evaluations;
        this.reports = reports;
    }

    /*
     * Gets the logged-in representative.
     *
     * It returns the current person cast to CompanyRepresentative.
     */
    private CompanyRepresentative me() {
        return (CompanyRepresentative) Session.getInstance().getPerson();
    }

    /*
     * Gets the company of the logged-in representative; used to scope every query to their own company.
     *
     * It returns the representative's company.
     */
    public Company company() {
        return me().getCompany();
    }

    /*
     * Lists the company's positions.
     *
     * It returns the positions.
     */
    public List<Position> positions() {
        return openings.positionsOf(company());
    }

    /*
     * Lists the company's openings.
     *
     * It returns the openings.
     */
    public List<Opening> openings() {
        return openings.openingsOf(company());
    }

    /*
     * Lists all applications made to the company.
     *
     * It returns the applications.
     */
    public List<Application> applications() {
        return applications.forCompany(company());
    }

    /*
     * Lists the company's applications that have a given status.
     *
     * It takes status (the status to keep, or null for all). It returns the matching applications.
     */
    public List<Application> applications(ApplicationStatus status) {
        return applications.forCompany(company(), status);
    }

    /*
     * Lists the company's interns that have a given status.
     *
     * It takes status (the status to keep, or null for all). It returns the matching interns.
     */
    public List<Intern> interns(InternStatus status) {
        return interns.internsOf(company(), status);
    }

    /*
     * Edits one of the company's positions.
     *
     * It takes position (the position to edit), title (the new title) and requiredHours (the new required
     * hours).
     */
    public void editPosition(Position position, String title, int requiredHours) {
        openings.updatePosition(company(), position, title, requiredHours);
    }

    /*
     * Deletes one of the company's positions.
     *
     * It takes position (the position to delete).
     */
    public void deletePosition(Position position) {
        openings.deletePosition(company(), position);
    }

    /*
     * Deletes one of the company's openings.
     *
     * It takes opening (the opening to delete).
     */
    public void deleteOpening(Opening opening) {
        openings.deleteOpening(company(), opening);
    }

    /*
     * Builds the summary report of the representative's company.
     *
     * It returns the report numbers.
     */
    public ReportService.Summary report() {
        return reports.forCompany(company());
    }

    /*
     * Lists the company's applications that still wait for a decision.
     *
     * It returns the PENDING applications.
     */
    public List<Application> pendingApplications() {
        return applications.pendingForCompany(company());
    }

    /*
     * Lists all interns of the company.
     *
     * It returns the interns.
     */
    public List<Intern> interns() {
        return interns.internsOf(company());
    }

    /*
     * Lists the daily logs of an intern.
     *
     * It takes intern (the intern). It returns the intern's logs.
     */
    public List<DailyLog> dailyLogs(Intern intern) {
        return interns.logsOf(intern);
    }

    /*
     * Lists the COMPLETED interns that still need an evaluation.
     *
     * It returns the interns awaiting an evaluation.
     */
    public List<Intern> internsAwaitingEvaluation() {
        return evaluations.awaitingEvaluation(company());
    }

    /*
     * Lists the evaluations of the company's interns.
     *
     * It returns the evaluations.
     */
    public List<Evaluation> evaluations() {
        return evaluations.forCompany(company());
    }

    /*
     * Adds a new position to the company.
     *
     * It takes title (the job title) and requiredHours (the hours needed to finish the internship). It
     * returns the new position.
     */
    public Position addPosition(String title, int requiredHours) {
        return openings.createPosition(company(), title, requiredHours);
    }

    /*
     * Publishes a position as an opening.
     *
     * It takes position (the position to publish). It returns the new opening.
     */
    public Opening createOpening(Position position) {
        return openings.createOpening(position);
    }

    /*
     * Switches an opening: OPEN becomes CLOSED and CLOSED becomes OPEN.
     *
     * It takes opening (the opening to toggle).
     */
    public void toggleOpening(Opening opening) {
        openings.setStatus(opening,
                opening.getStatus() == OpeningStatus.OPEN ? OpeningStatus.CLOSED : OpeningStatus.OPEN);
    }

    /*
     * Approves an application; the student becomes an intern.
     *
     * It takes application (the application to approve). It returns the new intern.
     */
    public Intern approve(Application application) {
        return applications.approve(application);
    }

    /*
     * Denies an application.
     *
     * It takes application (the application to reject).
     */
    public void deny(Application application) {
        applications.reject(application);
    }

    /*
     * Changes the status of an intern.
     *
     * It takes intern (the intern) and status (the new status).
     */
    public void changeInternStatus(Intern intern, InternStatus status) {
        interns.changeStatus(intern, status);
    }

    /*
     * Writes an evaluation for a completed intern, signed by the logged-in representative.
     *
     * It takes intern (the intern being evaluated), rating (the score from 1 to 5) and comments (the
     * written feedback). It returns the saved evaluation.
     */
    public Evaluation evaluate(Intern intern, int rating, String comments) {
        return evaluations.evaluate(me(), intern, rating, comments);
    }
}
