package iadms4.model;

/*
 * MODEL. A kind of job inside a Company, for example Backend Developer.
 * It stores the job title and the hours an intern must complete. A position must be published
 * as an Opening before students can be endorsed to it.
 */
public class Position {
    private final int id;
    private final Company company;
    private String title;
    private int requiredHours;

    // Creates a position with the given id, company, title and requiredHours.
    public Position(int id, Company company, String title, int requiredHours) {
        this.id = id;
        this.company = company;
        this.title = title;
        this.requiredHours = requiredHours;
    }

    // Returns the position id.
    public int getId() {
        return id;
    }

    // Returns the company that offers this position.
    public Company getCompany() {
        return company;
    }

    // Returns the job title.
    public String getTitle() {
        return title;
    }

    // Changes the job title to the given title.
    public void setTitle(String title) {
        this.title = title;
    }

    // Returns the hours an intern must complete.
    public int getRequiredHours() {
        return requiredHours;
    }

    // Changes the required hours to the given requiredHours.
    public void setRequiredHours(int requiredHours) {
        this.requiredHours = requiredHours;
    }
}
