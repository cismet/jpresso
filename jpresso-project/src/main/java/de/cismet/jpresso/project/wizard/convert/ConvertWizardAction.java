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
package de.cismet.jpresso.project.wizard.convert;

import org.netbeans.api.project.Project;

import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.Utilities;
import org.openide.util.actions.CallableSystemAction;

import java.awt.Component;
import java.awt.Dialog;

import java.text.MessageFormat;

import javax.swing.JComponent;

import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;

import de.cismet.jpresso.project.filetypes.AntHandler;
/**
 * An example action demonstrating how the wizard could be called from within your code. You can copy-paste the code
 * below wherever you need.
 *
 * @version  $Revision$, $Date$
 */
public final class ConvertWizardAction extends CallableSystemAction {

    //~ Instance fields --------------------------------------------------------

    private WizardDescriptor.Panel<WizardDescriptor>[] panels;

    //~ Methods ----------------------------------------------------------------

    @Override
    public void performAction() {
        final WizardDescriptor wizardDescriptor = new WizardDescriptor(getPanels());
        // {0} will be replaced by WizardDesriptor.Panel.getComponent().getName()
        wizardDescriptor.setTitleFormat(new MessageFormat("{0}"));
        wizardDescriptor.setTitle("Choose old JPresso file to import");
        final Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
        dialog.setVisible(true);
        dialog.toFront();
        final boolean cancelled = wizardDescriptor.getValue() != WizardDescriptor.FINISH_OPTION;
        if (!cancelled) {
            try {
                final Project activeNode = Utilities.actionsGlobalContext().lookup(Project.class);
                // String projDir = activeNode.getProjectDirectory().getPath();
                final FileObject projFO = activeNode.getProjectDirectory();
                final String projDir = FileUtil.toFile(projFO).getAbsolutePath();
                final String oldFile = (String)wizardDescriptor.getProperty(ConvertWizardPanel1.FILE_INFO);
                final String mergeProps = (String)wizardDescriptor.getProperty(ConvertWizardPanel1.MERGE_PROPS);
//                Converter.convertOldImportFile(new File(oldFile), new File(projDir));
                AntHandler.startConvert(activeNode.getProjectDirectory().getFileObject(
                        "/"
                                + JPressoFileManager.BUILD_XML),
                    oldFile,
                    projDir,
                    mergeProps);
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    /**
     * Initialize panels representing individual wizard's steps and sets various properties for them influencing wizard
     * appearance.
     *
     * @return  DOCUMENT ME!
     */
    @SuppressWarnings("unchecked") // Type-safty can not be compromised
    private WizardDescriptor.Panel<WizardDescriptor>[] getPanels() {
        if (panels == null) {
            panels = new WizardDescriptor.Panel[] { new ConvertWizardPanel1() };
            final String[] steps = new String[panels.length];
            for (int i = 0; i < panels.length; i++) {
                final Component c = panels[i].getComponent();
                // Default step name to component name of panel. Mainly useful
                // for getting the name of the target chooser to appear in the
                // list of steps.
                steps[i] = c.getName();
                if (c instanceof JComponent) { // assume Swing components
                    final JComponent jc = (JComponent)c;
                    // Sets step number of a component
                    jc.putClientProperty("WizardPanel_contentSelectedIndex", i);
                    // Sets steps names for a panel
                    jc.putClientProperty("WizardPanel_contentData", steps);
                    // Turn on subtitle creation on each step
                    jc.putClientProperty("WizardPanel_autoWizardStyle", Boolean.TRUE);
                    // Show steps on the left side with the image on the background
                    jc.putClientProperty("WizardPanel_contentDisplayed", Boolean.TRUE);
                    // Turn on numbering of all steps
                    jc.putClientProperty("WizardPanel_contentNumbered", Boolean.TRUE);
                }
            }
        }
        return panels;
    }

    @Override
    public String getName() {
        return "Import old JPresso file";
    }

    @Override
    public String iconResource() {
        return null;
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
