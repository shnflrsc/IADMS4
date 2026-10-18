package iadms4.model;

/**
 * MODEL. A kind of job inside a company (for example Backend Developer) with the number of hours an
 * intern must complete. A Position is not directly applyable - it must be published as an Opening.
 */
public class Position {
    private final int id;
    private final Company company;
    private String title;
    private int requiredHours;

    public Position(int id, Company company, String title, int requiredHours) {
        this.id = id;
        this.company = company;
        this.title = title;
        this.requiredHours = requiredHours;
    }

    public int getId() {
        return id;
    }

    public Company getCompany() {
        return company;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRequiredHours() {
        return requiredHours;
    }

    public void setRequiredHours(int requiredHours) {
        this.requiredHours = requiredHours;
    }
}
