package graph;

import java.util.Map;
import org.junit.Test;

public class FlowBPMatcherTest {

    @Test
    public void testGetBestMatch() {
        String[][] prefs = {{"J1"}, {"J1", "J3"}, {"J2", "J3", "J4"}, {"J3"}, {"J3"}};
        String[] workers = {"W1", "W2", "W3", "W4", "W5"};
        FlowBPMatcher<String> matcher = new FlowBPMatcher<>("S", "T");
        for (int i = 0; i < workers.length; i++) {
            String worker = workers[i];
            String[] jobs = prefs[i];
            for (String job : jobs) {
                matcher.link(worker, job);
            }
        }
        Flow.Max<String> match = matcher.getBestMatch();
        assert match.value == 3;
    }

    @Test
    public void testMatchings() {
        int[][][] prefs = { {{100}, {300}, {200}, {300, 500}, {400}},
                            {{1, 2}, {1}, {2,3}, {3,4,5}, {5}},
                            {{6,7,9}, {6,7,9,10}, {8}, {8}, {6,8,9,10}}
        };
        int[] workers = {1, 2, 3, 4, 5};
        int[] matches = {5,5,4};
        
        for (int j = 0; j < prefs.length; j++) {
        FlowBPMatcher<Integer> matcher = new FlowBPMatcher<>(0, workers.length + 1);
        for (int i = 0; i < workers.length; i++) {
            int worker = workers[i];
            int[] jobs = prefs[j][i];
            for (int job : jobs) {
                matcher.link(worker, job);
            }
        }
        Flow.Max<Integer> match = matcher.getBestMatch();
        assert match.value == matches[j];
        }
    }
    
    @Test
    public void testCrewFlight1() {
        int[][] prefs = {{1,2,3},{2},{}};
        int[] crew = {1,2,3};
        FlowBPMatcher<Integer> matcher = new FlowBPMatcher<>(0, crew.length + 1);
        for (int i = 0; i < crew.length; i++) {
            int worker = crew[i];
            int[] jobs = prefs[i];
            for (int job : jobs) {
                matcher.link(worker, job);
            }
        }
        Flow.Max<Integer> match = matcher.getBestMatch();
        assert match.value == 2;
        Map<Integer, Integer> matchUp = matcher.getMatchings();
        System.out.println(matchUp.toString());
        assert matchUp.get(1) == 1;
        assert matchUp.get(2) == 2;
        assert !matchUp.containsKey(3);
    }
    
    @Test
    public void testCrewFlight2() {
        int[][] prefs = {{1,2},{1}};
        int[] crew = {100,200};
        FlowBPMatcher<Integer> matcher = new FlowBPMatcher<>(0, crew.length + 1);
        for (int i = 0; i < crew.length; i++) {
            int worker = crew[i];
            int[] jobs = prefs[i];
            for (int job : jobs) {
                matcher.link(worker, job);
            }
        }
        Flow.Max<Integer> match = matcher.getBestMatch();
        assert match.value == 2;
        Map<Integer, Integer> matchUp = matcher.getMatchings();
        assert matchUp.get(100) == 2;
        assert matchUp.get(200) == 1;
    }
    
    @Test
    public void testNumbers() {
        // Order the numbers [1..16] so that the sum of each successive pair is a square.
        // Can solve using bipartite matching; the preference for each number is
        // the set of other numbers with which it can be added to make a square.
        int[][] prefs = {{3,8,15},{7,14},{6,13,1},{5,12},{4,11},{3,10},{2,9},{1},{7,16},{6,15},{5,14},{4,13},{3,12},{2,11},{1,10},{9},};
        int[] nums = { 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
        FlowBPMatcher<Integer> matcher = new FlowBPMatcher<>(0, nums.length + 1);
        for (int i = 0; i < nums.length; i++) {
            int worker = nums[i];
            int[] jobs = prefs[i];
            for (int job : jobs) {
                matcher.link(worker, job);
            }
        }
        Flow.Max<Integer> match = matcher.getBestMatch();
        assert match.value == 16;
    }
}
