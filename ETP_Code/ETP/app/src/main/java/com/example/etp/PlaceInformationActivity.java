package com.example.etp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import java.util.HashMap;

public class PlaceInformationActivity extends AppCompatActivity {
    private String mPoiCloseTime, mPoiOpenTime, mPoiPoiDescription, mPoiPoiID, mPoiPoiName, mPoiPoiPhone, mPoiPreferences, mPoiPoiPrice, mPoiAddress, mPoiLocation, mPoiURLImage, mPoiRate;
    private ImageView mPlaceItemBackToPlacesActivity, mPlaceDetailsMenuButton, mFavoriteItemSelectedImage, mForbiddenItemSelectedImage, mPlaceDetailsImage;
    private LinearLayout mMenuLayout, mItemMakeFavoritePlaceButton, mItemMakeForbiddenplaceButton;
    private TextView mPlaceDetailsName, mPlaceDetailsLocation, mPlaceDetailsRate, mPlaceDetailsDescription, mPlaceDetailsPreferences, mPlaceDetailsAddress, mPlaceDetailsPrice, mPlaceDetailsTime, mPlaceDetailsPhone;
    private DatabaseReference mUserTaple;
    private String mItemSelected;
    CurrentUserData mCurrentUserDataObject;
    HashMap<String, String> mUserFavoriteList, mUserForbiddenList;
    Boolean menuHidden;
    LinearLayout place_details_phone_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_information);
        initVariables();
        getplaceDataFromIntent();
        getItemInfo();
        menuHidden = true;
        mPlaceDetailsMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuHidden) {
                    mMenuLayout.setVisibility(View.VISIBLE);
                    menuHidden = false;
                    mItemMakeFavoritePlaceButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (mItemSelected){
                                case "Favorite": {
                                    mUserFavoriteList.remove(mPoiPoiName);
                                    mUserTaple.child("favoritePlaces").child(mPoiPoiName).removeValue();
                                    mCurrentUserDataObject.setmFavoritePlacesList(mUserFavoriteList);
                                    mItemMakeFavoritePlaceButton.setAlpha((float) 0.8);
                                    mFavoriteItemSelectedImage.setImageResource(R.drawable.ic_no_icon_24dp);
                                    mItemMakeForbiddenplaceButton.setAlpha((float) 0.8);
                                    mForbiddenItemSelectedImage.setImageResource(R.drawable.ic_no_icon_24dp);
                                    mItemSelected = "Null";
                                    break;
                                }
                                case "Forbidden": {
                                    mUserForbiddenList.remove(mPoiPoiName);
                                    mUserTaple.child("forbiddenPlaces").child(mPoiPoiName).removeValue();
                                    mUserFavoriteList.put(mPoiPoiName, mPoiPoiID);
                                    mUserTaple.child("favoritePlaces").child(mPoiPoiName).setValue(mPoiPoiID);
                                    mCurrentUserDataObject.setmFavoritePlacesList(mUserFavoriteList);
                                    mCurrentUserDataObject.setmForbiddenPlacesList(mUserForbiddenList);
                                    mItemMakeForbiddenplaceButton.setAlpha((float) 0.8);
                                    mForbiddenItemSelectedImage.setImageResource(R.drawable.ic_no_icon_24dp);
                                    mItemMakeFavoritePlaceButton.setAlpha((float) 1);
                                    mFavoriteItemSelectedImage.setImageResource(R.drawable.ic_done_all_black_24dp);
                                    mItemSelected = "Favorite";
                                    break;
                                }
                                case "Null": {
                                    mUserFavoriteList.put(mPoiPoiName, mPoiPoiID);
                                    mUserTaple.child("favoritePlaces").child(mPoiPoiName).setValue(mPoiPoiID);
                                    mCurrentUserDataObject.setmFavoritePlacesList(mUserFavoriteList);
                                    mItemMakeFavoritePlaceButton.setAlpha((float) 1);
                                    mFavoriteItemSelectedImage.setImageResource(R.drawable.ic_done_all_black_24dp);
                                    mItemSelected = "Favorite";
                                    break;
                                }
                                default: {

                                }
                            }
                        }
                    });
                    mItemMakeForbiddenplaceButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (mItemSelected){
                                case "Forbidden": {
                                    mUserForbiddenList.remove(mPoiPoiName);
                                    mUserTaple.child("forbiddenPlaces").child(mPoiPoiName).removeValue();
                                    mCurrentUserDataObject.setmForbiddenPlacesList(mUserForbiddenList);
                                    mItemMakeForbiddenplaceButton.setAlpha((float) 0.8);
                                    mForbiddenItemSelectedImage.setImageResource(R.drawable.ic_no_icon_24dp);
                                    mItemSelected = "Null";
                                    break;
                                }
                                case "Favorite": {
                                    mUserForbiddenList.put(mPoiPoiName, mPoiPoiID);
                                    mUserTaple.child("forbiddenPlaces").child(mPoiPoiName).setValue(mPoiPoiID);
                                    mUserFavoriteList.remove(mPoiPoiName);
                                    mUserTaple.child("favoritePlaces").child(mPoiPoiName).removeValue();
                                    mCurrentUserDataObject.setmFavoritePlacesList(mUserFavoriteList);
                                    mCurrentUserDataObject.setmForbiddenPlacesList(mUserForbiddenList);
                                    mItemMakeForbiddenplaceButton.setAlpha((float) 1);
                                    mForbiddenItemSelectedImage.setImageResource(R.drawable.ic_done_all_black_24dp);
                                    mItemMakeFavoritePlaceButton.setAlpha((float) 0.8);
                                    mFavoriteItemSelectedImage.setImageResource(R.drawable.ic_no_icon_24dp);
                                    mItemSelected = "Forbidden";
                                    break;
                                }
                                case "Null": {
                                    mUserForbiddenList.put(mPoiPoiName, mPoiPoiID);
                                    mUserTaple.child("forbiddenPlaces").child(mPoiPoiName).setValue(mPoiPoiID);
                                    mCurrentUserDataObject.setmForbiddenPlacesList(mUserForbiddenList);
                                    mItemMakeForbiddenplaceButton.setAlpha((float) 1);
                                    mForbiddenItemSelectedImage.setImageResource(R.drawable.ic_done_all_black_24dp);
                                    mItemSelected = "Forbidden";
                                    break;
                                }
                                default: {

                                }
                            }

                        }
                    });
                }
                else {
                    mMenuLayout.setVisibility(View.INVISIBLE);
                    menuHidden = true;
                }

            }
        });

        fillActivityByData();

        mPlaceItemBackToPlacesActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initVariables(){
        mPlaceItemBackToPlacesActivity = findViewById(R.id.place_item_back_to_Places); //
        mPlaceDetailsMenuButton = findViewById(R.id.place_details_menu);  //
        mFavoriteItemSelectedImage = findViewById(R.id.favorite_item_selected);
        mForbiddenItemSelectedImage = findViewById(R.id.forbidden_item_selected);
        mPlaceDetailsImage = findViewById(R.id.place_details_image); //
        mMenuLayout = findViewById(R.id.menu_layout);
        mItemMakeFavoritePlaceButton = findViewById(R.id.place_details_make_favorite_place);
        mItemMakeForbiddenplaceButton = findViewById(R.id.place_details_make_forbidden_place);
        mPlaceDetailsName = findViewById(R.id.place_details_name);
        mPlaceDetailsLocation = findViewById(R.id.place_details_location);
        mPlaceDetailsRate = findViewById(R.id.place_details_rate);
        mPlaceDetailsDescription = findViewById(R.id.place_details_description);
        mPlaceDetailsPreferences = findViewById(R.id.place_details_preferences);
        mPlaceDetailsAddress = findViewById(R.id.place_details_address);
        mPlaceDetailsPrice = findViewById(R.id.place_details_price);
        mPlaceDetailsTime = findViewById(R.id.place_details_time);
        mPlaceDetailsPhone = findViewById(R.id.place_details_phone);
        place_details_phone_layout = findViewById(R.id.place_details_phone_layout);

        mUserForbiddenList = new HashMap<>();
        mUserFavoriteList = new HashMap<>();
        mItemSelected = "Null";
        mCurrentUserDataObject = (CurrentUserData) getApplicationContext();
        if (mCurrentUserDataObject.getmFavoritePlacesList() != null) {
            mUserFavoriteList.putAll(mCurrentUserDataObject.getmFavoritePlacesList());
        }
        if (mCurrentUserDataObject.getmForbiddenPlacesList() != null) {
            mUserForbiddenList.putAll(mCurrentUserDataObject.getmForbiddenPlacesList());
        }
        mUserTaple = FirebaseDatabase.getInstance().getReference("Users").child(mCurrentUserDataObject.getmCurrentUserId());
    }

    private void getplaceDataFromIntent() {
        Intent intent = getIntent();
        mPoiCloseTime = intent.getStringExtra("mPoiCloseTime");
        mPoiOpenTime = intent.getStringExtra("mPoiOpenTime");
        mPoiPoiDescription = intent.getStringExtra("mPoiPoiDescription");
        mPoiPoiID = intent.getStringExtra("mPoiPoiID");
        mPoiPoiName = intent.getStringExtra("mPoiPoiName");
        mPoiPoiPhone = intent.getStringExtra("mPoiPoiPhone");
        mPoiPreferences = intent.getStringExtra("mPoiPreferences");
        mPoiPoiPrice = intent.getStringExtra("mPoiPoiPrice");
        mPoiAddress = intent.getStringExtra("mPoiAddress");
        mPoiLocation = intent.getStringExtra("mPoiLocation");
        mPoiURLImage = intent.getStringExtra("mPoiURLImage");
        mPoiRate = intent.getStringExtra("mPoiRate");
    }

    private void fillActivityByData() {
        mPlaceDetailsName.setText(mPoiPoiName);
        mPlaceDetailsLocation.setText(mPoiLocation);
        mPlaceDetailsRate.setText(mPoiRate);
        mPlaceDetailsDescription.setText(mPoiPoiDescription);
        mPlaceDetailsPreferences.setText(mPoiPreferences);
        mPlaceDetailsAddress.setText(mPoiAddress);
        mPlaceDetailsPrice.setText(mPoiPoiPrice);
        mPlaceDetailsTime.setText(mPoiOpenTime + "  -  " + mPoiCloseTime);
        mPlaceDetailsPhone.setText(mPoiPoiPhone);
        FirebaseStorage mStorage = FirebaseStorage.getInstance();
        StorageReference mImageReference = mStorage.getReferenceFromUrl(mPoiURLImage);
        mImageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Picasso.with(getApplicationContext()).load(uri)
                        .into(mPlaceDetailsImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        if (mPoiPoiPhone.equals("0")) {
            place_details_phone_layout.setLayoutParams(new LinearLayout.LayoutParams(0,0));
            mPlaceDetailsPhone.setText(mPoiPoiPhone);
        }
    }

    private void getItemInfo() {
//        mPoiPoiName

        if (mUserFavoriteList.containsKey(mPoiPoiName)) {
            mItemMakeForbiddenplaceButton.setAlpha((float) 0.8);
            mForbiddenItemSelectedImage.setImageResource(R.drawable.ic_no_icon_24dp);
            mItemMakeFavoritePlaceButton.setAlpha((float) 1);
            mFavoriteItemSelectedImage.setImageResource(R.drawable.ic_done_all_black_24dp);
            mItemSelected = "Favorite";
        }
        else if (mUserForbiddenList.containsKey(mPoiPoiName)) {
            mItemMakeFavoritePlaceButton.setAlpha((float) 0.8);
            mFavoriteItemSelectedImage.setImageResource(R.drawable.ic_no_icon_24dp);
            mItemMakeForbiddenplaceButton.setAlpha((float) 1);
            mForbiddenItemSelectedImage.setImageResource(R.drawable.ic_done_all_black_24dp);
            mItemSelected = "Forbidden";
        }
        else {
            mItemMakeForbiddenplaceButton.setAlpha((float) 0.8);
            mForbiddenItemSelectedImage.setImageResource(R.drawable.ic_no_icon_24dp);
            mItemMakeFavoritePlaceButton.setAlpha((float) 0.8);
            mFavoriteItemSelectedImage.setImageResource(R.drawable.ic_no_icon_24dp);
            mItemSelected = "Null";
        }
    }

}