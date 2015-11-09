package com.itraveller.materialadapter;

/**
 * Created by iTraveller on 10/28/2015.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.itraveller.R;
import com.itraveller.model.LandingModel;
import com.itraveller.volley.AppController;

import java.util.List;


public class RecentAdapater extends RecyclerView.Adapter<RecentAdapater.ViewHolder> {

    List<String> mItems;
    Activity activity;
    SharedPreferences.Editor editor;

    public RecentAdapater(List<String> LandingItems, Activity activity) {
        super();
        this.mItems = LandingItems;
        this.activity = activity;
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


    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.tvNature.setText(mItems.get(i).toString());
        viewHolder.tvNature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvNature;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNature = (TextView) itemView.findViewById(R.id.edt_tool_search);
            //tvDesNature = (TextView)itemView.findViewById(R.id.tv_des_nature);
        }
    }

}
