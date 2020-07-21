package com.example.etp;

public class Pios {
    private String CloseTime,OpenTime
    ,PoiDescription
    ,PoiID
    ,PoiLatitude
    ,PoiLongitude
    ,PoiName
    ,PoiPhone
    ,PoiPrice
    ,PoiType
    ,VisitTime;

    public Pios(String closeTime, String openTime, String poiDescription, String poiID, String poiLatitude, String poiLongitude, String poiName, String poiPhone, String poiPrice, String poiType, String visitTime) {
        CloseTime = closeTime;
        OpenTime = openTime;
        PoiDescription = poiDescription;
        PoiID = poiID;
        PoiLatitude = poiLatitude;
        PoiLongitude = poiLongitude;
        PoiName = poiName;
        PoiPhone = poiPhone;
        PoiPrice = poiPrice;
        PoiType = poiType;
        VisitTime = visitTime;
    }

    public Pios() {
    }

    public String getCloseTime() {
        return CloseTime;
    }

    public void setCloseTime(String closeTime) {
        CloseTime = closeTime;
    }

    public String getOpenTime() {
        return OpenTime;
    }

    public void setOpenTime(String openTime) {
        OpenTime = openTime;
    }

    public String getPoiDescription() {
        return PoiDescription;
    }

    public void setPoiDescription(String poiDescription) {
        PoiDescription = poiDescription;
    }

    public String getPoiID() {
        return PoiID;
    }

    public void setPoiID(String poiID) {
        PoiID = poiID;
    }

    public String getPoiLatitude() {
        return PoiLatitude;
    }

    public void setPoiLatitude(String poiLatitude) {
        PoiLatitude = poiLatitude;
    }

    public String getPoiLongitude() {
        return PoiLongitude;
    }

    public void setPoiLongitude(String poiLongitude) {
        PoiLongitude = poiLongitude;
    }

    public String getPoiName() {
        return PoiName;
    }

    public void setPoiName(String poiName) {
        PoiName = poiName;
    }

    public String getPoiPhone() {
        return PoiPhone;
    }

    public void setPoiPhone(String poiPhone) {
        PoiPhone = poiPhone;
    }

    public String getPoiPrice() {
        return PoiPrice;
    }

    public void setPoiPrice(String poiPrice) {
        PoiPrice = poiPrice;
    }

    public String getPoiType() {
        return PoiType;
    }

    public void setPoiType(String poiType) {
        PoiType = poiType;
    }

    public String getVisitTime() {
        return VisitTime;
    }

    public void setVisitTime(String visitTime) {
        VisitTime = visitTime;
    }
}
