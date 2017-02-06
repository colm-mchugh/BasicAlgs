package dp;

import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class TSPer {

    private int N;

    TSPer(Float[] data) {
        this.init(data);
    }

    TSPer() {
    }

    private float calcDistance(Point from, Point to) {
        float d1 = from.xCoord - to.xCoord;
        float d2 = from.yCoord - to.yCoord;
        return (float) Math.sqrt(d1 * d1 + d2 * d2);
    }

    private void init(Float[] data) {
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

    private Map<Short, Point> points;

    private float[][] distances;

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
    private final Short origin = 0;

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
        int originSet = 1;
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
            if (nextSet == (originSet)) {
                nextDistance.put(origin, 0f);
            } else {
                nextDistance.put(origin, Float.MAX_VALUE);
            }
            rv.put(nextSet, nextDistance);
        }
        return rv;
    }

    private void addOptSets(Map<Integer, Map<Short, Float>> rv, int cardinality, int setSize) {
        int originSet = 1;
        int sz = 1 << (cardinality - 1); // always include origin in each set
        int index;
        for (index = 0; index < sz; index++) {
            int nextSet = this.addEl(0, 0);
            for (int element = 1, flag = 1; element < cardinality; flag <<= 1, element++) {
                if ((setSize(index) == setSize) && (index & flag) != 0) {
                    nextSet = this.addEl(nextSet, element);
                }
            }
            Map<Short, Float> nextDistance = new HashMap<>();
            if (nextSet == (originSet)) {
                nextDistance.put(origin, 0f);
            } else {
                nextDistance.put(origin, Float.MAX_VALUE);
            }
            rv.put(nextSet, nextDistance);
        }
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
        BitSet originSet = new BitSet();
        originSet.set(origin);
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

    public void addSets(Map<BitSet, Map<Short, Float>> rv, int cardinality, int setSize) {
        BitSet originSet = new BitSet();
        originSet.set(origin);
        int sz = 1 << (cardinality - 1); // always include origin in each set
        int index;
        for (index = 0; index < sz; index++) {
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
        }
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
        Map<BitSet, Map<Short, Float>> A = new HashMap<>();//this.genBitSet(N);
        BitSet finalSet = null;
        for (short m = 2; m <= N; m++) {
            System.out.println("Starting iteration " + m + " of " + N);
            long startTime = System.nanoTime();
            this.addSets(A, N, m - 1);
            System.out.println("addSets done");
            for (BitSet s : A.keySet()) {
                if (s.cardinality() == m) {
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
                }
                if (finalSet == null && s.cardinality() == N) {
                    finalSet = s;
                }
            }
            this.removeSets(A, m - 2);
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
        return minDistance;
    }

    public float computeOptTsp() {
        Map<Integer, Map<Short, Float>> A = new HashMap<>();//= this.genOptBitSet(N);
        int finalSet = -1;
        for (short m = 2; m <= N; m++) {
            System.out.println("Starting iteration " + m + " of " + N);
            long startTime = System.nanoTime();
            this.addOptSets(A, N, m - 1);
            System.out.println("addSets done");
            for (int s : A.keySet()) {
                if (this.card(s) == m) {
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
                }
                if (finalSet == -1 && this.card(s) == N) {
                    finalSet = s;
                }
            }
            this.removeOptSets(A, m - 2);
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
        return minDistance;
    }

    
}
