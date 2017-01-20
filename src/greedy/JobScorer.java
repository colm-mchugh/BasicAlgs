package greedy;

public abstract class JobScorer {

    protected double score;

    public double score() {
        return this.score;
    }

    public static class WeightLengthDiff extends JobScorer {
        public WeightLengthDiff(Job j) {
            this.score = j.weight - j.length;
        }
    }

    public static class WeightLengthRatio extends JobScorer {
        public WeightLengthRatio(Job j) {
            this.score = j.weight / (double) j.length;
        }
    }
}
