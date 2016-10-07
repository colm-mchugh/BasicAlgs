package graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Apsp {
    
    public static void computeApsps(String file) {
        WeightedGraph<Integer> graph = new WGraphImpl<>();
        FileReader fr;
        try {
            fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] split = line.trim().split("(\\s)+");
                int u = Integer.parseInt(split[0]);
                int v = Integer.parseInt(split[1]);
                int d = Integer.parseInt(split[2]);
                graph.link(u, v, d);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        graph.apsp();
        
    }
    
    public static void main(String[] args) {
        String file = "resources/g3.txt";
        computeApsps(file);
    }
}
