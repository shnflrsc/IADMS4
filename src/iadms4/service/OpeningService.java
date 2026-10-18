package iadms4.service;

import java.util.List;

import iadms4.model.Company;
import iadms4.model.Opening;
import iadms4.model.OpeningStatus;
import iadms4.model.Position;
import iadms4.repository.ApplicationRepository;
import iadms4.repository.OpeningRepository;
import iadms4.repository.PersonRepository;
import iadms4.repository.PositionRepository;

/**
 * SERVICE. Positions and openings: create a position, publish an opening, open or close it, and
 * edit or delete them (with safety rules), and list them by company.
 */
public class OpeningService {
    private final PositionRepository positions;
    private final OpeningRepository openings;
    private final PersonRepository persons;
    private final ApplicationRepository applications;

    public OpeningService(PositionRepository positions, OpeningRepository openings, PersonRepository persons,
            ApplicationRepository applications) {
        this.positions = positions;
        this.openings = openings;
        this.persons = persons;
        this.applications = applications;
    }

    public Position createPosition(Company company, String title, int requiredHours) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Position title is required.");
        }
        if (requiredHours <= 0) {
            throw new IllegalArgumentException("Required hours must be greater than zero.");
        }
        return positions.save(new Position(positions.nextId(), company, title.trim(), requiredHours));
    }

    /** Edits a position of the representative company; blocked once an intern is deployed to it. */
    public void updatePosition(Company company, Position position, String title, int requiredHours) {
        requireOwn(company, position);
        if (persons.findInterns().stream().anyMatch(i -> i.getPosition().getId() == position.getId())) {
            throw new IllegalStateException("Interns are deployed to this position; it can no longer be edited.");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Position title is required.");
        }
        if (requiredHours <= 0) {
            throw new IllegalArgumentException("Required hours must be greater than zero.");
        }
        position.setTitle(title.trim());
        position.setRequiredHours(requiredHours);
    }

    /** Deletes a position that has no openings. */
    public void deletePosition(Company company, Position position) {
        requireOwn(company, position);
        if (openings.findAll().stream().anyMatch(o -> o.getPosition().getId() == position.getId())) {
            throw new IllegalStateException("This position has openings; delete them first.");
        }
        positions.remove(position.getId());
    }

    /** Deletes an opening nobody was ever endorsed to (otherwise close it instead). */
    public void deleteOpening(Company company, Opening opening) {
        requireOwn(company, opening.getPosition());
        if (applications.findAll().stream().anyMatch(a -> a.getOpening().getId() == opening.getId())) {
            throw new IllegalStateException("This opening has applications; close it instead of deleting it.");
        }
        openings.remove(opening.getId());
    }

    private void requireOwn(Company company, Position position) {
        if (position.getCompany().getId() != company.getId()) {
            throw new IllegalStateException("That position belongs to another company.");
        }
    }

    public Opening createOpening(Position position) {
        return openings.save(new Opening(openings.nextId(), position, OpeningStatus.OPEN));
    }

    public void setStatus(Opening opening, OpeningStatus status) {
        opening.setStatus(status);
    }

    public List<Position> positionsOf(Company company) {
        return positions.findByCompany(company);
    }

    public List<Opening> openingsOf(Company company) {
        return openings.findByCompany(company);
    }

    public List<Opening> allOpenings() {
        return openings.findAll();
    }

    public List<Opening> openOpenings() {
        return openings.findByStatus(OpeningStatus.OPEN);
    }
}
