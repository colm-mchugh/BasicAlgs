package graph;


import java.io.BufferedReader;
import java.io.FileReader;


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
    
    private int minCut() {
        int rv = Integer.MAX_VALUE;
        int trialNo = 0;
        int trials = graph.numVertices() * graph.numVertices();
        for (int i = 0; i < trials; i++) {
            int trial = graph.minCut();
            if (trial < rv) {
                rv = trial;
                trialNo = i;
            }
            if (i > 0 && i % 100 == 0) {
                System.out.println("Finished trial " + i + " of " + trials);
            }
        }
        System.out.println("Min value " + rv + " found in trial " + trialNo);
        return rv;
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
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        String file = "resources/graph.txt";
        KargerMinCut kmc = new KargerMinCut(file);
        kmc.print();
        
        int minCut = kmc.minCut();
        System.out.println("The minCut is " +  minCut);
    }

    
    
}
