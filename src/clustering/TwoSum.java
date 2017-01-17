package clustering;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * The two sum problem determines whether or not there are two numbers in a
 * given array that sum to a given target.
 *
 */
public class TwoSum {

    private final Map<Long, Integer> arrHash = new HashMap<>();
    
    /**
     * Constructor: put all the numbers of the array into a hashtable
     * The hashtable enables calculation of a two sum target in O(N) time
     * (see hasTwoSumFor method)
     * 
     * @param ar the array of numbers for two sum calculations
     */
    public TwoSum(Long[] ar) {
        for (Long l : ar) {
            arrHash.put(l, 1);
        }
    }

    /**
     * The two sum problem determines whether or not there are two numbers in a
     * given array that sum to a given target.
     *
     * @param target
     * @return true if there are two numbers in ar s.t. ar[i] + ar[j] = target
     * (i != j)
     */
    public boolean hasTwoSumFor(long target) {
        // For each number x in the hash, check if (target -x) is in the hash.
        // If it is, then the array has two numbers that sum to target.
        // The overall running time of this algorithm is O(N) to create the hash
        // and O(N) to traverse the hash, giving O(N) overall.
        for (Long x : arrHash.keySet()) {
            if (arrHash.get(x) == 1) {
                Integer y = arrHash.get(target - x);
                if ((y != null) && (y == 1)) {
                    return true;
                }
            }
        }
        return false;
    }

}
