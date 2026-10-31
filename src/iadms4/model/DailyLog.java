package iadms4.model;

import java.time.LocalDate;

/*
 * MODEL. One entry written by an Intern every time they log hours.
 * Every field is final, so a log cannot be changed after it is created.
 */
public class DailyLog {
    private final int id;
    private final Intern intern;
    private final LocalDate date;
    private final int hours;
    private final String description;

    // Creates a log entry with the given id, intern, date, credited hours and description.
    public DailyLog(int id, Intern intern, LocalDate date, int hours, String description) {
        this.id = id;
        this.intern = intern;
        this.date = date;
        this.hours = hours;
        this.description = description;
    }

    // Returns the log id.
    public int getId() {
        return id;
    }

    // Returns the intern who wrote this log.
    public Intern getIntern() {
        return intern;
    }

    // Returns the day the work was done.
    public LocalDate getDate() {
        return date;
    }

    // Returns the hours credited for that day.
    public int getHours() {
        return hours;
    }

    // Returns the description of the work done.
    public String getDescription() {
        return description;
    }
}
