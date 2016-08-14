package graph;


import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Set;


public abstract class GraphTraversalBase<T> implements GraphTraveral<T> {
    protected  Graph<T> graph;
    protected  T source;
    protected  Map<T, T> linksTo;
    protected  Set<T> visited;
    
    @Override
    public boolean hasPathTo(T x) {
        return this.visited.contains(x);
    }
    
    @Override
    public Iterable<T> pathTo(T x) {
        Deque<T> path = new ArrayDeque<>();
        if (!this.hasPathTo(x)) {
            return path;
        }
        for (T v = x; v != this.source; v = this.linksTo.get(v)) {
            path.push(v);
        }
        path.push(this.source);
        return path;
    }
}
