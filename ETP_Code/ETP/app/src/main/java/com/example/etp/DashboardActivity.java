package com.example.etp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Random;

public class DashboardActivity extends AppCompatActivity {
    private RecyclerView mRecyclerDashboardList;
    private RecyclerDashboardAdapter mAdapter;
    private TextView CurrentUserName;
    ImageView CurrentUserImage;
    RelativeLayout mDashboardUserInfoLayout;
    DatabaseReference mTripTable;
    private View openEkkoChat;
    CurrentUserData mCurrentUserData;
    EditText mEditTextSearch;
    LinearLayout mLayoutSearchList;
    ImageButton mSearchButton;
    ProgressBar progressBar;

    String SearchText;
    RelativeLayout mLayoutDashboardNoPlans;


    @SuppressLint({"ResourceType", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        initVariables();

        CurrentUserName.setText(mCurrentUserData.getmUserName());
        if (mCurrentUserData.getmUserImage() != null) {
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
//        mSearchButton.performClick();
        fillVariableTripData();
        SearchPlace();
        bottom_nav_dashboard();
    }


    // Function to initialize variables.
    private void initVariables() {
        SearchText = "All";
        mCurrentUserData = (CurrentUserData) getApplicationContext();
        CurrentUserName = findViewById(R.id.dashboard_current_user_name);
        CurrentUserImage = findViewById(R.id.dashboard_current_user_image);
        progressBar = findViewById(R.id.dashboard_loading_progress_bar);

        mDashboardUserInfoLayout = findViewById(R.id.txtLayout);
        mTripTable = FirebaseDatabase.getInstance().getReference("Trips");
        mRecyclerDashboardList = findViewById(R.id.recycler_view_dashboard);
        openEkkoChat = findViewById(R.id.open_ekko_chat);
        openEkkoChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ekko();
            }
        });
        mEditTextSearch = findViewById(R.id.dashboard_search_text);
//        mLayoutSearchList = findViewById(R.id.dashboard_search_list);
        mSearchButton = findViewById(R.id.dashboard_search_button);
        mLayoutDashboardNoPlans = findViewById(R.id.dashboard_no_plans);
    }

    // Function to get trips data from firebase
    private void fillVariableTripData() {
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mEditTextSearch.getText().toString())){
                    SearchText = "All";
                }
                else {
                    SearchText = mEditTextSearch.getText().toString();
                }
                InputMethodManager inputManager = (InputMethodManager) DashboardActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(mEditTextSearch.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

                mTripTable.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        progressBar.setVisibility(View.VISIBLE);

                        if (dataSnapshot.exists()) {


                            ArrayList<String> mTripsIdList = new ArrayList<>();
                            ArrayList<String> mTripsImageList = new ArrayList<>();
                            ArrayList<String> mTripsNameList = new ArrayList<>();
                            ArrayList<String> mPostsTimeList = new ArrayList<>();
                            ArrayList<String> mTripsRateList = new ArrayList<>();
                            ArrayList<String> mTripsPlaceList = new ArrayList<>();
                            ArrayList<String> mTripsBudgetList = new ArrayList<>();
                            ArrayList<String> mTripsNumOfExeList = new ArrayList<>();
                            ArrayList<String> mTripsNumOfCommentList = new ArrayList<>();


                            for (final DataSnapshot mDS:dataSnapshot.getChildren()) {
                                try {
                                    if (mDS.child("TripOnDashBord").getValue(String.class).equals("True")) {
                                        ArrayList<String> mTripPoisNameList = (ArrayList<String>) mDS.child("PoisNames").getValue();
                                        mTripPoisNameList.add("All");
                                        if (mTripPoisNameList.contains(SearchText)) {
                                            mTripsIdList.add(mDS.getKey());
                                            mTripsImageList.add(R.drawable.luxor_image + "");
                                            mTripsNameList.add(mDS.child("PlanNames").getValue(String.class));
                                            mPostsTimeList.add((mDS.child("fullDuration").getValue(Integer.class) / 60) + " h");
                                            mTripsRateList.add(mDS.child("TripRate").getValue(String.class));
                                            mTripsPlaceList.add(mDS.child("TripPlace").getValue(String.class));
                                            mTripsBudgetList.add(String.format("%.2f", mDS.child("fullCost").getValue(Double.class)));
                                            mTripsNumOfExeList.add(mDS.child("NumOfExe").getChildrenCount() + "");
                                            mTripsNumOfCommentList.add(mDS.child("tripCommentsId").getChildrenCount() + "");
                                        }
                                    }
                                }
                                catch (Exception e)
                                {
                                    Log.d("Dashboard Exeption", e.getMessage());
                                }
                            }


                            if (mTripsIdList.size() > 0) {
                                mLayoutDashboardNoPlans.setLayoutParams(new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                                mAdapter = new RecyclerDashboardAdapter(DashboardActivity.this, mTripsIdList, mTripsImageList, mTripsNameList, mPostsTimeList, mTripsRateList, mTripsPlaceList, mTripsBudgetList, mTripsNumOfExeList, mTripsNumOfCommentList);
                                mRecyclerDashboardList.setAdapter(mAdapter);
                                mRecyclerDashboardList.setLayoutManager(new LinearLayoutManager(DashboardActivity.this));
                            }
                            else {
                                mLayoutDashboardNoPlans.setLayoutParams(new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                                mRecyclerDashboardList.setAdapter(null);
                                mRecyclerDashboardList.setLayoutManager(new LinearLayoutManager(DashboardActivity.this));
                            }

                        }
                        progressBar.setVisibility(View.INVISIBLE);

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        mSearchButton.performClick();
    }

    // ekko Function
    public void ekko(){
        int x = new Random().nextInt(4);
        MediaPlayer ekkoSound;
        switch (x){
            case 0:
                ekkoSound = MediaPlayer.create(DashboardActivity.this,R.raw.e0);
                break;
            case 1:
                ekkoSound = MediaPlayer.create(DashboardActivity.this,R.raw.e1);
                break;
            case 2:
                ekkoSound = MediaPlayer.create(DashboardActivity.this,R.raw.e2);
                break;
            default:
                ekkoSound = MediaPlayer.create(DashboardActivity.this,R.raw.e3);
        }
        ekkoSound.start();
        setContentView(R.layout.ekko_chat);
        LinearLayout layout = (LinearLayout) findViewById(R.id.chat_layout);
        Ekko ekko = new Ekko(DashboardActivity.this,layout);
        ImageButton back = findViewById(R.id.ekko_back);
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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }

    // Bottom nav Function
    public void bottom_nav_dashboard(){
        BottomNavigationView btn_nav = findViewById(R.id.bottom_nav);
        btn_nav.setSelectedItemId(R.id.navigation_dashboard);

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
                        startActivity(new Intent(getApplicationContext(), Constrains.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.navigation_dashboard:
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

    // Search Function
    public void SearchPlace(){
//        mEditTextSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                InputMethodManager inputManager = (InputMethodManager) DashboardActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
////                inputManager.hideSoftInputFromWindow(DashboardActivity.this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
//
//                if(mLayoutSearchList.getVisibility() == View.INVISIBLE)
//                {
//                    mLayoutSearchList.setVisibility(View.VISIBLE);
//
//                }
//
//            }
//        });
    }

}
