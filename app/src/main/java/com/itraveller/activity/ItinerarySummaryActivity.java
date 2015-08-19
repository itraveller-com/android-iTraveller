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
import com.itraveller.constant.Utility;
import com.itraveller.volley.AppController;

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
        String[] ActivitiesDataArray = ActivitiesData.trim().split("/");

        String[] activities_val = new String[ActivitiesDataArray.length];
        for(int index=0;index <ActivitiesDataArray.length; index++){
            String activities_title = "";
                int count_bit = 0;
                String[] activities_Data = ActivitiesDataArray[index].trim().split("-");
                for (int j = 0; j < activities_Data.length; j++) {
                    String[] activites_data_value = activities_Data[j].trim().split(",");
                    //if(!activites_data_value.toString().equalsIgnoreCase("null")) {
                    if(!activites_data_value[0].startsWith("No Activities")){
                        if (count_bit == 0) {
                            activities_title = activites_data_value[2];
                            count_bit++;
                        } else {
                            activities_title = activities_title + ", " + activites_data_value[2];
                        }
                    }
                    else
                    {
                        activities_title = activites_data_value[0];
                    }
                }
                activities_val[index] = ""+activities_title;
                Log.i("Actvities_Titles",activities_title);
        }

        int count = 0;
        for (int i = 0; i < HotelsArray.length; i++) {

                for (int j = 0; j < Integer.parseInt(deatination_day_count[i]); j++) {
                    String[] hotels_Data = HotelsArray[i].trim().split(",");

                    View view = LayoutInflater.from(this).inflate(R.layout.summary_row, null);

                    NetworkImageView imageView = (NetworkImageView) view.findViewById(R.id.thumbnail);
                    imageView.setImageUrl("http://stage.itraveller.com/backend/images/hotels/" + hotels_Data[2] + ".jpg", imageLoader);
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

