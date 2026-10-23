package iadms4.repository;

import java.util.List;

import iadms4.model.Application;
import iadms4.model.Company;

/*
 * REPOSITORY. Stores Application objects (coordinator endorsements).
 *
 * Responsibilities: the basic operations from Repository plus filtering applications by student
 * or by company.
 */
public class ApplicationRepository extends Repository<Application> {
    /*
     * Reads the id of an application so the base class can use it as the map key.
     *
     * It takes item (the application). It returns the application's id.
     */
    @Override
    protected int idOf(Application item) {
        return item.getId();
    }

    /*
     * Lists the applications of one student.
     *
     * It takes studentId (the id of the student). It returns that student's applications.
     */
    public List<Application> findByStudentId(int studentId) {
        return findAll().stream().filter(a -> a.getStudent().getId() == studentId).toList();
    }

    /*
     * Lists the applications made to a company's openings.
     *
     * It takes company (the company to filter by). It returns the applications that target that company.
     */
    public List<Application> findByCompany(Company company) {
        return findAll().stream()
                .filter(a -> a.getOpening().getPosition().getCompany().getId() == company.getId()).toList();
    }
}
