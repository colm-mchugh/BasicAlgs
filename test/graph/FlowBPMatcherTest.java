package graph;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;

public class FlowBPMatcherTest {
    
    

    @Test
    public void testGetBestMatch() {
        String[][] prefs = { { "J1" }, { "J1", "J3" }, {"J2", "J3", "J4"}, { "J3" }, { "J3" } };
        String[] workers = { "W1", "W2", "W3", "W4", "W5" };
        Map<String, String[]> workerJobPrefs = new HashMap<>(workers.length);
        for (int i = 0; i < workers.length; i++) {
            workerJobPrefs.put(workers[i], prefs[i]);
        };
        FlowBPMatcher<String> matcher = new FlowBPMatcher<>(workerJobPrefs, "S", "T");
        Flow.Max<String> match = matcher.getBestMatch();
        assert match.value == 3;
    }
    
}
