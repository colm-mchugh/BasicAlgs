package graph;


import java.io.BufferedReader;
import java.io.FileReader;


public class StrongConnectedComponents {
    
    private StrongCC cc;
    
     public StrongConnectedComponents(String relPath) {
        DGraphImpl graph = new DGraphImpl<>();
        FileReader fr;
        try {
            fr = new FileReader( relPath );
            BufferedReader br = new BufferedReader( fr );
            String line;
            while( ( line = br.readLine() ) != null ) {
                String[] split = line.trim().split( "(\\s)+" );
                int u = Integer.parseInt(split[0]);
                int v = Integer.parseInt(split[1]);
                graph.add(u, v);
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        System.out.println("Completed creating DiGraph");
        this.cc = new StrongCC(graph);
        System.out.println("Completed computing CCs");
        //this.cc.print();
    }
    
   
    public static void main(String[] args) {
        String file = "resources/SCC.txt";
        StrongConnectedComponents scc = new StrongConnectedComponents(file);
        scc.cc.ccSizes();
    }
}
