package iadms4.view;

import java.util.List;

import iadms4.service.ReportService.CompanyLine;
import iadms4.service.ReportService.Summary;

/**
 * VIEW helper. Prints a summary report so the coordinator and representative dashboards show the
 * same layout.
 */
final class Reports {
    private Reports() {
    }

    static void print(Console console, String title, Summary s, boolean showStudents) {
        console.println("  " + title);
        if (showStudents) {
            console.println("  Students not yet deployed: " + s.students());
        }
        console.println("  Interns:      " + s.interns() + "  (ACTIVE " + s.active() + ", DEACTIVATED "
                + s.deactivated() + ", COMPLETED " + s.completed() + ")");
        console.println("  Hours:        " + s.recordedHours() + " recorded of " + s.requiredHours() + " required ("
                + s.progressPercent() + "%)");
        console.println("  Openings:     " + s.openOpenings() + " open, " + s.closedOpenings() + " closed");
        console.println("  Applications: PENDING " + s.pending() + ", APPROVED " + s.approved() + ", REJECTED "
                + s.rejected() + ", WITHDRAWN " + s.withdrawn());
        console.println("  Evaluations:  " + s.evaluations()
                + (s.evaluations() == 0 ? "" : String.format(", average rating %.2f/5", s.averageRating())));
    }

    static void printCompanies(Console console, List<CompanyLine> lines) {
        List<String[]> rows = lines.stream().map(l -> new String[] { l.company(),
                String.valueOf(l.summary().interns()), String.valueOf(l.summary().active()),
                String.valueOf(l.summary().completed()),
                l.summary().recordedHours() + "/" + l.summary().requiredHours(),
                String.valueOf(l.summary().openOpenings()), String.valueOf(l.summary().pending()) }).toList();
        console.println("  Per company");
        console.table(new String[] { "Company", "Interns", "Active", "Completed", "Hours", "Open openings", "Pending apps" },
                rows);
    }
}
