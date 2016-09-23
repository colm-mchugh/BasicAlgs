package graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Prim {

    public Prim(String file) {
        WUGraphImpl<Integer> graph = new WUGraphImpl<>();
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
        System.out.println("MST cost =" + graph.mstCost());
    }

    public static void main(String[] args) {
        String file = "resources/edges.txt";
        Prim p = new Prim(file);
    }
}
