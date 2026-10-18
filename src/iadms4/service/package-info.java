/**
 * SERVICE layer: the business rules of the system.
 *
 * Services validate input, enforce rules (for example "an intern can only be evaluated once"),
 * coordinate several repositories, and throw IllegalArgumentException / IllegalStateException with
 * a user-friendly message when a rule is broken. The Menu class catches those and shows the message.
 */
package iadms4.service;
