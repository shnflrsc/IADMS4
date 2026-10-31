package iadms4.model;

/*
 * MODEL. a company w/ internships
 * reps and positions point to this
 */
public class Company {
    private final int id;
    private String name;

    // constructor
    public Company(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // getter
    public int getId() {
        return id;
    }

    // getter
    public String getName() {
        return name;
    }

    // setter
    public void setName(String name) {
        this.name = name;
    }
}
