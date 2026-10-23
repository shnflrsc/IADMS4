package iadms4.service;

import java.util.List;

import iadms4.model.Company;
import iadms4.repository.CompanyRepository;
import iadms4.repository.PersonRepository;
import iadms4.repository.PositionRepository;

/*
 * SERVICE. Business rules for companies.
 *
 * Responsibilities: creates, renames and deletes companies (names must be unique) and lists them.
 * A company can only be deleted when no representatives or positions depend on it.
 */
public class CompanyService {
    // Storage of companies.
    private final CompanyRepository companies;
    // Used to check whether a company still has representatives.
    private final PersonRepository persons;
    // Used to check whether a company still has positions.
    private final PositionRepository positions;

    /*
     * Creates the service.
     *
     * It takes companies (the company repository), persons (the person repository) and positions (the
     * position repository).
     */
    public CompanyService(CompanyRepository companies, PersonRepository persons, PositionRepository positions) {
        this.companies = companies;
        this.persons = persons;
        this.positions = positions;
    }

    /*
     * Creates and saves a new company.
     *
     * It takes name (the company name). It returns the saved company. It throws IllegalArgumentException if
     * the name is blank or already used.
     */
    public Company create(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Company name is required.");
        }
        if (companies.existsByName(name)) {
            throw new IllegalArgumentException("A company with that name already exists.");
        }
        return companies.save(new Company(companies.nextId(), name.trim()));
    }

    /*
     * Renames a company.
     *
     * It takes company (the company to rename) and name (the new name). It throws IllegalArgumentException
     * if the name is blank or another company already uses it.
     */
    public void rename(Company company, String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Company name is required.");
        }
        if (!company.getName().equalsIgnoreCase(name.trim()) && companies.existsByName(name)) {
            throw new IllegalArgumentException("A company with that name already exists.");
        }
        company.setName(name.trim());
    }

    /*
     * Deletes a company that has no representatives and no positions.
     *
     * It takes company (the company to delete). It throws IllegalStateException if the company still has
     * representatives or positions.
     */
    public void delete(Company company) {
        if (!persons.findRepresentatives(company).isEmpty()) {
            throw new IllegalStateException("Remove the company representatives first.");
        }
        if (!positions.findByCompany(company).isEmpty()) {
            throw new IllegalStateException("This company still has positions.");
        }
        companies.remove(company.getId());
    }

    /*
     * Lists every company.
     *
     * It returns all companies.
     */
    public List<Company> all() {
        return companies.findAll();
    }
}
