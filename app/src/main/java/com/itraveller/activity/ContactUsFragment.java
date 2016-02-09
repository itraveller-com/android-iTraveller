package com.itraveller.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itraveller.R;


public class ContactUsFragment extends Fragment {

    View rootView;

    public ContactUsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.contact_us, container, false);


        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Contact Us");


        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                    // handle back button
                    Log.d("Hello user", "hi");
                    ((MainActivity) getActivity()).onDrawerItemSelected(rootView, 0);

                    return true;
                }

                return false;
            }
        });



        // Inflate the layout for this fragment
        return rootView;
    }
}
