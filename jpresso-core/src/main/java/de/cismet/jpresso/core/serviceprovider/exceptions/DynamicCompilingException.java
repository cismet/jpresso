/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * DynamicCompilingException.java
 *
 * Created on 26. September 2003, 12:43
 */
package de.cismet.jpresso.core.serviceprovider.exceptions;

import java.util.*;
import java.util.ArrayList;

/**
 * Exception die beim dynamischen Kompilieren der Assigner Klassen geworfen wird. Mit dieser Exception kann man naeheres
 * ueber die Fehlerursache herausfinden.
 *
 * <p>Teile des Codes sind nicht von mir. Aus dem OpenSource Projekt Schmortopf ;-)</p>
 *
 * @author   hell
 * @version  $Revision$, $Date$
 */
public class DynamicCompilingException extends JPressoException {

    //~ Instance fields --------------------------------------------------------

    /** Logger. */
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private List<String> details;
    private List<String> possibleErr;
    private String sourceCode;
//    private List<String> errors;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DynamicCompilingException object.
     *
     * @param  message     DOCUMENT ME!
     * @param  sourceCode  DOCUMENT ME!
     */
    public DynamicCompilingException(final String message, final String sourceCode) {
        this(message, null, null, sourceCode);
    }
    /**
     * Creates a new instance of DynamicCompilingException.
     *
     * @param  message         Komplette Fehlerausgabe des Java-Compilers
     * @param  possibleErrors  DOCUMENT ME!
     * @param  details         DOCUMENT ME!
     * @param  sourceCode      DOCUMENT ME!
     */
// public DynamicCompilingException(String message, List<String> errors) {
// super(message);
// if (errors != null) {
// this.errors = errors;
// } else {
// this.errors = new ArrayList<String>();
// }
//
// }
    public DynamicCompilingException(final String message,
            final List<String> possibleErrors,
            final List<String> details,
            final String sourceCode) {
        super(message);
        this.possibleErr = (possibleErrors != null) ? possibleErrors : new ArrayList<String>();
        this.details = (details != null) ? details : new ArrayList<String>();
        this.sourceCode = (sourceCode != null) ? sourceCode : "";
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public List<String> getDetails() {
        return details;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public List<String> getPossibleErr() {
        return possibleErr;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getSourceCode() {
        return sourceCode;
    }
}
