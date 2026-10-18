package iadms4.model;

/**
 * MODEL. Enum: ACTIVE (can log hours), DEACTIVATED (paused by the representative) or COMPLETED
 * (set manually by the company representative once the required hours are met; final).
 */
public enum InternStatus {
    ACTIVE,
    DEACTIVATED,
    COMPLETED
}
