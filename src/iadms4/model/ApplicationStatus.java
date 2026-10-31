package iadms4.model;

/*
 * MODEL. enum for application status
 * starts PENDING. representative approves/rejects or coordinator withdraws
 */
public enum ApplicationStatus {
    // endorsed already, still waiting for the company to decide
    PENDING,
    // the company representative said no to this application
    REJECTED,
    // representative said yes, student becomes intern
    APPROVED,
    // coordinator cancelled it before the company could decide
    WITHDRAWN
}
