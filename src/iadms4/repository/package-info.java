/**
 * REPOSITORY layer: in-memory storage (maps keyed by id). One repository per kind of object.
 *
 * Rule of thumb: a repository answers "give me / save this object". It never decides whether
 * something is allowed - that is the job of the service layer above it.
 */
package iadms4.repository;
