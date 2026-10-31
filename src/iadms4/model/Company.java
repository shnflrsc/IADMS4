package iadms4.model;

/*
 * MODEL. A company that offers internships.
 * Representatives and positions point back to their company.
 */
public class Company {
    private final int id;
    private String name;

    // Creates a company with the given id and name.
    public Company(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Returns the company id.
    public int getId() {
        return id;
    }

    // Returns the company name.
    public String getName() {
        return name;
    }

    // Changes the company name to the given name.
    public void setName(String name) {
        this.name = name;
    }
}
