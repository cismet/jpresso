/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.gui.editors;

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.TableModel;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoManager;

/**
 * A class that adds undo/redo support to JTextComponents.
 * 
 * @author srichter
 */
public class UndoRedoSupport {

    private UndoRedoSupport() {
    }

    /**
     * Add undo/redo support to the given JTextComponent.
     * 
     * Maps: Undo -> CONTROL-Z
     *       Redo -> CONTROL-Y
     * 
     * @param the JTextComponent to decorate with undo/redo support.
     */
    public static void decorate(final JTextComponent target) {
        final Document doc = target.getDocument();
        final UndoManager undo = new UndoManager();
        doc.addUndoableEditListener(undo);
        addCommandsToComponent(target, undo);
    }

    public static void decorate(final JTable target) {
        final TableModel mo = target.getModel();
        final UndoableTableModel umo = new UndoableTableModel(mo);
        final UndoManager undo = new UndoManager();
        umo.addUndoableEditListener(undo);
        target.setModel(umo);
        addCommandsToComponent(target, undo);
    }

    private static void addCommandsToComponent(final JComponent c, final UndoManager undo) {
        final InputMap im = c.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        final UndoAction ua = new UndoAction(undo);
        final KeyStroke controlZ = KeyStroke.getKeyStroke(KeyEvent.VK_Z, Event.CTRL_MASK);
        im.put(controlZ, "control z");
        c.getActionMap().put("control z", ua);
        final RedoAction ra = new RedoAction(undo);
        final KeyStroke controlY = KeyStroke.getKeyStroke(KeyEvent.VK_Y, Event.CTRL_MASK);
        im.put(controlY, "control y");
        c.getActionMap().put("control y", ra);
    }

    public static final void discardAllEdits(final JTextComponent c) {
        Action aa = c.getActionMap().get("control z");
        if (aa != null && aa instanceof ManagerAction) {
            ((ManagerAction) aa).discardAllEdits();
        }
        aa = c.getActionMap().get("control y");
        if (aa != null && aa instanceof ManagerAction) {
            ((ManagerAction) aa).discardAllEdits();
        }
    }

    private static abstract class ManagerAction extends AbstractAction {

        public ManagerAction(UndoManager undo) {
            if (undo == null) {
                throw new IllegalArgumentException("UndoManager can not be null for ManagerAction construction!");
            }
            this.undo = undo;
        }
        private final UndoManager undo;

        public void discardAllEdits() {
            getUndo().discardAllEdits();
        }

        public UndoManager getUndo() {
            return undo;
        }
    }

    /**
     * An Undo action on the given UndoManager.
     * @author srichter
     */
    private static class UndoAction extends ManagerAction {

        public UndoAction(UndoManager undo) {
            super(undo);
        }

        public void actionPerformed(ActionEvent e) {
            if (getUndo().canUndo()) {
                getUndo().undo();
            }
        }
    }

    /**
     * A Redo action on the given UndoManager.
     * 
     * @author srichter
     */
    private static class RedoAction extends ManagerAction {

        public RedoAction(UndoManager undo) {
            super(undo);
        }

        public void actionPerformed(ActionEvent e) {
            if (getUndo().canRedo()) {
                getUndo().redo();
            }
        }
    }
}
