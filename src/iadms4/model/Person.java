package iadms4.model;

import java.time.LocalDate;

/*
 * MODEL. Abstract parent of every kind of user: Student, Intern, Coordinator and CompanyRepresentative.
 * It holds the details every person has. It is abstract, so only its subclasses can be created.
 */
public abstract class Person {
    private final int id;
    private String name;
    private Gender gender;
    private LocalDate birthdate;

    // Creates a person from the given id, name, gender and birthdate.
    public Person(int id, String name, Gender gender, LocalDate birthdate) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.birthdate = birthdate;
    }

    // Returns the unique id of this person.
    public int getId() {
        return id;
    }

    // Returns the full name.
    public String getName() {
        return name;
    }

    // Changes the full name to the given name.
    public void setName(String name) {
        this.name = name;
    }

    // Returns the gender.
    public Gender getGender() {
        return gender;
    }

    // Changes the gender to the given gender.
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    // Returns the date of birth.
    public LocalDate getBirthdate() {
        return birthdate;
    }

    // Changes the date of birth to the given birthdate.
    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }
}
