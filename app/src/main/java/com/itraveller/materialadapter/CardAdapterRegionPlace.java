package com.itraveller.materialadapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.ImageLoader;
import com.itraveller.R;
import com.itraveller.activity.PlanTrip;
import com.itraveller.model.RegionPlaceModel;
import com.itraveller.volley.AppController;

import java.util.List;

/**
 * Created by I TRAVELLES on 30-10-2015.
 */
public class CardAdapterRegionPlace extends RecyclerView.Adapter<CardAdapterRegionPlace.ViewHolder>{

    List<RegionPlaceModel> mItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    Activity activity;
    SharedPreferences.Editor editor;

    public CardAdapterRegionPlace(List<RegionPlaceModel> RegionPlaceItems,Activity activity)
    {
        super();
        this.mItems=RegionPlaceItems;
        this.activity=activity;
        SharedPreferences sharedpreferences = activity.getSharedPreferences("Itinerary", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.material_region_place_card_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {

        final RegionPlaceModel nature = mItems.get(i);
        holder.tvTitle.setText(nature.getTitle());
        Log.d("Duration test test",""+nature.getDuration_Day());
        Log.d("Duration test test1",""+holder.tvNoOfNights);
        holder.tvNoOfNights.setText("("+""+(nature.getDuration_Day()-1)+" Nights"+"/"+""+nature.getDuration_Day()+" Days"+")");
        holder.tvDestination.setText(nature.getDestination());
        final Bundle bundle = activity.getIntent().getExtras();

        //viewHolder.tvDesNature.setText(""+nature.getPage_Title());
        holder.imgThumbnail.setImageUrl(nature.getImage(),imageLoader);
        holder.imgThumbnail.setErrorImageResId(R.drawable.default_img);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Intent i = new Intent(activity, PlanTrip.class);
                i.putExtra("Image", nature.getImage());
                i.putExtra("Duration", nature.getDuration_Day());
                Log.d("No of nights region11", "" + nature.getDuration_Day());
                i.putExtra("Title", nature.getTitle());
                i.putExtra("Destinations", nature.getDestination());
                i.putExtra("DestinationsID", nature.getDestination_Key());
                i.putExtra("DestinationsCount", nature.getDestination_Count());
                Log.d("No of nights region", "" + nature.getDestination_Count());
                i.putExtra("ArrivalPort", nature.getArrival_Port_Id());
                i.putExtra("DeparturePort", nature.getDeparture_Port_Id());
                i.putExtra("ItineraryID", nature.getItinerary_Id());
                i.putExtra("RegionID", bundle.getInt("RegionID"));
                i.putExtra("RegionString",nature.getRegionString());
                activity.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{


        public NetworkImageView imgThumbnail;
        public TextView tvTitle;
        public TextView tvNoOfNights;
        public TextView tvDestination;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (NetworkImageView)itemView.findViewById(R.id.thumbnail);
            tvTitle = (TextView)itemView.findViewById(R.id.title);
            tvNoOfNights=(TextView) itemView.findViewById(R.id.no_of_nights_txt);
            tvDestination=(TextView) itemView.findViewById(R.id.destination);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            //tvDesNature = (TextView)itemView.findViewById(R.id.tv_des_nature);
        }
    }

}
