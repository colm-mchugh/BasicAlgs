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
    public void testMatching() {
        int[][] prefs = {{100}, {300}, {200}, {300, 500}, {400}};
        int[] workers = {1, 2, 3, 4, 5};
        FlowBPMatcher<Integer> matcher = new FlowBPMatcher<>(0, workers.length + 1);
        for (int i = 0; i < workers.length; i++) {
            int worker = workers[i];
            int[] jobs = prefs[i];
            for (int job : jobs) {
                matcher.link(worker, job);
            }
        }
        Flow.Max<Integer> match = matcher.getBestMatch();
        assert match.value == 5;
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
    
}
