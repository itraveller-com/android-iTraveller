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

import org.w3c.dom.CharacterData;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

import com.itraveller.R;
import com.itraveller.adapter.FlightDomesticOnwardAdapter;
import com.itraveller.adapter.FlightDomesticReturnAdapter;
import com.itraveller.constant.CustomLoading;
import com.itraveller.model.FlightModel;
import com.itraveller.model.ReturnDomesticFlightModel;

/**
 * Created by VNK on 7/28/2015.
 */
public class FlightReturnDomestic extends Fragment {


    public static FlightDomesticReturnAdapter adapter = null;
    List<ReturnDomesticFlightModel> return_domestic_model;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.flights_domestic_list, container, false);
            SharedPreferences prefs = getActivity().getSharedPreferences("Itinerary",Context.MODE_PRIVATE);


            TextView dep=(TextView) rootView.findViewById(R.id.dep_city);
            TextView arr=(TextView) rootView.findViewById(R.id.arr_city);
            dep.setText(""+prefs.getString("TravelFrom", null));
            arr.setText(""+prefs.getString("DepartureAirport", null));
            ListView listview = (ListView) rootView.findViewById(R.id.flights_listview);
            adapter = new FlightDomesticReturnAdapter(getActivity(), return_domestic_model);
            listview.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            return rootView;
        }

    public void setOnwardModel(List<ReturnDomesticFlightModel> return_domestic_model) {
        this.return_domestic_model = return_domestic_model;
    }
}
