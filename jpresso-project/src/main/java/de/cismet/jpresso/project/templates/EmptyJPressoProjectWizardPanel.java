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
package de.cismet.jpresso.project.templates;

import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;

import java.awt.Component;

import java.util.HashSet;
import java.util.Set;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.cismet.jpresso.core.utils.TypeSafeCollections;

/**
 * Panel just asking for basic info.
 *
 * @version  $Revision$, $Date$
 */
public class EmptyJPressoProjectWizardPanel implements WizardDescriptor.Panel,
    WizardDescriptor.ValidatingPanel,
    WizardDescriptor.FinishablePanel {

    //~ Instance fields --------------------------------------------------------

    private WizardDescriptor wizardDescriptor;
    private EmptyJPressoProjectPanelVisual component;
    private final Set<ChangeListener> listeners = TypeSafeCollections.newHashSet(1);    // or can use ChangeSupport in
                                                                                        // NB 6.0

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new EmptyJPressoProjectWizardPanel object.
     */
    public EmptyJPressoProjectWizardPanel() {
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Component getComponent() {
        if (component == null) {
            component = new EmptyJPressoProjectPanelVisual(this);
            component.setName(NbBundle.getMessage(EmptyJPressoProjectWizardPanel.class, "LBL_CreateProjectStep"));
        }
        return component;
    }

    @Override
    public HelpCtx getHelp() {
        return new HelpCtx(EmptyJPressoProjectWizardPanel.class);
    }

    @Override
    public boolean isValid() {
        getComponent();
        return component.valid(wizardDescriptor);
    }

    @Override
    public final void addChangeListener(final ChangeListener l) {
        synchronized (listeners) {
            listeners.add(l);
        }
    }

    @Override
    public final void removeChangeListener(final ChangeListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }

    /**
     * DOCUMENT ME!
     */
    protected final void fireChangeEvent() {
        final Set<ChangeListener> ls;
        synchronized (listeners) {
            ls = TypeSafeCollections.newHashSet(listeners);
        }
        final ChangeEvent ev = new ChangeEvent(this);
        for (final ChangeListener l : ls) {
            l.stateChanged(ev);
        }
    }

    @Override
    public void readSettings(final Object settings) {
        wizardDescriptor = (WizardDescriptor)settings;
        component.read(wizardDescriptor);
    }

    @Override
    public void storeSettings(final Object settings) {
        final WizardDescriptor d = (WizardDescriptor)settings;
        component.store(d);
    }

    @Override
    public boolean isFinishPanel() {
        return true;
    }

    @Override
    public void validate() throws WizardValidationException {
        getComponent();
        component.validate(wizardDescriptor);
    }
}
