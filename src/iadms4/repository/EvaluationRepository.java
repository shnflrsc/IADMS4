package iadms4.repository;

import java.util.ArrayList;
import java.util.List;

import iadms4.model.Company;
import iadms4.model.Evaluation;

/*
 * REPOSITORY. stores evaluations
 */
public class EvaluationRepository extends Repository<Evaluation> {
    // id of evaluation
    @Override
    protected int idOf(Evaluation item) {
        return item.getId();
    }

    // evaluation of one intern, null if none yet
    public Evaluation findByInternId(int internId) {
        for (Evaluation evaluation : findAll()) {
            if (evaluation.getIntern().getId() == internId) {
                return evaluation;
            }
        }
        return null;
    }

    // evaluations of one company
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
