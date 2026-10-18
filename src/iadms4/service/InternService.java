package iadms4.service;

import java.time.LocalDate;
import java.util.List;

import iadms4.model.Company;
import iadms4.model.DailyLog;
import iadms4.model.Intern;
import iadms4.model.InternStatus;
import iadms4.repository.DailyLogRepository;
import iadms4.repository.PersonRepository;

/**
 * SERVICE. Everything an intern does after deployment: log hours (creates a DailyLog), the
 * representative status changes (COMPLETED is manual and final), and listing interns or logs.
 */
public class InternService {
    private final PersonRepository persons;
    private final DailyLogRepository logs;

    public InternService(PersonRepository persons, DailyLogRepository logs) {
        this.persons = persons;
        this.logs = logs;
    }

    /**
     * Logs hours with a daily log entry. Returns hours remaining. Reaching the requirement does NOT
     * complete the internship: the company representative does that manually (changeStatus).
     */
    public int logHours(Intern intern, int hours, String description) {
        if (intern.getStatus() != InternStatus.ACTIVE) {
            throw new IllegalStateException("Only ACTIVE interns can log hours (status: " + intern.getStatus() + ").");
        }
        if (hours <= 0) {
            throw new IllegalArgumentException("Hours must be greater than zero.");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("A daily log description is required.");
        }
        int required = intern.getPosition().getRequiredHours();
        if (intern.getRecordedHours() >= required) {
            throw new IllegalStateException("Required hours already met; wait for your company representative to mark the internship COMPLETED.");
        }
        // Never credit more than what is left to reach the requirement.
        int credited = Math.min(hours, required - intern.getRecordedHours());
        int total = intern.getRecordedHours() + credited;
        // The Intern object is shared with the repository, so changing it here is enough (no save needed).
        intern.setRecordedHours(total);
        // Every logging also stores a DailyLog (only company representatives can read these).
        logs.save(new DailyLog(logs.nextId(), intern, LocalDate.now(), credited, description.trim()));
        return required - total;
    }

    /**
     * Representative sets the status. COMPLETED is manual, needs the required hours, and is final
     * (a completed internship and its logs are never reopened).
     */
    public void changeStatus(Intern intern, InternStatus status) {
        if (intern.getStatus() == InternStatus.COMPLETED) {
            throw new IllegalStateException("A COMPLETED internship cannot be changed.");
        }
        if (status == InternStatus.COMPLETED
                && intern.getRecordedHours() < intern.getPosition().getRequiredHours()) {
            throw new IllegalStateException("Cannot complete yet: " + intern.getRecordedHours() + "/"
                    + intern.getPosition().getRequiredHours() + " hours recorded.");
        }
        intern.setStatus(status);
    }

    /** Interns of a company; a null status means no filter. */
    public List<Intern> internsOf(Company company, InternStatus status) {
        return persons.findInterns().stream()
                .filter(i -> i.getPosition().getCompany().getId() == company.getId())
                .filter(i -> status == null || i.getStatus() == status).toList();
    }

    public List<Intern> internsOf(Company company) {
        return internsOf(company, null);
    }

    public List<DailyLog> logsOf(Intern intern) {
        return logs.findByInternId(intern.getId());
    }
}
