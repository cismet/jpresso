/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * OutputInitializing.java
 *
 * Created on 30. Januar 2008, 18:27
 */
package de.cismet.jpresso.project.gui.output;

import javax.swing.JPanel;

import de.cismet.jpresso.core.serviceprovider.exceptions.DynamicCompilingException;
import de.cismet.jpresso.core.serviceprovider.exceptions.InitializingException;

/**
 * The panel that prints the initializing output (infos, errors, etc).
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public class OutputInitializing extends JPanel {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane scpDetail;
    private javax.swing.JScrollPane scpOut;
    private javax.swing.JTabbedPane tbpMain;
    private javax.swing.JTextArea txtDetail;
    private javax.swing.JTextArea txtOut;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form OutputInitializing.
     *
     * @param  iEx  DOCUMENT ME!
     */
    public OutputInitializing(final InitializingException iEx) {
        initComponents();
        setName("Init Output");
        txtOut.setForeground(java.awt.Color.RED);
        txtOut.append("\nLog -------------\n");
        txtOut.append(iEx.getInitializeLog());
        txtDetail.append(errorMessageFromThrowable(iEx));
        if (iEx.getCause() != null) {
            final StringBuilder detail = new StringBuilder("\n --- ErrorMessage :\n" + iEx.getCause().toString()
                            + "\n\n");
            if (iEx.getCause() instanceof DynamicCompilingException) {
                final DynamicCompilingException dex = (DynamicCompilingException)iEx.getCause();
                if (((dex.getDetails() != null) && (dex.getDetails().size() > 0))
                            || ((dex.getPossibleErr() != null) && (dex.getPossibleErr().size() > 0))) {
                    txtOut.append("\n\nPossible error source(s):\n");
                } else {
                    return;
                }
                if (dex.getDetails() != null) {
                    for (final String s : dex.getDetails()) {
                        detail.append(s);
                    }
                    txtDetail.setText(detail.toString());
                }
                txtDetail.append(dex.getSourceCode());
                if (dex.getPossibleErr() != null) {
                    for (final String s : dex.getPossibleErr()) {
                        txtOut.append("\n\t>> " + s);
//                }
                    }
                }
            } else {
                txtDetail.append(errorMessageFromThrowable(iEx));
            }
        } else {
            txtDetail.append(errorMessageFromThrowable(iEx));
        }
        if ((txtDetail.getText() != null) && (txtDetail.getText().length() > 0)) {
            txtOut.append("\n\n >> see log for details");
        }
    }
    /**
     * public List<String> getPossibleErrors(Iterator<Diagnostic<? extends JavaFileObject>> it) { List<String> erg = new
     * ArrayList<String>(); while (it.hasNext()) { Diagnostic d = it.next(); FileObject fo = (FileObject)
     * d.getSourceCode(); String[] errs = fo.getCharContent(true).toString().split("\n"); if (d.getKind() ==
     * Diagnostic.Kind.ERROR) { // errorLines.add(d.getLineNumber()); erg.add(errs[new
     * Long(d.getLineNumber()).intValue() - 1].trim()); } // System.out.println("code:" + d.getCode()); //
     * System.out.println("line:" + d.getLineNumber()); // System.out.println("column:" + d.getColumnNumber()); //
     * System.out.println("message:" + d.getMessage(Locale.getDefault())); // System.out.println("kind:" + d.getKind());
     * // System.out.println("position:" + d.getPosition()); // System.out.println("sposition:" + d.getStartPosition());
     * // System.out.println("eposition:" + d.getEndPosition()); // System.out.println(d); //
     * System.out.println(d.toString().toUpperCase()); // System.out.println("aaa " + d.getSourceCode().getClass()); }
     * return erg; }
     *
     * @param  out  DOCUMENT ME!
     * @param  log  DOCUMENT ME!
     */
    public OutputInitializing(final String out, final String log) {
        initComponents();
        setName("Init Output");
//        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/jpresso/project/res/testok.gif")));
//        setTitle("Log");
        txtOut.setForeground(java.awt.Color.BLUE);
        txtDetail.setForeground(java.awt.Color.BLUE);
        txtDetail.append("\nLog -------------\n");
        txtDetail.append(log);
        txtOut.append(out);
        if ((log != null) && (log.length() > 0)) {
            txtOut.append("\n\n >> see log for details");
        }
//        scpDetail.setVisible(false);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   tr  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static String errorMessageFromThrowable(final Throwable tr) {
        final StringBuffer msg = new StringBuffer("\n");
        Throwable t = tr;

        while (t != null) {
            msg.append(t.getClass().getCanonicalName()).append("\n");
            final StackTraceElement[] elements = t.getStackTrace();
            if (elements != null) {
                for (final StackTraceElement ste : elements) {
                    msg.append(ste.toString()).append("\n");
                }
            }
            if (t.getMessage() != null) {
                msg.append(t.getMessage()).append("\n\n");
            }
            t = t.getCause();
        }
        if (msg.length() > 2) {
            return msg.substring(0, msg.length() - 2);
        }
        return msg.toString();
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        tbpMain = new javax.swing.JTabbedPane();
        scpOut = new javax.swing.JScrollPane();
        txtOut = new javax.swing.JTextArea();
        scpDetail = new javax.swing.JScrollPane();
        txtDetail = new javax.swing.JTextArea();

        setLayout(new java.awt.BorderLayout());

        scpOut.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        txtOut.setBackground(new java.awt.Color(240, 240, 240));
        txtOut.setEditable(false);
        txtOut.setFont(new java.awt.Font("Dialog", 0, 11));
        txtOut.setForeground(java.awt.Color.red);
        scpOut.setViewportView(txtOut);

        tbpMain.addTab(org.openide.util.NbBundle.getMessage(
                OutputInitializing.class,
                "OutputInitializing.scpOut.TabConstraints.tabTitle_1"),
            scpOut); // NOI18N

        scpDetail.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        txtDetail.setBackground(new java.awt.Color(240, 240, 240));
        txtDetail.setEditable(false);
        txtDetail.setFont(new java.awt.Font("Dialog", 0, 11));
        txtDetail.setForeground(java.awt.Color.red);
        scpDetail.setViewportView(txtDetail);

        tbpMain.addTab(org.openide.util.NbBundle.getMessage(
                OutputInitializing.class,
                "OutputInitializing.scpDetail.TabConstraints.tabTitle_1"),
            scpDetail); // NOI18N

        add(tbpMain, java.awt.BorderLayout.CENTER);
    } // </editor-fold>//GEN-END:initComponents
}
