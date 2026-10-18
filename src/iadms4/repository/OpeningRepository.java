package iadms4.repository;

import java.util.List;

import iadms4.model.Company;
import iadms4.model.Opening;
import iadms4.model.OpeningStatus;

/**
 * REPOSITORY. Stores Openings; can filter by status or by company.
 */
public class OpeningRepository extends Repository<Opening> {
    @Override
    protected int idOf(Opening item) {
        return item.getId();
    }

    public List<Opening> findByStatus(OpeningStatus status) {
        return findAll().stream().filter(o -> o.getStatus() == status).toList();
    }

    public List<Opening> findByCompany(Company company) {
        return findAll().stream().filter(o -> o.getPosition().getCompany().getId() == company.getId()).toList();
    }
}
