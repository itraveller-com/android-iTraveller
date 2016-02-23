package com.itraveller.dashboard;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.itraveller.R;
import com.itraveller.model.MyTravelModel;

import java.util.List;

/**
 * Created by rohan bundelkhandi on 18/02/2016.
 */
public class ActivityAdapter extends BaseAdapter {

    private Activity mActivity;
    private LayoutInflater mLayoutInflater;
    private List<MyTravelActivityModel> myTravelActivityModels;

    private TextView mActivityName;
    private TextView mActivityDate;

    public ActivityAdapter(Activity mActivity, List<MyTravelActivityModel> myTravelActivityModels) {
        this.mActivity = mActivity;
        this.myTravelActivityModels = myTravelActivityModels;
    }

    //getting count of total number of Landingitems
    @Override
    public int getCount() {
        return myTravelActivityModels.size();
    }

    //getting item from given location
    @Override
    public Object getItem(int location) {
        return myTravelActivityModels.get(location);
    }

    //getting itemID
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (mLayoutInflater == null)
            mLayoutInflater = (LayoutInflater) mActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = mLayoutInflater.inflate(R.layout.trip_summary_activity_item, null);

        mActivityName=(TextView) convertView.findViewById(R.id.activity_name_value);
        mActivityDate=(TextView) convertView.findViewById(R.id.activity_date_value);

        Log.d("Position test",""+position);

        MyTravelActivityModel myTravelActivityModel = myTravelActivityModels.get(position);

        mActivityName.setText(myTravelActivityModel.getActivityName());

        String datea[]=(""+myTravelActivityModel.getActivityDate()).split("-");
        mActivityDate.setText(datea[2]+"-"+datea[1]+"-"+datea[0]);

        return convertView;
    }

}
