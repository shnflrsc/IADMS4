package iadms4.model;

/*
 * MODEL. a job in a company like Backend Developer
 * has to become an Opening before anyone can be endorsed to it
 */
public class Position {
    private final int id;
    private final Company company;
    private String title;
    private int requiredHours;

    // constructor
    public Position(int id, Company company, String title, int requiredHours) {
        this.id = id;
        this.company = company;
        this.title = title;
        this.requiredHours = requiredHours;
    }

    // getter
    public int getId() {
        return id;
    }

    // getter
    public Company getCompany() {
        return company;
    }

    // getter
    public String getTitle() {
        return title;
    }

    // setter
    public void setTitle(String title) {
        this.title = title;
    }

    // getter
    public int getRequiredHours() {
        return requiredHours;
    }

    // setter
    public void setRequiredHours(int requiredHours) {
        this.requiredHours = requiredHours;
    }
}
