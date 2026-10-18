package iadms4.model;

/**
 * MODEL. Enum: lifecycle of an Application (PENDING, then APPROVED, REJECTED by the company, or WITHDRAWN by a coordinator).
 */
public enum ApplicationStatus {
    PENDING,
    REJECTED,
    APPROVED,
    WITHDRAWN
}
