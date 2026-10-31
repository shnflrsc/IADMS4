package iadms4.model;

import java.time.LocalDate;

/*
 * MODEL. person who represents 1 company
 */
public class CompanyRepresentative extends Person {
    private final Company company;

    // constructor
    public CompanyRepresentative(int id, String name, Gender gender, LocalDate birthdate, Company company) {
        super(id, name, gender, birthdate);
        this.company = company;
    }

    // getter
    public Company getCompany() {
        return company;
    }
}
