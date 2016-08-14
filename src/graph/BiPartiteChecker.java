package graph;


import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;


public class BiPartiteChecker<T> {
    private final Graph<T> rep;
    private final Map<Boolean, Set<T>> colorBags;
    private static final boolean RED = true;
    private static final boolean BLUE = false;
    private boolean bipartite;
    
    private enum tristate {
        RED,
        BLUE,
        NONE
    }
    
    public BiPartiteChecker(Graph<T> rep) {
        this.rep = rep;
        this.colorBags = new HashMap<>();
        this.colorBags.put(RED, new HashSet<>());
        this.colorBags.put(BLUE, new HashSet<>());
        if (rep.numVertices() > 0) {
            this.twoColor();
        }
    }
    
    private void twoColor() {
        boolean color = RED;
        Queue<T> queue = new ArrayDeque<>();
        T source = rep.V().iterator().next();
        colorIt(source, color);
        queue.add(source);
        while (!queue.isEmpty()) {
            T next = queue.remove();
            color = (colorOf(next) == tristate.BLUE ? RED : BLUE);
            for (T v : rep.connections(next)) {
                if (sameColor(v, next)) {
                    this.bipartite = false;
                    return;
                }
                if (!this.colorBags.get(color).contains(v)) {
                    this.colorIt(v, color);
                    queue.add(v);
                }
            }
        }
        this.bipartite = true;
    }
    
    public boolean isBiPartite() {
        return this.bipartite;
    }

    private void colorIt(T source, boolean color) {
        this.colorBags.get(color).add(source);
    }
    
    private boolean sameColor(T e1, T e2) {
        tristate c1 = colorOf(e1);
        tristate c2 = colorOf(e2);
        return (c1 == c2) && (c1 != tristate.NONE);
    }
    
    private tristate colorOf(T e1) {
        if (this.colorBags.get(RED).contains(e1)) {
            return tristate.RED;
        }
        if (this.colorBags.get(BLUE).contains(e1)) {
            return tristate.BLUE;
        }
        return tristate.NONE;
    }
}
