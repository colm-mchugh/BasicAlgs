package approx;

import java.util.HashMap;
import java.util.HashSet;
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
    
    public boolean lookup(T element)
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
    
}
