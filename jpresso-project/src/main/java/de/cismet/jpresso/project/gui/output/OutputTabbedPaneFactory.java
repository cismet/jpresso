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
package de.cismet.jpresso.project.gui.output;

import org.openide.awt.TabbedPaneFactory;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public final class OutputTabbedPaneFactory extends JTabbedPane {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new OutputTabbedPaneFactory object.
     */
    private OutputTabbedPaneFactory() {
        // static
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  a JTabbedPane that has closeable tabs and close all/ all other
     */
    public static JTabbedPane createOutputTabbedPane() {
        final JTabbedPane instance = TabbedPaneFactory.createCloseButtonTabbedPane();
        instance.addPropertyChangeListener(TabbedPaneFactory.PROP_CLOSE, new PropertyChangeListener() {

                @Override
                public void propertyChange(final PropertyChangeEvent evt) {
                    final JTabbedPane pane = (JTabbedPane)evt.getSource();
                    pane.remove((Component)evt.getNewValue());
                }
            });

        final JPopupMenu menu = new JPopupMenu();
        menu.add(new AbstractAction("Close All") {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    ((JTabbedPane)(menu.getInvoker())).removeAll();
                }
            });
        menu.add(new AbstractAction("Close All Other") {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    final JTabbedPane jtp = (JTabbedPane)(menu.getInvoker());
                    final Component c = jtp.getSelectedComponent();
                    // jtp.removeAll();
                    if (c != null) {
                        for (final Component comp : jtp.getComponents()) {
                            if ((comp instanceof JPanel) && !comp.equals(c)) {
                                jtp.remove(comp);
                            }
                        }
                        // jtp.add(c, c.getName());
                    }
                }
            });

//        menu.add(new AbstractAction("Open in New Window") {
//
//            public void actionPerformed(ActionEvent e) {
//                JTabbedPane jtp = (JTabbedPane) (menu.getInvoker());
//                Component c = jtp.getSelectedComponent();
//                if (c != null) {
//                    TopComponent frame = new TopComponent();
//                    frame.setLayout(new BorderLayout());
//                    frame.add(c, BorderLayout.CENTER);
//                    frame.requestVisible();
//                    frame.requestActive();
//                }
//            }
//        });

        instance.addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(final MouseEvent evt) {
                    if (evt.isPopupTrigger()) {
                        menu.show(evt.getComponent(), evt.getX(), evt.getY());
                    } else {
                        super.mousePressed(evt);
                    }
                }

                @Override
                public void mouseReleased(final MouseEvent evt) {
                    if (evt.isPopupTrigger()) {
                        menu.show(evt.getComponent(), evt.getX(), evt.getY());
                    } else {
                        super.mouseReleased(evt);
                    }
                }
            });
        return instance;
    }
}
