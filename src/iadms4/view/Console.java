package iadms4.view;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import iadms4.model.ApplicationStatus;
import iadms4.model.Gender;
import iadms4.model.InternStatus;

/*
 * VIEW helper. ONLY class that uses System.in / System.out
 * all printing and reading goes here
 */
public class Console {
    private static final String RULE = "=".repeat(60);

    private final Scanner in = new Scanner(System.in);

    // prints text on its own line, shorter than System.out.println
    public void println(String text) {
        System.out.println(text);
    }

    // clears terminal with ANSI codes. they work don't touch
    public void clear() {
        System.out.print("\033[H\033[2J\033[3J");
        System.out.flush();
    }

    // clears the screen then prints the title between two lines
    public void header(String title) {
        clear();
        println(RULE);
        println("  " + title);
        println(RULE);
    }

    // reads a line, trimmed, can be empty
    public String readLine(String prompt) {
        System.out.print(prompt + ": ");
        System.out.flush();
        return in.nextLine().trim();
    }

    // same as readLine but keeps asking if the answer is empty
    public String readRequired(String prompt) {
        while (true) {
            String value = readLine(prompt);
            if (!value.isEmpty()) {
                return value;
            }
            println("  ! A value is required.");
        }
    }

    // keeps asking until valid number between min and max
    public int readInt(String prompt, int min, int max) {
        while (true) {
            String raw = readLine(prompt);
            try {
                int value = Integer.parseInt(raw);
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException e) {
                // not a number, message below handles it
            }
            println("  ! Enter a number between " + min + " and " + max + ".");
        }
    }

    // keeps asking until valid date yyyy-mm-dd
    public LocalDate readDate(String prompt) {
        while (true) {
            String raw = readLine(prompt + " (yyyy-mm-dd)");
            try {
                return LocalDate.parse(raw);
            } catch (DateTimeParseException e) {
                println("  ! Use the format yyyy-mm-dd.");
            }
        }
    }

    // same as readDate but blank is allowed and returns null
    public LocalDate readOptionalDate(String prompt) {
        while (true) {
            String raw = readLine(prompt + " (yyyy-mm-dd, blank = keep)");
            if (raw.isEmpty()) {
                return null;
            }
            try {
                return LocalDate.parse(raw);
            } catch (DateTimeParseException e) {
                println("  ! Use the format yyyy-mm-dd.");
            }
        }
    }

    // intern status filter, null means All
    public InternStatus chooseInternStatus() {
        InternStatus[] values = InternStatus.values();
        println("  [0] All");
        for (int i = 0; i < values.length; i++) {
            println("  [" + (i + 1) + "] " + values[i]);
        }
        int choice = readInt("Status", 0, values.length);
        if (choice == 0) {
            return null;
        }
        return values[choice - 1];
    }

    // application status filter, null means All. basically same as above
    public ApplicationStatus chooseApplicationStatus() {
        ApplicationStatus[] values = ApplicationStatus.values();
        println("  [0] All");
        for (int i = 0; i < values.length; i++) {
            println("  [" + (i + 1) + "] " + values[i]);
        }
        int choice = readInt("Status", 0, values.length);
        if (choice == 0) {
            return null;
        }
        return values[choice - 1];
    }

    // shows the genders as a list and returns the one picked
    public Gender readGender() {
        Gender[] values = Gender.values();
        for (int i = 0; i < values.length; i++) {
            println("  [" + (i + 1) + "] " + values[i]);
        }
        return values[readInt("Gender", 1, values.length) - 1];
    }

    // wait for enter or the screen clears too fast
    public void pause() {
        readLine("Press Enter to continue");
    }

    // numbered list, returns index. -1 means cancelled or empty list
    public int choose(String prompt, List<String> labels) {
        if (labels.isEmpty()) {
            println("  (nothing to choose from)");
            return -1;
        }
        for (int i = 0; i < labels.size(); i++) {
            println("  [" + (i + 1) + "] " + labels.get(i));
        }
        return readInt(prompt + " (0 to cancel)", 0, labels.size()) - 1;
    }

    // prints a table, columns fit the longest cell
    public void table(String[] headers, List<String[]> rows) {
        if (rows.isEmpty()) {
            println("  (none)");
            return;
        }
        int[] widths = new int[headers.length];
        for (int c = 0; c < headers.length; c++) {
            widths[c] = headers[c].length();
            for (String[] row : rows) {
                widths[c] = Math.max(widths[c], row[c].length());
            }
        }
        printRow(headers, widths);
        String separator = "  ";
        for (int width : widths) {
            separator += "-".repeat(width) + "  ";
        }
        println(separator);
        for (String[] row : rows) {
            printRow(row, widths);
        }
    }

    // prints one row, each cell gets padded to its column width
    private void printRow(String[] cells, int[] widths) {
        String line = "  ";
        for (int c = 0; c < cells.length; c++) {
            line += String.format("%-" + widths[c] + "s", cells[c]) + "  ";
        }
        println(line);
    }
}
