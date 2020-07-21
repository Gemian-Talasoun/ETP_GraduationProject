package com.example.etp;

import java.util.ArrayList;

public class Users {
    private String UserEmail,UserName,UserPassword;
    ArrayList<String> Perfrances = new ArrayList<>();

    public Users(String userEmail, String userName, String userPassword,ArrayList<String> perfrances ) {
        UserEmail = userEmail;
        UserName = userName;
        UserPassword = userPassword;
        Perfrances=perfrances;
    }
    public Users(){

    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public ArrayList<String> getPerfrances() {
        return Perfrances;
    }

    public void setPerfrances(ArrayList<String> perfrances) {
        this.Perfrances = perfrances;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public void setUserPassword(String userPassword) {
        UserPassword = userPassword;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public String getUserName() {
        return UserName;
    }

    public String getUserPassword() {
        return UserPassword;
    }

}
