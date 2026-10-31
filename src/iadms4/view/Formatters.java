package iadms4.view;

import java.util.ArrayList;
import java.util.List;

import iadms4.model.Application;
import iadms4.model.Company;
import iadms4.model.CompanyRepresentative;
import iadms4.model.Coordinator;
import iadms4.model.Intern;
import iadms4.model.InternStatus;
import iadms4.model.Opening;
import iadms4.model.Person;
import iadms4.model.Position;
import iadms4.model.Student;

/*
 * VIEW helper. Turns model objects into the one-line texts shown by Console.choose.
 * All methods are static, so no object of this class is ever created.
 */
class Formatters {
    // Returns text such as "Backend Developer @ Acme (300h)" for the given position.
    private static String position(Position p) {
        return p.getTitle() + " @ " + p.getCompany().getName() + " (" + p.getRequiredHours() + "h)";
    }

    // Returns "Intern", "Student", "Coordinator", "Company Rep" or "Person". Intern is checked before Student.
    private static String kind(Person p) {
        if (p instanceof Intern) {
            return "Intern";
        }
        if (p instanceof Student) {
            return "Student";
        }
        if (p instanceof Coordinator) {
            return "Coordinator";
        }
        if (p instanceof CompanyRepresentative) {
            return "Company Rep";
        }
        return "Person";
    }

    // Returns a label with the title, company and hours of each given position.
    static List<String> positions(List<Position> positions) {
        List<String> labels = new ArrayList<>();
        for (Position p : positions) {
            labels.add(position(p));
        }
        return labels;
    }

    // Returns only the title of each given position.
    static List<String> positionTitles(List<Position> positions) {
        List<String> labels = new ArrayList<>();
        for (Position p : positions) {
            labels.add(p.getTitle());
        }
        return labels;
    }

    // Returns a label with the id, position and status of each given opening.
    static List<String> openings(List<Opening> openings) {
        List<String> labels = new ArrayList<>();
        for (Opening o : openings) {
            labels.add("#" + o.getId() + " " + position(o.getPosition()) + " [" + o.getStatus() + "]");
        }
        return labels;
    }

    // Returns a label with the id, student, position and status of each given application.
    static List<String> applications(List<Application> applications) {
        List<String> labels = new ArrayList<>();
        for (Application a : applications) {
            labels.add("#" + a.getId() + " " + a.getStudent().getName() + " -> "
                    + position(a.getOpening().getPosition()) + " [" + a.getStatus() + "]");
        }
        return labels;
    }

    // Returns a label with the name, position, hours and status of each given intern.
    static List<String> interns(List<Intern> interns) {
        List<String> labels = new ArrayList<>();
        for (Intern i : interns) {
            labels.add(i.getName() + " (" + i.getStudentId() + ") " + i.getPosition().getTitle() + " "
                    + i.getRecordedHours() + "/" + i.getPosition().getRequiredHours() + "h [" + i.getStatus() + "]");
        }
        return labels;
    }

    // Returns a label with the kind and name of each given person.
    static List<String> people(List<Person> people) {
        List<String> labels = new ArrayList<>();
        for (Person p : people) {
            labels.add(kind(p) + ": " + p.getName());
        }
        return labels;
    }

    // Returns a label with the student number, name and kind of each given student.
    static List<String> students(List<Student> students) {
        List<String> labels = new ArrayList<>();
        for (Student s : students) {
            labels.add(s.getStudentId() + " " + s.getName() + " (" + kind(s) + ")");
        }
        return labels;
    }

    // Returns the name of each given company.
    static List<String> companies(List<Company> companies) {
        List<String> labels = new ArrayList<>();
        for (Company c : companies) {
            labels.add(c.getName());
        }
        return labels;
    }

    // Returns the name of every intern status.
    static List<String> internStatuses() {
        List<String> labels = new ArrayList<>();
        for (InternStatus status : InternStatus.values()) {
            labels.add(status.toString());
        }
        return labels;
    }
}
