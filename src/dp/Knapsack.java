package dp;

import java.util.ArrayList;
import java.util.List;

public abstract class Knapsack {
    
    public static class Item implements Comparable<Item> {

        public int value;
        public int weight;
        public boolean isLive;
        public int id;

        public Item(int value, int weight, int id) {
            this.weight = weight;
            this.value = value;
            this.isLive = false;
            this.id = id;
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
    
    protected int knapSackCapacity;
    protected List<Item> items;

    public Knapsack(int knapSackWeight, int[] valueWeightPairs) {
        this.knapSackCapacity = knapSackWeight;
        items = new ArrayList<>(valueWeightPairs.length / 2);
        for (int i = 0; i < valueWeightPairs.length / 2; i++) {
            int Vi = valueWeightPairs[2 * i];
            int Wi = valueWeightPairs[2 * i + 1];
            items.add(new Knapsack.Item(Vi, Wi, i + 1));
        }
    }
       
    public abstract int knapsack();
    
    public int size() {
        return this.items.size();
    }
    
    public int capacity() {
        return this.knapSackCapacity;
    }
    
    public int[] decisionVector() {
        int[] decisionVec = new int[items.size()];
        for (Item item : items) {
            decisionVec[item.id - 1] = (item.isLive ? 1 : 0);                   
        }
        return decisionVec;
    }
}
