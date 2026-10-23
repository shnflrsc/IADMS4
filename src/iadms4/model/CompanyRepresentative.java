package iadms4.model;

import java.time.LocalDate;

/*
 * MODEL. A person who represents exactly one Company. It extends Person.
 *
 * Responsibilities: remembers which company the person works for. The representative manages that
 * company's positions and openings, approves or denies endorsements, monitors interns, reads daily logs
 * and writes evaluations.
 */
public class CompanyRepresentative extends Person {
    // The company this representative works for.
    private final Company company;

    /*
     * Creates a company representative.
     *
     * It takes id (the unique person id), name (the full name), gender (the gender), birthdate (the date of
     * birth) and company (the company the person represents).
     */
    public CompanyRepresentative(int id, String name, Gender gender, LocalDate birthdate, Company company) {
        super(id, name, gender, birthdate);
        this.company = company;
    }

    /*
     * Gets the company of this representative.
     *
     * It returns the company the person works for.
     */
    public Company getCompany() {
        return company;
    }
}
