package iadms4.repository;

import java.util.List;

import iadms4.model.Company;
import iadms4.model.Position;

/**
 * REPOSITORY. Stores Positions; can list the positions of one company.
 */
public class PositionRepository extends Repository<Position> {
    @Override
    protected int idOf(Position item) {
        return item.getId();
    }

    public List<Position> findByCompany(Company company) {
        return findAll().stream().filter(p -> p.getCompany().getId() == company.getId()).toList();
    }
}
