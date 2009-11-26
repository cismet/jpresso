/*
 * DynamicCompilingException.java
 *
 * Created on 26. September 2003, 12:43
 */
package de.cismet.jpresso.core.serviceprovider.exceptions;

import java.util.*;
import java.util.ArrayList;

/** Exception die beim dynamischen Kompilieren der Assigner Klassen
 * geworfen wird. Mit dieser Exception kann man naeheres ueber die Fehlerursache
 * herausfinden.
 *
 * Teile des Codes sind nicht von mir. Aus dem OpenSource Projekt Schmortopf ;-)
 * @author hell
 */
public class DynamicCompilingException extends JPressoException {

    /** Logger */
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private List<String> details;
    private List<String> possibleErr;
    private String sourceCode;
//    private List<String> errors;
    /** Creates a new instance of DynamicCompilingException
     * @param message Komplette Fehlerausgabe des Java-Compilers
     */
//    public DynamicCompilingException(String message, List<String> errors) {
//        super(message);
//        if (errors != null) {
//            this.errors = errors;
//        } else {
//            this.errors = new ArrayList<String>();
//        }
//
//    }
    public DynamicCompilingException(String message, List<String> possibleErrors, List<String> details, final String sourceCode) {
        super(message);
        this.possibleErr = possibleErrors != null ? possibleErrors : new ArrayList<String>();
        this.details = details != null ? details : new ArrayList<String>();
        this.sourceCode = sourceCode != null ? sourceCode : "";
    }

    public DynamicCompilingException(final String message, final String sourceCode) {
        this(message, null, null, sourceCode);
    }

    public List<String> getDetails() {
        return details;
    }

    public List<String> getPossibleErr() {
        return possibleErr;
    }

    public String getSourceCode() {
        return sourceCode;
    }
}
