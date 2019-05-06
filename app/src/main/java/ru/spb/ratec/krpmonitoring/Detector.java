package ru.spb.ratec.krpmonitoring;

public class Detector {
    private int detState;
    private int block;
    private double level;
    private int detAlarmLevel;

    public Detector(){}

    public Detector(int detState, int block, double level, int detAlarmLevel) {
        this.detState = detState;
        this.block = block;
        this.level = level;
        this.detAlarmLevel = detAlarmLevel;
    }

    public int getDetState() {
        return detState;
    }

    public void setDetState(int detState) {
        this.detState = detState;
    }

    public int getBlock() {
        return block;
    }

    public void setBlock(int block) {
        this.block = block;
    }

    public double getLevel() {
        return level;
    }

    public void setLevel(double level) {
        this.level = level;
    }

    public int getDetAlarmLevel() {
        return detAlarmLevel;
    }

    public void setDetAlarmLevel(int detAlarmLevel) {
        this.detAlarmLevel = detAlarmLevel;
    }

    @Override
    public String toString() {
        return "Detector{" +
                "detState=" + detState +
                ", block=" + block +
                ", level=" + level +
                ", detAlarmLevel=" + detAlarmLevel +
                '}';
    }
}
