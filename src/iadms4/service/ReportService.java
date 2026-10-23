package iadms4.service;

import java.util.List;

import iadms4.model.Application;
import iadms4.model.ApplicationStatus;
import iadms4.model.Company;
import iadms4.model.Evaluation;
import iadms4.model.Intern;
import iadms4.model.InternStatus;
import iadms4.model.OpeningStatus;
import iadms4.repository.ApplicationRepository;
import iadms4.repository.CompanyRepository;
import iadms4.repository.EvaluationRepository;
import iadms4.repository.OpeningRepository;
import iadms4.repository.PersonRepository;

/*
 * SERVICE. Builds summary reports.
 *
 * Responsibilities: counts students, interns by status, openings, applications by status, hours recorded
 * against hours required, and the average evaluation rating. The same numbers can be computed for the
 * whole system (coordinators) or for one company (representatives).
 */
public class ReportService {
    /*
     * One report: a plain bundle of numbers (a Java record, which is an immutable data holder).
     *
     * Its components are students (students who are not interns yet), interns (total interns), active
     * (interns with status ACTIVE), deactivated (interns with status DEACTIVATED), completed (interns with
     * status COMPLETED), openOpenings (openings that are OPEN), closedOpenings (openings that are CLOSED),
     * pending (applications that are PENDING), approved (applications that are APPROVED), rejected
     * (applications that are REJECTED), withdrawn (applications that are WITHDRAWN), recordedHours (total
     * hours logged by the interns), requiredHours (total hours the interns must complete), evaluations
     * (number of evaluations) and averageRating (average rating of the evaluations (0 when there are
     * none)).
     */
    public record Summary(int students, int interns, int active, int deactivated, int completed,
            int openOpenings, int closedOpenings, int pending, int approved, int rejected, int withdrawn,
            int recordedHours, int requiredHours, int evaluations, double averageRating) {
        /*
         * Computes the percentage of required hours already recorded.
         *
         * It returns the progress from 0 to 100 (0 when there are no required hours).
         */
        public int progressPercent() {
            return requiredHours == 0 ? 0 : recordedHours * 100 / requiredHours;
        }
    }

    /*
     * One row of the per-company breakdown.
     *
     * Its components are company (the company name) and summary (the numbers for that company).
     */
    public record CompanyLine(String company, Summary summary) {
    }

    // Used to count students and interns.
    private final PersonRepository persons;
    // Used to list the companies for the per-company report.
    private final CompanyRepository companies;
    // Used to count openings.
    private final OpeningRepository openings;
    // Used to count applications.
    private final ApplicationRepository applications;
    // Used to count and average evaluations.
    private final EvaluationRepository evaluations;

    /*
     * Creates the service.
     *
     * It takes persons (the person repository), companies (the company repository), openings (the opening
     * repository), applications (the application repository) and evaluations (the evaluation repository).
     */
    public ReportService(PersonRepository persons, CompanyRepository companies, OpeningRepository openings,
            ApplicationRepository applications, EvaluationRepository evaluations) {
        this.persons = persons;
        this.companies = companies;
        this.openings = openings;
        this.applications = applications;
        this.evaluations = evaluations;
    }

    /*
     * Builds the whole-system report.
     *
     * It returns the summary for every company together.
     */
    public Summary system() {
        return build(null);
    }

    /*
     * Builds a report limited to one company.
     *
     * It takes company (the company). It returns the summary for that company.
     */
    public Summary forCompany(Company company) {
        return build(company);
    }

    /*
     * Builds one report line per company.
     *
     * It returns a list with the name and summary of each company.
     */
    public List<CompanyLine> perCompany() {
        return companies.findAll().stream().map(c -> new CompanyLine(c.getName(), build(c))).toList();
    }

    /*
     * Does the actual counting for either the whole system or one company.
     *
     * It takes company (the company to limit the numbers to, or null for the whole system). It returns the
     * computed summary.
     */
    private Summary build(Company company) {
        List<Intern> interns = persons.findInterns().stream()
                .filter(i -> company == null || i.getPosition().getCompany().getId() == company.getId()).toList();
        List<Application> apps = company == null ? applications.findAll() : applications.findByCompany(company);
        List<Evaluation> evals = company == null ? evaluations.findAll() : evaluations.findByCompany(company);
        long openCount = openings.findAll().stream()
                .filter(o -> company == null || o.getPosition().getCompany().getId() == company.getId())
                .filter(o -> o.getStatus() == OpeningStatus.OPEN).count();
        long closedCount = openings.findAll().stream()
                .filter(o -> company == null || o.getPosition().getCompany().getId() == company.getId())
                .filter(o -> o.getStatus() == OpeningStatus.CLOSED).count();
        int recorded = interns.stream().mapToInt(Intern::getRecordedHours).sum();
        int required = interns.stream().mapToInt(i -> i.getPosition().getRequiredHours()).sum();
        double avg = evals.stream().mapToInt(Evaluation::getRating).average().orElse(0);
        return new Summary(
                persons.findStudents().size() - persons.findInterns().size(), interns.size(),
                countStatus(interns, InternStatus.ACTIVE), countStatus(interns, InternStatus.DEACTIVATED),
                countStatus(interns, InternStatus.COMPLETED), (int) openCount, (int) closedCount,
                countApps(apps, ApplicationStatus.PENDING), countApps(apps, ApplicationStatus.APPROVED),
                countApps(apps, ApplicationStatus.REJECTED), countApps(apps, ApplicationStatus.WITHDRAWN),
                recorded, required, evals.size(), avg);
    }

    /*
     * Counts the interns that have a given status.
     *
     * It takes interns (the interns to look through) and status (the status to count). It returns how many
     * interns have that status.
     */
    private static int countStatus(List<Intern> interns, InternStatus status) {
        return (int) interns.stream().filter(i -> i.getStatus() == status).count();
    }

    /*
     * Counts the applications that have a given status.
     *
     * It takes apps (the applications to look through) and status (the status to count). It returns how
     * many applications have that status.
     */
    private static int countApps(List<Application> apps, ApplicationStatus status) {
        return (int) apps.stream().filter(a -> a.getStatus() == status).count();
    }
}
