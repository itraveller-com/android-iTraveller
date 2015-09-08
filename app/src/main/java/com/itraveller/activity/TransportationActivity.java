package com.itraveller.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.List;

import com.itraveller.R;
import com.itraveller.adapter.TransportationAdapter;
import com.itraveller.constant.Constants;
import com.itraveller.model.TransportationModel;
import com.itraveller.volley.AppController;

/**
 * Created by VNK on 6/9/2015.
 */


public class TransportationActivity extends ActionBarActivity {

    private String url;// = "http://stage.itraveller.com/backend/api/v1/transportation?region=";
    private List<TransportationModel> transportationList = new ArrayList<TransportationModel>();
    private TransportationAdapter adapter;
    private ListView transportation_list;
    private Toolbar mToolbar;
    public static final String MY_PREFS = "ScreenHeight";
    private  int _screen_height;
    private Button filter_btn;
    private LinearLayout filter_details;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    public static int lowest_trans = 0;
    Button next_btn;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transportation_list);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Transportations");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        sharedpreferences = getSharedPreferences("Itinerary", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        SharedPreferences prefsData = getSharedPreferences("Itinerary", MODE_PRIVATE);
        String Region_id = prefsData.getString("RegionID", null);
        url = Constants.API_TransportationActivity_URL + Region_id;
        Log.i("Transportation_URL", "" + url);
        Log.i("ArrivalAirport",""+prefsData.getString("ArrivalAirport", null));
        Log.i("DepartureAirport",""+prefsData.getString("DepartureAirport", null));
        Log.i("ArrivalPort",""+prefsData.getString("ArrivalPort", null));
        Log.i("DeparturePort", "" + prefsData.getString("DeparturePort", null));

        String NewURL =Constants.API_TransportationActivity_New_URL; //"http://stage.itraveller.com/backend/api/v1/destination/destinationId/";
        ShortName(NewURL + prefsData.getString("ArrivalPort", null),"Arrival");
        ShortName(NewURL + prefsData.getString("DeparturePort", null),"Departure");
        SharedPreferences prefs = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        _screen_height = prefs.getInt("Screen_Height", 0)-(prefs.getInt("Status_Height", 0) + prefs.getInt("ActionBar_Height", 0));
        Log.i("iTraveller", "Screen Height: " + _screen_height);
        int width = prefs.getInt("Screen_Width", 0); //0 is the default value.
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width,(_screen_height - 60));


        next_btn = (Button) findViewById(R.id.to_payment);
        next_btn.setEnabled(false);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefsData = getSharedPreferences("Itinerary", MODE_PRIVATE);
                prefsData.edit().putString("OnwardFlightPrice","0").commit();
                prefsData.edit().putString("ReturnFlightPrice", "0").commit();
                String F_bit = ""+prefsData.getString("FlightBit",null);
                int flightBit = Integer.parseInt(""+F_bit);
                if(prefsData.getString("TravelFrom", null).equalsIgnoreCase("1")||prefsData.getString("TravelTo", null).equalsIgnoreCase("1")) {
                    Intent intent = new Intent(TransportationActivity.this, ItinerarySummaryActivity.class);
                    startActivity(intent);
                }
                else
                {
                    if(flightBit== 0) {
                        Intent intent = new Intent(TransportationActivity.this, FlightActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(TransportationActivity.this, FlightDomesticActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });


        transportation_list = (ListView) findViewById(R.id.transportation_list);
        adapter = new TransportationAdapter(this, transportationList);
        transportation_list.setAdapter(adapter);
        final ProgressDialog pDialog = new ProgressDialog(TransportationActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("Boolean", ""+response.getBoolean("success"));
                    Log.d("Error", ""+response.getJSONObject("error"));
                    Log.d("Payload", ""+response.getJSONArray("payload"));

                    // Parsing json
                    int response_JSON_arr_length=response.getJSONArray("payload").length();
                    for (int i = 0; i < response_JSON_arr_length; i++) {

                        JSONObject jsonarr = response.getJSONArray("payload").getJSONObject(i);
                        String Tra_url =Constants.API_TransportationActivity_Tra_URL;    //"http://stage.itraveller.com/backend/api/v1/b2ctransportation?transportationId=";
                        TransportationCost(Tra_url + jsonarr.getInt("Id"),jsonarr.getString("Title"),jsonarr.getInt("Max_Person"),jsonarr.getString("Image"),i);
                    }
                    pDialog.dismiss();

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
                if( error instanceof NetworkError) {

                    pDialog.hide();
                    Toast.makeText(TransportationActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                } else if( error instanceof ServerError) {
                } else if( error instanceof AuthFailureError) {
                } else if( error instanceof ParseError) {
                } else if( error instanceof NoConnectionError) {
                    pDialog.hide();
                    Toast.makeText(TransportationActivity.this, "No Internet Connection" ,Toast.LENGTH_LONG).show();
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


    public void TransportationCost(String TransURL, final String title, final int max_person, final String img, final int index)
    {

        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET,
                TransURL, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("Boolean", ""+response.getBoolean("success"));
                    Log.d("Error", ""+response.getJSONObject("error"));
                    Log.d("Payload", ""+response.getJSONObject("payload"));

                    // JSONObject jsonobj = response.getJSONObject("payload").get;
                    // Parsing json
                        JSONObject jsonarr = response.getJSONObject("payload");
                        TransportationModel transportation_model = new TransportationModel();
                        if(index == 0) {
                        lowest_trans = Integer.parseInt(""+jsonarr.getInt("Cost"));
                        }
                    else {
                            if((lowest_trans >= Integer.parseInt(""+jsonarr.getInt("Cost"))) && (Integer.parseInt(""+jsonarr.getInt("Cost")) !=0) ){
                                lowest_trans = Integer.parseInt(""+jsonarr.getInt("Cost"));
                            }
                        }
                        transportation_model.setId(jsonarr.getInt("Id"));
                        transportation_model.setTransportation_Id(jsonarr.getInt("Transportation_Id"));
                        transportation_model.setTitle("" + title);
                        transportation_model.setCost(jsonarr.getInt("Cost"));
                        transportation_model.setCost1(jsonarr.getInt("Cost1"));
                        transportation_model.setKM_Limit(jsonarr.getInt("KM_Limit"));
                        transportation_model.setPrice_Per_KM(jsonarr.getInt("Price_Per_KM"));
                        transportation_model.setMax_Person(max_person);
                        transportation_model.setImage(img);

                        transportationList.add(transportation_model);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter.notifyDataSetChanged();
                next_btn.setEnabled(true);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if(id == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void ShortName(String ShortnameURL, final String arr_dep)
    {
        Log.i("ShortNameURL " + arr_dep,""+ShortnameURL);
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
                            } else {
                                editor.putString("TravelFrom", response.getJSONObject("payload").getString("Code"));
                            }
                        }else if(arr_dep.equalsIgnoreCase("Arrival")){
                            if (response.getJSONObject("payload").getString("Code").equalsIgnoreCase("1")) {
                                editor.putString("TravelTo", "1");
                            } else {
                                editor.putString("TravelTo", response.getJSONObject("payload").getString("Code"));
                            }
                        }
                        editor.commit();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter.notifyDataSetChanged();

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


    public void onBackPressed() {
        finish();
    }
}
