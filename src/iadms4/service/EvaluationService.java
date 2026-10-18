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

/**
 * SERVICE. Intern evaluations. Rules: only a COMPLETED intern, of the representative own company,
 * only once, with a rating from 1 to 5.
 */
public class EvaluationService {
    private final EvaluationRepository evaluations;
    private final PersonRepository persons;

    public EvaluationService(EvaluationRepository evaluations, PersonRepository persons) {
        this.evaluations = evaluations;
        this.persons = persons;
    }

    /** Creates the one-time evaluation of a COMPLETED intern of the representative's own company. */
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

    /** COMPLETED interns of the company that have no evaluation yet. */
    public List<Intern> awaitingEvaluation(Company company) {
        return persons.findInterns().stream()
                .filter(i -> i.getPosition().getCompany().getId() == company.getId())
                .filter(i -> i.getStatus() == InternStatus.COMPLETED)
                .filter(i -> evaluations.findByInternId(i.getId()).isEmpty()).toList();
    }

    /** The evaluation an intern received, if any (interns may read their own). */
    public Optional<Evaluation> forIntern(Intern intern) {
        return evaluations.findByInternId(intern.getId());
    }

    public List<Evaluation> forCompany(Company company) {
        return evaluations.findByCompany(company);
    }

    public List<Evaluation> all() {
        return evaluations.findAll();
    }
}
