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

    // makes a Person, takes id, name, gender, birthdate
    public Person(int id, String name, Gender gender, LocalDate birthdate) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.birthdate = birthdate;
    }

    // gets the unique id of this person (int)
    public int getId() {
        return id;
    }

    // gets the full name of this person (String)
    public String getName() {
        return name;
    }

    // sets new name, takes a String
    public void setName(String name) {
        this.name = name;
    }

    // gets the gender of this person (Gender)
    public Gender getGender() {
        return gender;
    }

    // sets new gender, takes a Gender
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    // gets the date of birth of this person (LocalDate)
    public LocalDate getBirthdate() {
        return birthdate;
    }

    // sets new birthdate, takes a LocalDate
    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }
}
