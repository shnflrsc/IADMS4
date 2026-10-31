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
 * REPOSITORY. all people in one place because they share ids
 * instanceof picks out each type
 */
public class PersonRepository extends Repository<Person> {
    // tells the base class how to get a person's id
    @Override
    protected int idOf(Person item) {
        return item.getId();
    }

    // all students. interns count too because Intern extends Student
    public List<Student> findStudents() {
        List<Student> students = new ArrayList<>();
        for (Person person : findAll()) {
            if (person instanceof Student) {
                students.add((Student) person);
            }
        }
        return students;
    }

    // goes through everyone and keeps only the interns
    public List<Intern> findInterns() {
        List<Intern> interns = new ArrayList<>();
        for (Person person : findAll()) {
            if (person instanceof Intern) {
                interns.add((Intern) person);
            }
        }
        return interns;
    }

    // goes through everyone and keeps only the coordinators
    public List<Coordinator> findCoordinators() {
        List<Coordinator> coordinators = new ArrayList<>();
        for (Person person : findAll()) {
            if (person instanceof Coordinator) {
                coordinators.add((Coordinator) person);
            }
        }
        return coordinators;
    }

    // goes through everyone and keeps only the company representatives
    public List<CompanyRepresentative> findRepresentatives() {
        List<CompanyRepresentative> representatives = new ArrayList<>();
        for (Person person : findAll()) {
            if (person instanceof CompanyRepresentative) {
                representatives.add((CompanyRepresentative) person);
            }
        }
        return representatives;
    }

    // takes all the representatives and keeps the ones of this company
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
