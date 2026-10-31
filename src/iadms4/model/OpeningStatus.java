package iadms4.model;

/*
 * MODEL. enum for opening status
 * rep can switch between these 2
 */
public enum OpeningStatus {
    // coordinators can still endorse students to this one
    OPEN,
    // closed, no more endorsements for this one
    CLOSED
}
