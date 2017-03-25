package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Faithful transfer of Tarjan's algorithm for computing the strongly
 * connected components of a directed graph. 
 * 
 * https://en.wikipedia.org/wiki/Tarjan%27s_strongly_connected_components_algorithm
 * 
 * @param <T> 
 */
public class CCTarjan<T> extends CCer<T> {
    
    private int pathIndex;
    private final Stack<T> stack;
    
    public static class Node<T> {
        T v;
        int index;
        int lowlink;
        boolean onStack;

        public Node(T v) {
            this.v = v;
            this.index = Integer.MIN_VALUE;
            this.onStack = false;
        }      

        @Override
        public String toString() {
            return "" + v + ", (" + index + ", " + lowlink + "), " + (onStack ? "on" : "off") + '}';
        }      
        
    }

    public CCTarjan() {
        super();
        this.stack = new Stack<>();
    }
       
    public Map<T, List<T>> getComponents(Graph<T> g) {
        if (this.g == g && !this.components.isEmpty()) {
            return this.components;
        }
        this.g = g;
        this.pathIndex = 0;
        this.stack.clear();
        HashMap<T, Node<T>> vertices = new HashMap<>(g.numVertices());
        for (T v : g.V()) {
            vertices.put(v, new Node<>(v));
        }
        for (Node<T> v : vertices.values()) {
            if (v.index == Integer.MIN_VALUE) {
                strongconnect(v, vertices);
            }
        }
        return this.components;
    }
    
    private void strongconnect(Node<T> curr, HashMap<T, Node<T>> vertices) {
        curr.index = pathIndex;
        curr.lowlink = pathIndex;
        pathIndex++;
        stack.push(curr.v);
        curr.onStack = true;
        for (T w : g.connections(curr.v)) {
            Node<T> wNode = vertices.get(w);
            if (wNode.index == Integer.MIN_VALUE) {
                strongconnect(wNode, vertices);
                curr.lowlink = Integer.min(curr.lowlink, wNode.lowlink);
            } else if (wNode.onStack) {
                curr.lowlink = Integer.min(curr.lowlink, wNode.lowlink);
            }
        }
        if (curr.lowlink == curr.index) {
            List<T> scc = new ArrayList<>();
            for (T w = null; w != curr.v; ) {
                w = stack.pop();
                vertices.get(w).onStack = false;
                scc.add(w);
                index.put(w, curr.v);
            }
            this.components.put(curr.v, scc);
        }
    }
   
}
