package dp;

import java.util.ArrayList;
import java.util.List;

public abstract class Knapsack {
    
    public static class Item implements Comparable<Item> {

        public int value;
        public int weight;
        public boolean decision;
        public int id;

        public Item(int value, int weight, int id) {
            this.weight = weight;
            this.value = value;
            this.decision = false;
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

    public Knapsack(int knapSackWeight, int[] data) {
        this.knapSackCapacity = knapSackWeight;
        items = new ArrayList<>(data.length / 2);
        for (int i = 0; i < data.length / 2; i++) {
            items.add(new Knapsack.Item(data[2 * i], data[2 * i + 1], i + 1));
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
            decisionVec[item.id - 1] = (item.decision ? 1 : 0);                   
        }
        return decisionVec;
    }
}
