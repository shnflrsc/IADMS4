package iadms4.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import iadms4.model.Company;
import iadms4.model.CompanyRepresentative;
import iadms4.model.Evaluation;
import iadms4.model.Intern;
import iadms4.model.InternStatus;
import iadms4.repository.EvaluationRepository;
import iadms4.repository.PersonRepository;

/*
 * SERVICE. evaluation rules
 * COMPLETED interns of your own company only, one time only
 */
public class EvaluationService {
    private final EvaluationRepository evaluations;
    private final PersonRepository persons;

    // makes the service, takes evaluations, persons
    public EvaluationService(EvaluationRepository evaluations, PersonRepository persons) {
        this.evaluations = evaluations;
        this.persons = persons;
    }

    // save evaluation, lots of checks first
    public void evaluate(CompanyRepresentative rep, Intern intern, int rating, String comments) {
        if (intern.getPosition().getCompany().getId() != rep.getCompany().getId()) {
            throw new IllegalStateException("That intern does not belong to your company.");
        }
        if (intern.getStatus() != InternStatus.COMPLETED) {
            throw new IllegalStateException("Only COMPLETED interns can be evaluated.");
        }
        if (evaluations.findByInternId(intern.getId()) != null) {
            throw new IllegalStateException("This intern has already been evaluated.");
        }
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        if (comments == null || comments.isBlank()) {
            throw new IllegalArgumentException("Comments are required.");
        }
        evaluations.save(new Evaluation(evaluations.nextId(), intern, rep, rating, comments.trim(), LocalDate.now()));
    }

    // completed interns with no evaluation yet
    public List<Intern> awaitingEvaluation(Company company) {
        List<Intern> matches = new ArrayList<>();
        for (Intern intern : persons.findInterns()) {
            if (intern.getPosition().getCompany().getId() == company.getId()
                    && intern.getStatus() == InternStatus.COMPLETED
                    && evaluations.findByInternId(intern.getId()) == null) {
                matches.add(intern);
            }
        }
        return matches;
    }

    // evaluation of intern, null if none
    public Evaluation forIntern(Intern intern) {
        return evaluations.findByInternId(intern.getId());
    }

    // gets the evaluations of this company's interns
    public List<Evaluation> forCompany(Company company) {
        return evaluations.findByCompany(company);
    }

    // gets every evaluation, this is for coordinators
    public List<Evaluation> all() {
        return evaluations.findAll();
    }
}
