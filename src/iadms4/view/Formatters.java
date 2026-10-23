package iadms4.view;

import iadms4.model.Application;
import iadms4.model.CompanyRepresentative;
import iadms4.model.Coordinator;
import iadms4.model.Intern;
import iadms4.model.Opening;
import iadms4.model.Person;
import iadms4.model.Position;
import iadms4.model.Student;

/*
 * VIEW helper. Turns model objects into short one-line texts.
 *
 * Responsibilities: the texts are reused by the choose-from-list prompts. All methods are static, so the
 * class is never instantiated (its constructor is private).
 */
final class Formatters {
    // Private so that nobody creates an object of this utility class.
    private Formatters() {
    }

    /*
     * Describes a position, for example "Backend Developer @ Acme (300h)".
     *
     * It takes p (the position). It returns the one-line text.
     */
    static String position(Position p) {
        return p.getTitle() + " @ " + p.getCompany().getName() + " (" + p.getRequiredHours() + "h)";
    }

    /*
     * Describes an opening with its id and status.
     *
     * It takes o (the opening). It returns the one-line text.
     */
    static String opening(Opening o) {
        return "#" + o.getId() + " " + position(o.getPosition()) + " [" + o.getStatus() + "]";
    }

    /*
     * Describes an application with the student, position and status.
     *
     * It takes a (the application). It returns the one-line text.
     */
    static String application(Application a) {
        return "#" + a.getId() + " " + a.getStudent().getName() + " -> " + position(a.getOpening().getPosition())
                + " [" + a.getStatus() + "]";
    }

    /*
     * Describes an intern with the position, hours progress and status.
     *
     * It takes i (the intern). It returns the one-line text.
     */
    static String intern(Intern i) {
        return i.getName() + " (" + i.getStudentId() + ") " + i.getPosition().getTitle() + " "
                + i.getRecordedHours() + "/" + i.getPosition().getRequiredHours() + "h [" + i.getStatus() + "]";
    }

    /*
     * Names the kind of a person. Intern is checked before Student because an Intern is also a Student.
     *
     * It takes p (the person). It returns "Intern", "Student", "Coordinator", "Company Rep" or "Person".
     */
    static String kind(Person p) {
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
}
