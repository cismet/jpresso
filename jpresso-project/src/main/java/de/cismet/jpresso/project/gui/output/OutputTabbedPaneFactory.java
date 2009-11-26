/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.gui.output;

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
import org.openide.awt.TabbedPaneFactory;

/**
 *
 * @author srichter
 */
public final class OutputTabbedPaneFactory extends JTabbedPane {

    private OutputTabbedPaneFactory() {
        //static
    }
    
    /**
     * 
     * @return a JTabbedPane that has closeable tabs and close all/ all other
     */
    public static final JTabbedPane createOutputTabbedPane() {
        JTabbedPane instance = TabbedPaneFactory.createCloseButtonTabbedPane();
        instance.addPropertyChangeListener(TabbedPaneFactory.PROP_CLOSE, new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                JTabbedPane pane = (JTabbedPane) evt.getSource();
                pane.remove((Component)evt.getNewValue());
            }
        });
        final JPopupMenu menu = new JPopupMenu();
        menu.add(new AbstractAction("Close All") {

            public void actionPerformed(ActionEvent e) {
                ((JTabbedPane) (menu.getInvoker())).removeAll();
            }
        });
        menu.add(new AbstractAction("Close All Other") {

            public void actionPerformed(ActionEvent e) {
                JTabbedPane jtp = (JTabbedPane) (menu.getInvoker());
                Component c = jtp.getSelectedComponent();
                //jtp.removeAll();
                if (c != null) {
                    for(Component comp : jtp.getComponents()) {
                        if(comp instanceof JPanel && !comp.equals(c)) {
                            jtp.remove(comp);
                        }
                    }
                    //jtp.add(c, c.getName());
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
            public void mousePressed(MouseEvent evt) {
                if (evt.isPopupTrigger()) {
                    menu.show(evt.getComponent(), evt.getX(), evt.getY());
                } else {
                    super.mousePressed(evt);
                }
            }

            @Override
            public void mouseReleased(MouseEvent evt) {
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
