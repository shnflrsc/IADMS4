package iadms4.repository;

import java.util.List;

import iadms4.model.Company;
import iadms4.model.CompanyRepresentative;
import iadms4.model.Coordinator;
import iadms4.model.Intern;
import iadms4.model.Person;
import iadms4.model.Student;

/**
 * REPOSITORY. Stores every Person (students, interns, coordinators, representatives) in one place
 * because they share one id space. The find* methods filter by subclass using instanceof.
 */
public class PersonRepository extends Repository<Person> {
    @Override
    protected int idOf(Person item) {
        return item.getId();
    }

    /** All students, including those already promoted to interns. */
    public List<Student> findStudents() {
        return findAll().stream().filter(p -> p instanceof Student).map(p -> (Student) p).toList();
    }

    public List<Intern> findInterns() {
        return findAll().stream().filter(p -> p instanceof Intern).map(p -> (Intern) p).toList();
    }

    public List<Coordinator> findCoordinators() {
        return findAll().stream().filter(p -> p instanceof Coordinator).map(p -> (Coordinator) p).toList();
    }

    public List<CompanyRepresentative> findRepresentatives() {
        return findAll().stream().filter(p -> p instanceof CompanyRepresentative)
                .map(p -> (CompanyRepresentative) p).toList();
    }

    public List<CompanyRepresentative> findRepresentatives(Company company) {
        return findAll().stream().filter(p -> p instanceof CompanyRepresentative)
                .map(p -> (CompanyRepresentative) p).filter(r -> r.getCompany().getId() == company.getId()).toList();
    }
}
