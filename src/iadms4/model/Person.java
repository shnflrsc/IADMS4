package iadms4.model;

import java.time.LocalDate;

/**
 * MODEL. Abstract parent of every kind of user: Student, Intern, Coordinator and CompanyRepresentative.
 * Holds the data every person has (id, name, gender, birthdate). The concrete subclass decides which
 * dashboard a person sees after logging in (see App.showDashboard).
 */
public abstract class Person {
    private final int id;
    private String name;
    private Gender gender;
    private LocalDate birthdate;
    
    public Person(int id, String name, Gender gender, LocalDate birthdate) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.birthdate = birthdate;
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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }
}
