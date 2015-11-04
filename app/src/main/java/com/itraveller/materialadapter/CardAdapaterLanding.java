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
import com.android.volley.toolbox.NetworkImageView;
import com.itraveller.R;
import com.itraveller.activity.MaterialRegionPlace;
import com.itraveller.activity.RegionPlace;
import com.itraveller.constant.Constants;
import com.itraveller.model.LandingModel;
import com.itraveller.volley.AppController;

import java.util.List;


public class CardAdapaterLanding extends RecyclerView.Adapter<CardAdapaterLanding.ViewHolder> {

    List<LandingModel> mItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    Activity activity;
    SharedPreferences.Editor editor;

    public CardAdapaterLanding(List<LandingModel> LandingItems, Activity activity) {
        super();
        this.mItems = LandingItems;
        this.activity = activity;
        SharedPreferences sharedpreferences = activity.getSharedPreferences("Itinerary", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.material_landing_card_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final LandingModel nature = mItems.get(i);
        viewHolder.tvNature.setText(nature.getRegion_Name());
        //viewHolder.tvDesNature.setText(""+nature.getPage_Title());
        viewHolder.imgThumbnail.setImageUrl(Constants.API_LandingAdapter_ImageURL+nature.getRegion_Id()+".jpg", imageLoader);
        viewHolder.imgThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent i = new Intent(activity, MaterialRegionPlace.class);
                i.putExtra("RegionID", nature.getRegion_Id());
                i.putExtra("RegionName", nature.getRegion_Name());
                editor.putString("FlightBit", "" + nature.getHome_Page());
                editor.commit();
                activity.startActivity(i);
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public NetworkImageView imgThumbnail;
        public TextView tvNature;
        //public TextView tvDesNature;

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (NetworkImageView)itemView.findViewById(R.id.img_thumbnail);
            imgThumbnail.setDefaultImageResId(R.drawable.itraveller_logo);
            imgThumbnail.setErrorImageResId(R.drawable.ic_launcher);
            tvNature = (TextView)itemView.findViewById(R.id.tv_nature);
            //tvDesNature = (TextView)itemView.findViewById(R.id.tv_des_nature);
        }
    }

}
