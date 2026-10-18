package iadms4.model;

import java.time.LocalDate;

/**
 * MODEL. One entry written by an Intern every time they log hours: date, hours credited and a short
 * description of the work. Only company representatives can read daily logs.
 */
public class DailyLog {
    private final int id;
    private final Intern intern;
    private final LocalDate date;
    private final int hours;
    private final String description;

    public DailyLog(int id, Intern intern, LocalDate date, int hours, String description) {
        this.id = id;
        this.intern = intern;
        this.date = date;
        this.hours = hours;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public Intern getIntern() {
        return intern;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getHours() {
        return hours;
    }

    public String getDescription() {
        return description;
    }
}
