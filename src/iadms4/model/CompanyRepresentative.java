package iadms4.model;

import java.time.LocalDate;

/*
 * MODEL. A person who represents exactly one Company.
 * It adds the company to Person.
 */
public class CompanyRepresentative extends Person {
    private final Company company;

    // Creates a representative from the Person details plus the company they work for.
    public CompanyRepresentative(int id, String name, Gender gender, LocalDate birthdate, Company company) {
        super(id, name, gender, birthdate);
        this.company = company;
    }

    // Returns the company this representative works for.
    public Company getCompany() {
        return company;
    }
}
