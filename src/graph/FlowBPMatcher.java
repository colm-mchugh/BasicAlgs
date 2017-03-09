package graph;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FlowBPMatcher<T> {

    private Map<T, T[]> matchings;
    private Flow.Max<T> bestMatch;

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
     * @param matchings
     * @param s
     * @param t
     */
    public FlowBPMatcher(Map<T, T[]> matchings, T s, T t) {
        this.matchings = matchings;
        Flow<T> flow = new FlowImpl<>();
        // Construct the residual network for the matching:
        Set<T> sinkSet = new HashSet<>();
        for (T l : matchings.keySet()) {
            flow.link(l, s, 1);
            for (T r : matchings.get(l)) {
                flow.link(r, l, 1);
                if (!sinkSet.contains(r)) {
                    flow.link(t, r, 1); 
                    sinkSet.add(r);
                }
            }
        }
        bestMatch = flow.getMax(s, t);
    }

    public Map<T, T[]> getMatchings() {
        return matchings;
    }

    public Flow.Max<T> getBestMatch() {
        return bestMatch;
    }

}
