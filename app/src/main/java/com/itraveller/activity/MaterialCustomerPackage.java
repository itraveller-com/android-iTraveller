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
public class MaterialCustomerPackage extends ActionBarActivity {

    private String url = Constants.API_CustomerPackage;   //"http://stage.itraveller.com/backend/api/v1/itinerary/regionId/";
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


        //Adding RecyclerView Object
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(MaterialCustomerPackage.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CardAdapterRegionPlace(regionList, MaterialCustomerPackage.this);
        mRecyclerView.setAdapter(mAdapter);

        JsonCallForPackage();
    }


    public void onBackPressed() {
        SharedPreferences prefs=getSharedPreferences("Preferences",MODE_PRIVATE);
        SharedPreferences.Editor editor=prefs.edit();
        editor.putString("back","1").commit();
        Intent i=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        finish();
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
        regionList.clear();
        JsonCallForPackage();
    }

    public void JsonCallForPackage()
    {
        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                mAdapter.notifyDataSetChanged();
                HiddenControls();
                SwipeRefreshOrNot();
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
