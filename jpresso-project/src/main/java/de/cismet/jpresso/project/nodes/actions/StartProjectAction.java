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
package de.cismet.jpresso.project.nodes.actions;

import org.netbeans.api.project.Project;

import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;

import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;

import de.cismet.jpresso.project.filetypes.AntHandler;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public final class StartProjectAction extends CookieAction {

    //~ Static fields/initializers ---------------------------------------------

    private static final Class[] COOKIE_CLASSES = new Class[] { Project.class };

    //~ Methods ----------------------------------------------------------------

    @Override
    protected void performAction(final Node[] activatedNodes) {
        final Project project = activatedNodes[0].getLookup().lookup(Project.class);
        AntHandler.startProject(project.getProjectDirectory().getFileObject("/" + JPressoFileManager.BUILD_XML));
    }

    @Override
    protected int mode() {
        return CookieAction.MODE_EXACTLY_ONE;
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(StartProjectAction.class, "CTL_StartProjectAction");
    }

    @Override
    protected Class[] cookieClasses() {
        return COOKIE_CLASSES;
    }

    @Override
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() Javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE);
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
