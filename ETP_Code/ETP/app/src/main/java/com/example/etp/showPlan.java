package com.example.etp;

/* Import */
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

// *---------------------------------*  Plan Recycler view adapter in show plan page and bottom dialog   *---------------------------------*
public class showPlan extends AppCompatActivity {

    /* Vars */
    private static Context context;
    ArrayList<String> POINames,POIStartTimes,POIEndTimes,POIDescribtions,POICosts,POIDurations,POIIDs,POIImageURLs;
    FullPlan plan;
    ArrayList<POI> POIs;
    ArrayList<Integer>favoriteList;
    ArrayList<String>userPrefrences;
    TextView slctPlaceAdd,slctPlaceNum,slctPlaceEdit;
    String[] arrPlaces;
    String[] indces;
    CurrentUserData mCurrentUserData;
    ImageView CurrentUserImage;
    // Used in min time between 2 places in showplan
    int id1=1,id2=2;
    Button place1,place2;
    TextView travelTime;

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        showPlan.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showplan);
        showPlan.context = getApplicationContext();
        setContext(showPlan.context);

        initVariables();

        //Get Plan
        /* Get Constrains */
        Time stTime = new Time(getIntent().getIntExtra("startTimeHour",0),getIntent().getIntExtra("startTimeMin",0)) ;
        Time enTime = new Time(getIntent().getIntExtra("endTimeHour",0),getIntent().getIntExtra("endTimeMin",0)) ;
        double bud = getIntent().getDoubleExtra("budget",0);
        ArrayList<String> p = getIntent().getStringArrayListExtra("prefrences");
        ArrayList<Integer> favoriteList = new ArrayList<Integer>();
        convertFromStringToInt(favoriteList,getIntent().getStringArrayListExtra("favoriteList"));
        ArrayList<Integer> forbiddenList = new ArrayList<Integer>();
        convertFromStringToInt(forbiddenList,getIntent().getStringArrayListExtra("forbiddenList"));
        Data data = new Data(stTime,enTime,p,forbiddenList,favoriteList,bud);
        /* Generate plan */
        final long testEndTime,testStartTime;
        testStartTime = System.currentTimeMillis();
        plan = Planner.getPlan(10000,3,1,3,2,10,data);
        testEndTime = System.currentTimeMillis();
        plan.getPlanD(POIStartTimes,POIEndTimes,POINames,POICosts,POIDurations,POIDescribtions,POIIDs,POIImageURLs);

        // Initlize Recycler View
        initRecyclerView();

        // Bottom Nav
        bottom_nav_home();

        // Set current user name and image

        if (mCurrentUserData.getmUserImage() != null)
        {
            if (mCurrentUserData.getmUserImage().contains("gs://test-b8daf.appspot.com/UserImage")) {
                FirebaseStorage mStorage = FirebaseStorage.getInstance();
                StorageReference mImageReference = mStorage.getReferenceFromUrl(mCurrentUserData.getmUserImage());
                mImageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(getApplicationContext()).load(uri).fit().into(CurrentUserImage);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            }
            else {
                Picasso.with(getApplicationContext()).load(mCurrentUserData.getmUserImage()).fit().into(CurrentUserImage);
            }
        }
        /*
        Log.d("Testing","");
        System.out.println("Execution time: "+(testEndTime-testStartTime));
        //Testing
        ImageView t = findViewById(R.id.showPlan_current_user_image);
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String test = ("FullCost: "+plan.planData.fullCost+"\nWasteTime: "+plan.planData.wasteTime+"\nTravelTime: "+plan.planData.travelTime
                +"\nIntial plan value: "+Planner.intialPlanValue+"\nFinal plan value: "+Planner.finalPlanValue+"\nExecution time: "+(testEndTime-testStartTime));
                Toast.makeText(showPlan.this,test,Toast.LENGTH_LONG).show();
            }
        });
*/
        ImageView t = findViewById(R.id.showPlan_current_user_image);
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String test = ("FullCost: "+plan.planData.fullCost+"\nWasteTime: "+plan.planData.wasteTime+"\nTravelTime: "+plan.planData.travelTime
                        +"\nIntial plan value: "+Planner.intialPlanValue+"\nFinal plan value: "+Planner.finalPlanValue+"\nExecution time: "+(testEndTime-testStartTime));
                Toast.makeText(showPlan.this,test,Toast.LENGTH_LONG).show();
            }
        });
    }


    // *---------------------------------*  Initialize Data  *---------------------------------*
    private void initVariables()
    {
        final CurrentUserData curr=(CurrentUserData)getApplicationContext();
        final TextView planName = findViewById(R.id.plan_name2);
        planName.setText(getIntent().getStringExtra("planName"));
        final String planname=getIntent().getStringExtra("planName");
        POIImageURLs = new ArrayList<String>();
        POINames = new ArrayList<String>();
        POIStartTimes = new ArrayList<String>();
        POIEndTimes = new ArrayList<String>();
        POIDescribtions = new ArrayList<String>();
        POICosts = new ArrayList<String>();
        POIDurations = new ArrayList<String>();
        POIIDs = new ArrayList<String>();
        favoriteList = new ArrayList<Integer>();
        userPrefrences = new ArrayList<String>();
        POIs = Data.POIsData(favoriteList,userPrefrences);
        arrPlaces = new String[POIs.size()];
        // Get all POI names sorted by id
        for(int i=0;i<POIs.size();i++)
            arrPlaces[i] = POIs.get(i).getName();

        place1 = (Button)findViewById(R.id.place1);
        place2 = (Button)findViewById(R.id.place2);
        travelTime = (TextView)findViewById(R.id.travel_time);
        place1.setText(POIs.get(0).getName());
        place2.setText(POIs.get(1).getName());
        travelTime.setText(""+POIs.get(0).getShortestPath(id2));
        place1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPlace(place1);
            }
        });
        place2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPlace(place2);
            }
        });
        ImageButton addButton = (ImageButton) findViewById(R.id.add_poi);
        addButton.setOnClickListener(new View.OnClickListener() {
            // Add new POI in plan
            @Override
            public void onClick(View v) {
                addItems();
            }
        });

        ImageButton saveButton = (ImageButton) findViewById(R.id.save_plan);
        saveButton.setOnClickListener(new View.OnClickListener() {
            // Save current plan
            @Override
            public void onClick(View v) {
                try {
                    // Test if plan empty
                    int test = 3/POINames.size();
                    // Cal full cost
                    double fullCost = 0;
                    for (int i = 0; i < POICosts.size(); i++)
                        fullCost += Double.parseDouble(POICosts.get(i));

                    // Cal fullDuration, trip start and end times
                    int fullDuration = 0;
                    Time tripStartTime = new Time(23, 59), tripEndTime = new Time(0, 0);
                    for (int i = 0; i < POIEndTimes.size(); i++) {
                        int h, m;
                        h = Integer.parseInt(POIStartTimes.get(i).substring(0, POIStartTimes.get(i).indexOf(":")));
                        String part = POIStartTimes.get(i).substring(POIStartTimes.get(i).indexOf(" ") + 1);
                        h = CF12T24(h, part);
                        m = Integer.parseInt(POIStartTimes.get(i).substring(POIStartTimes.get(i).indexOf(":") + 1, POIStartTimes.get(i).length() - 3));
                        Time startTime = new Time(h, m);
                        h = Integer.parseInt(POIEndTimes.get(i).substring(0, POIEndTimes.get(i).indexOf(":")));
                        part = POIEndTimes.get(i).substring(POIEndTimes.get(i).indexOf(" ") + 1);
                        h = CF12T24(h, part);
                        m = Integer.parseInt(POIEndTimes.get(i).substring(POIEndTimes.get(i).indexOf(":") + 1, POIEndTimes.get(i).length() - 3));
                        Time endTime = new Time(h, m);
                        fullDuration += Time.substract(startTime, endTime);
                        tripStartTime = Time.min(tripStartTime, startTime);
                        tripEndTime = Time.max(tripEndTime, endTime);
                    }

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference tripref = database.getReference().child("Trips");
                    DatabaseReference user = database.getReference().child("Users");
                    DatabaseReference myRef;
                    final String finalTripStartTime = CF24T12(tripStartTime.hour,tripStartTime.min);
                    final String finalTripEndTime = CF24T12(tripEndTime.hour,tripEndTime.min);
                    final int finalFullDuration = fullDuration;
                    final double finalFullCost = fullCost;
                    // Add plan in database (POINames,POIStartTimes,POIEndTimes,POIDescribtions,POICosts,POIDurations,fullCost,fullDuration,tripStartTime,tripEndTime)
                    myRef = tripref.push();
                    user.child(curr.getmCurrentUserId()).child("TripsId").push().setValue(myRef.getKey());
                    myRef.child("PoisNames").setValue(POINames);
                    myRef.child("PoisId").setValue(POIIDs);
                    myRef.child("PlanNames").setValue(planname);
                    myRef.child("POIStartTimes").setValue(POIStartTimes);
                    myRef.child("POIEndTimes").setValue(POIEndTimes);
                    myRef.child("POIDescribtions").setValue(POIDescribtions);
                    myRef.child("POICosts").setValue(POICosts);
                    myRef.child("POIDurations").setValue(POIDurations);
                    myRef.child("fullCost").setValue(finalFullCost);
                    myRef.child("fullDuration").setValue(finalFullDuration);
                    myRef.child("tripStartTime").setValue(finalTripStartTime);
                    myRef.child("tripEndTime").setValue(finalTripEndTime);
                    myRef.child("NumOfExe").push().setValue(curr.getmCurrentUserId());
                    myRef.child("TripDate").setValue("");
                    myRef.child("TripOnDashBord").setValue("False");
                    myRef.child("TripPlace").setValue("In Luxor");
                    myRef.child("TripRate").setValue("3");
                    myRef.child("UploadedBy").setValue(curr.getmCurrentUserId());

                    // Switch to home page
                    startActivity(new Intent(getApplicationContext(), home.class));
                    overridePendingTransition(0, 0);
                    finish();

                } catch (Exception e){
                    Toast.makeText(showPlan.this,"Something went wrong. Note you can't save empty plan",Toast.LENGTH_LONG).show();
                }

        }
        });
        // Set current user name and image
        mCurrentUserData = (CurrentUserData) getApplicationContext();
        CurrentUserImage = findViewById(R.id.showPlan_current_user_image);
    }

    // *---------------------------------*  Initialize Recycler View  *---------------------------------*
    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recycleview);
        RecyclerPlanAdapter recyclerViewAdapter = new RecyclerPlanAdapter(this,POINames,POIStartTimes,POIEndTimes,POIDescribtions,POICosts,POIDurations,POIIDs,true,POIImageURLs);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


    // *---------------------------------*  Bottom Nav  *---------------------------------*
    public void bottom_nav_home(){
        BottomNavigationView btn_nav = findViewById(R.id.bottom_nav);
        btn_nav.setSelectedItemId(R.id.add_plan);

        btn_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        startActivity(new Intent(getApplicationContext(), home.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.navigation_dashboard:
                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.navigation_profile:
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.add_plan:
                        startActivity(new Intent(getApplicationContext(), Constrains.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.navigation_places:
                        startActivity(new Intent(getApplicationContext(), PlacesActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                }
                return false;
            }
        });

    }

    // *---------------------------------*  Fun to read json file  *---------------------------------*
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


    public void convertFromStringToInt (ArrayList<Integer> intL,ArrayList<String> sL){
        for(int i=0;i<sL.size();i++) {
            if(!sL.get(i).equals(""))
                intL.add(Integer.parseInt(sL.get(i)));
        }
    }
    // *---------------------------------*  Change format from 24 to 12    *---------------------------------*
    public String CF24T12(int H,int M){
        String s ;
        if(H>=0&&H<12) {
            H = (H==0)?12:H;
            s = "" + H + ":" + M+" AM";
        }
        else{
            H = (H==12)?12:H%12;
            s = "" + H + ":" + M+" PM";
        }
        return s;
    }
    // *---------------------------------*  Change format from 12 to 24    *---------------------------------*
    public int CF12T24(int H,String type){
        if(type.equals("AM")) {
            if(H==12)
                H = 0;
        }
        else{
            if(H!=12)
                H = 12 + H;
        }
        return H;
    }

    // *---------------------------------*  Add place in plan  *---------------------------------*
    public void addItems(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        final  View edit_Sch = getLayoutInflater().inflate(R.layout.add_place,null);
        builder.setView(edit_Sch);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.time, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final TextView startTimePart = (TextView) edit_Sch.findViewById(R.id.add_starttime_part);
        final TextView endTimePart = (TextView) edit_Sch.findViewById(R.id.add_endtime_part);
        Spinner spinner = edit_Sch.findViewById(R.id.add_starttime_part_spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                startTimePart.setText(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner = edit_Sch.findViewById(R.id.add_endtime_part_spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                endTimePart.setText(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final Button startTimehour,startTimemin,endTimehour,endTimemin;
        startTimehour = edit_Sch.findViewById(R.id.add_starttime_hour);
        startTimemin = edit_Sch.findViewById(R.id.add_starttime_min);
        endTimehour = edit_Sch.findViewById(R.id.add_endtime_hour);
        endTimemin = edit_Sch.findViewById(R.id.add_endtime_min);
        startTimehour.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                selectHour(startTimehour);
            }
        });
        startTimemin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                selectMin(startTimemin);
            }
        });
        endTimehour.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                selectHour(endTimehour);
            }
        });
        endTimemin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                selectMin(endTimemin);
            }
        });

        slctPlaceAdd = (TextView)edit_Sch.findViewById(R.id.select_place_add);
        slctPlaceAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                selectPlaceAdd();
            }
        });
        slctPlaceNum = (TextView)edit_Sch.findViewById(R.id.select_place_Num_add);
        slctPlaceNum.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                selectPlaceNum();
            }
        });

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Button startTimehour,startTimemin,endTimehour,endTimemin;
                startTimehour = edit_Sch.findViewById(R.id.add_starttime_hour);
                startTimemin = edit_Sch.findViewById(R.id.add_starttime_min);
                endTimehour = edit_Sch.findViewById(R.id.add_endtime_hour);
                endTimemin = edit_Sch.findViewById(R.id.add_endtime_min);
                int index = Integer.parseInt(slctPlaceNum.getText().toString())-1;
                POINames.add(index,slctPlaceAdd.getText().toString());
                POIIDs.add(index,getID(slctPlaceAdd.getText().toString()));
                POIStartTimes.add(index,startTimehour.getText().toString()+":"+startTimemin.getText().toString()+" "+startTimePart.getText().toString());
                POIEndTimes.add(index,endTimehour.getText().toString()+":"+endTimemin.getText().toString()+" "+endTimePart.getText().toString());
                POI poi =null;
                String name = slctPlaceAdd.getText().toString();
                for(int i=0;i<POIs.size();i++){
                    if(POIs.get(i).getName().equals(name)){
                        poi = POIs.get(i);
                        break;
                    }
                }
                POIDescribtions.add(index,POIs.get(Integer.parseInt(POIIDs.get(index))-1).desc);
                POICosts.add(index,""+poi.getCost());
                int h = Integer.parseInt(startTimehour.getText().toString());
                h = CF12T24(h,startTimePart.getText().toString());
                Time startTime = new Time(h,Integer.parseInt(startTimemin.getText().toString()));
                h = Integer.parseInt(endTimehour.getText().toString());
                h = CF12T24(h,endTimePart.getText().toString());
                Time endTime = new Time(h,Integer.parseInt(endTimemin.getText().toString()));
                POIDurations.add(index,""+Time.substract(startTime,endTime));
                POIImageURLs.add(index,POIs.get(Integer.parseInt(POIIDs.get(index))-1).photoURL);
                dialog.dismiss();
                initRecyclerView();
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog mdialog = builder.create();
        mdialog.show();
    }

    // *---------------------------------*  Get POI ID by his name  *---------------------------------*
    public String getID(String name){
        for(int i=0;i<POIs.size();i++){
            if(name.equals(POIs.get(i).getName())){
                return ""+POIs.get(i).getId();
            }
        }
        return "";
    }

    public void editItems(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        final  View edit_Sch = getLayoutInflater().inflate(R.layout.edit_on_schedule,null);
        builder.setView(edit_Sch);

        slctPlaceEdit = (TextView)edit_Sch.findViewById(R.id.select_place_edit);
        slctPlaceEdit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                selectPlaceEdit();
            }
        });
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog mdialog = builder.create();
        mdialog.show();
    }

    public void selectPlaceEdit(){
//        checkedPlaces = new boolean[arrPlaces.length];
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Choose Place ");
        builder.setSingleChoiceItems(arrPlaces, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                slctPlaceEdit.setText(arrPlaces[which]);
                dialog.dismiss();
            }
        });

        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog mdialog = builder.create();
        mdialog.show();
    }


    public void selectPlaceAdd(){
//        checkedPlaces = new boolean[arrPlaces.length];
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Choose Place ");
        builder.setSingleChoiceItems(arrPlaces, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                slctPlaceAdd.setText(arrPlaces[which]);
                dialog.dismiss();
            }
        });

        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog mdialog = builder.create();
        mdialog.show();
    }

    public void selectPlace(final Button button){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Choose Place ");
        builder.setSingleChoiceItems(arrPlaces, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(button.getText().toString().equals(POIs.get(id1-1).getName()))
                    id1 = which+1;
                else
                    id2 = which+1;
                button.setText(arrPlaces[which]);
                travelTime.setText(""+POIs.get(id1-1).getShortestPath(id2));
                dialog.dismiss();
            }
        });

        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog mdialog = builder.create();
        mdialog.show();
    }

    public void selectPlaceNum(){
//        checkedPlaces = new boolean[arrPlaces.length];
        indces = new String[POINames.size()+1];
        for(int i=1;i<=POINames.size()+1;i++)
            indces[i-1]=""+i;
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Choose place order ");
        builder.setSingleChoiceItems(indces, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                slctPlaceNum.setText(indces[which]);
                dialog.dismiss();
            }
        });

        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog mdialog = builder.create();
        mdialog.show();
    }

    public void selectHour(final Button button){
        final String [] arr = new String[12];
        for(int i=1;i<13;i++)
            arr[i-1] = i+"";
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Choose Hour ");
        builder.setSingleChoiceItems(arr, -1,
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                button.setText(arr[which]);
                dialog.dismiss();
            }
        });

        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog mdialog = builder.create();
        mdialog.show();
    }

    public void selectMin(final Button button){
        final String [] arr = new String[60];
        for(int i=0;i<60;i++)
            arr[i] = i+"";
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Choose Min ");
        builder.setSingleChoiceItems(arr, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                button.setText(arr[which]);
                dialog.dismiss();
            }
        });

        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog mdialog = builder.create();
        mdialog.show();
    }

}


