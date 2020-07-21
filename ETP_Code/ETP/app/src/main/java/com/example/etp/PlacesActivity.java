package com.example.etp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.etp.adapters.RecyclerPlacesAdapter;
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

public class PlacesActivity extends AppCompatActivity {
    private RecyclerView mRecyclerPlaceList;
    private ArrayList<String> mPoiCloseTimeList, mPoiOpenTimeList, mPoiPoiDescriptionList, mPoiPoiIDList, mPoiPoiLatitudeList, mPoiPoiLongitudeList, mPoiPoiNameList, mPoiPoiPhoneList, mPoiPreferencesList, mPoiPoiPriceList, mPoiTypeList, mPoiVisitTimeList, mPoiWorkingTimeList, mPoiAddressList, mPoiLocationList, mPoiURLImageList, mPoiRateList;
    private RecyclerPlacesAdapter mAdapter;
    private TextView mCurrentUserName;
    private ImageView mCurrentUserImage;
    DatabaseReference mPoisTable;
    FirebaseStorage mStorage;
    CurrentUserData mCurrentUserData;
    private View openEkkoChat;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        initVariables();

        mCurrentUserName.setText(mCurrentUserData.getmUserName());
        if (mCurrentUserData.getmUserImage() != null) {
            if (mCurrentUserData.getmUserImage().contains("gs://test-b8daf.appspot.com/UserImage")) {
                FirebaseStorage mStorage = FirebaseStorage.getInstance();
                StorageReference mImageReference = mStorage.getReferenceFromUrl(mCurrentUserData.getmUserImage());
                mImageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(getApplicationContext()).load(uri).fit().into(mCurrentUserImage);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            }
            else {
                Picasso.with(getApplicationContext()).load(mCurrentUserData.getmUserImage()).fit().into(mCurrentUserImage);
            }
        }

        fillVariablePoisData();
        bottom_nav_profile();

        mAdapter = new RecyclerPlacesAdapter(getApplicationContext(), mPoiCloseTimeList, mPoiOpenTimeList, mPoiPoiDescriptionList, mPoiPoiIDList, mPoiPoiLatitudeList, mPoiPoiLongitudeList, mPoiPoiNameList, mPoiPoiPhoneList, mPoiPreferencesList, mPoiPoiPriceList, mPoiTypeList, mPoiVisitTimeList, mPoiWorkingTimeList, mPoiAddressList, mPoiLocationList, mPoiURLImageList, mPoiRateList);
        mRecyclerPlaceList.setAdapter(mAdapter);
        mRecyclerPlaceList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.notifyDataSetChanged();
    }

    private void initVariables() {
        mCurrentUserData = (CurrentUserData) getApplicationContext();
        mCurrentUserName = findViewById(R.id.places_current_user_name);
        mCurrentUserImage = findViewById(R.id.places_current_user_image);
        mPoisTable = FirebaseDatabase.getInstance().getReference("Pois");
        mRecyclerPlaceList = findViewById(R.id.places_recycler_view);
        progressBar = findViewById(R.id.places_loading_progress_bar);
        mPoiCloseTimeList = new ArrayList<>();
        mPoiOpenTimeList = new ArrayList<>();
        mPoiPoiDescriptionList = new ArrayList<>();
        mPoiPoiIDList = new ArrayList<>();
        mPoiPoiLatitudeList = new ArrayList<>();
        mPoiPoiLongitudeList = new ArrayList<>();
        mPoiPoiNameList = new ArrayList<>();
        mPoiPoiPhoneList = new ArrayList<>();
        mPoiPreferencesList = new ArrayList<>();
        mPoiPoiPriceList = new ArrayList<>();
        mPoiTypeList = new ArrayList<>();
        mPoiVisitTimeList = new ArrayList<>();
        mPoiWorkingTimeList = new ArrayList<>();
        mPoiAddressList = new ArrayList<>();
        mPoiLocationList = new ArrayList<>();
        mPoiURLImageList = new ArrayList<>();
        mPoiRateList = new ArrayList<>();
        mStorage = FirebaseStorage.getInstance();
        openEkkoChat = findViewById(R.id.open_ekko_chat);
        openEkkoChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ekko();
            }
        });
    }

    private void fillVariablePoisData() {
        mPoisTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    progressBar.setVisibility(View.VISIBLE);
                    for (final DataSnapshot mDS:dataSnapshot.getChildren())
                    {
                        mPoiCloseTimeList.add(mDS.child("CloseTime").getValue(String.class));
                        mPoiOpenTimeList.add(mDS.child("OpenTime").getValue(String.class));
                        mPoiPoiDescriptionList.add(mDS.child("PoiDescription").getValue(String.class));
                        mPoiPoiIDList.add(mDS.child("PoiID").getValue(String.class));
                        mPoiPoiLatitudeList.add(mDS.child("PoiLatitude").getValue(String.class));
                        mPoiPoiLongitudeList.add(mDS.child("PoiLongitude").getValue(String.class));
                        mPoiPoiNameList.add(mDS.child("PoiName").getValue(String.class));
                        mPoiPoiPhoneList.add(mDS.child("PoiPhone").getValue(String.class));
                        mPoiPoiPriceList.add(mDS.child("PoiPrice").getValue(String.class));
                        mPoiVisitTimeList.add(mDS.child("VisitTime").getValue(String.class));
                        mPoiWorkingTimeList.add(mDS.child("WorkingTime").getValue(String.class));
                        mPoiRateList.add(mDS.child("PoiRate").getValue(String.class));
                        mPoiAddressList.add(mDS.child("PoiAddress").getValue(String.class));
                        mPoiLocationList.add("In "+  mDS.child("PoiLocation").getValue(String.class));
                        mPoiURLImageList.add(mDS.child("PoiURLImage").getValue(String.class));


                        DatabaseReference mPoiPrefrencesTaple = mDS.getRef().child("PoiPreferences");
                        mPoiPrefrencesTaple.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot PoiPreferencesdataSnapshot) {
                                if (PoiPreferencesdataSnapshot.exists())
                                {
                                    String sPoiPreference = " ";
                                    for (DataSnapshot mDS2:PoiPreferencesdataSnapshot.getChildren())
                                    {
                                        sPoiPreference += mDS2.getValue(String.class) + " - ";
                                    }
                                    mPoiPreferencesList.add(sPoiPreference);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                        DatabaseReference mPoiTypeTaple = mDS.getRef().child("PoiType");
                        mPoiTypeTaple.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot PoiTypedataSnapshot) {
                                if (PoiTypedataSnapshot.exists())
                                {
                                    String sPoitype = " ";
                                    for (DataSnapshot mDS3:PoiTypedataSnapshot.getChildren())
                                    {
                                        sPoitype += mDS3.getValue() + " - ";
                                    }
                                    mPoiTypeList.add(sPoitype);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        mAdapter.notifyDataSetChanged();
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void ekko(){
        int x = new Random().nextInt(4);
        MediaPlayer ekkoSound;
        switch (x){
            case 0:
                ekkoSound = MediaPlayer.create(PlacesActivity.this,R.raw.e0);
                break;
            case 1:
                ekkoSound = MediaPlayer.create(PlacesActivity.this,R.raw.e1);
                break;
            case 2:
                ekkoSound = MediaPlayer.create(PlacesActivity.this,R.raw.e2);
                break;
            default:
                ekkoSound = MediaPlayer.create(PlacesActivity.this,R.raw.e3);
        }
        ekkoSound.start();

        setContentView(R.layout.ekko_chat);
        LinearLayout layout = (LinearLayout) findViewById(R.id.chat_layout);
        Ekko ekko = new Ekko(PlacesActivity.this,layout);
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
                startActivity(new Intent(getApplicationContext(), PlacesActivity.class));
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }

    public void bottom_nav_profile(){
        BottomNavigationView btn_nav = findViewById(R.id.bottom_nav);
        btn_nav.setSelectedItemId(R.id.navigation_places);

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
                        return true;

                }
                return false;
            }
        });
    }
}
