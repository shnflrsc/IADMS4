package iadms4.view;

import java.util.ArrayList;
import java.util.List;

/*
 * VIEW helper. Builds and runs a numbered menu.
 *
 * Responsibilities: add(label, action) registers an item, and run() shows the menu in a loop until
 * the user picks 0. Errors thrown by the services are caught here and shown to the user, so a broken
 * rule never crashes the program. Each chosen item starts one "drill" (view, controller, service,
 * repository and back).
 */
public final class Menu {
    /*
     * One menu entry: the text the user sees and the code that runs when it is chosen.
     *
     * Its components are label (the text shown in the menu) and action (the code to run when the item is
     * chosen).
     */
    private record Item(String label, Runnable action) {
    }

    // Used for printing and reading.
    private final Console console;
    // Title shown at the top of the menu.
    private final String title;
    // Label of item 0, for example "Log out" or "Back".
    private final String exitLabel;
    // The registered menu items, in the order they were added.
    private final List<Item> items = new ArrayList<>();

    /*
     * Creates an empty menu.
     *
     * It takes console (the console used for input and output), title (the title shown at the top) and
     * exitLabel (the label of option 0).
     */
    public Menu(Console console, String title, String exitLabel) {
        this.console = console;
        this.title = title;
        this.exitLabel = exitLabel;
    }

    /*
     * Adds an item to the menu. It returns the menu itself so that calls can be chained.
     *
     * It takes label (the text shown to the user) and action (the code to run when the item is chosen). It
     * returns this menu.
     */
    public Menu add(String label, Runnable action) {
        items.add(new Item(label, action));
        return this;
    }

    // Shows the menu again and again, running the chosen item each time, until the user picks 0.
    public void run() {
        while (true) {
            console.header(title);
            for (int i = 0; i < items.size(); i++) {
                console.println("  [" + (i + 1) + "] " + items.get(i).label());
            }
            console.println("  [0] " + exitLabel);
            int choice = console.readInt("Select", 0, items.size());
            if (choice == 0) {
                return;
            }
            // Run the chosen menu action. Rule violations from the service layer arrive as exceptions;
            // we print their message instead of crashing and return to the menu.
            try {
                items.get(choice - 1).action().run();
            } catch (IllegalArgumentException | IllegalStateException e) {
                console.println("  ! " + e.getMessage());
            }
            console.pause();
        }
    }
}
