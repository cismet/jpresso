/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.serviceprovider.exceptions;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public class DuplicateEntryException extends Exception {

    //~ Static fields/initializers ---------------------------------------------

    public static final String TXT = "Driver aliases must be unique! Found duplicated alias:  ";

    //~ Instance fields --------------------------------------------------------

    private String duplicateEntry;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DuplicateEntryException object.
     *
     * @param  s  DOCUMENT ME!
     */
    public DuplicateEntryException(final String s) {
        super(TXT + s);
        this.duplicateEntry = s;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getDuplicateEntry() {
        return this.duplicateEntry;
    }
}
