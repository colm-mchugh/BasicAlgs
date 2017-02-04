package sort;

import java.util.ArrayList;
import java.util.List;

public class TimSort {

    private final static int MINRUN = 64;

    private static void doTimSort(Comparable[] a, Comparable[] newa, List<Run> runs) {
        throw new UnsupportedOperationException("Not supported yet.");

    }

    public static class Run {
        int i, j;
    }

    public static List<Run> identifyRuns(Comparable[] a) {
        List<Run> runs = new ArrayList<>();
        for (int i = 0; i < a.length;) {
            Run run = new Run();
            run.i = run.j = i;
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
        doTimSort(a, newa, identifyRuns(a));
    }
}
