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
 * REPOSITORY. all people in one place bc they share ids
 * instanceof picks out each type
 */
public class PersonRepository extends Repository<Person> {
    // id of person
    @Override
    protected int idOf(Person item) {
        return item.getId();
    }

    // all students. interns count too bc Intern extends Student
    public List<Student> findStudents() {
        List<Student> students = new ArrayList<>();
        for (Person person : findAll()) {
            if (person instanceof Student) {
                students.add((Student) person);
            }
        }
        return students;
    }

    // interns only
    public List<Intern> findInterns() {
        List<Intern> interns = new ArrayList<>();
        for (Person person : findAll()) {
            if (person instanceof Intern) {
                interns.add((Intern) person);
            }
        }
        return interns;
    }

    // all coordinators
    public List<Coordinator> findCoordinators() {
        List<Coordinator> coordinators = new ArrayList<>();
        for (Person person : findAll()) {
            if (person instanceof Coordinator) {
                coordinators.add((Coordinator) person);
            }
        }
        return coordinators;
    }

    // all reps
    public List<CompanyRepresentative> findRepresentatives() {
        List<CompanyRepresentative> representatives = new ArrayList<>();
        for (Person person : findAll()) {
            if (person instanceof CompanyRepresentative) {
                representatives.add((CompanyRepresentative) person);
            }
        }
        return representatives;
    }

    // reps of one company
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
