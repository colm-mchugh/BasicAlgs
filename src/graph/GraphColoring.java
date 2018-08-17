package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphColoring<T> {
    protected  Graph<T> graph; // the graph under traversal
    protected Map<T, Integer> colorings;
    protected int numColors;
    protected List<T> vertexes;
    
    protected boolean canColor(T v, int c) {
        for (T u : graph.connections(v)) {
            if (colorings.containsKey(u) && colorings.get(u) == c) {
                return false;
            }
        }
        return true;
    }
    
    protected boolean exploreColorings(int vI) {
        
        if (vI == graph.numVertices()) {
            assert (colorings.keySet().size() == graph.numVertices()); 
            return true;
        }
        
        T v = vertexes.get(vI);
        // explore coloring the next vertex
        for (int c = 1; c <= numColors; c++) {
            if (canColor(v, c)) {
                colorings.put(v, c);
                if (exploreColorings(vI + 1)) {
                    return true;
                }
                colorings.remove(v);
            }
        }
        return false;
    }
    
    public boolean color(Graph<T> graph, int numColors) {
        // data structure set up
        this.graph = graph;
        this.numColors = numColors;
        this.colorings = new HashMap<>(graph.numVertices());
        // use fixed list of vertices to explore colorings
        this.vertexes = new ArrayList<>(graph.numVertices());
        for (T v : graph.V()) {
            this.vertexes.add(v);
        }
        
        // explore from vertex 0
        boolean canColor = exploreColorings(0);
        return canColor;
    }
}
