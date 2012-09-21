/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * WrongNameException.java
 *
 * Created on 22. September 2003, 11:02
 */
package de.cismet.jpresso.core.serviceprovider.exceptions;

import java.awt.Point;

import java.util.List;

import de.cismet.jpresso.core.utils.TypeSafeCollections;

/**
 * Wird geworfen wenn beim Initialisieren was schief geht.
 *
 * @author   hell
 * @version  $Revision$, $Date$
 */
public class InitializingException extends JPressoException {

    //~ Instance fields --------------------------------------------------------

    private String initializeLog = "";
    private List<Point> mappingErrors = TypeSafeCollections.newArrayList();
    private List<Point> referenceErrors = TypeSafeCollections.newArrayList();

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of InitializingException.
     */
    public InitializingException() {
    }

    /**
     * Creates a new instance of InitializingException.
     *
     * @param  s    Beschreibung der Exception
     * @param  log  DOCUMENT ME!
     */
    public InitializingException(final String s, final String log) {
        super(s);
        initializeLog = log;
    }

    /**
     * Creates a new InitializingException object.
     *
     * @param  s    DOCUMENT ME!
     * @param  log  DOCUMENT ME!
     * @param  e    DOCUMENT ME!
     */
    public InitializingException(final String s, final String log, final Throwable e) {
        super(s, e);
        initializeLog = log;
    }

    /**
     * Creates a new InitializingException object.
     *
     * @param  s       DOCUMENT ME!
     * @param  log     DOCUMENT ME!
     * @param  mapErr  DOCUMENT ME!
     * @param  refErr  DOCUMENT ME!
     */
    public InitializingException(final String s, final String log, final List<Point> mapErr, final List<Point> refErr) {
        super(s);
        initializeLog = log;
        if (mapErr != null) {
            mappingErrors = mapErr;
        }
        if (refErr != null) {
            referenceErrors = refErr;
        }
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Getter for property initializeLog.
     *
     * @return  Value of property initializeLog.
     */
    public java.lang.String getInitializeLog() {
        return initializeLog;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public List<Point> getMappingErrorFields() {
        return mappingErrors;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public List<Point> getReferenceErrorFields() {
        return referenceErrors;
    }
}
