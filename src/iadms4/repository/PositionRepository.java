package iadms4.repository;

import java.util.ArrayList;
import java.util.List;

import iadms4.model.Company;
import iadms4.model.Position;

/*
 * REPOSITORY. stores positions
 */
public class PositionRepository extends Repository<Position> {
    // tells the base class how to get a position's id
    @Override
    protected int idOf(Position item) {
        return item.getId();
    }

    // goes through all positions and keeps the ones of this company
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
