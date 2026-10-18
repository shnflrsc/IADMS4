package iadms4.model;

/**
 * MODEL. A company that offers internships. One company has many representatives and positions.
 */
public class Company {
    private final int id;
    private String name;

    public Company(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
