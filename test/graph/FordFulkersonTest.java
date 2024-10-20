package graph;

import org.junit.Test;

public class FordFulkersonTest {

    /* Calculate the maximum flow for some graphs. First graph is:
            0 -16-> 1, 0 -13-> 2, 1 -10-> 2, 2 -4-> 1, 1 -12-> 3, 2 -14-> 4,
            2 -9-> 3, 3 -7-> 4, 3 -20-> 5, 4 -4-> 5
     */
    int data[][] = {
        {4,1,12, 5,4,4, 2,1,8, 3,2,5, 6,5,3, 6,3,6, 7,5,7, 8,7,6, 9,8,10, 9,6,5},
        {4,1,12, 9,1,2, 5,4,4, 2,1,8, 3,2,5, 6,5,3, 6,3,6, 7,5,7, 8,7,6, 9,8,10, 9,6,5},
        {1, 0, 16, 2, 0, 13, 2, 1, 10, 1, 2, 4, 3, 1, 12, 4, 2, 14, 2, 3, 9, 3, 4, 7, 5, 3, 20, 5, 4, 4},
        {2, 1, 2, 3, 1, 13, 4, 2, 10, 4, 3, 5, 5, 2, 12, 7, 3, 6, 5, 4, 1, 6, 4, 1, 5, 6, 2, 7, 6, 3, 8, 5, 6, 8, 7, 2},
        {2, 1, 1, 3, 1, 100, 2, 3, 1, 3, 2, 100, 4, 2, 100, 4, 3, 1},
        {1, 0, 10, 2, 0, 10, 2, 1, 2, 4, 2, 9, 4, 1, 8, 3, 1, 4, 3, 4, 6, 5, 3, 10, 5, 4, 10},
        {1, 0, 15, 2, 0, 4, 3, 1, 12, 4, 2, 10, 2, 3, 3, 1, 4, 5, 5, 3, 7, 5, 4, 10},
        // A,B,3, A,D,3, B,C,4, C,A,3, C,D,1, C,E,2, D,E,2,D,F,6, E,G,1, F,G,9
        {2, 1, 3, 4, 1, 3, 3, 2, 4, 1, 3, 3, 4, 3, 1, 5, 3, 2, 5, 4, 2, 6, 4, 6, 7, 5, 1, 7, 6, 9},};

    int pts[] = {1, 9, 1, 9, 0, 5, 1, 8, 1, 4, 0, 5, 0, 5, 1, 7,}; // source and sink vertices for each graph.
    int maxF[] = {9, 11, 23, 6, 3, 19, 14, 5,};     // expected max flows

    private final static boolean DO_LOGGING = true;

    @Test
    public void testEdKarp() {
        testFlow(new FlowEdmndKarp<>());
    }

    @Test
    public void testPushRelabel() {
        testFlow(new FlowPushRelabel<>(DO_LOGGING));
    }

    private void testFlow(Flow<Integer> G) {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j += 3) {
                G.link(data[i][j], data[i][j + 1], data[i][j + 2]);
            }
            Flow.Max<Integer> maxflow = G.getMax(pts[i * 2], pts[i * 2 + 1]);
            assert maxflow.value == maxF[i];
            G.clear();
        }
    }

    @Test
    public void testPushRelabel1() {
        String data[] = {"V", "S", "1",
            "W", "S", "100",
            "V", "W", "1", 
            "T", "S", "1",
            "W", "V", "100",
            "T", "V", "100",
            "T", "W", "1",
        };
        FlowPushRelabel<String> G = new FlowPushRelabel<>(DO_LOGGING);
        for (int j = 0; j < data.length; j += 3) {
            G.link(data[j], data[j + 1], Integer.parseInt(data[j + 2]));
        }
        Flow.Max<String> flow = G.getMax("S", "T");
        assert (flow.value == 4);
    }

    @Test
    public void testPushRelabel2() {
        String edges[] = {"A", "B", "3",
            "A", "D", "3",
            "B", "C", "4",
            "C", "A", "3",
            "C", "D", "1",
            "C", "E", "2",
            "D", "E", "2",
            "D", "F", "6",
            "E", "G", "1",
            "F", "G", "9"};

        FlowPushRelabel<String> G = new FlowPushRelabel<>(DO_LOGGING);
        for (int j = 0; j < edges.length; j += 3) {
            G.link(edges[j + 1], edges[j], Integer.parseInt(edges[j + 2]));
        }
        Flow.Max<String> flow = G.getMax("A", "G");
        assert (flow.value == 5);
    }

    @Test
    public void testPushRelabel3() {
        String edges[] = {     
            "A", "S", "15",     
            "C", "S", "4",
            "T", "S", "3",
            "B", "A", "12",
            "D", "C", "10",
            "C", "B", "3",
            "A", "D", "5",
            "T", "B", "7",
            "T", "D", "10",};

        FlowPushRelabel<String> G = new FlowPushRelabel<>(DO_LOGGING);
        for (int j = 0; j < edges.length; j += 3) {
            G.link(edges[j], edges[j + 1], Integer.parseInt(edges[j + 2]));
        }
        Flow.Max<String> flow = G.getMax("S", "T");
        assert (flow.value == 17);
    }

    @Test
    public void testAugmentations() {
        /* Given this "pathological" flow:
            0 -> 1, cap=1000000
            0 -> 2, cap=1000000
            1 -> 2, cap=1
            1 -> 3, cap=1000000
            2 -> 3, cap=1000000
           The maximum function on a flow should have no more than |V| augmentations
           where |V| is the number of vertices in the flow.
         */
        Flow<Integer> G = new FlowEdmndKarp<>();
        // Note: G is the residual network of the "pathological" flow given above.
        G.link(1, 0, 1000000);
        G.link(2, 0, 1000000);
        G.link(2, 1, 1);
        G.link(3, 1, 1000000);
        G.link(3, 2, 1000000);

        Flow.Max<Integer> maxFlow = G.getMax(0, 3);
        assert maxFlow.value == 1000000 * 2;
        assert maxFlow.augmentations <= G.numVertices();
    }

    @Test
    public void bigTest() {
        int[] data = {1, 90, 10000, 97, 98, 9998, 95, 94, 9806, 94, 97, 9839, 96, 95, 9890, 93, 96, 9864, 91, 92, 9806, 94, 91, 9875, 92, 97, 9859, 92, 96, 9822, 97, 90, 9857, 93, 90, 9887, 97, 96, 9872, 90, 92, 9820, 1, 82, 9997, 89, 98, 9997, 87, 83, 1, 89, 83, 1, 88, 89, 1, 87, 82, 1, 89, 82, 9996, 85, 82, 1, 89, 85, 9996, 84, 86, 1, 83, 82, 9995, 84, 83, 1, 87, 84, 1, 82, 86, 1, 1, 74, 9993, 81, 98, 9992, 81, 74, 9857, 80, 77, 9871, 80, 74, 9861, 80, 78, 9880, 75, 81, 9899, 75, 76, 9844, 78, 77, 9868, 76, 81, 9896, 81, 79, 9882, 81, 77, 9862, 77, 76, 9839, 75, 80, 9828, 1, 66, 9990, 73, 98, 9987, 73, 69, 9985, 72, 70, 1, 68, 69, 9985, 69, 67, 9985, 73, 70, 9983, 73, 67, 1, 68, 73, 1, 71, 68, 9980, 73, 72, 9980, 66, 69, 1, 68, 67, 9977, 67, 71, 9975, 1, 58, 9975, 65, 98, 9972, 60, 62, 9829, 58, 65, 9878, 60, 63, 9837, 61, 59, 9840, 64, 65, 9882, 62, 58, 9855, 64, 58, 9822, 63, 61, 9882, 58, 63, 9853, 59, 62, 9871, 59, 64, 9813, 63, 62, 9866, 1, 50, 9971, 57, 98, 9968, 56, 52, 1, 56, 53, 9968, 57, 53, 9965, 56, 51, 9963, 56, 55, 9962, 54, 51, 1, 57, 52, 9959, 57, 51, 9958, 55, 57, 1, 50, 52, 1, 53, 51, 1, 50, 53, 1, 1, 42, 9955, 49, 98, 9955, 48, 47, 9815, 49, 44, 9834, 45, 47, 9875, 45, 48, 9902, 48, 43, 9904, 43, 47, 9897, 44, 48, 9857, 43, 44, 9841, 44, 46, 9864, 46, 42, 9808, 49, 43, 9884, 42, 45, 9847, 1, 34, 9955, 41, 98, 9954, 35, 38, 9953, 41, 34, 9951, 39, 37, 9948, 35, 39, 1, 37, 38, 9946, 34, 35, 9945, 35, 40, 9944, 41, 38, 9944, 39, 38, 9944, 34, 37, 1, 41, 39, 1, 39, 36, 1, 1, 26, 9943, 33, 98, 9941, 30, 28, 9903, 27, 32, 9893, 27, 30, 9875, 29, 28, 9853, 32, 26, 9861, 27, 28, 9872, 30, 29, 9902, 28, 33, 9850, 31, 30, 9810, 33, 29, 9860, 29, 27, 9872, 31, 32, 9837, 1, 18, 9938, 25, 98, 9937, 24, 23, 1, 23, 20, 1, 19, 25, 1, 23, 25, 9934, 23, 21, 1, 22, 20, 1, 19, 18, 9934, 24, 22, 9932, 20, 24, 9929, 19, 23, 9928, 25, 18, 1, 22, 21, 1, 1, 10, 9928, 17, 98, 9927, 12, 15, 9907, 10, 16, 9811, 11, 12, 9835, 10, 14, 9872, 11, 10, 9832, 12, 17, 9825, 14, 17, 9806, 16, 14, 9857, 17, 10, 9818, 12, 10, 9818, 17, 11, 9901, 17, 16, 9883, 1, 2, 9925, 9, 98, 9925, 2, 4, 9922, 2, 6, 1, 7, 6, 9919, 8, 2, 1, 8, 3, 9919, 8, 6, 9916, 9, 7, 1, 4, 5, 1, 8, 5, 1, 2, 3, 9913, 5, 9, 1, 6, 3, 9910,};
        Flow<Integer> G = new FlowEdmndKarp<>();
        for (int i = 0; i < data.length; i += 3) {
            G.link(data[i + 1], data[i], data[i + 2]);
        }

        long now = System.currentTimeMillis();
        Flow.Max<Integer> maxFlow = G.getMax(1, 98);
        long edKarpMillis = System.currentTimeMillis() - now;

        assert maxFlow.augmentations <= G.numVertices();
        int edKarpFlow = maxFlow.value;

        G = new FlowPushRelabel<>(!DO_LOGGING);
        for (int i = 0; i < data.length; i += 3) {
            G.link(data[i + 1], data[i], data[i + 2]);
        }

        now = System.currentTimeMillis();
        maxFlow = G.getMax(1, 98);
        long pushRelabelMillis = System.currentTimeMillis() - now;
        int pushRelabelFlow = maxFlow.value;

        assert (pushRelabelFlow == edKarpFlow);

        System.out.println("Time taken by EdKarp: " + edKarpMillis);
        System.out.println("Time taken by PushRelabel: " + pushRelabelMillis);
    }
}
