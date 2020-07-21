package com.example.etp;
import java.util.*;
// *---------------------------------*  Used to present user data   *---------------------------------*
public class User {

    private String UserEmail,UserName,UserPassword, RegisterDate,UserAddress,UserImage;
    private int UserID,ActiveTrip;
    private ArrayList<Integer>TripsId;
    private ArrayList<String> preferences;
    private ArrayList<Integer> favorite;  //id of each favorite place
    private ArrayList<Integer> forbidden; //id of each forbidden place
    
    User (ArrayList<String> preferences,
            ArrayList<Integer>favorite,
            ArrayList<Integer>forbidden){
        this.preferences=preferences;
        // Sort favorite and forbidden lists by id asc for fav and desc for forbidden
        Collections.sort(favorite);
        Collections.sort(forbidden,Collections.reverseOrder());
        this.favorite=favorite;
        this.forbidden=forbidden;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserPassword() {
        return UserPassword;
    }

    public void setUserPassword(String userPassword) {
        UserPassword = userPassword;
    }

    public String getRegisterDate() {
        return RegisterDate;
    }

    public void setRegisterDate(String registerDate) {
        RegisterDate = registerDate;
    }

    public String getUserAddress() {
        return UserAddress;
    }

    public void setUserAddress(String userAddress) {
        UserAddress = userAddress;
    }

    public String getUserImage() {
        return UserImage;
    }

    public void setUserImage(String userImage) {
        UserImage = userImage;
    }

    public int getActiveTrip() {
        return ActiveTrip;
    }

    public void setActiveTrip(int activeTrip) {
        ActiveTrip = activeTrip;
    }

    public ArrayList<Integer> getTripsId() {
        return TripsId;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public void setTripsId(ArrayList<Integer> tripsId) {
        TripsId = tripsId;
    }

    public void setPreferences(ArrayList<String> preferences) {
        this.preferences = preferences;
    }

    public void setFavorite(ArrayList<Integer> favorite) {
        this.favorite = favorite;
    }

    public void setForbidden(ArrayList<Integer> forbidden) {
        this.forbidden = forbidden;
    }

    public User(int userid,String userEmail, String userName, String userPassword, ArrayList<String> perfrances ) {
        UserID=userid;
        UserEmail = userEmail;
        UserName = userName;
        UserPassword = userPassword;
        preferences=perfrances;
    }
    public User(){

    }

    public ArrayList<String> getPreferences() {
        return preferences;
    }

    public ArrayList<Integer> getFavorite() {
        return favorite;
    }

    public ArrayList<Integer> getForbidden() {
        return forbidden;
    }
    
    
}
