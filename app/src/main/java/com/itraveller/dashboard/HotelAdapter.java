package com.itraveller.dashboard;

import android.app.Activity;
import android.content.Context;
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
public class HotelAdapter extends BaseAdapter{

    private Activity mActivity;
    private LayoutInflater mLayoutInflater;
    private List<MyTravelHotelModel> myTravelHotelModels;

    private TextView mDestinationName;
    private TextView mHotelName;
    private TextView mRoomType;
    private TextView mNoOfRooms;
    private TextView mNoOfDays;
    private TextView mCheckInDate;
    private TextView mCheckoutDate;

    public HotelAdapter(Activity mActivity, List<MyTravelHotelModel> myTravelHotelModels) {
        this.mActivity = mActivity;
        this.myTravelHotelModels = myTravelHotelModels;
    }

    //getting count of total number of Landingitems
    @Override
    public int getCount() {
        return myTravelHotelModels.size();
    }

    //getting item from given location
    @Override
    public Object getItem(int location) {
        return myTravelHotelModels.get(location);
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
            convertView = mLayoutInflater.inflate(R.layout.trip_summary_hotel_item, null);

        mDestinationName=(TextView) convertView.findViewById(R.id.destination_name_value);
        mHotelName=(TextView) convertView.findViewById(R.id.hotel_name_value);
        mRoomType=(TextView) convertView.findViewById(R.id.room_type_value);
        mNoOfRooms=(TextView) convertView.findViewById(R.id.no_of_room_value);
        mNoOfDays=(TextView) convertView.findViewById(R.id.no_of_days_value);
        mCheckInDate=(TextView) convertView.findViewById(R.id.check_in_date_value);
        mCheckoutDate=(TextView) convertView.findViewById(R.id.check_out_date_value);

        MyTravelHotelModel m = myTravelHotelModels.get(position);

        mDestinationName.setText(""+m.getDestinationName());
        mHotelName.setText(""+m.getHotelName());
        mRoomType.setText(""+m.getHotelRoomType());
        mNoOfRooms.setText(""+m.getNoOfRooms());
        mNoOfDays.setText(""+m.getNoOfRoomDays());

        String dateI[]=(""+m.getCheckInDate()).split("-");
        mCheckInDate.setText(dateI[2]+"-"+dateI[1]+"-"+dateI[0]);

        String dateO[]=(""+m.getCheckOutDate()).split("-");
        mCheckoutDate.setText(dateO[2]+"-"+dateO[1]+"-"+dateO[0]);

        return convertView;
    }


}
