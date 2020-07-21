package com.example.etp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

class RecyclerDashboardAdapter extends RecyclerView.Adapter<RecyclerDashboardAdapter.ViewDashboardHolder> {
    Context mContext;
    ArrayList<String> mTripsIdList, mTripsImageList, mTripsNameList, mPostsTimeList, mTripsRateList, mTripsPlaceList, mTripsBudgetList, mTripsNumOfExeList, mTripsNumOfCommentList;

    public RecyclerDashboardAdapter(Context mContext, ArrayList<String> mTripsIdList, ArrayList<String> mTripsImageList, ArrayList<String> mTripsNameList, ArrayList<String> mPostsTimeList, ArrayList<String> mTripsRateList, ArrayList<String> mTripsPlaceList, ArrayList<String> mTripsBudgetList, ArrayList<String> mTripsNumOfExeList, ArrayList<String> mTripsNumOfCommentList) {
        this.mContext = mContext;
        this.mTripsIdList = mTripsIdList;
        this.mTripsImageList = mTripsImageList;
        this.mTripsNameList = mTripsNameList;
        this.mPostsTimeList = mPostsTimeList;
        this.mTripsRateList = mTripsRateList;
        this.mTripsPlaceList = mTripsPlaceList;
        this.mTripsBudgetList = mTripsBudgetList;
        this.mTripsNumOfExeList = mTripsNumOfExeList;
        this.mTripsNumOfCommentList = mTripsNumOfCommentList;
    }

    @NonNull
    @Override
    public ViewDashboardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_dashboard_item, parent, false);
        ViewDashboardHolder mViewDashboardHolder = new ViewDashboardHolder(view);
        return mViewDashboardHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewDashboardHolder holder, final int position) {
        holder.mTripName.setText(mTripsNameList.get(position));
        holder.mPostTime.setText(mPostsTimeList.get(position));
        holder.mTripRate.setText(mTripsRateList.get(position));
        holder.mTripPlace.setText(mTripsPlaceList.get(position));
        holder.mTripBudget.setText(mTripsBudgetList.get(position));
        holder.mTripNumOfExe.setText(mTripsNumOfExeList.get(position));
        holder.mTripNumOfComment.setText(mTripsNumOfCommentList.get(position));
        holder.mTripImage.setImageResource(Integer.parseInt(mTripsImageList.get(position)));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ViewTripPostActivity.class);
                intent.putExtra("mViewTripId", mTripsIdList.get(position));
                intent.putExtra("mViewTripName", mTripsNameList.get(position));
                intent.putExtra("mViewTripTime", mPostsTimeList.get(position));
                intent.putExtra("mViewTripRate", mTripsRateList.get(position));
                intent.putExtra("mViewTripPlace", mTripsPlaceList.get(position));
                intent.putExtra("mViewTripBudget", mTripsBudgetList.get(position));
                intent.putExtra("mViewTripImage", mTripsImageList.get(position));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mTripsIdList.size();
    }

    public class ViewDashboardHolder extends RecyclerView.ViewHolder{
        ImageView mTripImage;
        TextView mTripName, mPostTime, mTripRate, mTripPlace, mTripBudget, mTripNumOfExe, mTripNumOfComment;

        public ViewDashboardHolder(View itemView){
            super(itemView);
            mTripImage = itemView.findViewById(R.id.trip_image2);
            mTripName = itemView.findViewById(R.id.trip_name2);
            mPostTime = itemView.findViewById(R.id.post_time2);
            mTripRate = itemView.findViewById(R.id.trip_rate2);
            mTripPlace = itemView.findViewById(R.id.trip_place2);
            mTripBudget = itemView.findViewById(R.id.budget2);
            mTripNumOfExe = itemView.findViewById(R.id.number_of_exe2);
            mTripNumOfComment = itemView.findViewById(R.id.number_of_comments2);
        }
    }
}