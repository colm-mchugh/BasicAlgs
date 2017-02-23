package string;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SuffixArray {

    public static int[] SortCharacters(String s) {
        // freqs gives the frequency of each character in bwt. 
        // freqs also gives the set of distinct characters in bwt.
        Map<Character, Integer> freqs = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            int occurence = 1;
            char c = s.charAt(i);
            if (freqs.containsKey(c)) {
                occurence = freqs.get(c) + 1;
            }
            freqs.put(c, occurence);
        }

        char[] distinct_chars = new char[freqs.keySet().size()];
        int i = 0;
        for (char c : freqs.keySet()) {
            distinct_chars[i++] = c;
        }
        // note that distinct_chars is at least s.length()/freqs.keySet().size() times smaller than s
        Arrays.sort(distinct_chars);
        
        // Replace each mapping in freqs with the last index
        for (i = 1; i < distinct_chars.length; i++) {
            char c = distinct_chars[i];
            int last_idx = freqs.get(c) + freqs.get(distinct_chars[i-1]);
            freqs.put(c, last_idx);
        }
        
        // Determine the ordering of each character S(i) in the sorted array:
        int[] order = new int[s.length()];
        for (i = s.length() - 1; i >= 0; i--) {
            char c = s.charAt(i);
            int next_idx = freqs.get(c) - 1;
            order[next_idx] = i;
            freqs.put(c, next_idx);
        }
        return order;
    }

    public static int[] ComputeCharClasses(String s, int[] order) {
        int[] classes = new int[s.length()];
        classes[0] = 0;
        for (int i = 1; i < classes.length; i++) {
            if (s.charAt(order[i]) == s.charAt(order[i-1])) {
                classes[order[i]] = classes[order[i-1]];
            } else {
                classes[order[i]] = classes[order[i-1]] + 1;
            }
        }
        return classes;
    }
    
    public static int[] SortDoubled(String s, int L, int[] order, int[] classes) {
        int[] count = new int[s.length()];
        int N = s.length();
        int[] newOrder = new int[N];
        for (int i = 0; i < N; i++) {
            count[classes[i]] = count[classes[i]] + 1;
        }
        for (int i = 1; i < N; i++) {
            count[i] = count[i] + count[i-1];
        }
        for (int i = N - 1; i >= 0; i--) {
            int start = (order[i] - L + N) % N;
            int cl = classes[start];
            count[cl] = count[cl] - 1;
            newOrder[count[cl]] = start;
        }
        return newOrder;
    }
    
    public static int[] UpdateClasses(int[] newOrder, int[] classes, int L) {
        int N = newOrder.length;
        int[] newClasses = new int[N];
        newClasses[newOrder[0]] = 0;
        for (int i = 1; i < N; i++) {
            int cur = newOrder[i];
            int prev = newOrder[i-1];
            int mid = (cur + L) % N;
            int midPrev = (prev + L) % N;
            if ((classes[cur] == classes[prev]) && (classes[mid] == classes[midPrev])) {
                newClasses[cur] = newClasses[prev];
            } else {
                newClasses[cur] = newClasses[prev] + 1;
            }
        }
        return newClasses;
    }
    
    public static int[] Build(String s) {
        int[] order = SortCharacters(s);
        int[] classes = ComputeCharClasses(s, order);
        for (int L = 1; L < s.length(); L *= 2) {
            order = SortDoubled(s, L, order, classes);
            classes = UpdateClasses(order, classes, L);
        }
        return order;
    }
}
