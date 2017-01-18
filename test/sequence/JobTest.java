/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sequence;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import sort.QuickSorter;

public class JobTest {
    
    @Test
    public void testJobSequencing() {
        Job[] jobs = readJobs("resources/jobs.txt");
        QuickSorter qs = new QuickSorter();
        
        for (Job job : jobs) {
            job.setScorer(new JobScorer.WeightLengthDiff(job));
        }
        qs.sort(jobs);
        assert Job.isOrderedByScore(jobs);
        long sequenceByWeigthLengthDiff = Job.jobSequenceTime(jobs);
        assert sequenceByWeigthLengthDiff == 69119377652l;
        
        for (Job job : jobs) {
            job.setScorer(new JobScorer.WeightLengthRatio(job));
        }
        qs.sort(jobs);
        assert Job.isOrderedByScore(jobs);
        long sequenceByWeigthLengthRatio = Job.jobSequenceTime(jobs);
        assert sequenceByWeigthLengthRatio == 67311454237l; 
    }

    private Job[] readJobs(String path) {
        BufferedReader br = null;
        Job[] jobs = null;
        try {
            br = new BufferedReader(new FileReader(path));
            String line;
            line = br.readLine();
            int numJobs = Integer.parseInt(line);
            jobs = new Job[numJobs];
            for (int id = 0; (line = br.readLine()) != null && id < numJobs; id++) {
                String[] split = line.trim().split("(\\s)+");
                int w = Integer.parseInt(split[0]);
                int l = Integer.parseInt(split[1]);
                jobs[id] = new Job(id, w, l);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JobTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JobTest.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(JobTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return jobs;
    }
    
}
