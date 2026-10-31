package iadms4.service;

import java.util.ArrayList;
import java.util.List;

import iadms4.model.Application;
import iadms4.model.ApplicationStatus;
import iadms4.model.Company;
import iadms4.model.Evaluation;
import iadms4.model.Intern;
import iadms4.model.InternStatus;
import iadms4.model.Opening;
import iadms4.model.OpeningStatus;
import iadms4.model.Summary;
import iadms4.repository.ApplicationRepository;
import iadms4.repository.CompanyRepository;
import iadms4.repository.EvaluationRepository;
import iadms4.repository.OpeningRepository;
import iadms4.repository.PersonRepository;

/*
 * SERVICE. builds the reports
 * same code for whole system or one company
 */
public class ReportService {
    private final PersonRepository persons;
    private final CompanyRepository companies;
    private final OpeningRepository openings;
    private final ApplicationRepository applications;
    private final EvaluationRepository evaluations;

    // makes the service, takes persons, companies, openings, applications, evaluations
    public ReportService(PersonRepository persons, CompanyRepository companies, OpeningRepository openings,
            ApplicationRepository applications, EvaluationRepository evaluations) {
        this.persons = persons;
        this.companies = companies;
        this.openings = openings;
        this.applications = applications;
        this.evaluations = evaluations;
    }

    // whole system
    public Summary system() {
        return build(null);
    }

    // one company
    public Summary forCompany(Company company) {
        return build(company);
    }

    // one report per company, same order as companies list
    public List<Summary> perCompany() {
        List<Summary> summaries = new ArrayList<>();
        for (Company company : companies.findAll()) {
            summaries.add(build(company));
        }
        return summaries;
    }

    // the actual counting. company null = whole system
    private Summary build(Company company) {
        int interns = 0;
        int active = 0;
        int deactivated = 0;
        int completed = 0;
        int recorded = 0;
        int required = 0;
        for (Intern intern : persons.findInterns()) {
            if (company == null || intern.getPosition().getCompany().getId() == company.getId()) {
                interns++;
                recorded += intern.getRecordedHours();
                required += intern.getPosition().getRequiredHours();
                if (intern.getStatus() == InternStatus.ACTIVE) {
                    active++;
                } else if (intern.getStatus() == InternStatus.DEACTIVATED) {
                    deactivated++;
                } else {
                    completed++;
                }
            }
        }

        int open = 0;
        int closed = 0;
        for (Opening opening : openings.findAll()) {
            if (company == null || opening.getPosition().getCompany().getId() == company.getId()) {
                if (opening.getStatus() == OpeningStatus.OPEN) {
                    open++;
                } else {
                    closed++;
                }
            }
        }

        List<Application> apps = applications.findAll();
        List<Evaluation> evals = evaluations.findAll();
        if (company != null) {
            apps = applications.findByCompany(company);
            evals = evaluations.findByCompany(company);
        }

        double average = 0;
        if (!evals.isEmpty()) {
            int total = 0;
            for (Evaluation evaluation : evals) {
                total += evaluation.getRating();
            }
            average = (double) total / evals.size();
        }

        int students = persons.findStudents().size() - persons.findInterns().size();
        return new Summary(students, interns, active, deactivated, completed, open, closed,
                countApplications(apps, ApplicationStatus.PENDING), countApplications(apps, ApplicationStatus.APPROVED),
                countApplications(apps, ApplicationStatus.REJECTED), countApplications(apps, ApplicationStatus.WITHDRAWN),
                recorded, required, evals.size(), average);
    }

    // count apps by status
    private int countApplications(List<Application> apps, ApplicationStatus status) {
        int count = 0;
        for (Application application : apps) {
            if (application.getStatus() == status) {
                count++;
            }
        }
        return count;
    }
}
