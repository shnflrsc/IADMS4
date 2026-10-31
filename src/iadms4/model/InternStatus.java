package iadms4.model;

/*
 * MODEL. enum for intern status
 * COMPLETED is final, can't change after that
 */
public enum InternStatus {
    // currently working, this is the only status that can log hours
    ACTIVE,
    // representative paused the internship so no logging hours for now
    DEACTIVATED,
    // hours are done and representative closed it, evaluation can happen now
    COMPLETED
}
