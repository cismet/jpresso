/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.gui.options;

import de.cismet.jpresso.project.serviceprovider.ClassResourceListener;

/**
 *
 * @author srichter
 */
public interface ProjectOptionsEditorListener extends ClassResourceListener {

    public void otherOptionsChanged();
}
