/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.filetypes.sql.action;

import de.cismet.jpresso.project.filetypes.cookies.RunCookie;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;

public final class RunSQLAction extends CookieAction {

    protected void performAction(Node[] activatedNodes) {
        RunCookie runCookie = activatedNodes[0].getLookup().lookup(RunCookie.class);
        runCookie.startRun();
    }

    protected int mode() {
        return CookieAction.MODE_EXACTLY_ONE;
    }

    public String getName() {
        return NbBundle.getMessage(RunSQLAction.class, "CTL_RunSQLAction");
    }

    protected Class[] cookieClasses() {
        return new Class[]{RunCookie.class};
    }

    @Override
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() Javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE);
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
