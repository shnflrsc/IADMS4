package iadms4.repository;

import java.util.ArrayList;
import java.util.List;

import iadms4.model.Company;
import iadms4.model.Opening;
import iadms4.model.OpeningStatus;

/*
 * REPOSITORY. Stores Opening objects and can filter them by status or by company.
 */
public class OpeningRepository extends Repository<Opening> {
    // Returns the id of the given opening.
    @Override
    protected int idOf(Opening item) {
        return item.getId();
    }

    // Returns the openings that have the given status.
    public List<Opening> findByStatus(OpeningStatus status) {
        List<Opening> matches = new ArrayList<>();
        for (Opening opening : findAll()) {
            if (opening.getStatus() == status) {
                matches.add(opening);
            }
        }
        return matches;
    }

    // Returns the openings published by the given company.
    public List<Opening> findByCompany(Company company) {
        List<Opening> matches = new ArrayList<>();
        for (Opening opening : findAll()) {
            if (opening.getPosition().getCompany().getId() == company.getId()) {
                matches.add(opening);
            }
        }
        return matches;
    }
}
