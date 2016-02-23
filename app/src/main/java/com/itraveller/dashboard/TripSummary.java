package com.itraveller.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itraveller.R;
import com.itraveller.constant.NonScrollListView;
import com.itraveller.moxtraChat.AccessTokenModel;
import com.itraveller.moxtraChat.AddUserResponse;
import com.itraveller.moxtraChat.CreateBinderRequest;
import com.itraveller.moxtraChat.CreateBinderResponse;
import com.itraveller.moxtraChat.PreferenceUtil;
import com.itraveller.volley.AppController;
import com.moxtra.sdk.MXAccountManager;
import com.moxtra.sdk.MXChatManager;
import com.moxtra.sdk.MXSDKConfig;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rohan bundelkhandi on 05/02/2016.
 */
public class TripSummary extends AppCompatActivity{


    private TextView mNameText;
    private TextView mPlacesText;
    private TextView mArrDateText;
    private TextView mNoDaysText;
    private TextView mAdultsText;
    private TextView mChild512Text;
    private TextView mChildBelow5Text;
    private TextView mBookingDateText;
    private TextView mTotalPriceText;
    private TextView mDiscountPriceText;
    private TextView mPriceAdvanceText;
    private TextView mRemainingDPriceText;
    private TextView mTransportationText;
    private TextView mEmailText;
    private TextView mTravelIDText;
    private TextView mPhoneText;
    private TextView mRemainingPriceText;


    private SharedPreferences mPrefs;
    private String mFlag;
    private int mSelectedPosition;
    private Toolbar mToolbar;

    private Bundle mBundle;

    private HotelAdapter hotelAdapter;
    private ActivityAdapter activityAdapter;

    private NonScrollListView hotelList;
    private NonScrollListView activityList;

    private List<MyTravelHotelModel> myTravelHotelModels=new ArrayList<>();
    private List<MyTravelActivityModel> myTravelActivityModels=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.trip_summary);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().show();
        getSupportActionBar().setTitle("Your Trip Details");

        mPrefs=getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        mBundle=getIntent().getExtras();

        mSelectedPosition=mBundle.getInt("ItineraryData");
        mFlag=mBundle.getString("LFlag");

        myTravelHotelModels.clear();
        myTravelActivityModels.clear();


        if(mFlag.equals("upcoming"))
        {
            if(mSelectedPosition==0)
            {
                int curr_ac=BookedTripsFragment.activitySize.get(mSelectedPosition);
                for(int i=0;i<curr_ac;i++)
                {
                    myTravelActivityModels.add(BookedTripsFragment.sActivityList.get(i));
                }

                int curr_h=BookedTripsFragment.hotelSize.get(mSelectedPosition);
                for(int i=0;i<curr_h;i++)
                {
                    myTravelHotelModels.add(BookedTripsFragment.sHotelList.get(i));
                }

            }
            else
            {
                int prev_ac_position=mSelectedPosition-1;
                int prev_ac=BookedTripsFragment.activitySize.get(prev_ac_position);
                int curr_ac=BookedTripsFragment.activitySize.get(mSelectedPosition)+prev_ac;
                for(int i=prev_ac;i<curr_ac;i++)
                {
                    myTravelActivityModels.add(BookedTripsFragment.sActivityList.get(i));
                }

                int prev_h_position=mSelectedPosition-1;
                int prev_h=BookedTripsFragment.hotelSize.get(prev_h_position);
                int curr_h=BookedTripsFragment.hotelSize.get(mSelectedPosition)+prev_h;
                for(int i=prev_h;i<curr_h;i++)
                {
                    myTravelHotelModels.add(BookedTripsFragment.sHotelList.get(i));
                }

            }
        }
        else
        {
            if(mSelectedPosition==0)
            {
                int curr_ac=BookedTripsFragment.activitySizeP.get(mSelectedPosition);
                for(int i=0;i<curr_ac;i++)
                {
                    myTravelActivityModels.add(BookedTripsFragment.sActivityListPast.get(i));
                }

                int curr_h=BookedTripsFragment.hotelSizeP.get(mSelectedPosition);
                for(int i=0;i<curr_h;i++)
                {
                    myTravelHotelModels.add(BookedTripsFragment.sHotelListPast.get(i));
                }

            }
            else
            {
                int prev_ac_position=mSelectedPosition-1;
                int prev_ac=BookedTripsFragment.activitySizeP.get(prev_ac_position);
                int curr_ac=BookedTripsFragment.activitySizeP.get(mSelectedPosition)+prev_ac;
                for(int i=prev_ac;i<curr_ac;i++)
                {
                    myTravelActivityModels.add(BookedTripsFragment.sActivityListPast.get(i));
                }

                int prev_h_position=mSelectedPosition-1;
                int prev_h=BookedTripsFragment.hotelSizeP.get(prev_h_position);
                int curr_h=BookedTripsFragment.hotelSizeP.get(mSelectedPosition)+prev_h;
                for(int i=prev_h;i<curr_h;i++)
                {
                    myTravelHotelModels.add(BookedTripsFragment.sHotelListPast.get(i));
                }

            }
        }

        hotelList=(NonScrollListView) findViewById(R.id.hotel_list);
        activityList=(NonScrollListView) findViewById(R.id.activity_list);

        hotelAdapter = new HotelAdapter(TripSummary.this, myTravelHotelModels);

        activityAdapter = new ActivityAdapter(TripSummary.this, myTravelActivityModels);


        hotelList.setAdapter(hotelAdapter);
        activityList.setAdapter(activityAdapter);

        Log.d("Hotel size", "" + hotelAdapter.getCount());
        Log.d("Hotel size1", "" + activityAdapter.getCount());

        mTravelIDText=(TextView) findViewById(R.id.travel_id_value);
        mNameText=(TextView) findViewById(R.id.name_value);
        mEmailText=(TextView) findViewById(R.id.email_value);
        mPhoneText=(TextView) findViewById(R.id.phone_number_value);
        mPlacesText=(TextView) findViewById(R.id.places_value);
        mBookingDateText=(TextView) findViewById(R.id.date_of_booking_value);
        mArrDateText=(TextView) findViewById(R.id.date_of_arrival_value);
        mNoDaysText=(TextView) findViewById(R.id.no_of_days_value);
        mAdultsText=(TextView) findViewById(R.id.no_of_adults_value);
        mChild512Text=(TextView) findViewById(R.id.no_of_children_5_12_value);
        mChildBelow5Text=(TextView) findViewById(R.id.no_of_children_below_5_value);
        mTotalPriceText=(TextView) findViewById(R.id.total_price_value);
        mDiscountPriceText=(TextView) findViewById(R.id.disount_value);
        mRemainingDPriceText=(TextView) findViewById(R.id.price_after_discount_value);
        mPriceAdvanceText=(TextView) findViewById(R.id.booking_advance_value);
        mRemainingPriceText=(TextView) findViewById(R.id.booking_remaining_value);
        mTransportationText=(TextView) findViewById(R.id.transportation_name_value);

        if(mFlag.equals("upcoming"))
        {
            mTravelIDText.setText("" + BookedTripsFragment.sUpcomingList.get(mSelectedPosition).getItinerary_Main_Id());
            mNameText.setText("" + BookedTripsFragment.sUpcomingList.get(mSelectedPosition).getCustomer_Name());
            mEmailText.setText("" + BookedTripsFragment.sUpcomingList.get(mSelectedPosition).getCustomer_Email());
            mPhoneText.setText("" + BookedTripsFragment.sUpcomingList.get(mSelectedPosition).getCustomer_Phone_Number());
            mPlacesText.setText("" + BookedTripsFragment.sUpcomingList.get(mSelectedPosition).getRegion_Name());

            String dateb_arr[]=("" + BookedTripsFragment.sUpcomingList.get(mSelectedPosition).getBooking_Date()).split("-");
            mBookingDateText.setText(dateb_arr[2]+"-"+dateb_arr[1]+"-"+dateb_arr[0]);

            String datea_arr[]=("" + BookedTripsFragment.sUpcomingList.get(mSelectedPosition).getTravelDate()).split("-");
            mArrDateText.setText(datea_arr[2]+"-"+datea_arr[1]+"-"+datea_arr[0]);

            mNoDaysText.setText(""+(Integer.parseInt("" + BookedTripsFragment.sUpcomingList.get(mSelectedPosition).getNo_of_Days())-1));

            mAdultsText.setText("" + BookedTripsFragment.sUpcomingList.get(mSelectedPosition).getNo_of_Adults());
            mChild512Text.setText("" + BookedTripsFragment.sUpcomingList.get(mSelectedPosition).getNo_of_Child());
            mChildBelow5Text.setText("" + BookedTripsFragment.sUpcomingList.get(mSelectedPosition).getNo_of_Infant());

            int total_price = Integer.parseInt("" + BookedTripsFragment.sUpcomingList.get(mSelectedPosition).getPackageValue()) +
                    Integer.parseInt("" + BookedTripsFragment.sUpcomingList.get(mSelectedPosition).getFlight_price())+
                    Integer.parseInt(""+ BookedTripsFragment.sUpcomingList.get(mSelectedPosition).getDiscount_value());

            mTotalPriceText.setText("Rs. " + total_price);

            mDiscountPriceText.setText("Rs. " + BookedTripsFragment.sUpcomingList.get(mSelectedPosition).getDiscount_value());

            int price_after_discount = total_price -
                    Integer.parseInt("" + BookedTripsFragment.sUpcomingList.get(mSelectedPosition).getDiscount_value());

            mRemainingDPriceText.setText("Rs ." + price_after_discount);

            int adv_amt = (Integer.parseInt("" + BookedTripsFragment.sUpcomingList.get(mSelectedPosition).getAdv_amt()) +
                    Integer.parseInt("" + BookedTripsFragment.sUpcomingList.get(mSelectedPosition).getInter_amt()) +
                    Integer.parseInt("" + BookedTripsFragment.sUpcomingList.get(mSelectedPosition).getFinal_amt()));

            int remaining_price = price_after_discount - adv_amt;

            mPriceAdvanceText.setText("Rs. " + adv_amt);
            mRemainingPriceText.setText("Rs. " + remaining_price);

            mTransportationText.setText("" + BookedTripsFragment.sUpcomingList.get(mSelectedPosition).getTransportation_Name());

        }
        else
        {
            mTravelIDText.setText("" + BookedTripsFragment.sPastList.get(mSelectedPosition).getItinerary_Main_Id());
            mNameText.setText("" + BookedTripsFragment.sPastList.get(mSelectedPosition).getCustomer_Name());
            mEmailText.setText("" + BookedTripsFragment.sPastList.get(mSelectedPosition).getCustomer_Email());
            mPhoneText.setText("" + BookedTripsFragment.sPastList.get(mSelectedPosition).getCustomer_Phone_Number());
            mPlacesText.setText("" + BookedTripsFragment.sPastList.get(mSelectedPosition).getRegion_Name());

            String dateb_arr[]=("" + BookedTripsFragment.sPastList.get(mSelectedPosition).getBooking_Date()).split("-");
            mBookingDateText.setText(dateb_arr[2]+"-"+dateb_arr[1]+"-"+dateb_arr[0]);

            String datea_arr[]=("" + BookedTripsFragment.sPastList.get(mSelectedPosition).getTravelDate()).split("-");
            mArrDateText.setText(datea_arr[2]+"-"+datea_arr[1]+"-"+datea_arr[0]);

            mNoDaysText.setText(""+(Integer.parseInt("" + BookedTripsFragment.sPastList.get(mSelectedPosition).getNo_of_Days())-1));

            mAdultsText.setText("" + BookedTripsFragment.sPastList.get(mSelectedPosition).getNo_of_Adults());
            mChild512Text.setText("" + BookedTripsFragment.sPastList.get(mSelectedPosition).getNo_of_Child());
            mChildBelow5Text.setText("" + BookedTripsFragment.sPastList.get(mSelectedPosition).getNo_of_Infant());

            int total_price = Integer.parseInt("" + BookedTripsFragment.sPastList.get(mSelectedPosition).getPackageValue()) +
                    Integer.parseInt("" + BookedTripsFragment.sPastList.get(mSelectedPosition).getFlight_price())
                    +Integer.parseInt(""+BookedTripsFragment.sPastList.get(mSelectedPosition).getDiscount_value());

            mTotalPriceText.setText("Rs. " + total_price);

            mDiscountPriceText.setText("Rs. " + BookedTripsFragment.sPastList.get(mSelectedPosition).getDiscount_value());

            int price_after_discount = total_price -
                    Integer.parseInt("" + BookedTripsFragment.sPastList.get(mSelectedPosition).getDiscount_value());

            mRemainingDPriceText.setText("Rs. " + price_after_discount);

            int adv_amt = (Integer.parseInt("" + BookedTripsFragment.sPastList.get(mSelectedPosition).getAdv_amt()) +
                    Integer.parseInt("" + BookedTripsFragment.sPastList.get(mSelectedPosition).getInter_amt()) +
                    Integer.parseInt("" + BookedTripsFragment.sPastList.get(mSelectedPosition).getFinal_amt()));

            int remaining_price = price_after_discount - adv_amt;

            mPriceAdvanceText.setText("Rs." + adv_amt);
            mRemainingPriceText.setText("Rs. " + remaining_price);

            mTransportationText.setText("" + BookedTripsFragment.sPastList.get(mSelectedPosition).getTransportation_Name());
        }

        hotelAdapter.notifyDataSetChanged();
        activityAdapter.notifyDataSetChanged();
    }




}