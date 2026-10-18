package iadms4.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REPOSITORY (base class). A repository is the in-memory "database table" for one type of object.
 * It stores objects in a map keyed by id and hands out new ids. Repositories only store and find
 * data - business rules belong in the service layer, not here. Data is lost when the app exits.
 */
public abstract class Repository<T> {
    private final Map<Integer, T> items = new LinkedHashMap<>();
    private int nextId = 1;

    protected abstract int idOf(T item);

    public int nextId() {
        return nextId++;
    }

    public T save(T item) {
        items.put(idOf(item), item);
        return item;
    }

    public Optional<T> findById(int id) {
        return Optional.ofNullable(items.get(id));
    }

    public List<T> findAll() {
        return new ArrayList<>(items.values());
    }

    public boolean remove(int id) {
        return items.remove(id) != null;
    }
}
