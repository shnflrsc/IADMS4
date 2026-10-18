package iadms4.repository;

import java.util.List;
import java.util.Optional;

import iadms4.model.Company;
import iadms4.model.Evaluation;

/**
 * REPOSITORY. Stores Evaluations; can find the evaluation of an intern or list a company evaluations.
 */
public class EvaluationRepository extends Repository<Evaluation> {
    @Override
    protected int idOf(Evaluation item) {
        return item.getId();
    }

    public Optional<Evaluation> findByInternId(int internId) {
        return findAll().stream().filter(e -> e.getIntern().getId() == internId).findFirst();
    }

    public List<Evaluation> findByCompany(Company company) {
        return findAll().stream()
                .filter(e -> e.getIntern().getPosition().getCompany().getId() == company.getId()).toList();
    }
}
