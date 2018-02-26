package dp;

import graph.WeightedGraph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Dynamic programming algorithm for Traveling Salesperson.
 * 
 * Problem definition: given a set of points P with an origin point, compute the shortest
 * path that starts and ends at the origin and visits each point in P exactly once.
 * 
 * Optimal substructure: Let P = 1,2,...,N with 1 as origin; 
 * for each j in { 1,2,...N } and each subset S of P that contains 1 and j,
 * L[S, j] = Min path from 1 to j that visits each point in S exactly once.
 * 
 * Recurrence: L[S, j] = MIN( L[S - j, k] + Ckj ) for all k in S, k != j
 * In other words, a shortest path of S with final point j consists of a 
 * shortest path of S - k with final point k plus the distance from k to j.
 * 
 * Algorithm:
 * L[S, 1] = 0 if S = { 1 }, +Inf otherwise     // Base case
 * For |S| in 2,3,..,N      // Solve in order of increasing subproblem size
 *  For each S in { 1,2,..,N } of size |S|  // S must contain 1
 *    For each j in S, j != 1
 *      L[S,j] = MIN ( L[ S - j, k] + Ckj ) for all k in S, k != j
 * Return MIN( L[P, j] + Cj1 ) for all j in 2..N  // Determine the minimum final hop back to 1
 * 
 * Running time: O(N^2 x 2^N) 2^N possible S, N choices of j, N choices of k
 * 
 */
public class TSPer {

    private int N;
    private Map<Short, Point> points;
    private float[][] distances;
    private final Short origin = 0;
    private final BitSet originSet = new BitSet();
    private final int originSetOpt = 1;
    private List<Integer> shortestPath;
    private boolean pruneIntermediateSets;

    TSPer(Float[] data, boolean pruneIntermediateSets) {
        this.N = data.length / 2;
        this.points = new HashMap<>(N);
        for (short i = 0, c = 0; i < data.length; i += 2) {
            this.points.put(c++, new Point(data[i], data[i + 1]));
        }
        this.distances = new float[N][N];
        for (short cFrom = 0; cFrom < N; cFrom++) {
            for (short cTo = 0; cTo < N; cTo++) {
                if (cFrom == cTo) {
                    distances[cFrom][cTo] = 0.0f;
                } else {
                    distances[cFrom][cTo] = this.calcDistance(this.points.get(cFrom), this.points.get(cTo));
                }
            }
        }
        init(pruneIntermediateSets);
    }

    TSPer(WeightedGraph<Integer> g, boolean pruneIntermediateSets) {
        this.N = g.numVertices();
        this.distances = new float[N][N];
        for (int i = 0; i < N; i++) {
            Arrays.fill(this.distances[i], Float.POSITIVE_INFINITY);
        }
        // Assumption: g vertices = 1..N
        for (Integer v : g.V()) {
            for (WeightedGraph.Edge<Integer> e : g.edgesOf(v)) {
                distances[v - 1][e.v - 1] = e.d;
            }
        }
        init(pruneIntermediateSets);
    }

    TSPer(boolean pruneIntermediateSets) {
        init(pruneIntermediateSets);
    }

    private void init(boolean prune) {
        this.pruneIntermediateSets = prune;
        originSet.set(origin);
    }

    private float calcDistance(Point from, Point to) {
        float d1 = from.xCoord - to.xCoord;
        float d2 = from.yCoord - to.yCoord;
        return (float) Math.sqrt(d1 * d1 + d2 * d2);
    }

    public static class Point {

        float xCoord;
        float yCoord;

        public Point(float xCoord, float yCoord) {
            this.xCoord = xCoord;
            this.yCoord = yCoord;
        }

        @Override
        public String toString() {
            return "(" + xCoord + ", " + yCoord + ')';
        }

    }

    public void printDistances() {
        for (Short n : this.points.keySet()) {
            System.out.println("" + n + ": " + this.points.get(n));
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(distances[i][j]);
                System.out.print(' ');
            }
            System.out.println();
        }
    }

    public int addEl(int bitset, int element) {
        bitset |= (1L << (element));
        return bitset;
    }

    public int remEl(int bitset, int element) {
        bitset &= ~(1L << (element));
        return bitset;
    }

    public int card(int bitset) {
        int rv = 0;
        for (int index = 0; index <= Integer.SIZE; index++) {
            if ((bitset & (1L << index)) != 0) {
                rv++;
            }
        }
        return rv;
    }

    public int setSize(int x) {
        x = x - ((x >> 1) & 0x55555555);
        x = (x & 0x33333333) + ((x >> 2) & 0x33333333);
        x = (x + (x >> 4)) & 0x0F0F0F0F;
        x = x + (x >> 8);
        x = x + (x >> 16);
        return x & 0x0000003F;
    }

    public int nextEl(int bitset, int startingElement) {
        if (startingElement > Integer.SIZE) {
            return -1;
        }
        int index = 1 << startingElement;
        while ((bitset & index) == 0 && startingElement <= Integer.SIZE) {
            index = index << 1;
            startingElement++;
        }
        return startingElement <= Integer.SIZE ? startingElement : -1;
    }

    public Map<Integer, Map<Short, Float>> genOptBitSet(int cardinality) {
        Map<Integer, Map<Short, Float>> rv = new HashMap<>();
        int sz = 1 << (cardinality - 1); // always include origin in each set
        int index;
        for (index = 0; index < sz; index++) {
            int nextSet = this.addEl(0, 0);
            for (int element = 1, flag = 1; element < cardinality; flag <<= 1, element++) {
                if ((index & flag) != 0) {
                    nextSet = this.addEl(nextSet, element);
                }
            }
            Map<Short, Float> nextDistance = new HashMap<>();
            if (nextSet == (originSetOpt)) {
                nextDistance.put(origin, 0f);
            } else {
                nextDistance.put(origin, Float.MAX_VALUE);
            }
            rv.put(nextSet, nextDistance);
        }
        return rv;
    }

    private Set<Integer> addOptSets(Map<Integer, Map<Short, Float>> rv, int cardinality, int setSize) {
        int sz = 1 << (cardinality - 1); // always include origin in each set
        Set<Integer> s = new HashSet<>(setSize);
        int index;
        for (index = 0; index < sz; index++) {
            int nextSet = this.addEl(0, 0);
            for (int element = 1, flag = 1; element < cardinality; flag <<= 1, element++) {
                if ((setSize(index) == setSize) && (index & flag) != 0) {
                    nextSet = this.addEl(nextSet, element);
                }
            }
            if (!rv.containsKey(nextSet)) {
                Map<Short, Float> nextDistance = new HashMap<>();
                if (nextSet == (originSetOpt)) {
                    nextDistance.put(origin, 0f);
                } else {
                    nextDistance.put(origin, Float.MAX_VALUE);
                }
                rv.put(nextSet, nextDistance);
            }
            if (!s.contains(nextSet)) {
                s.add(nextSet);
            }
        }
        return s;
    }

    private void removeOptSets(Map<Integer, Map<Short, Float>> rv, int setSize) {
        Set<Integer> deletes = new HashSet<>();
        for (Integer s : rv.keySet()) {
            if (this.card(s) == setSize) {
                deletes.add(s);
            }
        }
        for (Integer d : deletes) {
            rv.remove(d);
        }
    }

    public Map<BitSet, Map<Short, Float>> genBitSet(int cardinality) {
        Map<BitSet, Map<Short, Float>> rv = new HashMap<>();
        int sz = 1 << (cardinality - 1); // always include origin in each set
        int index;
        for (index = 0; index < sz; index++) {
            BitSet nextSet = new BitSet();
            nextSet.set(origin);
            for (int element = 1, flag = 1; element < cardinality; flag <<= 1, element++) {
                if ((index & flag) != 0) {
                    nextSet.set(element);
                }
            }
            Map<Short, Float> nextDistance = new HashMap<>();
            if (nextSet.equals(originSet)) {
                nextDistance.put(origin, 0f);
            } else {
                nextDistance.put(origin, Float.MAX_VALUE);
            }
            rv.put(nextSet, nextDistance);
        }
        return rv;
    }

    public Set<BitSet> addSets(Map<BitSet, Map<Short, Float>> rv, int cardinality, int setSize) {
        int sz = 1 << (cardinality - 1); // always include origin in each set
        Set<BitSet> s = new HashSet<>(sz);
        for (int index = 0; index < sz; index++) {
            BitSet nextSet = new BitSet();
            nextSet.set(origin);
            for (int element = 1, flag = 1; element < cardinality; flag <<= 1, element++) {
                if ((setSize(index) == setSize) && (index & flag) != 0) {
                    nextSet.set(element);
                }
            }
            if (!rv.containsKey(nextSet)) {
                Map<Short, Float> nextDistance = new HashMap<>();
                if (nextSet.equals(originSet)) {
                    nextDistance.put(origin, 0f);
                } else {
                    nextDistance.put(origin, Float.MAX_VALUE);
                }
                rv.put(nextSet, nextDistance);
            }
            if (!s.contains(nextSet)) {
                s.add(nextSet);
            }
        }
        return s;
    }

    public void removeSets(Map<BitSet, Map<Short, Float>> rv, int setSize) {
        Set<BitSet> deletes = new HashSet<>();
        for (BitSet s : rv.keySet()) {
            if (s.cardinality() == setSize) {
                deletes.add(s);
            }
        }
        for (BitSet d : deletes) {
            rv.remove(d);
        }
    }

    /**
     * A TSP path is a path that starts from an origin and visits every other
     * node exactly once, returning to the origin at the finish. This function
     * calculates the shortest such path using the following algorithm:
     * 
     * Given each possible destination j in { origin, 1, 2, ..., N - 1 }
     * For each possible subset S of { origin, 1, 2, ..., N - 1 } that contains 
     * the origin and j, calculate the minimum path from the origin to j that
     * visits only the nodes in S exactly once each. (note: |S| >= 2)
     * 
     * In any set S, if the last hop of the shortest path is (k, j) then the
     * shortest path - (k, j) is a shortest path from 1 to k that visits every
     * node of S - {j} exactly once. 
     * I.e. SP(S,j) = MIN( SP(S-{j},k) + (k,j) ) for all k in S, k != j
     * 
     * The algorithm starts with |S| = 2, i.e. where each S contains the origin and
     * every other node, and calculates the minimum path for each possible S.
     * Subsequent iterations (|S| = 2, 3, ... N) use the recurrence above to 
     * determine the minimum path from the origin to each node in S.
     * 
     * Finally, at the end, we need to calculate the minimum path back to the
     * origin by determining MIN( SP({origin, 1, 2, ..., N - 1}, j) + (k, origin)) for all j in 1..N -1
     * 
     * @return 
     */
    public float computeTsp() {
        Map<BitSet, Map<Short, Float>> A = new HashMap<>();
        BitSet finalSet = null;
        for (short m = 2; m <= N; m++) {
            System.out.println("Starting iteration " + m + " of " + N);
            long startTime = System.nanoTime();
            Set<BitSet> nxtSet = this.addSets(A, N, m - 1);
            System.out.println("addSets done");
            for (BitSet s : nxtSet) {
                assert s.equals(originSet) || s.cardinality() == m;
                for (Integer j = s.nextSetBit(0); j >= 0; j = s.nextSetBit(j + 1)) {
                    if (origin == j.shortValue()) {
                        continue;
                    }
                    float minVal = Float.MAX_VALUE;
                    s.clear(j);
                    for (Integer k = s.nextSetBit(0); k >= 0; k = s.nextSetBit(k + 1)) {
                        if (Objects.equals(j, k)) {
                            continue;
                        }
                        Float Dkj = A.get(s).get(k.shortValue()) + this.distances[k][j];
                        if (minVal > Dkj) {
                            minVal = Dkj;
                        }
                    }
                    s.set(j);
                    A.get(s).put(j.shortValue(), minVal);
                }
                if (finalSet == null && s.cardinality() == N) {
                    finalSet = s;
                }
            }
            if (pruneIntermediateSets) {
                this.removeSets(A, m - 2);
            }
            long elapsedTime = TimeUnit.SECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
            System.out.println("Finished iteration " + m + " of " + N + "(" + elapsedTime + " seconds)");
        }
        assert finalSet != null;
        float minDistance = Float.MAX_VALUE;
        for (Short j = 1; j < this.N; j++) {
            float jDistance = A.get(finalSet).get(j) + this.distances[j][origin];
            if (minDistance > jDistance) {
                minDistance = jDistance;
            }
        }
        if (!pruneIntermediateSets) {
            shortestPath = constructPath(finalSet, A);
        }
        return minDistance;
    }

    private List<Integer> constructPath(BitSet finalSet, Map<BitSet, Map<Short, Float>> A) {
        BitSet currSet = finalSet;
        this.shortestPath = new ArrayList<>(N);

        Short j = origin;
        Short nextHop = -1;
        List<Short> remainingPoints = new ArrayList<>(N - 1);
        for (Short i = 1; i < this.N; i++) {
            remainingPoints.add(i);
        }
        while (!currSet.equals(originSet)) {
            float minDistance = Float.MAX_VALUE;
            for (Short i : remainingPoints) {
                float jDistance = A.get(currSet).get(i) + this.distances[i][j];
                if (minDistance > jDistance) {
                    minDistance = jDistance;
                    nextHop = i;
                }
            }
            currSet.clear(nextHop);
            remainingPoints.remove(nextHop);
            shortestPath.add((int) nextHop + 1);
            j = nextHop;
        }
        shortestPath.add(origin + 1);
        return reverse(shortestPath);
    }

    public float computeOptTsp() {
        Map<Integer, Map<Short, Float>> A = new HashMap<>();
        int finalSet = -1;
        for (short m = 2; m <= N; m++) {
            System.out.println("Starting iteration " + m + " of " + N);
            long startTime = System.nanoTime();
            Set<Integer> nextSet = this.addOptSets(A, N, m - 1);
            System.out.println("addSets done");
            for (int s : nextSet) {
                assert (this.card(s) == m) || (s == originSetOpt);
                for (Integer j = this.nextEl(s, 0); j >= 0; j = this.nextEl(s, j + 1)) {
                    if (origin == j.shortValue()) {
                        continue;
                    }
                    float minVal = Float.MAX_VALUE;
                    s = this.remEl(s, j);
                    for (Integer k = this.nextEl(s, 0); k >= 0; k = this.nextEl(s, k + 1)) {
                        if (Objects.equals(j, k)) {
                            continue;
                        }
                        Float Dkj = A.get(s).get(k.shortValue()) + this.distances[k][j];
                        if (minVal > Dkj) {
                            minVal = Dkj;
                        }
                    }
                    s = this.addEl(s, j);
                    A.get(s).put(j.shortValue(), minVal);
                }
                if (finalSet == -1 && this.card(s) == N) {
                    finalSet = s;
                }
            }
            if (pruneIntermediateSets) {
                this.removeOptSets(A, m - 2);
            }
            long elapsedTime = TimeUnit.SECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
            System.out.println("Finished iteration " + m + " of " + N + "(" + elapsedTime + " seconds)");
        }
        assert finalSet != -1;
        float minDistance = Float.MAX_VALUE;
        for (Short j = 1; j < this.N; j++) {
            float jDistance = A.get(finalSet).get(j) + this.distances[j][origin];
            if (minDistance > jDistance) {
                minDistance = jDistance;
            }
        }
        if (!pruneIntermediateSets) {
            shortestPath = constructPathOpt(finalSet, A); // can't reconstruct final path if the set is pruned
        }
        return minDistance;
    }

    private List<Integer> constructPathOpt(int finalSet, Map<Integer, Map<Short, Float>> A) {
        int currSet = finalSet;
        this.shortestPath = new ArrayList<>(N);
        Short j = origin;
        Short nextHop = -1;
        List<Short> remainingPoints = new ArrayList<>(N - 1);
        for (Short i = 1; i < this.N; i++) {
            remainingPoints.add(i);
        }
        while (currSet != originSetOpt) {
            float minDistance = Float.MAX_VALUE;
            for (Short i : remainingPoints) {
                float jDistance = A.get(currSet).get(i) + this.distances[i][j];
                if (minDistance > jDistance) {
                    minDistance = jDistance;
                    nextHop = i;
                }
            }
            currSet = this.remEl(currSet, nextHop);
            remainingPoints.remove(nextHop);
            shortestPath.add((int) nextHop + 1);
            j = nextHop;
        }
        shortestPath.add(origin + 1);
        return reverse(shortestPath);
    }

    private List<Integer> reverse(List<Integer> l) {
        for (int i = 0, j = l.size() - 1; i < j; i++, j--) {
            Integer tmp = l.get(i);
            l.set(i, l.get(j));
            l.set(j, tmp);
        }
        return l;
    }

    public List<Integer> getShortestPath() {
        return shortestPath;
    }

}
