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

/**
 * SERVICE. Summary reports: counts of students, interns by status, openings, applications by status,
 * hours recorded against hours required, and the average evaluation rating. The same numbers can be
 * computed for the whole system (coordinators) or for one company (representatives).
 */
public class ReportService {
    /** One report: a plain bundle of numbers. */
    public record Summary(int students, int interns, int active, int deactivated, int completed,
            int openOpenings, int closedOpenings, int pending, int approved, int rejected, int withdrawn,
            int recordedHours, int requiredHours, int evaluations, double averageRating) {
        /** Percentage of required hours already recorded (0 when there are no interns). */
        public int progressPercent() {
            return requiredHours == 0 ? 0 : recordedHours * 100 / requiredHours;
        }
    }

    /** One row of the per-company breakdown. */
    public record CompanyLine(String company, Summary summary) {
    }

    private final PersonRepository persons;
    private final CompanyRepository companies;
    private final OpeningRepository openings;
    private final ApplicationRepository applications;
    private final EvaluationRepository evaluations;

    public ReportService(PersonRepository persons, CompanyRepository companies, OpeningRepository openings,
            ApplicationRepository applications, EvaluationRepository evaluations) {
        this.persons = persons;
        this.companies = companies;
        this.openings = openings;
        this.applications = applications;
        this.evaluations = evaluations;
    }

    /** Whole-system report. */
    public Summary system() {
        return build(null);
    }

    /** Report limited to one company. */
    public Summary forCompany(Company company) {
        return build(company);
    }

    /** One report line per company. */
    public List<CompanyLine> perCompany() {
        return companies.findAll().stream().map(c -> new CompanyLine(c.getName(), build(c))).toList();
    }

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

    private static int countStatus(List<Intern> interns, InternStatus status) {
        return (int) interns.stream().filter(i -> i.getStatus() == status).count();
    }

    private static int countApps(List<Application> apps, ApplicationStatus status) {
        return (int) apps.stream().filter(a -> a.getStatus() == status).count();
    }
}
