package clustering;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class TwoSum {

    public static long twoSum(Long[] ar, long lb, long ub) {
        Map<Long, Integer> numberSet = new HashMap<>();
        Integer times = 1;
        for (Long l : ar) {
            times = 1;
            if (numberSet.containsKey(l)) {
                times = numberSet.get(l);
            }
            numberSet.put(l, times);
        }
        long t = 0;
        for (long s = lb; s <= ub; s++) {
            for (Long x : numberSet.keySet()) {
                if (numberSet.get(x) == 1) {
                    Integer y = numberSet.get(s - x);
                    if ((y != null) && (y == 1)) {
                        t++;
                        break;
                    }
                }
            }
            if (s % 10 == 0) {
                System.out.println("Completed " + s + ", t=" + t);
            }
        }
        System.out.println("t=" + t);
        return t;
    }

    public static void main(String[] args) {
        Map<Long, Integer> numberSet = new HashMap<>();
        FileReader fr;
        String relPath = "/Users/Colm/Downloads/2sum.txt";
        try {
            fr = new FileReader(relPath);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.trim().split("(\\s)+");
                Long u = Long.parseLong(split[0]);
                Integer times = 1;
                if (numberSet.containsKey(u)) {
                    times = numberSet.get(u);
                }
                numberSet.put(u, times);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        int t = 0;
        int lb = -10000, ub = 10000;
        for (int s = lb; s <= ub; s++) {
            for (Long x : numberSet.keySet()) {
                if (numberSet.get(x) == 1) {
                    Integer y = numberSet.get(s - x);
                    if ((y != null) && (y == 1)) {
                        t++;
                        break;
                    }
                }
            }
            if (s % 10 == 0) {
                System.out.println("Completed " + s + ", t=" + t);
            }
        }
        System.out.println("t=" + t);
    }

}
