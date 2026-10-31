package iadms4.repository;

import java.util.ArrayList;
import java.util.List;

import iadms4.model.Application;
import iadms4.model.Company;

/*
 * REPOSITORY. stores applications, filter by student or company
 */
public class ApplicationRepository extends Repository<Application> {
    // tells the base class how to get an application's id
    @Override
    protected int idOf(Application item) {
        return item.getId();
    }

    // goes through all applications and keeps the ones of this student
    public List<Application> findByStudentId(int studentId) {
        List<Application> matches = new ArrayList<>();
        for (Application application : findAll()) {
            if (application.getStudent().getId() == studentId) {
                matches.add(application);
            }
        }
        return matches;
    }

    // goes through all applications and keeps the ones for this company
    public List<Application> findByCompany(Company company) {
        List<Application> matches = new ArrayList<>();
        for (Application application : findAll()) {
            if (application.getOpening().getPosition().getCompany().getId() == company.getId()) {
                matches.add(application);
            }
        }
        return matches;
    }
}
