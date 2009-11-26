/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.exceptions;

/**
 *
 * @author stefan
 */
public class DriverLoadException extends Exception {

    public DriverLoadException() {
        super();
    }

    public DriverLoadException(String msg) {
        super(msg);
    }

    public DriverLoadException(Exception ex) {
        super(ex);
    }

    public DriverLoadException(String msg, Exception ex) {
        super(msg, ex);
    }
}
