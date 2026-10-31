package iadms4.model;

import java.time.LocalDate;

/*
 * MODEL. one log entry from an intern
 * everything is final so logs cant be changed
 */
public class DailyLog {
    private final int id;
    private final Intern intern;
    private final LocalDate date;
    private final int hours;
    private final String description;

    // makes a DailyLog, takes id, intern, date, hours, description
    public DailyLog(int id, Intern intern, LocalDate date, int hours, String description) {
        this.id = id;
        this.intern = intern;
        this.date = date;
        this.hours = hours;
        this.description = description;
    }

    // gets the unique id of this log (int)
    public int getId() {
        return id;
    }

    // gets the intern who wrote this log (Intern)
    public Intern getIntern() {
        return intern;
    }

    // gets the day this log was saved (LocalDate)
    public LocalDate getDate() {
        return date;
    }

    // gets how many hours were actually credited (int)
    public int getHours() {
        return hours;
    }

    // gets what the intern said they did (String)
    public String getDescription() {
        return description;
    }
}
