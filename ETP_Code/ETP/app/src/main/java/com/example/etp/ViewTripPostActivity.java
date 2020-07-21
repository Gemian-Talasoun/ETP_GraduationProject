package com.example.etp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.etp.adapters.RecyclerViewPoisAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewTripPostActivity extends AppCompatActivity {
    ImageView mViewTripImage, btnBackToDashboard, mArrowState;
    TextView mViewTripName, mViewPostTime, mViewTripRate, mViewTripPlace, mViewTripBudget, mPostedUserName;
    private RecyclerView mRecyclerTripCommentsView, mRecyclerTripPoisView ;
    private EditText mComment;
    private LinearLayout mSubmitCommentButton, mTripInformation, mUpDownButton, mAddExistTripButton;;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mTripsTable, mCommentsTable, mUsersTable;
    private RecyclerCommentAdapter mAdapter;
    private String mCommentContent;
    private Boolean mPostUp;
    private Animation mUpDownAnimation;
    private String sTripId;
    CurrentUserData currentUserData;
    Intent getDataFromintent;
    RecyclerViewPoisAdapter mPoisAdapter;

    @SuppressLint({"ResourceType", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_plan);
        initVariables();
        fillTripBodyByData();
        mSubmitCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setComment();
                mAdapter.notifyDataSetChanged();
            }
        });
        btnBackToDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToDashboard();
            }
         });
        mUpDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPostUp) {
                    LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    mTripInformation.setLayoutParams(mLayoutParams);
                    mArrowState.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                    mPostUp = false;
                }
                else {
                    LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                    mTripInformation.setLayoutParams(mLayoutParams);
                    mArrowState.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                    mPostUp = true;
                }
            }
        });
        mAddExistTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExistTrip();
            }
        });
        ////////////////////     Comments Adapter      /////////////////////////////////////////////
        mTripsTable.child(sTripId).child("tripCommentsId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> mCommentsList = new ArrayList<>();
                for (DataSnapshot mDS : dataSnapshot.getChildren()) {
                    mCommentsList.add(mDS.getValue(String.class));
                }
                mAdapter = new RecyclerCommentAdapter(getApplicationContext(), mCommentsList);
                mRecyclerTripCommentsView.setAdapter(mAdapter);
                mRecyclerTripCommentsView.setLayoutManager(new LinearLayoutManager(ViewTripPostActivity.this));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////       Pois Adapter        /////////////////////////////////////////////
        mTripsTable.child(sTripId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> mViewPoiNameList;
                ArrayList<String> mViewPoiMoneyList;
                ArrayList<String> mViewPoiTimeList = new ArrayList<>();
                ArrayList<String> mViewPOIStartTimesList;
                ArrayList<String> mViewPOIEndTimesList;
                mViewPoiNameList = (ArrayList<String>) dataSnapshot.child("PoisNames").getValue();
                mViewPoiMoneyList = (ArrayList<String>) dataSnapshot.child("POICosts").getValue();
                mViewPOIStartTimesList = (ArrayList<String>) dataSnapshot.child("POIStartTimes").getValue();
                mViewPOIEndTimesList = (ArrayList<String>) dataSnapshot.child("POIEndTimes").getValue();
                for (int i = 0; i < mViewPOIStartTimesList.size(); i++) {
                    mViewPoiTimeList.add(mViewPOIStartTimesList.get(i) + " - " + mViewPOIEndTimesList.get(i));
                }
                mPoisAdapter = new RecyclerViewPoisAdapter(getApplicationContext(), mViewPoiNameList, mViewPoiTimeList, mViewPoiMoneyList);
                mRecyclerTripPoisView.setAdapter(mPoisAdapter);
                mRecyclerTripPoisView.setLayoutManager(new LinearLayoutManager(ViewTripPostActivity.this));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
    }

    private void initVariables() {
        mViewTripImage = findViewById(R.id.trip_image3);
        mViewTripName = findViewById(R.id.trip_name3);
        mViewPostTime = findViewById(R.id.post_time3);
        mViewTripRate = findViewById(R.id.trip_rate3);
        mViewTripPlace = findViewById(R.id.trip_place3);
        mViewTripBudget = findViewById(R.id.budget3);
        mPostedUserName = findViewById(R.id.posted_user_name);
        mRecyclerTripCommentsView = findViewById(R.id.recycler_comments_view);
        mRecyclerTripPoisView = findViewById(R.id.recycler_trip_pois_view);
        mComment = findViewById(R.id.comment);
        mSubmitCommentButton = findViewById(R.id.submit_comment);
        btnBackToDashboard = findViewById(R.id.back_to_dashboard);
        mTripInformation = findViewById(R.id.trip_information);
        mUpDownButton = findViewById(R.id.up_down_button);
        mArrowState = findViewById(R.id.up_down_arrow);
        mAddExistTripButton = findViewById(R.id.add_exist_trip);
        getDataFromintent = getIntent();
        sTripId = getDataFromintent.getStringExtra("mViewTripId");
        currentUserData = (CurrentUserData) getApplicationContext();
        mDatabase = FirebaseDatabase.getInstance();
        mTripsTable = mDatabase.getReference("Trips");
        mCommentsTable = mDatabase.getReference("Comments");
        mUsersTable = mDatabase.getReference("Users");
        mUpDownAnimation = AnimationUtils.loadAnimation(this, R.anim.charanim);
        mPostUp = false;
    }

    private void fillTripBodyByData() {
        mTripsTable.child(getDataFromintent.getStringExtra("mViewTripId")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsersTable.child(dataSnapshot.child("UploadedBy").getValue(String.class)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot DS) {
                        mPostedUserName.setText(DS.child("userName").getValue(String.class) + "'s Plan");
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError DE) {
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        mViewTripImage.setImageResource(Integer.parseInt(getDataFromintent.getStringExtra("mViewTripImage")));
        mViewTripName.setText(getDataFromintent.getStringExtra("mViewTripName"));
        mViewPostTime.setText(getDataFromintent.getStringExtra("mViewTripTime"));
        mViewTripRate.setText(getDataFromintent.getStringExtra("mViewTripRate"));
        mViewTripPlace.setText(getDataFromintent.getStringExtra("mViewTripPlace"));
        mViewTripBudget.setText(getDataFromintent.getStringExtra("mViewTripBudget"));
    }

    private void setComment() {
        mCommentContent = "null";
        mCommentContent = mComment.getText().toString();
        if (!mCommentContent.equals("")) {
            DatabaseReference mCurrentComment = mCommentsTable.push();
            mCurrentComment.child("CommentContent").setValue(mCommentContent);
            mCurrentComment.child("CommentDate").setValue(System.currentTimeMillis());
            mCurrentComment.child("CommentId").setValue(mCurrentComment.getKey());
            mCurrentComment.child("CommentUserId").setValue(currentUserData.getmCurrentUserId());
            mTripsTable.child(sTripId).child("tripCommentsId").push().setValue(mCurrentComment.getKey());
            mComment.setText("");
            mAdapter.notifyDataSetChanged();
        }
        else {
            Toast.makeText(ViewTripPostActivity.this, "Enter your comment first.", Toast.LENGTH_SHORT).show();
        }
    }

    private void addExistTrip() {
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(ViewTripPostActivity.this);
        mDialogBuilder.setTitle("Add Plan");
        mDialogBuilder.setMessage("This plan will be added to your plans.");
        mDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mUsersTable.child(currentUserData.getmCurrentUserId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("TripsId").exists()) {
                            HashMap<String, String> mUserTripsMap = (HashMap<String, String>) dataSnapshot.child("TripsId").getValue();
                            ArrayList<String> mUserTripsList = new ArrayList<String>(mUserTripsMap.values());
                            if (!mUserTripsList.contains(sTripId))
                            {
                                mUsersTable.child(currentUserData.getmCurrentUserId()).child("TripsId").push().setValue(sTripId);
                                mTripsTable.child(sTripId).child("NumOfExe").push().setValue(currentUserData.getmCurrentUserId());
                            }
                            else {

                            }
                        }
                        else {
                            mUsersTable.child(currentUserData.getmCurrentUserId()).child("TripsId").push().setValue(sTripId);
                            mTripsTable.child(sTripId).child("NumOfExe").push().setValue(currentUserData.getmCurrentUserId());
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

            }
        });
        mDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog mdialog = mDialogBuilder.create();
        mdialog.show();
    }

    public void goBackToDashboard() {
        finish();
    }
//////////////////////////////////////////////////////////////////
}