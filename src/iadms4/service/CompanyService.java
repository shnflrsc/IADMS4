package iadms4.service;

import java.util.List;

import iadms4.model.Company;
import iadms4.repository.CompanyRepository;
import iadms4.repository.PersonRepository;
import iadms4.repository.PositionRepository;

/*
 * SERVICE. company rules
 * names unique, cant delete if something depends on it
 */
public class CompanyService {
    private final CompanyRepository companies;
    private final PersonRepository persons;
    private final PositionRepository positions;

    // makes the service, takes companies, persons, positions
    public CompanyService(CompanyRepository companies, PersonRepository persons, PositionRepository positions) {
        this.companies = companies;
        this.persons = persons;
        this.positions = positions;
    }

    // new company
    public void create(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Company name is required.");
        }
        if (companies.existsByName(name)) {
            throw new IllegalArgumentException("A company with that name already exists.");
        }
        companies.save(new Company(companies.nextId(), name.trim()));
    }

    // rename
    public void rename(Company company, String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Company name is required.");
        }
        if (!company.getName().equalsIgnoreCase(name.trim()) && companies.existsByName(name)) {
            throw new IllegalArgumentException("A company with that name already exists.");
        }
        company.setName(name.trim());
    }

    // delete, only if no reps and no positions
    public void delete(Company company) {
        if (!persons.findRepresentatives(company).isEmpty()) {
            throw new IllegalStateException("Remove the company representatives first.");
        }
        if (!positions.findByCompany(company).isEmpty()) {
            throw new IllegalStateException("This company still has positions.");
        }
        companies.remove(company.getId());
    }

    // all companies
    public List<Company> all() {
        return companies.findAll();
    }
}
