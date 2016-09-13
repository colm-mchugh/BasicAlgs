package sequence;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import sort.QuickSorter;

public class SequnceJobs {

    public static long jobTime(Job[] jobs) {
        long t = 0; // cumulative time
        long l = 0; // weighted completion time
        for (Job job :jobs) {
            t += job.l;
            l += job.w * t;
        }
        return l;
    }

    // Verify that the jobs are ordered in
    // descending value 
    public static boolean verify(Job[] jobs) {
        for (int i = 0 ; i < jobs.length - 1; i++) {
            if(jobs[i].score() < jobs[i+1].score()) {
                return false;
            }
            System.out.println("w=" + jobs[i].w + ", l=" + jobs[i].l + ", score=" + jobs[i].score());
        }
        return true;
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader("resources/jobs.txt"));
        String line;
        line = br.readLine();
        int numJobs = Integer.parseInt(line);
        Job[] jobs = new Job[numJobs];
        for (int id = 0; (line = br.readLine()) != null && id < numJobs; id++) {
            String[] split = line.trim().split("(\\s)+");
            int w = Integer.parseInt(split[0]);
            int l = Integer.parseInt(split[1]);
            jobs[id] = new Job(id, w, l);
        }
        QuickSorter qs = new QuickSorter();
        qs.sort(jobs);
        if (verify(jobs)) {
            System.out.println("Jobs lenght is " + jobTime(jobs));
        } else {
            System.out.println("The array is not ordered in descending priority");
        }
        Job[] simple = new Job[2];
        simple[0] = new Job(0, 3, 5);
        simple[1] = new Job(1, 1, 2);
        
        qs.sort(simple);
        System.out.println("simple length is " + jobTime(simple));
    }
}
