package dp;

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

    public Knapsack(int knapSackWeight) {
        this.knapSackWeight = knapSackWeight;
    }
       
    public abstract int knapsack();
    
    public int size() {
        return this.items.size();
    }
    
    public int weight() {
        return this.knapSackWeight;
    }
}
