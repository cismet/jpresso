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
package de.cismet.jpresso.core.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.List;
import java.util.Properties;

import de.cismet.jpresso.core.utils.TypeSafeCollections;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public final class Options_V2 {

    //~ Constructors -----------------------------------------------------------

    /**
     * <editor-fold defaultstate="collapsed" desc="Constructors">.
     *
     * @param  normalize    DOCUMENT ME!
     * @param  escapeChars  DOCUMENT ME!
     * @param  filter       DOCUMENT ME!
     */
    public Options_V2(final List<String> normalize, final List<String> escapeChars, final Properties filter) {
        this.normalize = normalize;
        this.escapeChars = escapeChars;
        this.filter = filter;
    }

    /**
     * Creates a new Options_V2 object.
     */
    public Options_V2() {
        this.normalize = TypeSafeCollections.newArrayList();
        this.escapeChars = TypeSafeCollections.newArrayList();
        this.filter = new Properties();
    }
    // </editor-fold>

    /**
     * <editor-fold defaultstate="collapsed" desc="Setters & Getters">.
     *
     * @return  DOCUMENT ME!
     */
    public List<String> getNormalize() {
        return normalize;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Properties getFilter() {
        return filter;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  norm  DOCUMENT ME!
     */
    public void setNormalize(final List<String> norm) {
        if (norm != null) {
            this.normalize.clear();
            this.normalize.addAll(norm);
        } else {
            this.normalize.clear();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  filter  DOCUMENT ME!
     */
    public void setFilter(final Properties filter) {
        if (filter != null) {
            this.filter.clear();
            this.filter.putAll(filter);
        } else {
            this.filter.clear();
        }
    } // </editor-fold>

    //~ Instance fields --------------------------------------------------------

    @XStreamAlias("Normalization")
    private final List<String> normalize;
    @XStreamAlias("EscapeChars")
    private List<String> escapeChars;
    @XStreamAlias("Filter")
    private final Properties filter;

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  the escapeChars
     */
    public List<String> getEscapeChars() {
        return escapeChars;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  escapeChars  the escapeChars to set
     */
    public void setEscapeChars(final List<String> escapeChars) {
        if (escapeChars != null) {
            this.escapeChars.clear();
            this.escapeChars.addAll(escapeChars);
        } else {
            this.escapeChars.clear();
        }
    }
}
