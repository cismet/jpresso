/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.gui.editors.autocomplete;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

/**
 *
 * @author srichter
 */
public class SmartFilter extends DocumentFilter {

    private static final Pattern DIVIDE_PATTERN = Pattern.compile("[^\\p{L}\\$\\d\\.\\\"]");
    private final JComboBox combo;
    private int startIndex;
    private String currentStringToProcess; // The text in the input field before we started last looking for matches.
    //locks the replace method during item selection operations
    private boolean lock = false;
    private boolean caseSens = false;
    private boolean corrective = true;
    private int firstSelectedIndex = -1;

    public SmartFilter(JComboBox combo) {
        this.combo = combo;
    }

    public int getCompleterListSize() {
        return combo.getModel().getSize();
    }

    public Object getCompleterObjectAt(int i) {
        return combo.getItemAt(i);
    }

    public JTextField getTextField() {
        return (JTextField) combo.getEditor().getEditorComponent();
    }

    @Override
    public void replace(final FilterBypass filterBypass, final int offset, final int length, final String string, final AttributeSet attributeSet) throws BadLocationException {
        if (!isLock()) {
//            System.out.println("test");
            super.replace(filterBypass, offset, length, string, attributeSet);
            int caret = getTextField().getCaretPosition();
            final String txt = getTextField().getText();//.substring(0, caret);

            final Matcher m = DIVIDE_PATTERN.matcher(txt);
            startIndex = 0;
            final Document doc = filterBypass.getDocument();
            int endIndex = doc.getLength();
            while (m.find()) {
//                System.out.println("find@ " + m.end());
                if (m.end() <= caret) {
                    startIndex = m.end();
//                    System.out.println("index: " + doc.getText(startIndex, 1));
                } else {
                    endIndex = m.end() - 1;
                    break;
                }
            }
//        if(m.find()) {
//            endIndex = m.endIndex();
//        }
//            System.out.println("index: " + startIndex);
            //current text into pretext
            currentStringToProcess = doc.getText(startIndex, endIndex - startIndex);
            //cbox selection startIndex -1
            firstSelectedIndex = -1;
            if (currentStringToProcess.length() < 1) {
                return;
            }
            String objString = "";
            for (int i = 0; i < getCompleterListSize(); ++i) {
                //puts next combobox item into string (whole-match)
                objString = getCompleterObjectAt(i).toString();
                if ((caseSens) ? objString.equals(currentStringToProcess) : objString.equalsIgnoreCase(currentStringToProcess)) {
                    //if match...
                    firstSelectedIndex = i;
                    if (corrective) {
                        filterBypass.replace(startIndex, currentStringToProcess.length(), objString, attributeSet);
                        getTextField().setCaretPosition(caret);
                    }
                    break;
                }

                //if this item.lengh is smaller than the entered text length..take next
                if (objString.length() <= currentStringToProcess.length()) {
                    objString = "";
                    continue;
                }
                //(Partmatch)
                String objStringStart = objString.substring(0, currentStringToProcess.length());
                if ((caseSens) ? objStringStart.equals(currentStringToProcess) : objStringStart.equalsIgnoreCase(currentStringToProcess)) {
                    final String objStringEnd = objString.substring(currentStringToProcess.length());
//                    System.out.println("objstr: " + objString);
                    if (corrective) {
//                        System.out.println("x");
                        filterBypass.replace(startIndex, currentStringToProcess.length(), objString, attributeSet);
//                        System.out.println("x");
                    } else {
                        filterBypass.insertString(currentStringToProcess.length() + startIndex, objStringEnd, attributeSet);
//                        System.out.println("a");
                    }
                    firstSelectedIndex = i;
                    //selektion in combobox setzen
                    if (firstSelectedIndex != -1) {
                        //momentanen inhalt sichern
                        final String text = getTextField().getText();
                        //dabei den filter umgehen
                        setLock(true);
                        combo.setSelectedIndex(firstSelectedIndex);
                        //gesichten inhalt wieder einsetzen
                        filterBypass.replace(0, doc.getLength(), text, attributeSet);
                        //und den filter wieder aktivieren
                        setLock(false);
                    }
                    //begin has matched...done
                    break;
                }
                objString = "";
            }
            if (objString.length() > 0) {
                //set selection
                int start = currentStringToProcess.length() + startIndex;
                int end = startIndex + objString.length();
                if (start != end) {
                    getTextField().select(start, end);
                }
            }
        } else {
            super.replace(filterBypass, offset, length, string, attributeSet);
        }
    }

    @Override
    public void insertString(final FilterBypass filterBypass, int offset, final String string,final  AttributeSet attributeSet) throws BadLocationException {
        super.insertString(filterBypass, offset, string, attributeSet);
    }

    @Override
    public void remove(final FilterBypass filterBypass, final int offset, final int length) throws BadLocationException {
        super.remove(filterBypass, offset, length);
    }

    public void setCaseSensitive(final boolean caseSensitive) {
        caseSens = caseSensitive;
    }

    public boolean isCaseSensitive() {
        return caseSens;
    }

    /**
     * Will change the user entered part of the string to match the case of the matched item.
     * 
     * e.g.
     * "europe/lONdon" would be corrected to "Europe/London"
     * 
     * This option only makes sense if case sensitive is turned off
     */
    public void setCorrectCase(final boolean correctCase) {
        corrective = correctCase;
    }

    public boolean isCorrectingCase() {
        return corrective;
    }

    /**
     * 
     * @return the startIndex of the first object in the object array that can match 
     * the user entered string (-1 if no object is currently being used as a match)
     */
    public int getLeadingSelectedIndex() {
        return firstSelectedIndex;
    }

    static public SmartFilter addCompletionMechanism(final JComboBox combo) {
        if (!(combo.getEditor().getEditorComponent() instanceof JTextField)) {
            return null;
        }
        final JTextField tf = (JTextField) combo.getEditor().getEditorComponent();
//        JEditorPane ep = new JEditorPane();
//        ep.setText(tf.getText());
        final PlainDocument pd = new PlainDocument();
//        pd.addDocumentListener(new DocumentListener() {
//            public void insertUpdate(DocumentEvent e) {
//                System.out.println("insert");
//            }
//
//            public void removeUpdate(DocumentEvent e) {
//                System.out.println("remove");
//            }
//
//            public void changedUpdate(DocumentEvent e) {
//                System.out.println("change");
//            }
//        });

        final SmartFilter filter = new SmartFilter(combo);
        pd.setDocumentFilter(filter);
        tf.setDocument(pd);

        return filter;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }
}

