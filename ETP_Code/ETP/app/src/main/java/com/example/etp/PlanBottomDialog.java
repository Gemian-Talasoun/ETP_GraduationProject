package com.example.etp;
/* Imports */
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// *---------------------------------*  Bottom dialog that show plan POIs in home page   *---------------------------------*
public class PlanBottomDialog  extends BottomSheetDialogFragment {
    Context context;
    ArrayList<String> POINames,POICosts,POIDurations,POIIDs,POIStartTimes,POIEndTimes,POIDescribtions,POIImageURLs;
    public RecyclerPlanAdapter recyclerViewAdapter;

    public PlanBottomDialog(Context context,ArrayList<String> POINames, ArrayList<String> POICosts, ArrayList<String> POIDurations, ArrayList<String> POIIDs, ArrayList<String> POIStartTimes, ArrayList<String> POIEndTimes,ArrayList<String> POIDescribtions) {
        this.context = context;
        this.POINames = POINames;
        this.POICosts = POICosts;
        this.POIDurations = POIDurations;
        this.POIIDs = POIIDs;
        this.POIStartTimes = POIStartTimes;
        this.POIEndTimes = POIEndTimes;
        this.POIDescribtions = POIDescribtions;

        POIImageURLs = new ArrayList<String>();
        ArrayList<POI> poiData;
        poiData = POIsData(new ArrayList<Integer>(),new ArrayList<String>());
        for(int i=0;i<POIIDs.size();i++){
            POIImageURLs.add(poiData.get(Integer.parseInt(POIIDs.get(i))-1).photoURL);
        }


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.plan_bottom_layout,container,false);
        RecyclerView recyclerView = v.findViewById(R.id.recycleview2);
        recyclerViewAdapter = new RecyclerPlanAdapter(context,POINames,POIStartTimes,POIEndTimes,POIDescribtions,POICosts,POIDurations,POIIDs,false,POIImageURLs);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        return v;
    }

    // Get all POIs sorted by id
    public ArrayList<POI> POIsData(ArrayList<Integer>favorite,ArrayList<String>userPreferences){
        ArrayList<POI> list = new ArrayList<POI>();
        try {
            JSONArray jArray = new JSONArray(readJSONFromAsset(context));
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
                String image=jArray.getJSONObject(i).getString("PoiURLImage");
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
                Map<Integer,Integer> mp = new HashMap<Integer,Integer>();
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
    public  String readJSONFromAsset(Context c) {
        String json = null;
        try {
            InputStream is = c.getAssets().open("pois.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
