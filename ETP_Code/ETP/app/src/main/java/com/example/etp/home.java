package com.example.etp;

/* Imports */
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

// *---------------------------------*  Home Page    *---------------------------------*
public class home extends AppCompatActivity {

    /* Vars */

    // Recycler view vars
    private ArrayList<String> PlanIDs;
    ArrayList<String> mPlanNamesList, mfullCostList, mfullDurationList, mtripEndTimeList, mtripStartTimeList, mPlansIdsList;
    ArrayList<ArrayList<String>> mPOICostsListOfList, mPOIDescribtionsListOfList, mPOIDurationsListOfList, mPOIEndTimesListOfList, mPOIStartTimesListOfList, mPoisIdListOfList, mPoisNamesListOfList;
    ArrayList<String> mPOICostsList, mPOIDescribtionsList, mPOIDurationsList, mPOIEndTimesList, mPOIStartTimesList, mPoisIdList, mPoisNamesList, mTripInDashboardList, mUploadedByList;

    // Ekko
    private View openEkkoChat;

    // Current account
    CurrentUserData mCurrentUserData;
    ImageView CurrentUserImage;
    TextView CurrentUserName;
    ProgressBar progressBar;

    // Database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference mUserTable;

    RelativeLayout mLayoutHomeNoPlans;

//    ProgressDialog mProgressDialog;

    /* Funs */

    @SuppressLint("LongLogTag")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        init();

        // Get user's plans from database
        DatabaseReference myRef;
        if (mCurrentUserData.getmCurrentUserId() == null) {
//            setUserDataInGlobal(mAuth.getUid());
            myRef = database.getReference("Users").child(mAuth.getUid());

        }
        else {
            myRef = database.getReference("Users").child(mCurrentUserData.getmCurrentUserId());
        }

        // Get overview plans's data
        myRef.child("TripsId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
//                    mProgressDialog = new ProgressDialog(home.this);
//                    mProgressDialog.show();
//                    mProgressDialog.setContentView(R.layout.progress_dialog);
//                    mProgressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    for (DataSnapshot mDS:dataSnapshot.getChildren()) {
                        PlanIDs.add(mDS.getValue(String.class));
                        Log.d("gemian home Trips id: ", mDS.getValue(String.class));
                    }
                    for(final String key:PlanIDs) {
                        final DatabaseReference trip = database.getReference("Trips").child(key);
                        Log.d("gemian home Trips id: ", key);
                        trip.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot DS2) {
                                progressBar.setVisibility(View.VISIBLE);
                                if (DS2.exists()) {
                                    try {
                                        mPOICostsList = null;
                                        mPOIDescribtionsList = null;
                                        mPOIDurationsList = null;
                                        mPOIEndTimesList = null;
                                        mPOIStartTimesList = null;
                                        mPoisIdList = null;
                                        mPoisNamesList = null;

                                        mPOICostsList = (ArrayList<String>) DS2.child("POICosts").getValue();
                                        mPOIDescribtionsList = (ArrayList<String>) DS2.child("POIDescribtions").getValue();
                                        mPOIDurationsList = (ArrayList<String>) DS2.child("POIDurations").getValue();
                                        mPOIEndTimesList = (ArrayList<String>) DS2.child("POIEndTimes").getValue();
                                        mPOIStartTimesList = (ArrayList<String>) DS2.child("POIStartTimes").getValue();
                                        mPoisIdList = (ArrayList<String>) DS2.child("PoisId").getValue();
                                        mPoisNamesList = (ArrayList<String>) DS2.child("PoisNames").getValue();

                                        mPOICostsListOfList.add(mPOICostsList);
                                        mPOIDescribtionsListOfList.add(mPOIDescribtionsList);
                                        mPOIDurationsListOfList.add(mPOIDurationsList);
                                        mPOIEndTimesListOfList.add(mPOIEndTimesList);
                                        mPOIStartTimesListOfList.add(mPOIStartTimesList);
                                        mPoisIdListOfList.add(mPoisIdList);
                                        mPoisNamesListOfList.add(mPoisNamesList);

                                        mPlanNamesList.add(DS2.child("PlanNames").getValue(String.class));
                                        mfullCostList.add(DS2.child("fullCost").getValue(Double.class).toString());
                                        mfullDurationList.add(DS2.child("fullDuration").getValue(Double.class).toString());
                                        mtripEndTimeList.add(DS2.child("tripEndTime").getValue(String.class));
                                        mtripStartTimeList.add(DS2.child("tripStartTime").getValue(String.class));
                                        mTripInDashboardList.add(DS2.child("TripOnDashBord").getValue(String.class));
                                        mUploadedByList.add(DS2.child("UploadedBy").getValue(String.class));
                                        mPlansIdsList.add(key);
//                                        mProgressDialog.dismiss();
                                    }
                                    catch (Exception ex){

                                    }
                                }

                                if (mPlanNamesList.size() > 0) {
                                    mLayoutHomeNoPlans.setLayoutParams(new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                                    RecyclerView recyclerView = findViewById(R.id.plans_recycleview);
                                    HomeRecyclerViewAdapter recyclerViewAdapter = new HomeRecyclerViewAdapter(home.this, mPOICostsListOfList, mPOIDescribtionsListOfList, mPOIDurationsListOfList, mPOIEndTimesListOfList, mPOIStartTimesListOfList, mPoisIdListOfList, mPoisNamesListOfList, mPlanNamesList, mfullCostList, mfullDurationList, mtripEndTimeList, mtripStartTimeList, mPlansIdsList, mTripInDashboardList, mUploadedByList);
                                    recyclerView.setAdapter(recyclerViewAdapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(home.this));
                                }
                                else {
                                    mLayoutHomeNoPlans.setLayoutParams(new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                                }

                                progressBar.setVisibility(View.INVISIBLE);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError DE) {

                            }
                        });
                    }
                }
                else {

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

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

        // Set nav bar
        bottom_nav_home();
    }

    // *---------------------------------*  Initialize all data    *---------------------------------*
    public void init(){

        mCurrentUserData = (CurrentUserData) getApplicationContext();
        CurrentUserName = findViewById(R.id.home_current_user_name);
        CurrentUserImage = findViewById(R.id.home_current_user_image);
        progressBar = findViewById(R.id.home_loading_progress_bar);
        PlanIDs = new ArrayList<String>();
        openEkkoChat = findViewById(R.id.open_ekko_chat);
        mLayoutHomeNoPlans = findViewById(R.id.home_no_plans);
        openEkkoChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ekko();
            }
        });

        mPOICostsListOfList = new ArrayList<ArrayList<String>>();
        mPOIDescribtionsListOfList = new ArrayList<ArrayList<String>>();
        mPOIDurationsListOfList = new ArrayList<ArrayList<String>>();
        mPOIEndTimesListOfList = new ArrayList<ArrayList<String>>();
        mPOIStartTimesListOfList = new ArrayList<ArrayList<String>>();
        mPoisIdListOfList = new ArrayList<ArrayList<String>>();
        mPoisNamesListOfList = new ArrayList<ArrayList<String>>();

        mPlanNamesList = new ArrayList<String>();
        mfullCostList = new ArrayList<String>();
        mfullDurationList = new ArrayList<String>();
        mtripEndTimeList = new ArrayList<String>();
        mtripStartTimeList = new ArrayList<String>();
        mPlansIdsList = new ArrayList<String>();
        mTripInDashboardList = new ArrayList<String>();
        mUploadedByList = new ArrayList<>();

    }

    // *---------------------------------*  Call ekko chat bot    *---------------------------------*
    public void ekko(){
        /* Part 1 */
        // Choose voice
        int x = new Random().nextInt(4);
        MediaPlayer ekkoSound;
        switch (x){
            case 0:
                ekkoSound = MediaPlayer.create(home.this,R.raw.e0);
                break;
            case 1:
                ekkoSound = MediaPlayer.create(home.this,R.raw.e1);
                break;
            case 2:
                ekkoSound = MediaPlayer.create(home.this,R.raw.e2);
                break;
            default:
                ekkoSound = MediaPlayer.create(home.this,R.raw.e3);
        }
        ekkoSound.start();
        /* Part 2 */
        setContentView(R.layout.ekko_chat);
        LinearLayout layout = (LinearLayout) findViewById(R.id.chat_layout);
        ImageButton back = findViewById(R.id.ekko_back);
        /* Part 3 */
        // Call ekko class
        Ekko ekko = new Ekko(home.this,layout);
        /* Part 4 */
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
                startActivity(new Intent(getApplicationContext(), home.class));
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }

    // *---------------------------------*  Bottom Nav    *---------------------------------*
    public void bottom_nav_home(){
        BottomNavigationView btn_nav = findViewById(R.id.bottom_nav);
        btn_nav.setSelectedItemId(R.id.navigation_home);
        btn_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        return true;
                    case R.id.add_plan:
                        startActivity(new Intent(getApplicationContext(), Constrains.class));
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

    @Override
    protected void onStop() {
        super.onStop();
    }


    private void setUserDataInGlobal(final String currentUserId)  {
        mCurrentUserData = (CurrentUserData) getApplicationContext();
        mUserTable = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId);
        mUserTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mCurrentUserData.setmCurrentUserId(currentUserId);
                    mCurrentUserData.setmFirebaseUser(mAuth.getCurrentUser());
                    if (dataSnapshot.hasChild("userEmail")) {
                        mCurrentUserData.setmUserEmail(dataSnapshot.child("userEmail").getValue(String.class));
                    }
                    if (dataSnapshot.hasChild("userPassword")) {
                        mCurrentUserData.setmUserPassword(dataSnapshot.child("userPassword").getValue(String.class));
                    }
                    if (dataSnapshot.hasChild("userName")) {
                        mCurrentUserData.setmUserName(dataSnapshot.child("userName").getValue(String.class));
                    }
                    if (dataSnapshot.hasChild("userRegisterDate")) {
                        mCurrentUserData.setmRegisterDate(dataSnapshot.child("userRegisterDate").getValue(Long.class).toString());
                    }
                    if (dataSnapshot.hasChild("userAddress")) {
                        mCurrentUserData.setmUserAddress(dataSnapshot.child("userAddress").getValue(String.class));
                    }
                    if (dataSnapshot.hasChild("userID")) {
                        mCurrentUserData.setmUserID(dataSnapshot.child("userID").getValue(Integer.class).toString());
                    }
                    if (dataSnapshot.hasChild("userImage")) {
                        mCurrentUserData.setmUserImage(dataSnapshot.child("userImage").getValue(String.class));
                    }
                    if (dataSnapshot.hasChild("activeTrip")) {
//                        mCurrentUserData.setmActiveTrip(dataSnapshot.child("activeTrip").getValue(Integer.class).toString());
                    }
                    if (dataSnapshot.hasChild("userPhone")) {
                        mCurrentUserData.setmUserPhone(dataSnapshot.child("userPhone").getValue(String.class));
                    }
                    if (dataSnapshot.hasChild("userRatingApp")) {
                        mCurrentUserData.setmUserRatingApp(dataSnapshot.child("userRatingApp").getValue(String.class));
                    }
                    if (dataSnapshot.hasChild("tripsId")) {
                        mCurrentUserData.setmTripsIdList((HashMap<String, String>) dataSnapshot.child("tripsId").getValue());
                    }
                    if (dataSnapshot.hasChild("favoritePlaces")) {
                        mCurrentUserData.setmFavoritePlacesList((HashMap<String, String>) dataSnapshot.child("favoritePlaces").getValue());
                    }
                    if (dataSnapshot.hasChild("forbiddenPlaces")) {
                        mCurrentUserData.setmForbiddenPlacesList((HashMap<String, String>) dataSnapshot.child("forbiddenPlaces").getValue());
                    }
                    if (dataSnapshot.hasChild("preferences")) {
                        mCurrentUserData.setmPerfrancesList((ArrayList<String>) dataSnapshot.child("preferences").getValue());
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}