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
public final class Options {

    //~ Constructors -----------------------------------------------------------

    /**
     * <editor-fold defaultstate="collapsed" desc="Constructors">.
     *
     * @param  normalize       DOCUMENT ME!
     * @param  deleteOrphaned  DOCUMENT ME!
     * @param  filter          DOCUMENT ME!
     */
    public Options(final List<String> normalize, final List<String> deleteOrphaned, final Properties filter) {
        this.normalize = normalize;
        this.deleteOrphaned = deleteOrphaned;
        this.filter = filter;
    }

    /**
     * Creates a new Options object.
     */
    public Options() {
        this.normalize = TypeSafeCollections.newArrayList();
        this.deleteOrphaned = TypeSafeCollections.newArrayList();
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
     * @return  DOCUMENT ME!
     */
    public List<String> getDeleteOrphaned() {
        return deleteOrphaned;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  deleteOrhphaned  DOCUMENT ME!
     */
    public void setDeleteOrphaned(final List<String> deleteOrhphaned) {
        if (deleteOrhphaned != null) {
            this.deleteOrphaned.clear();
            this.deleteOrphaned.addAll(deleteOrhphaned);
        } else {
            this.deleteOrphaned.clear();
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
    @XStreamAlias("DeleteOrphaned")
    private final List<String> deleteOrphaned;
    @XStreamAlias("Filter")
    private final Properties filter;
}
