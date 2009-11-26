/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.kernel;

import de.cismet.jpresso.core.data.Reference;
import de.cismet.jpresso.core.serviceprovider.AlphanumComparator;
import de.cismet.jpresso.core.utils.TypeSafeCollections;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Die Klasse erzeugt eine topologische Sortierung der Tabellen und Referenzen
 * 
 * @author srichter
 */
public final class ImportTopology {

    private final Set<Reference> topologicalSortedReferences;
    private final Set<String> topologicalSortedTables;

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
                    if (oth != ref && oth.getReferencingField().equals(ref.getReferencedField())) {
                        refGraph.addEdge(oth, ref);
                    }
                }
            }
        }
        topologicalSortedReferences = refGraph.reverseOrderedTopologicalSort();
        topologicalSortedTables = tableGraph.orderedTopologicalSort();
        topologicalSortedReferences.addAll(sortedRefs);
    }

    /**
     * @return the topologicalSortedReferences
     */
    public Iterable<Reference> getTopologicalSortedReferences() {
        return Collections.unmodifiableSet(topologicalSortedReferences);
    }

    /**
     * @return the topologicalSortedTables
     */
    public Iterable<String> getTopologicalSortedTables() {
        return Collections.unmodifiableSet(topologicalSortedTables);
    }
}
