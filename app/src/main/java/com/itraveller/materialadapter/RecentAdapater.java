package com.itraveller.materialadapter;

/**
 * Created by iTraveller on 10/28/2015.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.itraveller.R;
import com.itraveller.activity.MaterialRegionPlace;
import com.itraveller.model.LandingModel;
import com.itraveller.volley.AppController;

import java.util.ArrayList;
import java.util.List;


public class RecentAdapater extends RecyclerView.Adapter<RecentAdapater.ViewHolder> {

    List<String> mItems;
    Activity activity;
    SharedPreferences.Editor editor;
    List<LandingModel> landingModel;

    public RecentAdapater(List<String> LandingItems, List<LandingModel> landingModel, Activity activity) {
        super();
        this.mItems = LandingItems;
        this.activity = activity;
        this.landingModel = landingModel;
        SharedPreferences sharedpreferences = activity.getSharedPreferences("Itinerary", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
          View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.searchbar_popular, viewGroup, false);
          return new ViewHolder(v);
    }
    //    need to override this method
    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        Log.i("Value","" + mItems.get(i));
        final String[] value = mItems.get(i).toString().trim().split(":");
        final String[] Region_id = mItems.get(i).trim().split("/");
        final int length = mItems.get(i).trim().split("/").length;
        //Log.i("OnCLick", "Clicked" + Integer.parseInt(Region_id[length-1]));



        viewHolder.tvNature.setText(value[0]);
        viewHolder.tvImageIcon.setImageResource(R.drawable.ic_restore);
        viewHolder.tvNature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Intent in = new Intent(activity, MaterialRegionPlace.class);
                in.putExtra("RegionID", Integer.parseInt(Region_id[length-1]));
                in.putExtra("RegionName", value[0]);
                String flightBit="";
                int landingList_size=landingModel.size();
                for(int index = 0; index < landingList_size ; index ++)
                {
                    if(Integer.parseInt(Region_id[length-1]) == landingModel.get(index).getRegion_Id()){
                        flightBit = ""+landingModel.get(index).getHome_Page();
                    }
                }
                editor.putString("FlightBit", flightBit);
                editor.commit();
                activity.startActivity(in);
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvNature;
        public ImageView tvImageIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNature = (TextView) itemView.findViewById(R.id.edt_tool_search);
            tvImageIcon = (ImageView) itemView.findViewById(R.id.img_tool_back);
            //tvDesNature = (TextView)itemView.findViewById(R.id.tv_des_nature);
        }
    }

}
