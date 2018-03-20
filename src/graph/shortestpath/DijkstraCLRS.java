
package graph.shortestpath;

import graph.WeightedGraph;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Implementation of Dijkstra shortest paths.
 * 
 * - uses Priority Queue with custom Comparator
 * - 
 * @author Colm
 * @param <T> 
 */
public class DijkstraCLRS<T> {
    
    private WeightedGraph<T> G;

    public DijkstraCLRS(WeightedGraph<T> G) {
        this.G = G;
    }

    public Map<T, Integer> sp(T s) {
        Map<T, Integer> distances = new HashMap<>(G.numVertices());
        for (T v : G.V()) {
            distances.put(v, (v != s ? Integer.MAX_VALUE : 0));
        }
        PriorityQueue<T> Q = new PriorityQueue<>(new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                return distances.get(o1) - distances.get(o2);
            }
        });
        for (T v : G.V()) {
            Q.add(v);
        }   
        while (!Q.isEmpty()) {
            s = Q.remove();
            for (WeightedGraph.Edge<T> e : G.edgesOf(s)) {
                if (Q.contains(e.v) && distances.get(s) + e.d < distances.get(e.v)) {
                    Q.remove(e.v);
                    distances.put(e.v, distances.get(s) + e.d);
                    Q.add(e.v);
                }
            }
        }
        return distances;
    }
}
