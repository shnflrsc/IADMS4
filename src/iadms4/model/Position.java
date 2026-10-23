package iadms4.model;

/*
 * MODEL. A kind of job inside a Company (for example Backend Developer).
 *
 * Responsibilities: stores the job title and the number of hours an intern must complete. A Position
 * cannot be applied to directly - it must first be published as an Opening.
 */
public class Position {
    // Unique id of the position.
    private final int id;
    // The company that offers this position.
    private final Company company;
    // Job title shown to users.
    private String title;
    // Hours an intern must finish before the internship can be completed.
    private int requiredHours;

    /*
     * Creates a position.
     *
     * It takes id (the unique id), company (the company that offers the position), title (the job title)
     * and requiredHours (the hours needed to finish the internship).
     */
    public Position(int id, Company company, String title, int requiredHours) {
        this.id = id;
        this.company = company;
        this.title = title;
        this.requiredHours = requiredHours;
    }

    /*
     * Gets the id.
     *
     * It returns the position id.
     */
    public int getId() {
        return id;
    }

    /*
     * Gets the company.
     *
     * It returns the company that offers this position.
     */
    public Company getCompany() {
        return company;
    }

    /*
     * Gets the job title.
     *
     * It returns the title.
     */
    public String getTitle() {
        return title;
    }

    /*
     * Changes the job title.
     *
     * It takes title (the new title).
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /*
     * Gets the required hours.
     *
     * It returns the hours an intern must complete.
     */
    public int getRequiredHours() {
        return requiredHours;
    }

    /*
     * Changes the required hours.
     *
     * It takes requiredHours (the new number of hours).
     */
    public void setRequiredHours(int requiredHours) {
        this.requiredHours = requiredHours;
    }
}
