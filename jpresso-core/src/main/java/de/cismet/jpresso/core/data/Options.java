/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import de.cismet.jpresso.core.utils.TypeSafeCollections;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author srichter
 */
public final class Options {

    // <editor-fold defaultstate="collapsed" desc="Constructors">
    public Options(final List<String> normalize, final List<String> deleteOrphaned, final Properties filter) {
        this.normalize = normalize;
        this.deleteOrphaned = deleteOrphaned;
        this.filter = filter;
    }

    public Options() {
        this.normalize = TypeSafeCollections.newArrayList();
        this.deleteOrphaned = TypeSafeCollections.newArrayList();
        this.filter = new Properties();
    }
    // </editor-fold>
    @XStreamAlias("Normalization")
    private final List<String> normalize;
    @XStreamAlias("DeleteOrphaned")
    private final List<String> deleteOrphaned;
    @XStreamAlias("Filter")
    private final Properties filter;

    // <editor-fold defaultstate="collapsed" desc="Setters & Getters">
    public List<String> getNormalize() {
        return normalize;
    }

    public Properties getFilter() {
        return filter;
    }

    public void setNormalize(List<String> norm) {
        if (norm != null) {
            this.normalize.clear();
            this.normalize.addAll(norm);
        } else {
            this.normalize.clear();
        }
    }
    public List<String> getDeleteOrphaned() {
        return deleteOrphaned;
    }

    public void setDeleteOrphaned(List<String> deleteOrhphaned) {
        if (deleteOrhphaned != null) {
            this.deleteOrphaned.clear();
            this.deleteOrphaned.addAll(deleteOrhphaned);
        } else {
            this.deleteOrphaned.clear();
        }
    }

    public void setFilter(Properties filter) {
        if (filter != null) {
            this.filter.clear();
            this.filter.putAll(filter);
        } else {
            this.filter.clear();
        }
    }// </editor-fold>
}
