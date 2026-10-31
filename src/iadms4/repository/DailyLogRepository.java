package iadms4.repository;

import java.util.ArrayList;
import java.util.List;

import iadms4.model.DailyLog;

/*
 * REPOSITORY. stores daily logs
 */
public class DailyLogRepository extends Repository<DailyLog> {
    // tells the base class how to get a log's id
    @Override
    protected int idOf(DailyLog item) {
        return item.getId();
    }

    // goes through all logs and keeps the ones of this intern
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
