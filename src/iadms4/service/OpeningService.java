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
 * SERVICE. positions AND openings, theyre together bc the rules depend on each other
 * cant delete stuff thats being used
 */
public class OpeningService {
    private final PositionRepository positions;
    private final OpeningRepository openings;
    private final PersonRepository persons;
    private final ApplicationRepository applications;

    // makes the service, takes positions, openings, persons, applications
    public OpeningService(PositionRepository positions, OpeningRepository openings, PersonRepository persons,
            ApplicationRepository applications) {
        this.positions = positions;
        this.openings = openings;
        this.persons = persons;
        this.applications = applications;
    }

    // new position
    public void createPosition(Company company, String title, int requiredHours) {
        requireValidDetails(title, requiredHours);
        positions.save(new Position(positions.nextId(), company, title.trim(), requiredHours));
    }

    // edit position, not allowed if theres an intern on it
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

    // title cant be blank, hours must be > 0
    private void requireValidDetails(String title, int requiredHours) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Position title is required.");
        }
        if (requiredHours <= 0) {
            throw new IllegalArgumentException("Required hours must be greater than zero.");
        }
    }

    // delete position, only if no openings
    public void deletePosition(Company company, Position position) {
        requireOwn(company, position);
        for (Opening opening : openings.findAll()) {
            if (opening.getPosition().getId() == position.getId()) {
                throw new IllegalStateException("This position has openings; delete them first.");
            }
        }
        positions.remove(position.getId());
    }

    // delete opening, only if nobody was ever endorsed
    public void deleteOpening(Company company, Opening opening) {
        requireOwn(company, opening.getPosition());
        for (Application application : applications.findAll()) {
            if (application.getOpening().getId() == opening.getId()) {
                throw new IllegalStateException("This opening has applications; close it instead of deleting it.");
            }
        }
        openings.remove(opening.getId());
    }

    // error if its another company's position
    private void requireOwn(Company company, Position position) {
        if (position.getCompany().getId() != company.getId()) {
            throw new IllegalStateException("That position belongs to another company.");
        }
    }

    // new OPEN opening
    public void createOpening(Position position) {
        openings.save(new Opening(openings.nextId(), position, OpeningStatus.OPEN));
    }

    // set status
    public void setStatus(Opening opening, OpeningStatus status) {
        opening.setStatus(status);
    }

    // positions of company
    public List<Position> positionsOf(Company company) {
        return positions.findByCompany(company);
    }

    // openings of company
    public List<Opening> openingsOf(Company company) {
        return openings.findByCompany(company);
    }

    // all openings
    public List<Opening> allOpenings() {
        return openings.findAll();
    }

    // OPEN ones only
    public List<Opening> openOpenings() {
        return openings.findByStatus(OpeningStatus.OPEN);
    }
}
