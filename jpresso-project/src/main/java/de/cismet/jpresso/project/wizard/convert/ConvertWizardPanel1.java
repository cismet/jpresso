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

import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;

import java.awt.Component;

import javax.swing.event.ChangeListener;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class ConvertWizardPanel1 implements WizardDescriptor.Panel {

    //~ Static fields/initializers ---------------------------------------------

    public static final String FILE_INFO = "file";
    public static final String MERGE_PROPS = "merge";

    //~ Instance fields --------------------------------------------------------

    /**
     * The visual component that displays this panel. If you need to access the component from this class, just use
     * getComponent().
     */
    private Component component;

    //~ Methods ----------------------------------------------------------------

    // Get the visual component for the panel. In this template, the component
    // is kept separate. This can be more efficient: if the wizard is created
    // but never displayed, or not all panels are displayed, it is better to
    // create only those which really need to be visible.
    @Override
    public Component getComponent() {
        if (component == null) {
            component = new ConvertVisualPanel1();
        }
        return component;
    }

    @Override
    public HelpCtx getHelp() {
        // Show no Help button for this panel:
        return HelpCtx.DEFAULT_HELP;
        // If you have context help:
        // return new HelpCtx(SampleWizardPanel1.class);
    }

    @Override
    public boolean isValid() {
        // If it is always OK to press Next or Finish, then:
        return true;
        // If it depends on some condition (form filled out...), then:
        // return someCondition();
        // and when this condition changes (last form field filled in...) then:
        // fireChangeEvent();
        // and uncomment the complicated stuff below.
    }

    @Override
    public final void addChangeListener(final ChangeListener l) {
    }

    @Override
    public final void removeChangeListener(final ChangeListener l) {
    }
    /*
     * private final Set<ChangeListener> listeners = new HashSet<ChangeListener>(1); // or can use ChangeSupport in NB
     * 6.0 public final void addChangeListener(ChangeListener l) { synchronized (listeners) { listeners.add(l); } }
     * public final void removeChangeListener(ChangeListener l) { synchronized (listeners) { listeners.remove(l); } }
     * protected final void fireChangeEvent() { Iterator<ChangeListener> it; synchronized (listeners) { it = new
     * HashSet<ChangeListener>(listeners).iterator(); } ChangeEvent ev = new ChangeEvent(this); while (it.hasNext()) {
     * it.next().stateChanged(ev); } }
     */

    // You can use a settings object to keep track of state. Normally the
    // settings object will be the WizardDescriptor, so you can use
    // WizardDescriptor.getProperty & putProperty to store information entered
    // by the user.
    @Override
    public void readSettings(final Object settings) {
    }

    @Override
    public void storeSettings(final Object settings) {
        final WizardDescriptor wd = (WizardDescriptor)settings;
        final ConvertVisualPanel1 content = (ConvertVisualPanel1)component;
        wd.putProperty(FILE_INFO, content.getFile());
        wd.putProperty(MERGE_PROPS, content.getMergeProperties());
    }
}
