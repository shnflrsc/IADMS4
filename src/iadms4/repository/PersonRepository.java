package iadms4.repository;

import java.util.ArrayList;
import java.util.List;

import iadms4.model.Company;
import iadms4.model.CompanyRepresentative;
import iadms4.model.Coordinator;
import iadms4.model.Intern;
import iadms4.model.Person;
import iadms4.model.Student;

/*
 * REPOSITORY. Stores every Person in one place because they all share one id space.
 * The find methods use instanceof to pick out the people of one subclass.
 */
public class PersonRepository extends Repository<Person> {
    // Returns the id of the given person.
    @Override
    protected int idOf(Person item) {
        return item.getId();
    }

    // Returns all students, including interns because an Intern is also a Student.
    public List<Student> findStudents() {
        List<Student> students = new ArrayList<>();
        for (Person person : findAll()) {
            if (person instanceof Student) {
                students.add((Student) person);
            }
        }
        return students;
    }

    // Returns only the interns.
    public List<Intern> findInterns() {
        List<Intern> interns = new ArrayList<>();
        for (Person person : findAll()) {
            if (person instanceof Intern) {
                interns.add((Intern) person);
            }
        }
        return interns;
    }

    // Returns all coordinators.
    public List<Coordinator> findCoordinators() {
        List<Coordinator> coordinators = new ArrayList<>();
        for (Person person : findAll()) {
            if (person instanceof Coordinator) {
                coordinators.add((Coordinator) person);
            }
        }
        return coordinators;
    }

    // Returns the representatives of every company.
    public List<CompanyRepresentative> findRepresentatives() {
        List<CompanyRepresentative> representatives = new ArrayList<>();
        for (Person person : findAll()) {
            if (person instanceof CompanyRepresentative) {
                representatives.add((CompanyRepresentative) person);
            }
        }
        return representatives;
    }

    // Returns the representatives who work for the given company.
    public List<CompanyRepresentative> findRepresentatives(Company company) {
        List<CompanyRepresentative> matches = new ArrayList<>();
        for (CompanyRepresentative representative : findRepresentatives()) {
            if (representative.getCompany().getId() == company.getId()) {
                matches.add(representative);
            }
        }
        return matches;
    }
}
