package graph;

import java.util.List;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

public class CCTarjanTest {
    
    @Test
    public void testCCTarjan() {
        Graph<Character> g = new DGraphImpl<>();
        char[] links = { 'A','B', 'B','C', 'C','A', 'D','B', 'D','C', 'D','F', 'F','D', 'F','E', 
                            'E','C', 'E','G', 'G','E', 'H','F', 'H','G', 'H','H', };
        for (int i = 0; i < links.length; i += 2) {
            g.add(links[i], links[i+1]);
        }
        CCTarjan<Character> Tarjaner = new CCTarjan<>(g);
        Map<Character, List<Character>> sccs = Tarjaner.getComponents();
        assert sccs.size() == 4;
        
    }
    
}
