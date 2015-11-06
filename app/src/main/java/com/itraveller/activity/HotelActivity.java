package com.itraveller.activity;

/**
 * Created by VNK on 6/25/2015.
 */

import android.app.ProgressDialog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.NonNull;
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

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;

import com.android.volley.toolbox.NetworkImageView;
import com.itraveller.R;

import com.itraveller.adapter.HotelRoomAdapter;
import com.itraveller.adapter.ListViewPagerAdapter;

import com.itraveller.adapter.TransportationAdapter;
import com.itraveller.constant.Constants;

import com.itraveller.constant.Utility;
import com.itraveller.dragsort.DragAndSort;
import com.itraveller.model.ActivitiesModel;
import com.itraveller.model.HotelModel;
import com.itraveller.model.HotelRoomModel;

import com.itraveller.model.MealPlanModel;
import com.itraveller.model.TransportationModel;
import com.itraveller.volley.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Arrays;
import java.util.Calendar;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class  HotelActivity extends ActionBarActivity {

    ProgressDialog ndDialog;
    public static int count=0;
    public static int transportation_cost;
    ArrayList<DiscountModel> Discount_list=new ArrayList<DiscountModel>();
    String discount_str="";
    String supplier_str="";
    String default_activity_cost_str="";
    String default_activity_id_str="";
    static String date_of_travel,date_of_return,source_str,arr_port,dept_port,dest_str;
    static String itinerary_id,no_of_adults,no_below_5,no_above_5;
    static String dest_id,no_of_nights,trans_id,activity_id;
    static String no_of_dests,activity_date,no_of_activities;


    public static String region_id;
    public static String destination_id_arr[];
    public static String destination_date_arr[];
    public static String Destination_Value;
    public static String Destination_Date;
    public static int sum;
    public static int activity_sum;
    public static int flag=0;
    public int totalPersons = 0;

    private TransportationAdapter tra_adapter;
    public static List<TransportationModel> transportationList = new ArrayList<TransportationModel>();


    String[] destination_id, hotel_id,hotel_room_id;

    SharedPreferences preferences,post_prefs;
    public SharedPreferences prefs;
    SharedPreferences prfs;
    SharedPreferences user_selected_data;
//    int destination_id,hotel_id;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    TextView nameText,placesText,destinationText,arr_dateText,dep_dateText,daysText,adultsText,child_5_12_Text,child_below_5_Text;
    TextView nameSellerText,addressSellerText,arrAtText,dateDisplayText,roomDisplayText,totalPriceText;
    TextView discountPriceText,priceAdvanceText,remainingPriceText,departureText,transportationText;
    // Declare Variable
    public static ListViewPagerAdapter listViewPagerAdapter;
    int ListItemPostion;
    int heightHotelList = 5;
    Toolbar mToolbar;
    int error_bit = 0;
    private ArrayList<String> hotelList;
    private ArrayList<String> mealPlanURL;
    private ArrayList<String> lowesthotelList;
    String[] hotel_destination, destination_date, pre_saved_hotel_destination_id;
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
    int Checkgpostion;
    String IDS_value;

    SharedPreferences sharedpreferences;

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


        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        setupDrawer();

        tra_adapter=new TransportationAdapter(this,transportationList);

        mealplanList = new ArrayList<MealPlanModel>();
        sharedpreferences = getSharedPreferences("Itinerary", Context.MODE_PRIVATE);

        region_id=""+sharedpreferences.getString("RegionID",null);

        Bundle bundle = getIntent().getExtras();

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
        departureText=(TextView) findViewById(R.id.departure_from_text_value);
        transportationText=(TextView) findViewById(R.id.transportation_text_value);

        preferences=getSharedPreferences("Preferences",MODE_PRIVATE);
        prefs=getSharedPreferences("Itinerary",MODE_PRIVATE);
        post_prefs=getSharedPreferences("PostData",MODE_PRIVATE);

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
        totalPersons = Integer.parseInt(prefs.getString("Adults", null)) + Integer.parseInt( prefs.getString("Children_12_5", null));
        child_5_12_Text.setText("" + prefs.getString("Children_12_5", null));
        child_below_5_Text.setText("" + prefs.getString("Children_5_2", null));
        totalPriceText.setText("");
        discountPriceText.setText("");
        remainingPriceText.setText("");
        priceAdvanceText.setText("");
        nameSellerText.setText("");
        addressSellerText.setText("");
        arrAtText.setText(prefs.getString("ArrivalPortString",null));
        dateDisplayText.setText("");
        roomDisplayText.setText("");
        departureText.setText(prefs.getString("DeparturePortString",null));
        transportationText.setText("");


        dest_id=""+prefs.getString("DestinationID",null);
        no_of_dests=""+dest_id.split(",").length;
        itinerary_id=""+post_prefs.getInt("ItineraryId", 0);
        date_of_travel=""+preferences.getString("Date_start_str", null);
        date_of_return=""+preferences.getString("Date_end_str", null);
        no_of_nights="" + prefs.getInt("Duration", 0);
        no_of_adults="" + prefs.getString("Adults", null);
        no_above_5="" + prefs.getString("Children_12_5", null);
        no_below_5="" + prefs.getString("Children_5_2", null);
        arr_port=prefs.getString("ArrivalPortString",null);
        dept_port=prefs.getString("DeparturePortString",null);
        source_str=prefs.getString("ArrivalAirportString",null);
        dest_str=prefs.getString("DepartureAirportString",null);

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
                    if(modelRow.size()!=0 && mealplanList.size() != 0) {
                        if (modelRow.get(cposition).getHotel_Id() == mealplanList.get(0).getHotel_Id())
                            modelRow.get(cposition).setLunch(Integer.parseInt("" + mealplanList.get(0).getLunch()));
                    }
                }
                else{
                    final ArrayList<HotelModel> modelRow = ListViewPagerAdapter.mHotelModels.get("" + gposition);
                    if(modelRow.size()!=0 && mealplanList.size() != 0) {
                        if (modelRow.get(cposition).getHotel_Id() == mealplanList.get(0).getHotel_Id())
                            modelRow.get(cposition).setLunch(Integer.parseInt("0"));
                    }
                }
                SaveData();

            }
        });

        chk_dinner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                /* if (!hotelcheckbit) {
                    Toast.makeText(HotelActivity.this, "Please select the room", Toast.LENGTH_LONG).show();
                }*/
                Log.e("Lowest_Value1",""+gposition);
                if (b) {
                    final ArrayList<HotelModel> modelRow = ListViewPagerAdapter.mHotelModels.get("" + gposition);
                    if(modelRow.size()!=0 && mealplanList.size() != 0) {
                        if (modelRow.get(cposition).getHotel_Id() == mealplanList.get(0).getHotel_Id())
                            modelRow.get(cposition).setDinner(Integer.parseInt("" + mealplanList.get(0).getDinner()));
                    }
                } else {
                    final ArrayList<HotelModel> modelRow = ListViewPagerAdapter.mHotelModels.get("" + gposition);
                    if(modelRow.size()!=0 && mealplanList.size() != 0) {
                        if (modelRow.get(cposition).getHotel_Id() == mealplanList.get(0).getHotel_Id())
                            modelRow.get(cposition).setDinner(Integer.parseInt("0"));
                    }
                }
                SaveData();
            }
        });

        pDialog = new ProgressDialog(HotelActivity.this);
        pDialog.setMessage("Loading...");

        ndDialog=new ProgressDialog(HotelActivity.this);

        IDS_value = bundle.getString("DestinationsIDs");
        prfs = getSharedPreferences("Itinerary", Context.MODE_PRIVATE);
        String Date_value = prfs.getString("DestinationDate", "");
        Region_ID= prfs.getString("RegionID", "");

        //Destination Count
    //    Log.d("Hotel cost test",""+prfs.getInt("Total_Hotel_Cost",0));

        SharedPreferences prfs1 = getSharedPreferences("SavedData", Context.MODE_PRIVATE);
        String preSavedHotelDestinationID = prfs1.getString("HotelDestinationID", "");

        pre_saved_hotel_destination_id = preSavedHotelDestinationID.trim().split(",");



        hotel_destination = IDS_value.trim().split(",");
        destination_date = Date_value.trim().split(",");
        HotelRoomData = new String[hotel_destination.length];
        second = (LinearLayout) findViewById(R.id.room_type_full);
        lv1 = (ListView) findViewById(R.id.campaignListView);
        // lv1.setAdapter(new ArrayAdapter<String>(getActivity(),
        // android.R.layout.simple_list_item_1,aList));




        user_selected_data=getSharedPreferences("User Selected Data",MODE_PRIVATE);

        SharedPreferences.Editor editor1=user_selected_data.edit();
        Log.d("Count first test",""+user_selected_data.getInt("count",0));
        if((String.valueOf(""+user_selected_data.getInt("count",0))).equals(null) || user_selected_data.getInt("count",0)==0 )
        {
            editor1.putInt("count",1);
            editor1.commit();
        }

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
                    Log.d("Hotel test14",""+lowesthotelList.get(i));
                    if(i == 0) {
                        hotel_string_main = "" + hotel_string;
                        itinerary_hotel = "" + lowesthotelList.get(i);
                        Log.d("Test test1",""+itinerary_hotel);
                    }
                    else{
                        hotel_string_main = hotel_string_main + "-" + hotel_string;
                        itinerary_hotel = itinerary_hotel + "-" + lowesthotelList.get(i);
                        Log.d("Test test2",""+itinerary_hotel);
                    }

                }

                    /*}
                    else{
                    set.add("" + HotelRoomData[i]);}*/
                    //Log.i("Hotel Room 123" + i, "" + set.toArray()[i]);
                editor.putString("Hotels", hotel_string_main);
                Log.d("Hotel test11", "" + hotel_string_main);
                editor.putString("HotelRooms", itinerary_hotel);
                Log.d("Hotel test12", "" + itinerary_hotel);
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


    public void CalculateDiscount()
    {
        Log.d("URL test test21", "" + discount_str);

        Log.d("URL test test45", "" + sum);
        Log.d("URL test test24", "" + Discount_list.size());
        for(int i=0;i<Discount_list.size();i++)
        Log.d("URL test test23",""+Discount_list.get(i).getCompany_ID()+"  "+Discount_list.get(i).getCompany_Discount()+" "+Discount_list.get(i).getCompany_Name());

        int discount_val=Integer.parseInt("" + Discount_list.get(0).getCompany_Discount());
        int discount_in_Rs=(sum*discount_val)/100;

        discountPriceText.setText("\u20B9 "+""+discount_in_Rs);
    //    discountPriceText.setText("" + Discount_list.get(0).getCompany_Discount() + " %");

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
                        Toast.makeText(HotelActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    } else if (error instanceof ServerError) {
                    } else if (error instanceof AuthFailureError) {
                    } else if (error instanceof ParseError) {
                    } else if (error instanceof NoConnectionError) {
                        // pDialog.hide();
                        Toast.makeText(HotelActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
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

    public void getSupplierDetails()
    {
        Log.d("URL test test10","hi");
        String url="http://m.itraveller.com/api/v1/supplier?region="+region_id;
        int i;
        Log.d("URL test test11", "" + url + "hii " + destination_date_arr.length);
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
                        Toast.makeText(HotelActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    } else if (error instanceof ServerError) {
                    } else if (error instanceof AuthFailureError) {
                    } else if (error instanceof ParseError) {
                    } else if (error instanceof NoConnectionError) {
                        // pDialog.hide();
                        Toast.makeText(HotelActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
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


    public void getDefaultActivityData()
    {

        Log.d("Welcome","ActivityEntry");
        SharedPreferences pref = getSharedPreferences("Itinerary", Context.MODE_PRIVATE);

        String No_of_Nights_arr[]=(""+pref.getString("No_of_nights_dest",null)).split(",");

        String Hotel_ID_arr[]=prefs.getString("HotelRooms",null).split("-");

        String Hotel_Data_arr[]=new String[4];

        Log.d("Welcome22", "" + destination_id_arr.length);
        Log.d("Welcome456", "" + prefs.getString("HotelRooms", null));

        for(int i=0;i<Hotel_ID_arr.length;i++) {

            Hotel_Data_arr=Hotel_ID_arr[i].split(",");

            sum+=Integer.parseInt(Hotel_Data_arr[2]);
            Log.d("Cost test test91",""+sum);

            String url ="" + "http://stage.itraveller.com/backend/api/v1/activities?fromDestination=" + destination_id_arr[i] + "&toDestination=" + destination_id_arr[i] + "&regionIds=" + region_id + "&day=" + No_of_Nights_arr[i] + "&hotelId=" + Hotel_Data_arr[0];

            Log.d("Welcome11",""+url);
                    JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET,
                    url, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Log.i("Test", "Testing" + response);
                        Log.d("Boolean", "" + response.getBoolean("success"));
                        Log.d("Error", "" + response.getJSONObject("error"));
                        Log.d("Payload", "" + response.getJSONArray("payload"));

                        // JSONObject jsonobj = response.getJSONObject("payload").get;
                        // Parsing json
                        ArrayList activitiesList = new ArrayList();
                        for (int i = 0; i < response.getJSONArray("payload").length(); i++) {
                            JSONObject jsonarr = response.getJSONArray("payload").getJSONObject(i);

                            ActivitiesModel activities_model = new ActivitiesModel();

                            activities_model.setId(jsonarr.getInt("Id"));
                            activities_model.setTitle(jsonarr.getString("Title"));
                            activities_model.setCost(jsonarr.getInt("Cost"));
                            Log.d("Cost test test", "" + jsonarr.getInt("Cost"));
                            activities_model.setHotel_Id(jsonarr.getString("Hotel_Id"));
                            activities_model.setMarkup(jsonarr.getInt("Markup"));
                            activities_model.setDisplay(jsonarr.getInt("Display"));
                            activities_model.setStatus(jsonarr.getInt("Status"));
                            activities_model.setRegion_Id(jsonarr.getString("Region_Id"));
                            activities_model.setDestination_Id(jsonarr.getInt("Destination_Id"));
                            activities_model.setCompany_Id(jsonarr.getString("Company_Id"));
                            activities_model.setDay(jsonarr.getString("Day"));
                            activities_model.setDuration(jsonarr.getString("Duration"));
                            activities_model.setImage(jsonarr.getString("Image"));
                            activities_model.setFlag(jsonarr.getInt("Flag"));
                            Log.d("Cost test test222", "" + jsonarr.getInt("Flag"));
                            if (jsonarr.getInt("Flag") == 1) {
                                sum+=jsonarr.getInt("Cost");
                                Log.d("Cost test test235", "" + sum);
                                activity_sum+=jsonarr.getInt("Cost");
                                Log.d("Cost test test234", "" +activity_sum);

                                default_activity_cost_str += "" + jsonarr.getInt("Cost") + ",";
                                default_activity_id_str+= ""+jsonarr.getInt("Id")+",";
                            }
                            activities_model.setDescription(jsonarr.getString("Description"));
                            activities_model.setNot_Available_Month(jsonarr.getString("Not_Available_Month"));
                            activities_model.setNot_Available_Days(jsonarr.getString("Not_Available_Days"));
                            activities_model.setDestination_Id_From(jsonarr.getString("Destination_Id_From"));
                            activities_model.setBookable(jsonarr.getString("Bookable"));
                            activities_model.setChecked(false);


                            //Log.i("JSON_Activities",""+act.getJSONArray("activities"));

                            if (response.getJSONArray("payload").length() != 0)
                                activitiesList.add(activities_model);
                        }

                        getDefaultTransportationCost();

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
                        Toast.makeText(HotelActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    } else if (error instanceof ServerError) {
                    } else if (error instanceof AuthFailureError) {
                    } else if (error instanceof ParseError) {
                    } else if (error instanceof NoConnectionError) {
                        // pDialog.hide();
                        Toast.makeText(HotelActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    } else if (error instanceof TimeoutError) {
                    }
                }
            }) {
            };
        /*strReq.setRetryPolicy(new DefaultRetryPolicy(10000,
                5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
            AppController.getInstance().addToRequestQueue(strReq);

            default_activity_cost_str+="-";
            default_activity_id_str+="-";
        }
        SharedPreferences.Editor editor=preferences.edit();
        editor.putInt("ActivityCost", activity_sum);
        editor.commit();
    }

    public void getUserSelectedHotelData()
    {
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
                        hotel_string = ""+ modelRow.get(k).getHotel_Name() + "," +  modelRow.get(k).getHotel_Description() + "," +  modelRow.get(k).getHotel_Id() + "," + modelRow.get(k).getLunch() + "," + modelRow.get(k).getDinner() +","+Utility.noRooms(3, totalPersons)+",0,0";
                               /* else
                              hotel_string = ""+hotel_string + "-" + modelRow.get(k).getHotel_Name() + "," +  modelRow.get(k).getHotel_Description() + "," +  modelRow.get(k).getHotel_Id();*/
                    }
                }
            }

            //if(datas.equalsIgnoreCase("null")) {
            set.add("" + lowesthotelList.get(i));
            Log.d("Hotel test14",""+lowesthotelList.get(i));
            if(i == 0) {
                hotel_string_main = "" + hotel_string;
                itinerary_hotel = "" + lowesthotelList.get(i);
            }
            else{
                hotel_string_main = hotel_string_main + "-" + hotel_string;
                itinerary_hotel = itinerary_hotel + "-" + lowesthotelList.get(i);
            }

        }

        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("Hotels", hotel_string_main);
        Log.d("Hotel test11", "" + hotel_string_main);
        editor.putString("HotelRooms", itinerary_hotel);
        Log.d("Hotel test12", "" + itinerary_hotel);
        editor.putString("ItineraryHotelRooms", itinerary_hotel);
        editor.commit();

        getDefaultActivityData();

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

        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("Date_start_str",temp[1]+"-"+temp_month+"-"+temp[3]);
        editor.commit();

        str=temp[0]+", "+temp[1]+" "+month[temp_month-1]+" "+temp[3];
        return str;
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                Destination_Value=prfs.getString("DestinationID",null);
                Destination_Date=prfs.getString("DestinationDate",null);

                String temp_destination_id_arr[]=Destination_Value.split(",");
                String temp_destination_date_arr[]=Destination_Date.split(",");

                destination_id_arr=new String[temp_destination_id_arr.length];
                destination_date_arr=new String[temp_destination_date_arr.length];

                //Log.d("Destination date test111", "" + Destination_Date);


                destination_id_arr=Destination_Value.split(",");
                destination_date_arr=Destination_Date.split(",");

                Log.d("Destination date te45",""+flag);

                ndDialog.setMessage("Loading...");
                ndDialog.setCancelable(true);
                ndDialog.show();

                getUserSelectedHotelData();

                totalPriceText.setText("Calculating...");
                discountPriceText.setText("Calculating...");
                remainingPriceText.setText("Calculating...");
                priceAdvanceText.setText("Calculating...");
                transportationText.setText("Calculating...");
                nameSellerText.setText("Loading...");
                addressSellerText.setText("Loading...");
                transportationText.setText("Loading...");


                SharedPreferences.Editor editor=post_prefs.edit();
                editor.putInt("Nav_Drawer_Flag",1);
                editor.commit();


                getSupportActionBar().setTitle("Summary Data");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);

                transportationList.clear();
                sum=0;
                count=0;
                transportation_cost=0;
                getSupportActionBar().setTitle("Hotels");
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
        lowesthotelList = new ArrayList<>();
        SharedPreferences.Editor editor1=user_selected_data.edit();




        for (int index = 0; index < hotel_destination.length; index++) {
        //    hotelList.add("http://stage.itraveller.com/backend/api/v1/hotel/destintionId/" + hotel_destination[index]);
            hotelList.add(Constants.API_HotelActivity_HotelList + hotel_destination[index]);
            editor1.putInt("DestinationIDValue" + hotel_destination[index], Integer.parseInt("" + hotel_destination[index]));
            editor1.commit();
            //Default Hotel Set Value
            DefaultHotelRoomSet(lowest_hotel_url + hotel_destination[index] + "&checkInDate=" + destination_date[index] + "&regionId=" + Region_ID, index, hotel_destination[index]);
            MealPlan("http://stage.itraveller.com/backend/api/v1/hotelinclusion?hotel=" + hotel_destination[index] + "&region=" + Region_ID + "&checkInDate=" + destination_date[index], -1);
            Log.i("HotelURL", "" + "http://stage.itraveller.com/backend/api/v1/hotel/destintionId/" + hotel_destination[index]);
            Log.i("MealPlanURL","http://stage.itraveller.com/backend/api/v1/hotelinclusion?hotel=" + hotel_destination[index] + "&region=" + Region_ID + "&checkInDate=" + destination_date[index]);
            Log.i("DefaultHotelURLCheck", "" + "http://stage.itraveller.com/backend/api/v1/hotel/destintionId=" + hotel_destination[index] + "&checkInDate=" + destination_date[index] + "&regionId=" + Region_ID);
            Log.d("Index check", "" + index);
            Log.d("Hotel URL test","http://stage.itraveller.com/backend/api/v1/lowesthotel?destinationId="+hotel_destination[index] + "&checkInDate=" + destination_date[index] + "&regionId=" + Region_ID);
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
                                Log.d("hotel123",""+url_checkroom);
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
                        if (flag_bit == 0) {
                            for (int index = 0; index < lowesthotelList.size(); index++) {
                                hrm.setHotel_Room_Id(jsonarr.getInt("Hotel_Room_Id"));
                                hrm.setHotel_Id(jsonarr.getInt("Hotel_Id"));
                                hrm.setRoom_Status(jsonarr.getInt("Room_Status"));
                                hrm.setRack_Rate(jsonarr.getInt("Rack_Rate"));
                                hrm.setDefault_Number(jsonarr.getInt("Default_Number"));
                                hrm.setMaximum_Number(jsonarr.getInt("Maximum_Number"));
                                hrm.setHotel_Room_Tariff_Id(jsonarr.getInt("Hotel_Room_Tariff_Id"));
                                hrm.setTAC(jsonarr.getInt("TAC"));
                                hrm.setCost(jsonarr.getInt("Cost"));
                                Log.d("Cost test test", "" + jsonarr.getInt("Cost"));
                                hrm.setMark_Up(jsonarr.getInt("Mark_Up"));
                                hrm.setDisplay_Tariff(jsonarr.getInt("Display_Tariff"));
                                Log.d("Cost test test2", "" + jsonarr.getInt("Display_Tariff"));
                                hrm.setCompany_Id(jsonarr.getInt("Company_Id"));
                                hrm.setRoom_Type(jsonarr.getString("Room_Type"));
                                hrm.setRoom_Description(jsonarr.getString("Room_Description"));
                                hrm.setFrom(jsonarr.getString("From"));
                                hrm.setTo(jsonarr.getString("To"));
                                Log.d("LowestHoteltest", "" + lowesthotelList.get(index));
                                String[] value = lowesthotelList.get(index).trim().split(",");
                                if (Integer.parseInt("" + value[0]) == jsonarr.getInt("Hotel_Id")) {
                                    if (Integer.parseInt("" + value[1]) == jsonarr.getInt("Hotel_Room_Id")) {
                                        hrm.setCheck(true);
                                        flag_bit = 1;
                                        hotelcheckbit = true;
                                        HiddenLayoutMealPlan(true);
                                    } else {
                                        //hrm.setCheck(false);
                                    }
                                } else {
                                    hotelcheckbit = false;
                                    //HiddenLayoutMealPlan(false);
                                }
                            }
                            roomList.add(hrm);

                            Collections.sort(roomList, new PriceComparison());

                            flag_bit = 0;
                        }
                        // if(index == (totalcount-1)) {
                        if (roomList.size() == 0) {
                            Log.d("Error_Catched", "No Rooms");
                            //     }
                        }

                        //com.itraveller.constant.Utility.setListViewHeightBasedOnChildren(listView);}

                       // if(totalcount==(index-2)){
                            Utility.setListViewHeightBasedOnChildren(listView);
                            adapter.notifyDataSetChanged();

                       // }
                    }



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

    public void DefaultHotelRoomSet(String url, final int depth, final String hotel_dest) {
        Log.d("LowestHotelURL", "" + url);
        Log.d("Hotel1234567","hi");

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
                    Log.d("Response in hotel page",""+response.toString());
                    Log.d("Boolean", "" + response.getBoolean("success"));
                    Log.d("Error", "" + response.getJSONObject("error"));
                    Log.d("Payload_RoomRate", "" + response.getJSONObject("payload"));

                    //for (int i = 0; i < response.getJSONObject("payload").length(); i++) {
                    if(response.getJSONObject("payload").length()==0){
                        Log.i("DefaultData","No Lowest hotel :" + depth);
                    }

                    int new_value =0;
                        JSONObject jsonarr = response.getJSONObject("payload");
                        Log.d("DefaultData",""+ jsonarr.getString("Hotel_Id") + "," + jsonarr.getString("Hotel_Room_Id") + "," + jsonarr.getString("Display_Tariff") +",1");
                    SharedPreferences sharedpreferences = getSharedPreferences("SavedData", Context.MODE_PRIVATE);
                    String saved_details = sharedpreferences.getString("HotelDetails", "NoData");
                    String[] hotel_room_Data = saved_details.trim().split("-");

                    //for(int i =0 ; i < hotel_destination.length; i++){
                        for(int j =0 ;j < pre_saved_hotel_destination_id.length ; j++) {
                            if(hotel_dest.equalsIgnoreCase(pre_saved_hotel_destination_id[j])){
                                lowesthotelList.add(hotel_room_Data[depth]);
                                new_value =1;
                            }
                        }
                    //}
                   /* if(!saved_details.equalsIgnoreCase("NoData")) {
                        *//*for(int i = 0;i<hotel_room_Data.length;i++){
                            String[] hotel_room_Data_id = hotel_room_Data[depth].trim().split(",");
                            if(hotel_room_Data_id[0].equalsIgnoreCase( jsonarr.getString("Hotel_Id"))){
                                if(hotel_room_Data_id[1].equalsIgnoreCase(jsonarr.getString("Hotel_Room_Id"))){
                                    lowesthotelList.add(hotel_room_Data[depth]);
                                    new_value =1;
                                }
                            }
                            }*//*
                    }else {
                        //here no of rooms add defaul
                        lowesthotelList.add(jsonarr.getString("Hotel_Id") + "," + jsonarr.getString("Hotel_Room_Id") + "," + jsonarr.getString("Display_Tariff") + ",1");
                    }*/
                    if(new_value == 0){
                        lowesthotelList.add(jsonarr.getString("Hotel_Id") + "," + jsonarr.getString("Hotel_Room_Id") + "," + jsonarr.getString("Display_Tariff") +","+Utility.noRooms(3, totalPersons)+",0,0");
                    }
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

                                HiddenLayoutMealPlan(true);
                                cposition = childpostion;
                                gposition = groupPosition;


                                mealplanList = new ArrayList<MealPlanModel>();

                                roomList = new ArrayList<HotelRoomModel>();
                                listView = (ListView) findViewById(R.id.room_type);
                                Utility.setListViewHeightBasedOnChildren(listView);
                                final ArrayList<HotelModel> modelRow = ListViewPagerAdapter.mHotelModels.get("" + groupPosition);

                                ImageLoader imageLoader = AppController.getInstance().getImageLoader();
                                NetworkImageView hotel_img=(NetworkImageView) findViewById(R.id.hotel_image);

                                Log.d("Image fetch URL",""+Constants.API_ViewPagerAdapter_ImageURL + modelRow.get(childpostion).getHotel_Id() + ".jpg");

                                hotel_img.setImageUrl(Constants.API_ViewPagerAdapter_ImageURL + modelRow.get(childpostion).getHotel_Id() + ".jpg",imageLoader);
                            //    hotel_img.setImageURI(Uri.parse(Constants.API_ViewPagerAdapter_ImageURL + modelRow.get(childpostion).getHotel_Id() + ".jpg"));
                             //   hotel_img.setImageResource(R.drawable.fail);
                                TextView hotel_name=(TextView) findViewById(R.id.hotel_name);

                                hotel_name.setText(modelRow.get(childpostion).getHotel_Name());

                                if(!modelRow.get(childpostion).isChecked()) {
                                    chk_lunch.setChecked(false);
                                    chk_dinner.setChecked(false);
                                }

                                if(modelRow.get(childpostion).getLunch() !=  0)
                                {
                                    chk_lunch.setChecked(true);
                                }
                                else {
                                    chk_lunch.setChecked(false);
                                }
                                if(modelRow.get(childpostion).getDinner()!=0)
                                {
                                    chk_dinner.setChecked(true);
                                }else{
                                    chk_dinner.setChecked(false);

                                }
                                if(modelRow.get(childpostion).isChecked()){
                                    HiddenLayoutMealPlan(true);
                                }
                                else{
                                    HiddenLayoutMealPlan(false);
                                }

                                if (modelRow.get(childpostion).getHotel_Id() == 0) {
                                    for (int index = 0; index < modelRow.size(); index++) {
                                        modelRow.get(index).setChecked(false);
                                    }
                                    modelRow.get(childpostion).setChecked(true);
                                    ListViewPagerAdapter.mViewPagerAdapter.notifyDataSetChanged();
                                    listViewPagerAdapter.notifyDataSetChanged();
                                    lowesthotelList.set(groupPosition, "0,0,0," + Utility.noRooms(3, totalPersons) + ",0,0");
                                    SaveData();

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
                                else
                                {


                                    Log.d("Image fetch URL",""+Constants.API_ViewPagerAdapter_ImageURL + modelRow.get(childpostion).getHotel_Id() + ".jpg");

                                    hotel_img.setImageUrl(Constants.API_ViewPagerAdapter_ImageURL + modelRow.get(childpostion).getHotel_Id() + ".jpg",imageLoader);
                                    //    hotel_img.setImageURI(Uri.parse(Constants.API_ViewPagerAdapter_ImageURL + modelRow.get(childpostion).getHotel_Id() + ".jpg"));
                                    //   hotel_img.setImageResource(R.drawable.fail);

                                    hotel_name.setText(modelRow.get(childpostion).getHotel_Name());


                                    Log.d("hotel1234",""+"http://stage.itraveller.com/backend/api/v1/hotelinclusion?hotel=" + modelRow.get(childpostion).getHotel_Id() + "&region=" + Region_ID + "&checkInDate=" + destination_date[groupPosition]);
                                    MealPlan("http://stage.itraveller.com/backend/api/v1/hotelinclusion?hotel=" + modelRow.get(childpostion).getHotel_Id() + "&region=" + Region_ID + "&checkInDate=" + destination_date[groupPosition], groupPosition);
                                    Log.i("MealPlanURL", "" + "http://stage.itraveller.com/backend/api/v1/hotelinclusion?hotel=" + modelRow.get(childpostion).getHotel_Id() + "&region=" + Region_ID + "&checkInDate=" + destination_date[groupPosition]);
                                    //Added Apdater to Null Here
                                    adapter = new HotelRoomAdapter(HotelActivity.this, roomList, lowesthotelList, groupPosition, new RadiobuttonListener() {
                                        @Override
                                        public void RadioChangeListenerCustom(String Value) {
                                            hotelcheckbit = true;
                                            Log.i("TestPostion", "" + Value);
                                            Log.i("TestPostionGroup", "" + groupPosition);
                                            lowesthotelList.set(groupPosition, "" + Value);
                                            HotelRoomData[gposition] = Value;
                                            final ArrayList<HotelModel> modelRow = ListViewPagerAdapter.mHotelModels.get("" + gposition);
                                            for (int index = 0; index < modelRow.size(); index++) {
                                                if (index == cposition) {
                                                    modelRow.get(cposition).setChecked(true);
                                                    HiddenLayoutMealPlan(true);
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
                                    //Utility.setListViewHeightBasedOnChildren(listView);
                                    //ArrayList<HotelModel> modelRow = ListViewPagerAdapter.mHotelModels.get("" + groupPosition);
                                    //modelRow.get(childpostion).
                                    Log.i("PagerView Clicked", groupPosition + "Clicked" + childpostion + " Check " + modelRow.get(childpostion).getHotel_Name());
                                    //    String url = "http://stage.itraveller.com/backend/api/v1/hotelRoom?regionId="+Region_ID+"&hotelIds=["+ modelRow.get(childpostion).getHotel_Id() +"]&checkInDate=" + destination_date[groupPosition];
                                    String url = Constants.API_HotelActivity_HOTEL_ROOMS + Region_ID + "&hotelIds=" + modelRow.get(childpostion).getHotel_Id() + "&checkInDate=" + destination_date[groupPosition];
                                    Log.i("URLForRooms", "" + groupPosition + " Url :" + url);
                                    Log.d("Hotel11",""+url);
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

                        if(modelRow.get(childpostion).getLunch()!=0)
                        {
                            chk_lunch.setChecked(true);
                        }else {
                            chk_lunch.setChecked(false);
                        }
                        if(modelRow.get(childpostion).getDinner()!=0)
                        {
                            chk_dinner.setChecked(true);
                        }else {
                            chk_dinner.setChecked(false);
                        }
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
                                            Log.d("Test test1","hi");
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

    public void CalculateSum()
    {
        Log.d("Destination date te771", "" + sum);
        Log.d("Destination date te778", "" + transportationList.get(0).getCost());
        Log.d("Destination date te779", "" + transportationList.get(0).getTitle());

        Log.d("Destination date te773", "" + transportationList.size());
    //    Log.d("Destination date te772", "" + transportationList.get(1).getCost());

        SharedPreferences.Editor editor=prfs.edit();

        if(count==0)
        {

            if(transportationList.size()>1) {

                sum += transportationList.get(1).getCost();
            }
            else if(transportationList.size()==1)
            {
                sum+=transportationList.get(0).getCost();
            }
            if(chk_lunch.isChecked())
            {
                sum+=(500*(Integer.parseInt("" + prefs.getString("Adults", null)))+Integer.parseInt("" + prefs.getString("Children_12_5", null))+Integer.parseInt("" + prefs.getString("Children_5_2", null)));
            }
            if(chk_dinner.isChecked())
            {
                sum+=(500*(Integer.parseInt("" + prefs.getString("Adults", null)))+Integer.parseInt("" + prefs.getString("Children_12_5", null))+Integer.parseInt("" + prefs.getString("Children_5_2", null)));
            }

            if(transportationList.size()>1) {
                transportation_cost += transportationList.get(1).getCost();
            }
            else if(transportationList.size()==1)
            {
                transportation_cost+=transportationList.get(0).getCost();
            }

            Log.d("Destination date te77", "" + sum);
        //    Log.d("Destination date tt77", "" + transportationList.get(1).getTitle());
        //    Log.d("Destination date tt87", "" + transportationList.get(1).getCost());

            String transportation_title;
            if(transportationList.size()>1)
            {
                transportationText.setText("" + transportationList.get(1).getTitle());
            }
            else if(transportationList.size()==1){
                transportationText.setText("" + transportationList.get(0).getTitle());

            }

            totalPriceText.setText("\u20B9 " + "" + sum);

            if(transportationList.size()>1) {

                editor.putString("TransportaionTitle", "" + transportationList.get(1).getTitle());
                editor.putString("TransportationIDV", "" + transportationList.get(1).getId());
                editor.putString("TransportationCostOld", "" + transportation_cost);
                Log.d("Destination date tt89", "" + transportationList.get(1).getCost());
                Log.d("Destination date tt97", "" + transportation_cost);


                editor.putInt("TotalCost", sum);
                editor.commit();

                trans_id = "" + transportationList.get(1).getId();
            }else if(transportationList.size()==1)
            {
                editor.putString("TransportaionTitle", "" + transportationList.get(0).getTitle());
                editor.putString("TransportationIDV", "" + transportationList.get(0).getId());
                editor.putString("TransportationCostOld", "" + transportation_cost);
                Log.d("Destination date tt89", "" + transportationList.get(0).getCost());
                Log.d("Destination date tt97", "" + transportation_cost);


                editor.putInt("TotalCost", sum);
                editor.commit();

                trans_id = "" + transportationList.get(0).getId();

            }
                count++;

        //    Log.d("TransporatationID test",""+transportationList.get(1).getId());

            getSupplierDetails();

        }

    }
    public void getDefaultTransportationCost(){

        Log.d("Welcome44","EntranceTransportation");

        String Transportation_URL=Constants.API_TransportationActivity_URL+region_id;

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

                        TransportationCost(Tra_url + jsonarr.getInt("Id"), jsonarr.getString("Title"), jsonarr.getInt("Max_Person"), jsonarr.getString("Image"), response.getJSONArray("payload").length(),i);
                        //url_data.add(Tra_url + jsonarr.getInt("Id") + "," + jsonarr.getString("Title") + "," + jsonarr.getInt("Max_Person") + "," + jsonarr.getString("Image") + "," + i + "," + response.getJSONArray("payload").length());
                        Log.d("Synchronous Task11", "" + Tra_url + jsonarr.getInt("Id"));
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
                    Toast.makeText(HotelActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                } else if (error instanceof AuthFailureError) {
                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                    //pDialog.hide();
                    Toast.makeText(HotelActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
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
        Log.d("Welcome47",""+TransURL);

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

                    Collections.sort(transportationList,new PriceComparison1());


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


    public void onBackPressed() {

        if(check_bit==0)
        {
            Log.d("Hotel testting", "hi");
            transportationList.clear();
            sum=0;
            count=0;
            flag=0;
            finish();
            //SaveData();
        }
        else
        {
           lv1.setVisibility(View.VISIBLE);
          activites.setVisibility(View.VISIBLE);
            second.setVisibility(View.GONE);
            check_bit=0;
            SaveData();
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
                        hotel_string = "" + modelRow.get(k).getLunch() + "," + modelRow.get(k).getDinner();
                    }
                }
            }

            Log.d("Lowest_Value1",""+ lowesthotelList.get(i));
           // Log.d("Lowest_Value",""+ lowesthotelList.get(i));
            //if(datas.equalsIgnoreCase("null")) {
            //set.add("" + lowesthotelList.get(i));
            String[] lowest_value = lowesthotelList.get(i).trim().split(",");

            if(i == 0) {
                itinerary_hotel = "" + lowest_value[0] + "," + lowest_value[1] + "," + lowest_value[2] + "," + lowest_value[3] + "," + hotel_string;
            }
            else{
                hotel_string_main = hotel_string_main + "-" + hotel_string;
                itinerary_hotel = itinerary_hotel + "-" +  lowest_value[0] + "," + lowest_value[1] + "," + lowest_value[2] + "," + lowest_value[3] + "," +hotel_string;
            }

        }
        Log.d("Lowest_Value1",""+ itinerary_hotel);
        SharedPreferences sharedpreferences = getSharedPreferences("SavedData", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("HotelDetails", itinerary_hotel);
        editor.putString("HotelDestinationID", ""+IDS_value);
        editor.commit();

    }

}
class DiscountModel
{

    String company_ID,company_Name,company_Address;
    int company_Discount;

    DiscountModel()
    {

    }
    DiscountModel(String company_ID,String company_Name,int company_Discount,String company_Address)
    {
        this.company_ID=company_ID;
        this.company_Name=company_Name;
        this.company_Discount=company_Discount;
        this.company_Address=company_Address;
    }
    public void setCompany_ID(String company_ID)
    {
        this.company_ID=company_ID;
    }
    public void setCompany_Name(String company_Name)
    {
        this.company_Name=company_Name;
    }
    public void setCompany_Discount(int company_Discount)
    {
        this.company_Discount=company_Discount;
    }
    public void setCompany_Address(String company_Address)
    {
        this.company_Address=company_Address;
    }
    public String getCompany_ID()
    {
        return company_ID;
    }
    public String getCompany_Name()
    {
        return company_Name;
    }
    public int getCompany_Discount()
    {
        return company_Discount;
    }
    public String getCompany_Address()
    {
        return company_Address;
    }
}
class DiscountComparison implements Comparator<DiscountModel> {

    @Override
    public int compare(DiscountModel o1,DiscountModel o2) {
        if(o1.getCompany_Discount() < o2.getCompany_Discount()){
            return 1;
        } else {
            return -1;
        }
    }

}