/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * UniversalContainer.java
 *
 * Created on 5. Mai 2004, 10:47
 */
package de.cismet.jpresso.core.kernel;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class UniversalContainer<T> {

    //~ Instance fields --------------------------------------------------------

    T o;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of UniversalContainer.
     */
    public UniversalContainer() {
        o = null;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Getter for property o.
     *
     * @return  Value of property o.
     */
    public T getObject() {
        return o;
    }

    /**
     * Setter for property o.
     *
     * @param  o  New value of property o.
     */
    public void setObject(final T o) {
        this.o = o;
    }
}
