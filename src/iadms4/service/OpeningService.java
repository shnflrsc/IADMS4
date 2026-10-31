package iadms4.service;

import java.util.List;

import iadms4.model.Application;
import iadms4.model.Company;
import iadms4.model.Intern;
import iadms4.model.Opening;
import iadms4.model.OpeningStatus;
import iadms4.model.Position;
import iadms4.repository.ApplicationRepository;
import iadms4.repository.OpeningRepository;
import iadms4.repository.PersonRepository;
import iadms4.repository.PositionRepository;

/*
 * SERVICE. Business rules for positions and openings.
 * It creates, edits and deletes them, with safety rules so that data in use is protected.
 */
public class OpeningService {
    private final PositionRepository positions;
    private final OpeningRepository openings;
    private final PersonRepository persons;
    private final ApplicationRepository applications;

    // Creates the service using the given repositories.
    public OpeningService(PositionRepository positions, OpeningRepository openings, PersonRepository persons,
            ApplicationRepository applications) {
        this.positions = positions;
        this.openings = openings;
        this.persons = persons;
        this.applications = applications;
    }

    // Creates a position for the given company with a title and requiredHours.
    public void createPosition(Company company, String title, int requiredHours) {
        requireValidDetails(title, requiredHours);
        positions.save(new Position(positions.nextId(), company, title.trim(), requiredHours));
    }

    // Edits a position of the given company. This is blocked once an intern is deployed to it.
    public void updatePosition(Company company, Position position, String title, int requiredHours) {
        requireOwn(company, position);
        for (Intern intern : persons.findInterns()) {
            if (intern.getPosition().getId() == position.getId()) {
                throw new IllegalStateException("Interns are deployed to this position; it can no longer be edited.");
            }
        }
        requireValidDetails(title, requiredHours);
        position.setTitle(title.trim());
        position.setRequiredHours(requiredHours);
    }

    // Throws an exception if the title is blank or the hours are not positive.
    private void requireValidDetails(String title, int requiredHours) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Position title is required.");
        }
        if (requiredHours <= 0) {
            throw new IllegalArgumentException("Required hours must be greater than zero.");
        }
    }

    // Deletes a position of the given company if it has no openings.
    public void deletePosition(Company company, Position position) {
        requireOwn(company, position);
        for (Opening opening : openings.findAll()) {
            if (opening.getPosition().getId() == position.getId()) {
                throw new IllegalStateException("This position has openings; delete them first.");
            }
        }
        positions.remove(position.getId());
    }

    // Deletes an opening of the given company if nobody was ever endorsed to it.
    public void deleteOpening(Company company, Opening opening) {
        requireOwn(company, opening.getPosition());
        for (Application application : applications.findAll()) {
            if (application.getOpening().getId() == opening.getId()) {
                throw new IllegalStateException("This opening has applications; close it instead of deleting it.");
            }
        }
        openings.remove(opening.getId());
    }

    // Throws an exception if the given position belongs to a different company.
    private void requireOwn(Company company, Position position) {
        if (position.getCompany().getId() != company.getId()) {
            throw new IllegalStateException("That position belongs to another company.");
        }
    }

    // Publishes the given position as a new OPEN opening.
    public void createOpening(Position position) {
        openings.save(new Opening(openings.nextId(), position, OpeningStatus.OPEN));
    }

    // Changes the given opening to the given status.
    public void setStatus(Opening opening, OpeningStatus status) {
        opening.setStatus(status);
    }

    // Returns the positions of the given company.
    public List<Position> positionsOf(Company company) {
        return positions.findByCompany(company);
    }

    // Returns the openings of the given company.
    public List<Opening> openingsOf(Company company) {
        return openings.findByCompany(company);
    }

    // Returns every opening of every company.
    public List<Opening> allOpenings() {
        return openings.findAll();
    }

    // Returns only the openings that are OPEN.
    public List<Opening> openOpenings() {
        return openings.findByStatus(OpeningStatus.OPEN);
    }
}
