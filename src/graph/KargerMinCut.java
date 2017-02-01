package graph;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Calculate the minimum cut of a given graph.
 */ 
public class KargerMinCut {

    private Graph<Integer> graph;

    public void print() {
        for (Integer vertex : this.graph.V()) {
            System.out.print(vertex + ": ");
            for (Integer connection : this.graph.connections(vertex)) {
                System.out.print(connection);
                System.out.print(' ');
            }
            System.out.println();
        }
    }
    
    /**
     * The probability of an invocation of Graph.makeCut() returning a
     * minimum cut is low: 1/N*N, where N is the number of vertices.
     * 
     * Therefore this algorithm performs N*N invocations of Graph.makeCut() 
     * to  bring the probability of finding a minimum cut to 1.
     * @return 
     */ 
    public Graph.GraphCut<Integer> minCut() {
        Graph.GraphCut<Integer> smallestCut = new Graph.GraphCut<>(null, null, Integer.MAX_VALUE);
        int trialNo = 0;
        int trials = graph.numVertices() * graph.numVertices();
        for (int i = 0; i < trials; i++) {
            Graph.GraphCut<Integer> trial = graph.makeCut(); 
            if (trial.compareTo(smallestCut) < 0) {
                smallestCut = trial;
                trialNo = i;
            }
            if (i > 0 && i % 100 == 0) {
                System.out.println("Finished trial " + i + " of " + trials);
            }
        }
        System.out.println("Min value " + smallestCut.crossings + " was found in trial " + trialNo);
        return smallestCut;
    }
    
    public KargerMinCut(String relPath) {
        this.graph = new UGraphMapImpl<>();
        FileReader fr;
        try {
            fr = new FileReader( relPath );
            BufferedReader br = new BufferedReader( fr );
            String line;
            while( ( line = br.readLine() ) != null ) {
                String[] split = line.trim().split( "(\\s)+" );
                int nextVertex = Integer.parseInt(split[0]);
                for(int i = 1; i < split.length; i++) {
                    this.graph.add(nextVertex, Integer.parseInt(split[i]));
                }      
            }
        } catch ( IOException | NumberFormatException e ) {
            e.printStackTrace();
        }
    }

    
}
