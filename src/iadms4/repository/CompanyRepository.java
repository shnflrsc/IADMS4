package iadms4.repository;

import iadms4.model.Company;

/**
 * REPOSITORY. Stores Companies.
 */
public class CompanyRepository extends Repository<Company> {
    @Override
    protected int idOf(Company item) {
        return item.getId();
    }

    public boolean existsByName(String name) {
        return findAll().stream().anyMatch(c -> c.getName().equalsIgnoreCase(name.trim()));
    }
}
