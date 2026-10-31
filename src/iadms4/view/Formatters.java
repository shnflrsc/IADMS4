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
 * VIEW helper. makes the one line labels for Console.choose
 * all static
 */
class Formatters {
    // like "Backend Developer @ Acme (300h)"
    private static String position(Position p) {
        return p.getTitle() + " @ " + p.getCompany().getName() + " (" + p.getRequiredHours() + "h)";
    }

    // what kind of person. intern first again because intern is a student
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

    // makes a label for each position with title, company, hours
    static List<String> positions(List<Position> positions) {
        List<String> labels = new ArrayList<>();
        for (Position p : positions) {
            labels.add(position(p));
        }
        return labels;
    }

    // makes a list of just the position titles
    static List<String> positionTitles(List<Position> positions) {
        List<String> labels = new ArrayList<>();
        for (Position p : positions) {
            labels.add(p.getTitle());
        }
        return labels;
    }

    // makes a label for each opening with id, position, status
    static List<String> openings(List<Opening> openings) {
        List<String> labels = new ArrayList<>();
        for (Opening o : openings) {
            labels.add("#" + o.getId() + " " + position(o.getPosition()) + " [" + o.getStatus() + "]");
        }
        return labels;
    }

    // makes a label for each application with id, student, position, status
    static List<String> applications(List<Application> applications) {
        List<String> labels = new ArrayList<>();
        for (Application a : applications) {
            labels.add("#" + a.getId() + " " + a.getStudent().getName() + " -> "
                    + position(a.getOpening().getPosition()) + " [" + a.getStatus() + "]");
        }
        return labels;
    }

    // makes a label for each intern with name, position, hours, status
    static List<String> interns(List<Intern> interns) {
        List<String> labels = new ArrayList<>();
        for (Intern i : interns) {
            labels.add(i.getName() + " (" + i.getStudentId() + ") " + i.getPosition().getTitle() + " "
                    + i.getRecordedHours() + "/" + i.getPosition().getRequiredHours() + "h [" + i.getStatus() + "]");
        }
        return labels;
    }

    // makes a label for each person like "Student: Ben Reyes"
    static List<String> people(List<Person> people) {
        List<String> labels = new ArrayList<>();
        for (Person p : people) {
            labels.add(kind(p) + ": " + p.getName());
        }
        return labels;
    }

    // makes a label for each student with number, name, kind
    static List<String> students(List<Student> students) {
        List<String> labels = new ArrayList<>();
        for (Student s : students) {
            labels.add(s.getStudentId() + " " + s.getName() + " (" + kind(s) + ")");
        }
        return labels;
    }

    // makes a list of just the company names
    static List<String> companies(List<Company> companies) {
        List<String> labels = new ArrayList<>();
        for (Company c : companies) {
            labels.add(c.getName());
        }
        return labels;
    }

    // makes a list of the intern status names for picking one
    static List<String> internStatuses() {
        List<String> labels = new ArrayList<>();
        for (InternStatus status : InternStatus.values()) {
            labels.add(status.toString());
        }
        return labels;
    }
}
