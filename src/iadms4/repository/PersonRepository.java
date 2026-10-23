package iadms4.repository;

import java.util.List;

import iadms4.model.Company;
import iadms4.model.CompanyRepresentative;
import iadms4.model.Coordinator;
import iadms4.model.Intern;
import iadms4.model.Person;
import iadms4.model.Student;

/*
 * REPOSITORY. Stores every Person (students, interns, coordinators, representatives) in one place
 * because they share one id space.
 *
 * Responsibilities: besides the basic operations inherited from Repository, it can list people of
 * one subclass. The find* methods filter by subclass using instanceof, which checks the real type of an
 * object at runtime.
 */
public class PersonRepository extends Repository<Person> {
    /*
     * Reads the id of a person so the base class can use it as the map key.
     *
     * It takes item (the person). It returns the person's id.
     */
    @Override
    protected int idOf(Person item) {
        return item.getId();
    }

    /*
     * Lists all students, including those already promoted to interns (an Intern is also a Student).
     *
     * It returns the list of students.
     */
    public List<Student> findStudents() {
        return findAll().stream().filter(p -> p instanceof Student).map(p -> (Student) p).toList();
    }

    /*
     * Lists only the interns.
     *
     * It returns the list of interns.
     */
    public List<Intern> findInterns() {
        return findAll().stream().filter(p -> p instanceof Intern).map(p -> (Intern) p).toList();
    }

    /*
     * Lists all coordinators.
     *
     * It returns the list of coordinators.
     */
    public List<Coordinator> findCoordinators() {
        return findAll().stream().filter(p -> p instanceof Coordinator).map(p -> (Coordinator) p).toList();
    }

    /*
     * Lists the representatives of every company.
     *
     * It returns the list of company representatives.
     */
    public List<CompanyRepresentative> findRepresentatives() {
        return findAll().stream().filter(p -> p instanceof CompanyRepresentative)
                .map(p -> (CompanyRepresentative) p).toList();
    }

    /*
     * Lists the representatives of one particular company.
     *
     * It takes company (the company to filter by). It returns the representatives who work for that
     * company.
     */
    public List<CompanyRepresentative> findRepresentatives(Company company) {
        return findAll().stream().filter(p -> p instanceof CompanyRepresentative)
                .map(p -> (CompanyRepresentative) p).filter(r -> r.getCompany().getId() == company.getId()).toList();
    }
}
