package iadms4.repository;

import java.util.ArrayList;
import java.util.List;

import iadms4.model.DailyLog;

/*
 * REPOSITORY. Stores DailyLog entries and can list the logs of one intern.
 */
public class DailyLogRepository extends Repository<DailyLog> {
    // Returns the id of the given log.
    @Override
    protected int idOf(DailyLog item) {
        return item.getId();
    }

    // Returns the logs written by the intern with the given id.
    public List<DailyLog> findByInternId(int internId) {
        List<DailyLog> matches = new ArrayList<>();
        for (DailyLog log : findAll()) {
            if (log.getIntern().getId() == internId) {
                matches.add(log);
            }
        }
        return matches;
    }
}
