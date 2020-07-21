package com.example.etp;

import android.app.Application;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;

/*
* this class to store the current user data and use it in anywhere in the application.
 */

public class CurrentUserData extends Application {
    private String mCurrentUserId;
    private HashMap<String, String>  mFavoritePlacesList, mForbiddenPlacesList, mTripsIdList;
    private ArrayList<String> mPerfrancesList;
    private String mUserEmail, mUserPassword, mUserName, mRegisterDate, mUserAddress, mUserID, mUserImage, mUserRatingApp, mUserPhone, AccountType;
    private FirebaseUser mFirebaseUser;

    public String getmCurrentUserId() {
        return mCurrentUserId;
    }

    public void setmCurrentUserId(String mCurrentUserId) {
        this.mCurrentUserId = mCurrentUserId;
    }

    public HashMap<String, String> getmFavoritePlacesList() {
        return mFavoritePlacesList;
    }

    public void setmFavoritePlacesList(HashMap<String, String> mFavoritePlacesList) {
        this.mFavoritePlacesList = new HashMap<>();
        this.mFavoritePlacesList = mFavoritePlacesList;
    }

    public HashMap<String, String> getmForbiddenPlacesList() {
        return mForbiddenPlacesList;
    }

    public void setmForbiddenPlacesList(HashMap<String, String> mForbiddenPlacesList) {
        this.mForbiddenPlacesList = new HashMap<>();
        this.mForbiddenPlacesList = mForbiddenPlacesList;
    }

    public HashMap<String, String> getmTripsIdList() {
        return mTripsIdList;
    }

    public void setmTripsIdList(HashMap<String, String> mTripsIdList) {
        this.mTripsIdList = new HashMap<>();
        this.mTripsIdList = mTripsIdList;
    }

    public ArrayList<String> getmPerfrancesList() {
        return mPerfrancesList;
    }

    public void setmPerfrancesList(ArrayList<String> mPerfrancesList) {
        this.mPerfrancesList = mPerfrancesList;
    }

    public String getmUserEmail() {
        return mUserEmail;
    }

    public void setmUserEmail(String mUserEmail) {
        this.mUserEmail = mUserEmail;
    }

    public String getmUserPassword() {
        return mUserPassword;
    }

    public void setmUserPassword(String mUserPassword) {
        this.mUserPassword = mUserPassword;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getmRegisterDate() {
        return mRegisterDate;
    }

    public void setmRegisterDate(String mRegisterDate) {
        this.mRegisterDate = mRegisterDate;
    }

    public String getmUserAddress() {
        return mUserAddress;
    }

    public void setmUserAddress(String mUserAddress) {
        this.mUserAddress = mUserAddress;
    }

    public String getmUserID() {
        return mUserID;
    }

    public void setmUserID(String mUserID) {
        this.mUserID = mUserID;
    }

    public String getmUserImage() {
        return mUserImage;
    }

    public void setmUserImage(String mUserImage) {
        this.mUserImage = mUserImage;
    }

    public String getmUserRatingApp() {
        return mUserRatingApp;
    }

    public void setmUserRatingApp(String mUserRatingApp) {
        this.mUserRatingApp = mUserRatingApp;
    }

    public String getmUserPhone() {
        return mUserPhone;
    }

    public void setmUserPhone(String mUserPhone) {
        this.mUserPhone = mUserPhone;
    }

    public FirebaseUser getmFirebaseUser() {
        return mFirebaseUser;
    }

    public void setmFirebaseUser(FirebaseUser mFirebaseUser) {
        this.mFirebaseUser = mFirebaseUser;
    }

    public String getAccountType() {
        return AccountType;
    }

    public void setAccountType(String accountType) {
        AccountType = accountType;
    }
}
