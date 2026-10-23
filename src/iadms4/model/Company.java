package iadms4.model;

/*
 * MODEL. A company that offers internships.
 *
 * Responsibilities: stores the id and name of the company. One company has many representatives and
 * many positions, which point back to it.
 */
public class Company {
    // Unique id of the company.
    private final int id;
    // Display name of the company.
    private String name;

    /*
     * Creates a company.
     *
     * It takes id (the unique id) and name (the company name).
     */
    public Company(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /*
     * Gets the id.
     *
     * It returns the company id.
     */
    public int getId() {
        return id;
    }

    /*
     * Gets the name.
     *
     * It returns the company name.
     */
    public String getName() {
        return name;
    }

    /*
     * Renames the company.
     *
     * It takes name (the new name).
     */
    public void setName(String name) {
        this.name = name;
    }
}
