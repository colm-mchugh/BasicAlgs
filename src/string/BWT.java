package string;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import sort.QuickSorter;

/**
 * BWT (Burrows Wheeler Transform)
 * 
 * transform - Apply Burrows Wheeler Transform to a string
 * reverse - Reverse a Burrows Wheeler Transformed string
 * @author administrator
 */
public class BWT {
   
    public static String transform(String p) {
        Rotation[] rotations = new Rotation[p.length()];
        
        for (int i = 0; i < p.length(); i++) {
            rotations[i] = new Rotation(p, i);
        }
        QuickSorter sorter = new QuickSorter();
        sorter.sort(rotations);
        
        StringBuilder bwText = new StringBuilder(p.length());
        for (int i = 0; i < rotations.length; i++) {
            bwText.append(rotations[i].charAt(p.length() - 1));
        }
        
        return bwText.toString();
    }

    private static void addSymbol(Symbol[] syms, Map<Character, Integer> freqs, char c, int i) {
        int occurence = 1;
        if (freqs.containsKey(c)) {
            occurence = freqs.get(c) + 1;
        }
        freqs.put(c, occurence);
        syms[i] = new Symbol(c, occurence);
    }
    
    private static class Symbol {
        char c;
        int occurence;

        public Symbol(char c, int occurence) {
            this.c = c;
            this.occurence = occurence;
        }

        @Override
        public String toString() {
            return c +  "(" + occurence + ')' ;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 89 * hash + this.c;
            hash = 89 * hash + this.occurence;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Symbol other = (Symbol) obj;
            if (this.c != other.c) {
                return false;
            }
            if (this.occurence != other.occurence) {
                return false;
            }
            return true;
        }
        
        
        
    }
    
    /**
     * Reverse a Burrows-wheeler transformed String
     * @param bwt
     * @return 
     */
    public static String reverse(String bwt) {
        char[] s_bwt = bwt.toCharArray();
        Arrays.sort(s_bwt);
        
        // Build the first and last columns of the Burrows Wheeler matrix
        Symbol[] first_col = new Symbol[bwt.length()];
        Symbol[] last_col = new Symbol[bwt.length()];
        
        // For keeping track of character occurences 
        Map<Character, Integer> first_freqs = new HashMap<>();
        Map<Character, Integer> last_freqs = new HashMap<>();
        
        // for each character in transform, and sorted transform,
        // add a symbol with the appropriate occurence to the relevant column.
        for (int i = 0; i < bwt.length(); i++) {
            addSymbol(first_col, first_freqs, s_bwt[i], i);
            addSymbol(last_col, last_freqs, bwt.charAt(i), i);
        }
        
        // Build the first-last dictionary; this maps the ith symbol in the
        // first column to the ith symbol in the last column
        Map<Symbol, Symbol> firstLastD = new HashMap<>();
        for (int i = 0; i < first_col.length; i++) {
            firstLastD.put(first_col[i], last_col[i]);
        }
        
        // Recreate the original string from the last character 
        Symbol nextSym = first_col[0];
        StringBuilder sb = new StringBuilder(bwt.length());
        sb.insert(0, nextSym.c);
        for (nextSym = firstLastD.get(nextSym); nextSym.c != '$'; nextSym = firstLastD.get(nextSym)) {
            sb.insert(0, nextSym.c);
        }
        
        return sb.toString();
    }
}
