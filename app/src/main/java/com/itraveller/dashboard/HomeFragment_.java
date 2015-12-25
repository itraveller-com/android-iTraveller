package com.itraveller.dashboard;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.itraveller.R;
import com.itraveller.activity.MaterialLandingActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment_ extends Fragment {

    Button my_travel_btn,my_explorer_btn;
    Toolbar mtoolbar;

    public HomeFragment_() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //setting layout of screen to login.xml file
        View view=inflater.inflate(R.layout.home_page, container, false);

        my_explorer_btn=(Button) view.findViewById(R.id.my_explore_btn);
        my_travel_btn=(Button) view.findViewById(R.id.my_travel_btn);


        ((MyTravelActivity) getActivity()).getSupportActionBar().setTitle("Welcome to itraveller");


        my_explorer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MaterialLandingActivity fragment=new MaterialLandingActivity();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                //    setActionBarNavDrawer();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.commit();

            }
        });

        my_travel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i=new Intent(getActivity(),MyTravelActivity.class);
                startActivity(i);
                getActivity().finish();
                /*MyTravelFragment fragment=new MyTravelFragment();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                //    setActionBarNavDrawer();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.commit();
*/

            }
        });

        return view;
    }


}
