package greedy;

public class Job implements Comparable<Job> {
    
    protected final int ID;
    protected final int weight;
    protected final int length;
    protected JobScorer scorer;
    
    public Job(int ID, int w, int l) {
        this.ID = ID;
        this.weight = w;
        this.length = l;
    }

    public void setScorer(JobScorer scorer) {
        this.scorer = scorer;
    }

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
    
    public static long jobSequenceTime(Job[] jobs) {
        long t = 0; // cumulative time
        long l = 0; // weighted completion time
        for (Job job :jobs) {
            t += job.length;
            l += job.weight * t;
        }
        return l;
    }
    
    public static boolean isOrderedByScore(Job[] jobs) {
        for (int i = 0 ; i < jobs.length - 1; i++) {
            if(jobs[i].scorer.score() < jobs[i+1].scorer.score()) {
                return false;
            }
        }
        return true;
    }
    
}
