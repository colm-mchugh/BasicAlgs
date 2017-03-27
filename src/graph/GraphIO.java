
package graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GraphIO {
    
    public static WeightedGraph<Integer> readWeightedGraphDirected(String file) {
        return readWeightedGraph(file, new WeightedGraphDirected<>());
    }

    public static WeightedGraph<Integer> readWeightedGraphUndirected(String file) {
        return readWeightedGraph(file, new WeightedGraphUndirected<>());
    }
    
    public static void populateGraph(WeightedGraph<Integer> graph, int[] links) {
        for (int i = 0; i < links.length; i += 3) {
            graph.link(links[i], links[i+1], links[i+2]);
        }
    }
    
    private static WeightedGraph<Integer> readWeightedGraph(String file, WeightedGraph<Integer> graph) {
        FileReader fr;
        try {
            fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] split = line.trim().split("(\\s)+");
                int uVertex = Integer.parseInt(split[0]);
                int vVertex = Integer.parseInt(split[1]);
                int edgeWeight = Integer.parseInt(split[2]);
                graph.link(uVertex, vVertex, edgeWeight);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return graph;
    }

}
