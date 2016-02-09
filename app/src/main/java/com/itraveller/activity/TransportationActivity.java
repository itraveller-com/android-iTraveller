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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    SharedPreferences prefs;
    public int count;

    String Region_id;

    private String url;// = "http://stage.itraveller.com/backend/api/v1/transportation?region=";
    private List<TransportationModel> transportationList = new ArrayList<TransportationModel>();
    private TransportationAdapter adapter;
    private RecyclerView transportation_list;
    private Toolbar mToolbar;
    public final String MY_PREFS = "ScreenHeight";
    private int _screen_height;
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
                String F_bit = "" + prefsData.getString("FlightBit","0");
                Log.d("Flight bit",""+F_bit);
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


        transportation_list = (RecyclerView) findViewById(R.id.transportation_list);
        adapter = new TransportationAdapter(this, transportationList);
        transportation_list.setLayoutManager(new LinearLayoutManager(this));
        transportation_list.setHasFixedSize(false);
        transportation_list.setAdapter(adapter);
        pDialog = new ProgressDialog(TransportationActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        ndDialog=new ProgressDialog(TransportationActivity.this);

        SynchronousFunction();
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


    public void TransportationCost(String TransURL, final String title, final int max_person, final String img, final int last_index) {
        synchronized (this) {

            JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET,
                    TransURL, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Log.d("Boolean", "" + response.getBoolean("success"));
                        Log.d("Error", "" + response.getJSONObject("error"));

                        Log.d("Payload", "" + response.isNull("payload"));

                        if(!(response.isNull("payload"))) {


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
                            Log.v("Swap Price", "" + jsonarr.getInt("Cost"));
                        /*if (index == 0) {
                            lowest_trans = Integer.parseInt("" + jsonarr.getInt("Cost"));
                                swap_value = index;
                        } else {
                            if ((lowest_trans >= Integer.parseInt("" + jsonarr.getInt("Cost"))) && (Integer.parseInt("" + jsonarr.getInt("Cost")) != 0)) {
                                swap_value = index;
                            }
                        }*/
                            if (jsonarr.getInt("Cost") == 0) {
                                zero_value++;
                            }
                            //transportation_model.setIsCheck(false);
                            transportationList.add(transportation_model);
                            Log.v("Swap Last Index", "" + last_index);
                            Log.v("Swap Last Index", "" + index);
                            if ((last_index - 1) == index) {
                                Log.v("Swap Value", "" + swap_value);
                                Log.v("Swap Lowest", "" + lowest_trans);

                                Collections.sort(transportationList, new PriceComparison());
                                mySubClass.doSomething();
                                if (zero_value != 0) {
                                    for (int i = 0; i < zero_value; i++) {
                                        transportationList.add((index_at + 1), transportationList.get(0));
                                        transportationList.remove(0);
                                    }
                                }
                                if (transportationID == 0) {
                                    transportationList.get(0).setIsCheck(true);
                                } else {
                                    for (int x = 0; x < transportationList.size(); x++) {
                                        if (transportationList.get(x).getTransportation_Id() == transportationID) {
                                            transportationList.get(x).setIsCheck(true);
                                            transportationList.add((0), transportationList.get(x));
                                            transportationList.remove(x + 1);
                                        }
                                    }
                                }


                                adapter.notifyDataSetChanged();
                                pDialog.hide();
                            }

                        }
                        else
                        {
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
