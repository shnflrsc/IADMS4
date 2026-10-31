package iadms4.view;

/*
 * VIEW (base class). A screen that shows a numbered menu.
 * Each dashboard extends this class and writes runItem(), which decides what each number does.
 * Errors thrown by the services are caught here and shown to the user, so a broken rule never
 * crashes the program.
 */
public abstract class Menu {
    protected final Console console;

    // Creates the menu using the given console for input and output.
    public Menu(Console console) {
        this.console = console;
    }

    // Runs the menu item with the given number. Each dashboard writes its own version.
    protected abstract void runItem(int choice);

    // Shows the menu with the given title and items again and again until the user picks 0 (exitLabel).
    protected void run(String title, String[] items, String exitLabel) {
        while (true) {
            console.header(title);
            for (int i = 0; i < items.length; i++) {
                console.println("  [" + (i + 1) + "] " + items[i]);
            }
            console.println("  [0] " + exitLabel);
            int choice = console.readInt("Select", 0, items.length);
            if (choice == 0) {
                return;
            }
            try {
                runItem(choice);
            } catch (IllegalArgumentException e) {
                console.println("  ! " + e.getMessage());
            } catch (IllegalStateException e) {
                console.println("  ! " + e.getMessage());
            }
            console.pause();
        }
    }
}
