package com.itraveller.activity;

/**
 * Created by VNK on 8/15/2015.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.itraveller.R;
import com.itraveller.adapter.FlightAdapter;
import com.itraveller.adapter.FlightDomesticOnwardAdapter;
import com.itraveller.adapter.FlightDomesticReturnAdapter;
import com.itraveller.constant.Utility;
import com.itraveller.map.DirectionsJSONParser;
import com.itraveller.map.WorkaroundMapFragment;
import com.itraveller.volley.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


public class ItinerarySummaryActivity extends ActionBarActivity {
    /* When using Appcombat support library
       you need to extend Main Activity to ActionBarActivity.*/
    ProgressDialog ndDialog;
    TextView nameText,placesText,destinationText,arr_dateText,dep_dateText,daysText,adultsText,child_5_12_Text,child_below_5_Text;
    TextView nameSellerText,addressSellerText,arrAtText,dateDisplayText,roomDisplayText,totalPriceText;
    TextView discountPriceText,priceAdvanceText,remainingPriceText,departureText,transportationText;
    SharedPreferences prfs;
    SharedPreferences prefs;
    SharedPreferences preferences;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    String package_name_str;

    String destination_arr[];
    ScrollView sc;
    GoogleMap map;
    ArrayList<LatLng> markerPoints;


    private Toolbar mToolbar; // Declaring the Toolbar Object
    String onward_flight_rate = "";
    String return_flight_rate = "";
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


        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        //setupDrawer();

        TextView fromhome = (TextView) findViewById(R.id.from_home);
        TextView totravel = (TextView) findViewById(R.id.to_travel);
        TextView fromtravel = (TextView) findViewById(R.id.from_travel);
        TextView tohome = (TextView) findViewById(R.id.to_home);
        TextView transportationname = (TextView) findViewById(R.id.transportation);

        final SharedPreferences preferencess = getSharedPreferences("Preferences", MODE_PRIVATE);


        Button confirm = (Button) findViewById(R.id.to_payment);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // if (preferencess.getInt("flag", 0) == 1) {
                    Intent in = new Intent(ItinerarySummaryActivity.this, SummaryActivity.class);
                    startActivity(in);
                //} else {
                   /* LoginFragment_Before_Payment fragment = new LoginFragment_Before_Payment();
                    getSupportFragmentManager().beginTransaction()
                            .add(android.R.id.content, fragment).commit();

                }*/
            }
        });

        int TotalCountDays = 0;
        SharedPreferences prefs = getSharedPreferences("Itinerary", MODE_PRIVATE);
        fromhome.setText("" + prefs.getString("ArrivalAirportString", null));
        totravel.setText("" + prefs.getString("ArrivalPortString", null));
        fromtravel.setText("" + prefs.getString("DeparturePortString", null));
        tohome.setText("" + prefs.getString("DepartureAirportString", null));
        transportationname.setText("" + prefs.getString("TransportationName", null));
        String travel_date = "" + prefs.getString("TravelDate", null);


        // Initializing
        markerPoints = new ArrayList<LatLng>();

        // Getting reference to SupportMapFragment of the activity_main
        SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);

        // Getting Map for the SupportMapFragment
        map = fm.getMap();

        map = ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.getUiSettings().setZoomControlsEnabled(true);
        sc = (ScrollView) findViewById(R.id.scrollView2); //parent scrollview in xml, give your scrollview id value

        ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .setListener(new WorkaroundMapFragment.OnTouchListener() {
                    @Override
                    public void onTouch() {
                        sc.requestDisallowInterceptTouchEvent(true);
                    }
                });


        if(map!=null) {

            map.clear();

            System.gc();
            map.clear();
            // Enable MyLocation Button in the Map
            map.setMyLocationEnabled(true);

            String source = "" + prefs.getString("DeparturePortString", null);


            Log.d("Test finally 2", "" + source);

/*            String source_arr[];
            if(source.contains(" ")) {
                source_arr = source.split(" ")
                for(int i=0;i<source_arr.length;i++)
                source_str+=""+source_arr[i];
            }
*/
            source = source.replace(" ", "");
            //   Log.d("Test finally 4",""+source_str);
            String destination = "" + prefs.getString("DestinationName", null);
            Log.d("Test finally 3", "" + destination);




            if (destination.contains(",")) {
                String destination_arr_temp[] = destination.split(",");
                Log.d("Test finally 9",""+destination_arr_temp.length);
                destination_arr = new String[destination_arr_temp.length];
                destination_arr=destination.split(",");

            }else {
                destination_arr=new String[1];
                destination_arr[0] = "" + destination;

            }

            String dest;
            Log.d("Test finally 6",""+destination_arr.length);

            Log.d("Test finally 3",""+destination);
            String dest_arr_temp[];
            for(int i=0;i<destination_arr.length;i++) {
                if (!destination_arr[i].equals(null)) {
                    destination_arr[i] = destination_arr[i].replace(" ", "");
                    Log.d("Test finally 5", "" + destination_arr[i]);
                    package_name_str=destination_arr[i];
                }
                else
                {
                    break;
                }
                Log.d("Test finally 7",""+i);
            }

            // Getting URL to the Google Directions API

            String url="https://maps.googleapis.com/maps/api/directions/json?origin="+source+"&destination="+destination_arr[0];
            //String url ="https://maps.googleapis.com/maps/api/directions/json?origin=Bengaluru&destination=Mumbai";
            if(source.contains(" "))
                Log.d("Test finally","https://maps.googleapis.com/maps/api/directions/json?origin="+source+"&destination="+destination);

            DownloadTask downloadTask = new DownloadTask();

            // Start downloading json data from Google Directions API
            downloadTask.execute(url);

            if(destination_arr.length>1) {
                for (int var = 0; var < destination_arr.length - 1; var++) {
                    if(destination_arr[var].equals("Katra") )
                        destination_arr[var]=destination_arr[var].replace("Katra","KatraJammu");
                    if(destination_arr[var+1].equals("Katra"))
                        destination_arr[var+1]=destination_arr[var+1].replace("Katra","KatraJammu");
                    String url1 = "https://maps.googleapis.com/maps/api/directions/json?origin=" + destination_arr[var] + "&destination=" + destination_arr[var + 1];

                    Log.d("Test finally1", "https://maps.googleapis.com/maps/api/directions/json?origin=" + destination_arr[var] + "&destination=" + destination_arr[var+1]);
                    DownloadTask downloadTask2 = new DownloadTask();

                    downloadTask2.execute(url1);
                }
            }

            //        String url2="https://maps.googleapis.com/maps/api/directions/json?origin="+destination_arr[destination_arr.length-1]+"&destination="+source_arr[0];

            //        DownloadTask downloadTask3=new DownloadTask();
            //            downloadTask3.execute(url2);

        }

        //String HotelData = prefs.getString("HotelRooms",null);
        //String[] HotelDataArray = HotelData.trim().split("-");
        //Set<String> HotelData = prefs.getStringSet("HotelRooms", null);
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
                for (int j = 0; j < (Integer.parseInt(deatination_day_count[Mat_count]) + 1); j++) {
                    Mat_Destination.add(destination_name[Mat_count]);
                }
                Mat_count++;
            }
        }
        for (int i = 0; i < Mat_Destination.size(); i++) {
            Log.i("DestinationName_Mat", Mat_Destination.get(i));

        }

        String ActivitiesData = prefs.getString("ActivitiesDataString", null);
        String transportation_rate = prefs.getString("TransportationCost", null);
        LinearLayout main_lay = (LinearLayout) findViewById(R.id.main_layout);

        String[] HotelsArray = Hotels.trim().split("-");
        String[] ActivitiesDay = ActivitiesData.trim().split("/");

        String[] activities_val = new String[ActivitiesDay.length];
        for (int index = 0; index < ActivitiesDay.length; index++) {
            String activities_title = "";
            int count_bit = 0;
            String[] different_activities = ActivitiesDay[index].trim().split(":");
            for (int i = 0; i < different_activities.length; i++) {
                if (!different_activities[i].equalsIgnoreCase("")) {
                    String[] activities_ = different_activities[i].trim().split(",");
                    if (count_bit == 0) {
                        activities_title = activities_[2];
                        count_bit++;
                    } else {
                        activities_title = activities_title + ", " + activities_[2];
                    }
                }
            }
            activities_val[index] = "" + activities_title;
        }

        int count = 0;
        for (int i = 0; i < HotelsArray.length; i++) {

            for (int j = 0; j < Integer.parseInt(deatination_day_count[i]); j++) {
                String[] hotels_Data = HotelsArray[i].trim().split(",");

                View view = LayoutInflater.from(this).inflate(R.layout.summary_row, null);

                NetworkImageView imageView = (NetworkImageView) view.findViewById(R.id.thumbnail);

                try {
                    imageView.setImageUrl("http://stage.itraveller.com/backend/images/hotels/" + hotels_Data[2] + ".jpg", imageLoader);
                    TextView hotel_name = (TextView) view.findViewById(R.id.hotel_name);
                    TextView hotel_des = (TextView) view.findViewById(R.id.hotel_des);
                    TextView activities_title_txt = (TextView) view.findViewById(R.id.activities);
                    TextView place_name = (TextView) view.findViewById(R.id.place_name);
                    TextView day_date = (TextView) view.findViewById(R.id.date_day);
                    // Assigning value to  imageview and textview here
                    //if(HotelsArray.length != i) {
                    imageView.setImageUrl("http://stage.itraveller.com/backend/images/hotels/" + hotels_Data[2] + ".jpg", imageLoader);
                    hotel_name.setText(hotels_Data[0]);
                    hotel_des.setText(hotels_Data[1]);
                    //}
                    day_date.setText("Day " + (count + 1));
                    place_name.setText("(" + destination_name[i] + ", " + Utility.addDays(travel_date.toString(), count, "yyyy-MM-dd", "dd-MM-yyyy") + ")");
                    activities_title_txt.setText(activities_val[count]);
                    count++;
                    main_lay.addView(view);
                } catch (ArrayIndexOutOfBoundsException e) {

                } catch(Exception e){

                }

            }

        }

        View view = LayoutInflater.from(this).inflate(R.layout.summary_row, null);

        NetworkImageView imageView = (NetworkImageView) view.findViewById(R.id.thumbnail);
        TextView hotel_name = (TextView) view.findViewById(R.id.hotel_name);
        TextView hotel_des = (TextView) view.findViewById(R.id.hotel_des);
        TextView activities_title_txt = (TextView) view.findViewById(R.id.activities);
        TextView place_name = (TextView) view.findViewById(R.id.place_name);
        TextView day_date = (TextView) view.findViewById(R.id.date_day);
        // Assigning value to  imageview and textview here

        imageView.getLayoutParams().height = 80;
        hotel_name.setText("No Hotels");

        day_date.setText("Day " + (count + 1));
        place_name.setText("(" + destination_name[(HotelsArray.length - 1)] + ", " + Utility.addDays(travel_date.toString(), count, "yyyy-MM-dd", "dd-MM-yyyy") + ")");
        activities_title_txt.setText(activities_val[count]);
        count++;
        main_lay.addView(view);

        PackagePrice();
        ///////////////////////////////////////////////////////////////////////////////////
        //////////////////////////Itinerary Email JSON ///////////////////////////////////
       //Travel ID making///////////////////////
        //Mon Nov 16 17:12:28 GMT+05:30 2015
        Date date = new Date();

        Log.d("Date test",""+date.toString());
        String dateString = "Fri, 09 Nov 2012 23:40:18 GMT";
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy");
        try {
            date = dateFormat.parse(date.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long unixTime = (long)date.getTime()/1000;

        System.out.println("A_"+ new SimpleDateFormat("MMM").format(date.getMonth()) + unixTime);
        String travelId = "A_"+ new SimpleDateFormat("MMM").format(date.getMonth()) + unixTime;
        SharedPreferences pref = getSharedPreferences("Itinerary", Context.MODE_PRIVATE);
        try {
            JSONObject itinerary_obj = new JSONObject();

            itinerary_obj.put("email", "" + pref.getInt("vishak.kumar@itraveller.com", 0));
            itinerary_obj.put("phone", "" + pref.getString("TravelDate", null));
            itinerary_obj.put("itineraryId", "" + pref.getInt("ItineraryID", 0));
            itinerary_obj.put("travelId", travelId );
            itinerary_obj.put("dateOfTravel", "" + pref.getString("TravelDate",null));
            itinerary_obj.put("adult", "" + pref.getString("Adults", "0"));
            itinerary_obj.put("child-above-5", "" + pref.getString("Children_12_5", "0"));
            itinerary_obj.put("child-below-5", "" + pref.getString("Children_5_2", "0"));
            itinerary_obj.put("infant", "" + pref.getString("Children_2_0", "0"));
            itinerary_obj.put("endDate", "" + pref.getString("EndDate", "0"));
            itinerary_obj.put("regionId", pref.getString("RegionID", "0"));
            itinerary_obj.put("masterTransportation",  pref.getString("MasterID", "0"));
            itinerary_obj.put("selectedTransportation",  pref.getString("TransportationID", "0"));

            SharedPreferences packagePrice = getSharedPreferences("PackagePrice", MODE_PRIVATE);

            itinerary_obj.put("packageValue",  packagePrice.getInt("Packageprice", 0));
            itinerary_obj.put("flightPrice",  packagePrice.getInt("Flightprice", 0));
            itinerary_obj.put("serviceTax",  packagePrice.getInt("3.5", 0));
            itinerary_obj.put("discount",  packagePrice.getInt("", 0));
            itinerary_obj.put("advancePrice",  packagePrice.getInt("DiscountValue", 0));

            itinerary_obj.put("travellingFrom", pref.getString("ArrivalAirportString", "0"));
            itinerary_obj.put("arrivalPort", pref.getString("DepartureAirportString", "0"));
            itinerary_obj.put("departurePort", pref.getString("ArrivalPortString", "0"));
            itinerary_obj.put("travelTo", pref.getString("DeparturePortString", "0"));
            if(Integer.parseInt(pref.getString("FlightBit", "0")) == 0){

                itinerary_obj.put("flight", "International");}
            else{
                itinerary_obj.put("flight", "Domestic");
            }

            JSONArray destination_id = new JSONArray();
            JSONArray destination_name_arr = new JSONArray();
            JSONArray night_arr = new JSONArray();

            String[] des_id = pref.getString("DestinationID", "0").trim().split(",");
            String[] des_name = pref.getString("DestinationName", "0").trim().split(",");
            String[] des_count = pref.getString("DestinationCount", "0").trim().split(",");

            for(int i = 0 ; i <des_count.length ; i++){
                destination_id.put("" + des_id[i]);
                destination_name_arr.put("" + des_name[i]);
                night_arr.put("" + des_count[i]);
            }

            itinerary_obj.put("destination" , destination_id);
            itinerary_obj.put("destinations" , destination_name_arr);
            itinerary_obj.put("nights" , night_arr);

            //JSONArray hotel_date = new JSONArray();
            JSONObject hotel_date_obj = new JSONObject();

            pref.getString("Hotels", "0");
//            pref.getStringSet("HotelRooms", null);
            Log.i("HotelDetails", "" + pref.getString("ItineraryHotelRooms", "0"));
            String[] hotels = pref.getString("ItineraryHotelRooms", "0").trim().split("-");

/////////Hotel String contain HotelID, RoomID, Tariff, No of Rooms, Lunch Price and Dinner Price

            //DestinationDate
            String Dates[] = pref.getString("DestinationDate", "0").trim().split(",");

            for(int j = 0 ; j < hotels.length ; j++)
            {   Log.i("HotelDetailsSingle", "" + hotels[j]);

                String[] hotelsDetails = hotels[j].trim().split(",");
                JSONObject itinerary_hotel_obj = new JSONObject();
                itinerary_hotel_obj.put("Destination_Id",""+ des_id[j]);
                itinerary_hotel_obj.put("Rooms",hotelsDetails[3]);
                itinerary_hotel_obj.put("Breakfast","1");
                itinerary_hotel_obj.put("Lunch",hotelsDetails[4]);
                itinerary_hotel_obj.put("Dinner",hotelsDetails[5]);
                itinerary_hotel_obj.put("Nights",""+ des_count[j]);
                itinerary_hotel_obj.put("Hotel_Id",hotelsDetails[0]);
                itinerary_hotel_obj.put("Hotel_Room_Id",hotelsDetails[1]);
                itinerary_hotel_obj.put("Display_Tariff",hotelsDetails[2]);
                //hotel_date.put(itinerary_hotel_obj);
                hotel_date_obj.put( Dates[j],itinerary_hotel_obj);
                //hotel_date.put(hotel_date_obj);
            }
            itinerary_obj.put("hotels", hotel_date_obj);

            Log.i("ActivityDetails", "" + pref.getStringSet("ActivitiesData", null));
            Log.i("Activities", "" + pref.getString("ActivitiesDataString", "0"));
            int totalCount = Integer.parseInt(pref.getString("TotalNightCount", "0"));
            JSONArray activites_date = new JSONArray();
            JSONObject activites_date_obj = new JSONObject();

            String[] activities = pref.getString("ActivitiesDataString", "0").trim().split("/");

            for(int k=0;k<activities.length;k++){
                String[] activityDay = activities[k].trim().split(",");
                JSONObject itinerary_activites = new JSONObject();
                for(int l =0 ;l<2;l++){
                    itinerary_activites.put(activityDay[1],activityDay[1]);
                }
                activites_date_obj.put(Utility.addDays(pref.getString("TravelDate", null), k, "yyyy-MM-dd", "yyyy-MM-dd"),itinerary_activites);
                //activites_date.put(activites_date_obj);
            }
            itinerary_obj.put("activities", activites_date_obj);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        return false;
    }
    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            Log.d("URL first",""+url[0]);
            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> > {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }

            if(routes==null) {
                Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_SHORT).show();
                return Collections.emptyList();
            }
            else
                return routes;
        }
        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = new ArrayList<LatLng>();;
            PolylineOptions lineOptions = new PolylineOptions();

            MarkerOptions markerOptions = new MarkerOptions();
            ArrayList<LatLng> position=new ArrayList<>();
            //    LatLng position = null;
            // Traversing through all the routes
            Log.d("Map test3","hi"+result.size());

            for(int i=0;i<result.size();i++)
            {

                Log.d("Map test1","hi");
                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++)
                {

                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    //    position = new LatLng(lat, lng);

                    position.add(new LatLng(lat, lng));


//                    points.add(position.get(i));
                }

                points.addAll(position);
//                markerOptions.position(position);
//                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));


                map.addMarker(new MarkerOptions().position(position.get(0)).title("Source"));
                map.addMarker((new MarkerOptions().position(position.get(position.size()-1))).title("Destination"));


                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(5);
                lineOptions.color(Color.BLUE);
            }

            // Drawing polyline in the Google Map for the i-th route
//            map.addMarker(markerOptions);
            map.addPolyline(lineOptions);

            String lat_long=getLatitudeAndLongitude(package_name_str);
            String lat_long_arr[]=lat_long.split(" ");

            LatLng tmp=new LatLng(Double.parseDouble(lat_long_arr[0]), Double.parseDouble(lat_long_arr[1]));

            if(position.size()>0)
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(position.get(0),3));
            else
            {
                map.addMarker(new MarkerOptions().position(tmp).title("Source"));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(tmp, 3));

            }
            map.animateCamera(CameraUpdateFactory.zoomTo(8), 2000, null);


        }
    }

    public String getLatitudeAndLongitude(String searchedAddress){

        String lat_long="";

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        try
        {
            address = coder.getFromLocationName(searchedAddress,5);
            if (address == null) {
                Log.d("Map testing","############Address not correct #########");
            }
            Address location = address.get(0);

            Log.d("Map testing 12","Address Latitude : "+ location.getLatitude()+ "Address Longitude : "+ location.getLongitude());

            lat_long+=""+location.getLatitude()+" "+location.getLongitude();

            return lat_long;
        //     return true;

        }
        catch(Exception e)
        {
            Log.d("Map testing 11", "MY_ERROR : ############Address Not Found");
            return " ";
        //    return false;
        }
    }

    public void onBackPressed()
    {
        SharedPreferences preferences=getSharedPreferences("Preferences",MODE_PRIVATE);
        SharedPreferences prefs=getSharedPreferences("Itinerary",MODE_PRIVATE);

        Log.d("Skip bit testing",""+preferences.getInt("Skip_Flight_Bit",0));
        Log.d("Skip bit testing1",""+preferences.getInt("No_Flights",0));
        if(preferences.getInt("Skip_Flight_Bit",0)==1 && preferences.getInt("No_Flights",0)!=1)
        {
            Log.d("Flight Bit testing",""+prefs.getString("FlightBit",null).equals("1"));
            if((""+prefs.getString("FlightBit",null)).equals("1"))
            {

                FlightDomesticOnwardAdapter.sBaseFlightPrice=0;
                FlightDomesticReturnAdapter.sBaseFlightPrice=0;
                FlightDomesticOnwardAdapter.sCount=0;
                FlightDomesticReturnAdapter.sCount=0;

                FlightDomesticActivity.fda.finish();
            }
            else
            {
                FlightAdapter.base_flight_price=0;
                FlightAdapter.count=0;

                FlightActivity.fa.finish();
            }
        }
        finish();
    }

    public void PackagePrice()
    {
        int total_price=0;
        SharedPreferences prefs = getSharedPreferences("Itinerary", MODE_PRIVATE);

        //Set<String> HotelData = prefs.getStringSet("HotelRooms", null);
        Set<String> ActivitiesData = prefs.getStringSet("ActivitiesData", null);
        String transportation_rate = prefs.getString("TransportationCost", null);


        String F_bit = ""+prefs.getString("FlightBit",null);
        int flightBit = Integer.parseInt("" + F_bit);
        if(flightBit == 0)
        {
            String flight_dom = prefs.getString("FlightPrice", "0");
            if(!flight_dom.equals("0"))
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

        String HotelData = prefs.getString("HotelRooms",null);
        String[] HotelDataArray = HotelData.trim().split("-");
        //String[] HotelDataArray = HotelData.toArray(new String[HotelData.size()]);
        String[] ActivitiesDataArray = ActivitiesData.toArray(new String[ActivitiesData.size()]);

        String DayCount = prefs.getString("DestinationCount", null);
        String[] destination_day_count = DayCount.trim().split(",");
        int rate_of_rooms =0;
        for (int index = 0; index < HotelDataArray.length; index++) {   //Log.i("Hoteldataaaaaa",""+ HotelDataArray[index]);
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
        for (int index = 0; index < ActivitiesDataArray.length; index++) {   //Log.i("Hoteldataaaaaa",""+ HotelDataArray[index]);


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

        // Log.i("ActvitiesRates","" +activities_rate);
        //Log.i("TransportationRates","" +transportation_rate);


        if(flight_rate == 0) {
            if (("" + transportation_rate).equals(null)) {
                transportation_rate="0";
                total_price = rate_of_rooms + activities_rate + Integer.parseInt("" + transportation_rate);

            }
        }
        else{
            if (("" + transportation_rate).equals(null))
            {
                transportation_rate = "0";
                total_price = rate_of_rooms + activities_rate + Integer.parseInt("" + transportation_rate) + Integer.parseInt("" + flight_rate);
            }
        }
        SharedPreferences packagePrice = getSharedPreferences("PackagePrice", MODE_PRIVATE);
        SharedPreferences.Editor editor = packagePrice.edit();
        editor.putInt("Packageprice", total_price);
        editor.putInt("Flightprice", Integer.parseInt(""+flight_rate));
        double discount_val = 0.2;
        Double total_discount = Double.parseDouble("" + total_price) * discount_val ;
        editor.putInt("DiscountValue", total_discount.intValue());
        editor.commit();
    }
}