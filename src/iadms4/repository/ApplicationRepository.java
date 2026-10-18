package iadms4.repository;

import java.util.List;

import iadms4.model.Application;
import iadms4.model.Company;

/**
 * REPOSITORY. Stores Applications; can filter by student or by company.
 */
public class ApplicationRepository extends Repository<Application> {
    @Override
    protected int idOf(Application item) {
        return item.getId();
    }

    public List<Application> findByStudentId(int studentId) {
        return findAll().stream().filter(a -> a.getStudent().getId() == studentId).toList();
    }

    public List<Application> findByCompany(Company company) {
        return findAll().stream()
                .filter(a -> a.getOpening().getPosition().getCompany().getId() == company.getId()).toList();
    }
}
