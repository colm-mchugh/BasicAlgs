package greedy;

/**
 * Job Scheduling; given a set of jobs each with weight Wj and length Lj,
 * minimize the weighted sum of completion times, SUM of Cj*Wj. 
 * Cj is the completion time of job j in schedule of the set of jobs.
 * 
 * Ordering jobs by using the ratio of Wj / Lj as a job's score gives an 
 * optimal scheduling; The job at index i has a higher score than it's
 * successor, i.e. Wi / Li > Wi+1 / Li+1
 * 
 * Proof can be shown by exchanging the order of two jobs i and j in such
 * an ordering and showing that the cost (increase in completion time) is 
 * greater than or equal to the benefit (decrease in completion time).
 * 
 * 
 */
public class Job implements Comparable<Job> {
    
    protected final int ID;     // job id
    protected final int weight; // job weight
    protected final int length; // job length
    protected JobScorer scorer; // job score (pluggable algorithm)
    
    public Job(int ID, int w, int l) {
        this.ID = ID;
        this.weight = w;
        this.length = l;
    }

    public void setScorer(JobScorer scorer) {
        this.scorer = scorer;
    }

    /**
     * Jobs are comparable as follows:
     * This job precedes a given job o if it has a higher score, and 
     * succeeds job o if it has a lower score. If this job and job o
     * have the same score, the job with the higher weight precedes
     * the other. Same weight means the jobs are equal.
     * 
     * @param o
     * @return 
     */
    @Override
    public int compareTo(Job o) {
        double myScore = this.scorer.score;
        double theirScore = o.scorer.score;
        if (myScore > theirScore) {
            return -1;
        } else {
            if (theirScore > myScore) {
                return 1;
            }
        }
        return o.weight - this.weight;
    }

    public int getWeight() {
        return weight;
    }

    public int getLength() {
        return length;
    }
    
    public double Score() {
        return this.scorer.score;
    }
    
    /**
     * Given a sequence of jobs, return the sum of their weighted completion time.
     * Return  SUM Wi * Ci for all i in jobs where:
     *      Wi is the weight of job i
     *      Ci is the completion time of job i
     * 
     * @param jobs
     * @return 
     */
    public static long jobSequenceTime(Job[] jobs) {
        long t = 0; // cumulative time
        long l = 0; // weighted completion time
        for (Job job :jobs) {
            t += job.length;
            l += job.weight * t;
        }
        return l;
    }
    
    /**
     * Given a sequence of jobs, return true if they are ordered by decreasing 
     * score, otherwise false.
     * 
     * If a sequence is ordered by score, then score(Job i) >= score(Job i+1).
     * Job 0 has the highest score, job 1 next highest, etc.
     * 
     * @param jobs
     * @return 
     */
    public static boolean isOrderedByScore(Job[] jobs) {
        for (int i = 0 ; i < jobs.length - 1; i++) {
            if(jobs[i].scorer.score() < jobs[i+1].scorer.score()) {
                return false;
            }
        }
        return true;
    }
    
}
