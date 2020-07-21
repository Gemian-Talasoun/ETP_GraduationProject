package com.example.etp;
/* Imports */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

// *---------------------------------*  Plan Recycler view adapter in show plan page and bottom dialog   *---------------------------------*
class RecyclerPlanAdapter extends RecyclerView.Adapter<RecyclerPlanAdapter.POIViewHolder>{
    private static final String TAG = "Plan Adapter";
    private Context context;
    private boolean isshowplan;
    private ArrayList<String> POINames = new ArrayList<String>();
    private ArrayList<String> POIStartTimes = new ArrayList<String>();
    private ArrayList<String> POIEndTimes = new ArrayList<String>();
    private ArrayList<String> POIDescribtions = new ArrayList<String>();
    private ArrayList<String> POICosts = new ArrayList<String>();
    private ArrayList<String> POIDurations = new ArrayList<String>();
    private ArrayList<String> POIIDs = new ArrayList<String>();
    private ArrayList<String> POIImageURLs = new ArrayList<String>();

    public RecyclerPlanAdapter(Context context, ArrayList<String> POINames, ArrayList<String> POIStartTimes, ArrayList<String> POIEndTimes, ArrayList<String> POIDescribtions, ArrayList<String> POICosts, ArrayList<String> POIDurations, ArrayList<String> POIIDs,boolean isshowplan,ArrayList<String> POIImageURLs) {
        this.context = context;
        this.POINames = POINames;
        this.POIStartTimes = POIStartTimes;
        this.POIEndTimes = POIEndTimes;
        this.POIDescribtions = POIDescribtions;
        this.POICosts = POICosts;
        this.POIDurations = POIDurations;
        this.POIIDs = POIIDs;
        this.isshowplan = isshowplan;
        this.POIImageURLs = POIImageURLs;
    }

    @NonNull
    @Override
    public POIViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.poi_listitem,parent,false);
        POIViewHolder poiViewHolder = new POIViewHolder(view);
        return poiViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final POIViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        holder.POICost.setText(POICosts.get(position));
        holder.POIDesc.setText(POIDescribtions.get(position));
        holder.POIDuration.setText(POIDurations.get(position));
        holder.POIEndTime.setText(POIEndTimes.get(position));
        holder.POIStartTime.setText(POIStartTimes.get(position));
        holder.POIName.setText(POINames.get(position));
        holder.POIID.setText(POIIDs.get(position));
        //Load image from internet
        LoadImage loadImage = new LoadImage(holder.POIImage);
        loadImage.execute(POIImageURLs.get(position));

        if(position==POICosts.size()-1)
            holder.botLine.setVisibility(View.GONE);

        // Set Remove POI from plan listener function in show plan page
        if(isshowplan) {
            holder.POILayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    POINames.remove(position);
                    POIStartTimes.remove(position);
                    POIEndTimes.remove(position);
                    POIDescribtions.remove(position);
                    POICosts.remove(position);
                    POIDurations.remove(position);
                    POIIDs.remove(position);
                    POIImageURLs.remove(position);
                    notifyDataSetChanged();
                    return true;
                }

            });
        }
    }

    @Override
    public int getItemCount() {
        return POINames.size();
    }

    public  class POIViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout POILayout;
        RadioButton POIStartTime,POIEndTime;
        ImageView POIImage;
        TextView POIName,POIDesc,POICost,POIDuration,POIID;
        View botLine;
        RelativeLayout item;
        public POIViewHolder(@NonNull View itemView) {
            super(itemView);
            POILayout = itemView.findViewById(R.id.poi_layout);
            POIStartTime = itemView.findViewById(R.id.poi_starttime);
            POIEndTime = itemView.findViewById(R.id.poi_endtime);
            POIImage = itemView.findViewById(R.id.poi_image);
            POIName = itemView.findViewById(R.id.poi_name);
            POIDesc = itemView.findViewById(R.id.poi_desc);
            POICost = itemView.findViewById(R.id.poi_cost);
            POIDuration = itemView.findViewById(R.id.poi_duration);
            POIID = itemView.findViewById(R.id.poi_id);
            botLine = itemView.findViewById(R.id.botline);
            item = itemView.findViewById(R.id.poi_item);
        }
    }


    private class LoadImage extends AsyncTask<String,Void, Bitmap> {
        ImageView imageView;
        public LoadImage(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String urlLink = strings[0];
            Bitmap bitmap  = null;
            try {
                InputStream inputStream = new java.net.URL(urlLink).openStream();
                bitmap  = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
}
