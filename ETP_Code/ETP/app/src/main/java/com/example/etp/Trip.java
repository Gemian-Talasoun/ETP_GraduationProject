
package com.example.etp;

// *---------------------------------*  Used to present trip data   *---------------------------------*
public class Trip {
    private Time startTime;
    private Time endTime;
    private double budget;
    private POI startPOI;
    private POI endPOI;

    public Trip() {
    }
    
    public Trip(Time startTime, Time endTime, double budget, POI startPOI, POI endPOI) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.budget = budget;
        this.startPOI = startPOI;
        this.endPOI = endPOI;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public POI getStartPOI() {
        return startPOI;
    }

    public void setStartPOI(POI startPOI) {
        this.startPOI = startPOI;
    }

    public POI getEndPOI() {
        return endPOI;
    }

    public void setEndPOI(POI endPOI) {
        this.endPOI = endPOI;
    }
    
    
    
}
