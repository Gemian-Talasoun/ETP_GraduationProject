package com.example.etp.profile_activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.etp.CurrentUserData;
import com.example.etp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RateActivity extends AppCompatActivity {
    private TextView resultRate;
    private Button buttonRate;
    private ImageView imageRate;
    private RatingBar ratingBarRate;
    private String answerValue;
    private Animation charanim;
    private ImageView rateBackToProfile;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mUsersTable;
    CurrentUserData mCurrentUserData;
    EditText mUserMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        resultRate = findViewById(R.id.rate_result);
        buttonRate = findViewById(R.id.rate_submit);
        imageRate = findViewById(R.id.rate_image);
        ratingBarRate = findViewById(R.id.rate_stars);
        mUserMessage = findViewById(R.id.rate_message);
        rateBackToProfile = findViewById(R.id.rate_back_to_profile);
        mCurrentUserData = (CurrentUserData) getApplicationContext();
        mDatabase = FirebaseDatabase.getInstance();
        mUsersTable = mDatabase.getReference("Users");
        charanim = AnimationUtils.loadAnimation(this, R.anim.charanim);
        resultRate.startAnimation(charanim);
        answerValue = mCurrentUserData.getmUserRatingApp();
        ratingBarRate.setRating(Integer.parseInt(answerValue));
        buttonRate.setVisibility(View.VISIBLE);

        // switch case to change image and text build on user rating.
        switch (answerValue) {
            case "1": {
                imageRate.setImageResource(R.drawable.very_sad_emotion);
                resultRate.setText("Very Sad");
                break;
            }
            case "2": {
                imageRate.setImageResource(R.drawable.sad_emotion);
                resultRate.setText("Sad");
                break;
            }
            case "3": {
                imageRate.setImageResource(R.drawable.ok_emotion);
                resultRate.setText("okay");
                break;
            }
            case "4": {
                imageRate.setImageResource(R.drawable.happy_emotion);
                resultRate.setText("Good");
                break;
            }
            case "5": {
                imageRate.setImageResource(R.drawable.very_happy_emotion);
                resultRate.setText("Amazing");
                break;
            }
        }

        // get data on rating bar change.
        ratingBarRate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                answerValue = String.valueOf((int) (ratingBarRate.getRating()));
                if(answerValue.equals("1")){
                    imageRate.setImageResource(R.drawable.very_sad_emotion);
                    imageRate.startAnimation(charanim);
                    resultRate.setText("Very Sad");
                    resultRate.startAnimation(charanim);
//                    buttonRate.startAnimation(charanim);
//                    buttonRate.setVisibility(View.VISIBLE);
                }
                else if(answerValue.equals("2")){
                    imageRate.setImageResource(R.drawable.sad_emotion);
                    imageRate.startAnimation(charanim);
                    resultRate.setText("Sad");
                    resultRate.startAnimation(charanim);
//                    buttonRate.startAnimation(charanim);
//                    buttonRate.setVisibility(View.VISIBLE);
                }
                else if(answerValue.equals("3")){
                    imageRate.setImageResource(R.drawable.ok_emotion);
                    imageRate.startAnimation(charanim);
                    resultRate.setText("okay");
                    resultRate.startAnimation(charanim);
//                    buttonRate.startAnimation(charanim);
//                    buttonRate.setVisibility(View.VISIBLE);
                }
                else if(answerValue.equals("4")){
                    imageRate.setImageResource(R.drawable.happy_emotion);
                    imageRate.startAnimation(charanim);
                    resultRate.setText("Good");
                    resultRate.startAnimation(charanim);
//                    buttonRate.startAnimation(charanim);
//                    buttonRate.setVisibility(View.VISIBLE);
                }
                else if(answerValue.equals("5")){
                    imageRate.setImageResource(R.drawable.very_happy_emotion);
                    imageRate.startAnimation(charanim);
                    resultRate.setText("Amazing");
                    resultRate.startAnimation(charanim);
//                    buttonRate.startAnimation(charanim);
//                    buttonRate.setVisibility(View.VISIBLE);
                }
                else{
                    Toast.makeText(RateActivity.this, "Doesn't Rating Yet.", Toast.LENGTH_LONG).show();
                    resultRate.setText("");
                    buttonRate.setVisibility(View.INVISIBLE);
                }
            }
        });

        // submit user rating.
        buttonRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUsersTable.child(mCurrentUserData.getmCurrentUserId()).child("userRatingApp")
                        .setValue(answerValue);
                if (!TextUtils.isEmpty(mUserMessage.getText().toString())) {
                    mUsersTable.child(mCurrentUserData.getmCurrentUserId()).child("userRatingMessage").push()
                            .setValue(mUserMessage.getText().toString());
                }
                Toast.makeText(RateActivity.this, "Submitted", Toast.LENGTH_LONG).show();
            }
        });

        // on click go back to profile activity.
        rateBackToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
