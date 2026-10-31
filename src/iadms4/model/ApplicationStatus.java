package iadms4.model;

/*
 * MODEL. enum for application status
 * starts PENDING. rep approves/rejects or coordinator withdraws
 */
public enum ApplicationStatus {
    // waiting for company
    PENDING,
    // rep said no
    REJECTED,
    // rep said yes, student becomes intern
    APPROVED,
    // coordinator cancelled it
    WITHDRAWN
}
