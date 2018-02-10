
package dp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Calculate the Maximum Weighted Independent Set of a sequence of weights.
 * 
 * Given a sequence of weights W1, W2, .... WN, its Maximum Weighted Independent
 * Set (MWIS) is a subset G of the weights with the following properties:
 * 1) G contains no adjacent weights; if Wi is in G then Wi-1 and Wi+1 are not.
 * 2) The sum of G is the maximum for all possible independent sets G'
 * 
 * The MWIS can be expressed with the recurrence relation: 
 *      MWIS(i) = MAX( MWIS(i-1), MWIS(i-2) + Wi ) 
 * 
 * If Wi is not in the MWIS, then MWIS(i) is MWIS(i-1)
 * If Wi is in the MWIS, then MWIS(i) is Wi and MWIS(i - 2)
 * 
 * A recursive algorithm can be directly written from the recurrence relation. 
 * It is O(2^n), effectively being set generation. But the MWIS for a given 
 * weight Wi is a prefix of the MWIS for the weight Wi+1, so there are a linear
 * (N) amount of distinct independent sets. By using a memo to record the MWIS
 * for each subsequence of i weights, the MWIS for the subsequence with i + 1 
 * weights can be calculated in linear (O(N)) time.
 */
public class MWIS {
    
    static public Set<Integer> calc(List<Integer> weights) {
        Set<Integer> mwis = new HashSet<>();
        List<Integer> memo = new ArrayList<>(weights.size());
        memo.add(0); // MWIS for 0 weights = 0
        memo.add(weights.get(0)); // MWIS for the first weight = the first weight
        for (int i = 2; i <= weights.size(); i++) {
            // Use the memo (instead of recursion) to get the previous MWIS in 
            // order to determine whether the current weight is in this MWIS.
            int withI = weights.get(i - 1) + memo.get(i - 2);
            int withoutI = memo.get(i - 1);
            memo.add(Integer.max(withI, withoutI));
        }
        // Reconstruct the MWIS from the memo of MWISs for each subsequence.
        for (int i = weights.size(); i >= 2;) {
            // If the MWIS that includes weight i is greater than the MWIS for
            // the first (i - 1) weights, then Weight i is in the MWIS, and 
            // weight i - 1 is not, so skip past it.
            if (weights.get(i - 1) + memo.get(i - 2) > memo.get(i - 1)) {
                mwis.add(i);
                i -= 2;
            } else {
                // weight i is not in the MWIS.
                i--;
            }
        }
        return mwis;
    }
}
