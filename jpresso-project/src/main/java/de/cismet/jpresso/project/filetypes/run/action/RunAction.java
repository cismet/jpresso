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
package de.cismet.jpresso.project.filetypes.run.action;

import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;

import de.cismet.jpresso.project.filetypes.cookies.RunCookie;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public final class RunAction extends CookieAction {

    //~ Methods ----------------------------------------------------------------

    @Override
    protected void performAction(final Node[] activatedNodes) {
        final RunCookie runCookie = activatedNodes[0].getLookup().lookup(RunCookie.class);
        runCookie.startRun();
        // TODO use runCookie
    }

    @Override
    protected int mode() {
        return CookieAction.MODE_EXACTLY_ONE;
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(RunAction.class, "CTL_RunAction");
    }

    @Override
    protected Class[] cookieClasses() {
        return new Class[] { RunCookie.class };
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
