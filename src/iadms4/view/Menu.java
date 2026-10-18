package iadms4.view;

import java.util.ArrayList;
import java.util.List;

/**
 * VIEW helper. Builds a numbered menu: add(label, action) registers an item and run() shows it in
 * a loop until the user picks 0. Errors thrown by services are caught here and shown to the user.
 */
public final class Menu {
    private record Item(String label, Runnable action) {
    }

    private final Console console;
    private final String title;
    private final String exitLabel;
    private final List<Item> items = new ArrayList<>();

    public Menu(Console console, String title, String exitLabel) {
        this.console = console;
        this.title = title;
        this.exitLabel = exitLabel;
    }

    public Menu add(String label, Runnable action) {
        items.add(new Item(label, action));
        return this;
    }

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
