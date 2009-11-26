package de.cismet.jpresso.project.gui.editors.autocomplete;

import java.util.Vector;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ComboBoxEditor;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;

/**
 * An editable combo class that will autocomplete the user entered text to the entries
 * in the combo drop down. 
 */
public class SmartComboBox extends JComboBox implements CellEditorListener {

    private static final Pattern DIVIDE_PATTERN = Pattern.compile("[^\\p{L}\\$\\d\\.\\\"]");

    public SmartComboBox(ComboBoxModel aModel) {
        super(aModel);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void configureEditor(final ComboBoxEditor anEditor, final Object anItem) {
        if (filter != null && anItem != null) {
            //skip replace method
            filter.setLock(true);
            final JTextField tf = (JTextField) anEditor.getEditorComponent();
            int caret = tf.getCaretPosition();
            int start = tf.getSelectionStart();
            int end = tf.getSelectionEnd();
            int bu;
            if (start == end) {
                //no selection
                start = caret;
                end = caret;
                bu = start;
                hidePopup();
            } else {
                bu = start;
                start = 0;
                final Matcher m = DIVIDE_PATTERN.matcher(tf.getText());
                while (m.find() && m.end() <= bu) {
                    start = m.end();
                }
            }
            final String beg = tf.getText().substring(0, start);
            final String fin = tf.getText().substring(end);
            anEditor.setItem(beg + anItem.toString() + fin);
            tf.setSelectionStart(bu);
            tf.setSelectionEnd(start + anItem.toString().length());
            //disable skip replace method
            filter.setLock(false);
        } else {
            super.configureEditor(anEditor, anItem);
        }

    }

    public SmartComboBox(final Object[] items) {
        super(items);
//        super(new StringSelectionComboBoxModel(items));
        init();
    }

    public SmartComboBox(final Vector<?> items) {
        super(items);
        // TODO Auto-generated constructor stub
    }

    public SmartComboBox() {
        super();
        // TODO Auto-generated constructor stub
    }

    private void init() {
        setEditable(true);

        filter = SmartFilter.addCompletionMechanism(this);
//        filter = ComboCompleterFilter.addCompletionMechanism(this);
    }

    @Override
    public void setSelectedIndex(final int anIndex) {
        super.setSelectedIndex(anIndex);
    }

    public boolean isCaseSensitive() {
        return filter.isCaseSensitive();
    }

    public boolean isCorrectingCase() {
        return filter.isCorrectingCase();
    }

    public void setCaseSensitive(boolean caseSensitive) {
        filter.setCaseSensitive(caseSensitive);
    }

    public void setCorrectCase(boolean correctCase) {
        filter.setCorrectCase(correctCase);
    }
    private SmartFilter filter;

    public void editingStopped(ChangeEvent e) {
        filter.getTextField().setText("");
    }

    public void editingCanceled(ChangeEvent e) {
        
    }
    
    
}
