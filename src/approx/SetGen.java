package approx;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Queue;
import utils.Stack;

public class SetGen {

    public static class item {
        int level;
        boolean live;

        public item(int level, boolean live) {
            this.level = level;
            this.live = live;
        }

    }

    public static List<BitSet> dfsSetGen(int N) {
        List<BitSet> perms = new ArrayList<>(1 << N);
        BitSet live = new BitSet(N + 1);
        Stack<item> S = new Stack<>();
        item root = new item(0, false);
        S.Push(root);
        while (!S.isEmpty()) {
            item next = S.Pop();
            live.set(next.level, next.live);
            if (next.level <= N) {
                S.Push(new item(next.level + 1, false));
                S.Push(new item(next.level + 1, true));
            }
            if (next.level == N) {
                perms.add(live.get(0, live.size() - 1));
            }
        }
        return perms;
    }

    public static List<BitSet> bfsSetGen(int N) {
        List<BitSet> perms = new ArrayList<>(1 << N);
        BitSet live = new BitSet(N + 1);
        Queue<item> Q = new ArrayDeque<>(N);
        item root = new item(0, false);
        Q.add(root);
        while (!Q.isEmpty()) {
            item next = Q.remove();
            live.set(next.level, next.live);
            if (next.level <= N) {
                Q.add(new item(next.level + 1, true));
                Q.add(new item(next.level + 1, false));
            }
            if (next.level == N) {
                perms.add(live.get(0, live.size() - 1));
            }
        }
        return perms;
    }

    public static class itemDiscrepancy extends item {
        int d;
        int t;
        private itemDiscrepancy(int i, boolean b, int d, int t) {
            super(i, b);
            this.d = d;
            this.t = t;
        }
    }

    public static List<BitSet> ldsSetGen(int N) {
        List<BitSet> perms = new ArrayList<>(1 << N);
        BitSet live = new BitSet(N + 1);
        Stack<itemDiscrepancy> S = new Stack<>();
        for (int d = 0; d <= N; d++) {
            itemDiscrepancy root = new itemDiscrepancy(0, false, d, N - d);
            S.Push(root);
            while (!S.isEmpty()) {
                itemDiscrepancy next = S.Pop();
                live.set(next.level, next.live);
                if (next.level < N) {
                    if (next.d > 0) {
                        S.Push(new itemDiscrepancy(next.level + 1, false, next.d - 1, next.t));
                    }
                    if (next.t > 0) {
                        S.Push(new itemDiscrepancy(next.level + 1, true, next.d, next.t - 1));
                    }
                }
                if (next.level == N) {
                    perms.add(live.get(0, live.size() - 1));
                }
            }
        }
        return perms;
    }

}
