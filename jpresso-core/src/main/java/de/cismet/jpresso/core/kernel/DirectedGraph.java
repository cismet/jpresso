/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.kernel;

import de.cismet.jpresso.core.utils.TypeSafeCollections;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Represents a directed graph with vertices and edges
 * 
 * @author stefan
 */
public class DirectedGraph<T> {

    public DirectedGraph() {
        this.comparator = null;
    }

    public DirectedGraph(Comparator<T> graphSortComparator) {
        this.comparator = graphSortComparator;
    }
    public static final int VISITED = 0;
    public static final int COMPLETED = 1;
    //adjacency list that uses a HashMap to map each vertex to its list of neighbor vertices
    private final Map<T, Set<T>> adjacencyList = TypeSafeCollections.newHashMap();
    private final Comparator<T> comparator;

    /**
     * Add a new vertex.  Nothing happens if it's already in.
     */
    public void addVertex(T vertex) {
        if (adjacencyList.containsKey(vertex)) {
            return;
        }
        adjacencyList.put(vertex, new HashSet<T>());
    }

    /**
     * Vertex in the graph?
     */
    public boolean contains(T vertex) {
        return adjacencyList.containsKey(vertex);
    }

    /**
     * Add an edge from one vertex to another to the graph.
     * If vertex is not already in, it's added.
     * 
     * Multi-edges and self-loops are possible.
     */
    public void addEdge(T from, T to) {
        this.addVertex(from);
        this.addVertex(to);
        adjacencyList.get(from).add(to);
    }

    /**
     * Remove an edge from the graph.
     * 
     * @throws IllegalArgumentException if the vertex does not exist in the graph
     */
    public void remove(T from, T to) {
        if (!(this.contains(from) && this.contains(to))) {
            throw new IllegalArgumentException("Nonexistent vertex");
        }
        adjacencyList.get(from).remove(to);
    }

    /**
     * Topological sorts the graph using and returns the order as list.
     * It tries to bring Objects on one common level in a sorted order.
     * If a loop is detected, it returns null as no sorting is possible.
     */
    public final Set<T> orderedTopologicalSort() {
        final Set<T> result = TypeSafeCollections.newLinkedHashSet();
        final Map<T, Set<T>> orderedAdjacencyListCopy = TypeSafeCollections.newLinkedHashMap();
        final List<T> pathsToOrderList = TypeSafeCollections.newArrayList(adjacencyList.keySet());
        if (comparator != null) {
            Collections.sort(pathsToOrderList, comparator);
        }
        for (final T item : pathsToOrderList) {
            orderedAdjacencyListCopy.put(item, new HashSet<T>(adjacencyList.get(item)));
        }
        while (!orderedAdjacencyListCopy.isEmpty()) {
            final List<T> current = TypeSafeCollections.newArrayList();
            final Iterator<Entry<T, Set<T>>> it = orderedAdjacencyListCopy.entrySet().iterator();
            T item = null;
            while (it.hasNext()) {
                final Entry<T, Set<T>> entry = it.next();
                item = entry.getKey();
                final Set<T> chk = entry.getValue();
                if (chk == null || chk.isEmpty()) {
                    it.remove();
                    current.add(item);
                    for (final Set<T> set : orderedAdjacencyListCopy.values()) {
                        set.remove(item);
                    }
                }
            }
            if (current.isEmpty()) {
                throw new IllegalStateException("Relationgraph has cyclic dependencies on item " + item + "!");
            }

            result.addAll(current);

        }
        return result;
    }

    public final Set<T> reverseOrderedTopologicalSort() {
        final List<T> tmp = TypeSafeCollections.newArrayList(orderedTopologicalSort());
        Collections.reverse(tmp);
        final Set<T> res = TypeSafeCollections.newLinkedHashSet(tmp);
        return res;
    }
}
