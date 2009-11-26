/*
 * UniversalContainer.java
 *
 * Created on 5. Mai 2004, 10:47
 */
package de.cismet.jpresso.core.kernel;

public class UniversalContainer<T> {

    T o;

    /** Creates a new instance of UniversalContainer */
    public UniversalContainer() {
        o = null;

    }

    /**
     * Getter for property o.
     * @return Value of property o.
     */
    public T getObject() {
        return o;
    }

    /**
     * Setter for property o.
     * @param o New value of property o.
     */
    public void setObject(T o) {
        this.o = o;
    }
}
