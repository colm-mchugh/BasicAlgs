package sort;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class TimSort {

    private final static int MINRUN = 64;

    private static void mergeRuns(Comparable[] a, Queue<Run> runs, Comparable[] tmp) {
        if (runs.size() <= 1) {
            return;
        }
        Queue<Run> newRuns = new ArrayDeque<>(runs.size() / 2);

        while (runs.size() >= 2) {
            Run r1 = runs.remove();
            Run r2 = runs.remove();

            System.arraycopy(a, r1.i, tmp, r1.i, r2.j - r1.i + 1);

            int li = r1.i;       // for indexing into the sorted lower half
            int ui = r2.i;       // for indexing into the sorted upper half
            int ia = r1.i;
            while ((li <= r1.j) && (ui <= r2.j)) {
                if (tmp[li].compareTo(tmp[ui]) <= 0) {
                    a[ia++] = tmp[li++];    // take the next lower half element - its smaller (or equal)
                } else {
                    a[ia++] = tmp[ui++];    // take the next upper half element - its smaller
                }
            }
            while (ui < r2.j + 1) {  // upper half not completely processed ?
                a[ia++] = tmp[ui++]; // => copy over the remaining upper half
            }
            while (li < r1.j + 1) {    // lower half not completely processed ?
                a[ia++] = tmp[li++]; // => copy over the remaining lower half
            }
            newRuns.add(new Run(r1.i, r2.j));
        }
        if (runs.size() == 1) {
            newRuns.add(runs.remove());
        }
        mergeRuns(a, newRuns, tmp);
    }

    public static class Run {

        int i, j;

        Run(int i, int j) {
            this.i = i;
            this.j = j;
        }

        int len() {
            return j - i + 1;
        }
    }

    public static List<Run> identifyRuns(Comparable[] a) {
        List<Run> runs = new ArrayList<>();
        for (int i = 0; i < a.length;) {
            Run run = new Run(i, i);
            if (run.j + 1 < a.length) {
                boolean isDesc = a[run.j].compareTo(a[run.j + 1]) > 0;
                while ((run.j + 1 < a.length)) {
                    if ((!isDesc && a[run.j].compareTo(a[run.j + 1]) <= 0)
                            || (isDesc && a[run.j].compareTo(a[run.j + 1]) > 0)) {
                        run.j++;
                    } else {
                        break;
                    }
                }
                if (isDesc) {
                    Reverse(a, run.i, run.j);
                }
            }
            i = run.j + 1;
            runs.add(run);
        }
        for (int i = 0, j = 0; i < runs.size(); i++) {
            int l = runs.get(i).len();
            for (j = i + 1; j < runs.size() && l < MINRUN; j++) {
                l += runs.get(j).len();
            }
        }
        return runs;
    }

    public static void Reverse(Comparable[] a, int from, int to) {
        for (; from < to; from++, to--) {
            Comparable tmp = a[from];
            a[from] = a[to];
            a[to] = tmp;
        }
    }

    public static void Sort(Comparable[] a) {
        if (a.length <= MINRUN) {
            InsertionSort.Sort(a, 0, a.length - 1);
            return;
        }
        Comparable[] newa = new Comparable[a.length];
        Queue<Run> runQueue = new ArrayDeque<>(identifyRuns(a));
        mergeRuns(a, runQueue, newa);
    }
}
