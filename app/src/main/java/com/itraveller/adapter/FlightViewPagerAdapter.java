package com.itraveller.adapter;

/**
 * Created by VNK on 7/28/2015.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import com.itraveller.activity.FlightOnwardDomestic;
import com.itraveller.activity.FlightReturnDomestic;
import com.itraveller.model.OnwardDomesticFlightModel;
import com.itraveller.model.ReturnDomesticFlightModel;

public class FlightViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    FragmentManager fm;
    FlightOnwardDomestic flight_onward;
    FlightReturnDomestic flight_return;
    List<OnwardDomesticFlightModel> onward_domestic_model;
    List<ReturnDomesticFlightModel> return_domestic_model;

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public FlightViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb, List<OnwardDomesticFlightModel> onward_domestic_model, List<ReturnDomesticFlightModel> return_domestic_model) {
        super(fm);
        this.fm = fm;
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
        this.onward_domestic_model = onward_domestic_model;
        this.return_domestic_model = return_domestic_model;
    }

    //This method return the fragment for the every position in the View Pager
    public Fragment getItem(int position) {

        if(position == 0) // if the position is 0 we are returning the First tab
        {
            flight_onward = new FlightOnwardDomestic();
            flight_onward.setOnwardModel(onward_domestic_model);
            return flight_onward;
        }
        else             // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            flight_return = new FlightReturnDomestic();
            flight_return.setOnwardModel(return_domestic_model);
            return flight_return;
        }


    }

    // This method return the titles for the Tabs in the Tab Strip
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip
    public int getCount() {
        return NumbOfTabs;
    }
}