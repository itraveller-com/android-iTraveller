package com.itraveller.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import com.itraveller.R;
import com.itraveller.activity.PlanTrip;
import com.itraveller.activity.RegionPlace;
import com.itraveller.model.RegionPlaceModel;
import com.itraveller.volley.AppController;


/**
 * Created by VNK on 6/10/2015.
 */
public class RegionPlaceAdapter extends BaseAdapter implements Filterable{
        private Activity activity;
        private LayoutInflater inflater;
        private List<RegionPlaceModel> Places;
        private List<RegionPlaceModel> PlacesFilter;
        public static final String MY_PREFS = "ScreenHeight";
        private  int _screen_height;
        private ArrayList<RegionPlaceModel> arraylist;
    RegionPlace.CustomNoDataHandler custom_data_handler;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

        public RegionPlaceAdapter(Activity activity, List Places, RegionPlace.CustomNoDataHandler customNoDataHandler) {
            this.activity = activity;
            this.Places = Places;
            this.PlacesFilter = Places;
            this.custom_data_handler = customNoDataHandler;
        }

        @Override
        public int getCount() {
            return Places.size();
        }

        @Override
        public Object getItem(int location) {
            return Places.get(location);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (inflater == null)
                inflater = (LayoutInflater) activity
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null)
                convertView = inflater.inflate(R.layout.region_place_row, null);

            if (imageLoader == null)
                imageLoader = AppController.getInstance().getImageLoader();


            SharedPreferences prefs = activity.getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
            _screen_height = prefs.getInt("Screen_Height", 0)-(prefs.getInt("Status_Height", 0) + prefs.getInt("ActionBar_Height", 0));
            Log.i("iTraveller", "Screen Height: " + _screen_height);
            int width = prefs.getInt("Screen_Width", 0); //0 is the default value.


            NetworkImageView thumbNail = (NetworkImageView) convertView
                    .findViewById(R.id.thumbnail);
            FrameLayout frame_lay = (FrameLayout) convertView.findViewById(R.id.imgMain);
            TextView title = (TextView) convertView.findViewById(R.id.title);
            TextView destination = (TextView) convertView.findViewById(R.id.destination);
            TextView discountRs = (TextView) convertView.findViewById(R.id.discount);
            // 60 is the height of the filter button
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width,(_screen_height/2) - 60);
            frame_lay.setLayoutParams(lp);

            // getting data for the row
            RegionPlaceModel m = Places.get(position);

            // thumbnail image
            //thumbNail.setImageUrl("http://stage.itraveller.com/backend/images/destinations/" + m.getImage().toLowerCase() , imageLoader);
            thumbNail.setImageUrl(m.getImage(), imageLoader);
            Log.d("I",""+m.getImage());
            //Log.i("ImageURL", "http://stage.itraveller.com/backend/images/destinations/" + m.getRegion_Name() + ".jpg");
            // title
            title.setText(m.getTitle());
            destination.setText(m.getDestination());
            discountRs.setText("Rs: "+m.getDiscount());
            final Bundle bundle = activity.getIntent().getExtras();
            //Print
            //System.out.println("RegionID: " + bundle.getInt("RegionID"));
            // Listen for ListView Item Click
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    final Intent i = new Intent(activity, PlanTrip.class);
                    i.putExtra("Image", Places.get(position).getImage());
                    i.putExtra("Duration", Places.get(position).getDuration_Day());
                    i.putExtra("Title", Places.get(position).getTitle());
                    i.putExtra("Destinations", Places.get(position).getDestination());
                    i.putExtra("DestinationsID", Places.get(position).getDestination_Key());
                    i.putExtra("DestinationsCount", Places.get(position).getDestination_Count());
                    i.putExtra("ArrivalPort", Places.get(position).getArrival_Port_Id());
                    i.putExtra("DeparturePort", Places.get(position).getDeparture_Port_Id());
                    i.putExtra("ItineraryID", Places.get(position).getItinerary_Id());
                    i.putExtra("RegionID", bundle.getInt("RegionID"));
                    activity.startActivity(i);
                }
            });

            return convertView;
        }

    public Filter getFilter() {
        // TODO Auto-generated method stub
        return new Filter(){

            // ContactSetGet is your gettersetter class

            @Override
            protected FilterResults performFiltering(CharSequence prefix) {
                FilterResults results = new FilterResults();
                String value = prefix.toString();
                String[] parts = value.split(",");
                List<RegionPlaceModel> i = new ArrayList<RegionPlaceModel>();

                if (prefix!= null && prefix.toString().length() > 0) {

                    for (int index = 0; index < PlacesFilter.size(); index++) {
                        RegionPlaceModel si = PlacesFilter.get(index);
                        Log.i("Price","."+si.getPrice());
                        //String number
                        if((si.getPrice()>= Integer.parseInt(parts[0]))&&(si.getPrice()<=Integer.parseInt(parts[1]))){
                            Log.i("Days"," "+si.getDuration_Day()+" Days" + Integer.parseInt(parts[2]));
                            if(((si.getDuration_Day() - 1) == Integer.parseInt(parts[2]))&& ((si.getDuration_Day() - 1) != 0)) {
                                i.add(si);
                                Log.i("Days 123", " " + si.getDuration_Day() + " Days" + Integer.parseInt(parts[2]));
                            }
                            else if((Integer.parseInt(parts[2]) == 0)) {
                                Log.i("Days","Test");
                                i.add(si);
                            }
                        }
                    }
                    results.values = i;
                    results.count = i.size();
                }
                //  else{
                //synchronized (mylstcont){
                //  results.values = mycontnamesetget;
                //results.count = mycontnamesetget.size();
                //  }
                // }
                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                // TODO Auto-generated method stub
                Log.i("Price","."+results.values);
                Places = (ArrayList<RegionPlaceModel>) results.values;
                RegionPlaceAdapter.this.notifyDataSetChanged();
                custom_data_handler.DataHandler(results.count);
               // Places = PlacesFilter;
            }
        };
    }

 /*   // Filter Class
    public void filter(int minValue, int maxValue) {
        Log.i("6Test" , ""+arraylist.size());
      //  Places.clear();
        //if (min.length() == 0) {
       ///     worldpopulationlist.addAll(arraylist);
       // } else {
        for (RegionPlaceModel Rpm : arraylist)
        {
                if (Rpm.getPrice() >= minValue || Rpm.getPrice()<=maxValue) {
                    Log.i("6Test" , "qqq"+minValue);
                    Places.add(Rpm);
                }
            }
      //  }
        notifyDataSetChanged();
    }*/

}

