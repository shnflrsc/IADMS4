package iadms4.repository;

import java.util.ArrayList;
import java.util.List;

/*
 * REPOSITORY (base class). The in-memory "database table" for one type of object.
 * It stores items in a list, hands out new ids, and offers save, find and remove. Business rules belong
 * in the service layer. T is the type of object stored, and each child class decides what T is.
 */
public abstract class Repository<T> {
    private final List<T> items = new ArrayList<>();
    private int nextId = 1;

    // Returns the id of the given item. Each child class writes this because only it knows what T is.
    protected abstract int idOf(T item);

    // Returns a new unused id and moves the counter forward.
    public int nextId() {
        int id = nextId;
        nextId++;
        return id;
    }

    // Stores the given item and returns it. An item with the same id is replaced in the same place.
    public T save(T item) {
        for (int i = 0; i < items.size(); i++) {
            if (idOf(items.get(i)) == idOf(item)) {
                items.set(i, item);
                return item;
            }
        }
        items.add(item);
        return item;
    }

    // Returns the item with the given id, or null if there is none.
    public T findById(int id) {
        for (T item : items) {
            if (idOf(item) == id) {
                return item;
            }
        }
        return null;
    }

    // Returns a copy of the list of items, so callers cannot change the storage by accident.
    public List<T> findAll() {
        return new ArrayList<>(items);
    }

    // Deletes the item with the given id.
    public void remove(int id) {
        for (int i = 0; i < items.size(); i++) {
            if (idOf(items.get(i)) == id) {
                items.remove(i);
                return;
            }
        }
    }
}
