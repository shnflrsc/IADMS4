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

    // gets id (int)
    public int getId() {
        return id;
    }

    // gets intern (Intern)
    public Intern getIntern() {
        return intern;
    }

    // gets date (LocalDate)
    public LocalDate getDate() {
        return date;
    }

    // gets hours (int)
    public int getHours() {
        return hours;
    }

    // gets description (String)
    public String getDescription() {
        return description;
    }
}
