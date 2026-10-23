/*
 * CONTROLLER layer: the glue between views and services.
 *
 * One controller per kind of dashboard. A controller method receives what the user typed from the
 * view, finds who is logged in (Session), calls the right service method and returns the result.
 * Keep them thin: no rules and no printing here.
 */
package iadms4.controller;
