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
package de.cismet.jpresso.project.gui.connection;

import org.openide.util.NbBundle;

import java.awt.BorderLayout;

import java.io.Serializable;

import java.text.DateFormat;

import java.util.Calendar;
import java.util.Locale;

import javax.swing.JPanel;
import javax.swing.text.Document;

import de.cismet.jpresso.project.filetypes.connection.ConnectionDataObject;
import de.cismet.jpresso.project.gui.AbstractJPTopComponent;
import de.cismet.jpresso.project.gui.editors.ConnectionEditor;
import de.cismet.jpresso.project.gui.output.OutputTabbedPaneFactory;

/**
 * Top component which displays something.
 *
 * @version  $Revision$, $Date$
 */
public final class ConnectionTopComponent extends AbstractJPTopComponent<ConnectionDataObject> {

    //~ Static fields/initializers ---------------------------------------------

    /** path to the icon used by the component and its open action. */
// static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
    private static final String PREFERRED_ID = "ConnectionTopComponent";

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;

    private ConnectionEditor dbe;

    /**
     * private ConnectionDataObject data;
     *
     * @param  data  DOCUMENT ME!
     */
    public ConnectionTopComponent(final ConnectionDataObject data) {
        super(data);
        initComponents();
//        setDisplayName(data.getPrimaryFile().getNameExt());
        dbe = new ConnectionEditor(data.getData());
        jPanel4.add(dbe, BorderLayout.CENTER);
        setName(NbBundle.getMessage(ConnectionTopComponent.class, "CTL_ConnectionTopComponent"));
        setToolTipText(NbBundle.getMessage(ConnectionTopComponent.class, "HINT_ConnectionTopComponent"));
//        setIcon(Utilities.loadImage(ICON_PATH, true));
        for (final Document d : dbe.getDocuments()) {
            if (d != null) {
                d.addDocumentListener(this);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane1 = OutputTabbedPaneFactory.createOutputTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        jSplitPane1.setDividerLocation(500);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel2.setLayout(new java.awt.BorderLayout());
        jPanel2.add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jSplitPane1.setBottomComponent(jPanel2);

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel4.setLayout(new java.awt.BorderLayout());
        jPanel3.add(jPanel4, java.awt.BorderLayout.CENTER);

        jSplitPane1.setLeftComponent(jPanel3);

        jPanel1.add(jSplitPane1, java.awt.BorderLayout.CENTER);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jButton1.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/jpresso/project/res/connection-established.png"))); // NOI18N
        jButton1.setToolTipText("Check Connection");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jButton1ActionPerformed(evt);
                }
            });
        jToolBar1.add(jButton1);

        jButton2.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/jpresso/project/res/search.png"))); // NOI18N
        jButton2.setToolTipText("Preview Database");
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jButton2ActionPerformed(evt);
                }
            });
        jToolBar1.add(jButton2);

        jPanel1.add(jToolBar1, java.awt.BorderLayout.NORTH);

        add(jPanel1, java.awt.BorderLayout.CENTER);
    } // </editor-fold>//GEN-END:initComponents

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jButton1ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jButton1ActionPerformed
        this.dbe.checkTargetConnection(true);
    }                                                                            //GEN-LAST:event_jButton1ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jButton2ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jButton2ActionPerformed
        this.dbe.checkTargetConnection(false);
    }                                                                            //GEN-LAST:event_jButton2ActionPerformed
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    /**
     * replaces this in object stream.
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public Object writeReplace() {
        return new ResolvableHelper();
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    @Override
    public void addOutput(final JPanel out) {
        final Calendar now = Calendar.getInstance();
        final DateFormat format = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.getDefault());
        this.jTabbedPane1.addTab("Connection Preview @ " + format.format(now.getTime()) + "    ", out);
        if (!isOpened()) {
            ConnectionTopComponent.this.open();
        }
        ConnectionTopComponent.this.requestActive();
        ConnectionTopComponent.this.requestFocusInWindow(true);

//        int xpos = jDesktopPane1.getAllFrames().length * 10;
//        int ypos = jDesktopPane1.getAllFrames().length * 10;
//        double width = (int) out.getPreferredSize().getWidth();
//        double height = (int) out.getPreferredSize().getHeight();
//        if (width + xpos > jDesktopPane1.getWidth()) {
//            width = jDesktopPane1.getWidth() * 0.9 - xpos;
//        }
//        if (height + ypos > jDesktopPane1.getHeight()) {
//            height = jDesktopPane1.getHeight() * 0.9 - ypos;
//        }
//        if (width < 10) {
//            width = (int) out.getPreferredSize().getWidth() / 2;
//        }
//        if (height < 10) {
//            height = (int) out.getPreferredSize().getHeight() / 2;
//        }
//        jDesktopPane1.add(out, javax.swing.JLayeredPane.DEFAULT_LAYER);
//        out.setBounds(xpos, ypos, (int) width, (int) height);
//        //out.pack();
//        out.setVisible(true);
//
//
////        try {out.setMaximum(true);
////        }catch (Exception e) {}
    }

    @Override
    protected void finalize() throws Throwable {
        for (final Document d : dbe.getDocuments()) {
            if (d != null) {
                d.removeDocumentListener(this);
            }
        }
        super.finalize();
    }

    @Override
    public boolean updateDataObject() {
        if ((getData() != null) && (dbe != null)) {
            getData().setData(dbe.getContent());
        } else {
            log.error("Can not save: DataObject not existent!");
            return false;
        }
        return true;
    }

    @Override
    public void componentClosed() {
        super.componentClosed();
        jTabbedPane1.removeAll();
//        if (getData() != null) {
//            getData().setTopComponent(null);
//        }
    }

    @Override
    protected void removeAllListenerOnClosed() {
        // nothing
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    static final class ResolvableHelper implements Serializable {

        //~ Static fields/initializers -----------------------------------------

        private static final long serialVersionUID = 1L;

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public Object readResolve() {
            return null; // new ConnectionTopComponent;
        }
    }
}
