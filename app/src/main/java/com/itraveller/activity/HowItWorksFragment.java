package com.itraveller.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.itraveller.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HowItWorksFragment extends Fragment {


    public HowItWorksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("How it works?");
        return inflater.inflate(R.layout.how_it_works, container, false);
    }


}
