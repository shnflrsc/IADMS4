package iadms4.repository;

import java.util.List;

import iadms4.model.DailyLog;

/**
 * REPOSITORY. Stores DailyLogs; can list the logs of one intern.
 */
public class DailyLogRepository extends Repository<DailyLog> {
    @Override
    protected int idOf(DailyLog item) {
        return item.getId();
    }

    public List<DailyLog> findByInternId(int internId) {
        return findAll().stream().filter(l -> l.getIntern().getId() == internId).toList();
    }
}
