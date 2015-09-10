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
import com.itraveller.constant.Utility;
import com.itraveller.volley.AppController;

import static com.itraveller.R.id.LinearLayout1;
import static com.itraveller.R.id.btn_confirm_payment;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;


public class ItinerarySummaryActivity extends ActionBarActivity {
/* When using Appcombat support library
   you need to extend Main Activity to ActionBarActivity.*/

    GoogleMap map;
    ArrayList<LatLng> markerPoints;


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

        final SharedPreferences preferencess = getSharedPreferences("Preferences", MODE_PRIVATE);

        Button confirm = (Button) findViewById(R.id.to_payment);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(preferencess.getInt("flag",0)==1) {
                    Intent in = new Intent(ItinerarySummaryActivity.this, SummaryActivity.class);
                    startActivity(in);
                }
                else
                {
                    LoginFragment_Before_Payment fragment=new LoginFragment_Before_Payment();
                    getSupportFragmentManager().beginTransaction()
                            .add(android.R.id.content,fragment).commit();

                }
            }
        });


        int TotalCountDays = 0;
        SharedPreferences prefs = getSharedPreferences("Itinerary", MODE_PRIVATE);
        fromhome.setText("" + prefs.getString("ArrivalAirportString", null));
        totravel.setText("" + prefs.getString("DeparturePortString", null));
        fromtravel.setText("" + prefs.getString("ArrivalPortString", null));
        tohome.setText("" + prefs.getString("DepartureAirportString", null));
        transportationname.setText("" + prefs.getString("TransportationName", null));
        String travel_date = "" + prefs.getString("TravelDate", null);

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

                if (hotels_Data.length > 2) {
                    imageView.setImageUrl("http://stage.itraveller.com/backend/images/hotels/" + hotels_Data[2] + ".jpg", imageLoader);
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

