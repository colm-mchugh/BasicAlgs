package clustering;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class QuickFind<T> implements UnionFind<T> {

    // groupings maps an object to its group number
    private final Map<T, Integer> groupings = new HashMap<>();
    
    // groupingSets maps a group number to its set of objects
    private final Map<Integer, Set<T>> groupingSets = new HashMap<>();
    
    // keeps track of number of groups (1 based). 
    int groupCounter = 0;

    public QuickFind() {
    }
    
    public QuickFind(Iterable<T> elements) {
        for (T element : elements) {
            addCluster(element);
        }
    }

    public QuickFind(T elements[]) {
        for (T element : elements) {
            addCluster(element);
        }
    }

    @Override
    public void addCluster(T p) {
        groupCounter++;
        groupings.put(p, groupCounter);
        Set<T> elementSet = new HashSet<>();
        elementSet.add(p);
        groupingSets.put(groupCounter, elementSet);
    }
    
    
    
    @Override
    public void union(T p, T q) {
        // Change all objects in p's set to q's set
        int pSetID = groupings.get(p);
        int qSetID = groupings.get(q);
        
        // Check if p and q are in the same set
        if (pSetID == qSetID) {
            return;
        }
        
        // Merge the smaller set into the larger
        Set<T> pSet = groupingSets.get(pSetID);
        Set<T> qSet = groupingSets.get(qSetID);
        
        if (qSet.size() > pSet.size()) {
            pSet = qSet;
            qSet = groupingSets.get(pSetID);
            int t = pSetID;
            pSetID = qSetID;
            qSetID = t;
        }
        
        for (T el : qSet) {
            groupings.put(el, pSetID);
        }
        pSet.addAll(qSet);
        groupingSets.put(pSetID, pSet);
        groupingSets.remove(qSetID);
    }

    @Override
    public boolean find(T p, T q) {
        return Objects.equals(groupings.get(p), groupings.get(q));
    }
    
    @Override
    public Iterator<T> iterator() {
        return groupings.keySet().iterator();
    }
    
    @Override
    public Set<T> Cluster(T p) {
        Set<T> rv = null;
        if (this.groupings.containsKey(p)) {
            int groupNumber = this.groupings.get(p);
            rv = this.groupingSets.get(groupNumber);
        }
        return rv;
    }

    @Override
    public int numClusters() {
        return this.groupingSets.keySet().size();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        UnionFind<Integer> uf;
        Integer[] pairs = { 5,8, 2,5, 9,7, 6,5, 3,8, 2,0 };
        uf = new QuickFind(pairs);
        for (int i = 0; i < pairs.length; i += 2) {
            uf.union(pairs[i], pairs[i+1]);
        }
        
        for (Iterator it = uf.iterator(); it.hasNext();) {
            System.out.print(it.next());
            System.out.print(' ');
        }
        System.out.println();
    }

    
}
