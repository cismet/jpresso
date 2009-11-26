/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.serviceprovider.exceptions;

/**
 *
 * @author srichter
 */
public class DuplicateEntryException extends Exception {

    public static final String TXT = "Driver aliases must be unique! Found duplicated alias:  ";
    private String duplicateEntry;

    public DuplicateEntryException(String s) {
        super(TXT + s);
        this.duplicateEntry = s;
    }

    public String getDuplicateEntry() {
        return this.duplicateEntry;
    }
}
