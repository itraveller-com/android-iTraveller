package com.itraveller.activity;

/**
 * Created by VNK on 6/11/2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Set;

import com.itraveller.R;

import static com.itraveller.R.id.btn_confirm_payment;


public class SummaryActivity extends ActionBarActivity {

    private Toolbar mToolbar; // Declaring the Toolbar Object
    String onward_flight_rate="";
    String return_flight_rate="";
    int flight_rate = 0;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.payment_billing);
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle("Price Summary");

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            Button confirm = (Button) findViewById(R.id.btn_confirm_payment);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Intent in = new Intent(SummaryActivity.this, ItinerarySummaryActivity.class);
                    //startActivity(in);
                }
            });
            SharedPreferences prefs = getSharedPreferences("Itinerary", MODE_PRIVATE);
            Set<String> HotelData = prefs.getStringSet("HotelRooms", null);
            Set<String> ActivitiesData = prefs.getStringSet("ActivitiesData", null);
            String transportation_rate = prefs.getString("TransportationCost", null);


            String F_bit = ""+prefs.getString("FlightBit",null);
            int flightBit = Integer.parseInt("" + F_bit);
            if(flightBit == 0)
            {
                String flight_dom = prefs.getString("FlightPrice", null);
                if(!flight_dom.equalsIgnoreCase(""))
                flight_rate = Integer.parseInt(flight_dom);
            }
            else{
                onward_flight_rate = prefs.getString("OnwardFlightPrice", null);
                return_flight_rate = prefs.getString("ReturnFlightPrice", null);


                if(!((onward_flight_rate.equals("0"))&&(return_flight_rate.equals("0")))){
                flight_rate = Integer.parseInt(onward_flight_rate) + Integer.parseInt(return_flight_rate);
                }
                else if(!onward_flight_rate.equals("0"))
                    flight_rate = Integer.parseInt(onward_flight_rate);
                else if(!return_flight_rate.equals("0"))
                    flight_rate = Integer.parseInt(return_flight_rate);


            }

            String[] HotelDataArray = HotelData.toArray(new String[HotelData.size()]);
            String[] ActivitiesDataArray = ActivitiesData.toArray(new String[ActivitiesData.size()]);

            String DayCount = prefs.getString("DestinationCount", null);
            String[] destination_day_count = DayCount.trim().split(",");
            int rate_of_rooms =0;

            int HotelDataArray_length=HotelDataArray.length;
            for (int index = 0; index < HotelDataArray_length; index++) {   //Log.i("Hoteldataaaaaa",""+ HotelDataArray[index]);
                String[] hotel_room_Data = HotelDataArray[index].trim().split(",");
                //no fo rooms and price
                int no_room_price = Integer.parseInt("" + hotel_room_Data[3]) * Integer.parseInt("" + hotel_room_Data[2]);
                int room_rate = Integer.parseInt("" + destination_day_count[index]) * no_room_price;
                if(index == 0)
                {
                    rate_of_rooms = room_rate;
                }
                else{
                    rate_of_rooms = rate_of_rooms + room_rate;
                }
                Log.i("RoomRates","" +rate_of_rooms);
            }


            int activities_rate =0;
            int count_bit= 0;
            int ActivitiesDataArray_length=ActivitiesDataArray.length;
            for (int index = 0; index < ActivitiesDataArray_length; index++) {   //Log.i("Hoteldataaaaaa",""+ HotelDataArray[index]);


                if(!ActivitiesDataArray[index].toString().equalsIgnoreCase("null")) {

                    String[] activities_Data = ActivitiesDataArray[index].trim().split(",");
                    Log.i("ActivityData","" +activities_Data);
                    try{
                    if (count_bit == 0) {
                        activities_rate = Integer.parseInt("" + activities_Data[1]);
                        count_bit ++;
                    } else {
                        activities_rate = activities_rate + Integer.parseInt("" + activities_Data[1]);
                    }}
                    catch(Exception e){

                    }
                }
            }
            Log.i("ActvitiesRates","" +activities_rate);
            Log.i("TransportationRates","" +transportation_rate);
            int total_price = 0;

            if(flight_rate == 0)
            {
                total_price = rate_of_rooms + activities_rate + Integer.parseInt("" +transportation_rate);
            }
            else{
                 total_price = rate_of_rooms + activities_rate + Integer.parseInt("" +transportation_rate) + Integer.parseInt("" +flight_rate);
            }

            double discount_val = 0.2;
            Double total_discount = Double.parseDouble("" + total_price) * discount_val ;
            TextView package_v = (TextView) findViewById(R.id.price_txt);
            TextView total = (TextView) findViewById(R.id.total_p_txt);
            TextView total_dis = (TextView) findViewById(R.id.booking_price_txt);
            package_v.setText("Rs " + total_price);
            total.setText("Rs " + total_price);
            total_dis.setText("Rs " + total_discount.intValue());
        }


        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            return super.onOptionsItemSelected(item);
        }
    }

