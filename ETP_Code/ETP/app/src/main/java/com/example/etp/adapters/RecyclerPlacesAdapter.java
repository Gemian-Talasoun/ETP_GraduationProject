package com.example.etp.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.etp.PlaceInformationActivity;
import com.example.etp.R;
import java.util.ArrayList;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

public class RecyclerPlacesAdapter extends RecyclerView.Adapter<RecyclerPlacesAdapter.ViewPlaceHolder>{
    Context mContext;
    ArrayList<String> mPoiCloseTimeList, mPoiOpenTimeList, mPoiPoiDescriptionList, mPoiPoiIDList, mPoiPoiLatitudeList, mPoiPoiLongitudeList, mPoiPoiNameList, mPoiPoiPhoneList, mPoiPreferencesList, mPoiPoiPriceList, mPoiTypeList, mPoiVisitTimeList, mPoiWorkingTimeList, mPoiAddressList, mPoiLocationList, mPoiURLImageList, mPoiRateList;
    FirebaseStorage mStorage = FirebaseStorage.getInstance();

    public RecyclerPlacesAdapter(Context mContext, ArrayList<String> mPoiCloseTimeList, ArrayList<String> mPoiOpenTimeList, ArrayList<String> mPoiPoiDescriptionList, ArrayList<String> mPoiPoiIDList, ArrayList<String> mPoiPoiLatitudeList, ArrayList<String> mPoiPoiLongitudeList, ArrayList<String> mPoiPoiNameList, ArrayList<String> mPoiPoiPhoneList, ArrayList<String> mPoiPreferencesList, ArrayList<String> mPoiPoiPriceList, ArrayList<String> mPoiTypeList, ArrayList<String> mPoiVisitTimeList, ArrayList<String> mPoiWorkingTimeList, ArrayList<String> mPoiAddressList, ArrayList<String> mPoiLocationList, ArrayList<String> mPoiURLImageList, ArrayList<String> mPoiRateList) {
        this.mContext = mContext;
        this.mPoiCloseTimeList = mPoiCloseTimeList;
        this.mPoiOpenTimeList = mPoiOpenTimeList;
        this.mPoiPoiDescriptionList = mPoiPoiDescriptionList;
        this.mPoiPoiIDList = mPoiPoiIDList;
        this.mPoiPoiLatitudeList = mPoiPoiLatitudeList;
        this.mPoiPoiLongitudeList = mPoiPoiLongitudeList;
        this.mPoiPoiNameList = mPoiPoiNameList;
        this.mPoiPoiPhoneList = mPoiPoiPhoneList;
        this.mPoiPreferencesList = mPoiPreferencesList;
        this.mPoiPoiPriceList = mPoiPoiPriceList;
        this.mPoiTypeList = mPoiTypeList;
        this.mPoiVisitTimeList = mPoiVisitTimeList;
        this.mPoiWorkingTimeList = mPoiWorkingTimeList;
        this.mPoiAddressList = mPoiAddressList;
        this.mPoiLocationList = mPoiLocationList;
        this.mPoiURLImageList = mPoiURLImageList;
        this.mPoiRateList = mPoiRateList;
    }

    @NonNull
    @Override
    public ViewPlaceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item_recycler_view, parent, false);
        ViewPlaceHolder mViewPlaceHolder = new ViewPlaceHolder(view);
        return mViewPlaceHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewPlaceHolder holder, final int position) {
        holder.mPlaceName.setText(mPoiPoiNameList.get(position));
        holder.mPlaceDescription.setText(mPoiPoiDescriptionList.get(position));
        StorageReference mImageReference = mStorage.getReferenceFromUrl(mPoiURLImageList.get(position));
        mImageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Picasso.with(mContext).load(uri)
                        .into(holder.mPlaceImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PlaceInformationActivity.class);
                intent.putExtra("mPoiCloseTime", mPoiCloseTimeList.get(position));
                intent.putExtra("mPoiOpenTime", mPoiOpenTimeList.get(position));
                intent.putExtra("mPoiPoiDescription", mPoiPoiDescriptionList.get(position));
                intent.putExtra("mPoiPoiID", mPoiPoiIDList.get(position));
                intent.putExtra("mPoiPoiLatitude", mPoiPoiLatitudeList.get(position));
                intent.putExtra("mPoiPoiLongitude", mPoiPoiLongitudeList.get(position));
                intent.putExtra("mPoiPoiName", mPoiPoiNameList.get(position));
                intent.putExtra("mPoiPoiPhone", mPoiPoiPhoneList.get(position));
                intent.putExtra("mPoiPreferences", mPoiPreferencesList.get(position));
                intent.putExtra("mPoiPoiPrice", mPoiPoiPriceList.get(position));
                intent.putExtra("mPoiType", mPoiTypeList.get(position));
                intent.putExtra("mPoiVisitTime", mPoiVisitTimeList.get(position));
                intent.putExtra("mPoiWorkingTime", mPoiWorkingTimeList.get(position));
                intent.putExtra("mPoiAddress", mPoiAddressList.get(position));
                intent.putExtra("mPoiLocation", mPoiLocationList.get(position));
                intent.putExtra("mPoiURLImage", mPoiURLImageList.get(position));
                intent.putExtra("mPoiRate", mPoiRateList.get(position));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPoiPoiNameList.size();
    }

    public class ViewPlaceHolder extends RecyclerView.ViewHolder{
        ImageView mPlaceImage;
        TextView mPlaceName, mPlaceDescription;
        LinearLayout mItemLayout;

        public ViewPlaceHolder(View itemView){
            super(itemView);
            mPlaceImage = itemView.findViewById(R.id.places_item_image);
            mPlaceName = itemView.findViewById(R.id.places_item_name);
            mPlaceDescription = itemView.findViewById(R.id.places_item_description);
            mItemLayout = itemView.findViewById(R.id.places_item_layout);
        }
    }
}
