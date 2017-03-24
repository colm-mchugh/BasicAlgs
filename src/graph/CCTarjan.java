package graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
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
public class CCTarjan<T> {
    
    private final Map<T, List<T>> components;
    private int index;
    private final Stack<T> stack;
    private final Map<T, T> revCmpntIndx;
    
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
    
    public CCTarjan(Graph<T> rep) {
        this.components = new HashMap<>();
        this.index = 0;
        this.stack = new Stack<>();
        this.revCmpntIndx = new Hashtable<>();
        HashMap<T, Node<T>> vertices = new HashMap<>(rep.numVertices());
        for (T v : rep.V()) {
            vertices.put(v, new Node<>(v));
        }
        for (Node<T> v : vertices.values()) {
            if (v.index == Integer.MIN_VALUE) {
                strongconnect(rep, v, vertices);
            }
        }
    }
    
    private void strongconnect(Graph<T> rep, Node<T> curr, HashMap<T, Node<T>> vertices) {
        curr.index = index;
        curr.lowlink = index;
        index++;
        stack.push(curr.v);
        curr.onStack = true;
        for (T w : rep.connections(curr.v)) {
            Node<T> wNode = vertices.get(w);
            if (wNode.index == Integer.MIN_VALUE) {
                strongconnect(rep, wNode, vertices);
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
                revCmpntIndx.put(w, curr.v);
            }
            this.components.put(curr.v, scc);
        }
    }

    public Map<T, List<T>> getComponents() {
        return components;
    }
    
    public List<Integer> ccSizes() {
        List<Integer> rv = new ArrayList<>(this.components.keySet().size());
        for (T v : this.components.keySet()) {
            rv.add(this.components.get(v).size());
        }
        rv.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        });
        System.out.println("#components=" + this.components.keySet().size());
        return rv;
    }
    
    public boolean sameCC(T t1, T t2) {
        T l1 = this.revCmpntIndx.get(t1);
        T l2 = this.revCmpntIndx.get(t2);
        return l1 != null && l2 != null && l1 == l2;
    }
    
}
