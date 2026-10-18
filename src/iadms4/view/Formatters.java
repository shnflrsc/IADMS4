package iadms4.view;

import iadms4.model.Application;
import iadms4.model.Intern;
import iadms4.model.Opening;
import iadms4.model.Person;
import iadms4.model.Position;

/**
 * VIEW helper. One-line text versions of model objects, reused by the choose-from-list prompts.
 */
final class Formatters {
    private Formatters() {
    }

    static String position(Position p) {
        return p.getTitle() + " @ " + p.getCompany().getName() + " (" + p.getRequiredHours() + "h)";
    }

    static String opening(Opening o) {
        return "#" + o.getId() + " " + position(o.getPosition()) + " [" + o.getStatus() + "]";
    }

    static String application(Application a) {
        return "#" + a.getId() + " " + a.getStudent().getName() + " -> " + position(a.getOpening().getPosition())
                + " [" + a.getStatus() + "]";
    }

    static String intern(Intern i) {
        return i.getName() + " (" + i.getStudentId() + ") " + i.getPosition().getTitle() + " "
                + i.getRecordedHours() + "/" + i.getPosition().getRequiredHours() + "h [" + i.getStatus() + "]";
    }

    static String kind(Person p) {
        return p instanceof Intern ? "Intern" : "Student";
    }
}
