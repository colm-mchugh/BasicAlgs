package sort;

import java.util.ArrayList;
import java.util.List;

public class TimSort {
    
    private final static boolean ASC = true;
    private final static boolean DESC = false;

    private static void doTimSort(Comparable[] a, Comparable[] newa, List<Run> runs) {
        throw new UnsupportedOperationException("Not supported yet."); 
        
    }
    
    private static class Run {
        int i, j;
        boolean direction;
    }
    
    private static List<Run> identifyRuns(Comparable[] a) {
        List<Run> runs = new ArrayList<>();
        for (int i = 0; i < a.length;) {
            Run run = new Run();
            run.i = run.j = i;
            while((run.j + 1 < a.length) && a[run.j].equals(a[run.j + 1])) {
                run.j++;
            }
            if (run.j + 1 < a.length) {
                run.direction = (a[run.j].compareTo(a[run.j + 1]) < 0 ? ASC : DESC);
                while ((run.j + 1 < a.length)) {
                    if ((run.direction == ASC && a[run.j].compareTo(a[run.j + 1]) < 0)
                            || (run.direction == ASC && a[run.j].compareTo(a[run.j + 1]) > 0)) {
                        run.j++;
                    } else {
                        break;
                    }
                }
            }
            i = run.j + 1;
            runs.add(run);
        }
        return runs;
    }
    
    public static void timSort(Comparable[] a) {
        Comparable[] newa = new Comparable[a.length];
        doTimSort(a, newa, identifyRuns(a));
    }
}
