package iadms4.repository;

import java.util.ArrayList;
import java.util.List;

import iadms4.model.Company;
import iadms4.model.Opening;
import iadms4.model.OpeningStatus;

/*
 * REPOSITORY. stores openings, filter by status or company
 */
public class OpeningRepository extends Repository<Opening> {
    // tells the base class how to get an opening's id
    @Override
    protected int idOf(Opening item) {
        return item.getId();
    }

    // goes through all openings and keeps the ones w/ this status
    public List<Opening> findByStatus(OpeningStatus status) {
        List<Opening> matches = new ArrayList<>();
        for (Opening opening : findAll()) {
            if (opening.getStatus() == status) {
                matches.add(opening);
            }
        }
        return matches;
    }

    // goes through all openings and keeps the ones of this company
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
