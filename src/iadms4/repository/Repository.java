package iadms4.repository;

import java.util.ArrayList;
import java.util.List;

/*
 * REPOSITORY base class. acts like a database table but its just a list in memory
 * save, find, remove, next id. NO business rules here, those go in services
 * T means whatever type the child class stores
 */
public abstract class Repository<T> {
    private final List<T> items = new ArrayList<>();
    private int nextId = 1;

    // child classes write this because only they know what T is
    protected abstract int idOf(T item);

    // gives the next free id then moves the counter up
    public int nextId() {
        int id = nextId;
        nextId++;
        return id;
    }

    // save item. same id means replace, keeps its spot in the list
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

    // find by id, null if none
    public T findById(int id) {
        for (T item : items) {
            if (idOf(item) == id) {
                return item;
            }
        }
        return null;
    }

    // gives a copy so nobody messes up the real list
    public List<T> findAll() {
        return new ArrayList<>(items);
    }

    // finds the item with this id and removes it, does nothing if not found
    public void remove(int id) {
        for (int i = 0; i < items.size(); i++) {
            if (idOf(items.get(i)) == id) {
                items.remove(i);
                return;
            }
        }
    }
}
