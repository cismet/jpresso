/*
 * JPressoException.java
 *
 * Created on 22. September 2003, 11:09
 */
package de.cismet.jpresso.core.serviceprovider.exceptions;

/** Gefangene Exceptions in diesem Package sind alle von diesem Typ
 * @author hell
 */
public class JPressoException extends java.lang.Exception {

    /** Creates a new instance of JPressoException */
    public JPressoException() {
    }

    /** Zweiter Konstruktor der einen String uebergibt
     * @param s String Wert der Exception
     */
    public JPressoException(String s) {
        super(s);
    }

    /** Dritter Konstruktor der einen String und eine Exception uebergibt
     * @param s String Wert der Exception
     * @param e Zusaetzliche Exception
     */
    public JPressoException(String s, Throwable e) {
        super(s, e);
    }
}
