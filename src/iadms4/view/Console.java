package iadms4.view;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Function;

import iadms4.model.Gender;

/*
 * VIEW helper. The only class that touches System.in and System.out.
 *
 * Responsibilities: every screen uses it to print text, clear the terminal, read validated numbers and
 * dates, let the user pick from a list, and draw tables. Keeping all input and output in one place
 * means the other classes never have to deal with Scanner directly.
 */
public final class Console {
    // Thrown when standard input is closed (for example when the user presses Ctrl+D).
    public static final class InputClosedException extends RuntimeException {
        // Creates the exception with a fixed message.
        public InputClosedException() {
            super("Input closed");
        }
    }

    // A line of 60 equal signs used in screen banners.
    private static final String RULE = "=".repeat(60);

    // Reads what the user types.
    private final Scanner in = new Scanner(System.in);

    /*
     * Prints a line of text.
     *
     * It takes text (the text to print).
     */
    public void println(String text) {
        System.out.println(text);
    }

    // Clears the terminal (ANSI: erase screen and scrollback, cursor home).
    public void clear() {
        System.out.print("\033[H\033[2J\033[3J");
        System.out.flush();
    }

    /*
     * Starts a new screen: clears the terminal, then prints the title banner.
     *
     * It takes title (the title shown in the banner).
     */
    public void header(String title) {
        clear();
        println(RULE);
        println("  " + title);
        println(RULE);
    }

    /*
     * Shows a prompt and reads one line of text.
     *
     * It takes prompt (the question shown to the user). It returns the typed text with outer spaces removed
     * (may be empty). It throws InputClosedException if standard input has been closed.
     */
    public String readLine(String prompt) {
        System.out.print(prompt + ": ");
        System.out.flush();
        try {
            return in.nextLine().trim();
        } catch (NoSuchElementException e) {
            throw new InputClosedException();
        }
    }

    /*
     * Reads a line and keeps asking until the user types something.
     *
     * It takes prompt (the question shown to the user). It returns a non-empty text.
     */
    public String readRequired(String prompt) {
        while (true) {
            String value = readLine(prompt);
            if (!value.isEmpty()) {
                return value;
            }
            println("  ! A value is required.");
        }
    }

    /*
     * Reads a whole number inside a range and keeps asking until the input is valid.
     *
     * It takes prompt (the question shown to the user), min (the smallest accepted number) and max (the
     * largest accepted number). It returns the number typed by the user.
     */
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

    /*
     * Reads a date in the format yyyy-mm-dd and keeps asking until it is valid.
     *
     * It takes prompt (the question shown to the user). It returns the date typed by the user.
     */
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

    /*
     * Like readDate but a blank answer keeps the current value.
     *
     * It takes prompt (the question shown to the user). It returns the date typed by the user, or null if
     * the answer was blank.
     */
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

    /*
     * Picks one value of an enum as a filter; choosing 0 means "All".
     *
     * It takes prompt (the question shown to the user) and values (the enum constants to choose from (for
     * example InternStatus.values())). The type parameter T is the enum type. It returns the chosen value,
     * or null when the user chose "All".
     */
    public <T> T chooseFilter(String prompt, T[] values) {
        println("  [0] All");
        for (int i = 0; i < values.length; i++) {
            println("  [" + (i + 1) + "] " + values[i]);
        }
        int n = readInt(prompt, 0, values.length);
        return n == 0 ? null : values[n - 1];
    }

    /*
     * Shows the genders as a numbered list and reads the choice.
     *
     * It returns the gender chosen by the user.
     */
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

    /*
     * Lets the user pick from a list.
     *
     * It takes prompt (the question shown to the user), items (the objects to choose from) and label (a
     * function that turns one object into the text shown in the list). The type parameter T is the type of
     * the objects in the list. It returns the chosen object, or null when the user cancelled or the list is
     * empty.
     */
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

    /*
     * Prints a table whose columns are as wide as their longest cell.
     *
     * It takes headers (the column titles) and rows (the table rows; each row needs one cell per header).
     */
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

    /*
     * Prints one table row with every cell padded to its column width.
     *
     * It takes cells (the text of each cell) and widths (the width of each column).
     */
    private void printRow(String[] cells, int[] widths) {
        StringBuilder sb = new StringBuilder("  ");
        for (int c = 0; c < cells.length; c++) {
            sb.append(String.format("%-" + widths[c] + "s", cells[c])).append("  ");
        }
        println(sb.toString());
    }
}
