package com.example.etp.profile_activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.example.etp.R;

public class CommunicateActivity extends AppCompatActivity {
    private ImageView facebookButton, twitterButton, instagramButton, pinterestButton, emailImage, phoneImage;
    private Animation charanim;
    private ImageView communicateBackToProfile;
    private LinearLayout mEmailButton, mPhoneButton;
    private String mPhoneNumber, mEmailAddress, mAppName, mPageNameInApp, mPageNameinWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communicate);
        facebookButton = findViewById(R.id.facebook_communication);
        twitterButton = findViewById(R.id.twitter_communication);
        instagramButton = findViewById(R.id.instagram_communication);
        pinterestButton = findViewById(R.id.pinterest_communication);
        emailImage = findViewById(R.id.email_communication);
        phoneImage = findViewById(R.id.phone_communication);
        mEmailButton = findViewById(R.id.email_layout);
        mPhoneButton = findViewById(R.id.phone_layout);
        communicateBackToProfile = findViewById(R.id.communicate_back_to_profile);
        mPhoneNumber = "tel:" + "01151130933";
        mEmailAddress = "mailto:" + "bolaaskander90@gmail.com";
        charanim = AnimationUtils.loadAnimation(this, R.anim.charanim);

        // on click open facebook app
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookButton.startAnimation(charanim);
                mAppName = "Com.facebook.katana";
                mPageNameInApp = "fb://profile/100014867222738";
                mPageNameinWeb = "https://www.facebook.com/GemianTalasoun";
                startActivity(openSocialApps(mAppName, mPageNameInApp, mPageNameinWeb));
            }
        });

        // on click open twitter app
        twitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twitterButton.startAnimation(charanim);
                mAppName = "com.twitter.android";
                mPageNameInApp = "";
                mPageNameinWeb = "https://twitter.com/GemianTalasoun";
                startActivity(openSocialApps(mAppName, mPageNameInApp, mPageNameinWeb));
            }
        });

        // on click open instagram app
        instagramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instagramButton.startAnimation(charanim);
                mAppName = "com.instagram.exclusivegram";
                mPageNameInApp = "";
                mPageNameinWeb = "https://www.instagram.com/smilesmile714/";
                startActivity(openSocialApps(mAppName, mPageNameInApp, mPageNameinWeb));
            }
        });

        // on click open pinterest app
        pinterestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinterestButton.startAnimation(charanim);
                mAppName = "com.pinterest.activity.create.PinItActivity";
                mPageNameInApp = "";
                mPageNameinWeb = "https://www.pinterest.com/bolaaskander90/";
                startActivity(openSocialApps(mAppName, mPageNameInApp, mPageNameinWeb));
            }
        });

        // on click open email app.
        mEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailImage.startAnimation(charanim);
                Intent mSendEmailIntent = new Intent(Intent.ACTION_SENDTO);
                mSendEmailIntent.setData(Uri.parse(mEmailAddress));
                startActivity(mSendEmailIntent);
            }
        });

        // on Click make a call.
        mPhoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneImage.startAnimation(charanim);
                if (ActivityCompat.checkSelfPermission(CommunicateActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(CommunicateActivity.this, "You didn't give us the permission to make a call.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent mMakeCallIntent = new Intent(Intent.ACTION_CALL);
                    mMakeCallIntent.setData(Uri.parse(mPhoneNumber));
                    startActivity(mMakeCallIntent);
                    return;
                }
            }
        });
        communicateBackToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    // Function to open social media apps if exists, and open web sites of this apps in browser if not.
    public Intent openSocialApps(String appName, String pageNameInApp, String pageNameInWeb) {
        try { // if app exist.
            getPackageManager().getPackageInfo(appName, 0);
            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pageNameInApp));
            return appIntent;
        } catch (PackageManager.NameNotFoundException e) { // if app not exist.
            e.printStackTrace();
            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pageNameInWeb));
            return appIntent;
        }

    }

}
