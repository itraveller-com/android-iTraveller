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

    String default_activity_id_str="";
    public static String destination_id_arr[];
    public static String destination_date_arr[];
    static String trans_id;
    String supplier_str="";
    ArrayList<DiscountModel> Discount_list=new ArrayList<DiscountModel>();
    String discount_str="";
    public static String Destination_Date;


    public static int sum=0;
    String default_activity_cost_str="";
    String[] HotelRoomData;
    private ArrayList<String> lowesthotelList;
    public static int total_sum,activity_sum;
    public static String Region_id;
    SharedPreferences prfs;
    public static String Destination_Value;
    String[] hotel_destination;

    public static int count;
    public static int transportation_cost;


    ProgressDialog ndDialog;

    SharedPreferences preferences;
    SharedPreferences prefs;
    TextView nameText,placesText,destinationText,arr_dateText,dep_dateText,daysText,adultsText,child_5_12_Text,child_below_5_Text;
    TextView nameSellerText,addressSellerText,arrAtText,dateDisplayText,roomDisplayText,totalPriceText;
    TextView discountPriceText,priceAdvanceText,remainingPriceText,departureText,transportationText;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
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



        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        setupDrawer();


        ndDialog=new ProgressDialog(ActivitiesActivity.this);


        preferences=getSharedPreferences("Preferences",MODE_PRIVATE);
        prefs=getSharedPreferences("Itinerary",MODE_PRIVATE);
        prfs=getSharedPreferences("Itinerary",MODE_PRIVATE);
        post_prefs=getSharedPreferences("PostData", MODE_PRIVATE);



        Destination_Value=prfs.getString("DestinationID", null);

        hotel_destination = Destination_Value.trim().split(",");
        HotelRoomData = new String[hotel_destination.length];
        lowesthotelList = new ArrayList<>();


        nameText=(TextView) findViewById(R.id.name_value);
        placesText=(TextView) findViewById(R.id.places_value);
        destinationText=(TextView) findViewById(R.id.destinations_value);
        arr_dateText=(TextView) findViewById(R.id.date_of_arrival_value);
        dep_dateText=(TextView) findViewById(R.id.date_of_departure_value);
        daysText=(TextView) findViewById(R.id.no_of_days_value);
        adultsText=(TextView) findViewById(R.id.no_of_adults_value);
        child_5_12_Text=(TextView) findViewById(R.id.no_of_children_5_12_value);
        child_below_5_Text=(TextView) findViewById(R.id.no_of_children_below_5_value);
        totalPriceText=(TextView) findViewById(R.id.total_price_value);
        discountPriceText=(TextView) findViewById(R.id.disount_value);
        remainingPriceText=(TextView) findViewById(R.id.price_after_discount_value);
        priceAdvanceText=(TextView) findViewById(R.id.booking_advance_value);
        nameSellerText=(TextView) findViewById(R.id.name_of_seller_value);
        addressSellerText=(TextView) findViewById(R.id.address_of_seller_value);
        arrAtText=(TextView) findViewById(R.id.arrival_at_value);
        dateDisplayText=(TextView) findViewById(R.id.date_of_arrival_display);
        roomDisplayText=(TextView) findViewById(R.id.room_type_display);
        departureText=(TextView) findViewById(R.id.departure_from_text_value);
        transportationText=(TextView) findViewById(R.id.transportation_text_value);

        Log.d("No of nights count", "" + preferences.getString("package_name", null));

        if(preferences.getInt("flag",0)==1)
        {
            String str=""+preferences.getString("f_name", null);
            nameText.setText(str.substring(0,1).toUpperCase()+str.substring(1));
        }
        else
        {
            nameText.setText("User");
        }

        placesText.setText("" + preferences.getString("package_name", null));
        destinationText.setText("" + prefs.getString("DestinationName", null));
        arr_dateText.setText(getConvertedDate("" + preferences.getString("Date_str", null)));
        dep_dateText.setText(getNextConvertedDate("" + preferences.getString("Date_str", null)));
        daysText.setText("" + prefs.getInt("Duration", 0));
        adultsText.setText("" + prefs.getString("Adults", null));
        child_5_12_Text.setText("" + prefs.getString("Children_12_5",null));
        child_below_5_Text.setText("" + prefs.getString("Children_5_2",null));
        totalPriceText.setText("Calculating...");
        discountPriceText.setText("Calculating...");
        remainingPriceText.setText("Calculating...");
        priceAdvanceText.setText("Calculating...");
        arrAtText.setText(prefs.getString("ArrivalPortString",null));
        dateDisplayText.setText("");
        roomDisplayText.setText("");
        departureText.setText(prefs.getString("DeparturePortString",null));
        transportationText.setText("Loading...");


        //"Proceed" button to go to payment page
        proceed_activity = (Button) findViewById(R.id.to_activities);
        proceed_activity.setText("Proceed to Transportation >>");


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
                    Log.d("Activity test23",""+mActivitiesModel_size);
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
                    Log.d("Activity test11",""+activity_string);
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
        Log.d("Activity rohan",""+HotelData);
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

            Log.v("Activities URL", "" + "http://itraveller.com/api/v1/activities?fromDestination=" + Mat2_Destination.get(i) + "&toDestination=" + Mat2_Destination.get(i + 1) + "&regionIds=" + Region_id + "&day=" + Mat2_DayCount.get(i) + "&hotelId=" + Mat2_HotelID.get(i));
        //     aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa   activitiesList.add("http://stage.itraveller.com/backend/api/v1/activities?fromDestination=" + Mat2_Destination.get(i) + "&toDestination=" + Mat2_Destination.get(i + 1) + "&regionIds=" + Region_id + "&day=" + Mat2_DayCount.get(i) + "&hotelId=" + Mat2_HotelID.get(i));
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

    public void CalculateActivitySum()
    {
        int activity_old_price=preferences.getInt("ActivityCost", 0);

        Log.d("Activity test5",""+activity_old_price);

        int old_sum=prefs.getInt("TotalCost",0);
        String activity_str=post_prefs.getString("ActivitiesDataString",null);
        Log.d("Activity test6",""+activity_str);

        String day_activity_arr[]=activity_str.split("/");


        for(int i=0;i<day_activity_arr.length;i++)
        {
            String activity_arr[]=day_activity_arr[i].split(":");

            Log.d("Activity test65", "" + activity_arr.length);

            for(int j=0;j<activity_arr.length;j++)
            {
                String activity_data_arr[]=activity_arr[j].split(",");
                Log.d("Activity test45", "" + activity_data_arr.length);

                    if(activity_data_arr.length>2)
                    {
                        String temp_str = activity_data_arr[1];
                        Log.d("Activity test56", "" + temp_str);

                        if (temp_str.equals(" ") || temp_str.equals(""))
                            temp_str = "0";

                        int temp = Integer.parseInt(temp_str);
                        Log.d("Activity test57", "" + temp);

                        activity_sum += temp;
                    }
                    else
                    {
                        activity_sum+=0;
                    }
            }
        }

        Log.d("Activity test1",""+activity_sum);
        Log.d("Activity test2", "" + activity_old_price);
//        Log.d("Activity test3", "" + old_sum);

        sum=sum+(activity_sum);


        getDefaultTransportationCost();
    }



    public void getDefaultTransportationCost(){

        Log.d("Welcome44","EntranceTransportation");

        String Transportation_URL=Constants.API_TransportationActivity_URL+Region_id;

        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET,
                Transportation_URL, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("Welcome454",""+response.toString());
                    Log.d("Boolean44", "" + response.getBoolean("success"));
                    Log.d("Error44", "" + response.getJSONObject("error"));
                    Log.d("Payload44", "" + response.getJSONArray("payload"));

                    // Parsing json
                    int response_JSON_arr_length = response.getJSONArray("payload").length();

                    for (int i = 0; i < response_JSON_arr_length; i++) {

                        JSONObject jsonarr = response.getJSONArray("payload").getJSONObject(i);
                        String Tra_url = Constants.API_TransportationActivity_Tra_URL;    //"http://stage.itraveller.com/backend/api/v1/b2ctransportation?transportationId=";

                        Log.d("Welcome45",""+response_JSON_arr_length);
                        //    transportation_flag=response.getJSONArray("payload").length();
                        Log.d("Welcome67", "" + Tra_url + jsonarr.getInt("Id") + "," + jsonarr.getString("Title"));
                        TransportationCost(Tra_url + jsonarr.getInt("Id"), jsonarr.getString("Title"), jsonarr.getInt("Max_Person"), jsonarr.getString("Image"), response.getJSONArray("payload").length(), i);
                        //url_data.add(Tra_url + jsonarr.getInt("Id") + "," + jsonarr.getString("Title") + "," + jsonarr.getInt("Max_Person") + "," + jsonarr.getString("Image") + "," + i + "," + response.getJSONArray("payload").length());
                        Log.d("Synchronous Task", "" + Tra_url + jsonarr.getInt("Id"));
                    }
                    //pDialog.dismiss();
                    // worker.callback = new TransportationActivity();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                catch (Exception e){
                    Log.d("Error Catched", "" + e.getMessage());

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

                    //pDialog.hide();
                    Toast.makeText(ActivitiesActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                } else if (error instanceof AuthFailureError) {
                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                    //pDialog.hide();
                    Toast.makeText(ActivitiesActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                } else if (error instanceof TimeoutError) {
                }
            }
        }) {
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(10000,
                5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }


    public void TransportationCost(String TransURL, final String title, final int max_person, final String img, final int last_index, final int temp_index) {

        Log.d("Welcome46","Welcome");

        Log.d("Welcome47", "" + TransURL);

        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET,
                TransURL, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("Boolean66", "" + response.getBoolean("success"));
                    Log.d("Error66", "" + response.getJSONObject("error"));
                    Log.d("Payload66", "" + response.getJSONObject("payload"));

                    // JSONObject jsonobj = response.getJSONObject("payload").get;
                    // Parsing json
                    JSONObject jsonarr = response.getJSONObject("payload");
                    TransportationModel transportation_model = new TransportationModel();
                    transportation_model.setId(jsonarr.getInt("Id"));
                    transportation_model.setTransportation_Id(jsonarr.getInt("Transportation_Id"));
                    transportation_model.setTitle("" + title);
                    transportation_model.setCost(jsonarr.getInt("Cost"));
                    Log.d("Transportation cost", "" + jsonarr.getInt("Cost"));
                    transportation_model.setCost1(jsonarr.getInt("Cost1"));
                    transportation_model.setKM_Limit(jsonarr.getInt("KM_Limit"));
                    transportation_model.setPrice_Per_KM(jsonarr.getInt("Price_Per_KM"));
                    transportation_model.setMax_Person(max_person);
                    transportation_model.setImage(img);
                    transportation_model.setIsCheck(false);
                    Log.d("Destination date test81", "" + transportationList.size());


                    transportationList.add(transportation_model);

                    Collections.sort(transportationList, new PriceComparison1());


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("Welcome122",""+(last_index-1));
                Log.d("Welcome147",""+temp_index);
                if((last_index-1)==temp_index)
                {
                    Log.d("Destination date te110","hi");
                    CalculateSum();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Volley Error", "Error: " + error.getMessage());
                //pDialog.hide();

            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);



    }

    public void CalculateSum()
    {
        Log.d("Destination date te771", "" + sum);
    //    Log.d("Destination date te772", "" + transportationList.get(1).getCost());
        Log.d("Destination date te773", "" + transportationList.size());

        SharedPreferences.Editor editor=prfs.edit();

        if(count==0)
        {

            if(transportationList.size()>1)
            {
                sum += transportationList.get(1).getCost();
                transportation_cost+=transportationList.get(1).getCost();
                transportationText.setText("" + transportationList.get(1).getTitle());



                editor.putString("TransportaionTitle", "" + transportationList.get(1).getTitle());
                editor.putString("TransportationIDV", "" + transportationList.get(1).getId());
                editor.putString("TransportationCostOld",""+transportation_cost);
                Log.d("Destination date tt89", "" + transportationList.get(1).getCost());
                Log.d("Destination date tt97", "" + transportation_cost);


                editor.putInt("TotalCost", sum);
                editor.commit();

                trans_id=""+transportationList.get(1).getId();
            }
            else if(transportationList.size()==1){
                sum+=transportationList.get(0).getCost();
                transportation_cost+=transportationList.get(0).getCost();
                transportationText.setText("" + transportationList.get(0).getTitle());


                editor.putString("TransportaionTitle", "" + transportationList.get(0).getTitle());
                editor.putString("TransportationIDV", "" + transportationList.get(0).getId());
                editor.putString("TransportationCostOld",""+transportation_cost);
                Log.d("Destination date tt89", "" + transportationList.get(0).getCost());
                Log.d("Destination date tt97", "" + transportation_cost);


                editor.putInt("TotalCost", sum);
                editor.commit();

                trans_id=""+transportationList.get(0).getId();
            }

        //    transportation_cost+=transportationList.get(1).getCost();


            Log.d("Destination date te77", "" + sum);
        //    Log.d("Destination date tt77", "" + transportationList.get(1).getTitle());
        //    Log.d("Destination date tt87", "" + transportationList.get(1).getCost());



            count++;

        //    Log.d("TransporatationID test",""+transportationList.get(1).getId());

            getSupplierDetails();

        }

    }


    public void getSupplierDetails()
    {
        Log.d("URL test test10","hi");
        String url="http://m.itraveller.com/api/v1/supplier?region="+Region_id;
        int i;
        Log.d("URL test test11",""+url+"hii "+destination_date_arr.length);
        for(i=0;i<destination_date_arr.length;i++) {

            final int finalI = i;
            JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET,
                    url, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Log.i("Test99", "Testing7657" + response);
                        Log.d("Boolean99", "" + response.getBoolean("success"));
                        Log.d("Error99", "" + response.getJSONObject("error"));
                        Log.d("Payload99", "" + response.getJSONArray("payload"));
                        // JSONObject jsonobj = response.getJSONObject("payload").get;
                        // Parsing json
                        int temp=0;

                        Log.d("URL test test14",""+response.getJSONArray("payload").length());
                        Log.d("URL test test15",""+destination_date_arr[finalI]);
                        for (int j = 0; j < response.getJSONArray("payload").length(); j++) {
                            JSONObject jsonarr = response.getJSONArray("payload").getJSONObject(j);
                            Log.d("URL test test16",""+jsonarr.getString("Company_Id"));
                            supplier_str +=""+ jsonarr.getString("Company_Id") + "::" + ""+jsonarr.getString("Name") + "::" + ""+jsonarr.getString("Address")+"::" +""+destination_date_arr[finalI] +":-";
                            Log.d("URL test test6",""+supplier_str);
                            temp=j;
                        }

                        if(temp==(response.getJSONArray("payload").length()-1))
                        {
                            Log.d("URL test test5",""+supplier_str);
                            getDiscount(supplier_str);
                        }

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

                        // pDialog.hide();
                        Toast.makeText(ActivitiesActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    } else if (error instanceof ServerError) {
                    } else if (error instanceof AuthFailureError) {
                    } else if (error instanceof ParseError) {
                    } else if (error instanceof NoConnectionError) {
                        // pDialog.hide();
                        Toast.makeText(ActivitiesActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    } else if (error instanceof TimeoutError) {
                    }
                }
            }) {
            };
        /*strReq.setRetryPolicy(new DefaultRetryPolicy(10000,
                5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
            AppController.getInstance().addToRequestQueue(strReq);

        }

    }

    public void getDiscount(String supplier_details)
    {
        Log.d("URL test test1",""+supplier_details);

        final String no_of_supplier[]=supplier_details.split(":-");
        String supplier_data[]=new String[4];

        Log.d("URL test test2",""+no_of_supplier.length);
        int temp;
        final DiscountModel discount_model=new DiscountModel();


        supplier_data=no_of_supplier[0].split("::");

        String url = "http://m.itraveller.com/api/v1/supplier/discount?checkInDate="+supplier_data[3]+"&companyId=" + supplier_data[0];

        Log.d("URL test test",""+url);

        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.i("Test", "Testing7657" + response);
                    Log.d("Boolean", "" + response.getBoolean("success"));
                    Log.d("Error78", "" + response.getJSONObject("error"));
                    Log.d("Payload78", "" + response.getJSONArray("payload"));
                    // JSONObject jsonobj = response.getJSONObject("payload").get;
                    // Parsing jso

                    JSONObject jsonarr = response.getJSONArray("payload").getJSONObject(0);

                    discount_model.setCompany_ID("" + jsonarr.getString("Company_Id"));
                    discount_model.setCompany_Name("" + jsonarr.getString("Company_Name"));
                    discount_model.setCompany_Discount(Integer.parseInt("" + jsonarr.getString("Discount")));
                    discount_model.setCompany_Address(""+jsonarr.getString("Company_Address"));

                    Discount_list.add(discount_model);

                    Collections.sort(Discount_list, new DiscountComparison());

                    discount_str+=""+jsonarr.getString("Company_Id")+","+""+jsonarr.getString("Company_Name")+","+jsonarr.getString("Discount")+"::";

                    CalculateDiscount();

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

                    // pDialog.hide();
                    Toast.makeText(ActivitiesActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                } else if (error instanceof AuthFailureError) {
                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                    // pDialog.hide();
                    Toast.makeText(ActivitiesActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                } else if (error instanceof TimeoutError) {
                }
            }
        }) {
        };

        /*strReq.setRetryPolicy(new DefaultRetryPolicy(10000,
                5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        AppController.getInstance().addToRequestQueue(strReq);


    }

    public void CalculateDiscount()
    {
        Log.d("URL test test21", "" + discount_str);

        Log.d("URL test test45", "" + sum);
        Log.d("URL test test24", "" + Discount_list.size());
        for(int i=0;i<Discount_list.size();i++)
            Log.d("URL test test23",""+Discount_list.get(i).getCompany_ID()+"  "+Discount_list.get(i).getCompany_Discount()+" "+Discount_list.get(i).getCompany_Name());

        totalPriceText.setText("\u20B9 "+"" + sum);

        discountPriceText.setText("" + Discount_list.get(0).getCompany_Discount() + " %");

        SharedPreferences.Editor editor=post_prefs.edit();
        editor.putInt("DiscountValue", Discount_list.get(0).getCompany_Discount());

        int remaining_price=sum-((Integer.parseInt(""+Discount_list.get(0).getCompany_Discount())*sum)/100);

        remainingPriceText.setText("\u20B9 "+""+remaining_price);

        int adv_price=(20*remaining_price)/100;

        priceAdvanceText.setText("\u20B9 "+""+adv_price);

        nameSellerText.setText("" + Discount_list.get(0).getCompany_Name());
        editor.putString("SellerName", "" + Discount_list.get(0).getCompany_Name());

        addressSellerText.setText("" + Discount_list.get(0).getCompany_Address());
        editor.putString("SellerAddress", "" + Discount_list.get(0).getCompany_Address());

        editor.commit();

        ndDialog.hide();
    }


    class PriceComparison1 implements Comparator<TransportationModel> {

        @Override
        public int compare(TransportationModel o1,TransportationModel o2) {
            if(o1.getCost() > o2.getCost()){
                return 1;
            } else {
                return -1;
            }
        }

    }


    public void getUserSelectedActivityData()
    {
        if(Utility.isNetworkConnected(getApplicationContext())) {

            String Hotel_ID_arr[]=prefs.getString("HotelRooms",null).split("-");
            Log.d("Activity test testte",Hotel_ID_arr[0]);
            String Hotel_Data_arr[]=new String[4];

            for(int i=0;i<Hotel_ID_arr.length;i++)
            {
                Hotel_Data_arr = Hotel_ID_arr[i].split(",");
                Log.d("Activity test testtt",""+Hotel_Data_arr.length);
                sum += Integer.parseInt(Hotel_Data_arr[2]);

            }

            Set<String> set = new HashSet<String>();
            final SharedPreferences.Editor editor = post_prefs.edit();
            ActivityData = new String[ListViewPagerActivitiesAdapter.mActivitiesModel.size()];
            String activity_string="";
            int mActivitiesModel_size=ListViewPagerActivitiesAdapter.mActivitiesModel.size();
            Log.d("Activity test23", "" + mActivitiesModel_size);
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
            Log.d("Activity test11", "" + activity_string);
            editor.commit();

            CalculateActivitySum();
        }

    }

    public String getNextConvertedDate(String str)
    {
        String month[]={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov", "Dec"};
        String nights_count="" + prefs.getString("DestinationCount", null);
        Log.d("URL test test46",""+nights_count);
        String nights_count_arr[]=nights_count.split(",");
        int no_of_nights=0;

        for(int i=0;i<nights_count_arr.length;i++)
        {
            no_of_nights+=Integer.parseInt(nights_count_arr[i]);
        }


        //rohan
        String dateInString = str; // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        Calendar c = Calendar.getInstance();

        try {
            c.setTime(sdf.parse(dateInString));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        c.add(Calendar.DATE, (no_of_nights+1));//insert the number of days you want to be added to the current date
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date resultdate = new Date(c.getTimeInMillis());
        dateInString = sdf.format(resultdate);

        Log.d("Calendar test test455", "" + dateInString);

        String temp[]=dateInString.split("-");


        int day=Integer.parseInt(temp[0]);
        Log.d("URL test test47",""+day);
        int temp_month = Integer.parseInt(temp[1]);
        Log.d("URL test test48",""+temp_month);
        int year = Integer.parseInt(temp[2]);
        Log.d("URL test test49",""+no_of_nights);



        String day_str = getDay(temp[0] + "-" + temp[1]+"-"+temp[2]);

        Log.d("URL test test50",temp[0] + "-" + temp[1]+"-"+temp[2]);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("Date_end_str",temp[0] + "-" + temp[1]+"-"+temp[2]);
        editor.commit();

        day_str=day_str.substring(0, 3);

        str=day_str+", "+day+" "+month[temp_month-1]+" "+temp[2];

        return str;
    }


    private String getDay(String dateStr){
        //dateStr must be in DD-MM-YYYY Formate
        Date date = null;
        String day=null;

        try {
            date = new SimpleDateFormat("DD-MM-yyyy").parse(dateStr);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
            //System.out.println("DAY "+simpleDateFormat.format(date).toUpperCase());
            day = simpleDateFormat.format(date);


        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return day;
    }

    public String getConvertedDate(String str)
    {
        String month[]={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        String temp[]=str.split("-");
        int temp_month=Integer.parseInt(temp[2]);
        str=temp[0]+", "+temp[1]+" "+month[temp_month-1]+" "+temp[3];
        return str;
    }


    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                ndDialog.setMessage("Loading...");
                ndDialog.setCancelable(true);
                ndDialog.show();

                Destination_Value=prfs.getString("DestinationID",null);
                Destination_Date=prfs.getString("DestinationDate",null);

                String temp_destination_id_arr[]=Destination_Value.split(",");
                String temp_destination_date_arr[]=Destination_Date.split(",");

                destination_id_arr=new String[temp_destination_id_arr.length];
                destination_date_arr=new String[temp_destination_date_arr.length];

                Log.d("Destination date test111", "" + Destination_Date);


                destination_id_arr=Destination_Value.split(",");
                destination_date_arr=Destination_Date.split(",");




                    totalPriceText.setText("Calculating...");
                    discountPriceText.setText("Calculating...");
                    remainingPriceText.setText("Calculating...");
                    priceAdvanceText.setText("Calculating...");
                    nameSellerText.setText("Loading...");
                    addressSellerText.setText("Loading...");
                    transportationText.setText("Loading...");

                    getUserSelectedActivityData();

                getSupportActionBar().setTitle("Summary Data");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);

                activity_sum=0;
                total_sum=0;
                transportationList.clear();
                sum=0;
                count=0;
                transportation_cost=0;


                getSupportActionBar().setTitle("Activities");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }


        };

        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        int menuToUse = R.menu.right_side_menu;

        MenuInflater inflater = getMenuInflater();


        inflater.inflate(menuToUse, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        if (item != null && item.getItemId() == R.id.btnMyMenu) {
            if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
            } else {
                mDrawerLayout.openDrawer(Gravity.RIGHT);
            }
        }


        return false;
    }

    private void setData() {



        listViewPagerAdapter = new ListViewPagerActivitiesAdapter(ActivitiesActivity.this, activitiesList, Mat2_DayCount);

        // listViewPagerAdapter.add(null);
        lv1.setAdapter(listViewPagerAdapter);
        //ListViewPagerActivitiesAdapter listviewactivities = new ListViewPagerActivitiesAdapter();
        listViewPagerAdapter.SetAdapterListview(listViewPagerAdapter, activitiesList);
        //ListViewPagerActivitiesAdapter.SetAdapterListview();

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
                    ArrayList<ActivitiesModel> modelRow = ListViewPagerActivitiesAdapter.mActivitiesModel.get("" + index);
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
