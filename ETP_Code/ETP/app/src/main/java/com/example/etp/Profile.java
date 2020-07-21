package com.example.etp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.etp.profile_activities.AboutUsActivity;
import com.example.etp.profile_activities.CommunicateActivity;
import com.example.etp.profile_activities.RateActivity;
import com.example.etp.profile_activities.SettingActivity;
import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.internal.FacebookSignatureValidator;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Profile extends AppCompatActivity {
    private LinearLayout settingsButton, rateButton, shareButton, aboutButton, communicateButton, logOutButton, deleteAccountButton;
    private TextView mCurrentUserName;
    private ImageView mCurrentUserImage;
    CurrentUserData mCurrentUserData;
    FirebaseUser mCurrentUser;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bottom_nav_profile();
        initVariables();
        // to show user image and name
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

        // on click open setting activity.
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(Profile.this, SettingActivity.class);
                startActivity(main);
            }
        });

        // on click open rate app activity.
        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(Profile.this, RateActivity.class);
                startActivity(main);
            }
        });

        // on click open communication activity.
        communicateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(Profile.this, CommunicateActivity.class);
                startActivity(main);
            }
        });

        // on click open about us activity
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(Profile.this, AboutUsActivity.class);
                startActivity(main);
            }
        });

        // on click open Intent to share app by share apps
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ApplicationInfo api = getApplicationContext().getApplicationInfo();
                    String apkPath = api.sourceDir;
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("application/vnd.android.package-archive");
                    share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(apkPath)));
                    startActivity(Intent.createChooser(share, "Share By ...."));
                }
                catch (Exception ex) {
                    Toast.makeText(Profile.this, "Error: " + ex.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // on click logout from the account and open login activity
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setContentView(R.id.logout_button);
                logout.performClick();
            }
        });


        // on click delete the account and open sign up activity
        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(Profile.this);
//                                    mDialogBuilder.setTitle("Execute Plan");
                mDialogBuilder.setMessage("Your ETP account will be deleted ...");
                mDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        try {
                            mCurrentUser.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("GemyDeleteAccount", "User account deleted.");
                                            }
                                        }
                                    });
                            mCurrentUserData.setmFirebaseUser(null);
                            mCurrentUserData.setmPerfrancesList(null);
                            mCurrentUserData.setmFavoritePlacesList(null);
                            mCurrentUserData.setmForbiddenPlacesList(null);
                            mCurrentUserData.setmTripsIdList(null);
                            mCurrentUserData.setmUserRatingApp(null);
                            mCurrentUserData.setmUserPhone(null);
                            mCurrentUserData.setmUserAddress(null);
                            mCurrentUserData.setmUserPassword(null);
                            mCurrentUserData.setmUserEmail(null);
                            mCurrentUserData.setmUserName(null);
                            mCurrentUserData.setmCurrentUserId(null);
                            mCurrentUserData.setmUserImage(null);
                            mCurrentUserData.setmUserID(null);
                            mCurrentUserData.setmRegisterDate(null);
                            LogOutFunction(deleteAccountButton.getRootView());
                            Intent main = new Intent(Profile.this, logIn.class);
                            startActivity(main);
                            finish();
                        }
                        catch (Exception ex){
                            Log.d("GemyDeleteAccountExeption", "delete account Exeption: " + ex);
                        }

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
        });

    }

    private void initVariables(){
        mCurrentUserData = (CurrentUserData) getApplicationContext();
        mCurrentUserImage = findViewById(R.id.profile_current_user_image);
        mCurrentUserName = findViewById(R.id.profile_current_user_name);
        settingsButton = findViewById(R.id.setting_button);
        rateButton = findViewById(R.id.rate_button);
        shareButton = findViewById(R.id.share_button);
        aboutButton = findViewById(R.id.about_button);
        communicateButton = findViewById(R.id.communicate_button);
        logOutButton = findViewById(R.id.log_out_button);
        deleteAccountButton = findViewById(R.id.delete_account_button);
        mCurrentUser = mCurrentUserData.getmFirebaseUser();
        logout = findViewById(R.id.logout_button);
    }

    public void bottom_nav_profile(){
        BottomNavigationView btn_nav = findViewById(R.id.bottom_nav);
        btn_nav.setSelectedItemId(R.id.navigation_profile);
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

    public void LogOutFunction(View view) {
        switch (mCurrentUserData.getAccountType()) {
            case "Google":{
                GoogleSignIn.getClient(this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .build()).signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        FirebaseAuth.getInstance().signOut();
                        Intent main = new Intent(Profile.this, logIn.class);
                        startActivity(main);
                        finish();
                    }
                });
            }
            case "Facebook":{
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
                if (isLoggedIn) {
                    LoginManager.getInstance().logOut();
                    FirebaseAuth.getInstance().signOut();
                    Intent main = new Intent(Profile.this, logIn.class);
                    startActivity(main);
                    finish();
                }
            }
            case "Email":{
                FirebaseAuth.getInstance().signOut();
                Intent main = new Intent(Profile.this, logIn.class);
                startActivity(main);
                finish();
            }
        }

    }
}
