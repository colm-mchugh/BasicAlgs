package graph;

import org.junit.Test;

public class GraphColoringTest {

    @Test
    public void testColoring() {
        int[][] links = {{1, 2, 1, 3, 1, 4, 2, 5, 4, 6,
            3, 7, 3, 8, 5, 6, 5, 8, 6, 7,
            9, 7, 10, 8,
            2, 9, 9, 10, 10, 4,},
        // Graph with clique of size 4 (nodes 3,4,5,6) => 4 colors required
        {1, 3, 1, 5, 2, 4, 2, 8, 2, 3, 3, 4, 3, 6, 3, 5, 4, 5,
            4, 6, 5, 6, 5, 7, 5, 9, 6, 7, 6, 8, 7, 8, 7, 9, 8, 9},
        // Hub & spoke: 2 colors
        {1, 2, 1, 3, 1, 4, 1, 5, 1, 6, 1, 7, 1, 8, 1, 9},
        // Hub & spoke, but with triangles
        {1, 2, 1, 3, 2,3, 1, 4, 1, 5, 4, 5, 1, 6, 1, 7, 6, 7, 1, 8, 1, 9, 8, 9},};
        int[] expected = {3, 4, 2, 3,};
        int c = 0;
        for (int[] link : links) {
            Graph<Integer> g = new UGraphMapImpl<>();
            for (int i = 0; i < link.length; i += 2) {
                g.add(link[i], link[i + 1]);
            }
            GraphColoring<Integer> gc = new GraphColoring<>();
            assert gc.color(g, expected[c]);
            assert !gc.color(g, expected[c] - 1);
            c++;
        }

    }

}
