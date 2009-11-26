/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 *
 * @author srichter
 */
@XStreamAlias("Mapping")
public final class Mapping {

    // <editor-fold defaultstate="collapsed" desc="Constructors">
    public Mapping(final String path, final String targetTable, final String targetField, final String enclosingChar, final String content, final boolean autoIncrement, final boolean comparing) {
        setPath(path);
        setTargetField(targetField);
        setTargetTable(targetTable);
        setEnclosingChar(enclosingChar);
        setAutoIncrement(autoIncrement);
        setComparing(comparing);
        setContent(content);
    }

    public Mapping() {
        targetField = "";
        targetTable = "";
        path = "";
        enclosingChar = "";
        content = "";
    }
// </editor-fold>
    //private String sourceConnection; oder private SourceJDBCConnection?
    //private String targetConnection; oder targetTable = connectionname.tablename?
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
    private static final String DOT = ".";
    private static final String OPEN_BRACKET = "[";
    private static final String CLOSE_BRACKET = "]";
    // <editor-fold defaultstate="collapsed" desc="Setters & Getters">

    public String getContent() {
        if (content == null) {
            content = "";
        }
        return content;
    }

    public void setContent(final String content) {
        if (content != null) {
            this.content = content;
        } else {
            this.content = "";
        }
    }

    public String getPath() {
        if (path == null) {
            path = "";
        }
        return path;
//        return path.toUpperCase();
    }

    public void setPath(final String path) {
        if (path != null) {
            this.path = path.trim();
        } else {
            this.path = "";
        }
    }

    public String getTargetTable() {
        if (targetTable == null) {
            targetTable = "";
        }
        return targetTable;
    }

    public void setTargetTable(final String targetTable) {
        if (targetTable != null) {
            this.targetTable = targetTable;
        } else {
            this.targetTable = "";
        }
    }

    public String getTargetTableWithPath() {
        //TODO maybe optimize with caching?
        final StringBuffer ret = new StringBuffer(getTargetTable());
        final String p = getPath();
        if (p.length() > 0) {
            ret.append("[").append(p).append("]");
        }
        return ret.toString();
    }

    public String getTargetField() {
        if (targetField == null) {
            targetField = "";
        }
        return targetField;
    }

    public void setTargetField(final String targetField) {
        if (targetField != null) {
            this.targetField = targetField;
        } else {
            this.targetField = "";
        }
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(final boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public boolean isComparing() {
        return comparing;
    }

    public void setComparing(final boolean comparing) {
        this.comparing = comparing;
    }

    public String getEnclosingChar() {
        if (enclosingChar == null) {
            enclosingChar = "";
        }
        return enclosingChar;
    }

    public void setEnclosingChar(String enclosingChar) {
        if (enclosingChar != null) {
            this.enclosingChar = enclosingChar;
        } else {
            this.enclosingChar = "";
        }
    }
// </editor-fold>

    public Mapping copy() {
        return new Mapping(path, targetTable, targetField, enclosingChar, content, autoIncrement, comparing);
    }

//    public final String getCompleteTargetWithPath() {
//        return (path.length() > 0) ? getCompleteTarget() + OPEN_BRACKET + path + CLOSE_BRACKET : getCompleteTarget();
//    }
    public final String getCompleteTarget() {
        return (targetTable.length() > 0 && targetField.length() > 0) ? targetTable + DOT + targetField : "";
    }
}
