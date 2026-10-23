package iadms4.repository;

import iadms4.model.Company;

/*
 * REPOSITORY. Stores Company objects.
 *
 * Responsibilities: the basic operations from Repository plus a check for duplicate company names.
 */
public class CompanyRepository extends Repository<Company> {
    /*
     * Reads the id of a company so the base class can use it as the map key.
     *
     * It takes item (the company). It returns the company's id.
     */
    @Override
    protected int idOf(Company item) {
        return item.getId();
    }

    /*
     * Checks whether a company with this name already exists (capital letters and outer spaces are ignored).
     *
     * It takes name (the name to check). It returns true if a company already uses that name.
     */
    public boolean existsByName(String name) {
        return findAll().stream().anyMatch(c -> c.getName().equalsIgnoreCase(name.trim()));
    }
}
