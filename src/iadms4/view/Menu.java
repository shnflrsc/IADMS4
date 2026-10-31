package iadms4.view;

/*
 * VIEW base class. numbered menu
 * dashboards extend this and write runItem()
 * service errors get caught here so the program doesnt crash
 */
public abstract class Menu {
    protected final Console console;

    // constructor
    public Menu(Console console) {
        this.console = console;
    }

    // every dashboard has to write this
    protected abstract void runItem(int choice);

    // the menu loop. runs til user picks 0
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
