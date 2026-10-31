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
 * SERVICE. intern stuff after deployment
 * logging hours + status changes. COMPLETED is final
 */
public class InternService {
    private final PersonRepository persons;
    private final DailyLogRepository logs;

    // makes the service, takes persons, logs
    public InternService(PersonRepository persons, DailyLogRepository logs) {
        this.persons = persons;
        this.logs = logs;
    }

    // log hours, gives back hours left
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
        // cap it so they dont go over required hours
        int credited = Math.min(hours, required - intern.getRecordedHours());
        int total = intern.getRecordedHours() + credited;
        intern.setRecordedHours(total);
        logs.save(new DailyLog(logs.nextId(), intern, LocalDate.now(), credited, description.trim()));
        return required - total;
    }

    // change status. COMPLETED needs full hours and cant be undone
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

    // interns of company by status, null = all
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

    // daily logs of this intern
    public List<DailyLog> logsOf(Intern intern) {
        return logs.findByInternId(intern.getId());
    }
}
