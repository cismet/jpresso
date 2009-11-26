/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.gui.editors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JTable;

/**
 * Not yet finished....
 * @author srichter
 */
public class FindReplace {

    public void replaceAll(final JTable tab, final String pattern, final String replacement, final int col) {
        if (tab == null || replacement == null || col < 0 || col > tab.getColumnCount()) {
            return;
        }
        final Pattern regPat = Pattern.compile(pattern);
        String output;
        for (int i = 0; i < tab.getRowCount(); ++i) {
            final Object o = tab.getValueAt(i, col);
            if (o instanceof String) {
                final Matcher matcher = regPat.matcher((String) o);
                output = matcher.replaceAll(replacement);
                tab.setValueAt(output, i, col);
            }
        }
    }
}
