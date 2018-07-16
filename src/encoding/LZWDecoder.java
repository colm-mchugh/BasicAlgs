package encoding;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LZWDecoder {
    
    private final Map<Integer, String> substrs = new HashMap<>();
    private int next_code = 1;
    
    public LZWDecoder(char[] alphabet) {
        StringBuilder sb = new StringBuilder();
        for(char c : alphabet) {
            sb.append(c);
            substrs.put(next_code++, sb.toString());
            sb.deleteCharAt(0);
        }
    }
    
    public String decode(List<Integer> encoding) {
        StringBuilder sb = new StringBuilder(encoding.size());
        int curr = encoding.get(0);
        sb.append(substrs.get(curr));
        int prev;
        for(int i = 1; i < encoding.size(); i++) {
            prev = curr;
            curr = encoding.get(i);
            if (substrs.containsKey(curr)) {
                String s = substrs.get(curr);
                sb.append(s);
                substrs.put(next_code++, substrs.get(prev) + s.charAt(0));
            } else {
                // TODO: substrs does not have a mapping for curr
            }
        }
        return sb.toString();
    }
}
