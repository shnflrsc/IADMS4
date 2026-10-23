package iadms4.model;

/*
 * MODEL. Enum describing the state of an Intern during the internship.
 *
 * COMPLETED is final: once an intern is completed the status can no longer change.
 */
public enum InternStatus {
    // The intern is currently working and can log hours.
    ACTIVE,
    // The company representative paused the internship; no hours can be logged.
    DEACTIVATED,
    // The required hours were met and the representative closed the internship. This unlocks the evaluation.
    COMPLETED
}
