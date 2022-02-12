package approx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * 
 */
public class MisraGries<T> {
    Map<T, Integer> table;
    Set<T> evictionSet;   
    int hits;
    int misses;
    int capacity;
    int evictions;
    
    public MisraGries(int k) {
        capacity = k;
        hits = 0;
        misses = 0;
        evictions = 0;
        
        table = new HashMap<>(capacity);
        evictionSet = new HashSet<>();
    }
    
    public boolean contains(T element)
    {
        boolean result = table.containsKey(element);
        
        if (result)
            hits++;
        else
            misses++;
        
        return result;
    }
    
    public void process(T element)
    {
        if (table.containsKey(element))
        {
            table.put(element, table.get(element) + 1);
            return;
        }

        if (table.size() < this.capacity)
        {
            table.put(element, 1);
            return;
        }

        for (Map.Entry<T, Integer> set : table.entrySet())
        {
            set.setValue(set.getValue() - 1);
            if (set.getValue() == 0)
                evictionSet.add(set.getKey());
        }

        evictions += evictionSet.size();

        for (T key : evictionSet)
        {
            table.remove(key);
        }

        evictionSet.clear();
    }
    
    Iterator<Map.Entry<T, Integer>> getTopK()
    {
        return table.entrySet().iterator();        
    }

    List<T> getTopKKeys()
    {
        List<T> keys = new ArrayList<>(table.keySet());
        Comparator<T> comparer = new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                return table.get(o2) - table.get(o1);
            }
        };
        Collections.sort(keys, comparer);
        return keys;
    }
    
    public int getHits() {
        return hits;
    }

    public int getMisses() {
        return misses;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getEvictions() {
        return evictions;
    }
    
    public static int requiredK(int sz, int desiredFrequency)
    {
        // sz / k  = (dF - 1) => sz  / (dF - 1) = k
        double k = (sz * 1.0) / ((desiredFrequency - 1) * 1.0);
        return (int) Math.ceil(k);
    }
}
