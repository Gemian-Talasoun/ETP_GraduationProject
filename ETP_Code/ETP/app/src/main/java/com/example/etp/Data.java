package com.example.etp;

/* Imports */
import org.json.JSONArray;
import org.json.JSONException;
import java.util.*;

// *---------------------------------*  Data class used to get all data used in planner algorithm (User-Trip-POIs)    *---------------------------------*
public class Data {
    /* Vars */
    public Time startTime;
    public Time endTime;
    public ArrayList<String>userPrefrences;
    public ArrayList<Integer>userForbiddenPOIs;
    public ArrayList<Integer>userFavoritePOIs;
    public  double budget=0;
    // *---------------------------------*  Get user and trip Data from constrains page    *---------------------------------*
    public Data(Time startTime, Time endTime, ArrayList<String> userPrefrences, ArrayList<Integer> userForbiddenPOIs,
                ArrayList<Integer> userFavoritePOIs, double budget) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.userPrefrences = userPrefrences;
        this.userForbiddenPOIs = userForbiddenPOIs;
        this.userFavoritePOIs = userFavoritePOIs;
        this.budget = budget;
    }
    // *---------------------------------*  Return user data (preferences, favorites, forbidden)   *---------------------------------*
    public User userData(){
        return ( new User(userPrefrences,userFavoritePOIs,userForbiddenPOIs) );
    }
    // *---------------------------------*  Return trip data (start time, end time , budget)    *---------------------------------*
    public Trip tripData(){
        return ( new Trip(startTime,endTime,budget,null,null) );
    }
    // *---------------------------------*  Return POIs from json file     *---------------------------------*
    public static ArrayList<POI> POIsData(ArrayList<Integer>favorite,ArrayList<String>userPreferences){
        showPlan readfun=new showPlan(); //oject from showPlan to use function on it
        ArrayList<POI> list = new ArrayList<POI>();
        try {
            JSONArray jArray = new JSONArray(readfun.readJSONFromAsset(showPlan.getContext()));
            for (int i = 0; i < jArray.length(); ++i) {
                int id=Integer.parseInt(jArray.getJSONObject(i).getString("PoiID"));
                String name=jArray.getJSONObject(i).getString("PoiName");
                String start=jArray.getJSONObject(i).getString("OpenTime");
                int startHour=Integer.parseInt(start.substring(0,start.indexOf(":")));
                int startMin=Integer.parseInt(start.substring(start.indexOf(":")+1));
                String end=jArray.getJSONObject(i).getString("CloseTime");
                int endHour=Integer.parseInt(end.substring(0,end.indexOf(":")));
                int endMin=Integer.parseInt(end.substring(end.indexOf(":")+1));
                double cost=Double.parseDouble(jArray.getJSONObject(i).getString("PoiPrice"));
                int dur=Integer.parseInt(jArray.getJSONObject(i).getString("VisitTime"));
                String type=jArray.getJSONObject(i).getString("WorkingTime");
                ArrayList<String>p = new ArrayList<String>();
                ArrayList<Integer>da= new ArrayList<Integer>();
                String perf=jArray.getJSONObject(i).getString("PoiPreferences");
                int n=perf.length();
                for(int j=0;j<n;j++)
                {
                    String ans="";
                    if(perf.charAt(j)=='"')
                    {
                        j++;
                        while ((perf.charAt(j)!='"')) {
                            ans += perf.charAt(j);
                            j++;
                        }
                        p.add(ans);
                        j++;

                    }
                }
                String daur=jArray.getJSONObject(i).getString("Duration");
                int x=daur.length();
                x=daur.length();
                for(int j=0;j<x;j++)
                {
                    String ans="";
                    if(Character.isDigit(daur.charAt(j)))
                    {
                        ans+=daur.charAt(j);
                        j++;
                        while (Character.isDigit(daur.charAt(j)))
                        {
                            ans+=daur.charAt(j);
                            j++;
                        }
                        da.add(Integer.parseInt(ans));
                    }
                    else if(daur.charAt(j)=='n')
                    {
                        da.add(0);
                    }
                }
                Map<Integer,Integer>mp = new HashMap<Integer,Integer>();
                for(int u=0;u<da.size();u++) {
                    mp.put(u + 1, da.get(u));
                }

                POI poi = new POI(id,name,cost,p,mp,type,dur,favorite,userPreferences,new Time(startHour,startMin),new Time(endHour,endMin));
                poi.photoURL = jArray.getJSONObject(i).getString("PoiURLImage");
                poi.desc = jArray.getJSONObject(i).getString("PoiDescription");

                list.add(poi);





            }
        } catch (JSONException e) {
            e.printStackTrace();
        }




        return list;
    }

}

