package iadms4.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import iadms4.model.Company;
import iadms4.model.DailyLog;
import iadms4.model.Intern;
import iadms4.model.InternStatus;
import iadms4.repository.DailyLogRepository;
import iadms4.repository.PersonRepository;

/*
 * SERVICE. Business rules for what happens to an intern after deployment.
 * It handles logging hours and status changes. COMPLETED is set by the representative and is final.
 */
public class InternService {
    private final PersonRepository persons;
    private final DailyLogRepository logs;

    // Creates the service using the given repositories.
    public InternService(PersonRepository persons, DailyLogRepository logs) {
        this.persons = persons;
        this.logs = logs;
    }

    // Logs hours with a description for the given intern and returns the hours still needed.
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
        intern.setRecordedHours(total);
        logs.save(new DailyLog(logs.nextId(), intern, LocalDate.now(), credited, description.trim()));
        return required - total;
    }

    // Sets the status of the given intern. COMPLETED needs the required hours, and it cannot be changed later.
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

    // Returns the interns of the given company with the given status, or all of them if status is null.
    public List<Intern> internsOf(Company company, InternStatus status) {
        List<Intern> matches = new ArrayList<>();
        for (Intern intern : persons.findInterns()) {
            if (intern.getPosition().getCompany().getId() == company.getId()
                    && (status == null || intern.getStatus() == status)) {
                matches.add(intern);
            }
        }
        return matches;
    }

    // Returns the daily logs of the given intern.
    public List<DailyLog> logsOf(Intern intern) {
        return logs.findByInternId(intern.getId());
    }
}
