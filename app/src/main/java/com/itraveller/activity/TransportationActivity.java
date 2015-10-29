package com.itraveller.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
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
import android.widget.ImageButton;
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

import com.itraveller.R;
import com.itraveller.adapter.ListViewPagerActivitiesAdapter;
import com.itraveller.adapter.ListViewPagerAdapter;
import com.itraveller.adapter.TransportationAdapter;
import com.itraveller.constant.Constants;
import com.itraveller.constant.Utility;
import com.itraveller.model.ActivitiesModel;
import com.itraveller.model.HotelModel;
import com.itraveller.model.OnwardDomesticFlightModel;
import com.itraveller.model.RegionPlaceModel;
import com.itraveller.model.ReturnDomesticFlightModel;
import com.itraveller.model.TransportationModel;
import com.itraveller.volley.AppController;

/**
 * Created by VNK on 6/9/2015.
 */


public class TransportationActivity extends ActionBarActivity implements MyCallbackClass {

    SharedPreferences preferences;
    ProgressDialog ndDialog;
    TextView nameText,placesText,destinationText,arr_dateText,dep_dateText,daysText,adultsText,child_5_12_Text,child_below_5_Text;
    TextView nameSellerText,addressSellerText,arrAtText,dateDisplayText,roomDisplayText,totalPriceText;
    TextView discountPriceText,priceAdvanceText,remainingPriceText,departureText,transportationText;
    SharedPreferences prfs,post_prefs;
    SharedPreferences prefs;
    String[] ActivityData;
    public static int total_sum,activity_sum;
    public static int count;
    public static int transportation_cost;
    public static String Destination_Value;
    public static String Destination_Date;


    ArrayList<DiscountModel> Discount_list=new ArrayList<DiscountModel>();
    String[] HotelRoomData;
    private ArrayList<String> lowesthotelList;
    String Region_id;
    public static String destination_id_arr[];
    public static String destination_date_arr[];
    static String trans_id;
    String supplier_str="";
    String discount_str="";
    public static int sum;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;


    int cost;
    JSONObject jsonarr;
    static TransportationModel temp_model;

    private String url;// = "http://stage.itraveller.com/backend/api/v1/transportation?region=";
    private List<TransportationModel> transportationList = new ArrayList<TransportationModel>();
    private TransportationAdapter adapter;
    private ListView transportation_list;
    private Toolbar mToolbar;
    public static final String MY_PREFS = "ScreenHeight";
    private int _screen_height;
    private Button filter_btn;
    private LinearLayout filter_details;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    public static int lowest_trans = 0;
    Button next_btn;
    int swap_value = 0;
    int zero_value = 0 ;
    SubClass mySubClass;
    ArrayList<String> url_data;
    int index = 0 ;
    int index_at = 0;
    ProgressDialog pDialog;
    SharedPreferences savedPrefsData;
    int transportationID;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transportation_list);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Transportations");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mySubClass = new SubClass();

        mySubClass.registerCallback(this);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        setupDrawer();


        preferences=getSharedPreferences("Preferences",MODE_PRIVATE);
        prefs=getSharedPreferences("Itinerary",MODE_PRIVATE);
        prfs=getSharedPreferences("Itinerary",MODE_PRIVATE);
        post_prefs=getSharedPreferences("PostData",MODE_PRIVATE);

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
        arr_dateText.setText(getConvertedDate(""+preferences.getString("Date_str",null)));
        dep_dateText.setText(getNextConvertedDate("" + preferences.getString("Date_str", null)));
        daysText.setText("" + prefs.getInt("Duration",0));
        adultsText.setText("" + prefs.getString("Adults", null));
        child_5_12_Text.setText("" + prefs.getString("Children_12_5",null));
        child_below_5_Text.setText("" + prefs.getString("Children_5_2",null));
        totalPriceText.setText("Calculating...");
        discountPriceText.setText("Calculating...");
        remainingPriceText.setText("Calculating...");
        priceAdvanceText.setText("Calculating...");
        addressSellerText.setText("");
        arrAtText.setText(prefs.getString("ArrivalPortString",null));
        dateDisplayText.setText("");
        roomDisplayText.setText("");
        departureText.setText(prefs.getString("DeparturePortString",null));
        transportationText.setText("Loading...");



        sharedpreferences = getSharedPreferences("Itinerary", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        savedPrefsData = getSharedPreferences("SavedData", MODE_PRIVATE);
        transportationID = Integer.parseInt("" + savedPrefsData.getString("TransportationID", "0"));

        SharedPreferences prefsData = getSharedPreferences("Itinerary", MODE_PRIVATE);
        Region_id = prefsData.getString("RegionID", null);



        url = Constants.API_TransportationActivity_URL + Region_id;
        Log.i("Transportation_URL", "" + url);
        Log.i("ArrivalAirport", "" + prefsData.getString("ArrivalAirport", null));
        Log.i("DepartureAirport", "" + prefsData.getString("DepartureAirport", null));
        Log.i("ArrivalPort", "" + prefsData.getString("ArrivalPort", null));
        Log.i("DeparturePort", "" + prefsData.getString("DeparturePort", null));

        String NewURL = Constants.API_TransportationActivity_New_URL; //"http://stage.itraveller.com/backend/api/v1/destination/destinationId/";
        ShortName(NewURL + prefsData.getString("ArrivalPort", null), "Arrival");
        ShortName(NewURL + prefsData.getString("DeparturePort", null), "Departure");
        SharedPreferences prefs = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        _screen_height = prefs.getInt("Screen_Height", 0) - (prefs.getInt("Status_Height", 0) + prefs.getInt("ActionBar_Height", 0));
        Log.i("iTraveller", "Screen Height: " + _screen_height);
        int width = prefs.getInt("Screen_Width", 0); //0 is the default value.
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, (_screen_height - 60));


        //For changing the text in next button


        next_btn = (Button) findViewById(R.id.to_payment);


        Log.d("Nextbtn test",""+next_btn);

        next_btn.setEnabled(false);

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SharedPreferences prefsData = getSharedPreferences("Itinerary", MODE_PRIVATE);
                prefsData.edit().putString("OnwardFlightPrice", "0").commit();
                prefsData.edit().putString("ReturnFlightPrice", "0").commit();
                String F_bit = "" + prefsData.getString("FlightBit", null);
                int flightBit = Integer.parseInt("" + F_bit);
                if (prefsData.getString("TravelFrom", null).equalsIgnoreCase("1") || prefsData.getString("TravelTo", null).equalsIgnoreCase("1")) {
                    Intent intent = new Intent(TransportationActivity.this, ItinerarySummaryActivity.class);
                    SharedPreferences prefs = getSharedPreferences("Preferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("No_Flights", 1);
                    editor.commit();
                    startActivity(intent);
                } else {
                    if (flightBit == 0) {
                        Intent intent = new Intent(TransportationActivity.this, FlightActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(TransportationActivity.this, FlightDomesticActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });


        transportation_list = (ListView) findViewById(R.id.transportation_list);
        adapter = new TransportationAdapter(this, transportationList);
        transportation_list.setAdapter(adapter);
        pDialog = new ProgressDialog(TransportationActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        ndDialog=new ProgressDialog(TransportationActivity.this);

        SynchronousFunction();
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

                getHotelAndActivitySum();

                getSupportActionBar().setTitle("Summary Data");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);

                sum=0;

                getSupportActionBar().setTitle("Transportations");
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

/*        int id = item.getItemId();


        if(id == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
*/

        if (item != null && item.getItemId() == R.id.btnMyMenu) {
            if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
            } else {
                mDrawerLayout.openDrawer(Gravity.RIGHT);
            }
        }


        return false;
    }

    public void getHotelAndActivitySum()
    {
        String Hotel_ID_arr[]=prefs.getString("HotelRooms",null).split("-");
        Log.d("Activity test testte",Hotel_ID_arr[0]);
        String Hotel_Data_arr[]=new String[4];

        for(int i=0;i<Hotel_ID_arr.length;i++)
        {
            Hotel_Data_arr = Hotel_ID_arr[i].split(",");
            Log.d("Activity test testtt",""+Hotel_Data_arr.length);
            sum += Integer.parseInt(Hotel_Data_arr[2]);

        }
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

        sum=sum+(activity_sum);

        getUserSelectedTransportation();
    }


    public void getSupplierDetails()
    {
        Log.d("URL test test10","hi");
        String url="http://m.itraveller.com/api/v1/supplier?region="+Region_id;
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
                        Toast.makeText(TransportationActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    } else if (error instanceof ServerError) {
                    } else if (error instanceof AuthFailureError) {
                    } else if (error instanceof ParseError) {
                    } else if (error instanceof NoConnectionError) {
                        // pDialog.hide();
                        Toast.makeText(TransportationActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(TransportationActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                } else if (error instanceof AuthFailureError) {
                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                    // pDialog.hide();
                    Toast.makeText(TransportationActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
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

        SharedPreferences.Editor editor=post_prefs.edit();

        totalPriceText.setText("\u20B9 " + sum);
        editor.putInt("TotalPackagePrice", sum);

        discountPriceText.setText("" + Discount_list.get(0).getCompany_Discount() + " %");
        editor.putInt("DiscountValue", Discount_list.get(0).getCompany_Discount());

        int remaining_price=sum-((Integer.parseInt(""+Discount_list.get(0).getCompany_Discount())*sum)/100);
        remainingPriceText.setText("\u20B9 "+""+remaining_price);
        editor.putInt("RemainingPrice", remaining_price);

        int adv_price=(20*remaining_price)/100;
        priceAdvanceText.setText("\u20B9 " + "" + adv_price);
        editor.putInt("AdvancePrice", adv_price);

        nameSellerText.setText("" + Discount_list.get(0).getCompany_Name());
        editor.putString("SellerName", "" + Discount_list.get(0).getCompany_Name());

        addressSellerText.setText("" + Discount_list.get(0).getCompany_Address());
        editor.putString("SellerAddress", "" + Discount_list.get(0).getCompany_Address());

        transportationText.setText("" + prefs.getString("TransportationName", null));
        editor.putString("TransportationName", ""+prefs.getString("TransportationName",null));

        editor.commit();

        ndDialog.hide();
    }


    public void ShortName(String ShortnameURL, final String arr_dep)
    {
        Log.i("TestingURL", "Enter into function");
        Log.i("ShortNameURL " + arr_dep, "" + ShortnameURL);
        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET,
                ShortnameURL, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("Boolean", ""+response.getBoolean("success"));
                    Log.d("Error", ""+response.getJSONObject("error"));
                    Log.d("Payload", ""+response.getJSONObject("payload"));

                    // JSONObject jsonobj = response.getJSONObject("payload").get;
                    // Parsing json
                    int response_JSON_length=response.getJSONObject("payload").length();
                    for (int i = 0; i < response_JSON_length; i++) {
                        if(arr_dep.equalsIgnoreCase("Departure")) {
                            if (response.getJSONObject("payload").getString("Code").equalsIgnoreCase("1")) {
                                editor.putString("TravelFrom", "1");
                                next_btn.setText("Itinerary Summary");
                            } else {
                                editor.putString("TravelFrom", response.getJSONObject("payload").getString("Code"));
                                next_btn.setText("Book Flights");
                            }
                        }else if(arr_dep.equalsIgnoreCase("Arrival")){
                            if (response.getJSONObject("payload").getString("Code").equalsIgnoreCase("1")) {
                                editor.putString("TravelTo", "1");
                                next_btn.setText("Itinerary Summary");
                            } else {
                                editor.putString("TravelTo", response.getJSONObject("payload").getString("Code"));
                                next_btn.setText("Book Flights");
                            }
                        }
                        editor.commit();

                    }
                    Log.d("TestingURL", "Function in");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

              //  adapter.notifyDataSetChanged();

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
        Log.d("TestingURL", "Exit from function");
    }


    @Override
    public void callbackReturn() {
        Log.d("Synchronous Task4", "Enter into Call Back function");
    }


    class PriceComparison implements Comparator<TransportationModel> {

        @Override
        public int compare(TransportationModel o1,TransportationModel o2) {
            if(o1.getCost() > o2.getCost()){
                return 1;
            } else {
                return -1;
            }
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

    public void onBackPressed() {
        finish();
    }



        public void SynchronousFunction(){
                JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET,
                        url, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("Boolean", "" + response.getBoolean("success"));
                            Log.d("Error", "" + response.getJSONObject("error"));
                            Log.d("Payload", "" + response.getJSONArray("payload"));

                            // Parsing json
                            int response_JSON_arr_length = response.getJSONArray("payload").length();
                            url_data = new ArrayList<String>();

                            for (int i = 0; i < response_JSON_arr_length; i++) {

                                JSONObject jsonarr = response.getJSONArray("payload").getJSONObject(i);
                                String Tra_url = Constants.API_TransportationActivity_Tra_URL+Region_id+"&transportationId=";    //"http://stage.itraveller.com/backend/api/v1/b2ctransportation?transportationId=";

                                TransportationCost(Tra_url + jsonarr.getInt("Id"), jsonarr.getString("Title"), jsonarr.getInt("Max_Person"), jsonarr.getString("Image"), response.getJSONArray("payload").length());
                                //url_data.add(Tra_url + jsonarr.getInt("Id") + "," + jsonarr.getString("Title") + "," + jsonarr.getInt("Max_Person") + "," + jsonarr.getString("Image") + "," + i + "," + response.getJSONArray("payload").length());
                                Log.d("Synchronous Task", "" + Tra_url + jsonarr.getInt("Id"));
                            }
                            //pDialog.dismiss();
                           // worker.callback = new TransportationActivity();
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                            Toast.makeText(TransportationActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                        } else if (error instanceof AuthFailureError) {
                        } else if (error instanceof ParseError) {
                        } else if (error instanceof NoConnectionError) {
                            //pDialog.hide();
                            Toast.makeText(TransportationActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
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

    public void getUserSelectedTransportation()
    {
        String tra_id=prefs.getString("TransportationID",null);
        String tra_name=prefs.getString("TransportationName",null);
        String tra_cost=prefs.getString("TransportationCost",null);

        Log.d("Transportation test2",""+tra_cost);


        sum+=Integer.parseInt(tra_cost);
        Log.d("Transportation test6",""+total_sum);

        getSupplierDetails();
    }


    public void TransportationCost(String TransURL, final String title, final int max_person, final String img, final int last_index) {
        synchronized (this) {

            JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET,
                    TransURL, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Log.d("Boolean", "" + response.getBoolean("success"));
                        Log.d("Error", "" + response.getJSONObject("error"));
                        Log.d("Payload", "" + response.getJSONObject("payload"));

                        // JSONObject jsonobj = response.getJSONObject("payload").get;
                        // Parsing json
                        JSONObject jsonarr = response.getJSONObject("payload");
                        TransportationModel transportation_model = new TransportationModel();
                        transportation_model.setId(jsonarr.getInt("Id"));
                        transportation_model.setTransportation_Id(jsonarr.getInt("Transportation_Id"));
                        transportation_model.setTitle("" + title);
                        transportation_model.setCost(jsonarr.getInt("Cost"));
                        Log.d("Transportation cost",""+jsonarr.getInt("Cost"));
                        transportation_model.setCost1(jsonarr.getInt("Cost1"));
                        transportation_model.setKM_Limit(jsonarr.getInt("KM_Limit"));
                        transportation_model.setPrice_Per_KM(jsonarr.getInt("Price_Per_KM"));
                        transportation_model.setMax_Person(max_person);
                        transportation_model.setImage(img);
                        transportation_model.setIsCheck(false);
                        Log.v("Swap Price",""+jsonarr.getInt("Cost"));
                        /*if (index == 0) {
                            lowest_trans = Integer.parseInt("" + jsonarr.getInt("Cost"));
                                swap_value = index;
                        } else {
                            if ((lowest_trans >= Integer.parseInt("" + jsonarr.getInt("Cost"))) && (Integer.parseInt("" + jsonarr.getInt("Cost")) != 0)) {
                                swap_value = index;
                            }
                        }*/
                        if(jsonarr.getInt("Cost") == 0){
                            zero_value ++;
                        }
                        //transportation_model.setIsCheck(false);
                        transportationList.add(transportation_model);
                        Log.v("Swap Last Index", "" + last_index);
                        Log.v("Swap Last Index", "" + index);
                        if ((last_index - 1) == index) {
                            Log.v("Swap Value", ""+swap_value);
                            Log.v("Swap Lowest", "" + lowest_trans);

                            Collections.sort(transportationList, new PriceComparison());
                            mySubClass.doSomething();
                            if(zero_value != 0){
                                for(int i = 0; i < zero_value ; i++ ) {
                                    transportationList.add((index_at + 1), transportationList.get(0));
                                    transportationList.remove(0);
                                }
                            }
                            if(transportationID == 0) {
                                transportationList.get(0).setIsCheck(true);
                            }
                            else{
                                for(int x = 0; x < transportationList.size(); x++) {
                                    if(transportationList.get(x).getTransportation_Id() == transportationID) {
                                        transportationList.get(x).setIsCheck(true);
                                        transportationList.add((0), transportationList.get(x));
                                        transportationList.remove(x + 1);
                                    }
                                }
                            }
                            adapter.notifyDataSetChanged();
                            pDialog.hide();
                        }
                        index_at++;
                        index ++;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    Log.d("Synchronous Task2", "Exit at " +index);
                    next_btn.setEnabled(true);

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("Volley Error", "Error: " + error.getMessage());
                    //pDialog.hide();
                    if ((last_index - 1) == index) {
                        Log.v("Swap Value", ""+swap_value);
                        Log.v("Swap Lowest", "" + lowest_trans);

                        Collections.sort(transportationList, new PriceComparison());
                        mySubClass.doSomething();
                        if(zero_value != -1){
                            for(int i = 0; i < zero_value ; i++ ) {
                                transportationList.add((index_at), transportationList.get(0));
                                transportationList.remove(0);
                            }
                        }
                        transportationList.get(0).setIsCheck(true);
                        adapter.notifyDataSetChanged();
                        pDialog.hide();
                    }
                    index ++;

                }
            });
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq);

        }

    }

}


interface MyCallbackClass{
    void callbackReturn();
}
class SubClass {

    MyCallbackClass myCallbackClass;

    void registerCallback(MyCallbackClass callbackClass){
        myCallbackClass = callbackClass;
    }

    void doSomething(){
        myCallbackClass.callbackReturn();
    }

}
