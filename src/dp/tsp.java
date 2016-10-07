package dp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class tsp {
    
    private int N;

    private double calcDistance(Point from, Point to) {
        double d1 = from.xCoord - to.xCoord;
        double d2 = from.yCoord - from.yCoord;
        return Math.sqrt(d1 * d1 + d2 * d2);
    }
    
    public static class Point {
        double xCoord;
        double yCoord;

        public Point(double xCoord, double yCoord) {
            this.xCoord = xCoord;
            this.yCoord = yCoord;
        }
        
                
    }
    
    private Map<Integer, Point> points;
    
    private double[][] distances;
    
    private void init(String file) {
        FileReader fr;
        try {
            fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            this.N = Integer.parseInt(line);
            this.points = new HashMap<>(N);
            Integer c = 0;
            while ((line = br.readLine()) != null) {
                String[] split = line.trim().split("(\\s)+");
                Point p = new Point(
                        Double.parseDouble(split[0]), 
                        Double.parseDouble(split[1]));
                this.points.put(c++, p);
            }
            this.distances = new double[N][N];
            for (int cFrom = 0; cFrom < N; cFrom++) {
                for (int cTo = 0; cTo < N; cTo++) {
                    if (cFrom == cTo) {
                        distances[cFrom][cTo] = 0.0;
                    } else {
                        distances[cFrom][cTo] = this.calcDistance(this.points.get(cFrom), this.points.get(cTo));
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        String file = "resources/tsp.txt";
        tsp t = new tsp();
        t.init(file);
    }
}
