package graph;

import java.util.List;
import org.junit.Test;

public class TopSortTest {
    
    @Test
    public void test1() {
        DGraphImpl<Character> graph = new DGraphImpl<>();
        graph.add('s', 'w');
        graph.add('s', 'v');
        graph.add('v', 'w');
        graph.add('v', 't');
        graph.add('v', 's');
        graph.add('w', 't');
            
        TopologicalSort<Character> topSorter = new TopologicalSort<>();
        List<Character> graphOrdering = topSorter.sort(graph);
        int sIndex = Integer.MAX_VALUE;
        int tIndex = Integer.MAX_VALUE;
        int wIndex = Integer.MAX_VALUE;
        for (int i = 0; i < graphOrdering.size(); i++) {
            Character c = graphOrdering.get(i);
            System.out.print(c);
            System.out.print(' ');
            if (c == 's') {
                sIndex = i;
            } else if (c == 't') {
                tIndex = i;
            } else if (c == 'w') {
                wIndex = i;
            }
        }  
        assert sIndex < tIndex && sIndex < wIndex;
        assert wIndex < tIndex;
    }
    
    /*                
       A cyclic graph should produce an illegal argument exception:
    
       3 ->4-> 5->6-> 9-> 7
       ^  /    ^      ^  /
       | /     |      | /
       2       5      8
    
    */
    @Test(expected = IllegalArgumentException.class)
    public void test2() {
        DGraphImpl<Integer> g = new DGraphImpl<>();
        g.add(9, 7);
        g.add(8, 9);
        g.add(7, 8);
        g.add(6, 9);
        g.add(6, 1);
        g.add(5, 6);
        g.add(4, 5);
        g.add(4, 2);
        g.add(3, 4);
        g.add(2, 3);
        g.add(1, 5);

        
        TopologicalSort<Integer> topSorter = new TopologicalSort<>();
        topSorter.sort(g);
        System.out.println("This will never be printed.");
    }
}
