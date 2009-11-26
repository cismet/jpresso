/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.cismet.jpresso.core.exceptions;

/**
 *
 * @author srichter
 */
public class InvalidFormatFileException extends Exception {

    public InvalidFormatFileException(String message) {
        super(message);
    }
    public InvalidFormatFileException(Exception e) {
        super(e);
    }
    public InvalidFormatFileException() {
    }
    

}
