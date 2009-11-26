/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.gui.editors;

import de.cismet.jpresso.project.gui.AbstractJPTopComponent;
import java.awt.Container;
import java.io.File;
import javax.swing.JPanel;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileUtil;
import org.openide.windows.TopComponent;

/**
 * A JPanel that can find its parent TopComponent (if it has one).
 * 
 * @author srichter
 */
public abstract class TopComponentFinderPanel extends JPanel {

    protected final AbstractJPTopComponent findTopComponent() {
        Container c = this;
        while (!(c instanceof AbstractJPTopComponent || c == null)) {
            c = c.getParent();
        }
        return (AbstractJPTopComponent) c;
    }
    
    /**
     * Find this panels parent TopComponent of a specific class type
     * 
     * @param clazz
     * @return
     */
    
    protected final <T extends TopComponent> T findSpecificTopComponent(Class<T> clazz) {
        Container c = this;
        while (c != null && !(clazz.isAssignableFrom(c.getClass()))) {
//        while (c != null && !(c.getClass().equals(clazz))) {
            c = c.getParent();
        }
        return clazz.cast(c);
    }
    
    /**
     * 
     * @return
     */
    protected File findProjectDirectory() {
        File projDir = null;
        AbstractJPTopComponent tc = findTopComponent();
        if (tc != null && tc.getProject() != null && tc.getProject().getProjectDirectory() != null) {
            projDir = FileUtil.toFile(tc.getProject().getProjectDirectory());
        }
        return projDir;
    }
    
    /**
     * 
     * @return
     */
    protected Project findProject() {
        Project proj = null;
        AbstractJPTopComponent tc = findTopComponent();
        if (tc != null && tc.getProject() != null && tc.getProject().getProjectDirectory() != null) {
            proj = tc.getProject();
        }
        return proj;
    }
}
