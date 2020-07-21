
package com.example.etp;

import java.util.*;

// *---------------------------------*  Used to present Point of interest (Place)   *---------------------------------*
public class POI {
    private int id;                                 // POI ID
    private String name;                            // POI Name
    private double cost;                            // POI Minimum cost
    private ArrayList<String> preferences;          // POI Preferences
    private Map<Integer,Integer> shortestPathes;    // POI Shortest Pathes to other POIs
    private String type;                            // POI Working time type (M,A,N)
    private Integer duration;                       // POI Minimum spend time
    private boolean isFavorite;                     // If this POI in user's favorite list
    private double value;                           // POI satisfaction factor
    private Time closeTime;                         // POI Close Time
    private Time openTime;                          // POI Open Time
    public String desc;                             // POI Desciption
    public String photoURL;                         // POI photo URL


    public POI(int id,String name, double cost, ArrayList<String> preferences, Map<Integer,Integer> shortestPathes, String type, Integer duration,ArrayList<Integer>favorite,ArrayList<String>userPreferences,Time openTime,Time closeTime) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.preferences = preferences;
        this.shortestPathes = shortestPathes;
        this.type = type;
        this.duration = duration;
        this.closeTime = closeTime;
        this.openTime = openTime;
        this.isFavorite =false;

        // Check if POI is in favorite list or not
        for(int i=0;i<favorite.size();i++){
            if(this.id == favorite.get(i)){
                isFavorite=true;
                break;
            }
        }


        // Get POI's Satisfication factor
        this.value = 0;
        for(int i=0;i<this.preferences.size();i++){
            for(int u=0;u<userPreferences.size();u++){
                if(this.preferences.get(i).equalsIgnoreCase(userPreferences.get(u))){
                    value++;
                    break;
                }
            }
        }
        value+=10*((this.isFavorite)?1:0);

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }

    public ArrayList<String> getPreferences() {
        return preferences;
    }

    public Map<Integer,Integer> getShortestPathes() {
        return shortestPathes;
    }

    public Integer getShortestPath (int id){
        return shortestPathes.get(id);
    }

    public String getType() {
        return type;
    }

    public Integer getDuration() {
        return duration;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public double getValue() {
        return value;
    }

    public Time getCloseTime() {
        return closeTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setPreferences(ArrayList<String> preferences) {
        this.preferences = preferences;
    }

    public void setShortestPathes(Map<Integer, Integer> shortestPathes) {
        this.shortestPathes = shortestPathes;
    }

    public void setType(String type) {
        this.type = type;
    }


    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setCloseTime(Time closeTime) {
        this.closeTime = closeTime;
    }

    public Time getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Time openTime) {
        this.openTime = openTime;
    }


/*
    @Override
    public int compareTo(Object poi) {
        return Double.compare(this.cost, ((POI)poi).cost);
    }
*/

}
