package iadms4.model;

import java.time.LocalDate;

/*
 * MODEL. parent of Student, Intern, Coordinator, CompanyRepresentative
 * abstract so u cant make a plain Person
 */
public abstract class Person {
    private final int id;
    private String name;
    private Gender gender;
    private LocalDate birthdate;

    // constructor
    public Person(int id, String name, Gender gender, LocalDate birthdate) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.birthdate = birthdate;
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

    // getter
    public Gender getGender() {
        return gender;
    }

    // setter
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    // getter
    public LocalDate getBirthdate() {
        return birthdate;
    }

    // setter
    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }
}
