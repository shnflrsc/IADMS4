package iadms4.repository;

import java.util.ArrayList;
import java.util.List;

import iadms4.model.DailyLog;

/*
 * REPOSITORY. stores daily logs
 */
public class DailyLogRepository extends Repository<DailyLog> {
    // id of log
    @Override
    protected int idOf(DailyLog item) {
        return item.getId();
    }

    // logs of one intern
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
