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

    private CharSequence mTitles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    private int mNumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    private FragmentManager mFragmentManager;
    private FlightOnwardDomestic mFlightOnward;
    private FlightReturnDomestic mFlightReturn;
    private List<OnwardDomesticFlightModel> mOnwardDomesticModel;
    List<ReturnDomesticFlightModel> mReturnDomesticModel;

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public FlightViewPagerAdapter(FragmentManager mFragmentManager,CharSequence mTitles[], int mNumbOfTabsumb, List<OnwardDomesticFlightModel> mOnwardDomesticModel, List<ReturnDomesticFlightModel> return_domestic_model) {
        super(mFragmentManager);
        this.mFragmentManager = mFragmentManager;
        this.mTitles = mTitles;
        this.mNumbOfTabs = mNumbOfTabsumb;
        this.mOnwardDomesticModel = mOnwardDomesticModel;
        this.mReturnDomesticModel = return_domestic_model;
    }

    //This method return the fragment for the every position in the View Pager
    public Fragment getItem(int position) {

        if(position == 0) // if the position is 0 we are returning the First tab
        {
            mFlightOnward = new FlightOnwardDomestic();
            mFlightOnward.setOnwardModel(mOnwardDomesticModel);
            return mFlightOnward;
        }
        else             // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            mFlightReturn = new FlightReturnDomestic();
            mFlightReturn.setOnwardModel(mReturnDomesticModel);
            return mFlightReturn;
        }


    }

    // This method return the titles for the Tabs in the Tab Strip
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    // This method return the Number of tabs for the tabs Strip
    public int getCount() {
        return mNumbOfTabs;
    }
}