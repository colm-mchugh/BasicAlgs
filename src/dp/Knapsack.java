package dp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public abstract class Knapsack {
    
    public static class Item implements Comparable<Item> {

        public int value;
        public int weight;

        public Item(int value, int weight) {
            this.weight = weight;
            this.value = value;
        }

        @Override
        public String toString() {
            return "{" + "value=" + value + ", weight=" + weight + '}';
        }

        @Override
        public int compareTo(Knapsack.Item o) {
            if (weight > o.weight) {
                return 1;
            } else if (weight < o.weight) {
                return -1;
            }
            return 0;
        }

    }
    
    protected int knapSackWeight;
    protected List<Item> items;

    protected int[] readData(String file) {
        int[] data = null;
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            String[] firstLine = line.trim().split("(\\s)+");
            knapSackWeight = Integer.parseInt(firstLine[0]);
            int numItems = Integer.parseInt(firstLine[1]);
            data = new int[numItems * 2];
            for (int i = 0; (line = br.readLine()) != null; i += 2) {
                String[] itemData = line.trim().split("(\\s)+");
                data[i] = Integer.parseInt(itemData[0]);
                data[i + 1] = Integer.parseInt(itemData[1]);
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println(e.getMessage());
        }
        return data;
    }
    
    public abstract int knapsack();
    
    public void setKnapSackWeight(int knapSackWeight) {
        this.knapSackWeight = knapSackWeight;
    }
    
    public int size() {
        return this.items.size();
    }
    
    public int weight() {
        return this.knapSackWeight;
    }
}
