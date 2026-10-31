package iadms4.view;

import java.util.ArrayList;
import java.util.List;

import iadms4.model.Company;
import iadms4.model.Summary;

/*
 * VIEW helper. prints reports so coordinator and representative screens look the same
 * all static
 */
class Reports {
    // prints a summary. showStudents means also show undeployed students
    static void print(Console console, String title, Summary s, boolean showStudents) {
        console.println("  " + title);
        if (showStudents) {
            console.println("  Students not yet deployed: " + s.getStudents());
        }
        console.println("  Interns:      " + s.getInterns() + "  (ACTIVE " + s.getActive() + ", DEACTIVATED "
                + s.getDeactivated() + ", COMPLETED " + s.getCompleted() + ")");
        console.println("  Hours:        " + s.getRecordedHours() + " recorded of " + s.getRequiredHours()
                + " required (" + s.getProgressPercent() + "%)");
        console.println("  Openings:     " + s.getOpenOpenings() + " open, " + s.getClosedOpenings() + " closed");
        console.println("  Applications: PENDING " + s.getPending() + ", APPROVED " + s.getApproved() + ", REJECTED "
                + s.getRejected() + ", WITHDRAWN " + s.getWithdrawn());
        String evaluations = "  Evaluations:  " + s.getEvaluations();
        if (s.getEvaluations() != 0) {
            evaluations += String.format(", average rating %.2f/5", s.getAverageRating());
        }
        console.println(evaluations);
    }

    // per company table. both lists MUST be same order
    static void printCompanies(Console console, List<Company> companies, List<Summary> summaries) {
        List<String[]> rows = new ArrayList<>();
        for (int i = 0; i < companies.size(); i++) {
            Summary s = summaries.get(i);
            rows.add(new String[] { companies.get(i).getName(), String.valueOf(s.getInterns()),
                    String.valueOf(s.getActive()), String.valueOf(s.getCompleted()),
                    s.getRecordedHours() + "/" + s.getRequiredHours(), String.valueOf(s.getOpenOpenings()),
                    String.valueOf(s.getPending()) });
        }
        console.println("  Per company");
        console.table(new String[] { "Company", "Interns", "Active", "Completed", "Hours", "Open openings", "Pending apps" },
                rows);
    }
}
