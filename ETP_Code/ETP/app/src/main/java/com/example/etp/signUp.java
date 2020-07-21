package com.example.etp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class signUp extends AppCompatActivity {
    private Button btn_pref;
    boolean flag=false;
    private TextView txt_pref;
    String[] ArrItems;
    ArrayList<Integer> listItms = new ArrayList<>();
    ArrayList<String> perfrances = new ArrayList<>();
    boolean[] checkedItem ;
    private EditText user_name;
    private EditText user_email;
    private EditText user_pass;
    private EditText user_pass2;
    private Button goo;
    private int RC_Sgin=1;
    FirebaseAuth mAuth;
    GoogleSignInClient mgooglesgin;
    CallbackManager mCallbackManager;

    ProgressBar mSignUpProgressBar;


    private Button button;
    private Button tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);
        // Configure Google Sign In//

        /////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////
        btn_pref = (Button) findViewById(R.id.preference_btn);
        txt_pref =(TextView) findViewById(R.id.preference_txt);


        ArrItems = new String[]{"Photography","Meditation","Archaeological","Cultural","Desert","Religious","Biography","Antiques","Walking",
                "Music", "Barbecue","swimming","Running", "Cycling","Shopping","Reading", "Draw", "Event"};
        checkedItem = new boolean[ArrItems.length];

        btn_pref.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                selectItems();
            }
        });

        tv = (Button) findViewById(R.id.btn_signUp);
        mAuth=FirebaseAuth.getInstance();

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Sgin_Up();
                if(flag)
                Login();
            }
        });

    }
    public void Login() {
        Intent intent = new Intent(this, logIn.class);
        startActivity(intent);
    }

    public void selectItems(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chose Your Preference ");
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
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String item = "";
                for (int i =0;i<listItms.size();i++){
                    item = item + ArrItems[listItms.get(i)];
                    perfrances.add(ArrItems[listItms.get(i)]);
                    if (i != listItms.size()-1){
                        item = item +", ";
                    }
                }
                txt_pref.setText(item);
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
    // function sgin up using mail
    public void Sgin_Up()
    {
        user_name=(EditText)findViewById(R.id.fullname);// input user name
        user_email=(EditText)findViewById(R.id.email);// input user email
        user_pass=(EditText)findViewById(R.id.pass);// input password
        user_pass2=(EditText)findViewById(R.id.pass2);// confrim password
        final String name=user_name.getText().toString();
        final String email=user_email.getText().toString();
        final String pass=user_pass.getText().toString();
        final String pass2=user_pass2.getText().toString();


        if(perfrances.isEmpty())
        {
            btn_pref.setError("please enter your perfrances");
            return;
        }
        if(TextUtils.isEmpty(name))
        {
            user_name.setError("please enter name");
            user_name.setFocusable(true);
            return;
        }
        if(TextUtils.isEmpty(email))
        {
            user_name.setError("please enter email");
            user_name.setFocusable(true);
            return;
        }
        if(TextUtils.isEmpty(pass))
        {
            user_name.setError("please enter password");
            user_name.setFocusable(true);
            return;
        }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                user_email.setError("Invalid email");
                user_email.setFocusable(true);
                return;

            }
            if(pass.length()<6)
            {
                user_pass.setError("password to short ");
                user_pass.setFocusable(true);
                return;

            }
        if(!pass.equals(pass2))
        {
            user_pass2.setError("password is worng ");
            user_pass2.setFocusable(true);
            return;

        }
        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final int[] size = new int[1];
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference().child("Users").child(mAuth.getUid());
                            DatabaseReference refcount = database.getReference().child("Users");
                            refcount.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // get total available quest
                                    size[0] = (int) dataSnapshot.getChildrenCount();
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            User user=new User(size[0]+1,email,name,pass,perfrances);
                            myRef.setValue(user);

                            myRef.child("userImage").setValue("gs://test-b8daf.appspot.com/UserImage/Default_Person.jpg");
                            myRef.child("userPhone").setValue("");
                            myRef.child("userRatingApp").setValue("3");
                            myRef.child("userRegisterDate").setValue(System.currentTimeMillis());
                            myRef.child("userAddress").setValue("");
                            myRef.child("activeTrip").setValue(0);
                            myRef.child("AccountType").setValue("Email");

                            flag=true;

                        } else {

                            user_email.setError("Invalid email");
                            user_email.setFocusable(true);
                            user_pass.setError("Invalid password");
                            user_pass.setFocusable(true);
                            return;
                        }
                    }
                });








    }


   /* //google sgin up
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_Sgin) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            UpdateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();

            UpdateUI(null);
        }
    }
    private void UpdateUI(GoogleSignInAccount account) {
        GoogleSignInAccount s=GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(s !=null)
        {

            String child=s.getEmail().substring(0,s.getEmail().indexOf("@"));

//            Log.d("Gemian google User Id", account.getId());

            final int[] size = new int[1];
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference().child("Users").child(child);
            DatabaseReference refcount = database.getReference().child("Users");
            refcount.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // get total available quest
                    size[0] = (int) dataSnapshot.getChildrenCount();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            User user=new User();
            user.setUserEmail(s.getEmail());
            user.setPreferences(perfrances);
            user.setUserName(s.getGivenName()+s.getFamilyName());
            user.setUserPassword(null);
            user.setUserID(size[0]+1);
            myRef.setValue(user);

            myRef.child("userImage").setValue("gs://test-b8daf.appspot.com/UserImage/Default_Person.jpg");
            myRef.child("userPhone").setValue("");
            myRef.child("userRatingApp").setValue("3");
            myRef.child("userRegisterDate").setValue(System.currentTimeMillis());
            myRef.child("userAddress").setValue("");
            myRef.child("activeTrip").setValue(0);

        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////
*/
}
