package iadms4.repository;

import java.util.ArrayList;
import java.util.List;

import iadms4.model.Company;
import iadms4.model.Evaluation;

/*
 * REPOSITORY. Stores Evaluation objects and can find them by intern or by company.
 */
public class EvaluationRepository extends Repository<Evaluation> {
    // Returns the id of the given evaluation.
    @Override
    protected int idOf(Evaluation item) {
        return item.getId();
    }

    // Returns the evaluation of the intern with the given id, or null if there is none yet.
    public Evaluation findByInternId(int internId) {
        for (Evaluation evaluation : findAll()) {
            if (evaluation.getIntern().getId() == internId) {
                return evaluation;
            }
        }
        return null;
    }

    // Returns the evaluations of the interns who belong to the given company.
    public List<Evaluation> findByCompany(Company company) {
        List<Evaluation> matches = new ArrayList<>();
        for (Evaluation evaluation : findAll()) {
            if (evaluation.getIntern().getPosition().getCompany().getId() == company.getId()) {
                matches.add(evaluation);
            }
        }
        return matches;
    }
}
