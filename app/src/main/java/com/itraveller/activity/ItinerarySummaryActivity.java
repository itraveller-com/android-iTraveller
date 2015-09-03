package com.itraveller.activity;

/**
 * Created by VNK on 8/15/2015.
 */
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.JsonWriter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Set;

import com.itraveller.R;
import com.itraveller.constant.Constants;
import com.itraveller.constant.Utility;
import com.itraveller.volley.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import static com.itraveller.R.id.LinearLayout1;
import static com.itraveller.R.id.btn_confirm_payment;


public class ItinerarySummaryActivity extends ActionBarActivity {
/* When using Appcombat support library
   you need to extend Main Activity to ActionBarActivity.*/

    private Toolbar mToolbar; // Declaring the Toolbar Object
    String onward_flight_rate="";
    String return_flight_rate="";
    int flight_rate = 0;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Itinerary Summary");

        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView fromhome = (TextView) findViewById(R.id.from_home);
        TextView totravel = (TextView) findViewById(R.id.to_travel);
        TextView fromtravel = (TextView) findViewById(R.id.from_travel);
        TextView tohome = (TextView) findViewById(R.id.to_home);
        TextView transportationname = (TextView) findViewById(R.id.transportation);

        Button confirm = (Button) findViewById(R.id.to_payment);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ItinerarySummaryActivity.this, SummaryActivity.class);
                startActivity(in);
            }
        });


        int TotalCountDays = 0;
        SharedPreferences prefs = getSharedPreferences("Itinerary", MODE_PRIVATE);
        fromhome.setText(""+prefs.getString("ArrivalAirportString", null));
        totravel.setText(""+prefs.getString("DeparturePortString", null));
        fromtravel.setText(""+prefs.getString("ArrivalPortString", null));
        tohome.setText(""+prefs.getString("DepartureAirportString", null));
        transportationname.setText(""+prefs.getString("TransportationName", null));
        String travel_date = ""+prefs.getString("TravelDate", null);

        Set<String> HotelData = prefs.getStringSet("HotelRooms", null);
        String Hotels = prefs.getString("Hotels", null);
        String DayCount = prefs.getString("DestinationCount", null);
        String[] deatination_day_count = DayCount.trim().split(",");
       // Array
        for (int x = 0; x < deatination_day_count.length; x++) {
            TotalCountDays = TotalCountDays + Integer.parseInt(deatination_day_count[x]);
        }

        String DestinationName = prefs.getString("DestinationName", null);
        String[] destination_name = DestinationName.trim().split(",");
        ArrayList<String> Mat_Destination = new ArrayList<String>();

        String Arrival_port = prefs.getString("ArrivalPort", null);
        //Log.i("Hoteldataaaaaa","AP"+ Arrival_port);
        String Departure_port = prefs.getString("DeparturePort", null);

        int Mat_count = 0;
        for (int index = 0; index < (destination_name.length + 2); index++) {
            //Log.i("DestinationMat",deatination_day_count[index]);
            if (index == 0) {
                Mat_Destination.add(Arrival_port);
            } else if (index == (destination_name.length + 1)) {
                Mat_Destination.add(Departure_port);
            } else {
                for(int j = 0; j< (Integer.parseInt(deatination_day_count[Mat_count]) + 1); j++) {
                    Mat_Destination.add(destination_name[Mat_count]);
                }
                Mat_count++;
            }
        }
        for(int i = 0 ; i < Mat_Destination.size(); i++){
            Log.i("DestinationName_Mat", Mat_Destination.get(i));

        }

        String ActivitiesData = prefs.getString("ActivitiesDataString", null);
        String transportation_rate = prefs.getString("TransportationCost", null);
        LinearLayout main_lay = (LinearLayout) findViewById(R.id.main_layout);

        String[] HotelsArray = Hotels.trim().split("-");
        String[] ActivitiesDay = ActivitiesData.trim().split("/");

        String[] activities_val = new String[ActivitiesDay.length];
        for(int index=0;index <ActivitiesDay.length; index++){
            String activities_title = "";
            int count_bit = 0;
            String[] different_activities = ActivitiesDay[index].trim().split(":");
            for(int i = 0; i< different_activities.length ;i++){
                if(!different_activities[i].equalsIgnoreCase("")) {
                    String[] activities_ = different_activities[i].trim().split(",");
                    if (count_bit == 0) {
                        activities_title = activities_[2];
                        count_bit++;
                    } else {
                        activities_title = activities_title + ", " + activities_[2];
                    }
                }
            }
            activities_val[index] = ""+activities_title;
        }

        int count = 0;
        for (int i = 0; i < HotelsArray.length; i++) {

                for (int j = 0; j < Integer.parseInt(deatination_day_count[i]); j++) {
                    String[] hotels_Data = HotelsArray[i].trim().split(",");

                    View view = LayoutInflater.from(this).inflate(R.layout.summary_row, null);



                    NetworkImageView imageView = (NetworkImageView) view.findViewById(R.id.thumbnail);
                    imageView.setImageUrl(Constants.API_ItinerarySummaryActivity_ImageURL+hotels_Data[2]+ ".jpg", imageLoader);
                    TextView hotel_name = (TextView) view.findViewById(R.id.hotel_name);
                    TextView hotel_des = (TextView) view.findViewById(R.id.hotel_des);
                    TextView activities_title_txt = (TextView) view.findViewById(R.id.activities);
                    TextView place_name = (TextView) view.findViewById(R.id.place_name);
                    TextView day_date = (TextView) view.findViewById(R.id.date_day);
                    // Assigning value to  imageview and textview here
                    hotel_name.setText(hotels_Data[0]);
                    hotel_des.setText(hotels_Data[1]);
                    day_date.setText("Day " + (count + 1) );
                    place_name.setText("(" + destination_name[i] + ", " + Utility.addDays(travel_date.toString(), count, "yyyy-MM-dd", "dd-MM-yyyy") + ")");
                    activities_title_txt.setText(activities_val[count]);
                    count++;
                    main_lay.addView(view);

                }

        }

        ///////////////////////////////////////////////////////////////////////////////////
        //////////////////////////Itinerary Email JSON ///////////////////////////////////
        try {
            JSONObject itinerary_obj = new JSONObject();
            itinerary_obj.put("itineraryId", "2274");
            itinerary_obj.put("dateOfTravel", "06-07-2015");
            itinerary_obj.put("adult", 2);
            itinerary_obj.put("child-above-5", 0);
            itinerary_obj.put("child-below-5", 0);
            itinerary_obj.put("infant", 0);
            itinerary_obj.put("endDate", "08-07-2015");
            itinerary_obj.put("regionId", "7,13,14");
            itinerary_obj.put("masterTransportation", 50);
            itinerary_obj.put("selectedTransportation", 68);
            itinerary_obj.put("travellingFrom", "MUMBAI");
            itinerary_obj.put("arrivalPort", "Cochin International Airport");
            itinerary_obj.put("departurePort", "Cochin International Airport");
            itinerary_obj.put("travelTo", "MUMBAI");
            itinerary_obj.put("flight", "International");

            JSONArray destination_id = new JSONArray();
            JSONArray destination_name_arr = new JSONArray();
            JSONArray night_arr = new JSONArray();

            for(int i = 0 ; i <3 ; i++){
                destination_id.put("101");
                destination_name_arr.put("Alleppey-Houseboats");
                night_arr.put("2");
            }

            itinerary_obj.put("destination" , destination_id);
            itinerary_obj.put("destinations" , destination_name_arr);
            itinerary_obj.put("nights" , night_arr);

            JSONObject itinerary_hotel_obj = new JSONObject();
            JSONArray hotel_date = new JSONArray();
            JSONObject hotel_date_obj = new JSONObject();

            for(int j = 0 ; j < 3 ; j++)
            {
                itinerary_hotel_obj.put("Destination_Id","101");
                itinerary_hotel_obj.put("Rooms","1");
                itinerary_hotel_obj.put("Breakfast","1");
                itinerary_hotel_obj.put("Lunch","0");
                itinerary_hotel_obj.put("Dinner","0");
                itinerary_hotel_obj.put("Nights","1");
                itinerary_hotel_obj.put("Hotel_Id","387");
                itinerary_hotel_obj.put("Hotel_Room_Id","1122");
                itinerary_hotel_obj.put("Display_Tariff","9310");
                //hotel_date.put(itinerary_hotel_obj);
                hotel_date_obj.put("10-09-2015",itinerary_hotel_obj);
                hotel_date.put(hotel_date_obj);
            }
            itinerary_obj.put("hotels", hotel_date);


            JSONArray activites_date = new JSONArray();
            JSONObject activites_date_obj = new JSONObject();

            for(int k=0;k<3;k++){
                JSONArray itinerary_activites = new JSONArray();
                for(int l =0 ;l<2;l++){
                    itinerary_activites.put("1254");
                }
                activites_date_obj.put("10-09-2015",itinerary_activites);
                activites_date.put(activites_date_obj);
            }
            itinerary_obj.put("activities", activites_date);
//////////////////////////////////////////////////////////////////////////////////////////////
            ///////////Domestic Flight///////////////
            int x = 1;
            if(x==1) {
                JSONObject flight = new JSONObject();
                JSONObject flights = new JSONObject();
                flight.put("ActualBaseFare", "");
                flight.put("Tax", "");
                flight.put("STax", "");
                flight.put("TCharge", "");
                flight.put("SCharge", "");
                flight.put("TDiscount", "");
                flight.put("TMarkup", "");
                flight.put("TPartnerCommission", "");
                flight.put("TSdiscount", "");
                flight.put("ocTax", "");
                flight.put("id", "");
                flight.put("key", "");
                flight.put("AirEquipType", "");
                flight.put("ArrivalAirportCode", "");
                flight.put("ArrivalAirportName", "");
                flight.put("ArrivalDateTime", "");
                flight.put("DepartureAirportCode", "");
                flight.put("DepartureAirportName", "");
                flight.put("DepartureDateTime", "");
                flight.put("FlightNumber", "");
                flight.put("AirLineName", "");

                flights.put("onward", flight);

                JSONObject flight_ = new JSONObject();
                flight_.put("ActualBaseFare", "");
                flight_.put("Tax", "");
                flight_.put("STax", "");
                flight_.put("TCharge", "");
                flight_.put("SCharge", "");
                flight_.put("TDiscount", "");
                flight_.put("TMarkup", "");
                flight_.put("TPartnerCommission", "");
                flight_.put("TSdiscount", "");
                flight_.put("ocTax", "");
                flight_.put("id", "");
                flight_.put("key", "");
                flight_.put("AirEquipType", "");
                flight_.put("ArrivalAirportCode", "");
                flight_.put("ArrivalAirportName", "");
                flight_.put("ArrivalDateTime", "");
                flight_.put("DepartureAirportCode", "");
                flight_.put("DepartureAirportName", "");
                flight_.put("DepartureDateTime", "");
                flight_.put("FlightNumber", "");
                flight_.put("AirLineName", "");

                flights.put("return", flight_);

                //JSONArray flight_value = new JSONArray();
                //flight_value.put(flight_onward);
                //flight_value.put(flight_return);
                itinerary_obj.put("flight", flights);
            }
            else{
                JSONObject flight = new JSONObject();
                flight.put("ActualBaseFare","");
                flight.put("Tax","");
                flight.put("STax","");
                flight.put("TCharge","");
                flight.put("SCharge","");
                flight.put("TDiscount","");
                flight.put("TMarkup","");
                flight.put("TPartnerCommission","");
                flight.put("TSdiscount","");
                flight.put("ocTax","");
                flight.put("id","");
                flight.put("key", "");
                JSONArray flight_onward_arr =new JSONArray();
                for(int i=0 ; i<2; i++){
                 JSONObject flightonward = new JSONObject();
                    flightonward.put("AirEquipType","");
                    flightonward.put("ArrivalAirportCode","");
                    flightonward.put("ArrivalAirportName","");
                    flightonward.put("ArrivalDateTime","");
                    flightonward.put("DepartureAirportCode","");
                    flightonward.put("DepartureAirportName","");
                    flightonward.put("DepartureDateTime","");
                    flightonward.put("FlightNumber","");
                    flightonward.put("MarketingAirlineCode","");
                    flightonward.put("OperatingAirlineCode","");
                    flightonward.put("OperatingAirlineName","");
                    flightonward.put("OperatingAirlineFlightNumber","");
                    flightonward.put("NumStops","");
                    flightonward.put("LinkSellAgrmnt","");
                    flightonward.put("Conx","");
                    flightonward.put("AirpChg","");
                    flightonward.put("InsideAvailOption","");
                    flightonward.put("GenTrafRestriction","");
                    flightonward.put("DaysOperates","");
                    flightonward.put("JrnyTm","");
                    flightonward.put("EndDt","");
                    flightonward.put("StartTerminal","");
                    flightonward.put("EndTerminal","");
                    flight_onward_arr.put(flightonward);
                    }
                flight.put("onward",flight_onward_arr);
                JSONArray flight_return_arr =new JSONArray();
                for(int j=0 ; j<2; j++){
                    JSONObject flightreturn = new JSONObject();
                    flightreturn.put("AirEquipType","");
                    flightreturn.put("ArrivalAirportCode","");
                    flightreturn.put("ArrivalAirportName","");
                    flightreturn.put("ArrivalDateTime","");
                    flightreturn.put("DepartureAirportCode","");
                    flightreturn.put("DepartureAirportName","");
                    flightreturn.put("DepartureDateTime","");
                    flightreturn.put("FlightNumber","");
                    flightreturn.put("MarketingAirlineCode","");
                    flightreturn.put("OperatingAirlineCode","");
                    flightreturn.put("OperatingAirlineName","");
                    flightreturn.put("OperatingAirlineFlightNumber","");
                    flightreturn.put("NumStops","");
                    flightreturn.put("LinkSellAgrmnt","");
                    flightreturn.put("Conx","");
                    flightreturn.put("AirpChg","");
                    flightreturn.put("InsideAvailOption","");
                    flightreturn.put("GenTrafRestriction","");
                    flightreturn.put("DaysOperates","");
                    flightreturn.put("JrnyTm","");
                    flightreturn.put("EndDt","");
                    flightreturn.put("StartTerminal","");
                    flightreturn.put("EndTerminal","");
                    flight_return_arr.put(flightreturn);
                }
                flight.put("return",flight_return_arr);

                itinerary_obj.put("flight", flight);
            }


            Log.i("JSON Itinerary", "" + itinerary_obj.toString());

        }
        catch (JSONException e){
            System.out.println("JSON Exception " + e);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}

