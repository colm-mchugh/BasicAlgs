package greedy;

/**
 * A JobScorer - record a score for a job.
 * 
 */
public abstract class JobScorer {

    protected double score;

    public double score() {
        return this.score;
    }

    /**
     * Specialization of JobScorer that computes the score of the given job 
     * as the difference between its weight and length.
     */
    public static class WeightLengthDiff extends JobScorer {
        public WeightLengthDiff(Job j) {
            this.score = j.weight - j.length;
        }
    }

    /**
     * Specialization of JobScorer that computes the score of the given job 
     * as the ratio of its weight over its length.
     */
    public static class WeightLengthRatio extends JobScorer {
        public WeightLengthRatio(Job j) {
            this.score = j.weight / (double) j.length;
        }
    }
}
