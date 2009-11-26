/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.kernel;

import de.cismet.jpresso.core.utils.TypeSafeCollections;
import java.util.List;

/**
 * An infinite read-only ListIterator, that always jumps to position 0 after end has been reached
 * @param <T>
 *
 * @author stefan
 */
public final class ListCirculator<T> {

    public ListCirculator(List<? extends T> list) {
        if (list == null) {
            throw new IllegalArgumentException("Null not allowed!");
        }
        if (list.size() < 1) {
            throw new IllegalStateException("Can not create ListCirculator for empty List!");
        }
        this.internalList = TypeSafeCollections.newArrayList();
        this.internalList.addAll(list);
        this.cursor = 0;
    }
    private int cursor;
    private final List<T> internalList;

    public final T next() {
        if (!(++cursor < internalList.size())) {
            cursor = 0;
        }
        return internalList.get(cursor);
    }
}
