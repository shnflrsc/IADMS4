package iadms4.service;

import java.time.LocalDate;
import java.util.List;

import iadms4.model.Company;
import iadms4.model.DailyLog;
import iadms4.model.Intern;
import iadms4.model.InternStatus;
import iadms4.repository.DailyLogRepository;
import iadms4.repository.PersonRepository;

/*
 * SERVICE. Business rules for everything that happens to an intern after deployment.
 *
 * Responsibilities: logging hours (which creates a DailyLog), the representative's status
 * changes (COMPLETED is manual and final), and listing interns or logs.
 */
public class InternService {
    // Used to look up interns.
    private final PersonRepository persons;
    // Storage of daily logs.
    private final DailyLogRepository logs;

    /*
     * Creates the service.
     *
     * It takes persons (the person repository) and logs (the daily log repository).
     */
    public InternService(PersonRepository persons, DailyLogRepository logs) {
        this.persons = persons;
        this.logs = logs;
    }

    /*
     * Logs hours with a daily log entry. Reaching the requirement does NOT complete the internship:
     * the company representative does that manually (see changeStatus).
     *
     * It takes intern (the intern logging hours), hours (the hours worked) and description (what the intern
     * did). It returns the number of hours still needed to reach the requirement. It throws
     * IllegalStateException if the intern is not ACTIVE or already met the required hours. It throws
     * IllegalArgumentException if the hours are not positive or the description is blank.
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

    /*
     * A representative sets the status of an intern. COMPLETED is manual, needs the required hours, and is
     * final (a completed internship and its logs are never reopened).
     *
     * It takes intern (the intern whose status changes) and status (the new status). It throws
     * IllegalStateException if the intern is already COMPLETED, or the hours are not met yet.
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

    /*
     * Lists the interns of a company, optionally filtered by status.
     *
     * It takes company (the company) and status (the status to keep, or null for no filter). It returns the
     * matching interns.
     */
    public List<Intern> internsOf(Company company, InternStatus status) {
        return persons.findInterns().stream()
                .filter(i -> i.getPosition().getCompany().getId() == company.getId())
                .filter(i -> status == null || i.getStatus() == status).toList();
    }

    /*
     * Lists all interns of a company regardless of status.
     *
     * It takes company (the company). It returns that company's interns.
     */
    public List<Intern> internsOf(Company company) {
        return internsOf(company, null);
    }

    /*
     * Lists the daily logs of an intern.
     *
     * It takes intern (the intern). It returns the intern's logs.
     */
    public List<DailyLog> logsOf(Intern intern) {
        return logs.findByInternId(intern.getId());
    }
}
