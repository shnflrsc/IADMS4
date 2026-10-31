package iadms4.model;

import java.time.LocalDate;

/*
 * MODEL. person who represents 1 company
 */
public class CompanyRepresentative extends Person {
    private final Company company;

    // makes a CompanyRepresentative, takes id, name, gender, birthdate, company
    public CompanyRepresentative(int id, String name, Gender gender, LocalDate birthdate, Company company) {
        super(id, name, gender, birthdate);
        this.company = company;
    }

    // gets company (Company)
    public Company getCompany() {
        return company;
    }
}
