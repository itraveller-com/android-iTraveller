package com.itraveller.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.itraveller.R;
import com.itraveller.constant.Constants;
import com.itraveller.materialadapter.CardAdapaterLanding;
import com.itraveller.materialadapter.CardAdapterRegionPlace;
import com.itraveller.model.RegionPlaceModel;
import com.itraveller.volley.AppController;
import com.melnykov.fab.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by I TRAVELLES on 30-10-2015.
 */
public class MaterialRegionPlace extends ActionBarActivity {

    private String url = Constants.API_RegionPlace_URL;   //"http://stage.itraveller.com/backend/api/v1/itinerary/regionId/";
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    ProgressBar progessbar;
    private List<RegionPlaceModel> regionList = new ArrayList<RegionPlaceModel>();
    SwipeRefreshLayout mSwipeRefreshLayout;
    FloatingActionButton fab;
    int min_value = 0, max_value = 0;
    SharedPreferences prefss;
    private Bundle bundle;
    Toolbar mToolbar;// Declaring the Toolbar Object

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.material_region_place_listview);

        bundle = getIntent().getExtras();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle(bundle.getString("RegionName") + " Packages");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        prefss = getSharedPreferences("PostData",MODE_PRIVATE);

        //Print
        Log.d("regionId in regionplace", url + bundle.getInt("RegionID"));
        url= url + bundle.getInt("RegionID");


        progessbar = (ProgressBar) findViewById(R.id.progressBarLanding);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setRefreshing(true);

        //Fab Floating Button
        fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.hide();
            }
        });

        //Adding RecyclerView Object
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(MaterialRegionPlace.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CardAdapterRegionPlace(regionList, MaterialRegionPlace.this);
        mRecyclerView.setAdapter(mAdapter);

        JsonCallForRegion();
    }


    public void onBackPressed() {
    /*    if(filter_btn.getText().toString().equalsIgnoreCase("Apply Filter"))
        {
            filter_details.setVisibility(View.GONE);
            filter_btn.setText("Filter");
        }
        else
        {
     */       SharedPreferences prefs=getSharedPreferences("Preferences",MODE_PRIVATE);
        SharedPreferences.Editor editor=prefs.edit();
        Intent i=new Intent(getApplicationContext(),MainActivity.class);
        Log.d("Var value checking ",""+prefs.getString("var",null));
        if(prefs.getInt("flag",0)==1)
        {
            if (("" + prefs.getString("var", null)).equals("y")) {

                editor.putString("Profile","temp");
                editor.putString("ID","temp");
                editor.commit();

                startActivity(i);
                finish();

            }
            else
            {
                editor.putString("Profile","login_from_server");
                editor.putString("ID", "login_from_server");
                editor.commit();

                startActivity(i);
                finish();
            }
        }
        else
        {
            editor.putString("Profile","unregistered");
            editor.putString("ID","unregistered");
            editor.commit();

            startActivity(i);
            finish();
        }

        //    }
    }


    public void SwipeRefreshOrNot(){
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    public void HiddenControls()
    {
        progessbar.setVisibility(View.GONE);
    }


    private void refreshContent() {
        mSwipeRefreshLayout.setRefreshing(true);
        JsonCallForRegion();
    }

    public void JsonCallForRegion()
    {
        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    int index =0;
                    //Log.d("Boolean", "" + response.getJSONObject("Itinerary"));
                    JSONObject resobj = response.getJSONObject("Itinerary");
                    Iterator<?> keys = resobj.keys();
                    while(keys.hasNext() ) {
                        String key = (String)keys.next();
                        Log.i("KeyValue", "" + key);
                        if ( resobj.get(key) instanceof JSONObject ) {
                            RegionPlaceModel region_adp = new RegionPlaceModel();

                            JSONObject jsonobj = new JSONObject(resobj.get(key).toString());
                            // Log.d("res1",""+xx.getJSONObject("Master"));
                            Log.d("KeyDestinationDiscount", "" + jsonobj.getInt("Discount"));

                            region_adp.setTitle(jsonobj.getJSONObject("Master").getString("Title"));
                            region_adp.setLink(jsonobj.getJSONObject("Master").getString("Link"));
                            //region_adp.setImage(jsonobj.getJSONObject("Master").getString("Image"));
                            //    region_adp.setImage("http://stage.itraveller.com/backend/images/packages/" + jsonobj.getJSONObject("Master").getInt("Itinerary_Id") + ".jpg");
                            region_adp.setImage(Constants.API_RegionPlace_ImageURL + jsonobj.getJSONObject("Master").getInt("Itinerary_Id") + ".jpg");
                            Log.d("Images:", "http://stage.itraveller.com/backend/images/packages/" + jsonobj.getJSONObject("Master").getInt("Itinerary_Id") + ".jpg");
                            region_adp.setArrival_Port_Id(jsonobj.getJSONObject("Master").getInt("Arrival_Port_Id"));
                            region_adp.setDeparture_Port_Id(jsonobj.getJSONObject("Master").getInt("Departure_Port_Id"));
                            region_adp.setItinerary_Id(jsonobj.getJSONObject("Master").getInt("Itinerary_Id"));
                            region_adp.setDuration_Day(jsonobj.getJSONObject("Master").getInt("Duration_Day"));
                            Log.d("No of nights testing in package",""+jsonobj.getJSONObject("Master").getInt("Duration_Day"));
                            region_adp.setPrice(jsonobj.getJSONObject("Master").getInt("Price"));

                            SharedPreferences.Editor editor2=prefss.edit();

                            editor2.putInt("ItineraryId", jsonobj.getJSONObject("Master").getInt("Itinerary_Id"));
                            editor2.putInt("TripDuration",jsonobj.getJSONObject("Master").getInt("Duration_Day"));
                            editor2.commit();

                            if(index ==0) {
                                min_value = jsonobj.getJSONObject("Master").getInt("Price");
                                max_value = jsonobj.getJSONObject("Master").getInt("Price");
                                index = 1;
                            }
                            if( min_value >= jsonobj.getJSONObject("Master").getInt("Price") ){
                                min_value = jsonobj.getJSONObject("Master").getInt("Price");
                            }
                            else if( max_value <= jsonobj.getJSONObject("Master").getInt("Price") )
                            {max_value = jsonobj.getJSONObject("Master").getInt("Price");
                            }
                            region_adp.setDiscount(jsonobj.getInt("Discount"));


                            Iterator<?> destinationKeys = jsonobj.getJSONObject("Destination").keys();
                            int i = 0;
                            String destinationKeyValue = null;
                            String destinationValue = null;
                            String destinationCount = null;

                            while(destinationKeys.hasNext() ) {
                                String destinationKey = (String) destinationKeys.next();
                                if(i == 0)
                                {
                                    destinationKeyValue = destinationKey;
                                    JSONObject destobj = jsonobj.getJSONObject("Destination").getJSONObject(destinationKey);
                                    destinationValue = destobj.getString("name");
                                    destinationCount = destobj.getString("count");

                                    Log.d("Rohan test test",""+destobj.getString("count"));

                                    i++;
                                }
                                else{
                                    destinationKeyValue = destinationKeyValue + "," + destinationKey;
                                    JSONObject destobj = jsonobj.getJSONObject("Destination").getJSONObject(destinationKey);
                                    destinationValue = destinationValue + "," + destobj.getString("name").toString();
                                    destinationCount = destinationCount + "," + destobj.getString("count").toString();

                                    Log.d("Rohan test test1",""+destobj.getString("count"));
                                    Log.d("Rohan test test2",""+destobj.getString("count").toString());


                                }
                                //Log.i("KeyDestination", "" + jsonobj.getJSONObject("Destination").getString(destinationKey));
                                region_adp.setDestination_Key(destinationKeyValue);
                                region_adp.setDestination(destinationValue);
                                region_adp.setDestination_Count(destinationCount);

                            }
                            Log.i("Key_DestinationValue", "" + destinationKeyValue);
                            Log.i("Key_Destination", "" + destinationValue);
                            Log.i("Key_DestinationDayCount", "" + destinationCount);
                            //region_adp.setDestination("Testing");
                            Log.d("Discount", "" +region_adp.getDiscount());
                            regionList.add(region_adp);

                            //    Collections.sort(regionList,new PriceComparison());
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // pDialog.hide();
                //  region_adapter.notifyDataSetChanged();
                // Update UI elements
            /*    minval.setText(""+ (min_value - 1));
                maxval.setText(""+ (max_value + 1));
                rangeSeekBar.setRangeValues(min_value - 1, max_value + 1);
                rangeSeekBar.setSelectedMinValue(min_value - 1);
                rangeSeekBar.setSelectedMaxValue(max_value + 1);
            */

                mAdapter.notifyDataSetChanged();
                HiddenControls();
                SwipeRefreshOrNot();
                //searchText.startAnimation(animFadein);
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

                } else if( error instanceof ServerError) {
                } else if( error instanceof AuthFailureError) {
                } else if( error instanceof ParseError) {
                } else if( error instanceof NoConnectionError) {
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
}
