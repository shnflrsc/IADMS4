package iadms4.repository;

import iadms4.model.Company;

/*
 * REPOSITORY. Stores Company objects and can check for duplicate names.
 */
public class CompanyRepository extends Repository<Company> {
    // Returns the id of the given company.
    @Override
    protected int idOf(Company item) {
        return item.getId();
    }

    // Returns true if a company already uses the given name, ignoring capital letters and outer spaces.
    public boolean existsByName(String name) {
        for (Company company : findAll()) {
            if (company.getName().equalsIgnoreCase(name.trim())) {
                return true;
            }
        }
        return false;
    }
}
