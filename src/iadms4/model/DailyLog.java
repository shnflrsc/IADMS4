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

    // constructor
    public DailyLog(int id, Intern intern, LocalDate date, int hours, String description) {
        this.id = id;
        this.intern = intern;
        this.date = date;
        this.hours = hours;
        this.description = description;
    }

    // getter
    public int getId() {
        return id;
    }

    // getter
    public Intern getIntern() {
        return intern;
    }

    // getter
    public LocalDate getDate() {
        return date;
    }

    // getter
    public int getHours() {
        return hours;
    }

    // getter
    public String getDescription() {
        return description;
    }
}
