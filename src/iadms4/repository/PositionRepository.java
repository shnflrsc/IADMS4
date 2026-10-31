package iadms4.repository;

import java.util.ArrayList;
import java.util.List;

import iadms4.model.Company;
import iadms4.model.Position;

/*
 * REPOSITORY. Stores Position objects and can list the positions of one company.
 */
public class PositionRepository extends Repository<Position> {
    // Returns the id of the given position.
    @Override
    protected int idOf(Position item) {
        return item.getId();
    }

    // Returns the positions offered by the given company.
    public List<Position> findByCompany(Company company) {
        List<Position> matches = new ArrayList<>();
        for (Position position : findAll()) {
            if (position.getCompany().getId() == company.getId()) {
                matches.add(position);
            }
        }
        return matches;
    }
}
