package com.example.etp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class logIn extends AppCompatActivity {
    private static final String TAG = "LogInActivity";
    public static final int GOOGLE_SIGN_IN_CODE = 10005;
    public static final int FACEBOOK_SIGN_IN_CODE = 10010;
    private Button button;
    TextView create_acc_btn;
    private EditText email;
    private EditText pass;
    private EditText reset;
    TextView resetPass;
    //    ProgressBar mProgressBarLogin;
    ProgressDialog mProgressDialog;
    CurrentUserData mCurrentUserData;

    String AccountType;

    ////////  Firebase Database ////////
    private FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    AccessTokenTracker accessTokenTracker;
    DatabaseReference mUserTable;

    ////////  Authentication With Google ////////
    SignInButton mGoogleButton;
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;

    ////////  Authentication With Facebook ////////
    LoginButton mFbLoginButton;
    CallbackManager mFbCallbackManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_log_in);
        mAuth = FirebaseAuth.getInstance();

        resetPass = (TextView)findViewById(R.id.reset_pass_btn);
        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_dialog_reset_pass();
            }
        });
//        mProgressBarLogin = findViewById(R.id.login_progress_bar);


//  log in
        button = (Button) findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                email=(EditText)findViewById(R.id.emai_txt); //get email
                pass=(EditText)findViewById(R.id.pass_txt); //get password
                final String emails=email.getText().toString().trim(); //email
                String passs=pass.getText().toString().trim();// password
                if(TextUtils.isEmpty(emails))// validation of email
                {
                    email.setError("please enter email");
                    email.setFocusable(true);
                    return;
                }
                if(TextUtils.isEmpty(passs)) //validation of pass
                {
                    email.setError("please enter password");
                    email.setFocusable(true);
                    return;
                }
                if(passs.length()<6) //validation of pass
                {
                    pass.setError("password to short");
                    pass.setFocusable(true);
                    return;
                }
                mAuth.signInWithEmailAndPassword(emails,passs) //validtion pf email to check if is exit or it log in before this time
                        .addOnCompleteListener(logIn.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    setUserDataInGlobal(mAuth.getUid()); //put user data in public class to use it in anthor function
                                }
                                else
                                {
                                    email.setError("invalid email");
                                    email.setFocusable(true);
                                    pass.setError("invalid password");
                                    pass.setFocusable(true);
                                    return;

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
            }
        });
        create_acc_btn = (TextView) findViewById(R.id.create_account);
        create_acc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        mFbCallbackManager = CallbackManager.Factory.create();
        mFbLoginButton = findViewById(R.id.facebook_sign_in_button);
        mFbLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(logIn.this, Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(mFbCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("TAG", "facebook:onSuccess:" + loginResult);
//                        mProgressBarLogin.setVisibility(View.VISIBLE);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }
                    @Override
                    public void onCancel() {
                        Log.d("TAG", "facebook:onCancel");
                        Toast.makeText(logIn.this, "You Canceled Login ...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("TAG", "facebook:onError", error);
                        Toast.makeText(logIn.this, "Some Errors Happened, Login Again ...", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        mGoogleButton = findViewById(R.id.google_sign_in_button);
        googleSignIn();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFbCallbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                fbUpdateUI(null);
                // [END_EXCLUDE]
            }
        }
    }

    public void signUp() {
        Intent intent = new Intent(this, signUp.class);
        startActivity(intent);
    }

    public void OpenHomePage() {
        Intent intent = new Intent(logIn.this, home.class);
        startActivity(intent);
        finish();
    }

    // function return user data and put in global class
    private void setUserDataInGlobal(final String currentUserId)  {
        mProgressDialog = new ProgressDialog(logIn.this);
        mProgressDialog.show();
        mProgressDialog.setContentView(R.layout.progress_dialog);
        mProgressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mCurrentUserData = (CurrentUserData) getApplicationContext();
        mUserTable = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId);
        mUserTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mCurrentUserData.setmCurrentUserId(currentUserId);
//                    Toast.makeText(getApplicationContext(),currentUserId,Toast.LENGTH_LONG).show();
                    mCurrentUserData.setmFirebaseUser(mAuth.getCurrentUser());
                    if (dataSnapshot.hasChild("userEmail")) {
                        mCurrentUserData.setmUserEmail(dataSnapshot.child("userEmail").getValue(String.class));
//                        Toast.makeText(getApplicationContext(),dataSnapshot.child("userEmail").getValue(String.class),Toast.LENGTH_LONG).show();
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
                    if (dataSnapshot.hasChild("AccountType")) {
                        mCurrentUserData.setAccountType(dataSnapshot.child("AccountType").getValue(String.class));
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
        if (mAuth.getCurrentUser() != null) {
            OpenHomePage();
        }
    }

    //facebook login
    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            AccountType = "Facebook";
                            fbUpdateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(logIn.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            LoginManager.getInstance().logOut();
                            FirebaseAuth.getInstance().signOut();
                            fbUpdateUI(null);
                        }
                    }
                });
    }

    private void fbUpdateUI(final FirebaseUser mUser) {
        if (mUser != null) {
            final DatabaseReference mFirebaseUsers = FirebaseDatabase.getInstance().getReference("Users");
            mFirebaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.hasChild(mUser.getUid())) {
                        mFirebaseUsers.child(mUser.getUid()).child("userName").setValue(mUser.getDisplayName());
                        mFirebaseUsers.child(mUser.getUid()).child("userEmail").setValue(mUser.getEmail());
                        mFirebaseUsers.child(mUser.getUid()).child("userImage").setValue(mUser.getPhotoUrl().toString());
                        mFirebaseUsers.child(mUser.getUid()).child("userPhone").setValue(mUser.getPhoneNumber());
                        mFirebaseUsers.child(mUser.getUid()).child("userPassword").setValue("F");
                        mFirebaseUsers.child(mUser.getUid()).child("userRatingApp").setValue("3");
                        mFirebaseUsers.child(mUser.getUid()).child("userRegisterDate").setValue(System.currentTimeMillis());
                        mFirebaseUsers.child(mUser.getUid()).child("userAddress").setValue("");
                        mFirebaseUsers.child(mUser.getUid()).child("userID").setValue(1);
                        mFirebaseUsers.child(mUser.getUid()).child("activeTrip").setValue(0);
                        mFirebaseUsers.child(mUser.getUid()).child("AccountType").setValue(AccountType);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            setUserDataInGlobal(mUser.getUid());
        }
        else
        {
            Toast.makeText(this, "Login again.", Toast.LENGTH_SHORT).show();
        }
    }

    public void show_dialog_reset_pass(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        final  View rst = getLayoutInflater().inflate(R.layout.reset_password,null);
        reset=(EditText)rst.findViewById(R.id.resetemail);

        builder.setView(rst);


        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                final String email=reset.getText().toString();
                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(logIn.this,"invalid email please enter invalid email to reset",Toast.LENGTH_LONG).show();
                    return;

                }

                mAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(logIn.this,"invalid email",Toast.LENGTH_LONG).show();
                    }
                });
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

    public void googleSignIn(){
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("14574451871-moi1ne7v55k2ergehuqptrj7st4q6qn3.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



        mGoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, GOOGLE_SIGN_IN_CODE);
            }
        });

    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            AccountType = "Google";
                            fbUpdateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Login With Another Email.", Toast.LENGTH_SHORT).show();
                            GoogleSignIn.getClient(getApplicationContext(), new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()).signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    FirebaseAuth.getInstance().signOut();
                            }});
                            fbUpdateUI(null);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, e.getMessage());
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (accessToken != null && !accessToken.isExpired()) {
            handleFacebookAccessToken(accessToken);
        }
        else if (googleSignInAccount != null && !googleSignInAccount.isExpired()){
            firebaseAuthWithGoogle(googleSignInAccount);
        }
        else if (FirebaseAuth.getInstance().getCurrentUser() != null){
            setUserDataInGlobal(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
    }

}