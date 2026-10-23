package iadms4.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/*
 * REPOSITORY (base class). A repository is the in-memory "database table" for one type of object.
 *
 * Responsibilities: stores objects in a map keyed by id, hands out new ids, and offers the basic
 * save / find / remove operations. Repositories only store and find data - business rules belong in the
 * service layer, not here. Data is lost when the app exits.
 *
 * This class uses a generic type parameter T, so one piece of code can serve Persons, Companies,
 * Openings and so on. Each child class decides what T is.
 *
 * The type parameter T is the type of object stored in this repository.
 */
public abstract class Repository<T> {
    // The storage. LinkedHashMap keeps items in the order they were saved.
    private final Map<Integer, T> items = new LinkedHashMap<>();
    // The id that will be given out next.
    private int nextId = 1;

    /*
     * Tells the repository how to read the id of an item. Each child class implements this because
     * the base class does not know what T is.
     *
     * It takes item (the object to read the id from). It returns the id of the item.
     */
    protected abstract int idOf(T item);

    /*
     * Gives out a fresh unique id and moves the counter forward.
     *
     * It returns the next unused id.
     */
    public int nextId() {
        return nextId++;
    }

    /*
     * Stores an item. If an item with the same id already exists it is replaced.
     *
     * It takes item (the object to store). It returns the same item that was stored.
     */
    public T save(T item) {
        items.put(idOf(item), item);
        return item;
    }

    /*
     * Looks up one item by its id.
     *
     * It takes id (the id to look for). It returns an Optional holding the item, or an empty Optional if
     * none has that id.
     */
    public Optional<T> findById(int id) {
        return Optional.ofNullable(items.get(id));
    }

    /*
     * Gets every stored item. A copy of the list is returned so callers cannot change the storage by accident.
     *
     * It returns a new list of all items.
     */
    public List<T> findAll() {
        return new ArrayList<>(items.values());
    }

    /*
     * Deletes the item with the given id.
     *
     * It takes id (the id of the item to delete). It returns true if an item was removed, false if no item
     * had that id.
     */
    public boolean remove(int id) {
        return items.remove(id) != null;
    }
}
