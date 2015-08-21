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
import com.itraveller.constant.Utility;
import com.itraveller.model.ActivitiesModel;


public class ActivitiesActivity extends ActionBarActivity {

    // Declare Variable
    public ListViewPagerActivitiesAdapter listViewPagerAdapter;
    int ListItemPostion;
    int heightactivitiesList = 4;
    private ArrayList<String> activitiesList;
    int[] from_destination = new int[10];
    int[] to_destination = new int[10];
    int[] region_Id = new int[10];
    int[] day = new int[10];
    int[] hotel_Id = new int[10];
    ListView lv1;
    String[] destination_id, deatination_day_count, hotel_id_data;
    ArrayList<String> DataURL = new ArrayList<String>();
    int TotalCountDays = 0;
    String[] ActivityData;
    private Toolbar mToolbar;
    Button proceed_activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from viewpager_main.xml
        setContentView(R.layout.view_pager_list_view);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Activities");

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


        proceed_activity = (Button) findViewById(R.id.to_activities);
        proceed_activity.setText("Proceed");
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
                    for (int i = 0; i < ListViewPagerActivitiesAdapter.mActivitiesModel.size(); i++) {
                        ArrayList<ActivitiesModel> modelRow = ListViewPagerActivitiesAdapter.mActivitiesModel.get("" + i);
                        String Datas = "";
                        int x = 0;
                        for (int j = 0; j < modelRow.size(); j++) {
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
        deatination_day_count = DayCount.trim().split(",");
        for (int x = 0; x < deatination_day_count.length; x++) {
            TotalCountDays = TotalCountDays + Integer.parseInt(deatination_day_count[x]);
        }
        Log.i("Hoteldataaaaaa", "DDC" + DayCount);

        String Arrival_port = prefs.getString("ArrivalPort", null);
        //Log.i("Hoteldataaaaaa","AP"+ Arrival_port);
        String Departure_port = prefs.getString("DeparturePort", null);
        //Log.i("Hoteldataaaaaa","DP"+ Departure_port);

        Set<String> HotelData = prefs.getStringSet("HotelRooms", null);
        String[] HotelDataArray = HotelData.toArray(new String[HotelData.size()]);
        hotel_id_data = new String[HotelDataArray.length];
        for (int index = 0; index < HotelDataArray.length; index++) {   //Log.i("Hoteldataaaaaa",""+ HotelDataArray[index]);
            String[] hotel_room_Data = HotelDataArray[index].trim().split(",");


            hotel_id_data[index] = hotel_room_Data[index];
        }

        ArrayList<String> Mat_Destination_ID = new ArrayList<String>();
        ArrayList<String> Mat_Destination_Count = new ArrayList<String>();
        ArrayList<String> Mat_Destination_Hotel = new ArrayList<String>();

        ArrayList<String> Mat2_Destination = new ArrayList<String>();
        ArrayList<String> Mat2_DayCount = new ArrayList<String>();
        ArrayList<String> Mat2_HotelID = new ArrayList<String>();
        int Mat_count = 0;
        for (int index = 0; index < (destination_id.length + 2); index++) {
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
                Mat_Destination_Count.add(deatination_day_count[Mat_count]);
                Mat_Destination_Hotel.add(hotel_id_data[Mat_count]);
                Mat_Destination_ID.add(destination_id[Mat_count]);
                Mat_count++;
            }
            Log.i("DestinationMatcount", Mat_Destination_Hotel.get(index));
        }

        for (int index = 0; index < Mat_Destination_ID.size(); index++) {
            int night = Integer.parseInt("" + Mat_Destination_Count.get(index));
            //Log.i("DestinationMatNight",""+ night);
            for (int x = 0; x <= night; x++) {
               // Mat2_HotelID.add(hotel_id_data[index]);
                if (x == 0) {
                    night--;
                }
                Mat2_Destination.add(Mat_Destination_ID.get(index));
                Log.i("DestinationMat", Mat_Destination_ID.get(index));
            }
        }

        int Mat2_count = 1;
        int x = 0;
        for (int index = 1; index < Mat2_Destination.size(); index++) {
            if (Mat2_Destination.get(index - 1) == Mat2_Destination.get(index)) {
               /* if (x == 0) {
                    Mat2_count = 1;
                    x = 1;
                } else*/
                    Mat2_count++;
            } else {
                //x = 0;
                Mat2_count = 1;

            }
            Mat2_DayCount.add("" + Mat2_count);
            Log.i("DestinationMatDataCount", "" + Mat2_count);
        }
        for (int index = 0; index < hotel_id_data.length; index++) {
            int night = Integer.parseInt("" + deatination_day_count[index]);
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
        for(int i = 0 ; i< TotalCountDays +1;i++)
        {
            Log.v("Activities URL",""+"http://stage.itraveller.com/backend/api/v1/activities?fromDestination=" + Mat2_Destination.get(i) + "&toDestination=" + Mat2_Destination.get(i + 1) + "&regionIds=" + Region_id + "&day=" + Mat2_DayCount.get(i) + "&hotelId=" + Mat2_HotelID.get(i));
            activitiesList.add("http://stage.itraveller.com/backend/api/v1/activities?fromDestination=" + Mat2_Destination.get(i) + "&toDestination=" + Mat2_Destination.get(i + 1) + "&regionIds=" + Region_id + "&day=" + Mat2_DayCount.get(i) + "&hotelId=" + Mat2_HotelID.get(i));
            /*Log.i("FinaL", "" + Mat2_Destination.get(i));
            Log.i("FinaLValue", "" + Mat2_HotelID.get(i));
            Log.i("FinaL", "" + Mat2_DayCount.get(i));*/
            //Log.i("FinalURL", "" + activitiesList.get(i));
        }

       /* from_destination [0] = 86;
        from_destination [1] = 99;
        from_destination [2] = 65;
        from_destination [3] = 86;
        from_destination [4] = 99;

        to_destination [0] = 86;
        to_destination [1] = 99;
        to_destination [2] = 86;
        to_destination [3] = 86;
        to_destination [4] = 86;

        region_Id [0] = 86;
        region_Id [1] = 99;
        region_Id [2] = 86;
        region_Id [3] = 86;
        region_Id [4] = 86;*/


        lv1 = (ListView) findViewById(
                R.id.campaignListView);
        // lv1.setAdapter(new ArrayAdapter<String>(getActivity(),
        // android.R.layout.simple_list_item_1,aList));
        setData();


    }

    private void setData() {

        /*for (int index = 0; index < heightactivitiesList; index++) {
        *//*activitiesList.add("http://stage.itraveller.com/backend/api/v1/activities?fromDestination="
                + from_destination[index] + "&toDestination=" + to_destination[index]
                + "&regionIds=" + region_Id[index]);*//*
            activitiesList.add("http://stage.itraveller.com/backend/api/v1/activities?fromDestination=85&toDestination=85&regionIds=7&day=1&hotelId=4370");
        }*/

        listViewPagerAdapter = new ListViewPagerActivitiesAdapter(ActivitiesActivity.this, activitiesList);

        // listViewPagerAdapter.add(null);
        lv1.setAdapter(listViewPagerAdapter);
        //ListViewPagerActivitiesAdapter listviewactivities = new ListViewPagerActivitiesAdapter();
        listViewPagerAdapter.SetAdapterListview(listViewPagerAdapter,activitiesList);
        //ListViewPagerActivitiesAdapter.SetAdapterListview();

    }

    interface pagerCheckBoxChangedListner {
        public void OnCheckedChangeListenerCustomPager(int childPosition, boolean isChecked);

        public void OnImageClickListenerCustomPager(int childpostion);
    }


    private class PagerCheckedChangeListnerCustom implements pagerCheckBoxChangedListner {
        int groupPosition;

        public PagerCheckedChangeListnerCustom(int groupPosition) {
            this.groupPosition = groupPosition;
        }

        @Override
        public void OnCheckedChangeListenerCustomPager(int childPosition, boolean isChecked) {

        }

        @Override
        public void OnImageClickListenerCustomPager(int childpostion) {
            ArrayList<ActivitiesModel> modelRow = ListViewPagerActivitiesAdapter.mActivitiesModel.get("" + groupPosition);
            Log.i("PagerView Clicked", groupPosition + "Clicked" + childpostion + " Check " + modelRow.get(childpostion).isChecked());
            //activitiesList.size();

            //listViewPagerAdapter.notifyDataSetChanged();
        }
    }

    public void onBackPressed() {
        finish();
    }
}
