/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * NormalizeEditor.java
 *
 * Created on 17. November 2003, 14:19
 */
package de.cismet.jpresso.project.gui.editors;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import de.cismet.jpresso.core.data.Options;
import de.cismet.jpresso.core.data.RuntimeProperties;
import de.cismet.jpresso.core.serviceprovider.AlphanumComparator;
import de.cismet.jpresso.core.utils.TypeSafeCollections;

import de.cismet.jpresso.project.gui.run.RunTopComponent;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public final class OptionsEditor extends javax.swing.JPanel {

    //~ Instance fields --------------------------------------------------------

    public final String[] finalizerClassnames;

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private final RunTopComponent topComponent;
    private final Map<String, JCheckBox> normalizeOptions;
    private final Map<String, JTextField> filterOptions;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdFinalizeClass;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblFinalizeClass;
    private javax.swing.JLabel lblFinalizeParam;
    private javax.swing.JPanel panFilter;
    private javax.swing.JPanel panFilterTitle;
    private javax.swing.JPanel panNormalize;
    private javax.swing.JPanel panNormalizeTitle;
    private javax.swing.JPanel panOptions;
    private javax.swing.JTextField txtFinalizeClass;
    private javax.swing.JTextArea txtFinalizeParam;
    // End of variables declaration//GEN-END:variables
//    public static void main (String[] args) throws Exception{
//        FileReader r=new FileReader("C:\\importTest.xml");
//        // aus dem XML File die entsprechende Datenstruktur machen (CASTOR)
//        ImportRules impRules=ImportRules.unmarshal(r);
//        Options o=impRules.getOptions();
//        javax.swing.JFrame frame = new javax.swing.JFrame("SwingApplication");
//
//        String[] lala=new String[]{"Url","Url_Base","COORDINATE","Altablagerungen","TEST"};
//        OptionsEditor t=new OptionsEditor();
//        t.setTables(lala);
//        t.setContent(o,null);
//
//        frame.getContentPane().add(t);
//
//        frame.addWindowListener(new java.awt.event.WindowAdapter() {
//            @Override
//            public void windowClosing(java.awt.event.WindowEvent evt) {
//                System.exit(0);
//            }
//        });
//
//
//
//        //Finish setting up the frame, and show it.
//        frame.pack();
//        frame.setVisible(true);
//    }

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form NormalizeEditor.
     *
     * @param  topComponent  DOCUMENT ME!
     */
    public OptionsEditor(final RunTopComponent topComponent) {
        this.topComponent = topComponent;
        initComponents();
        txtFinalizeClass.getDocument().addDocumentListener(topComponent);
        txtFinalizeParam.getDocument().addDocumentListener(topComponent);
        normalizeOptions = TypeSafeCollections.newLinkedHashMap();
        filterOptions = TypeSafeCollections.newLinkedHashMap();
        final ResourceBundle res = ResourceBundle.getBundle(
                "de.cismet.jpresso.core.finalizer.finalizer",
                Locale.getDefault(),
                Thread.currentThread().getContextClassLoader());
        final List<String> finalizerList = TypeSafeCollections.newArrayList();
        final List<String> keyList = TypeSafeCollections.newArrayList(res.keySet());
        Collections.sort(keyList);
        for (final String key : keyList) {
            if (key.toLowerCase().matches("^finalizer[0-9]+")) {
                finalizerList.add(res.getString(key));
            }
        }
        finalizerClassnames = finalizerList.toArray(new String[finalizerList.size()]);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  withoutPath  DOCUMENT ME!
     * @param  withPath     DOCUMENT ME!
     */
    public void setTables(String[] withoutPath, final String[] withPath) {
        Arrays.sort(withPath, AlphanumComparator.getInstance());
        Arrays.sort(withoutPath, AlphanumComparator.getInstance());
        if (withoutPath == null) {
            withoutPath = new String[0];
        }
        normalizeOptions.clear();
        filterOptions.clear();
        panNormalize.removeAll();
        panFilter.removeAll();
        java.awt.GridBagConstraints normalizeGridBagConstraints;
        java.awt.GridBagConstraints filterGridBagConstraints;
        java.awt.GridBagConstraints labelGridBagConstraints;
        for (int i = 0; i < withoutPath.length; ++i) {
            final JCheckBox c = new JCheckBox();
            c.setText(withoutPath[i]);
            normalizeGridBagConstraints = new java.awt.GridBagConstraints();
            // normalizeGridBagConstraints.insets
            normalizeGridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
            normalizeGridBagConstraints.gridx = 0;
            normalizeGridBagConstraints.gridy = i;
            panNormalize.add(c, normalizeGridBagConstraints);
            if (c != null) {
                c.addItemListener(topComponent);
            }
            normalizeOptions.put(c.getText(), c);
        }

        for (int i = 0; i < withPath.length; ++i) {
            final String tabName = withPath[i];
            final JLabel l = new JLabel(tabName);
            l.setMinimumSize(l.getPreferredSize());
            final JTextField t = new JTextField();
            final Dimension d = t.getPreferredSize();
            final Dimension mind = new Dimension(800, d.height);
            t.setMinimumSize(mind);
            t.setPreferredSize(mind);
//            t.setPreferredSize(new Dimension(50, 50));
//            t.setMinimumSize(new Dimension(50, 50));
//            t.setName(withPath[i]);
            labelGridBagConstraints = new java.awt.GridBagConstraints();
            filterGridBagConstraints = new java.awt.GridBagConstraints();
            labelGridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
            labelGridBagConstraints.gridx = 0;
            labelGridBagConstraints.gridy = i;
            labelGridBagConstraints.insets = new Insets(2, 2, 2, 2);
//            labelGridBagConstraints.weightx = 1.0;

            labelGridBagConstraints.fill = GridBagConstraints.BOTH;

            panFilter.add(l, labelGridBagConstraints);
            // --------
            filterGridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
            filterGridBagConstraints.gridx = 1;
            filterGridBagConstraints.gridy = i;
            filterGridBagConstraints.insets = new Insets(2, 2, 2, 2);
            filterGridBagConstraints.fill = GridBagConstraints.BOTH;
//            filterGridBagConstraints.weighty = 0.0;

//            filterGridBagConstraints.gridheight = 50;
//            filterGridBagConstraints.gridwidth = 50;
            panFilter.add(t, filterGridBagConstraints);
            if (t != null) {
                t.getDocument().addDocumentListener(topComponent);
            }
            filterOptions.put(tabName, t);
            UndoRedoSupport.decorate(t);
        }
        revalidate();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  options  DOCUMENT ME!
     */
    public void setContent(final Options options) {
        if (options != null) {
            for (final String name : options.getNormalize()) {
                final String tmp = name;
//                String tmp = name.toUpperCase();
                if (normalizeOptions.containsKey(tmp)) {
                    normalizeOptions.get(tmp).setSelected(true);
                } else {
                    log.error("Table not in Options catalog but in XML-file");
                }
            }
            for (final String name : options.getNormalize()) {
                final String tmp = name;
//                String tmp = name.toUpperCase();
                if (normalizeOptions.containsKey(tmp)) {
                    normalizeOptions.get(tmp).setSelected(true);
                } else {
                    log.error("Table not in Options catalog but in XML-file");
                }
            }
            for (final String name : options.getFilter().stringPropertyNames()) {
                final String tmp = name;
//                String tmp = name.toUpperCase();
                if (filterOptions.containsKey(tmp)) {
                    filterOptions.get(tmp).setText(options.getFilter().getProperty(tmp));
                } else {
                    log.error("Table " + tmp + " not in Options catalog but in XML-file");
                }
            }
        } else {
            for (final JCheckBox box : normalizeOptions.values()) {
                box.setSelected(false);
            }
            for (final JTextField field : filterOptions.values()) {
                field.setText("");
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void clearUndoRedo() {
        UndoRedoSupport.discardAllEdits(txtFinalizeClass);
        UndoRedoSupport.discardAllEdits(txtFinalizeParam);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  runtimeProps  DOCUMENT ME!
     */
    public void setContent(final RuntimeProperties runtimeProps) {
        if (runtimeProps != null) {
            if (runtimeProps.getFinalizerClass() != null) {
                if (log.isDebugEnabled()) {
                    log.debug("Setting runtime properties.");
                }
                this.txtFinalizeClass.setText(runtimeProps.getFinalizerClass());
                final Properties props = runtimeProps.getFinalizerProperties();
                this.txtFinalizeParam.setText("");
                for (final Object key : props.keySet()) {
                    final String keyString = (String)key;
                    this.txtFinalizeParam.append(keyString + "=" + props.getProperty(keyString) + "\n");
                }
                this.txtFinalizeParam.setText(txtFinalizeParam.getText().trim());
                txtFinalizeParam.setCaretPosition(0);
            }
        } else {
            this.txtFinalizeClass.setText("");
            this.txtFinalizeParam.setText("");
        }
        clearUndoRedo();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  options       DOCUMENT ME!
     * @param  runtimeProps  DOCUMENT ME!
     */
    public void setContent(final Options options, final RuntimeProperties runtimeProps) {
        setContent(options);
        setContent(runtimeProps);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Options getOptionsContent() {
        final Options o = new Options();
        Iterator<String> it = normalizeOptions.keySet().iterator();
        while (it.hasNext()) {
            final JCheckBox c = normalizeOptions.get(it.next());
            if (c.isSelected()) {
                o.getNormalize().add(c.getText());
            }
        }
        it = filterOptions.keySet().iterator();
        while (it.hasNext()) {
            final String tab = it.next();
            final JTextField c = filterOptions.get(tab);
            final String filter = c.getText();
            if (filter.length() > 0) {
                o.getFilter().put(tab, filter);
            }
        }
        return o;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public RuntimeProperties getRuntimePropsContent() {
        final RuntimeProperties p = new RuntimeProperties();

        p.setFinalizerClass(this.txtFinalizeClass.getText());

        final String tParam = this.txtFinalizeParam.getText().trim();
        final String[] tp = tParam.split("\n");
        for (int i = 0; i < tp.length; ++i) {
            final String[] keyValue = tp[i].split("=");
            if (keyValue.length != 2) {
                continue;
            }
            p.getFinalizerProperties().setProperty(keyValue[0], keyValue[1]);
        }
        return p;
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panNormalizeTitle = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        panNormalize = new javax.swing.JPanel();
        panOptions = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        lblFinalizeParam = new javax.swing.JLabel();
        lblFinalizeClass = new javax.swing.JLabel();
        txtFinalizeClass = new javax.swing.JTextField();
        cmdFinalizeClass = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtFinalizeParam = new javax.swing.JTextArea();
        panFilterTitle = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        panFilter = new javax.swing.JPanel();

        setLayout(new java.awt.GridBagLayout());

        panNormalizeTitle.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createTitledBorder(
                    null,
                    "Normalize",
                    javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                    javax.swing.border.TitledBorder.DEFAULT_POSITION,
                    new java.awt.Font("Dialog", 0, 12),
                    javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground")),
                javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))); // NOI18N
        panNormalizeTitle.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setBorder(null);

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        panNormalize.setLayout(new java.awt.GridBagLayout());
        jPanel2.add(panNormalize);

        jScrollPane1.setViewportView(jPanel2);

        panNormalizeTitle.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.15;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(panNormalizeTitle, gridBagConstraints);

        panOptions.setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createTitledBorder(
                    null,
                    "Finalizer",
                    javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                    javax.swing.border.TitledBorder.DEFAULT_POSITION,
                    new java.awt.Font("Dialog", 0, 12),
                    javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground")),
                javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))); // NOI18N
        jPanel1.setLayout(new java.awt.GridBagLayout());

        lblFinalizeParam.setText("Parameter");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(lblFinalizeParam, gridBagConstraints);

        lblFinalizeClass.setText("Class");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(lblFinalizeClass, gridBagConstraints);

        UndoRedoSupport.decorate(txtFinalizeClass);
        txtFinalizeClass.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(txtFinalizeClass, gridBagConstraints);

        cmdFinalizeClass.setText("...");
        cmdFinalizeClass.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    cmdFinalizeClassActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(cmdFinalizeClass, gridBagConstraints);

        UndoRedoSupport.decorate(txtFinalizeParam);
        txtFinalizeParam.setRows(5);
        txtFinalizeParam.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jScrollPane3.setViewportView(txtFinalizeParam);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jScrollPane3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panOptions.add(jPanel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 4, 4);
        add(panOptions, gridBagConstraints);

        panFilterTitle.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createTitledBorder(
                    null,
                    "Filter",
                    javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                    javax.swing.border.TitledBorder.DEFAULT_POSITION,
                    new java.awt.Font("Dialog", 0, 12),
                    javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground")),
                javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))); // NOI18N
        panFilterTitle.setMinimumSize(new java.awt.Dimension(42, 100));
        panFilterTitle.setPreferredSize(new java.awt.Dimension(42, 100));
        panFilterTitle.setLayout(new java.awt.BorderLayout());

        jScrollPane2.setBorder(null);
        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        panFilter.setLayout(new java.awt.GridBagLayout());
        jPanel3.add(panFilter);

        jScrollPane2.setViewportView(jPanel3);

        panFilterTitle.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.weighty = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(panFilterTitle, gridBagConstraints);
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void cmdFinalizeClassActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_cmdFinalizeClassActionPerformed
        final Object res = JOptionPane.showInputDialog(
                this,
                "Please select a Finalizer:",
                "Finalizer",
                JOptionPane.PLAIN_MESSAGE,
                null,
                finalizerClassnames,
                finalizerClassnames[0]);
        if (res != null) {
            txtFinalizeClass.setText(res.toString());
        }
    }                                                                                    //GEN-LAST:event_cmdFinalizeClassActionPerformed
}
