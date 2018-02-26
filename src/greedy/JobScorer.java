package greedy;

/**
 * A JobScorer - score a job.
 * 
 */
public abstract class JobScorer {

    protected double score;

    public double score() {
        return this.score;
    }

    /**
     * Job score is difference between its weight and length.
     */
    public static class WeightLengthDiff extends JobScorer {
        public WeightLengthDiff(Job j) {
            this.score = j.weight - j.length;
        }
    }

    /**
     * Job score is the ratio of its weight over its length.
     */
    public static class WeightLengthRatio extends JobScorer {
        public WeightLengthRatio(Job j) {
            this.score = j.weight / (double) j.length;
        }
    }
}
