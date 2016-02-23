package com.itraveller.activity;

/*
 * Created by VNK on 6/25/2015.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.itraveller.R;
import com.itraveller.adapter.ListViewPagerActivitiesAdapter;
import com.itraveller.adapter.ListViewPagerAdapter;
import com.itraveller.constant.Constants;
import com.itraveller.constant.Utility;
import com.itraveller.crash.ExceptionHandler;
import com.itraveller.model.ActivitiesModel;
import com.itraveller.model.HotelModel;
import com.itraveller.model.TransportationModel;
import com.itraveller.volley.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ActivitiesActivity extends ActionBarActivity {

    String default_activity_id_str = "";
    public String destination_id_arr[];
    public String destination_date_arr[];
    static String trans_id;
    String supplier_str = "";
    ArrayList<DiscountModel> Discount_list = new ArrayList<DiscountModel>();
    String discount_str = "";
    public String Destination_Date;


    public int sum = 0;
    String default_activity_cost_str = "";
    String[] HotelRoomData;
    private ArrayList<String> lowesthotelList;
    public int total_sum, activity_sum;
    public String Region_id;
    SharedPreferences prfs;
    public String Destination_Value;
    String[] hotel_destination;

    public int count;
    public ProgressBar loadingBar;


    ProgressDialog ndDialog;

    SharedPreferences preferences;
    SharedPreferences prefs;
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

    public static List<TransportationModel> transportationList = new ArrayList<TransportationModel>();

    SharedPreferences post_prefs;
    String activities_data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from viewpager_main.xml
        setContentView(R.layout.view_pager_list_view);
        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

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
        SharedPreferences prefs = getSharedPreferences("SavedData", MODE_PRIVATE);
        activities_data = prefs.getString("Activities", "0");


        /*mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        setupDrawer();*/
        ndDialog = new ProgressDialog(ActivitiesActivity.this);

        preferences = getSharedPreferences("Preferences", MODE_PRIVATE);
        prefs = getSharedPreferences("Itinerary", MODE_PRIVATE);
        prfs = getSharedPreferences("Itinerary", MODE_PRIVATE);
        post_prefs = getSharedPreferences("PostData", MODE_PRIVATE);

        Destination_Value = prfs.getString("DestinationID", null);

        hotel_destination = Destination_Value.trim().split(",");
        HotelRoomData = new String[hotel_destination.length];
        lowesthotelList = new ArrayList<>();

        //"Proceed" button to go to payment page
        proceed_activity = (Button) findViewById(R.id.to_activities);
        proceed_activity.setEnabled(false);

        proceed_activity.setText("Proceed to Transportation >>");

        //proceed_activity.setEnabled(false);
        proceed_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utility.isNetworkConnected(getApplicationContext())) {
                    Set<String> set = new HashSet<String>();
                    SharedPreferences sharedpreferences = getSharedPreferences("Itinerary", Context.MODE_PRIVATE);
                    final SharedPreferences.Editor editor = sharedpreferences.edit();
                    ActivityData = new String[mActivitiesModel.size()];
                    String activity_string = "";
                    int mActivitiesModel_size = mActivitiesModel.size();
                    Log.d("Activity test23", "" + mActivitiesModel_size);
                    for (int i = 0; i < mActivitiesModel_size; i++) {
                        ArrayList<ActivitiesModel> modelRow = mActivitiesModel.get("" + i);
                        String Datas = "";
                        int x = 0;
                        int modelRow_size = modelRow.size();
                        for (int j = 0; j < modelRow_size; j++) {
                            if (modelRow.get(j).isChecked() == true) {
                                if (x == 0) {
                                    Datas = "" + modelRow.get(j).getId() + "," + modelRow.get(j).getDisplay() + "," + modelRow.get(j).getTitle();
                                    x = 1;
                                } else {
                                    Datas = Datas + ":" + modelRow.get(j).getId() + "," + modelRow.get(j).getDisplay() + "," + modelRow.get(j).getTitle();
                                }
                                //Log.i("DataValue ", i + " Clicked " + j + " Check " + modelRow.get(j).isChecked());
                            }

                        }

                        if (!Datas.toString().equalsIgnoreCase(""))
                            ActivityData[i] = "" + Datas;
                        else
                            ActivityData[i] = ",,No Activities on " + (i + 1) + " Day";
                        if (i == 0) {
                            activity_string = ActivityData[i];
                        } else {
                            activity_string = activity_string + "/" + ActivityData[i];
                        }
                        set.add("" + ActivityData[i]);
                        Log.i("ActivitiesData", "" + ActivityData[i]);
                    }

                    editor.putStringSet("ActivitiesData", set);
                    editor.putString("ActivitiesDataString", activity_string);
                    Log.d("Activity test11", "" + activity_string);
                    editor.commit();

                    Intent intent = new Intent(ActivitiesActivity.this, TransportationActivity.class);
                    startActivity(intent);
                }
            }
        });

        prefs = getSharedPreferences("Itinerary", MODE_PRIVATE);
        Region_id = prefs.getString("RegionID", null);
        //Log.i("Hoteldataaaaaa","RID"+ Region_id);

        //DestinationName

        String Destinations = prefs.getString("DestinationID", null);
        destination_id = Destinations.trim().split(",");
        //Log.i("Hoteldataaaaaa","DID"+ Destinations);

        String DayCount = prefs.getString("DestinationCount", null);
        destination_day_count = DayCount.trim().split(",");
        Log.d("Destination day 1", "" + destination_day_count);
        for (int x = 0; x < destination_day_count.length; x++) {
            TotalCountDays = TotalCountDays + Integer.parseInt(destination_day_count[x]);
        }
        Log.i("Hoteldataaaaaa", "DDC" + DayCount);

        Log.d("Destination day 4", "" + TotalCountDays);

        String Arrival_port = prefs.getString("ArrivalPort", null);
        //Log.i("Hoteldataaaaaa","AP"+ Arrival_port);
        String Departure_port = prefs.getString("DeparturePort", null);
        //Log.i("Hoteldataaaaaa","DP"+ Departure_port);

        /*Set<String> HotelData = prefs.getStringSet("HotelRooms", null);
        String[] HotelDataArray = HotelData.toArray(new String[HotelData.size()]);*/

        String HotelData = prefs.getString("HotelRooms", null);
        Log.d("Activity rohan", "" + HotelData);
        String[] HotelDataArray = HotelData.trim().split("-");
        hotel_id_data = new String[HotelDataArray.length];
        int HotelDataArray_length = HotelDataArray.length;
        Log.d("Hotel Data Array", "" + HotelDataArray_length);
        for (int index = 0; index < HotelDataArray_length; index++) {   //Log.i("Hoteldataaaaaa",""+ HotelDataArray[index]);
            String[] hotel_room_Data = HotelDataArray[index].trim().split(",");

            //Changed hotel_id_data[index] = hotel_room_Data[index]; //Problem in hotel_room_Data (Length)
            hotel_id_data[index] = hotel_room_Data[0];
        }


        int Mat_count = 0;
        int destination_id_length = destination_id.length;
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

        int Mat_Destination_ID_size = Mat_Destination_ID.size();
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
        int Mat2_Destination_size = Mat2_Destination.size();
        for (int index = 1; index < Mat2_Destination_size; index++) {
            if (Mat2_Destination.get(index - 1) == Mat2_Destination.get(index)) {
                Mat2_count++;
            } else {
                Mat2_count = 1;

            }
            Mat2_DayCount.add("" + Mat2_count);
            Log.i("DestinationMatDataCount", "" + Mat2_count);
        }
        int hotel_id_data_length = hotel_id_data.length;
        for (int index = 0; index < hotel_id_data_length; index++) {
            int night = Integer.parseInt("" + destination_day_count[index]);
            //Log.i("DestinationMatNight",""+ night);
            for (int j = 0; j < night; j++) {
                Mat2_HotelID.add(hotel_id_data[index]);
                //Log.i("FinalValue", "" + hotel_id_data[index]);
            }
            if (index == (hotel_id_data.length - 1)) {
                Mat2_HotelID.add("0");
            }

        }

        activitiesList = new ArrayList<>();

        Log.d("Destination day 5", "" + Mat2_DayCount.size());
        Log.d("Destination day 6", "" + Mat2_Destination.size());
        Log.d("Destination day 7", "" + Mat2_HotelID.size());
        for (int i = 0; i < TotalCountDays + 1; i++) {

        //    if(Mat2_Destination.size()<=(Mat2_HotelID.size()-1)) {
                Log.v("Activities URL", "" + "http://stage.itraveller.com/backend/api/v1/activities?fromDestination=" + Mat2_Destination.get(i) + "&toDestination=" + Mat2_Destination.get(i + 1) + "&regionIds=" + Region_id + "&day=" + Mat2_DayCount.get(i) + "&hotelId=" + Mat2_HotelID.get(i));
                //        activitiesList.add("http://stage.itraveller.com/backend/api/v1/activities?fromDestination=" + Mat2_Destination.get(i) + "&toDestination=" + Mat2_Destination.get(i + 1) + "&regionIds=" + Region_id + "&day=" + Mat2_DayCount.get(i) + "&hotelId=" + Mat2_HotelID.get(i));
                activitiesList.add(Constants.API_ActivitiesActivity_URL + Mat2_Destination.get(i) + "&toDestination=" + Mat2_Destination.get(i + 1) + "&regionIds=" + Region_id + "&day=" + Mat2_DayCount.get(i) + "&hotelId=" + Mat2_HotelID.get(i));
            /*Log.i("FinaL", "" + Mat2_Destination.get(i));
            Log.i("FinaLValue", "" + Mat2_HotelID.get(i));
            Log.i("FinaL", "" + Mat2_DayCount.get(i));*/
                //Log.i("FinalURL", "" + activitiesList.get(i));
        //    }
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

        loadingBar = (ProgressBar) findViewById(R.id.progressBar5);
        loadingBar.setVisibility(View.VISIBLE);
        lv1 = (ListView) findViewById(
                R.id.campaignListView);
        lv1.setVisibility(View.GONE);
        setData();


    }



    class PriceComparison1 implements Comparator<TransportationModel> {

        @Override
        public int compare(TransportationModel o1, TransportationModel o2) {
            if (o1.getCost() > o2.getCost()) {
                return 1;
            } else {
                return -1;
            }
        }

    }




    public static HashMap<String, ArrayList<ActivitiesModel>> mActivitiesModel;

    private void setData() {
        listViewPagerAdapter = new ListViewPagerActivitiesAdapter(ActivitiesActivity.this, activitiesList, Mat2_DayCount);
        // listViewPagerAdapter.add(null);
        lv1.setAdapter(listViewPagerAdapter);
        //ListViewPagerActivitiesAdapter listviewactivities = new ListViewPagerActivitiesAdapter();
        listViewPagerAdapter.SetAdapterListview(listViewPagerAdapter, activitiesList);
        //ListViewPagerActivitiesAdapter.SetAdapterListview();
        mActivitiesModel = new HashMap<>();
        for (int index = 0; index < activitiesList.size(); index++) {
            mActivitiesModel.put("" + index, new ArrayList<ActivitiesModel>());

            Log.i("TestingRound", "Test");
        }
        for (int i = 0; i < activitiesList.size(); i++) {
            airportJSONForText(activitiesList.get(i), i, Integer.parseInt(Mat2_DayCount.get(i)), activitiesList.size());
        }
    }
    int totalLooping = 0;

    public void airportJSONForText(String url, final int position, final int dayCount, final int totalCount) {

        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    totalLooping++;
                    Log.i("Test", "Testing" + response);
                    Log.d("Boolean", "" + response.getBoolean("success"));
                    Log.d("Error", "" + response.getJSONObject("error"));
                    Log.d("Payload", "" + response.getJSONArray("payload"));
                    Log.d("Cost test test1233", "hi");
                    // JSONObject jsonobj = response.getJSONObject("payload").get;
                    // Parsing json
                    ArrayList activitiesList = new ArrayList();
                    for (int i = 0; i < response.getJSONArray("payload").length(); i++) {
                        JSONObject jsonarr = response.getJSONArray("payload").getJSONObject(i);

                        ActivitiesModel activities_model = new ActivitiesModel();
                        activities_model.setId(jsonarr.getInt("Id"));
                        activities_model.setTitle(jsonarr.getString("Title"));
                        activities_model.setCost(jsonarr.getInt("Cost"));
                        Log.d("Activity Cost", "" + jsonarr.getInt("Cost"));
                        activities_model.setHotel_Id(jsonarr.getString("Hotel_Id"));
                        activities_model.setMarkup(jsonarr.getInt("Markup"));
                        activities_model.setDisplay(jsonarr.getInt("Display"));
                        activities_model.setStatus(jsonarr.getInt("Status"));
                        activities_model.setRegion_Id(jsonarr.getString("Region_Id"));
                        if(!(jsonarr.isNull("Destination_Id")))
                        activities_model.setDestination_Id(jsonarr.getInt("Destination_Id"));
                        activities_model.setCompany_Id(jsonarr.getString("Company_Id"));
                        activities_model.setDay(jsonarr.getString("Day"));
                        activities_model.setDuration(jsonarr.getString("Duration"));
                        activities_model.setImage(jsonarr.getString("Image"));
                        activities_model.setFlag(jsonarr.getInt("Flag"));
                        if (jsonarr.getInt("Flag") == 1) {
                            default_activity_id_str += "" + jsonarr.getInt("Id") + ",";
                        }
                        activities_model.setDescription(jsonarr.getString("Description"));
                        activities_model.setNot_Available_Month(jsonarr.getString("Not_Available_Month"));
                        activities_model.setNot_Available_Days(jsonarr.getString("Not_Available_Days"));
                        activities_model.setDestination_Id_From(jsonarr.getString("Destination_Id_From"));
                        activities_model.setBookable(jsonarr.getString("Bookable"));
                        if (jsonarr.getInt("Flag") == 1) {
                            activities_model.setChecked(true);
                        }
                        activities_model.setChecked(false);

                        if (!activities_data.equalsIgnoreCase("0")) {
                            JSONObject act = new JSONObject(activities_data);
                            Log.i("JSON_Activities", "" + act);

                            JSONArray act_arr = act.getJSONArray("activities");
                            for (int p = 0; p < act_arr.length(); p++) {
                                JSONObject json_obj = act_arr.getJSONObject(p);
                                if (Integer.parseInt(json_obj.getString("DestinationID")) == jsonarr.getInt("Destination_Id")) {
                                    JSONArray json_array = json_obj.getJSONArray("ActivitiesID");
                                    JSONObject json_object = json_array.getJSONObject(dayCount - 1);
                                    JSONArray json_a1 = json_object.getJSONArray("Day " + (dayCount - 1));
                                    for (int q = 0; q < json_a1.length(); q++) {
                                        if (Integer.parseInt("" + json_a1.getInt(q)) == jsonarr.getInt("Id")) {
                                            activities_model.setChecked(true);
                                        }
                                    }
                                }
                            }
                        }
                        if (response.getJSONArray("payload").length() != 0)
                            activitiesList.add(activities_model);
                    }
                    mActivitiesModel.put(position + "", activitiesList);
                    if(totalCount == totalLooping){
                    listViewPagerAdapter.notifyDataSetChanged();
                        loadingBar.setVisibility(View.GONE);
                        lv1.setVisibility(View.VISIBLE);
                    }
                    proceed_activity.setEnabled(true);

                } catch (JSONException e) {
                    e.printStackTrace();
                    VolleyLog.d("Volley Error", "Error: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //System.err.println(error);
                // Handle your error types accordingly.For Timeout & No connection error, you can show 'retry' button.
                // For AuthFailure, you can re login with user credentials.
                // For ClientError, 400 & 401, Errors happening on client side when sending api request.
                // In this case you can check how client is forming the api and debug accordingly.
                // For ServerError 5xx, you can do retry or handle accordingly.
                if (error instanceof NetworkError) {
                    Toast.makeText(ActivitiesActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                } else if (error instanceof AuthFailureError) {
                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(ActivitiesActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                } else if (error instanceof TimeoutError) {
                }
            }
        });
        strReq.setRetryPolicy(new DefaultRetryPolicy(10000,
                5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq);
    }

    public void onBackPressed() {
        SaveDataActivities();

        finish();
    }

    public void SaveDataActivities() {
        try {
            int index =0;
            JSONArray json_main_arr = new JSONArray();
            for (int in = 0; in < destination_id.length; in++) {
                JSONObject json_obj = new JSONObject();
                json_obj.put("DestinationID", "" + destination_id[in]);
                json_obj.put("NightsCount", "" + destination_day_count[in]);
                JSONArray actvity_array = new JSONArray();
                for (int i = 0; i < Integer.parseInt(destination_day_count[in]); i++) {
                    JSONObject activities_obj = new JSONObject();
                    JSONArray activities_arr = new JSONArray();
                    ArrayList<ActivitiesModel> modelRow = mActivitiesModel.get("" + index);
                    index++;
                    int modelRow_size = modelRow.size();
                    for (int j = 0; j < modelRow_size; j++) {
                        if (modelRow.get(j).isChecked() == true) {
                            activities_arr.put(modelRow.get(j).getId());
                            //Log.i("DataValue ", i + " Clicked " + j + " Check " + modelRow.get(j).isChecked());
                        }

                    }

                    activities_obj.put("Day " + i, activities_arr);
                    actvity_array.put(activities_obj);
                }
                json_obj.put("ActivitiesID", actvity_array);
                json_main_arr.put(json_obj);
            }
            JSONObject final_data = new JSONObject();
            final_data.put("activities", json_main_arr);
            Log.e("Final String", final_data.toString());
            final SharedPreferences prefsData = getSharedPreferences("SavedData", MODE_PRIVATE);
            prefsData.edit().putString("Activities", "" + final_data).commit();


        } catch (JSONException e) {
        } catch (NullPointerException e){
        } catch (Exception e) {
        }

    }

}