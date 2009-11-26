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
public final class Options_V2 {

    // <editor-fold defaultstate="collapsed" desc="Constructors">
    public Options_V2(final List<String> normalize, final List<String> escapeChars, final Properties filter) {
        this.normalize = normalize;
        this.escapeChars = escapeChars;
        this.filter = filter;
    }

    public Options_V2() {
        this.normalize = TypeSafeCollections.newArrayList();
        this.escapeChars = TypeSafeCollections.newArrayList();
        this.filter = new Properties();
    }
    // </editor-fold>
    @XStreamAlias("Normalization")
    private final List<String> normalize;
    @XStreamAlias("EscapeChars")
    private List<String> escapeChars;
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

    public void setFilter(Properties filter) {
        if (filter != null) {
            this.filter.clear();
            this.filter.putAll(filter);
        } else {
            this.filter.clear();
        }
    }// </editor-fold>

    /**
     * @return the escapeChars
     */
    public List<String> getEscapeChars() {
        return escapeChars;
    }

    /**
     * @param escapeChars the escapeChars to set
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
