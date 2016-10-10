package dp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class tsp {

    private int N;

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

    public Map<BitSet, Map<Short, Float>> genBitSet(int cardinality) {
        Map<BitSet, Map<Short, Float>> rv = new HashMap<>();
        BitSet originSet = new BitSet();
        originSet.set(origin);
        int sz = 1 << (cardinality - 1); // always include origin in each set
        int index;
        for (index = 0; index < sz; index++) {
            BitSet nextSet = new BitSet(N);
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
    
    public Map<Set<Short>, Map<Short, Float>> genSet(int cardinality) {
        Map<Set<Short>, Map<Short, Float>> rv = new HashMap<>();
        Set<Short> originSet = new HashSet<>();
        originSet.add(origin);
        int sz = 1 << (cardinality - 1); // always include origin in each set
        int index;
        for (index = 0; index < sz; index++) {
            Set<Short> nextSet = new HashSet<>();
            nextSet.add(origin);
            int flag = 1;
            for (Short element = 1; element < cardinality; element++) {
                if ((index & flag) != 0) {
                    nextSet.add(element);
                }
                flag <<= 1;
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

    public float computeTsp() {
        Map<BitSet, Map<Short, Float>> A = this.genBitSet(N);
        BitSet finalSet = null;
        for (short m = 2; m <= N; m++) {
            for (BitSet s : A.keySet()) {
                if (s.cardinality() == m) {
                    for (Integer j = s.nextSetBit(0); j >= 0; j = s.nextSetBit(j+1)) {
                        if (origin == j.shortValue()) {
                            continue;
                        }
                        float minVal = Float.MAX_VALUE;
                        s.clear(j);
                        for (Integer k = s.nextSetBit(0); k >= 0; k = s.nextSetBit(k+1)) {
                            if (Objects.equals(j, k)) {
                                continue;
                            }                            
                            Float t2 = A.get(s).get(k.shortValue());
                            Float Dkj = this.distances[k][j];
                            if (minVal > t2 + Dkj) {
                                minVal = t2 + Dkj;
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
        }
        assert finalSet != null;
        float minDistance = Float.MAX_VALUE;
        for (Short j = 1; j < this.N; j++) {
            float jDistance = A.get(finalSet).get(j);
            float finalHop = this.distances[j][origin];
            if (minDistance > jDistance + finalHop) {
                minDistance = jDistance + finalHop;
            }
        }
        return minDistance;
    }

    public void init(String file) {
        FileReader fr;
        try {
            fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            this.N = Integer.parseInt(line);
            this.points = new HashMap<>(N);
            Short c = 0;
            while ((line = br.readLine()) != null) {
                String[] split = line.trim().split("(\\s)+");
                Point p = new Point(
                        Float.parseFloat(split[0]),
                        Float.parseFloat(split[1]));
                this.points.put(c++, p);
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
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String file = "resources/tsp.txt";
        tsp t = new tsp();
        t.init(file);
        float ans = t.computeTsp();
        System.out.println(ans);
    }
}
