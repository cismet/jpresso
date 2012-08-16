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
package de.cismet.jpresso.core.kernel;

import java.util.List;

import de.cismet.jpresso.core.utils.TypeSafeCollections;

/**
 * An infinite read-only ListIterator, that always jumps to position 0 after end has been reached.
 *
 * @param    <T>
 *
 * @author   stefan
 * @version  $Revision$, $Date$
 */
public final class ListCirculator<T> {

    //~ Instance fields --------------------------------------------------------

    private int cursor;
    private final List<T> internalList;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ListCirculator object.
     *
     * @param   list  DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     * @throws  IllegalStateException     DOCUMENT ME!
     */
    public ListCirculator(final List<? extends T> list) {
        if (list == null) {
            throw new IllegalArgumentException("Null not allowed!");
        }
        if (list.size() < 1) {
            throw new IllegalStateException("Can not create ListCirculator for empty List!");
        }
        this.internalList = TypeSafeCollections.newArrayList();
        this.internalList.addAll(list);
        this.cursor = 0;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public T next() {
        if (!(++cursor < internalList.size())) {
            cursor = 0;
        }
        return internalList.get(cursor);
    }
}
