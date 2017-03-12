package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FlowBPMatcher<T> {

    private final Map<T, List<T>> preferences = new HashMap<>();
    private Flow.Max<T> bestMatch = null;
    private final Map<T, T> matchings = new HashMap<>();
    private final Set<T> U = new HashSet<>();
    private final Set<T> V = new HashSet<>();
    private final T source;
    private final T sink;
    private final Flow<T> flow = new FlowImpl<>();

    /**
     * Provide the matcher with a map of preferences. This is application
     * dependent, it may reflect "who can do what" or "who wants what" or "what
     * can go where".. For example, if trying to match workers to jobs, the
     * matcher may be given the jobs each worker can do:
     *
     * W1 -> J1; W2 -> J1,J3; W3 -> J2,J3,J4; W4 -> J3; W5 -> J3.
     *
     * In addition, the client also specifies two values not in the matchings;
     * these are used as the source and sink in the flow network that calculates
     * the optimal matching.
     *
     * @param s
     * @param t
     */
    public FlowBPMatcher(T s, T t) {
        this.source = s;
        this.sink = t;
    }

    public void link(T u, T v) {
        if (!U.contains(u)) {
            flow.link(u, source, 1);
            U.add(u);
        }
        flow.link(v, u, 1);
        if (!V.contains(v)) {
            flow.link(sink, v, 1);
            V.add(v);
        }
        List<T> uPrefs = preferences.get(u);
        if (uPrefs == null) {
            uPrefs = new ArrayList<>();
        }
        uPrefs.add(v);
        preferences.put(u, uPrefs);
    }

    public Map<T, List<T>> getPreferences() {
        return preferences;
    }

    public Map<T, T> getMatchings() {
        return matchings;
    }

    public Flow.Max<T> getBestMatch() {
        if (bestMatch == null) {
            bestMatch = flow.getMax(this.source, this.sink);
            for (T u : U) {
                for (Flow.Edge<T> e : flow.edgesOf(u)) {
                    T v = e.other(u);
                    if (V.contains(v) && e.residualCapacity(v) == 0) {
                        matchings.put(u, v);
                    }
                }
            }
        }
        return bestMatch;
    }

}
