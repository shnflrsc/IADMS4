package iadms4.repository;

import java.util.List;

import iadms4.model.DailyLog;

/*
 * REPOSITORY. Stores DailyLog entries.
 *
 * Responsibilities: the basic operations from Repository plus listing the logs of one intern.
 */
public class DailyLogRepository extends Repository<DailyLog> {
    /*
     * Reads the id of a log so the base class can use it as the map key.
     *
     * It takes item (the daily log). It returns the log's id.
     */
    @Override
    protected int idOf(DailyLog item) {
        return item.getId();
    }

    /*
     * Lists the daily logs written by one intern.
     *
     * It takes internId (the id of the intern). It returns that intern's logs (empty if there are none).
     */
    public List<DailyLog> findByInternId(int internId) {
        return findAll().stream().filter(l -> l.getIntern().getId() == internId).toList();
    }
}
