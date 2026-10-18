package iadms4.model;

import java.time.LocalDate;

/**
 * MODEL. Works for exactly one Company. Manages that company positions and openings, approves or
 * denies endorsements, monitors interns, reads daily logs and writes evaluations.
 */
public class CompanyRepresentative extends Person {
    private final Company company;

    public CompanyRepresentative(int id, String name, Gender gender, LocalDate birthdate, Company company) {
        super(id, name, gender, birthdate);
        this.company = company;
    }

    public Company getCompany() {
        return company;
    }
}
