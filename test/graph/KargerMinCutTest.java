package graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.junit.Test;

public class KargerMinCutTest {

    /**
     * Test of minCut method, of class KargerMinCut.
     */
    @Test
    public void testMinCut() {
        Graph<Integer> graph = getGraph("resources/kargerMinCut.txt");
        KargerMinCut kmc = new KargerMinCut();
        for (Integer vertex : graph.V()) {
            System.out.print(vertex + ": ");
            for (Integer connection : graph.connections(vertex)) {
                System.out.print(connection);
                System.out.print(' ');
            }
            System.out.println();
        }
        GraphCut<Integer> minCut = kmc.minCut(graph);
        int minCutCrossings = minCut.crossings();
        System.out.println("The minCut is " +  minCutCrossings);
        assert minCutCrossings == 17;
    }
    
    private Graph<Integer> getGraph(String relPath) {
        UGraphMapImpl<Integer> graph = new UGraphMapImpl<>();
        FileReader fr;
        try {
            fr = new FileReader( relPath );
            BufferedReader br = new BufferedReader( fr );
            String line;
            while( ( line = br.readLine() ) != null ) {
                String[] split = line.trim().split( "(\\s)+" );
                int nextVertex = Integer.parseInt(split[0]);
                for(int i = 1; i < split.length; i++) {
                    graph.add(nextVertex, Integer.parseInt(split[i]));
                }      
            }
        } catch ( IOException | NumberFormatException e ) {
            e.printStackTrace();
        }
        return graph;
    }

    
    
}
