package com.itraveller.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.itraveller.R;
import com.itraveller.dashboard.MyTravelActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home_Fragment extends Fragment {

    Button my_travel_btn,my_explorer_btn;
    Toolbar mtoolbar;
    View rootView;
    boolean doubleBackToExitPressedOnce = false;

    public Home_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //setting layout of screen to login.xml file
        rootView=inflater.inflate(R.layout.home_page, container, false);

        my_explorer_btn=(Button) rootView.findViewById(R.id.my_explore_btn);
        my_travel_btn=(Button) rootView.findViewById(R.id.my_travel_btn);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Welcome to itraveller");

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK)
                {

                    // handle back button

                    if (doubleBackToExitPressedOnce)
                    {
                        getActivity().finish();
                        return true;
                    }

                    doubleBackToExitPressedOnce = true;
                    Toast.makeText(getActivity(), "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            doubleBackToExitPressedOnce=false;
                        }
                    }, 2000);

                    return true;
                }

                return false;
            }
        });



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
/*                MyTravelFragment fragment=new MyTravelFragment();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //    setActionBarNavDrawer();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.commit();
*/

            }
        });

        return rootView;
    }


}
