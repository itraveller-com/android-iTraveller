package com.itraveller.activity;

/**
 * Created by VNK on 8/15/2015.
 */
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.JsonWriter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import static com.itraveller.R.id.destination_name;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class ItinerarySummaryActivity extends ActionBarActivity {

    private Toolbar mToolbar; // Declaring the Toolbar Object
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    GoogleMap map;
    ArrayList<LatLng> markerPoints;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Itinerary Summary");

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
        fromhome.setText("" + prefs.getString("ArrivalAirportString", null));
        Log.d("From home", "" + prefs.getString("ArrivalAirportString", null));
        totravel.setText("" + prefs.getString("DeparturePortString", null));
        Log.d("To travel", "" + prefs.getString("DeparturePortString", null));
        fromtravel.setText("" + prefs.getString("ArrivalPortString", null));
        Log.d("From travel",""+prefs.getString("ArrivalPortString", null));
        tohome.setText(""+prefs.getString("DepartureAirportString", null));
        Log.d("To home",""+prefs.getString("DepartureAirportString", null));
        transportationname.setText(""+prefs.getString("TransportationName", null));
        String travel_date = ""+prefs.getString("TravelDate", null);

        // Initializing
        markerPoints = new ArrayList<LatLng>();

        // Getting reference to SupportMapFragment of the activity_main
        SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);

        // Getting Map for the SupportMapFragment
        map = fm.getMap();




        if(map!=null){

            // Enable MyLocation Button in the Map
            map.setMyLocationEnabled(true);

            String source=""+prefs.getString("DeparturePortString", null);
            String source_arr[]=source.split(" ");
            String destination=""+prefs.getString("DestinationName",null);
            String destination_arr[]=destination.split(",");
            String dest;

            String dest_arr_temp[];
            for(int i=0;i<destination_arr.length;i++)
            {
                if(destination_arr[i].contains(" "))
                {
                    dest_arr_temp=destination_arr[i].split(" ");
                    destination_arr[i]=""+dest_arr_temp[0]+""+dest_arr_temp[1];

                }

            }
            map.clear();

            // Getting URL to the Google Directions API
            String url="https://maps.googleapis.com/maps/api/directions/json?origin="+source_arr[0]+"&destination="+destination_arr[0];
            //String url ="https://maps.googleapis.com/maps/api/directions/json?origin=Bengaluru&destination=Mumbai";
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


        String Hotels = prefs.getString("Hotels", null);
        String DayCount = prefs.getString("DestinationCount", null);
        String[] destination_day_count = DayCount.trim().split(",");
       // Array
        int destination_day_count_length=destination_day_count.length;
        for (int x = 0; x < destination_day_count_length; x++) {
            TotalCountDays = TotalCountDays + Integer.parseInt(destination_day_count[x]);
        }

        String DestinationName = prefs.getString("DestinationName", null);
        String[] destination_name = DestinationName.trim().split(",");
        ArrayList<String> Mat_Destination = new ArrayList<String>();

        String Arrival_port = prefs.getString("ArrivalPort", null);
        //Log.i("Hoteldataaaaaa","AP"+ Arrival_port);
        String Departure_port = prefs.getString("DeparturePort", null);

        int Mat_count = 0;
        int destination_name_length=destination_name.length;
        for (int index = 0; index < (destination_name_length + 2); index++) {
            //Log.i("DestinationMat",deatination_day_count[index]);
            if (index == 0) {
                Mat_Destination.add(Arrival_port);
            } else if (index == (destination_name.length + 1)) {
                Mat_Destination.add(Departure_port);
            } else {
                for(int j = 0; j< (Integer.parseInt(destination_day_count[Mat_count]) + 1); j++) {
                    Mat_Destination.add(destination_name[Mat_count]);
                }
                Mat_count++;
            }
        }
        int Mat_Destination_size=Mat_Destination.size();
        for(int i = 0 ; i < Mat_Destination_size; i++){
            Log.i("DestinationName_Mat", Mat_Destination.get(i));

        }

        String ActivitiesData = prefs.getString("ActivitiesDataString", null);
        LinearLayout main_lay = (LinearLayout) findViewById(R.id.main_layout);

        String[] HotelsArray = Hotels.trim().split("-");
        String[] ActivitiesDay = ActivitiesData.trim().split("/");

        String[] activities_val = new String[ActivitiesDay.length];
        int ActivitiesDay_length=ActivitiesDay.length;
        for(int index=0;index <ActivitiesDay_length; index++){
            String activities_title = "";
            int count_bit = 0;
            String[] different_activities = ActivitiesDay[index].trim().split(":");
            int different_activities_length=different_activities.length;
            for(int i = 0; i< different_activities_length;i++){
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
        int HotelsArray_length=HotelsArray.length;
        for (int i = 0; i < HotelsArray_length; i++) {

                for (int j = 0; j < Integer.parseInt(destination_day_count[i]); j++) {
                    String[] hotels_Data = HotelsArray[i].trim().split(",");

                    Log.d("Hotel Array Length",""+hotels_Data.length);
                    Log.d("Hotel Array data0",""+hotels_Data[0]);
                    View view = LayoutInflater.from(this).inflate(R.layout.summary_row, null);


                    NetworkImageView imageView = (NetworkImageView) view.findViewById(R.id.thumbnail);

                    if(hotels_Data.length>2) {
                        imageView.setImageUrl(Constants.API_ItinerarySummaryActivity_ImageURL + hotels_Data[2] + ".jpg", imageLoader);
                        TextView hotel_name = (TextView) view.findViewById(R.id.hotel_name);
                        TextView hotel_des = (TextView) view.findViewById(R.id.hotel_des);
                        TextView activities_title_txt = (TextView) view.findViewById(R.id.activities);
                        TextView place_name = (TextView) view.findViewById(R.id.place_name);
                        TextView day_date = (TextView) view.findViewById(R.id.date_day);
                        // Assigning value to  imageview and textview here
                        hotel_name.setText(hotels_Data[0]);
                        hotel_des.setText(hotels_Data[1]);

                        day_date.setText("Day " + (count + 1));
                        place_name.setText("(" + destination_name[i] + ", " + Utility.addDays(travel_date.toString(), count, "yyyy-MM-dd", "dd-MM-yyyy") + ")");
                        activities_title_txt.setText(activities_val[count]);
                        count++;
                    }
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
            Log.d("Exception while downloading url", e.toString());
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
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

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
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            ArrayList<LatLng> position=new ArrayList<>();
            //    LatLng position = null;
            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
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
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(position.get(0),3));
            map.animateCamera(CameraUpdateFactory.zoomTo(8), 2000, null);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}

