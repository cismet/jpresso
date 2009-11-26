/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.actions;

import de.cismet.jpresso.project.filetypes.AntHandler;
import java.awt.Frame;
import java.io.IOException;
import javax.swing.JOptionPane;
import org.netbeans.api.project.Project;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.openide.util.actions.CookieAction;
import org.openide.windows.WindowManager;

public final class CreateDefaultPropertiesAction extends CallableSystemAction {

    private static final String ERROR = "Error";
    private static final String SUCCESS = "Success";
    private static final String MSG_ERROR = "Could not create Ant-properties!";
    private static final String MSG_SUCCESS = "Successfully created Ant-properties!";
    private static final Class[] COOKIE_CLASSES = new Class[]{Project.class};

    protected int mode() {
        return CookieAction.MODE_EXACTLY_ONE;
    }

    public String getName() {
        return NbBundle.getMessage(CreateDefaultPropertiesAction.class, "CTL_CreateDefaultPropertiesAction");
    }

    protected Class[] cookieClasses() {
        return COOKIE_CLASSES;
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

    @Override
    public void performAction() {
        final Frame mainWindow = WindowManager.getDefault().getMainWindow();
//        final Project project = activatedNodes[0].getLookup().lookup(Project.class);
        try {
            AntHandler.writeDefaultProperties();
            JOptionPane.showMessageDialog(mainWindow, MSG_SUCCESS, SUCCESS, JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(mainWindow, MSG_ERROR, ERROR + "\n" + ex, JOptionPane.ERROR_MESSAGE);
        }
    }
}

