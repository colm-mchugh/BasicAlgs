package approx;

import dp.TSPer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *  TSP using nearest neighbor heuristic.
 * 
 *  Tour starts at a given origin. Repeatedly visit the closest point that
 *  has not been visited yet. TSP() builds a list of the points visited in
 *  the NN tour. tourDistance() computes the actual distance of the given 
 *  tour.
 */
public class TSP_NN {
    Set<TSPer.Point> points; 
    TSPer.Point origin;
    List<TSPer.Point> tour;
    
    public TSP_NN(String file) {
        FileReader fr;
        origin = null;
        try {
            fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            int N = Integer.parseInt(line);
            points = new HashSet<>(N);
            while ((line = br.readLine()) != null) {
                String[] split = line.trim().split("(\\s)+");
                float x = Float.parseFloat(split[1]);
                float y = Float.parseFloat(split[2]);
                TSPer.Point p = new TSPer.Point(x, y);
                points.add(p);
                origin = (origin != null ? origin : p);
            }
            
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }
      
    List<TSPer.Point> TSP() {
        tour = new ArrayList<>(points.size());
        TSPer.Point nextPoint = origin;
        while (!points.isEmpty()) {
            points.remove(nextPoint);
            tour.add(nextPoint);
            float nearest = Float.MAX_VALUE;
            TSPer.Point nearestP = null;
            for (TSPer.Point p : points) {
                float proximity = eucSquare(nextPoint, p);
                if (proximity < nearest) {
                    nearest = proximity;
                    nearestP = p;
                }
            }
            nextPoint = nearestP;
        }
        return tour;
    }
    
    Float tourDistance(List<TSPer.Point> aTour) {
        float distance = 0;
        for (int i = 0; i < aTour.size(); i++) {
            int nextI = (i + 1) % aTour.size();
            distance += eucDistance(aTour.get(i), aTour.get(nextI));
        }
        return distance;
    }
    
    private float eucDistance(TSPer.Point from, TSPer.Point to) {
        return (float) Math.sqrt(eucSquare(from, to));
    }
    
    private float eucSquare(TSPer.Point from, TSPer.Point to) {
        float d1 = from.getxCoord() - to.getxCoord();
        float d2 = from.getyCoord() - to.getyCoord();
        return (d1 * d1 + d2 * d2);
    }
}
