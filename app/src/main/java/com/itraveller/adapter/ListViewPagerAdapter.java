package com.itraveller.adapter;

/**
 * Created by VNK on 6/25/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.itraveller.R;
import com.itraveller.activity.HotelActivity;
import com.itraveller.model.HotelModel;
import com.itraveller.volley.AppController;


public class ListViewPagerAdapter extends ArrayAdapter<String> {
    private ViewPager[] mViewPagers;
    public static ViewPagerAdapter sViewPagerAdapter;
    private int mSwapValue = 0;
    private int mSingleLoopBit =0;
    private SharedPreferences mMySettings;
    private ImageView[] mLeftArrow;
    private ImageView[] mRightArrow;
    private RelativeLayout[] mRelLeftArrow;
    private RelativeLayout[] mRelRightArrow;
    private int mCheckbit = 0;
    private int mCheckBoolean = 0;

    private HotelActivity.pagerCheckBoxChangedListner1 mListviewChangedListener;

    private Context mContext;
    private ArrayList<String> mNavigationItems;
    private ArrayList<String> mDefaultHotelRoom;
    public static HashMap<String,ArrayList<HotelModel>> mHotelModels;
    private Map<Integer, Integer> mPagerPositions ;
    private String[] mHotelDestination;
    private String[] mHotelNights;

    public ListViewPagerAdapter(Context mContext, ArrayList<String> mNavigationItems, ArrayList<String> mDefaultHotelRoom, HotelActivity.pagerCheckBoxChangedListner1 pagerviewlistener) {
        super(mContext, R.layout.view_pager_list_view, mNavigationItems);
        this.mContext = mContext;
        this.mNavigationItems = mNavigationItems;
        this.mDefaultHotelRoom = mDefaultHotelRoom;
        this.mListviewChangedListener = pagerviewlistener;
        mHotelModels=new HashMap<>();
        for(int index=0;index<mNavigationItems.size();index++){
            mHotelModels.put(""+index,new ArrayList<HotelModel>());
        }

        mPagerPositions= new HashMap<Integer, Integer>();
        //mViewPagerAdapter=new ViewPagerAdapter[navigationItems.size()];
        mViewPagers=new ViewPager[mNavigationItems.size()];

        mLeftArrow = new ImageView[mNavigationItems.size()];
        mRightArrow = new ImageView[mNavigationItems.size()];
        mRelLeftArrow = new RelativeLayout[mNavigationItems.size()];
        mRelRightArrow = new RelativeLayout[mNavigationItems.size()];
    }

    @Override
    public int getCount() {
        return mNavigationItems.size();
    }


    @Override
    public String getItem(int position) {
        return mNavigationItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView( final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.hotel_pageviewer, null);


        }else{

        }

        mMySettings= mContext.getSharedPreferences("Itinerary", 0);

        String Destintion_night = mMySettings.getString("DestinationCount", null);
        String Destintion_NAME = mMySettings.getString("DestinationName", null);
        mHotelDestination = Destintion_NAME.trim().split(",");
        mHotelNights = Destintion_night.trim().split(",");
        TextView txtview = (TextView) convertView.findViewById(R.id.hotel_place_name);
        txtview.setText("" + mHotelDestination[position] + " (" + mHotelNights[position] + "Nights)");
        mViewPagers[position] = (ViewPager) convertView.findViewById(R.id.list_pager);
        sViewPagerAdapter = new ViewPagerAdapter(mHotelModels.get(""+position),new PagerCheckedChangeListnerCustom(position));
        mViewPagers[position].setAdapter(sViewPagerAdapter);

        mRelLeftArrow[position]=(RelativeLayout) convertView.findViewById(R.id.rel_left_arrow);
        mRelRightArrow[position]=(RelativeLayout) convertView.findViewById(R.id.rel_right_arrow);

        mLeftArrow[position]=(ImageView) convertView.findViewById(R.id.left_arrow);
        mRightArrow[position]=(ImageView) convertView.findViewById(R.id.right_arrow);

        mViewPagers[position].setOnClickListener(new ViewPagerClickListner(position));
        mViewPagers[position].setOnPageChangeListener(new ViewPageChangeListner(position));
        if(mPagerPositions.get(position)!=null){
            mViewPagers[position].setCurrentItem(mPagerPositions.get(position));
        }

        if(mHotelModels.get(""+position).size()<1){

            mLeftArrow[position].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Left arrow", "hi");
                    mViewPagers[position].setCurrentItem(mViewPagers[position].getCurrentItem() - 1);

                    ViewPagerAdapter.sCount=0;
                }
            });

            mRightArrow[position].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Right arrow", "bye");
                    mViewPagers[position].setCurrentItem(mViewPagers[position].getCurrentItem() + 1);

                    ViewPagerAdapter.sCount=0;
                }
            });


            setHotelModelData(mNavigationItems.get(position), position);
            ArrayList<HotelModel> modelRow=mHotelModels.get("" + position);
            Log.i("TestingRound","Testing123" + modelRow.size());

            if(modelRow.size() == 0){
                mViewPagers[position].setVisibility(View.GONE);
                mRightArrow[position].setVisibility(View.GONE);
                mLeftArrow[position].setVisibility(View.GONE);
                mRelLeftArrow[position].setVisibility(View.GONE);
                mRelRightArrow[position].setVisibility(View.GONE);
            }
            else{
                mViewPagers[position].setVisibility(View.VISIBLE);
                mRightArrow[position].setVisibility(View.VISIBLE);
                mLeftArrow[position].setVisibility(View.VISIBLE);
                mRelLeftArrow[position].setVisibility(View.GONE);
                mRelRightArrow[position].setVisibility(View.GONE);
            }

        }
        else
        {

        }

        return convertView;
    }


    public void setHotelModelData(String url, final int position )
    {
        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.i("Test", "Testing" + response);
                    Log.d("Boolean", "" + response.getBoolean("success"));
                    Log.d("Error", ""+response.getJSONObject("error"));
                    Log.d("Payload", ""+response.getJSONArray("payload"));
                    if(response.getJSONArray("payload").length()==0){
                        mSingleLoopBit =1;
                    }else{
                        mSingleLoopBit =0;
                    }
                    int flag_bit = 0;
                    ArrayList hotelList=new ArrayList();
                    for (int i = 0; i < response.getJSONArray("payload").length(); i++) {
                        JSONObject jsonarr = response.getJSONArray("payload").getJSONObject(i);

                        HotelModel hotel_model = new HotelModel();
                        for(int index = 0;index < mDefaultHotelRoom.size();index++) {
                            if(flag_bit  == 0) {
                                hotel_model.setHotel_Id(jsonarr.getInt("Hotel_Id"));
                                hotel_model.setRegion_Id(jsonarr.getString("Region_Id"));
                                hotel_model.setDestination_Id(jsonarr.getString("Destination_Id"));
                                hotel_model.setHotel_Name(jsonarr.getString("Hotel_Name"));
                                hotel_model.setHotel_Email(jsonarr.getString("Hotel_Email"));
                                hotel_model.setHotel_Description(jsonarr.getString("Hotel_Description"));
                                hotel_model.setHotel_Tripadvisor(jsonarr.getString("Hotel_Tripadvisor"));
                                hotel_model.setHotel_Meal_Plan(jsonarr.getString("Hotel_Meal_Plan"));
                                hotel_model.setHotel_Image(jsonarr.getString("Hotel_Image"));
                                hotel_model.setHotel_Status(jsonarr.getInt("Hotel_Status"));
                                hotel_model.setHotel_Star_Rating(jsonarr.getString("Hotel_Star_Rating"));
                                hotel_model.setHotel_Address(jsonarr.getString("Hotel_Address"));
                                hotel_model.setHotel_Latitude(jsonarr.getString("Hotel_Latitude"));
                                hotel_model.setHotel_Longitude(jsonarr.getString("Hotel_Longitude"));
                                hotel_model.setHotel_URL(jsonarr.getString("Hotel_URL"));
                                hotel_model.setHotel_Number(jsonarr.getString("Hotel_Number"));
                                hotel_model.setDistrict(jsonarr.getString("District"));
                                hotel_model.setState(jsonarr.getString("State"));
                                hotel_model.setCountry(jsonarr.getString("Country"));
                                hotel_model.setPincode(jsonarr.getString("Pincode"));
                                hotel_model.setDinner(jsonarr.getInt("Dinner"));
                                hotel_model.setLunch(jsonarr.getInt("Lunch"));
                                hotel_model.setExtra_Adult(jsonarr.getInt("Extra_Adult"));
                                hotel_model.setVisibility(jsonarr.getInt("Visibility"));
                                hotel_model.setWebsite(jsonarr.getString("Website"));
                                hotel_model.setB2C_Flag(jsonarr.getInt("B2C_Flag"));
                                hotel_model.setTrip_Image(jsonarr.getString("Trip_Image"));
                                hotel_model.setTrip_Script(jsonarr.getString("Trip_Script"));
                                hotel_model.setAccount_Holder(jsonarr.getString("Account_Holder"));
                                hotel_model.setAccount_Number(jsonarr.getString("Account_Number"));
                                hotel_model.setBank(jsonarr.getString("Bank"));
                                hotel_model.setIFSC_Code(jsonarr.getString("IFSC_Code"));
                                hotel_model.setDate(jsonarr.getString("Date"));
                                hotel_model.setAdmin_Id(jsonarr.getString("admin_Id"));
                                String[] value = mDefaultHotelRoom.get(index).trim().split(",");
                                if (Integer.parseInt("" + value[0]) == jsonarr.getInt("Hotel_Id")) {
                                    hotel_model.setChecked(true);
                                    if(value.length>4) {
                                        hotel_model.setLunch(Integer.parseInt(value[4]));
                                        hotel_model.setDinner(Integer.parseInt(value[5]));
                                    }
                                    mSwapValue = i;
                                    flag_bit = 1;
                                    mCheckBoolean =1;
                                } else {
                                    hotel_model.setChecked(false);
                                    hotel_model.setLunch(0);
                                    hotel_model.setDinner(0);
                                }
                            }

                        }

                        if(i == (response.getJSONArray("payload").length() - 1)){
                            hotelList.add(hotel_model);
                        }
                        else{
                            hotelList.add(hotel_model);
                            mCheckbit = 1;
                        }
                        flag_bit =0;
                    }

                    if(response.getJSONArray("payload").length()!=0){
                        HotelModel hotel_model = new HotelModel();
                        hotelList.add(ownAccomadation(hotel_model, mCheckBoolean, hotelList.size()));
                        Collections.swap(hotelList, 0, mSwapValue);

                        mSwapValue = 0;
                        sViewPagerAdapter.notifyDataSetChanged();
                    }
                    mHotelModels.put(position + "", hotelList);
                    ArrayList<HotelModel> modelRow=mHotelModels.get("" + position);
                    if(modelRow.size() == 0)
                    {
                        mViewPagers[position].setVisibility(View.GONE);
                        mLeftArrow[position].setVisibility(View.GONE);
                        mRightArrow[position].setVisibility(View.GONE);

                        mRelLeftArrow[position].setVisibility(View.GONE);
                        mRelRightArrow[position].setVisibility(View.GONE);
                    }
                    else
                    {
                        mViewPagers[position].setVisibility(View.VISIBLE);
                        mLeftArrow[position].setVisibility(View.VISIBLE);
                        mRightArrow[position].setVisibility(View.VISIBLE);

                        mRelLeftArrow[position].setVisibility(View.VISIBLE);
                        mRelRightArrow[position].setVisibility(View.VISIBLE);
                    }

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    VolleyLog.d("Volley Error", "Error: " + e.getMessage());
                }

                if(mSingleLoopBit!=1)
                {
                    HotelActivity.listViewPagerAdapter.notifyDataSetChanged();
                    sViewPagerAdapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Volley Error", "Error: " + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(strReq);
    }

    private class ViewPagerClickListner implements ViewPager.OnClickListener{

        int postionClicked;
        public ViewPagerClickListner(int postionClick)
        {
            this.postionClicked = postionClick;
        }

        @Override
        public void onClick(View view) {

            Log.e("Pager position ", "parent " + postionClicked + "child position " + mPagerPositions.get(postionClicked) + "Viewpager.Currentpostion" +mViewPagers[postionClicked].getCurrentItem());
            mViewPagers[postionClicked].getCurrentItem();

        }
    }
    private class ViewPageChangeListner implements ViewPager.OnPageChangeListener{
        private int selectPosition;
        public ViewPageChangeListner(int selectPosition){
            this.selectPosition=selectPosition;


        }
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Log.d("PAGER ", "PAGER SCROLL child POSITION " + position + "parent position " + selectPosition);
            mPagerPositions.put(selectPosition,position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    interface pagerCheckBoxChangedListner{
        public void OnCheckedChangeListenerCustomPager(int childPosition, boolean isChecked);
        public  void OnImageClickListenerCustomPager(int childpostion);
    }


    public class PagerCheckedChangeListnerCustom implements pagerCheckBoxChangedListner{
        int groupPosition;

        public PagerCheckedChangeListnerCustom(int groupPosition) {
            this.groupPosition = groupPosition;
        }

        @Override
        public void OnCheckedChangeListenerCustomPager(int childPosition,boolean isChecked) {
            mListviewChangedListener.OnCheckedChangeListenerCustomPager(childPosition, isChecked);
            ArrayList<HotelModel> modelRow=mHotelModels.get(""+groupPosition);
            for(int index =0 ; index<modelRow.size();index++) {
                if(childPosition==index) {
                    modelRow.get(index).setChecked(isChecked);
                    mHotelModels.put("" + groupPosition, modelRow);
                }
                else
                    modelRow.get(index).setChecked(false);
            }
            notifyDataSetChanged();
        }

        @Override
        public void OnImageClickListenerCustomPager(int childpostion) {
            mListviewChangedListener.OnImageClickListenerCustomPager(childpostion, groupPosition );

        }
    }

    public HotelModel ownAccomadation(HotelModel hotel_model, int checkvalue, int swapvalueat){
        hotel_model.setHotel_Id(0);
        hotel_model.setRegion_Id("All");
        hotel_model.setDestination_Id("0");
        hotel_model.setHotel_Name("Own Accommodation");
        hotel_model.setHotel_Email("");
        hotel_model.setHotel_Description("");
        hotel_model.setHotel_Tripadvisor("");
        hotel_model.setHotel_Meal_Plan("");
        hotel_model.setHotel_Image("");
        hotel_model.setHotel_Status(0);
        hotel_model.setHotel_Star_Rating("");
        hotel_model.setHotel_Address("");
        hotel_model.setHotel_Latitude("");
        hotel_model.setHotel_Longitude("");
        hotel_model.setHotel_URL("");
        hotel_model.setDistrict("");
        hotel_model.setState("");
        hotel_model.setCountry("");
        hotel_model.setPincode("");
        hotel_model.setDinner(0);
        hotel_model.setLunch(0);
        hotel_model.setExtra_Adult(0);
        hotel_model.setVisibility(0);
        hotel_model.setWebsite("");
        hotel_model.setB2C_Flag(0);
        hotel_model.setTrip_Image("");
        hotel_model.setTrip_Script("");
        hotel_model.setAccount_Holder("");
        hotel_model.setAccount_Number("0");
        hotel_model.setBank("");
        hotel_model.setIFSC_Code("");
        hotel_model.setDate("");
        hotel_model.setAdmin_Id("");
        if(checkvalue == 0) {
            hotel_model.setChecked(true);
            mSwapValue = swapvalueat;
        }
        else
        {
            hotel_model.setChecked(false);
        }
        return hotel_model;
    }

}