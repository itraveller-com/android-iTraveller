package com.itraveller.adapter;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import com.itraveller.R;
import com.itraveller.constant.Constants;
import com.itraveller.model.LandingModel;
import com.itraveller.model.MyTravelModel;
import com.itraveller.volley.AppController;

public class MyTravelAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<MyTravelModel> LandingItems;
    TextView date_text,id_text;

    
    public MyTravelAdapter(Activity activity, List<MyTravelModel> LandingItems) {
        this.activity = activity;
        this.LandingItems = LandingItems;
    }

    //getting count of total number of Landingitems
    @Override
    public int getCount() {
        return LandingItems.size();
    }

    //getting item from given location
    @Override
    public Object getItem(int location) {
        return LandingItems.get(location);
    }

    //getting itemID
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.my_trips_item, null);

        date_text = (TextView) convertView.findViewById(R.id.date_text);
        id_text= (TextView) convertView.findViewById(R.id.id_text);

        MyTravelModel m = LandingItems.get(position);

        id_text.setText(""+m.getItinerary_Main_Id());

        String dateF=m.getTravelDate();
        String dateA[]=dateF.split("-");

        dateF=""+dateA[2]+"-"+dateA[1]+"-"+dateA[0];
        date_text.setText(dateF);

        return convertView;
    }

}