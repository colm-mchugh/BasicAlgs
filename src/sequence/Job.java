package sequence;

public class Job implements Comparable<Job> {
    
    public int ID;
    public int w;
    public int l;

    public Job(int ID, int w, int l) {
        this.ID = ID;
        this.w = w;
        this.l = l;
    }

    @Override
    public int compareTo(Job o) {
        //int myScore = this.w - this.l;
        // int oScore = o.w - o.l;
        if (this.score() > o.score()) {
            return -1;
        } else {
            if (o.score() > this.score()) {
                return 1;
            }
        }
        return o.w - this.w;
    }
    
    public double score() {
        return this.w / (double)this.l;
    }
}
