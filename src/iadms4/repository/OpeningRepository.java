package iadms4.repository;

import java.util.List;

import iadms4.model.Company;
import iadms4.model.Opening;
import iadms4.model.OpeningStatus;

/*
 * REPOSITORY. Stores Opening objects.
 *
 * Responsibilities: the basic operations from Repository plus filtering openings by status
 * or by company.
 */
public class OpeningRepository extends Repository<Opening> {
    /*
     * Reads the id of an opening so the base class can use it as the map key.
     *
     * It takes item (the opening). It returns the opening's id.
     */
    @Override
    protected int idOf(Opening item) {
        return item.getId();
    }

    /*
     * Lists the openings that have the given status.
     *
     * It takes status (OPEN or CLOSED). It returns the matching openings.
     */
    public List<Opening> findByStatus(OpeningStatus status) {
        return findAll().stream().filter(o -> o.getStatus() == status).toList();
    }

    /*
     * Lists the openings published by one company.
     *
     * It takes company (the company to filter by). It returns that company's openings.
     */
    public List<Opening> findByCompany(Company company) {
        return findAll().stream().filter(o -> o.getPosition().getCompany().getId() == company.getId()).toList();
    }
}
