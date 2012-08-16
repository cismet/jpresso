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
package de.cismet.jpresso.core.kernel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.cismet.jpresso.core.data.Reference;
import de.cismet.jpresso.core.serviceprovider.AlphanumComparator;
import de.cismet.jpresso.core.utils.TypeSafeCollections;

/**
 * Die Klasse erzeugt eine topologische Sortierung der Tabellen und Referenzen.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public final class ImportTopology {

    //~ Instance fields --------------------------------------------------------

    private final Set<Reference> topologicalSortedReferences;
    private final Set<String> topologicalSortedTables;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ImportTopology object.
     *
     * @param  references  DOCUMENT ME!
     */
    public ImportTopology(final List<Reference> references) {
        final ArrayList<Reference> refList = TypeSafeCollections.newArrayList(references);
        final List<Reference> sortedRefs = TypeSafeCollections.newArrayList(references.size());
        final Iterator<Reference> it = references.iterator();
        Reference r = null;
        while (it.hasNext()) {
            r = it.next();
            if (r.getReferencingTable().equals(r.getReferencedTable())) {
                sortedRefs.add(r);
                it.remove();
            }
        }
        sortedRefs.addAll(references);

        final DirectedGraph<String> tableGraph = new DirectedGraph<String>(AlphanumComparator.getInstance());
        final DirectedGraph<Reference> refGraph = new DirectedGraph<Reference>();
        for (final Reference ref : sortedRefs) {
            if (!ref.getReferencingTable().equals(ref.getReferencedTable())) {
                tableGraph.addEdge(ref.getReferencingTable(), ref.getReferencedTable());
            } else {
                for (final Reference oth : refList) {
                    if ((oth != ref) && oth.getReferencingField().equals(ref.getReferencedField())) {
                        refGraph.addEdge(oth, ref);
                    }
                }
            }
        }
        topologicalSortedReferences = refGraph.reverseOrderedTopologicalSort();
        topologicalSortedTables = tableGraph.orderedTopologicalSort();
        topologicalSortedReferences.addAll(sortedRefs);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  the topologicalSortedReferences
     */
    public Iterable<Reference> getTopologicalSortedReferences() {
        return Collections.unmodifiableSet(topologicalSortedReferences);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the topologicalSortedTables
     */
    public Iterable<String> getTopologicalSortedTables() {
        return Collections.unmodifiableSet(topologicalSortedTables);
    }
}
