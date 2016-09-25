package dp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class knapsackSolver {

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
        public int compareTo(Item o) {
            if (weight > o.weight) {
                return 1;
            } else {
                if (weight < o.weight) {
                    return -1;
                }
            }
            return 0;
        }
        
    }

    private int knapSackWeight;
    private List<Item> items;
    private int[][] memo;
    private Map<Integer, Map<Integer, Integer>> recMemo;
    
    public List<Item> readItems(String file) {
        items = null;
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            String[] firstLine = line.trim().split("(\\s)+");
            knapSackWeight = Integer.parseInt(firstLine[0]);
            int numItems = Integer.parseInt(firstLine[1]);
            items = new ArrayList<Item>(numItems);
            while ((line = br.readLine()) != null) {
                String[] itemData = line.trim().split("(\\s)+");
                items.add(new Item(
                        Integer.parseInt(itemData[0]),
                        Integer.parseInt(itemData[1])));
            }
            recMemo = new HashMap<>();
            for (int i = -1; i < items.size(); i++) {
                recMemo.put(i, new HashMap<>());
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println(e.getMessage());
        }
        return items;
    }
    
    public void printItems(PrintStream pw) {
        for (Item item : items) {
            pw.println(item);
        }
    }

    private void initMemo() {
        memo = new int[knapSackWeight + 1][items.size() + 1];
        for (int w = 0; w <= knapSackWeight; w++) {
            memo[w][0] = 0;
        }
    }
    
    public int calcMemo() {
        int rv = 0;
        this.initMemo();
        for (int i = 1; i <= items.size(); i++) {
            Item itemi = items.get(i-1);
            for (int w = 0; w <= knapSackWeight; w++) {
                int prevValue = memo[w][i-1];
                if (itemi.weight > w) {
                    // item i exceeds capacity w => retain previous value
                    memo[w][i] = prevValue;
                } else {
                    // Can include item i => set value to item i's value 
                    // plus the value of the bag with capacity w - (i's weight) for i-1 items
                    int withI = itemi.value + memo[w - itemi.weight][i-1];
                    memo[w][i] = Integer.max(prevValue, withI);
                }
            }
        }
        rv = memo[knapSackWeight][0];
        for (int i = 1; i <= items.size(); i++) {
            System.out.println("memo[" + knapSackWeight + "][" + i + "]=" + memo[knapSackWeight][i]);
            if (rv < memo[knapSackWeight][i]) {
                rv = memo[knapSackWeight][i];
            }
        }
        return rv;
    }
    
    private int knapsack (int i, int W) {
        if (i < 0) {
            return 0;
        }
        Item item = this.items.get(i);
        Integer prev = recMemo.get(i).get(W);
        if (prev != null) {
            return prev;
        }
        Integer prev1 = recMemo.get(i - 1).get(W);
        if (prev1 == null) {
            prev1 = knapsack(i - 1, W);
            recMemo.get(i - 1).put(W, prev1);
        } 
        if (item.weight > W) {
            return prev1;
        }
        Integer prev2 = recMemo.get(i - 1).get(W - item.weight);
        if (prev2 == null) {
            prev2 = knapsack(i - 1, W - item.weight);
            recMemo.get(i - 1).put(W - item.weight, prev2);
        } 
        Integer thisVal = Integer.max(prev1, item.value + prev2);
        recMemo.get(i).put(W, thisVal);
        return thisVal;
    }
    
    public int doit() {
        return knapsack(items.size() - 1, knapSackWeight);
    }
    
    public static void main(String[] args) {
        knapsackSolver ks = new knapsackSolver();
        ks.readItems("resources/knapsack2.txt");
        System.out.println(ks.doit());
    }
}
