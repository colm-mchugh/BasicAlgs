package approx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import utils.RandGen;
import utils.math;

public class MisraGriesTest {
    
    public MisraGriesTest() {
        RandGen.setSeed(12345678);
    }
    
    @Test
    public void testK()
    {
        Integer[] stream = { 1,1,1,2,3,4,5,6,7,8 };
        
        MisraGries<Integer> mg3 = new MisraGries<>(3);
        MisraGries<Integer> mg4 = new MisraGries<>(4);
        Map.Entry<Integer, Integer> e;
        
        for (int s : stream)
        {
            mg3.process(s);
            mg4.process(s);
        }
        
        printTopK("Top 3 Cache", mg3);
        printTopK("Top 4 Cache", mg4);
    }
    
    @Test
    public void testK2()
    {
        Integer[] stream = math.genExponentialArray(100, 0.1f, 2);
        List<Integer> orderedTopK;
        int k = MisraGries.requiredK(stream.length, 10);
        
        System.out.println("k=" + k);
        MisraGries<Integer> mg5 = new MisraGries<>(k);
        
        for (int s: stream)
        {
            mg5.process(s);
        }
        
        orderedTopK = mg5.getTopKKeys();
        System.out.println("Keys in order of frequency");
        for (Integer i : orderedTopK)
        {
            System.out.println(i);
        }
        
        printCounts(stream); 
    }

    private void printTopK(String header, MisraGries<Integer> mg)
    {
        Iterator<Map.Entry<Integer, Integer>> itrtr = mg.getTopK();
        Map.Entry<Integer, Integer> e;
        
        System.out.println("Hits=" + mg.getHits() + ", Misses=" + mg.getMisses() + ", Evictions=" + mg.getEvictions());
        System.out.println(header);
        while (itrtr.hasNext())
        {
            e = itrtr.next();
            System.out.println(e.getKey() + ", count=" + e.getValue());
        }
    }   
    
    private Map<Integer, Integer> getCounts(Integer[] a) {      
        Map<Integer, Integer> freqs = new HashMap<>(a.length);
        
        for (int x : a) {
            if (!freqs.containsKey(x)) {
                freqs.put(x, 1);
            } else {
                freqs.put(x, freqs.get(x) + 1);
            }
        }
        
        return freqs;
    }

    private void printCounts(Integer[] a) {     
        Map<Integer, Integer> valueCount = getCounts(a);
        List<Integer> distinctValues = new ArrayList<>(valueCount.keySet());
        Comparator<Integer> comparer = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return valueCount.get(o2) - valueCount.get(o1);
            }
        };
        Collections.sort(distinctValues, comparer);
        
        for (int i : distinctValues) {
            System.out.print("(" + i + ": " + valueCount.get(i) + "), ");
        }
        System.out.println();
    }
}
