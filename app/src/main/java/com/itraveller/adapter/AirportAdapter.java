package com.itraveller.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import com.itraveller.R;
import com.itraveller.model.AirportModel;
import com.itraveller.volley.AppController;

public class AirportAdapter extends BaseAdapter implements Filterable{
    private Activity mActivity;
    private LayoutInflater mLayoutInflater;
    public static List<AirportModel> sAirportItems;
    public  List<AirportModel> FilterAirportItems;
    public List<AirportModel> airportList;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public AirportAdapter(Activity mActivity, List<AirportModel> sAirportItems) {
        this.mActivity = mActivity;
        this.sAirportItems = sAirportItems;
        FilterAirportItems = sAirportItems;
    }

    //getting count of total numnbet of AirportItems
    @Override
    public int getCount() {
        return sAirportItems.size();
    }

    //getting item from given location
    @Override
    public Object getItem(int location) {
        return sAirportItems.get(location);
    }

    //getting itemID
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
 
        if (mLayoutInflater == null)
            mLayoutInflater = (LayoutInflater) mActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = mLayoutInflater.inflate(R.layout.airport_row, null);
 
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        TextView title = (TextView) convertView.findViewById(R.id.title);

        // getting data for the row
        AirportModel airportModel = sAirportItems.get(position);

        // title
        title.setText(airportModel.getValue());
 
        return convertView;
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                Log.i("ChangeText", " " + constraint);
                FilterResults results = new FilterResults();

                if (constraint != null && constraint.length() > 0) {
                    airportList = new ArrayList<AirportModel>();

                    for (int i = 0; i < FilterAirportItems.size(); i++) {
                        if ((FilterAirportItems.get(i).getValue().toUpperCase())
                                .contains(constraint.toString().toUpperCase())) {

                            AirportModel am = new AirportModel();
                            am.setValue(FilterAirportItems.get(i).getValue());
                            am.setKey(FilterAirportItems.get(i).getKey());
                            airportList.add(am);
                        }
                    }
                    results.count = airportList.size();
                    results.values = airportList;
                }
                else
                {
                    results.count = FilterAirportItems.size();
                    results.values = FilterAirportItems;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults results) {
                sAirportItems = (ArrayList<AirportModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}