package com.itraveller.activity;

/*
 * Created by VNK on 6/25/2015.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.itraveller.R;
import com.itraveller.adapter.ListViewPagerActivitiesAdapter;
import com.itraveller.constant.Constants;
import com.itraveller.constant.Utility;
import com.itraveller.model.ActivitiesModel;


public class ActivitiesActivity extends ActionBarActivity {

    // Declare Variable
    public ListViewPagerActivitiesAdapter listViewPagerAdapter;
    private ArrayList<String> activitiesList;
    int[] day = new int[10];
    ListView lv1;
    String[] destination_id, destination_day_count, hotel_id_data;
    int TotalCountDays = 0;
    String[] ActivityData;
    private Toolbar mToolbar;
    Button proceed_activity;

    ArrayList<String> Mat_Destination_ID = new ArrayList<String>();
    ArrayList<String> Mat_Destination_Count = new ArrayList<String>();
    ArrayList<String> Mat_Destination_Hotel = new ArrayList<String>();

    ArrayList<String> Mat2_Destination = new ArrayList<String>();
    ArrayList<String> Mat2_DayCount = new ArrayList<String>();
    ArrayList<String> Mat2_HotelID = new ArrayList<String>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from viewpager_main.xml
        setContentView(R.layout.view_pager_list_view);

        //Toolbar for dsplaying "Activities" title
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Activities");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        //code for handling back arrow
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        //"Proceed" button to go to payment page
        proceed_activity = (Button) findViewById(R.id.to_activities);
        proceed_activity.setText("Proceed to Transportation");


        //proceed_activity.setEnabled(false);
        proceed_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utility.isNetworkConnected(getApplicationContext())) {
                    Set<String> set = new HashSet<String>();
                    SharedPreferences sharedpreferences = getSharedPreferences("Itinerary", Context.MODE_PRIVATE);
                    final SharedPreferences.Editor editor = sharedpreferences.edit();
                    ActivityData = new String[ListViewPagerActivitiesAdapter.mActivitiesModel.size()];
                    String activity_string="";
                    int mActivitiesModel_size=ListViewPagerActivitiesAdapter.mActivitiesModel.size();
                    for (int i = 0; i < mActivitiesModel_size; i++) {
                        ArrayList<ActivitiesModel> modelRow = ListViewPagerActivitiesAdapter.mActivitiesModel.get("" + i);
                        String Datas = "";
                        int x = 0;
                        int modelRow_size=modelRow.size();
                        for (int j = 0; j < modelRow_size; j++) {
                            if (modelRow.get(j).isChecked() == true) {
                                if (x == 0) {
                                    Datas = "" + modelRow.get(j).getId() + "," + modelRow.get(j).getCost() + "," + modelRow.get(j).getTitle();
                                    x = 1;
                                } else {
                                    Datas = Datas + ":" + modelRow.get(j).getId() + "," + modelRow.get(j).getCost() + "," + modelRow.get(j).getTitle();
                                }
                                //Log.i("DataValue ", i + " Clicked " + j + " Check " + modelRow.get(j).isChecked());
                            }

                        }

                        if(!Datas.toString().equalsIgnoreCase(""))
                        ActivityData[i] = "" + Datas;
                        else
                            ActivityData[i] = ",,No Activities on " + (i+1) + " Day";
                        if(i == 0){
                           activity_string =  ActivityData[i];
                        }
                        else{
                            activity_string = activity_string + "/" + ActivityData[i];
                        }
                        set.add("" + ActivityData[i]);
                        Log.i("ActivitiesData", "" + ActivityData[i]);
                    }

                    editor.putStringSet("ActivitiesData", set);
                    editor.putString("ActivitiesDataString", activity_string);
                    editor.commit();

                    Intent intent = new Intent(ActivitiesActivity.this, TransportationActivity.class);
                    startActivity(intent);
                }
            }
        });

        SharedPreferences prefs = getSharedPreferences("Itinerary", MODE_PRIVATE);
        String Region_id = prefs.getString("RegionID", null);
        //Log.i("Hoteldataaaaaa","RID"+ Region_id);

        //DestinationName

        String Destinations = prefs.getString("DestinationID", null);
        destination_id = Destinations.trim().split(",");
        //Log.i("Hoteldataaaaaa","DID"+ Destinations);

        String DayCount = prefs.getString("DestinationCount", null);
        destination_day_count = DayCount.trim().split(",");
        Log.d("Destination day 1",""+destination_day_count);
        for (int x = 0; x < destination_day_count.length; x++) {
            TotalCountDays = TotalCountDays + Integer.parseInt(destination_day_count[x]);
        }
        Log.i("Hoteldataaaaaa", "DDC" + DayCount);

        Log.d("Destination day 4",""+TotalCountDays);

        String Arrival_port = prefs.getString("ArrivalPort", null);
        //Log.i("Hoteldataaaaaa","AP"+ Arrival_port);
        String Departure_port = prefs.getString("DeparturePort", null);
        //Log.i("Hoteldataaaaaa","DP"+ Departure_port);

        /*Set<String> HotelData = prefs.getStringSet("HotelRooms", null);
        String[] HotelDataArray = HotelData.toArray(new String[HotelData.size()]);*/

        String HotelData = prefs.getString("HotelRooms",null);
        String[] HotelDataArray = HotelData.trim().split("-");
        hotel_id_data = new String[HotelDataArray.length];
        int  HotelDataArray_length=HotelDataArray.length;
        Log.d("Hotel Data Array",""+HotelDataArray_length);
        for (int index = 0; index < HotelDataArray_length; index++) {   //Log.i("Hoteldataaaaaa",""+ HotelDataArray[index]);
            String[] hotel_room_Data = HotelDataArray[index].trim().split(",");

            //Changed hotel_id_data[index] = hotel_room_Data[index]; //Problem in hotel_room_Data (Length)
            hotel_id_data[index] = hotel_room_Data[0];
        }


        int Mat_count = 0;
        int destination_id_length=destination_id.length;
        for (int index = 0; index < (destination_id_length + 2); index++) {
            //Log.i("DestinationMat",deatination_day_count[index]);
            if (index == 0) {
                Mat_Destination_Count.add("0");
                Mat_Destination_Hotel.add("0");
                Mat_Destination_ID.add(Arrival_port);
            } else if (index == (destination_id.length + 1)) {

                Mat_Destination_Count.add("0");
                Mat_Destination_Hotel.add("0");
                Mat_Destination_ID.add(Departure_port);
            } else {

                Mat_Destination_Count.add(destination_day_count[Mat_count]);
                if (Mat_count < hotel_id_data.length) {
                    Mat_Destination_Hotel.add(hotel_id_data[Mat_count]);
                    Log.d("Mat count ", "" + Mat_count);
                }
                Mat_Destination_ID.add(destination_id[Mat_count]);
                Mat_count++;
            }
        //    Log.i("DestinationMatcount", Mat_Destination_Hotel.get(index));
        }

        int Mat_Destination_ID_size=Mat_Destination_ID.size();
        for (int index = 0; index < Mat_Destination_ID_size; index++) {
            int night = Integer.parseInt("" + Mat_Destination_Count.get(index));
            //Log.i("DestinationMatNight",""+ night);
            for (int x = 0; x <= night; x++) {
                if (x == 0) {
                    night--;
                }


                Mat2_Destination.add(Mat_Destination_ID.get(index));
                Log.i("DestinationMat", Mat_Destination_ID.get(index));
            }
        }



        int Mat2_count = 1;
        int x = 0;
        int Mat2_Destination_size=Mat2_Destination.size();
        for (int index = 1; index < Mat2_Destination_size; index++) {
            if (Mat2_Destination.get(index - 1) == Mat2_Destination.get(index)) {
                    Mat2_count++;
            } else {
                Mat2_count = 1;

            }
            Mat2_DayCount.add("" + Mat2_count);
            Log.i("DestinationMatDataCount", "" + Mat2_count);
        }
        int hotel_id_data_length=hotel_id_data.length;
        for (int index = 0; index < hotel_id_data_length; index++) {
            int night = Integer.parseInt("" + destination_day_count[index]);
            //Log.i("DestinationMatNight",""+ night);
            for (int j = 0;  j< night; j++) {
                Mat2_HotelID.add(hotel_id_data[index]);
                //Log.i("FinalValue", "" + hotel_id_data[index]);
            }
            if(index == (hotel_id_data.length - 1))
            {
                Mat2_HotelID.add("0");
            }

        }

        activitiesList = new ArrayList<>();

        Log.d("Destination day 5",""+Mat2_DayCount.size());
        Log.d("Destination day 6",""+Mat2_Destination.size());
        Log.d("Destination day 7",""+Mat2_HotelID.size());
        for(int i = 0 ; i< TotalCountDays +1;i++)
        {

                Log.v("Activities URL", "" + "http://stage.itraveller.com/backend/api/v1/activities?fromDestination=" + Mat2_Destination.get(i) + "&toDestination=" + Mat2_Destination.get(i + 1) + "&regionIds=" + Region_id + "&day=" + Mat2_DayCount.get(i) + "&hotelId=" + Mat2_HotelID.get(i));
        //        activitiesList.add("http://stage.itraveller.com/backend/api/v1/activities?fromDestination=" + Mat2_Destination.get(i) + "&toDestination=" + Mat2_Destination.get(i + 1) + "&regionIds=" + Region_id + "&day=" + Mat2_DayCount.get(i) + "&hotelId=" + Mat2_HotelID.get(i));
            activitiesList.add(Constants.API_ActivitiesActivity_URL+ Mat2_Destination.get(i) + "&toDestination=" + Mat2_Destination.get(i + 1) + "&regionIds=" + Region_id + "&day=" + Mat2_DayCount.get(i) + "&hotelId=" + Mat2_HotelID.get(i));
            /*Log.i("FinaL", "" + Mat2_Destination.get(i));
            Log.i("FinaLValue", "" + Mat2_HotelID.get(i));
            Log.i("FinaL", "" + Mat2_DayCount.get(i));*/
            //Log.i("FinalURL", "" + activitiesList.get(i));
        }

/*        activitiesList = new ArrayList<>();


        for(int i = 0 ; i< TotalCountDays +1;i++)
        {


        //    Log.v("Activities URL", "" + "http://stage.itraveller.com/backend/api/v1/activities?fromDestination=" + Mat2_Destination.get(i) + "&toDestination=" + Mat2_Destination.get(i + 1) + "&regionIds=" + Region_id + "&day=" + Mat2_DayCount.get(i) + "&hotelId=" + Mat2_HotelID.get(i));
            activitiesList.add("http://stage.itraveller.com/backend/api/v1/activities?fromDestination=" + Mat2_Destination.get(i) + "&toDestination=" + Mat2_Destination.get(i + 1) + "&regionIds=" + Region_id + "&day=" + Mat2_DayCount.get(i) + "&hotelId=" + Mat2_HotelID.get(i));
//            activitiesList.add(Constants.API_ActivitiesActivity_URL+ Mat2_Destination.get(i) + "&toDestination=" + Mat2_Destination.get(i + 1) + "&regionIds=" + Region_id + "&day=" + Mat2_DayCount.get(i) + "&hotelId=" + Mat2_HotelID.get(i));
            /*Log.i("FinaL", "" + Mat2_Destination.get(i));
            Log.i("FinaLValue", "" + Mat2_HotelID.get(i));
            Log.i("FinaL", "" + Mat2_DayCount.get(i));*/
            //Log.i("FinalURL", "" + activitiesList.get(i));
//        }


        lv1 = (ListView) findViewById(
                R.id.campaignListView);
        setData();


    }

    private void setData() {



        listViewPagerAdapter = new ListViewPagerActivitiesAdapter(ActivitiesActivity.this, activitiesList);

        // listViewPagerAdapter.add(null);
        lv1.setAdapter(listViewPagerAdapter);
        //ListViewPagerActivitiesAdapter listviewactivities = new ListViewPagerActivitiesAdapter();
        listViewPagerAdapter.SetAdapterListview(listViewPagerAdapter,activitiesList);
        //ListViewPagerActivitiesAdapter.SetAdapterListview();

    }


    public void onBackPressed() {
        finish();
    }
}
