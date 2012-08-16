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
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
@XStreamAlias("Mapping")
public final class Mapping {

    //~ Constructors -----------------------------------------------------------

    /**
     * <editor-fold defaultstate="collapsed" desc="Constructors">.
     *
     * @param  path           DOCUMENT ME!
     * @param  targetTable    DOCUMENT ME!
     * @param  targetField    DOCUMENT ME!
     * @param  enclosingChar  DOCUMENT ME!
     * @param  content        DOCUMENT ME!
     * @param  autoIncrement  DOCUMENT ME!
     * @param  comparing      DOCUMENT ME!
     */
    public Mapping(final String path,
            final String targetTable,
            final String targetField,
            final String enclosingChar,
            final String content,
            final boolean autoIncrement,
            final boolean comparing) {
        setPath(path);
        setTargetField(targetField);
        setTargetTable(targetTable);
        setEnclosingChar(enclosingChar);
        setAutoIncrement(autoIncrement);
        setComparing(comparing);
        setContent(content);
    }

    /**
     * Creates a new Mapping object.
     */
    public Mapping() {
        targetField = "";
        targetTable = "";
        path = "";
        enclosingChar = "";
        content = "";
    }
// </editor-fold>
    private static final String DOT = ".";
    private static final String OPEN_BRACKET = "[";
    private static final String CLOSE_BRACKET = "]";
    // <editor-fold defaultstate="collapsed" desc="Setters & Getters">

    //~ Instance fields --------------------------------------------------------

    // private String sourceConnection; oder private SourceJDBCConnection?
    // private String targetConnection; oder targetTable = connectionname.tablename?
    @XStreamAsAttribute
    private String path;
    @XStreamAsAttribute
    private String targetTable;
    @XStreamAsAttribute
    private String targetField;
    @XStreamAsAttribute
    private boolean autoIncrement = false;
    @XStreamAsAttribute
    private boolean comparing = false;
    @XStreamAsAttribute
    private String enclosingChar;
    @XStreamAlias("Content")
    private String content;

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getContent() {
        if (content == null) {
            content = "";
        }
        return content;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  content  DOCUMENT ME!
     */
    public void setContent(final String content) {
        if (content != null) {
            this.content = content;
        } else {
            this.content = "";
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getPath() {
        if (path == null) {
            path = "";
        }
        return path;
//        return path.toUpperCase();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  path  DOCUMENT ME!
     */
    public void setPath(final String path) {
        if (path != null) {
            this.path = path.trim();
        } else {
            this.path = "";
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getTargetTable() {
        if (targetTable == null) {
            targetTable = "";
        }
        return targetTable;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  targetTable  DOCUMENT ME!
     */
    public void setTargetTable(final String targetTable) {
        if (targetTable != null) {
            this.targetTable = targetTable;
        } else {
            this.targetTable = "";
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getTargetTableWithPath() {
        // TODO maybe optimize with caching?
        final StringBuffer ret = new StringBuffer(getTargetTable());
        final String p = getPath();
        if (p.length() > 0) {
            ret.append("[").append(p).append("]");
        }
        return ret.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getTargetField() {
        if (targetField == null) {
            targetField = "";
        }
        return targetField;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  targetField  DOCUMENT ME!
     */
    public void setTargetField(final String targetField) {
        if (targetField != null) {
            this.targetField = targetField;
        } else {
            this.targetField = "";
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  autoIncrement  DOCUMENT ME!
     */
    public void setAutoIncrement(final boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isComparing() {
        return comparing;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  comparing  DOCUMENT ME!
     */
    public void setComparing(final boolean comparing) {
        this.comparing = comparing;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getEnclosingChar() {
        if (enclosingChar == null) {
            enclosingChar = "";
        }
        return enclosingChar;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  enclosingChar  DOCUMENT ME!
     */
    public void setEnclosingChar(final String enclosingChar) {
        if (enclosingChar != null) {
            this.enclosingChar = enclosingChar;
        } else {
            this.enclosingChar = "";
        }
    }
// </editor-fold>

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Mapping copy() {
        return new Mapping(path, targetTable, targetField, enclosingChar, content, autoIncrement, comparing);
    }
    /**
     * public final String getCompleteTargetWithPath() { return (path.length() > 0) ? getCompleteTarget() + OPEN_BRACKET
     * + path + CLOSE_BRACKET : getCompleteTarget(); }
     *
     * @return  DOCUMENT ME!
     */
    public String getCompleteTarget() {
        return ((targetTable.length() > 0) && (targetField.length() > 0)) ? (targetTable + DOT + targetField) : "";
    }
}
