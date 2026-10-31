package iadms4.view;

import java.util.ArrayList;
import java.util.List;

import iadms4.model.Company;
import iadms4.model.Summary;

/*
 * VIEW helper. Prints summary reports so that the coordinator and representative dashboards
 * show the same layout. All methods are static.
 */
class Reports {
    // Prints one summary with the given title. showStudents adds the count of students not yet deployed.
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

    // Prints a table with one row per company. The summaries must be in the same order as the companies.
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
