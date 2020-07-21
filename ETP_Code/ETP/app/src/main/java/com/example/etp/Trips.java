package com.example.etp;

import java.util.ArrayList;

public class Trips {


    private ArrayList<String>CommentsId;
    private String EndDate,NumOfExe;
    private ArrayList<String>PoisId;
    private String StartDate
    ,StartLocationLongitude
    ,TripBudget
    ,TripId
    ,TripName
    ,TripOnDashBord
    ,TripPlace
    ,TripRate
    ,TripTolalTime
    ,UploadedBy
    ,startLocationLatitude;

    public Trips(ArrayList<String> commentsId, String endDate, String numOfExe, ArrayList<String> poisId, String startDate, String startLocationLongitude, String tripBudget, String tripId, String tripName, String tripOnDashBord, String tripPlace, String tripRate, String tripTolalTime, String uploadedBy, String startLocationLatitude) {
        CommentsId = commentsId;
        EndDate = endDate;
        NumOfExe = numOfExe;
        PoisId = poisId;
        StartDate = startDate;
        StartLocationLongitude = startLocationLongitude;
        TripBudget = tripBudget;
        TripId = tripId;
        TripName = tripName;
        TripOnDashBord = tripOnDashBord;
        TripPlace = tripPlace;
        TripRate = tripRate;
        TripTolalTime = tripTolalTime;
        UploadedBy = uploadedBy;
        this.startLocationLatitude = startLocationLatitude;
    }

    public Trips() {
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getNumOfExe() {
        return NumOfExe;
    }

    public void setNumOfExe(String numOfExe) {
        NumOfExe = numOfExe;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getStartLocationLongitude() {
        return StartLocationLongitude;
    }

    public void setStartLocationLongitude(String startLocationLongitude) {
        StartLocationLongitude = startLocationLongitude;
    }

    public String getTripBudget() {
        return TripBudget;
    }

    public void setTripBudget(String tripBudget) {
        TripBudget = tripBudget;
    }

    public String getTripId() {
        return TripId;
    }

    public void setTripId(String tripId) {
        TripId = tripId;
    }

    public String getTripName() {
        return TripName;
    }

    public void setTripName(String tripName) {
        TripName = tripName;
    }

    public String getTripOnDashBord() {
        return TripOnDashBord;
    }

    public void setTripOnDashBord(String tripOnDashBord) {
        TripOnDashBord = tripOnDashBord;
    }

    public String getTripPlace() {
        return TripPlace;
    }

    public void setTripPlace(String tripPlace) {
        TripPlace = tripPlace;
    }

    public String getTripRate() {
        return TripRate;
    }

    public void setTripRate(String tripRate) {
        TripRate = tripRate;
    }

    public String getTripTolalTime() {
        return TripTolalTime;
    }

    public void setTripTolalTime(String tripTolalTime) {
        TripTolalTime = tripTolalTime;
    }

    public String getUploadedBy() {
        return UploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        UploadedBy = uploadedBy;
    }

    public String getStartLocationLatitude() {
        return startLocationLatitude;
    }

    public void setStartLocationLatitude(String startLocationLatitude) {
        this.startLocationLatitude = startLocationLatitude;
    }

    public ArrayList<String> getPoisId() {
        return PoisId;
    }

    public void setPoisId(ArrayList<String> poisId) {
        PoisId = poisId;
    }

    public ArrayList<String> getCommentsId() {
        return CommentsId;
    }

    public void setCommentsId(ArrayList<String> commentsId) {
        CommentsId = commentsId;
    }
}
