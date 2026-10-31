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

    // makes a Position, takes id, company, title, requiredHours
    public Position(int id, Company company, String title, int requiredHours) {
        this.id = id;
        this.company = company;
        this.title = title;
        this.requiredHours = requiredHours;
    }

    // gets the unique id of this position (int)
    public int getId() {
        return id;
    }

    // gets the company offering this position (Company)
    public Company getCompany() {
        return company;
    }

    // gets the job title shown to users (String)
    public String getTitle() {
        return title;
    }

    // sets new title, takes a String
    public void setTitle(String title) {
        this.title = title;
    }

    // gets how many hours an intern has to finish (int)
    public int getRequiredHours() {
        return requiredHours;
    }

    // sets new required hours, takes an int
    public void setRequiredHours(int requiredHours) {
        this.requiredHours = requiredHours;
    }
}
