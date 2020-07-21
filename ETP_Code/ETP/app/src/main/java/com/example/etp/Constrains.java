package com.example.etp;
/* Imports */
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class Constrains extends AppCompatActivity {

    /* Vars */
    // Constrains layout
    private Button btn_pref;
    private Button applyBtn;
    private TextView txt_pref;
    private EditText budget,planName;
    Button startTimeHour,startTimeMin,startTimeType,endTimeHour,endTimeMin,endTimeType;
    String[] ArrItems;
    ArrayList<Integer> listItms = new ArrayList<>();
    boolean[] checkedItem ;
    ArrayList<Integer> listForbidden= new ArrayList<>();
    boolean[] checkedForbidden ;
    HashMap<String, String> forb;
    HashMap<String,String>fov;
    ArrayList<String>forbidden,favorite;
    private Button btn_forbidden;
    private Button btn_favorite;
    private TextView txt_forbidden;
    private TextView txt_favorite;
    ArrayList<Integer> listFavorite = new ArrayList<>();
    boolean[] checkedFavorite ;
    String[] arrpios;
    // Current user name and image
    CurrentUserData mCurrentUserData;
    ImageView CurrentUserImage;
    TextView CurrentUserName;
    // Ekko
    private View openEkkoChat;

    ArrayList<POI> POIs;


    // *---------------------------------*  Constrains Page    *---------------------------------*

    /* Funs */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.constrains_page);

        init();

        //Bottom Nav
        bottom_nav_home();

        // Set current user name and image
        CurrentUserName.setText(mCurrentUserData.getmUserName());
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


    }

    // *---------------------------------*  Initialize all data    *---------------------------------*
    public void init(){
        mCurrentUserData = (CurrentUserData) getApplicationContext();
        CurrentUserName = findViewById(R.id.constrains_current_user_name);
        CurrentUserImage = findViewById(R.id.constrains_current_user_image);

        POIs = POIsData(new ArrayList<Integer>(),new ArrayList<String>());
        forbidden=new ArrayList<>();
        favorite=new ArrayList<>();
        final ArrayList<String>poi=new ArrayList<>();
        planName = (EditText)findViewById(R.id.plan_name);
        startTimeHour=(Button) findViewById(R.id.start_time_hour);
        startTimeMin=(Button)findViewById(R.id.start_time_min);
        startTimeType=(Button)findViewById(R.id.start_time_type);
        endTimeHour=(Button)findViewById(R.id.end_time_hour);
        endTimeMin=(Button)findViewById(R.id.end_time_min);
        endTimeType=(Button)findViewById(R.id.end_time_type);
        btn_favorite = (Button) findViewById(R.id.favorite_places);
        btn_forbidden = (Button) findViewById(R.id.forbidden_places);
        txt_forbidden =(TextView) findViewById(R.id.forbidden_txt);
        txt_favorite =(TextView) findViewById(R.id.favorite_txt);
        txt_pref =(TextView) findViewById(R.id.preference_txt);
        budget = (EditText)findViewById(R.id.budget);
        btn_pref = (Button) findViewById(R.id.preference_btn);
        forb=mCurrentUserData.getmForbiddenPlacesList();
        fov=mCurrentUserData.getmFavoritePlacesList();
        checkedForbidden = new boolean[20];
        checkedFavorite = new boolean[20];
        startTimeHour.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                selectHour(startTimeHour);
            }
        });
        endTimeHour.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                selectHour(endTimeHour);
            }
        });
        startTimeMin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                selectMin(startTimeMin);
            }
        });
        endTimeMin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                selectMin(endTimeMin);
            }
        });
        startTimeType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTimeType(startTimeType);
            }
        });
        endTimeType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTimeType(endTimeType);
            }
        });
        btn_forbidden.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                selectforbidden();
            }
        });
        btn_favorite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                selectFavorite();
            }
        });
        btn_pref.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                selectItems();
            }
        });
        // All preferences
        ArrItems = new String[]{"Photography", "Meditation", "Archaeological","Cultural","Desert","Religious","Biography","Antiques","Walking",
                "Music", "Barbecue","swimming","Running", "Cycling","Shopping","Reading", "Draw", "Event"};
        checkedItem = new boolean[ArrItems.length];
        if(mCurrentUserData.getmForbiddenPlacesList()!=null) {
            Iterator hmIterator = forb.entrySet().iterator();

            // Iterate through the hashmap
            // and add some bonus marks for every student
            while (hmIterator.hasNext()) {
                Map.Entry mapElement = (Map.Entry) hmIterator.next();
                int marks = Integer.parseInt((String) mapElement.getValue());
                listForbidden.add(marks - 1);
                checkedForbidden[marks - 1] = true;

            }

        }
        if(mCurrentUserData.getmFavoritePlacesList()!=null) {
            Iterator  hmIterator = fov.entrySet().iterator();

            // Iterate through the hashmap
            // and add some bonus marks for every student


            while (hmIterator.hasNext()) {
                Map.Entry mapElement = (Map.Entry) hmIterator.next();
                int marks = Integer.parseInt((String) mapElement.getValue());
                listFavorite.add(marks - 1);
                checkedFavorite[marks - 1] = true;

            }


        }
        // Get all POI names
        DatabaseReference mPoisTable = FirebaseDatabase.getInstance().getReference("Pois");
        mPoisTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (final DataSnapshot mDS : dataSnapshot.getChildren()) {
                        poi.add(mDS.child("PoiName").getValue(String.class));
                        int z=poi.size();
                    }
                    arrpios=new String[poi.size()];
                    int j=0;
                    for(String s:poi)
                    {
                        arrpios[j]= s;
                        j++;
                    }
                    String s="";
                    if(!listFavorite.isEmpty())
                    {
                        for(int i=0;i<listFavorite.size()-1;i++)
                        {
                            s+=arrpios[listFavorite.get(i)]+',';
                        }
                        s+=arrpios[listFavorite.get(listFavorite.size()-1)];
                        txt_favorite.setText(s);
                    }
                    if(!listForbidden.isEmpty())
                    {
                        s="";
                        for(int i=0;i<listForbidden.size()-1;i++)
                        {
                            s+=arrpios[listForbidden.get(i)]+',';
                        }
                        s+=arrpios[listForbidden.get(listForbidden.size()-1)];
                        txt_forbidden.setText(s);
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        // To hide constraints and view your plan
        applyBtn = (Button) findViewById(R.id.apply_btn);
        applyBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                boolean e = false;
                Time startTime=new Time(0,0);
                Time endTime = new Time(0,0);
                int hour,min;
                String type;
                try {
                    hour = Integer.parseInt(startTimeHour.getText().toString());
                    min = Integer.parseInt(startTimeMin.getText().toString());
                    type = startTimeType.getText().toString();
                    hour = CF12T24(hour,type);
                    if (hour >= 0 && hour < 24 && min >= 0 && min < 60) {
                        startTime = new Time(hour, min);
                        hour = Integer.parseInt(endTimeHour.getText().toString());
                        min = Integer.parseInt(endTimeMin.getText().toString());
                        type = endTimeType.getText().toString();
                        hour = CF12T24(hour,type);
                        if (hour >= 0 && hour < 24 && min >= 0 && min < 60) {
                            endTime = new Time(hour, min);
                            if (startTime.compare(endTime)) {
                                if (!budget.getText().toString().equals("")&&!planName.getText().toString().equals(""))
                                    e = true;
                            }
                        }
                    }

                    if (e) {
                        Intent intent = new Intent(getApplicationContext(), showPlan.class);
                        intent.putExtra("planName", planName.getText().toString());
                        intent.putExtra("startTimeHour", startTime.hour);
                        intent.putExtra("startTimeMin", startTime.min);
                        intent.putExtra("endTimeHour", endTime.hour);
                        intent.putExtra("endTimeMin", endTime.min);
                        intent.putExtra("budget", Double.parseDouble(budget.getText().toString()));

                        ArrayList<String> p = new ArrayList<String>();
                        getItemsFromString(txt_pref.getText().toString(),p);
                        intent.putExtra("prefrences", p);

                        ArrayList<String> Fav = new ArrayList<String>();
                        getItemsFromString(txt_favorite.getText().toString(),Fav);
                        for(int m=0;m<Fav.size();m++)
                            Fav.set(m,getID(Fav.get(m)));
                        intent.putExtra("favoriteList",Fav);

                        ArrayList<String> For = new ArrayList<String>();
                        getItemsFromString(txt_forbidden.getText().toString(),For);
                        for(int m=0;m<For.size();m++)
                            For.set(m,getID(For.get(m)));
                        intent.putExtra("forbiddenList",For);

                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                    } else
                        Toast.makeText(Constrains.this, "Please fill the constrains with correct data", Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    Toast.makeText(Constrains.this, "Please fill the constrains with correct data", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Ekko
        openEkkoChat = findViewById(R.id.open_ekko_chat);
        openEkkoChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ekko();
            }
        });

    }

    // *---------------------------------*  Call Ekko Chatbot   *---------------------------------*
    public void ekko(){
        // Choose voice
        int x = new Random().nextInt(4);
        MediaPlayer ekkoSound;
        switch (x){
            case 0:
                ekkoSound = MediaPlayer.create(Constrains.this,R.raw.e0);
                break;
            case 1:
                ekkoSound = MediaPlayer.create(Constrains.this,R.raw.e1);
                break;
            case 2:
                ekkoSound = MediaPlayer.create(Constrains.this,R.raw.e2);
                break;
            default:
                ekkoSound = MediaPlayer.create(Constrains.this,R.raw.e3);
        }
        ekkoSound.start();

        setContentView(R.layout.ekko_chat);
        LinearLayout layout = (LinearLayout) findViewById(R.id.chat_layout);
        // Call ekko class
        Ekko ekko = new Ekko(Constrains.this,layout);
        ImageButton back = findViewById(R.id.ekko_back);
        // Set current user name and image
        CurrentUserData EkkoCurrentUserData = (CurrentUserData) getApplicationContext();
        final ImageView EkkoCurrentUserImage = findViewById(R.id.ekko_current_user_image);
        TextView EkkoCurrentUserName = findViewById(R.id.ekko_current_user_name);
        EkkoCurrentUserName.setText(EkkoCurrentUserData.getmUserName());
        if (mCurrentUserData.getmUserImage() != null)
        {
            if (mCurrentUserData.getmUserImage().contains("gs://test-b8daf.appspot.com/UserImage")) {
                FirebaseStorage mStorage = FirebaseStorage.getInstance();
                StorageReference mImageReference = mStorage.getReferenceFromUrl(EkkoCurrentUserData.getmUserImage());
                mImageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(getApplicationContext()).load(uri).fit().into(EkkoCurrentUserImage);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            }
            else {
                Picasso.with(getApplicationContext()).load(EkkoCurrentUserData.getmUserImage()).fit().into(EkkoCurrentUserImage);
            }
        }
        //Return to pervious page
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Constrains.class));
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }

    // *---------------------------------*  Bottom Nav    *---------------------------------*
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
                    case R.id.add_plan:
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

    // *---------------------------------*  Split string in the text view into items    *---------------------------------*
    public void getItemsFromString(String s,ArrayList<String> p){
        while (s != "") {
            if (s.indexOf(",") >= 0)
                p.add(s.substring(0, s.indexOf(",")));
            else {
                p.add(s.substring(0));
                break;
            }
            s = s.substring(s.indexOf(",") + 1);
        }
    }

   // *---------------------------------*  Select user's preferences in Constraints layout    *---------------------------------*
    public void selectItems(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Choose Your Preference ");
        // Select items
        builder.setMultiChoiceItems(ArrItems, checkedItem, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked){
                    listItms.add(which);
                }
                else {
                    listItms.remove((Integer.valueOf(which)));
                }
            }
        });
        builder.setCancelable(false);
        // Confirm selected items
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String item = "";
                for (int i =0;i<listItms.size();i++){
                    item = item + ArrItems[listItms.get(i)];

                    if (i != listItms.size()-1){
                        item = item +", ";
                    }
                }
                // View selected items in the text view
                txt_pref.setText(item);
            }
        });
        // Cancel the opration
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog mdialog = builder.create();
        mdialog.show();
    }

    // *---------------------------------*  Select user's forbidden places in Constraints layout    *---------------------------------*
    public void selectforbidden(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Choose Your Forbidden places:");
        // Select items
        builder.setMultiChoiceItems(arrpios, checkedForbidden, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked){
                    listForbidden.add(which);

                }
                else {
                    listForbidden.remove((Integer.valueOf(which)));
                }
            }
        });
        builder.setCancelable(false);
        // Confirm selected items
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String item = "";
                for (int i =0;i<listForbidden.size();i++){
                    item = item + arrpios[listForbidden.get(i)];
                    forbidden.add(""+(listForbidden.get(i)+1));

                    if (i != listForbidden.size()-1){
                        item = item +", ";
                    }
                }
                // View selected items in the text view
                txt_forbidden.setText(item);
            }
        });
        // Cancel the opration
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog mdialog = builder.create();
        mdialog.show();
    }
    // *---------------------------------*  Select user's favorite places in Constraints layout    *---------------------------------*
    public void selectFavorite(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Choose Your Favorite places:");
        // Select items
        builder.setMultiChoiceItems(arrpios, checkedFavorite, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked){
                    listFavorite.add(which);
                }
                else {
                    listFavorite.remove((Integer.valueOf(which)));
                }
            }
        });
        builder.setCancelable(false);
        // Confirm selected items
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String item = "";
                for (int i =0;i<listFavorite.size();i++){
                    item = item + arrpios[listFavorite.get(i)];
                    favorite.add(""+(listFavorite.get(i)+1));
                    if (i != listFavorite.size()-1){
                        item = item +", ";
                    }
                }
                // View selected items in the text view
                txt_favorite.setText(item);
            }
        });
        // Cancel the opration
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog mdialog = builder.create();
        mdialog.show();
    }
    // *---------------------------------*  Select HOUR in start and end times in Constraints layout    *---------------------------------*
    public void selectHour(final Button button){
        // Hour from 1 to 12
        final String [] arr = new String[12];
        for(int i=1;i<13;i++)
            arr[i-1] = i+"";
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Choose Hour ");
        // Select the hour
        builder.setSingleChoiceItems(arr, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                button.setText(arr[which]);
                dialog.dismiss();
            }
        });
        // Cancel the opeation
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog mdialog = builder.create();
        mdialog.show();
    }
    // *---------------------------------*  Select time type (AM,PM) in Constraints layout    *---------------------------------*
    public void selectTimeType(final Button button){
        final String [] arr = new String[2];
        arr[0] = "AM";
        arr[1] = "PM";
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Choose ");
        // Select the hour
        builder.setSingleChoiceItems(arr, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                button.setText(arr[which]);
                dialog.dismiss();
            }
        });
        // Cancel the opeation
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog mdialog = builder.create();
        mdialog.show();
    }
    // *---------------------------------*  Select MIN in start and end times in Constraints layout    *---------------------------------*
    public void selectMin(final Button button){
        // Min from 0 to 59
        final String [] arr = new String[60];
        for(int i=0;i<60;i++)
            arr[i] = i+"";
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Choose Min ");
        // Select min
        builder.setSingleChoiceItems(arr, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                button.setText(arr[which]);
                dialog.dismiss();
            }
        });
        // Cancel opreation
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog mdialog = builder.create();
        mdialog.show();
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
    // *---------------------------------*  Get POI ID by his name  *---------------------------------*
    public String getID(String name){
        for(int i=0;i<POIs.size();i++){
            if(name.equals(POIs.get(i).getName())){
                return ""+POIs.get(i).getId();
            }
        }
        return "";
    }
    // Get all POIs
    public ArrayList<POI> POIsData(ArrayList<Integer>favorite,ArrayList<String>userPreferences){
        ArrayList<POI> list = new ArrayList<POI>();
        try {
            JSONArray jArray = new JSONArray(readJSONFromAsset(this));
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
