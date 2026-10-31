package iadms4.view;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import iadms4.model.ApplicationStatus;
import iadms4.model.Gender;
import iadms4.model.InternStatus;

/*
 * VIEW helper. The only class that touches System.in and System.out.
 * Every screen uses it to print text, read validated input, pick from a list and draw tables.
 */
public class Console {
    private static final String RULE = "=".repeat(60);

    private final Scanner in = new Scanner(System.in);

    // Prints the given text on its own line.
    public void println(String text) {
        System.out.println(text);
    }

    // Clears the terminal using ANSI codes (erase the screen and scrollback, move the cursor home).
    public void clear() {
        System.out.print("\033[H\033[2J\033[3J");
        System.out.flush();
    }

    // Clears the terminal and prints a banner with the given title.
    public void header(String title) {
        clear();
        println(RULE);
        println("  " + title);
        println(RULE);
    }

    // Shows the given prompt and returns the typed line without outer spaces (it may be empty).
    public String readLine(String prompt) {
        System.out.print(prompt + ": ");
        System.out.flush();
        return in.nextLine().trim();
    }

    // Shows the given prompt until the user types something, then returns it.
    public String readRequired(String prompt) {
        while (true) {
            String value = readLine(prompt);
            if (!value.isEmpty()) {
                return value;
            }
            println("  ! A value is required.");
        }
    }

    // Shows the given prompt until the user types a whole number from min to max, then returns it.
    public int readInt(String prompt, int min, int max) {
        while (true) {
            String raw = readLine(prompt);
            try {
                int value = Integer.parseInt(raw);
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException e) {
                // Not a number, so the message below is shown.
            }
            println("  ! Enter a number between " + min + " and " + max + ".");
        }
    }

    // Shows the given prompt until the user types a date in the format yyyy-mm-dd, then returns it.
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

    // Works like readDate, but returns null if the answer is blank.
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

    // Lets the user pick an intern status to filter by and returns it, or null for "All".
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

    // Lets the user pick an application status to filter by and returns it, or null for "All".
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

    // Shows the genders as a numbered list and returns the one chosen.
    public Gender readGender() {
        Gender[] values = Gender.values();
        for (int i = 0; i < values.length; i++) {
            println("  [" + (i + 1) + "] " + values[i]);
        }
        return values[readInt("Gender", 1, values.length) - 1];
    }

    // Waits for the user to press Enter so that the output stays visible.
    public void pause() {
        readLine("Press Enter to continue");
    }

    // Shows the given labels as a numbered list and returns the index picked, or -1 if cancelled or empty.
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

    // Prints a table with the given headers and rows. Each column is as wide as its longest cell.
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

    // Prints one table row with every cell padded to its column width.
    private void printRow(String[] cells, int[] widths) {
        String line = "  ";
        for (int c = 0; c < cells.length; c++) {
            line += String.format("%-" + widths[c] + "s", cells[c]) + "  ";
        }
        println(line);
    }
}
