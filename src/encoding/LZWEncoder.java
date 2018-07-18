package encoding;

import java.util.ArrayList;
import java.util.List;
import string.Trie;

public class LZWEncoder {
    
    private final Trie<Integer> codes = new Trie<>();
    private int next_code = 1;
    
    public LZWEncoder(char[] alphabet) {
        for(char c : alphabet) {
            codes.add(c, next_code++);
        }
    }
    
    public List<Integer> encode(String text) {
        List<Integer> encoding = new ArrayList<>();
        int i = 0;
        while (i < text.length()) {
            String prefix = codes.longestPrefixOf(text, i);
            encoding.add(codes.lookup(prefix));
            if (i + prefix.length() < text.length()) {
                codes.add(prefix + text.charAt(i + prefix.length()), next_code++);
            }
            i += prefix.length();
        }
        return encoding;
    }
    
}
