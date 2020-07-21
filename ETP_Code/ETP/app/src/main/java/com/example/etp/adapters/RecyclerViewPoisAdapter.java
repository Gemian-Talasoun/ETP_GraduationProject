package com.example.etp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.etp.R;
import java.util.ArrayList;

public class RecyclerViewPoisAdapter extends RecyclerView.Adapter<RecyclerViewPoisAdapter.ViewPoisHolder> {
    Context mContext;
    ArrayList<String> mViewPoiNameList, mViewPoiTimeList, mViewPoiMoneyList;

    public RecyclerViewPoisAdapter(Context mContext, ArrayList<String> mViewPoiNameList, ArrayList<String> mViewPoiTimeList, ArrayList<String> mViewPoiMoneyList) {
        this.mContext = mContext;
        this.mViewPoiNameList = mViewPoiNameList;
        this.mViewPoiTimeList = mViewPoiTimeList;
        this.mViewPoiMoneyList = mViewPoiMoneyList;
    }

    @NonNull
    @Override
    public ViewPoisHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_pois, parent, false);
        ViewPoisHolder mViewPoiHolder = new ViewPoisHolder(view);
        return mViewPoiHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPoisHolder holder, int position) {
        holder.mViewPoiName.setText(mViewPoiNameList.get(position));
        holder.mViewPoiTime.setText(mViewPoiTimeList.get(position));
        holder.mViewPoiMoney.setText(mViewPoiMoneyList.get(position));
    }

    @Override
    public int getItemCount() {
        return mViewPoiNameList.size();
    }

    public class ViewPoisHolder extends RecyclerView.ViewHolder{
        TextView mViewPoiName, mViewPoiTime, mViewPoiMoney;

        public ViewPoisHolder(View itemView){
            super(itemView);
            mViewPoiName = itemView.findViewById(R.id.view_poi_name);
            mViewPoiTime = itemView.findViewById(R.id.view_poi_time);
            mViewPoiMoney = itemView.findViewById(R.id.view_poi_money);
        }
    }
}
