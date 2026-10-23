/*
 * VIEW layer: the text user interface (TUI).
 *
 * Views print screens and read what the user types (always through Console). Each dashboard builds
 * a Menu whose items are small private methods; one menu item = one "drill" (view -> controller ->
 * service -> repository and back). Views never call services or repositories directly.
 */
package iadms4.view;
