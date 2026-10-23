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

/*
 * SERVICE. Business rules for positions and openings.
 *
 * Responsibilities: creates positions, publishes openings, opens or closes them, edits or deletes them
 * (with safety rules so that data in use is protected), and lists them by company.
 */
public class OpeningService {
    // Storage of positions.
    private final PositionRepository positions;
    // Storage of openings.
    private final OpeningRepository openings;
    // Used to check whether interns are deployed to a position.
    private final PersonRepository persons;
    // Used to check whether an opening already has applications.
    private final ApplicationRepository applications;

    /*
     * Creates the service.
     *
     * It takes positions (the position repository), openings (the opening repository), persons (the person
     * repository) and applications (the application repository).
     */
    public OpeningService(PositionRepository positions, OpeningRepository openings, PersonRepository persons,
            ApplicationRepository applications) {
        this.positions = positions;
        this.openings = openings;
        this.persons = persons;
        this.applications = applications;
    }

    /*
     * Creates and saves a new position for a company.
     *
     * It takes company (the company that offers the position), title (the job title) and requiredHours (the
     * hours needed to finish the internship). It returns the saved position. It throws
     * IllegalArgumentException if the title is blank or the hours are not positive.
     */
    public Position createPosition(Company company, String title, int requiredHours) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Position title is required.");
        }
        if (requiredHours <= 0) {
            throw new IllegalArgumentException("Required hours must be greater than zero.");
        }
        return positions.save(new Position(positions.nextId(), company, title.trim(), requiredHours));
    }

    /*
     * Edits a position of the representative's company. This is blocked once an intern is deployed to it.
     *
     * It takes company (the company of the representative making the change), position (the position to
     * edit), title (the new title) and requiredHours (the new required hours). It throws
     * IllegalStateException if the position belongs to another company or has interns. It throws
     * IllegalArgumentException if the title is blank or the hours are not positive.
     */
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

    /*
     * Deletes a position that has no openings.
     *
     * It takes company (the company of the representative making the change) and position (the position to
     * delete). It throws IllegalStateException if the position belongs to another company or still has
     * openings.
     */
    public void deletePosition(Company company, Position position) {
        requireOwn(company, position);
        if (openings.findAll().stream().anyMatch(o -> o.getPosition().getId() == position.getId())) {
            throw new IllegalStateException("This position has openings; delete them first.");
        }
        positions.remove(position.getId());
    }

    /*
     * Deletes an opening nobody was ever endorsed to (otherwise close it instead).
     *
     * It takes company (the company of the representative making the change) and opening (the opening to
     * delete). It throws IllegalStateException if the opening belongs to another company or has
     * applications.
     */
    public void deleteOpening(Company company, Opening opening) {
        requireOwn(company, opening.getPosition());
        if (applications.findAll().stream().anyMatch(a -> a.getOpening().getId() == opening.getId())) {
            throw new IllegalStateException("This opening has applications; close it instead of deleting it.");
        }
        openings.remove(opening.getId());
    }

    /*
     * Makes sure a position belongs to the given company.
     *
     * It takes company (the company that should own the position) and position (the position to check). It
     * throws IllegalStateException if the position belongs to a different company.
     */
    private void requireOwn(Company company, Position position) {
        if (position.getCompany().getId() != company.getId()) {
            throw new IllegalStateException("That position belongs to another company.");
        }
    }

    /*
     * Publishes a position as a new OPEN opening.
     *
     * It takes position (the position to publish). It returns the saved opening.
     */
    public Opening createOpening(Position position) {
        return openings.save(new Opening(openings.nextId(), position, OpeningStatus.OPEN));
    }

    /*
     * Opens or closes an opening.
     *
     * It takes opening (the opening to change) and status (the new status).
     */
    public void setStatus(Opening opening, OpeningStatus status) {
        opening.setStatus(status);
    }

    /*
     * Lists a company's positions.
     *
     * It takes company (the company). It returns that company's positions.
     */
    public List<Position> positionsOf(Company company) {
        return positions.findByCompany(company);
    }

    /*
     * Lists a company's openings.
     *
     * It takes company (the company). It returns that company's openings.
     */
    public List<Opening> openingsOf(Company company) {
        return openings.findByCompany(company);
    }

    /*
     * Lists every opening of every company.
     *
     * It returns all openings.
     */
    public List<Opening> allOpenings() {
        return openings.findAll();
    }

    /*
     * Lists only the openings that are accepting endorsements.
     *
     * It returns the OPEN openings.
     */
    public List<Opening> openOpenings() {
        return openings.findByStatus(OpeningStatus.OPEN);
    }
}
