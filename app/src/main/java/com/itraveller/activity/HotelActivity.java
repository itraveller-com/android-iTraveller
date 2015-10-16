package com.itraveller.activity;

/**
 * Created by VNK on 6/25/2015.
 */

import android.app.ProgressDialog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import android.util.Log;

import android.view.View;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
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

import com.itraveller.adapter.HotelRoomAdapter;
import com.itraveller.adapter.ListViewPagerAdapter;

import com.itraveller.constant.Constants;

import com.itraveller.constant.Utility;
import com.itraveller.model.ActivitiesModel;
import com.itraveller.model.HotelModel;
import com.itraveller.model.HotelRoomModel;

import com.itraveller.model.MealPlanModel;
import com.itraveller.volley.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class  HotelActivity extends ActionBarActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    TextView nameText,placesText,destinationText,arr_dateText,dep_dateText,daysText,adultsText,child_5_12_Text,child_below_5_Text;
    TextView nameSellerText,addressSellerText,arrAtText,dateDisplayText,roomDisplayText,totalPriceText;
    TextView discountPriceText,priceAdvanceText,remainingPriceText;
    // Declare Variable
    public static ListViewPagerAdapter listViewPagerAdapter;
    int ListItemPostion;
    int heightHotelList = 5;
    Toolbar mToolbar;
    int error_bit = 0;
    private ArrayList<String> hotelList;
    private ArrayList<String> mealPlanURL;
    private ArrayList<String> lowesthotelList;
    String[] hotel_destination, destination_date;
    ListView lv1;
    LinearLayout second;
    ///HOTEL ROOM ACTIVITY
    int[] value = new int[10];
    private ListView listView;
    private List<HotelRoomModel> roomList ;
    private List<MealPlanModel> mealplanList;
    private HotelRoomAdapter adapter;
    int check_bit=0;
    String[] HotelRoomData;
    int cposition, gposition;
    String lowest_hotel_url = Constants.API_HotelActivity_Lowest_Hotel; //"http://stage.itraveller.com/backend/api/v1/lowesthotel?destinationId=";
    String Region_ID;
    Button activites;
    ProgressDialog pDialog;
    CheckBox chk_breakfast,chk_lunch,chk_dinner;
    boolean hotelcheckbit ;
    private LinearLayout mealplanlayout;
    private TextView mealhead;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from viewpager_main.xml
        setContentView(R.layout.view_pager_list_view);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle("Hotels");

        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //      mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

  //      setupDrawer();

        mealplanList = new ArrayList<MealPlanModel>();

        mealplanlayout = (LinearLayout) findViewById(R.id.meal_plan);
        mealhead = (TextView) findViewById(R.id.room);
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



/*        SharedPreferences preferences=getSharedPreferences("Preferences",MODE_PRIVATE);
        SharedPreferences prefs=getSharedPreferences("Itinerary",MODE_PRIVATE);

        if(preferences.getInt("flag",0)==1)
        {
            String str=""+preferences.getString("f_name", null);
            nameText.setText(str.substring(0,1).toUpperCase()+str.substring(1));
        }
        else
        {
            nameText.setText("User");
        }
        placesText.setText("" + prefs.getString("package_name", null));
        destinationText.setText("" + prefs.getString("DestinationName", null));
        arr_dateText.setText(""+preferences.getString("Date_str",null));
        dep_dateText.setText(""+preferences.getString("Date_str",null));
        daysText.setText(""+prefs.getInt("Duration",0));
        adultsText.setText(""+prefs.getString("Adults",null));
        child_5_12_Text.setText(""+prefs.getString("Children_12_5",null));
        child_below_5_Text.setText(""+prefs.getString("Children_5_2",null));
        totalPriceText.setText("");
        discountPriceText.setText("");
        remainingPriceText.setText("");
        priceAdvanceText.setText("");
        addressSellerText.setText("");
*/

        chk_breakfast = (CheckBox) findViewById(R.id.breakfast);
        chk_lunch = (CheckBox) findViewById(R.id.lunch);
        chk_dinner = (CheckBox) findViewById(R.id.dinner);
        chk_lunch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                /* if (!hotelcheckbit) {
                    Toast.makeText(HotelActivity.this, "Please select the room", Toast.LENGTH_LONG).show();
                }*/
                if(b) {
                    final ArrayList<HotelModel> modelRow = ListViewPagerAdapter.mHotelModels.get("" + gposition);
                    if (modelRow.get(cposition).getHotel_Id() == mealplanList.get(0).getHotel_Id())
                        modelRow.get(cposition).setLunch(Integer.parseInt("" + mealplanList.get(0).getLunch()));
                }
                else{
                    final ArrayList<HotelModel> modelRow = ListViewPagerAdapter.mHotelModels.get("" + gposition);
                    if (modelRow.get(cposition).getHotel_Id() == mealplanList.get(0).getHotel_Id())
                        modelRow.get(cposition).setLunch(Integer.parseInt("0"));
                }
            }
        });

        chk_dinner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                /* if (!hotelcheckbit) {
                    Toast.makeText(HotelActivity.this, "Please select the room", Toast.LENGTH_LONG).show();
                }*/
                if (b) {
                    final ArrayList<HotelModel> modelRow = ListViewPagerAdapter.mHotelModels.get("" + gposition);
                    if (modelRow.get(cposition).getHotel_Id() == mealplanList.get(0).getHotel_Id())
                        modelRow.get(cposition).setDinner(Integer.parseInt("" + mealplanList.get(0).getDinner()));
                } else {
                    final ArrayList<HotelModel> modelRow = ListViewPagerAdapter.mHotelModels.get("" + gposition);
                    if (modelRow.get(cposition).getHotel_Id() == mealplanList.get(0).getHotel_Id())
                        modelRow.get(cposition).setDinner(Integer.parseInt("0"));
                }
            }
        });

        pDialog = new ProgressDialog(HotelActivity.this);

        pDialog.setMessage("Loading...");

        Bundle bundle = getIntent().getExtras();
        String IDS_value = bundle.getString("DestinationsIDs");
        SharedPreferences prfs = getSharedPreferences("Itinerary", Context.MODE_PRIVATE);
        String Date_value = prfs.getString("DestinationDate", "");
        Region_ID= prfs.getString("RegionID", "");

        //Destination Count



        hotel_destination = IDS_value.trim().split(",");
        destination_date = Date_value.trim().split(",");
        HotelRoomData = new String[hotel_destination.length];
        second = (LinearLayout) findViewById(R.id.room_type_full);
        lv1 = (ListView) findViewById(R.id.campaignListView);
        // lv1.setAdapter(new ArrayAdapter<String>(getActivity(),
        // android.R.layout.simple_list_item_1,aList));
        SharedPreferences sharedpreferences = getSharedPreferences("Itinerary", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();
        setData();
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.i("TestDATA", "Testing" + position);
            }
        });

        activites = (Button) findViewById(R.id.to_activities);
        activites.setEnabled(false);
        activites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<String> set = new HashSet<String>();
                String hotel_string_main = "";
                String itinerary_hotel = "";
              /*  for(int i =0; i< HotelRoomData.length;i++)
                {
                    Log.i("Hotel Room "+i,""+ HotelRoomData[i]);
                    set.add("" + HotelRoomData[i]);
                }*/
                HashMap<String,ArrayList<ActivitiesModel>> mActivitiesModel = new HashMap<String, ArrayList<ActivitiesModel>>();



                for(int i =0; i< lowesthotelList.size();i++)
                {
                    Log.d("Destination day 3",""+i);
                    String hotel_string = "";
                    String datas = ""+ HotelRoomData[i];
                    Log.i("lowestHotelData1 " + i, "" + lowesthotelList.get(i));
                    Log.i("HotelData1 " + i, "" + HotelRoomData[i]);
                    String[] hotel_room_Data = lowesthotelList.get(i).trim().split(",");

                    for(int j = 0 ;j < lowesthotelList.size(); j++) {
                        ArrayList<HotelModel> modelRow = ListViewPagerAdapter.mHotelModels.get("" + j);
                        for(int k = 0;k< modelRow.size();k++) {
                            if (modelRow.get(k).getHotel_Id() == Integer.parseInt(hotel_room_Data[0])) {
                               //if(k == 0)
                                 hotel_string = ""+ modelRow.get(k).getHotel_Name() + "," +  modelRow.get(k).getHotel_Description() + "," +  modelRow.get(k).getHotel_Id() + "," + modelRow.get(k).getLunch() + "," + modelRow.get(k).getDinner();
                               /* else
                                   hotel_string = ""+hotel_string + "-" + modelRow.get(k).getHotel_Name() + "," +  modelRow.get(k).getHotel_Description() + "," +  modelRow.get(k).getHotel_Id();*/
                            }
                        }
                    }

                    //if(datas.equalsIgnoreCase("null")) {
                    set.add("" + lowesthotelList.get(i));
                    if(i == 0) {
                        hotel_string_main = "" + hotel_string;
                        itinerary_hotel = "" + lowesthotelList.get(i);
                    }
                    else{
                        hotel_string_main = hotel_string_main + "-" + hotel_string;
                        itinerary_hotel = itinerary_hotel + "-" + lowesthotelList.get(i);
                    }

                }

                    /*}
                    else{
                    set.add("" + HotelRoomData[i]);}*/
                    //Log.i("Hotel Room 123" + i, "" + set.toArray()[i]);
                editor.putString("Hotels", hotel_string_main);
                editor.putString("HotelRooms", itinerary_hotel);
                editor.putString("ItineraryHotelRooms", itinerary_hotel);

                editor.commit();

                if(error_bit != 1) {
                    Intent intent = new Intent(HotelActivity.this, ActivitiesActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(HotelActivity.this, "Please remove the destinations without hotels/rooms" ,Toast.LENGTH_LONG).show();

                }
            }
        });

        //String url_checkroom = "http://stage.itraveller.com/backend/api/v1/roomtariff?region=7&room=52&checkInDate=2015-07-26";
        //url = "http://stage.itraveller.com/backend/api/v1/internationalflight?travelFrom=BOM&arrivalPort=MRU&departDate=2015-07-26&returnDate=2015-08-01&adults=2&children=0&infants=0&departurePort=MRU&travelTo=BOM";
        //hotelRoomsCheck(url_checkroom);



    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Summary Data");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle("Hotels");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }


        };

        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }


    /*    @Override
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
    */
    public interface RadiobuttonListener {
        public void RadioChangeListenerCustom(String position);
    }

    public interface pagerCheckBoxChangedListner1 {
        public void OnCheckedChangeListenerCustomPager(int childPosition, boolean isChecked);

        public void OnImageClickListenerCustomPager(int childpostion, int grouppostion);
    }

    private void setData() {
        hotelList = new ArrayList<>();
        mealPlanURL = new ArrayList<>();
        lowesthotelList = new ArrayList<String>();
        for (int index = 0; index < hotel_destination.length; index++) {
        //    hotelList.add("http://stage.itraveller.com/backend/api/v1/hotel/destintionId/" + hotel_destination[index]);
            hotelList.add(Constants.API_HotelActivity_HotelList + hotel_destination[index]);
            //Default Hotel Set Value
            DefaultHotelRoomSet(lowest_hotel_url + hotel_destination[index] + "&checkInDate=" + destination_date[index] + "&regionId=" + Region_ID, index);
            MealPlan("http://stage.itraveller.com/backend/api/v1/hotelinclusion?hotel=" + hotel_destination[index] + "&region=" + Region_ID + "&checkInDate=" + destination_date[index], -1);
            Log.i("HotelURL", "" + "http://stage.itraveller.com/backend/api/v1/hotel/destintionId/" + hotel_destination[index]);
            Log.i("MealPlanURL","http://stage.itraveller.com/backend/api/v1/hotelinclusion?hotel=" + hotel_destination[index] + "&region=" + Region_ID + "&checkInDate=" + destination_date[index]);
        }

    }

    public void HiddenLayoutMealPlan(boolean value)
    {
        if(value){
            mealplanlayout.setVisibility(View.VISIBLE);
            mealhead.setVisibility(View.VISIBLE);

        }else{
            mealplanlayout.setVisibility(View.INVISIBLE);
            mealhead.setVisibility(View.INVISIBLE);
        }
    }


    public void hotelRooms(String url, final String checkinDate) {

        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("Boolean", "" + response.getBoolean("success"));
                    Log.d("Error", "" + response.getJSONObject("error"));
                    Log.d("Payload", "" + response.getJSONObject("payload"));

                    // JSONObject jsonobj = response.getJSONObject("payload").getJSONObject()
                    // Parsing json
                    for (int i = 0; i < response.getJSONObject("payload").length(); i++) {
                        Iterator<?> destinationKeys = response.getJSONObject("payload").keys();

                        while (destinationKeys.hasNext()) {
                            String destinationKey = (String) destinationKeys.next();
                            JSONArray RoomObj = response.getJSONObject("payload").getJSONArray(destinationKey);
                            // destinationValue = destobj.getString("name");
                            Log.d("Room_Type", "" + RoomObj.length());
                            for (int inc = 0; inc < RoomObj.length(); inc++) {
                                Log.d("Room_Type", "Test" + RoomObj.getJSONObject(inc).getString("Hotel_Room_Id"));
                                value[inc] = RoomObj.getJSONObject(inc).getInt("Hotel_Room_Id");

                            //    String url_checkroom = "http://stage.itraveller.com/backend/api/v1/roomtariff?region="+ Region_ID +"&room=" + value[inc] + "&checkInDate=" + checkinDate;
                                String url_checkroom=Constants.API_HotelActivity_Checkroom+ Region_ID +"&room=" + value[inc] + "&checkInDate=" + checkinDate;

                                hotelRoomsCheck(url_checkroom, RoomObj.length(), inc);
                                second.setVisibility(View.VISIBLE);
                                activites.setVisibility(View.GONE);
                                lv1.setVisibility(View.GONE);
                            }
                        }

                        /// /JSONObject jsonarr1 =
                        //Log.d("Room_Type", "" + jsonarr.);
                    }

                } catch (JSONException e) {
                    Log.d("Error Catched", "" + e.getMessage());
                    Toast.makeText(HotelActivity.this, "No rooms available" ,Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.err.println(error);
                // Handle your error types accordingly.For Timeout & No connection error, you can show 'retry' button.
                // For AuthFailure, you can re login with user credentials.
                // For ClientError, 400 & 401, Errors happening on client side when sending api request.
                // In this case you can check how client is forming the api and debug accordingly.
                // For ServerError 5xx, you can do retry or handle accordingly.
                if( error instanceof NetworkError) {

                    Toast.makeText(HotelActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                } else if( error instanceof ServerError) {
                } else if( error instanceof AuthFailureError) {
                } else if( error instanceof ParseError) {
                } else if( error instanceof NoConnectionError) {

                    Toast.makeText(HotelActivity.this, "No Internet Connection" ,Toast.LENGTH_LONG).show();
                } else if( error instanceof TimeoutError) {
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


    public void hotelRoomsCheck(String url, final int totalcount, final int index) {
        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("Boolean", "" + response.getBoolean("success"));
                    Log.d("Error", "" + response.getJSONObject("error"));
                    Log.d("Payload_RoomRate", "" + response.getJSONArray("payload").length());
                    int flag_bit = 0;
                    for (int i = 0; i < response.getJSONArray("payload").length(); i++) {
                        JSONObject jsonarr = response.getJSONArray("payload").getJSONObject(i);
                        HotelRoomModel hrm = new HotelRoomModel();
                        if(flag_bit == 0){
                        for(int index = 0;index < lowesthotelList.size();index++) {
                            hrm.setHotel_Room_Id(jsonarr.getInt("Hotel_Room_Id"));
                            hrm.setHotel_Id(jsonarr.getInt("Hotel_Id"));
                            hrm.setRoom_Status(jsonarr.getInt("Room_Status"));
                            hrm.setRack_Rate(jsonarr.getInt("Rack_Rate"));
                            hrm.setDefault_Number(jsonarr.getInt("Default_Number"));
                            hrm.setMaximum_Number(jsonarr.getInt("Maximum_Number"));
                            hrm.setHotel_Room_Tariff_Id(jsonarr.getInt("Hotel_Room_Tariff_Id"));
                            hrm.setTAC(jsonarr.getInt("TAC"));
                            hrm.setCost(jsonarr.getInt("Cost"));
                            hrm.setMark_Up(jsonarr.getInt("Mark_Up"));
                            hrm.setDisplay_Tariff(jsonarr.getInt("Display_Tariff"));
                            hrm.setCompany_Id(jsonarr.getInt("Company_Id"));
                            hrm.setRoom_Type(jsonarr.getString("Room_Type"));
                            hrm.setRoom_Description(jsonarr.getString("Room_Description"));
                            hrm.setFrom(jsonarr.getString("From"));
                            hrm.setTo(jsonarr.getString("To"));
                            String[] value = lowesthotelList.get(index).trim().split(",");
                            if (Integer.parseInt("" + value[0]) == jsonarr.getInt("Hotel_Id")) {
                                if (Integer.parseInt("" + value[1]) == jsonarr.getInt("Hotel_Room_Id")) {
                                    hrm.setCheck(true);
                                    flag_bit = 1;
                                    hotelcheckbit = true;
                                    HiddenLayoutMealPlan(true);
                                }
                                else{
                                    //hrm.setCheck(false);
                                }
                            }
                            else{
                                hotelcheckbit = false;
                                HiddenLayoutMealPlan(false);
                            }
                        }
                        }
                        roomList.add(hrm);

                        Collections.sort(roomList, new PriceComparison());

                        flag_bit =0;
                    }
                   // if(index == (totalcount-1)) {
                        if(roomList.size()== 0){
                            Log.d("Error_Catched", "No Rooms" );
                   //     }
                            }
                       Utility.setListViewHeightBasedOnChildren(listView);
                        adapter.notifyDataSetChanged();
                        //com.itraveller.constant.Utility.setListViewHeightBasedOnChildren(listView);}


                } catch (JSONException e) {
                    Log.d("Error Catched", "" + e.getMessage());
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.d("Volley Error", "Error: " + error.networkResponse);

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }

    public void DefaultHotelRoomSet(String url, final int depth) {
        Log.d("LowestHotelURL", "" + url);

        if(depth==0){
        pDialog.show();}
        else{
            pDialog.hide();
        }
        final JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("Boolean", "" + response.getBoolean("success"));
                    Log.d("Error", "" + response.getJSONObject("error"));
                    Log.d("Payload_RoomRate", "" + response.getJSONObject("payload"));

                    //for (int i = 0; i < response.getJSONObject("payload").length(); i++) {
                    if(response.getJSONObject("payload").length()==0){
                        Log.i("DefaultData","No Lowest hotel :" + depth);
                    }

                        JSONObject jsonarr = response.getJSONObject("payload");
                        Log.i("DefaultData",""+ jsonarr.getString("Hotel_Id") + "," + jsonarr.getString("Hotel_Room_Id") + "," + jsonarr.getString("Display_Tariff") +",1");

                    //here no of rooms add defaul
                    lowesthotelList.add(jsonarr.getString("Hotel_Id") + "," + jsonarr.getString("Hotel_Room_Id") + "," + jsonarr.getString("Display_Tariff") +",1" );
                        //roomList.add();
                    //}
                   // if(depth==(hotel_destination.length-1))
                   {
                        listViewPagerAdapter = new ListViewPagerAdapter(HotelActivity.this, hotelList, lowesthotelList, new pagerCheckBoxChangedListner1() {
                            @Override
                            public void OnCheckedChangeListenerCustomPager(int childPosition, boolean isChecked) {
                            }

                            @Override
                            public void OnImageClickListenerCustomPager(final int childpostion, final int groupPosition) {
                                chk_lunch.setChecked(false);
                                chk_dinner.setChecked(false);

                                HiddenLayoutMealPlan(true);
                                cposition = childpostion;
                                gposition = groupPosition;

                                mealplanList = new ArrayList<MealPlanModel>();

                                roomList = new ArrayList<HotelRoomModel>();
                                listView = (ListView) findViewById(R.id.room_type);
                                listView.setAdapter(null);
                                Utility.setListViewHeightBasedOnChildren(listView);
                                final ArrayList<HotelModel> modelRow = ListViewPagerAdapter.mHotelModels.get("" + groupPosition);
                                if (modelRow.get(childpostion).getHotel_Id() == 0) {
                                    for (int index = 0; index < modelRow.size(); index++) {
                                        modelRow.get(index).setChecked(false);
                                    }
                                    modelRow.get(childpostion).setChecked(true);
                                    ListViewPagerAdapter.mViewPagerAdapter.notifyDataSetChanged();
                                    listViewPagerAdapter.notifyDataSetChanged();
                                    /*if(modelRow.get(childpostion).isChecked()){
                                        hotelcheckbit = true;
                                    }
                                    else{
                                        hotelcheckbit = false;
                                    }*/


                                /*String meal_plan = modelRow.get(childpostion).getHotel_Meal_Plan();
                                String[] meal_plan_data = meal_plan.split(",");
                                if()
                                            if(modelRow.get(childpostion).getLunch()!=0)
                                    {
                                        chk_lunch.setChecked(true);
                                    }
                                    if(modelRow.get(childpostion).getDinner()!=0)
                                    {
                                        chk_dinner.setChecked(true);
                                    }*/

                               /* chk_lunch.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Toast.makeText(HotelActivity.this, "Please select the room", Toast.LENGTH_LONG).show();

                                        if (!modelRow.get(childpostion).isChecked()) {
                                            Toast.makeText(HotelActivity.this, "Please select the room", Toast.LENGTH_LONG).show();
                                        }
                                           *//* if(chk_lunch.isChecked())
                                                modelRow.get(childpostion).setLunch(1);
                                            else
                                                modelRow.get(childpostion).setLunch(0);*//*
                                    }
                                });*/
                            }
                                else {

                                    MealPlan("http://stage.itraveller.com/backend/api/v1/hotelinclusion?hotel=" + modelRow.get(childpostion).getHotel_Id() + "&region=" + Region_ID + "&checkInDate=" + destination_date[groupPosition], groupPosition);
                                    Log.i("MealPlanURL", "" + "http://stage.itraveller.com/backend/api/v1/hotelinclusion?hotel=" + modelRow.get(childpostion).getHotel_Id() + "&region=" + Region_ID + "&checkInDate=" + destination_date[groupPosition]);
                                    adapter = new HotelRoomAdapter(HotelActivity.this, roomList, lowesthotelList, groupPosition, new RadiobuttonListener() {
                                        @Override
                                        public void RadioChangeListenerCustom(String Value) {
                                            HiddenLayoutMealPlan(true);
                                            hotelcheckbit = true;
                                            Log.i("TestPostion", "" + Value);
                                            Log.i("TestPostionGroup", "" + groupPosition);
                                            lowesthotelList.set(groupPosition, "" + Value);
                                            HotelRoomData[gposition] = Value;
                                            final ArrayList<HotelModel> modelRow = ListViewPagerAdapter.mHotelModels.get("" + gposition);
                                            for (int index = 0; index < modelRow.size(); index++) {
                                                if (index == cposition) {
                                                    modelRow.get(cposition).setChecked(true);
                                                } else {
                                                    modelRow.get(index).setChecked(false);
                                                }
                                            }
                                            SaveData();
                                            ListViewPagerAdapter.mViewPagerAdapter.notifyDataSetChanged();
                                            listViewPagerAdapter.notifyDataSetChanged();
                                            //notifyAll();
                                        }


                                    });

                                    listView.setAdapter(adapter);
                                    Utility.setListViewHeightBasedOnChildren(listView);
                                    //ArrayList<HotelModel> modelRow = ListViewPagerAdapter.mHotelModels.get("" + groupPosition);
                                    //modelRow.get(childpostion).
                                    Log.i("PagerView Clicked", groupPosition + "Clicked" + childpostion + " Check " + modelRow.get(childpostion).getHotel_Name());
                                    //    String url = "http://stage.itraveller.com/backend/api/v1/hotelRoom?regionId="+Region_ID+"&hotelIds=["+ modelRow.get(childpostion).getHotel_Id() +"]&checkInDate=" + destination_date[groupPosition];
                                    String url = Constants.API_HotelActivity_HOTEL_ROOMS + Region_ID + "&hotelIds=" + modelRow.get(childpostion).getHotel_Id() + "&checkInDate=" + destination_date[groupPosition];
                                    Log.i("URLForRooms", "" + groupPosition + " Url :" + url);
                                    hotelRooms(url, destination_date[groupPosition]);
                                }
                                check_bit=1;
                            }
                        });

                        pDialog.hide();
                        activites.setEnabled(true);
                        // listViewPagerAdapter.add(null);
                        lv1.setAdapter(listViewPagerAdapter);

                    }
                } catch (JSONException e) {
                    Log.d("Error Catched", "" + e.getMessage());
                    Log.i("DefaultData", "No Lowest hotel :" + depth);

                }
                catch (Exception e){
                    Log.d("Error Catched", "" + e.getMessage());
                    Log.i("DefaultData", "No Lowest hotel :" + depth);

                }

            }
        },  new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("DefaultData", "No Lowest hotel :" + depth);
                error_bit = 1;
                pDialog.hide();
                activites.setEnabled(true);
                // listViewPagerAdapter.add(null);
                listViewPagerAdapter = new ListViewPagerAdapter(HotelActivity.this, hotelList, lowesthotelList, new pagerCheckBoxChangedListner1() {
                    @Override
                    public void OnCheckedChangeListenerCustomPager(int childPosition, boolean isChecked) {
                    }

                    @Override
                    public void OnImageClickListenerCustomPager(final int childpostion, final int groupPosition) {
                        cposition = childpostion;
                        gposition = groupPosition;
                        final ArrayList<HotelModel> modelRow = ListViewPagerAdapter.mHotelModels.get("" + groupPosition);

                        second.setVisibility(View.VISIBLE);
                        activites.setVisibility(View.GONE);
                        lv1.setVisibility(View.GONE);
                        roomList = new ArrayList<HotelRoomModel>();
                        listView = (ListView) findViewById(R.id.room_type);
                        Utility.setListViewHeightBasedOnChildren(listView);
                                /*String meal_plan = modelRow.get(childpostion).getHotel_Meal_Plan();
                                String[] meal_plan_data = meal_plan.split(",");
                                if()
                                            if(modelRow.get(childpostion).getLunch()!=0)
                                    {
                                        chk_lunch.setChecked(true);
                                    }
                                    if(modelRow.get(childpostion).getDinner()!=0)
                                    {
                                        chk_dinner.setChecked(true);
                                    }
                                    chk_lunch.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if(chk_lunch.isChecked())
                                                modelRow.get(childpostion).setLunch(1);
                                            else
                                                modelRow.get(childpostion).setLunch(0);
                                        }
                                });
                                chk_dinner.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if(chk_dinner.isChecked())
                                            modelRow.get(childpostion).setDinner(1);
                                        else
                                            modelRow.get(childpostion).setDinner(0);
                                    }
                                });*/
                        adapter = new HotelRoomAdapter(HotelActivity.this, roomList, lowesthotelList,groupPosition, new RadiobuttonListener() {
                            @Override
                            public void RadioChangeListenerCustom(String Value) {


                                Log.i("TestPostion",""+ Value);
                                Log.i("TestPostionGroup",""+groupPosition);
                                lowesthotelList.set(groupPosition,""+Value);
                                HotelRoomData[gposition] = Value;
                                final ArrayList<HotelModel> modelRow = ListViewPagerAdapter.mHotelModels.get("" + gposition);
                                for(int index =0 ; index<modelRow.size();index++) {
                                    if(index==cposition) {
                                        modelRow.get(cposition).setChecked(true);
                                    }
                                    else {
                                        modelRow.get(index).setChecked(false);
                                    }
                                }
                                ListViewPagerAdapter.mViewPagerAdapter.notifyDataSetChanged();
                                listViewPagerAdapter.notifyDataSetChanged();
                                //notifyAll();
                            }


                        });
                        listView.setAdapter(adapter);
                        Utility.setListViewHeightBasedOnChildren(listView);
                        //ArrayList<HotelModel> modelRow = ListViewPagerAdapter.mHotelModels.get("" + groupPosition);
                        //modelRow.get(childpostion).
                        Log.i("PagerView Clicked", groupPosition + "Clicked" + childpostion + " Check " + modelRow.get(childpostion).getHotel_Name());
                        //String url = "http://stage.itraveller.com/backend/api/v1/hotelRoom?regionId="+Region_ID+"&hotelIds=["+ modelRow.get(childpostion).getHotel_Id() +"]&checkInDate=" + destination_date[groupPosition];
                        String url=Constants.API_HotelActivity_HOTEL_ROOMS+Region_ID+"&hotelIds="+ modelRow.get(childpostion).getHotel_Id() +"&checkInDate=" + destination_date[groupPosition];
                        Log.i("URLForRooms", "" + groupPosition + " Url :" + url);
                        hotelRooms(url, destination_date[groupPosition]);
                        check_bit=1;
                    }
                });

                lv1.setAdapter(listViewPagerAdapter);
                //System.err.println(error);
                // Handle your error types accordingly.For Timeout & No connection error, you can show 'retry' button.
                // For AuthFailure, you can re login with user credentials.
                // For ClientError, 400 & 401, Errors happening on client side when sending api request.
                // In this case you can check how client is forming the api and debug accordingly.
                // For ServerError 5xx, you can do retry or handle accordingly.
                if( error instanceof NetworkError) {
                    pDialog.hide();
                    Toast.makeText(HotelActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                } else if( error instanceof ServerError) {
                    //Toast.makeText(HotelActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                } else if( error instanceof AuthFailureError) {
                } else if( error instanceof ParseError) {
                } else if( error instanceof NoConnectionError) {
                    pDialog.hide();
                    Toast.makeText(HotelActivity.this, "No Internet Connection" ,Toast.LENGTH_LONG).show();
                } else if( error instanceof TimeoutError) {
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

    class PriceComparison implements Comparator<HotelRoomModel> {

        @Override
        public int compare(HotelRoomModel o1,HotelRoomModel o2) {
            if(o1.getCost() > o2.getCost()){
                return 1;
            } else {
                return -1;
            }
        }

    }

    public void onBackPressed() {

        if(check_bit==0)
        {
            finish();
        }
        else
        {
           lv1.setVisibility(View.VISIBLE);
          activites.setVisibility(View.VISIBLE);
            second.setVisibility(View.GONE);
            check_bit=0;
        }
    }

    public void MealPlan(String url, final int index) {

        Log.d("IndexCount",""+index);
        //String url = "http://stage.itraveller.com/backend/api/v1/hotelinclusion?hotel=7&region=7&checkInDate=2015-10-10";
        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("Boolean", "" + response.getBoolean("success"));
                    Log.d("Error", "" + response.getJSONObject("error"));
                    Log.d("Payload", "" + response.getJSONArray("payload"));
                    // JSONObject jsonobj = response.getJSONObject("payload").getJSONObject()
                    // Parsing json
                    if(response.getJSONArray("payload").length() != 0) {
                        for (int i = 0; i < response.getJSONArray("payload").length(); i++) {
                            JSONObject jsonObj = response.getJSONArray("payload").getJSONObject(i);
                            MealPlanModel mealplan = new MealPlanModel();
                            mealplan.setId(jsonObj.getInt("Id"));
                            mealplan.setCompany_Id(jsonObj.getInt("Company_Id"));
                            mealplan.setHotel_Id(jsonObj.getInt("Hotel_Id"));
                            mealplan.setHotel_Room_Id(jsonObj.getInt("Hotel_Room_Id"));
                            mealplan.setBreakfast(jsonObj.getInt("Breakfast"));
                            mealplan.setLunch(jsonObj.getInt("Lunch"));
                            mealplan.setDinner(jsonObj.getInt("Dinner"));
                            mealplan.setAdult_With_Bed(jsonObj.getInt("Adult_With_Bed"));
                            mealplan.setAdult_Without_Bed(jsonObj.getInt("Adult_Without_Bed"));
                            mealplan.setChild_With_Bed(jsonObj.getInt("Child_With_Bed"));
                            mealplan.setChild_Without_Bed(jsonObj.getInt("Child_Without_Bed"));
                            mealplan.setChild_Below_Five(jsonObj.getInt("Child_Below_Five"));
                            mealplan.setMarkup(jsonObj.getInt("Markup"));
                            mealplan.setFrom_Date(jsonObj.getString("From_Date"));
                            mealplan.setTo_Date(jsonObj.getString("To_Date"));

                           /* if(index == -1){
                            mealplanList.add(mealplan);}
                            else{
                                mealplanList.add(index,mealplan);*/
                            mealplanList.add(mealplan);
                                if (jsonObj.getInt("Lunch") == 0) {
                                    chk_lunch.setEnabled(false);
                                }
                                else{
                                    chk_lunch.setEnabled(true);
                                }
                                if (jsonObj.getInt("Dinner") == 0) {
                                    chk_dinner.setEnabled(false);
                                }
                                else{
                                    chk_dinner.setEnabled(true);
                                }
                            //}
                        }
                    }
                    else{

                        MealPlanModel mealplan = new MealPlanModel();
                        mealplan.setId(0);
                        mealplan.setCompany_Id(0);
                        mealplan.setHotel_Id(0);
                        mealplan.setHotel_Room_Id(0);
                        mealplan.setBreakfast(0);
                        mealplan.setLunch(0);
                        mealplan.setDinner(0);
                        mealplan.setAdult_With_Bed(0);
                        mealplan.setAdult_Without_Bed(0);
                        mealplan.setChild_With_Bed(0);
                        mealplan.setChild_Without_Bed(0);
                        mealplan.setChild_Below_Five(0);
                        mealplan.setMarkup(0);
                        mealplan.setFrom_Date("0");
                        mealplan.setTo_Date("0");
                        /*if(index == -1){
                            mealplanList.add(mealplan);
                          }
                        else{*/
                            //mealplanList.add(index,mealplan);
                        mealplanList.add(mealplan);
                            chk_lunch.setEnabled(false);
                            chk_dinner.setEnabled(false);
                        //}
                        }
                } catch (JSONException e) {
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
                if( error instanceof NetworkError) {
                    Toast.makeText(HotelActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                } else if( error instanceof ServerError) {
                } else if( error instanceof AuthFailureError) {
                } else if( error instanceof ParseError) {
                } else if( error instanceof NoConnectionError) {
                    Toast.makeText(HotelActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                } else if (error instanceof TimeoutError) {
                }
            }
        }) {
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(10000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }


    public void SaveData(){
        Set<String> set = new HashSet<String>();
        String hotel_string_main = "";
        String itinerary_hotel = "";

        for(int i =0; i< lowesthotelList.size();i++)
        {
            String hotel_string = "";
            String[] hotel_room_Data = lowesthotelList.get(i).trim().split(",");

            for(int j = 0 ;j < lowesthotelList.size(); j++) {
                ArrayList<HotelModel> modelRow = ListViewPagerAdapter.mHotelModels.get("" + j);
                for(int k = 0;k< modelRow.size();k++) {
                    if (modelRow.get(k).getHotel_Id() == Integer.parseInt(hotel_room_Data[0])) {
                        hotel_string = ""+ modelRow.get(k).getHotel_Name() + "," +  modelRow.get(k).getHotel_Description() + "," +  modelRow.get(k).getHotel_Id() + "," + modelRow.get(k).getLunch() + "," + modelRow.get(k).getDinner();
                    }
                }
            }

            Log.d("Lowest_Value",""+ lowesthotelList.get(i));
            //if(datas.equalsIgnoreCase("null")) {
            set.add("" + lowesthotelList.get(i));
            if(i == 0) {
                hotel_string_main = "" + hotel_string;
                itinerary_hotel = "" + lowesthotelList.get(i);
            }
            else{
                hotel_string_main = hotel_string_main + "-" + hotel_string;
                itinerary_hotel = itinerary_hotel + "-" + lowesthotelList.get(i);
            }

        }
        SharedPreferences sharedpreferences = getSharedPreferences("Itinerary", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("Hotels", hotel_string_main);
        editor.putString("HotelRooms", itinerary_hotel);
        editor.putString("ItineraryHotelRooms", itinerary_hotel);
        editor.commit();

    }

}
