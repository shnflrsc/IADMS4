package iadms4.repository;

import java.util.List;
import java.util.Optional;

import iadms4.model.Company;
import iadms4.model.Evaluation;

/*
 * REPOSITORY. Stores Evaluation objects.
 *
 * Responsibilities: the basic operations from Repository plus finding the evaluation of one intern
 * and listing the evaluations of a company's interns.
 */
public class EvaluationRepository extends Repository<Evaluation> {
    /*
     * Reads the id of an evaluation so the base class can use it as the map key.
     *
     * It takes item (the evaluation). It returns the evaluation's id.
     */
    @Override
    protected int idOf(Evaluation item) {
        return item.getId();
    }

    /*
     * Finds the evaluation written for an intern. There is at most one per intern.
     *
     * It takes internId (the id of the intern). It returns an Optional with the evaluation, or an empty
     * Optional if the intern is not evaluated yet.
     */
    public Optional<Evaluation> findByInternId(int internId) {
        return findAll().stream().filter(e -> e.getIntern().getId() == internId).findFirst();
    }

    /*
     * Lists the evaluations of the interns who belong to a company.
     *
     * It takes company (the company to filter by). It returns that company's evaluations.
     */
    public List<Evaluation> findByCompany(Company company) {
        return findAll().stream()
                .filter(e -> e.getIntern().getPosition().getCompany().getId() == company.getId()).toList();
    }
}
