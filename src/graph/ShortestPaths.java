package graph;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;


public class ShortestPaths {
    
    public ShortestPaths(String relPath) {
        WeightedGraph<Integer> graph = new WGraphImpl<>();
        FileReader fr;
        try {
            fr = new FileReader( relPath );
            BufferedReader br = new BufferedReader( fr );
            String line;
            while( ( line = br.readLine() ) != null ) {
                String[] split = line.trim().split( "(\\s)+" );
                int u = Integer.parseInt(split[0]);
                for (int j = 1; j < split.length; j++) {
                    String[] sp2 = split[j].split(",");
                    int v = Integer.parseInt(sp2[0]);
                    int d = Integer.parseInt(sp2[1]);
                    graph.link(u, v, d);
                }
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        System.out.println("Completed creating WGraph");
        Integer s = 1;
        
        for (Integer t : graph.V()) {
            System.out.print(t);
            System.out.print(':');
            System.out.print(graph.sp(s, t));
            System.out.print(' ');
        }
        System.out.println();
        
        Integer[] vertices = {7,37,59,82,99,115,133,165,188,197};
        Map<Integer, Integer> distances = new HashMap<>();
        for (Integer v : vertices) {
            distances.put(v, 1000000);
        }
        for (Integer t : graph.V()) {
            if (distances.keySet().contains(t)) {
                int d = graph.sp(s, t);
                System.out.println("Computed sp(" + s + ", " + t + ") = " + d);
                distances.replace(t, d);
            }
        }
        for (Integer i = 0; i < vertices.length; i++) {
            System.out.print(distances.get(vertices[i]));
            System.out.print(',');
        }
    }
    
    public static void main(String[] args) {
        String file = "resources/spaths.txt";
        ShortestPaths scc = new ShortestPaths(file);
    }
    
}
