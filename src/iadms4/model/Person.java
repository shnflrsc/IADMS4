package iadms4.model;

import java.time.LocalDate;

/*
 * MODEL. Abstract parent of every kind of user: Student, Intern, Coordinator and CompanyRepresentative.
 *
 * Responsibilities: holds the data every person has (id, name, gender and birthdate). Because the class
 * is abstract it cannot be created directly; only its subclasses can. The concrete subclass decides which
 * dashboard a person sees after logging in (see App.showDashboard).
 */
public abstract class Person {
    // Unique number of this person. It never changes, so it is final.
    private final int id;
    // Full name of the person.
    private String name;
    // Gender of the person.
    private Gender gender;
    // Date the person was born.
    private LocalDate birthdate;

    /*
     * Creates a person with all of the shared details.
     *
     * It takes id (the unique id of the person), name (the full name), gender (the gender) and birthdate
     * (the date of birth).
     */
    public Person(int id, String name, Gender gender, LocalDate birthdate) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.birthdate = birthdate;
    }

    /*
     * Gets the unique id.
     *
     * It returns the id of this person.
     */
    public int getId() {
        return id;
    }

    /*
     * Gets the full name.
     *
     * It returns the name of this person.
     */
    public String getName() {
        return name;
    }

    /*
     * Changes the full name.
     *
     * It takes name (the new name).
     */
    public void setName(String name) {
        this.name = name;
    }

    /*
     * Gets the gender.
     *
     * It returns the gender of this person.
     */
    public Gender getGender() {
        return gender;
    }

    /*
     * Changes the gender.
     *
     * It takes gender (the new gender).
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    /*
     * Gets the date of birth.
     *
     * It returns the birthdate of this person.
     */
    public LocalDate getBirthdate() {
        return birthdate;
    }

    /*
     * Changes the date of birth.
     *
     * It takes birthdate (the new birthdate).
     */
    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }
}
