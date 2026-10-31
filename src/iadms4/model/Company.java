package iadms4.model;

/*
 * MODEL. a company w/ internships
 * reps and positions point to this
 */
public class Company {
    private final int id;
    private String name;

    // makes a Company, takes id, name
    public Company(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // gets the unique id of this company (int)
    public int getId() {
        return id;
    }

    // gets the name shown for this company (String)
    public String getName() {
        return name;
    }

    // sets new name, takes a String
    public void setName(String name) {
        this.name = name;
    }
}
