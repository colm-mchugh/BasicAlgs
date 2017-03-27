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
            Map<Short, Float> nextDistance = new HashMap<>();
            if (nextSet == (originSetOpt)) {
                nextDistance.put(origin, 0f);
            } else {
                nextDistance.put(origin, Float.MAX_VALUE);
            }
            rv.put(nextSet, nextDistance);
            s.add(nextSet);
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
            Map<Short, Float> nextDistance = new HashMap<>();
            if (nextSet.equals(originSet)) {
                nextDistance.put(origin, 0f);
            } else {
                nextDistance.put(origin, Float.MAX_VALUE);
            }
            rv.put(nextSet, nextDistance);
            s.add(nextSet);
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
        List<Short> remainingPoints = new ArrayList<>(N-1);
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
            shortestPath.add((int)nextHop + 1);
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
        List<Short> remainingPoints = new ArrayList<>(N-1);
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
            shortestPath.add((int)nextHop + 1);
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
