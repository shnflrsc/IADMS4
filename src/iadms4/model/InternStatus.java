package iadms4.model;

/*
 * MODEL. enum for intern status
 * COMPLETED is final, cant change after that
 */
public enum InternStatus {
    // working, can log hours
    ACTIVE,
    // paused, no logging
    DEACTIVATED,
    // done. evaluation can happen now
    COMPLETED
}
