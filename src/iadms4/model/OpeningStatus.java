package iadms4.model;

/*
 * MODEL. Enum describing whether an Opening can still receive endorsements.
 *
 * A company representative can switch an opening between these two values.
 */
public enum OpeningStatus {
    // The opening is accepting endorsements.
    OPEN,
    // The opening no longer accepts endorsements.
    CLOSED
}
