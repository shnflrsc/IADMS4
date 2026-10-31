package iadms4.model;

/*
 * MODEL. enum for application status
 * starts PENDING. rep approves/rejects or coordinator withdraws
 */
public enum ApplicationStatus {
    // endorsed already, still waiting for the company to decide
    PENDING,
    // the company rep said no to this application
    REJECTED,
    // rep said yes, student becomes intern
    APPROVED,
    // coordinator cancelled it before the company could decide
    WITHDRAWN
}
