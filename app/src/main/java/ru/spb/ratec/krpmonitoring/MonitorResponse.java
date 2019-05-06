package ru.spb.ratec.krpmonitoring;

import java.util.ArrayList;

public class MonitorResponse {
    private String iD;
    private String device;
    private int deviceState;
    private int numDet;
    private int alarmLevel;
    private String isotope;
    private int alarmDet;
    private String threshold;
    private ArrayList<Detector> detectorState;


    MonitorResponse() {

    }

    MonitorResponse(String iD, String device, int deviceState, int numDet, int alarmLevel,
                    String isotope, int alarmDet, String threshold, ArrayList<Detector> detectorState) {
        this.iD = iD;
        this.device = device;
        this.deviceState = deviceState;
        this.numDet = numDet;
        this.alarmLevel = alarmLevel;
        this.isotope = isotope;
        this.alarmDet = alarmDet;
        this.threshold = threshold;
        this.detectorState = detectorState;

    }

    public String getId() {
        return iD;
    }

    public void setId(String iD) {
        this.iD = iD;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public int getDeviceState() {
        return deviceState;
    }

    public void setDeviceState(int deviceState) {
        this.deviceState = deviceState;
    }

    public int getNumDet() {
        return numDet;
    }

    public void setNumDet(int numDet) {
        this.numDet = numDet;
    }

    public int getAlarmLevel() {
        return alarmLevel;
    }

    public void setAlarmLevel(int alarmLevel) {
        this.alarmLevel = alarmLevel;
    }

    public String getIsotope() {
        return isotope;
    }

    public void setIsotope(String isotope) {
        this.isotope = isotope;
    }

    public int getAlarmDet() {
        return alarmDet;
    }

    public void setAlarmDet(int alarmDet) {
        this.alarmDet = alarmDet;
    }

    public String getThreshold() {
        return threshold;
    }

    public void setThreshold(String threshold) {
        this.threshold = threshold;
    }

    public ArrayList<Detector> getDetectorState() { return detectorState; }

    public void setDetectorState(ArrayList<Detector> detectorState) {
        this.detectorState = detectorState;
    }

    @Override
    public String toString() {
        return "MonitorResponse{" +
                "iD='" + iD + '\'' +
                ", device='" + device + '\'' +
                ", deviceState='" + deviceState + '\'' +
                ", numDet=" + numDet +
                ", alarmLevel=" + alarmLevel +
                ", isotope='" + isotope + '\'' +
                ", alarmDet='" + alarmDet + '\'' +
                ", threshold='" + threshold + '\'' +
                ", detectorState=" + detectorState +
                '}';
    }
}

