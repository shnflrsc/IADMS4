package iadms4.model;

/*
 * MODEL. Enum describing the lifecycle of an Application.
 *
 * Every application starts as PENDING. The company representative then APPROVES or REJECTS it,
 * or a coordinator WITHDRAWS it while it is still pending.
 */
public enum ApplicationStatus {
    // Endorsed by a coordinator and waiting for the company's decision.
    PENDING,
    // The company representative denied the application.
    REJECTED,
    // The company representative accepted the application; the student becomes an intern.
    APPROVED,
    // A coordinator cancelled the application before the company decided.
    WITHDRAWN
}
