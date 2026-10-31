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
 * CONTROLLER. Handles everything the Company Representative dashboard can do.
 * Every request is limited to the representative's own company.
 */
public class CompanyRepController {
    private final OpeningService openings;
    private final ApplicationService applications;
    private final InternService interns;
    private final EvaluationService evaluations;
    private final ReportService reports;

    // Creates the controller using the given services.
    public CompanyRepController(OpeningService openings, ApplicationService applications, InternService interns,
            EvaluationService evaluations, ReportService reports) {
        this.openings = openings;
        this.applications = applications;
        this.interns = interns;
        this.evaluations = evaluations;
        this.reports = reports;
    }

    // Returns the logged-in representative.
    private CompanyRepresentative me() {
        return (CompanyRepresentative) Session.getInstance().getPerson();
    }

    // Returns the company of the logged-in representative.
    public Company company() {
        return me().getCompany();
    }

    // Returns the company's positions.
    public List<Position> positions() {
        return openings.positionsOf(company());
    }

    // Returns the company's openings.
    public List<Opening> openings() {
        return openings.openingsOf(company());
    }

    // Returns all applications made to the company.
    public List<Application> applications() {
        return applications.forCompany(company());
    }

    // Returns the company's applications with the given status, or all if status is null.
    public List<Application> applications(ApplicationStatus status) {
        return applications.forCompany(company(), status);
    }

    // Returns the company's PENDING applications.
    public List<Application> pendingApplications() {
        return applications.pendingForCompany(company());
    }

    // Returns all interns of the company.
    public List<Intern> interns() {
        return interns.internsOf(company(), null);
    }

    // Returns the company's interns with the given status, or all if status is null.
    public List<Intern> interns(InternStatus status) {
        return interns.internsOf(company(), status);
    }

    // Returns the daily logs of the given intern.
    public List<DailyLog> dailyLogs(Intern intern) {
        return interns.logsOf(intern);
    }

    // Returns the COMPLETED interns that still need an evaluation.
    public List<Intern> internsAwaitingEvaluation() {
        return evaluations.awaitingEvaluation(company());
    }

    // Returns the evaluations of the company's interns.
    public List<Evaluation> evaluations() {
        return evaluations.forCompany(company());
    }

    // Returns the summary report of the company.
    public Summary report() {
        return reports.forCompany(company());
    }

    // Adds a position with the given title and requiredHours to the company.
    public void addPosition(String title, int requiredHours) {
        openings.createPosition(company(), title, requiredHours);
    }

    // Edits the given position of the company.
    public void editPosition(Position position, String title, int requiredHours) {
        openings.updatePosition(company(), position, title, requiredHours);
    }

    // Deletes the given position of the company.
    public void deletePosition(Position position) {
        openings.deletePosition(company(), position);
    }

    // Publishes the given position as an opening.
    public void createOpening(Position position) {
        openings.createOpening(position);
    }

    // Deletes the given opening of the company.
    public void deleteOpening(Opening opening) {
        openings.deleteOpening(company(), opening);
    }

    // Switches the given opening between OPEN and CLOSED.
    public void toggleOpening(Opening opening) {
        if (opening.getStatus() == OpeningStatus.OPEN) {
            openings.setStatus(opening, OpeningStatus.CLOSED);
        } else {
            openings.setStatus(opening, OpeningStatus.OPEN);
        }
    }

    // Approves the given application and returns the new intern.
    public Intern approve(Application application) {
        return applications.approve(application);
    }

    // Denies the given application.
    public void deny(Application application) {
        applications.reject(application);
    }

    // Changes the status of the given intern.
    public void changeInternStatus(Intern intern, InternStatus status) {
        interns.changeStatus(intern, status);
    }

    // Saves an evaluation of the given intern, signed by the logged-in representative.
    public void evaluate(Intern intern, int rating, String comments) {
        evaluations.evaluate(me(), intern, rating, comments);
    }
}
