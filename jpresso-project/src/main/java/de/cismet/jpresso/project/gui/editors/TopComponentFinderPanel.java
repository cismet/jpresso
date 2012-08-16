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
package de.cismet.jpresso.project.gui.editors;

import org.netbeans.api.project.Project;

import org.openide.filesystems.FileUtil;
import org.openide.windows.TopComponent;

import java.awt.Container;

import java.io.File;

import javax.swing.JPanel;

import de.cismet.jpresso.project.gui.AbstractJPTopComponent;

/**
 * A JPanel that can find its parent TopComponent (if it has one).
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public abstract class TopComponentFinderPanel extends JPanel {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected final AbstractJPTopComponent findTopComponent() {
        Container c = this;
        while (!((c instanceof AbstractJPTopComponent) || (c == null))) {
            c = c.getParent();
        }
        return (AbstractJPTopComponent)c;
    }

    /**
     * Find this panels parent TopComponent of a specific class type.
     *
     * @param   <T>    DOCUMENT ME!
     * @param   clazz  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */

    protected final <T extends TopComponent> T findSpecificTopComponent(final Class<T> clazz) {
        Container c = this;
        while ((c != null) && !(clazz.isAssignableFrom(c.getClass()))) {
//        while (c != null && !(c.getClass().equals(clazz))) {
            c = c.getParent();
        }
        return clazz.cast(c);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected File findProjectDirectory() {
        File projDir = null;
        final AbstractJPTopComponent tc = findTopComponent();
        if ((tc != null) && (tc.getProject() != null) && (tc.getProject().getProjectDirectory() != null)) {
            projDir = FileUtil.toFile(tc.getProject().getProjectDirectory());
        }
        return projDir;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected Project findProject() {
        Project proj = null;
        final AbstractJPTopComponent tc = findTopComponent();
        if ((tc != null) && (tc.getProject() != null) && (tc.getProject().getProjectDirectory() != null)) {
            proj = tc.getProject();
        }
        return proj;
    }
}
