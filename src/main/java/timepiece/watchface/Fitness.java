package timepiece.watchface;

public class Fitness {
    private int fitness;
    private int checkedOK = 0;
    private int checkedNOK = 0;
    private int checkedTimesOK = 0;
    private int checkedTimesNOK = 0;
    private int splitPos = 0;
    private double variance = 0;
    private double avgLen = 0;

    private Fitness(int fitness, int checkedOK, int checkedNOK, int checkedTimesOK, int checkedTimesNOK, int splitPos, double variance, double avgLen) {
        this.fitness = fitness;
        this.checkedOK = checkedOK;
        this.checkedNOK = checkedNOK;
        this.checkedTimesOK = checkedTimesOK;
        this.checkedTimesNOK = checkedTimesNOK;
        this.splitPos = splitPos;
        this.variance = variance;
        this.avgLen = avgLen;
    }

    public Fitness() {
        this.fitness = Integer.MIN_VALUE;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public int getCheckedOK() {
        return checkedOK;
    }

    public void setCheckedOK(int checkedOK) {
        this.checkedOK = checkedOK;
    }

    public int getCheckedNOK() {
        return checkedNOK;
    }

    public void setCheckedNOK(int checkedNOK) {
        this.checkedNOK = checkedNOK;
    }

    public int getCheckedTimesOK() {
        return checkedTimesOK;
    }

    public void setCheckedTimesOK(int checkedTimesOK) {
        this.checkedTimesOK = checkedTimesOK;
    }

    public int getCheckedTimesNOK() {
        return checkedTimesNOK;
    }

    public void setCheckedTimesNOK(int checkedTimesNOK) {
        this.checkedTimesNOK = checkedTimesNOK;
    }

    public int getSplitPos() {
        return splitPos;
    }

    public void setSplitPos(int splitPos) {
        this.splitPos = splitPos;
    }

    public double getVariance() {
        return variance;
    }

    public void setVariance(double variance) {
        this.variance = variance;
    }

    public double getAvgLen() {
        return avgLen;
    }

    public void setAvgLen(double avgLen) {
        this.avgLen = avgLen;
    }

    public static Fitness createFitness(int fitness, int checkedOK, int checkedNOK, int checkedTimesOK, int checkedTimesNOK, int splitPos, double variance, double avgLen) {
        return new Fitness(fitness, checkedOK, checkedNOK, checkedTimesOK, checkedTimesNOK, splitPos, variance, avgLen);
    }
}