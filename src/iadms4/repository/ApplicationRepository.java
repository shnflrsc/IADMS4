package iadms4.repository;

import java.util.ArrayList;
import java.util.List;

import iadms4.model.Application;
import iadms4.model.Company;

/*
 * REPOSITORY. Stores Application objects and can filter them by student or by company.
 */
public class ApplicationRepository extends Repository<Application> {
    // Returns the id of the given application.
    @Override
    protected int idOf(Application item) {
        return item.getId();
    }

    // Returns the applications of the student with the given id.
    public List<Application> findByStudentId(int studentId) {
        List<Application> matches = new ArrayList<>();
        for (Application application : findAll()) {
            if (application.getStudent().getId() == studentId) {
                matches.add(application);
            }
        }
        return matches;
    }

    // Returns the applications made to the openings of the given company.
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
