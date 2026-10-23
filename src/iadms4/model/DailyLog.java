package iadms4.model;

import java.time.LocalDate;

/*
 * MODEL. One entry written by an Intern every time they log hours.
 *
 * Responsibilities: stores the date, the hours credited and a short description of the work done.
 * Every field is final, so a log cannot be changed after it is created. Only company representatives
 * can read daily logs.
 */
public class DailyLog {
    // Unique id of the log entry.
    private final int id;
    // The intern who wrote the log.
    private final Intern intern;
    // The day the work was done.
    private final LocalDate date;
    // Hours credited for that day.
    private final int hours;
    // Short description of the work.
    private final String description;

    /*
     * Creates a daily log entry.
     *
     * It takes id (the unique id), intern (the intern who logged the hours), date (the date of the work),
     * hours (the hours credited) and description (what the intern did).
     */
    public DailyLog(int id, Intern intern, LocalDate date, int hours, String description) {
        this.id = id;
        this.intern = intern;
        this.date = date;
        this.hours = hours;
        this.description = description;
    }

    /*
     * Gets the id.
     *
     * It returns the log id.
     */
    public int getId() {
        return id;
    }

    /*
     * Gets the intern.
     *
     * It returns the intern who wrote this log.
     */
    public Intern getIntern() {
        return intern;
    }

    /*
     * Gets the date.
     *
     * It returns the day the work was done.
     */
    public LocalDate getDate() {
        return date;
    }

    /*
     * Gets the hours.
     *
     * It returns the hours credited.
     */
    public int getHours() {
        return hours;
    }

    /*
     * Gets the description.
     *
     * It returns the description of the work.
     */
    public String getDescription() {
        return description;
    }
}
