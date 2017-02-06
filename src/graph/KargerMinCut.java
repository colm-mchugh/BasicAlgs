package graph;

/**
 * Calculate the minimum cut of a given graph.
 */ 
public class KargerMinCut<T> {

    /**
     * Return the minimum cut of the given graph.
     * 
     * The probability of an invocation of Graph.makeCut() returning a minimum
     * cut is low: 1/N*N, where N is the number of vertices. Therefore, this
     * algorithm performs N*N invocations of Graph.makeCut() to  bring the 
     * probability of finding a minimum cut to 1.
     * 
     * @param graph
     * @return 
     */ 
    public Graph.GraphCut<T> minCut(Graph<T> graph) {
        Graph.GraphCut<T> smallestCut = new Graph.GraphCut<>(null, null, Integer.MAX_VALUE);
        int trialNo = 0;
        int trials = graph.numVertices() * graph.numVertices();
        for (int i = 0; i < trials; i++) {
            Graph.GraphCut<T> trial = graph.makeCut(); 
            if (trial.crossings < smallestCut.crossings) {
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
       
}
