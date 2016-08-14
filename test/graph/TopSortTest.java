package graph;

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
        //graph.add('t' , 's');
            
        TopologicalSort<Character> sorter = new TopologicalSort<>(graph);
        for (Character i = sorter.next(); i != null; i = sorter.next()) {
            System.out.println(i);
        }
           
    }
    
    @Test
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

        
        TopologicalSort<Integer> s2 = new TopologicalSort<>(g);
        for (Integer i = s2.next(); i != null; i = s2.next()) {
            System.out.print(i);
            System.out.print(' ');
        }
        System.out.println();
    }
}
