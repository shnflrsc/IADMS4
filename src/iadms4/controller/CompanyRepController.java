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

/**
 * CONTROLLER. Everything the Company Representative dashboard can do. Every query is scoped to the
 * representative own company (company()), so a representative never sees other companies data.
 */
public class CompanyRepController {
    private final OpeningService openings;
    private final ApplicationService applications;
    private final InternService interns;
    private final EvaluationService evaluations;
    private final ReportService reports;

    public CompanyRepController(OpeningService openings, ApplicationService applications, InternService interns,
            EvaluationService evaluations, ReportService reports) {
        this.openings = openings;
        this.applications = applications;
        this.interns = interns;
        this.evaluations = evaluations;
        this.reports = reports;
    }

    private CompanyRepresentative me() {
        return (CompanyRepresentative) Session.getInstance().getPerson();
    }

    /** The company of the logged-in representative; used to scope every query to their own company. */
    public Company company() {
        return me().getCompany();
    }

    public List<Position> positions() {
        return openings.positionsOf(company());
    }

    public List<Opening> openings() {
        return openings.openingsOf(company());
    }

    public List<Application> applications() {
        return applications.forCompany(company());
    }

    public List<Application> applications(ApplicationStatus status) {
        return applications.forCompany(company(), status);
    }

    public List<Intern> interns(InternStatus status) {
        return interns.internsOf(company(), status);
    }

    public void editPosition(Position position, String title, int requiredHours) {
        openings.updatePosition(company(), position, title, requiredHours);
    }

    public void deletePosition(Position position) {
        openings.deletePosition(company(), position);
    }

    public void deleteOpening(Opening opening) {
        openings.deleteOpening(company(), opening);
    }

    public ReportService.Summary report() {
        return reports.forCompany(company());
    }

    public List<Application> pendingApplications() {
        return applications.pendingForCompany(company());
    }

    public List<Intern> interns() {
        return interns.internsOf(company());
    }

    public List<DailyLog> dailyLogs(Intern intern) {
        return interns.logsOf(intern);
    }

    public List<Intern> internsAwaitingEvaluation() {
        return evaluations.awaitingEvaluation(company());
    }

    public List<Evaluation> evaluations() {
        return evaluations.forCompany(company());
    }

    public Position addPosition(String title, int requiredHours) {
        return openings.createPosition(company(), title, requiredHours);
    }

    public Opening createOpening(Position position) {
        return openings.createOpening(position);
    }

    public void toggleOpening(Opening opening) {
        openings.setStatus(opening,
                opening.getStatus() == OpeningStatus.OPEN ? OpeningStatus.CLOSED : OpeningStatus.OPEN);
    }

    public Intern approve(Application application) {
        return applications.approve(application);
    }

    public void deny(Application application) {
        applications.reject(application);
    }

    public void changeInternStatus(Intern intern, InternStatus status) {
        interns.changeStatus(intern, status);
    }

    public Evaluation evaluate(Intern intern, int rating, String comments) {
        return evaluations.evaluate(me(), intern, rating, comments);
    }
}
