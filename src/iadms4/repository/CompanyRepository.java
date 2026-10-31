package iadms4.repository;

import iadms4.model.Company;

/*
 * REPOSITORY. stores companies, checks duplicate names
 */
public class CompanyRepository extends Repository<Company> {
    // id of company
    @Override
    protected int idOf(Company item) {
        return item.getId();
    }

    // true if name is taken. ignores caps and spaces
    public boolean existsByName(String name) {
        for (Company company : findAll()) {
            if (company.getName().equalsIgnoreCase(name.trim())) {
                return true;
            }
        }
        return false;
    }
}
