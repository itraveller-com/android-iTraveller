package com.itraveller.dashboard;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itraveller.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreatedTripsFragment extends Fragment {


    public CreatedTripsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.created_trips, container, false);


        ((MyTravelActivity) getActivity()).getSupportActionBar().setTitle("Your created Trips");

        return view;
    }


}
