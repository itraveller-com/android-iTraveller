package com.itraveller.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import com.itraveller.R;
import com.itraveller.activity.HotelActivity;
import com.itraveller.constant.Utility;
import com.itraveller.model.HotelRoomModel;
import com.itraveller.volley.AppController;

public class HotelRoomAdapter extends BaseAdapter {

    private Activity mActivity;
    private LayoutInflater mLayoutInflater;
    private List<HotelRoomModel> mHotelRooms;
    private ArrayList<String> mLowestHotelList;
    private RadioButton mSelectedRB;
    private int mSelectedPosition = -1;
    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private HotelActivity.RadiobuttonListener RadioListener;
    private int mAdults;
    private int mChildren;
    private int mGroupPosition;
    private SharedPreferences mPrefs;

    public HotelRoomAdapter(Activity mActivity, List<HotelRoomModel> mHotelRooms, ArrayList<String> mLowestHotelList,int mGroupPosition, HotelActivity.RadiobuttonListener RadiobuttonListener) {
        this.mActivity = mActivity;
        this.mHotelRooms = mHotelRooms;
        this.RadioListener = RadiobuttonListener;
        this.mLowestHotelList = mLowestHotelList;
        this.mGroupPosition = mGroupPosition;
    }

    //getting count of total number of hotel rooms
    @Override
    public int getCount() {
        return mHotelRooms.size();
    }

    //getting item from given location
    @Override
    public Object getItem(int location) {
        return mHotelRooms.get(location);
    }

    //getting itemID
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

       ViewHolder holder = null;

        if (mLayoutInflater == null)
            mLayoutInflater = (LayoutInflater) mActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.hotel_single, null);
            holder.title = (TextView) convertView.findViewById(R.id.room_name);
            holder.rate = (TextView) convertView.findViewById(R.id.rate);
            holder.radioButton = (RadioButton) convertView.findViewById(R.id.radioButton);
            holder.btn_plus =(Button) convertView.findViewById(R.id.plus);
            holder.btn_minus =(Button) convertView.findViewById(R.id.minus);
            holder.btn_count =(Button) convertView.findViewById(R.id.count);
            convertView.setTag(holder);

            ViewPagerAdapter.sCount=0;

            mPrefs = mActivity.getSharedPreferences("Itinerary", mActivity.MODE_PRIVATE);

            mAdults = Integer.parseInt(mPrefs.getString("Adult", "0"));
            mChildren=Integer.parseInt(mPrefs.getString("Child","0"));
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        // getting data for the row
        final HotelRoomModel m = mHotelRooms.get(position);

        // title
        holder.title.setText(m.getRoom_Type());

        try {
            String[] hotel_room_Data = mLowestHotelList.get(mGroupPosition).trim().split(",");

            int min_capacity=m.getDefault_Number();
            int max_capacity=m.getMaximum_Number();
            int no_of_rooms;


            Log.d("No of rooms adul",""+mAdults);

            Log.d("No of rooms min",""+min_capacity);


            Log.d("No of rooms max",""+max_capacity);

        /*    if(adults%max_capacity==0)
            {
                no_of_rooms=adults/max_capacity;
            }
            else
            {
                if(adults==min_capacity)
                    no_of_rooms=(adults/min_capacity);
                else
                    no_of_rooms=(adults/min_capacity)+1;
            }
        */

            if((mAdults+mChildren)%min_capacity==0)
                no_of_rooms=(mAdults/min_capacity);
            else
                no_of_rooms=(mAdults/min_capacity)+1;

            Log.d("No of rooms or",""+no_of_rooms);

            holder.btn_count.setText("" +no_of_rooms);
            //holder.rate.setText(""+m.getDisplay_Tariff());
           if (Integer.parseInt(hotel_room_Data[2]) == m.getDisplay_Tariff()){
                //holder.rate.setText("\u20B9"+"" + m.getDisplay_Tariff());
                holder.rate.setText("Same price");
            }
            else if(Integer.parseInt(hotel_room_Data[2]) > m.getDisplay_Tariff()){
                holder.rate.setText((Integer.parseInt(hotel_room_Data[2]) - m.getDisplay_Tariff()) + " less");
            }
            else {
                holder.rate.setText((m.getDisplay_Tariff() - Integer.parseInt(hotel_room_Data[2])) + " more");
            }
        }
        catch (Exception e)
        {

        }

        //holder.rate.setText("\u20B9"+"" + m.getDisplay_Tariff());

        final ViewHolder finalHolder = holder;
        holder.btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(finalHolder.radioButton.isChecked()) {
                    int x = Integer.parseInt(finalHolder.btn_count.getText().toString()) + 1;
                    finalHolder.btn_count.setText("" + x);
                    RadioListener.RadioChangeListenerCustom(m.getHotel_Id() + "," + m.getHotel_Room_Id() + "," + m.getDisplay_Tariff() + "," + finalHolder.btn_count.getText().toString()  +",0,0" );
                    //m.set("" + x);
                }
            }
        });

        holder.btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               int x = Integer.parseInt(finalHolder.btn_count.getText().toString()) - 1;
                if(finalHolder.radioButton.isChecked()) {
                    if (x > 0) {
                        finalHolder.btn_count.setText("" + x);
                        RadioListener.RadioChangeListenerCustom(m.getHotel_Id() + "," + m.getHotel_Room_Id() + "," + m.getDisplay_Tariff() + "," + finalHolder.btn_count.getText().toString()  +",0,0");
                    }
                }
                //m.setNights("" + x);
            }
        });

        holder.radioButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(position != mSelectedPosition && mSelectedRB != null){
                    mSelectedRB.setChecked(false);
                }
                mSelectedPosition = position;
                mSelectedRB = (RadioButton)v;
                RadioListener.RadioChangeListenerCustom(m.getHotel_Id() + "," + m.getHotel_Room_Id() +"," + m.getDisplay_Tariff()+ "," +finalHolder.btn_count.getText().toString() +",0,0" );
                Log.i("Room Data", m.getHotel_Id() + "," + m.getHotel_Room_Id() +"," + m.getDisplay_Tariff());

            }
        });

        if(mSelectedPosition != position){
            holder.radioButton.setChecked(false);
        }else{
                holder.radioButton.setChecked(true);
            Log.d("Checked box", "hi");
            if(mSelectedRB != null && holder.radioButton != mSelectedRB){
                mSelectedRB = holder.radioButton;
            }
        }
        holder.radioButton.setChecked(m.getCheck());
        if(m.getCheck())
        {  mSelectedRB = holder.radioButton;
            mSelectedPosition = position;
        }

        return convertView;
    }

    public List<HotelRoomModel> getList(){
        return mHotelRooms;
    }

    private class ViewHolder{
        TextView title;
        TextView rate;
        RadioButton radioButton;
        Button btn_plus;
        Button btn_minus;
        Button btn_count;
    }
}