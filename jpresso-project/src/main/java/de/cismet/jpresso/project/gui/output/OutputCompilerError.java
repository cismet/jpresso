/*
 * OutputCompilerError.java
 *
 * Created on 30. Januar 2008, 17:43
 */
package de.cismet.jpresso.project.gui.output;

import de.cismet.jpresso.core.serviceprovider.exceptions.DynamicCompilingException;
import javax.swing.JPanel;

/**
 *
 * @author  srichter
 */
public class OutputCompilerError extends JPanel {

    /** Creates new form OutputCompilerError */
    public OutputCompilerError(DynamicCompilingException dcEx) {
        initComponents();
        setName("Compiler Output");
        this.setOpaque(false);
//        for (int i = 0; i < dcEx.getNumberOfErrors(); ++i) {
//            txtOut.append("Fehler " + (i + 1) + " -------------------\n");
//            String[] desc = dcEx.getErrorDesc(i);
//            for (int j = 0; j < desc.length; ++j) {
//                txtOut.append("  " + desc[j] + "\n");
//            }
//            txtOut.append("\n\n");
//        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scpOut = new javax.swing.JScrollPane();
        txtOut = new javax.swing.JTextArea();

        setLayout(new java.awt.BorderLayout());

        txtOut.setBackground(new java.awt.Color(240, 240, 240));
        txtOut.setEditable(false);
        txtOut.setFont(new java.awt.Font("Dialog", 0, 11));
        txtOut.setForeground(java.awt.Color.red);
        scpOut.setViewportView(txtOut);

        add(scpOut, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane scpOut;
    private javax.swing.JTextArea txtOut;
    // End of variables declaration//GEN-END:variables
}