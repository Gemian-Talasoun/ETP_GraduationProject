package com.example.etp.profile_activities;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.etp.CurrentUserData;
import com.example.etp.PlacesActivity;
import com.example.etp.Profile;
import com.example.etp.R;
import com.example.etp.adapters.RecyclerPlacesAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.HashMap;

public class SettingActivity extends AppCompatActivity {
    private ImageView mBackToBrofile, mSettingUserImage;
    private ImageButton mSettingEditUserImage;
    private StorageReference mStorageReference;
    private DatabaseReference mUsersTable;
    private Button mSettingSubmit;
    private static final int IMAGE_REQUEST = 1;
    private Uri mUriImage;
    private LinearLayout mFavoriteButton, mForbiddenButton, mPreferencesButton;
    CurrentUserData mCurrentUserData;
    String mListType;
    ArrayList<String> mPoiCloseTimeList, mPoiOpenTimeList, mPoiPoiDescriptionList, mPoiPoiIDList, mPoiPoiLatitudeList, mPoiPoiLongitudeList, mPoiPoiNameList, mPoiPoiPhoneList, mPoiPreferencesList, mPoiPoiPriceList, mPoiTypeList, mPoiVisitTimeList, mPoiWorkingTimeList, mPoiAddressList, mPoiLocationList, mPoiURLImageList, mPoiRateList;
    FirebaseUser CurrentUser;
    private DatabaseReference mPoisTable;
    FirebaseStorage mStorage;
    RecyclerPlacesAdapter mAdapter;
    String mImageName;
    // view
    EditText mSettingFullName, mSettingEmail, mSettingPassword, mSettingAddress;
    ImageView mSettingEditFullUserName, mSettingEditPassword, mSettingEditAddress;
    // preference
    String[] ArrItems;
    boolean[] checkedItem;
    ArrayList<Integer> listItms;
    ArrayList<String> perfrances;
    ArrayList<String> mPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initVariables();

        // on click give permission to the user to change his name.
        mSettingEditFullUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSettingFullName.setEnabled(true);
            }
        });

        // on click give permission to the user to change his password.
        mSettingEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSettingPassword.setEnabled(true);
            }
        });

        // on click give permission to the user to change his address.
        mSettingEditAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSettingAddress.setEnabled(true);
            }
        });

        // on click give permission to the user to choose image from his images.
        mSettingEditUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        // on click open user favorite places list.
        mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListType = "Favorite";
                setDataInDialogList(mListType, mCurrentUserData.getmFavoritePlacesList());
            }
        });

        // on click open user forbidden places list.
        mForbiddenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListType = "Forbidden";
                setDataInDialogList(mListType, mCurrentUserData.getmForbiddenPlacesList());
            }
        });

        // on click open user preferences list.
        mPreferencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrItems = new String[]{"Photography","Meditation","Archaeological","Cultural","Desert","Religious","Biography","Antiques","Walking",
                        "Music", "Barbecue","swimming","Running", "Cycling","Shopping","Reading", "Draw", "Event"};
                checkedItem = new boolean[ArrItems.length];
                listItms = new ArrayList<>();
                perfrances = new ArrayList<>();

                mPreferences = mCurrentUserData.getmPerfrancesList();

                if (mPreferences != null)
                {
                    for (int u = 0; u < ArrItems.length; u++) {
                        if (mPreferences.contains(ArrItems[u])) {
                            listItms.add(u);
                            perfrances.add(ArrItems[u]);
                            checkedItem[u] = true;
                        }
                    }
                }
                else {
                    mPreferences = new ArrayList<>();
                }
                selectItems();
            }
        });
        mBackToBrofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, Profile.class));
                finish();
            }
        });
        mSettingSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitSettingData();
            }
        });
        if (mCurrentUserData.getmUserImage() != null) {
            if (mCurrentUserData.getmUserImage().contains("gs://test-b8daf.appspot.com/UserImage")) {
                FirebaseStorage mStorage = FirebaseStorage.getInstance();
                StorageReference mImageReference = mStorage.getReferenceFromUrl(mCurrentUserData.getmUserImage());
                mImageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(getApplicationContext()).load(uri).fit().into(mSettingUserImage);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            }
            else {
                Picasso.with(getApplicationContext()).load(mCurrentUserData.getmUserImage()).fit().into(mSettingUserImage);
            }
        }
        else {
            Picasso.with(getApplicationContext()).load(R.drawable.ic_person).fit().into(mSettingUserImage);
        }
        mSettingFullName.setText(mCurrentUserData.getmUserName());
        mSettingEmail.setText(mCurrentUserData.getmUserEmail());
        mSettingPassword.setText(mCurrentUserData.getmUserPassword());
        mSettingAddress.setText(mCurrentUserData.getmUserAddress());
    }

    // Function to initialize the variables.
    private void initVariables() {
        mBackToBrofile = findViewById(R.id.setting_back_to_profile);
        mSettingEditUserImage = findViewById(R.id.setting_edit_user_image);
        mSettingUserImage = findViewById(R.id.setting_user_image);
        mSettingSubmit = findViewById(R.id.setting_submit);
        mFavoriteButton = findViewById(R.id.setting_favorite_list_layout);
        mForbiddenButton = findViewById(R.id.setting_forbidden_list_layout);
        mPreferencesButton = findViewById(R.id.setting_preferences_list_layout);
        mSettingFullName = findViewById(R.id.setting_full_name);
        mSettingEmail = findViewById(R.id.setting_email);
        mSettingPassword = findViewById(R.id.setting_password);
        mSettingAddress = findViewById(R.id.setting_address);
        mSettingEditFullUserName = findViewById(R.id.setting_edit_full_user_name);
        mSettingEditPassword = findViewById(R.id.setting_edit_password);
        mSettingEditAddress = findViewById(R.id.setting_edit_address);
        mCurrentUserData = (CurrentUserData) getApplicationContext();
        mStorageReference = FirebaseStorage.getInstance().getReference("UserImage");
        mUsersTable = FirebaseDatabase.getInstance().getReference("Users").child(mCurrentUserData.getmCurrentUserId());
        mUriImage = null;
        CurrentUser = mCurrentUserData.getmFirebaseUser();
    }

    // Function to get the new user image extension.
    private String getFileExtension(Uri uri) {
        ContentResolver mCR = getContentResolver();
        MimeTypeMap mMime = MimeTypeMap.getSingleton();
        return mMime.getExtensionFromMimeType(mCR.getType(uri));
    }

    // Function to delete user previous image.
    private void deletePreviousImage(String imageUrl) {
//        Toast.makeText(getApplicationContext(), imageUrl, Toast.LENGTH_SHORT).show();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference mPreviousImageReference = storage.getReferenceFromUrl(imageUrl);
        mPreviousImageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });
    }

    // Function to submit new user data.
    private void submitSettingData() {
        if(mUriImage != null) {
            mImageName = System.currentTimeMillis() + "." + getFileExtension(mUriImage);
            StorageReference fileReference = mStorageReference.child(mImageName);
            fileReference.putFile(mUriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    if (!mCurrentUserData.getmUserImage().contains("Default_Person") && !mCurrentUserData.getmUserImage()
                            .contains("https://graph.facebook.com") && !mCurrentUserData.getmUserImage()
                            .contains("googleusercontent.com")) {
                        deletePreviousImage(mCurrentUserData.getmUserImage());
                    }
                    mCurrentUserData.setmUserImage("gs://test-b8daf.appspot.com/UserImage/" + mImageName);
                    mUsersTable.child("userImage").setValue("gs://test-b8daf.appspot.com/UserImage/" + mImageName);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SettingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                }
            });
        }
        if (!mSettingPassword.getText().toString().equals(mCurrentUserData.getmUserPassword())) {
            String newPassword = mSettingPassword.getText().toString();
            if (CurrentUser != null) {
                CurrentUser.updatePassword(newPassword)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    mCurrentUserData.setmUserPassword(mSettingPassword.getText().toString());
                                    mUsersTable.child("userPassword").setValue(mSettingPassword.getText().toString());
                                }
                            }
                        }); }
        }
        if (!mSettingFullName.getText().toString().equals(mCurrentUserData.getmUserName())) {
            mCurrentUserData.setmUserName(mSettingFullName.getText().toString());
            mUsersTable.child("userName").setValue(mSettingFullName.getText().toString());
        }
        if (!mSettingEmail.getText().toString().equals(mCurrentUserData.getmUserEmail())) {
            mCurrentUserData.setmUserEmail(mSettingEmail.getText().toString());
            mUsersTable.child("userEmail").setValue(mSettingEmail.getText().toString());
        }
        if (!mSettingAddress.getText().toString().equals(mCurrentUserData.getmUserAddress())) {
            mCurrentUserData.setmUserAddress(mSettingAddress.getText().toString());
            mUsersTable.child("userAddress").setValue(mSettingAddress.getText().toString());
        }
        Toast.makeText(SettingActivity.this, "Data Submitted", Toast.LENGTH_LONG).show();
    }

    // Function to choose image.
    private void chooseImage(){
        Intent mChooseImageIntent = new Intent();
        mChooseImageIntent.setType("image/*");
        mChooseImageIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(mChooseImageIntent, IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            mUriImage = data.getData();
            mSettingUserImage.setImageURI(mUriImage);
        }
    }

    // Function to set favorite or forbidden list in dialog.
    private void setDataInDialogList(String mType, HashMap<String, String> mListItem) {
        AlertDialog.Builder mFavoriteDialogBuilder = new AlertDialog.Builder(SettingActivity.this);
        View mFavoriteDialogView = getLayoutInflater().inflate(R.layout.dialog_list, null);

        TextView mDialogTitle = mFavoriteDialogView.findViewById(R.id.dialog_list_title);
        RecyclerView mFavoriteRecyclerView = mFavoriteDialogView.findViewById(R.id.dialog_list_recycler_view);
        LinearLayout  mAddNewPlaceButton = mFavoriteDialogView.findViewById(R.id.dialog_list_add_new_Place);
        switch (mType)
        {
            case "Favorite":
            {
                mDialogTitle.setText("Favorite Places");
                break;
            }
            case "Forbidden":
            {
                mDialogTitle.setText("Forbidden Places");
                break;
            }
        }

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

        mPoisTable = FirebaseDatabase.getInstance().getReference("Pois");
        mStorage = FirebaseStorage.getInstance();


        if (mListItem != null){
            fillListsByData(mListItem);
        }
        else {

        }
        mAdapter = new RecyclerPlacesAdapter(getApplicationContext(), mPoiCloseTimeList, mPoiOpenTimeList, mPoiPoiDescriptionList, mPoiPoiIDList, mPoiPoiLatitudeList, mPoiPoiLongitudeList, mPoiPoiNameList, mPoiPoiPhoneList, mPoiPreferencesList, mPoiPoiPriceList, mPoiTypeList, mPoiVisitTimeList, mPoiWorkingTimeList, mPoiAddressList, mPoiLocationList, mPoiURLImageList, mPoiRateList);
        mFavoriteRecyclerView.setAdapter(mAdapter);
        mFavoriteRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.notifyDataSetChanged();



        mFavoriteDialogBuilder.setView(mFavoriteDialogView);
        final AlertDialog mFavoriteDialog = mFavoriteDialogBuilder.create();
        mFavoriteDialog.show();

        mAddNewPlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PlacesActivity.class));
                mFavoriteDialog.dismiss();
            }
        });

    }

    // Function to get data of favorite and forbidden lists from firebase.
    private void fillListsByData(final HashMap<String, String> mPlacesList) {
        mPoisTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    for (final DataSnapshot mDS:dataSnapshot.getChildren()) {
                        if (mPlacesList.containsKey(mDS.child("PoiName").getValue(String.class))) {
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
                            mPoiLocationList.add("In " + mDS.child("PoiLocation").getValue(String.class));
                            mPoiURLImageList.add(mDS.child("PoiURLImage").getValue(String.class));


                            DatabaseReference mPoiPrefrencesTaple = mDS.getRef().child("PoiPreferences");
                            mPoiPrefrencesTaple.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot PoiPreferencesdataSnapshot) {
                                    if (PoiPreferencesdataSnapshot.exists()) {
                                        String sPoiPreference = " ";
                                        for (DataSnapshot mDS2 : PoiPreferencesdataSnapshot.getChildren()) {
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
                                    if (PoiTypedataSnapshot.exists()) {
                                        String sPoitype = " ";
                                        for (DataSnapshot mDS3 : PoiTypedataSnapshot.getChildren()) {
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
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Function to choose new preference or remove preference from list.
    public void selectItems(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Your Preference ");
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
                mUsersTable.child("preferences").removeValue();
                for (int i =0;i<listItms.size();i++){
                    item = item + ArrItems[listItms.get(i)];
                    perfrances.add(ArrItems[listItms.get(i)]);
                    mPreferences.add(ArrItems[listItms.get(i)]);
                    mUsersTable.child("preferences").child(i+"").setValue(ArrItems[listItms.get(i)]);
                    if (i != listItms.size()-1){
                        item = item +", ";
                    }
                }
                mCurrentUserData.setmPerfrancesList(mPreferences);
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

}
