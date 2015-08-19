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
    private Activity activity;
    private LayoutInflater inflater;
    public static List<AirportModel> AirportItems;
    public  List<AirportModel> FilterAirportItems;
    public List<AirportModel> airportList;
    private  int _screen_height;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public AirportAdapter(Activity activity, List<AirportModel> AirportItems) {
        this.activity = activity;
        this.AirportItems = AirportItems;
        FilterAirportItems = AirportItems;
    }
 
    @Override
    public int getCount() {
        return AirportItems.size();
    }
 
    @Override
    public Object getItem(int location) {
        return AirportItems.get(location);
    }
 
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
            convertView = inflater.inflate(R.layout.airport_row, null);
 
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        TextView title = (TextView) convertView.findViewById(R.id.title);

        // getting data for the row
        AirportModel m = AirportItems.get(position);

        // title
        title.setText(m.getValue());
 
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
                AirportItems = (ArrayList<AirportModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}