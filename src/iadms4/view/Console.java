package iadms4.view;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Function;

import iadms4.model.Gender;

/**
 * VIEW helper. The only class that touches System.in / System.out. Every screen uses it to print
 * text, clear the terminal, read validated numbers/dates, and draw tables.
 */
public final class Console {
    /** Thrown when standard input is closed. */
    public static final class InputClosedException extends RuntimeException {
        public InputClosedException() {
            super("Input closed");
        }
    }

    private static final String RULE = "=".repeat(60);

    private final Scanner in = new Scanner(System.in);

    public void println(String text) {
        System.out.println(text);
    }

    /** Clears the terminal (ANSI: erase screen and scrollback, cursor home). */
    public void clear() {
        System.out.print("\033[H\033[2J\033[3J");
        System.out.flush();
    }

    /** Starts a new screen: clears the terminal, then prints the title banner. */
    public void header(String title) {
        clear();
        println(RULE);
        println("  " + title);
        println(RULE);
    }

    public String readLine(String prompt) {
        System.out.print(prompt + ": ");
        System.out.flush();
        try {
            return in.nextLine().trim();
        } catch (NoSuchElementException e) {
            throw new InputClosedException();
        }
    }

    public String readRequired(String prompt) {
        while (true) {
            String value = readLine(prompt);
            if (!value.isEmpty()) {
                return value;
            }
            println("  ! A value is required.");
        }
    }

    public int readInt(String prompt, int min, int max) {
        while (true) {
            String raw = readLine(prompt);
            try {
                int value = Integer.parseInt(raw);
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
                // fall through to message
            }
            println("  ! Enter a number between " + min + " and " + max + ".");
        }
    }

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

    /** Like readDate but a blank answer keeps the current value and returns null. */
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

    /** Picks one value of an enum as a filter; choosing 0 means "All" and returns null. */
    public <T> T chooseFilter(String prompt, T[] values) {
        println("  [0] All");
        for (int i = 0; i < values.length; i++) {
            println("  [" + (i + 1) + "] " + values[i]);
        }
        int n = readInt(prompt, 0, values.length);
        return n == 0 ? null : values[n - 1];
    }

    public Gender readGender() {
        Gender[] values = Gender.values();
        for (int i = 0; i < values.length; i++) {
            println("  [" + (i + 1) + "] " + values[i]);
        }
        return values[readInt("Gender", 1, values.length) - 1];
    }

    public void pause() {
        readLine("Press Enter to continue");
    }

    /** Lets the user pick from a list; returns null when cancelled or the list is empty. */
    public <T> T choose(String prompt, List<T> items, Function<T, String> label) {
        if (items.isEmpty()) {
            println("  (nothing to choose from)");
            return null;
        }
        for (int i = 0; i < items.size(); i++) {
            println("  [" + (i + 1) + "] " + label.apply(items.get(i)));
        }
        int n = readInt(prompt + " (0 to cancel)", 0, items.size());
        return n == 0 ? null : items.get(n - 1);
    }

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
        StringBuilder sep = new StringBuilder("  ");
        for (int w : widths) {
            sep.append("-".repeat(w)).append("  ");
        }
        println(sep.toString());
        rows.forEach(r -> printRow(r, widths));
    }

    private void printRow(String[] cells, int[] widths) {
        StringBuilder sb = new StringBuilder("  ");
        for (int c = 0; c < cells.length; c++) {
            sb.append(String.format("%-" + widths[c] + "s", cells[c])).append("  ");
        }
        println(sb.toString());
    }
}
