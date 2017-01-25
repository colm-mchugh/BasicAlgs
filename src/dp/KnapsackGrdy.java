package dp;

import sort.QuickSorter;

public class KnapsackGrdy extends Knapsack  {

    public static class Item extends Knapsack.Item  {

        public Item(int value, int weight) {
            super(value, weight);
        }

        public int compareTo(Knapsack.Item o) {
            float myRatio = value / (float) weight;
            float itsRatio = o.value / (float) o.weight;
            if (myRatio > itsRatio) {
                return -1;
            } else if (myRatio < itsRatio) {
                return 1;
            }
            return 0;
        }

    }

    private Item[] items;

    public Item[] initItems(int[] data) {
        items = new Item[data.length / 2];
        for (int i = 0; i < items.length; i++) {
            items[i] = (new Item(data[2 * i], data[2 * i + 1]));
        }
        QuickSorter qs = new QuickSorter();
        qs.sort(items);
        return items;
    }

    public void setKnapSackWeight(int knapSackWeight) {
        this.knapSackWeight = knapSackWeight;
    }
    
    public int knapsack() {
        int v = 0;
        int w = 0;
        for (int i = 0; i < items.length && w + items[i].weight < this.knapSackWeight; i++) {
            w += items[i].weight;
            v += items[i].value;
        }
        return v;
    }
}
