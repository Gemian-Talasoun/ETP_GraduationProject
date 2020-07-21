package com.example.etp;

/* Imports */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.Random;
// *---------------------------------*  Home Page Recycler View   *---------------------------------*
public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.HomeRecyclerViewAdapterHolder>
{
    Context context;
    ArrayList<ArrayList<String>> mPOICostsListOfList, mPOIDescribtionsListOfList, mPOIDurationsListOfList, mPOIEndTimesListOfList, mPOIStartTimesListOfList, mPoisIdListOfList, mPoisNamesListOfList;
    ArrayList<String> mPlanNamesList, mfullCostList, mfullDurationList, mtripEndTimeList, mtripStartTimeList, mPlansIdsList, mTripInDashboardList, mUploadedByList;

    CurrentUserData mCurrentUserData;

    public HomeRecyclerViewAdapter(Context context, ArrayList<ArrayList<String>> mPOICostsListOfList, ArrayList<ArrayList<String>> mPOIDescribtionsListOfList, ArrayList<ArrayList<String>> mPOIDurationsListOfList, ArrayList<ArrayList<String>> mPOIEndTimesListOfList, ArrayList<ArrayList<String>> mPOIStartTimesListOfList, ArrayList<ArrayList<String>> mPoisIdListOfList, ArrayList<ArrayList<String>> mPoisNamesListOfList, ArrayList<String> mPlanNamesList, ArrayList<String> mfullCostList, ArrayList<String> mfullDurationList, ArrayList<String> mtripEndTimeList, ArrayList<String> mtripStartTimeList, ArrayList<String> mPlansIdsList, ArrayList<String> mTripInDashboardList,ArrayList<String> mUploadedByList) {
        this.context = context;
        this.mPOICostsListOfList = mPOICostsListOfList;
        this.mPOIDescribtionsListOfList = mPOIDescribtionsListOfList;
        this.mPOIDurationsListOfList = mPOIDurationsListOfList;
        this.mPOIEndTimesListOfList = mPOIEndTimesListOfList;
        this.mPOIStartTimesListOfList = mPOIStartTimesListOfList;
        this.mPoisIdListOfList = mPoisIdListOfList;
        this.mPoisNamesListOfList = mPoisNamesListOfList;
        this.mPlanNamesList = mPlanNamesList;
        this.mfullCostList = mfullCostList;
        this.mfullDurationList = mfullDurationList;
        this.mtripEndTimeList = mtripEndTimeList;
        this.mtripStartTimeList = mtripStartTimeList;
        this.mPlansIdsList = mPlansIdsList;
        this.mTripInDashboardList = mTripInDashboardList;
        this.mUploadedByList = mUploadedByList;

        mCurrentUserData = (CurrentUserData) context.getApplicationContext();
    }

    @NonNull
    @Override
    public HomeRecyclerViewAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_listitem,parent,false);
        HomeRecyclerViewAdapter.HomeRecyclerViewAdapterHolder planViewHolder = new HomeRecyclerViewAdapter.HomeRecyclerViewAdapterHolder(view);
        return planViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeRecyclerViewAdapterHolder holder, final int position) {
        // take 2 numbers after the point
        int index = mfullCostList.get(position).indexOf(".");
        if(index==-1||(mfullCostList.get(position).length()-index)<=3)
            holder.planCost.setText(mfullCostList.get(position));
        else
            holder.planCost.setText(mfullCostList.get(position).substring(0,index+3));

        holder.planEndTime.setText(mtripEndTimeList.get(position));
        holder.planID.setText(mPlansIdsList.get(position));
        holder.planName.setText(mPlanNamesList.get(position));
        holder.planPlace.setText("Luxor");
        holder.planStartTime.setText(mtripStartTimeList.get(position));

        //Set random luxor image
        int x = new Random().nextInt(8);
        switch (x){
            case 0:
                holder.planPlaceImage.setImageResource(R.drawable.l0);
                break;
            case 1:
                holder.planPlaceImage.setImageResource(R.drawable.l1);
                break;
            case 2:
                holder.planPlaceImage.setImageResource(R.drawable.l2);
                break;
            case 3:
                holder.planPlaceImage.setImageResource(R.drawable.l3);
                break;
            case 4:
                holder.planPlaceImage.setImageResource(R.drawable.l4);
                break;
            case 5:
                holder.planPlaceImage.setImageResource(R.drawable.l5);
                break;
            case 6:
                holder.planPlaceImage.setImageResource(R.drawable.l6);
                break;
            default:
                holder.planPlaceImage.setImageResource(R.drawable.l7);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            // Show bottom dialog and plan POIs
            @Override
            public void onClick(View v) {
                PlanBottomDialog dialog = new PlanBottomDialog(context,mPoisNamesListOfList.get(position),mPOICostsListOfList.get(position),mPOIDurationsListOfList.get(position),mPoisIdListOfList.get(position),mPOIStartTimesListOfList.get(position),mPOIEndTimesListOfList.get(position),mPOIDescribtionsListOfList.get(position));
                dialog.show(((FragmentActivity)context).getSupportFragmentManager(),"test");
            }
        });

        //Share plan
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if plan belong to current user
                if (mUploadedByList.get(position).equals(mCurrentUserData.getmCurrentUserId())) {
                    DatabaseReference mTripRef = FirebaseDatabase.getInstance().getReference("Trips").child(mPlansIdsList.get(position));
                    if (mTripInDashboardList.get(position).equals("False")) {
                        mTripRef.child("TripOnDashBord").setValue("True");

                        mPOICostsListOfList.remove(position);
                        mPOIDescribtionsListOfList.remove(position);
                        mPOIDurationsListOfList.remove(position);
                        mPOIEndTimesListOfList.remove(position);
                        mPOIStartTimesListOfList.remove(position);
                        mPoisIdListOfList.remove(position);
                        mPoisNamesListOfList.remove(position);
                        mPlanNamesList.remove(position);
                        mfullCostList.remove(position);
                        mfullDurationList.remove(position);
                        mtripEndTimeList.remove(position);
                        mtripStartTimeList.remove(position);
                        mPlansIdsList.remove(position);
                        mTripInDashboardList.remove(position);

                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mPlanNamesList.size());
                        Toast.makeText(context, "The plan added to dashboard.", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        mTripRef.child("TripOnDashBord").setValue("False");
                        mPOICostsListOfList.remove(position);
                        mPOIDescribtionsListOfList.remove(position);
                        mPOIDurationsListOfList.remove(position);
                        mPOIEndTimesListOfList.remove(position);
                        mPOIStartTimesListOfList.remove(position);
                        mPoisIdListOfList.remove(position);
                        mPoisNamesListOfList.remove(position);
                        mPlanNamesList.remove(position);
                        mfullCostList.remove(position);
                        mfullDurationList.remove(position);
                        mtripEndTimeList.remove(position);
                        mtripStartTimeList.remove(position);
                        mPlansIdsList.remove(position);
                        mTripInDashboardList.remove(position);

                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mPlanNamesList.size());
                        Toast.makeText(context, "The plan removed from dashboard.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(context, "You cannot use this feature because you have not created this plan", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return mPlanNamesList.size();
    }


    public class  HomeRecyclerViewAdapterHolder extends RecyclerView.ViewHolder{

        RelativeLayout planLayout;
        ImageView planPlaceImage;
        TextView planName,planCost,planPlace,planID,planStartTime,planEndTime;
        ImageButton share;
        public HomeRecyclerViewAdapterHolder(@NonNull View itemView) {
            super(itemView);
            planLayout = itemView.findViewById(R.id.plan_layout);
            planName = itemView.findViewById(R.id.plan_name);
            planCost = itemView.findViewById(R.id.plan_cost);
            planPlace = itemView.findViewById(R.id.plan_place);
            planID = itemView.findViewById(R.id.plan_id);
            planStartTime = itemView.findViewById(R.id.plan_starttime);
            planEndTime = itemView.findViewById(R.id.plan_endtime);
            planPlaceImage = itemView.findViewById(R.id.plan_place_image);
            share = itemView.findViewById(R.id.add_in_dash);
        }
    }
}