package iadms4.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import iadms4.model.Company;
import iadms4.model.CompanyRepresentative;
import iadms4.model.Evaluation;
import iadms4.model.Intern;
import iadms4.model.InternStatus;
import iadms4.repository.EvaluationRepository;
import iadms4.repository.PersonRepository;

/*
 * SERVICE. Business rules for intern evaluations.
 *
 * Responsibilities: creates evaluations and lists them. Rules: only a COMPLETED intern, of the
 * representative's own company, only once, with a rating from 1 to 5.
 */
public class EvaluationService {
    // Storage of evaluations.
    private final EvaluationRepository evaluations;
    // Used to look up interns.
    private final PersonRepository persons;

    /*
     * Creates the service.
     *
     * It takes evaluations (the evaluation repository) and persons (the person repository).
     */
    public EvaluationService(EvaluationRepository evaluations, PersonRepository persons) {
        this.evaluations = evaluations;
        this.persons = persons;
    }

    /*
     * Creates the one-time evaluation of a COMPLETED intern of the representative's own company.
     *
     * It takes rep (the representative writing the evaluation), intern (the intern being evaluated), rating
     * (the score, from 1 to 5) and comments (the written feedback). It returns the saved evaluation. It
     * throws IllegalStateException if the intern is from another company, not COMPLETED, or already
     * evaluated. It throws IllegalArgumentException if the rating is out of range or the comments are
     * blank.
     */
    public Evaluation evaluate(CompanyRepresentative rep, Intern intern, int rating, String comments) {
        if (intern.getPosition().getCompany().getId() != rep.getCompany().getId()) {
            throw new IllegalStateException("That intern does not belong to your company.");
        }
        if (intern.getStatus() != InternStatus.COMPLETED) {
            throw new IllegalStateException("Only COMPLETED interns can be evaluated.");
        }
        if (evaluations.findByInternId(intern.getId()).isPresent()) {
            throw new IllegalStateException("This intern has already been evaluated.");
        }
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        if (comments == null || comments.isBlank()) {
            throw new IllegalArgumentException("Comments are required.");
        }
        return evaluations.save(new Evaluation(evaluations.nextId(), intern, rep, rating, comments.trim(),
                LocalDate.now()));
    }

    /*
     * Lists the COMPLETED interns of a company that have no evaluation yet.
     *
     * It takes company (the company to check). It returns the interns still waiting for an evaluation.
     */
    public List<Intern> awaitingEvaluation(Company company) {
        return persons.findInterns().stream()
                .filter(i -> i.getPosition().getCompany().getId() == company.getId())
                .filter(i -> i.getStatus() == InternStatus.COMPLETED)
                .filter(i -> evaluations.findByInternId(i.getId()).isEmpty()).toList();
    }

    /*
     * Gets the evaluation an intern received, if any (interns may read their own).
     *
     * It takes intern (the intern). It returns an Optional with the evaluation, or an empty Optional if
     * there is none yet.
     */
    public Optional<Evaluation> forIntern(Intern intern) {
        return evaluations.findByInternId(intern.getId());
    }

    /*
     * Lists the evaluations of a company's interns.
     *
     * It takes company (the company). It returns that company's evaluations.
     */
    public List<Evaluation> forCompany(Company company) {
        return evaluations.findByCompany(company);
    }

    /*
     * Lists every evaluation in the system (used by coordinators).
     *
     * It returns all evaluations.
     */
    public List<Evaluation> all() {
        return evaluations.findAll();
    }
}
