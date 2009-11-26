/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.serviceprovider.exceptions;

/**
 *
 * @author stefan
 */
public class DriverLoaderCreateException extends Exception {

    public DriverLoaderCreateException() {
        super();
    }

    public DriverLoaderCreateException(String msg) {
        super(msg);
    }

    public DriverLoaderCreateException(Exception ex) {
        super(ex);
    }

    public DriverLoaderCreateException(String msg, Exception ex) {
        super(msg, ex);
    }
}
