package com.itraveller.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import com.itraveller.R;
import com.itraveller.adapter.FlightDomesticOnwardAdapter;
import com.itraveller.model.OnwardDomesticFlightModel;

/**
 * Created by VNK on 7/28/2015.
 */
public class FlightOnwardDomestic extends Fragment {


    public static FlightDomesticOnwardAdapter adapter = null;
    List<OnwardDomesticFlightModel> onward_domestic_model;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {



            View rootView = inflater.inflate(R.layout.flights_domestic_list, container, false);
            //CustomLoading.LoadingScreen(getActivity(), false);
            SharedPreferences prefs = getActivity().getSharedPreferences("Itinerary", Context.MODE_PRIVATE);
            TextView dep=(TextView) rootView.findViewById(R.id.dep_city);
            TextView arr=(TextView) rootView.findViewById(R.id.arr_city);
            dep.setText(""+prefs.getString("ArrivalAirport", null));
            arr.setText(""+prefs.getString("TravelFrom", null));
            ListView listview = (ListView) rootView.findViewById(R.id.flights_listview);
            adapter = new FlightDomesticOnwardAdapter(getActivity(), onward_domestic_model);
            listview.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            return rootView;
        }

    public void setOnwardModel(List<OnwardDomesticFlightModel> onward_domestic_model) {
        this.onward_domestic_model = onward_domestic_model;
    }
}
