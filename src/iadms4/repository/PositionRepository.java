package iadms4.repository;

import java.util.List;

import iadms4.model.Company;
import iadms4.model.Position;

/*
 * REPOSITORY. Stores Position objects.
 *
 * Responsibilities: the basic operations from Repository plus listing the positions of one company.
 */
public class PositionRepository extends Repository<Position> {
    /*
     * Reads the id of a position so the base class can use it as the map key.
     *
     * It takes item (the position). It returns the position's id.
     */
    @Override
    protected int idOf(Position item) {
        return item.getId();
    }

    /*
     * Lists the positions offered by one company.
     *
     * It takes company (the company to filter by). It returns that company's positions.
     */
    public List<Position> findByCompany(Company company) {
        return findAll().stream().filter(p -> p.getCompany().getId() == company.getId()).toList();
    }
}
