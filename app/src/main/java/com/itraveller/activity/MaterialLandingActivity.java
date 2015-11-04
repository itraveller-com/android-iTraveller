package com.itraveller.activity;

/**
 * Created by iTraveller on 10/28/2015.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ViewFlipper;

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
import com.itraveller.materialadapter.CardAdapater;
import com.itraveller.materialadapter.CardAdapaterLanding;
import com.itraveller.model.LandingModel;
import com.itraveller.volley.AppController;
import com.melnykov.fab.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MaterialLandingActivity extends Fragment {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    FloatingActionButton fab;
    ProgressBar progessbar;
    private List<LandingModel> landingList = new ArrayList<LandingModel>();
    ViewFlipper vf;
    SharedPreferences.Editor editor;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.material_landing_listview, container, false);
        //Destination fetching URL
        String url = Constants.API_LandingActivity_Region;



        vf=(ViewFlipper) rootView.findViewById(R.id.viewFlipper);

        SharedPreferences prefs = getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);


        SharedPreferences.Editor editor2=prefs.edit();
        editor2.putString("Profile","Hi");
        editor2.putString("ID","Hi");
        editor2.commit();

        Log.d("Flag testing2", "" + prefs.getInt("flag", 0) + " " + prefs.getInt("login_flag", 0));

        if(prefs.getInt("flag",0)==1 && prefs.getInt("login_flag",0)==1)
        {
            ((MainActivity) getActivity()).getSupportActionBar().hide();

            LinearLayout splash_screen=(LinearLayout) rootView.findViewById(R.id.splash_screen_id);
            //    LinearLayout login_form=(LinearLayout) view.findViewById(R.id.login_form);

            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;

            ImageView itr_logo=(ImageView) rootView.findViewById(R.id.itraveller_logo_);

            TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f,
                    height/8, 0.0f);          //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)
            animation.setDuration(2250);  // animation duration
            //    animation.setRepeatCount(5);  // animation repeat count
            animation.setRepeatMode(0);   // repeat animation (left to right, right to left )
            animation.setFillAfter(true);


            itr_logo.startAnimation(animation);


            splash_screen.postDelayed(new Runnable() {
                @Override
                public void run() {
                    vf.showNext();
                    ((MainActivity) getActivity()).getSupportActionBar().show();

                }
            }, 1200);

        }
        else if(prefs.getInt("flag",0)==1 && prefs.getInt("login_flag",0)==2)
        {
            SharedPreferences.Editor editor=prefs.edit();
            editor.putInt("login_flag", 1);
            editor.commit();
            vf.showNext();
        }
        else
        {
            vf.showNext();
        }


        progessbar = (ProgressBar) rootView.findViewById(R.id.progressBarLanding);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setRefreshing(true);

        //Fab Floating Button
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        //fab.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.hide();
            }
        });

        //Adding RecyclerView Object
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CardAdapaterLanding(landingList, getActivity());
        mRecyclerView.setAdapter(mAdapter);

        JsonCallForDestination();

        return rootView;
    }

    private void refreshContent() {
        mSwipeRefreshLayout.setRefreshing(true);
        JsonCallForDestination();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void JsonCallForDestination(){
        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET,
                Constants.API_LandingActivity_Region, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("Boolean", "" + response.getBoolean("success"));
                    Log.d("Error", ""+response.getJSONObject("error"));
                    Log.d("Payload_regions", ""+response.getJSONArray("payload").length());

                    // JSONObject jsonobj = response.getJSONObject("payload").get;
                    // Parsing json
                    int json_response_length=response.getJSONArray("payload").length();
                    for (int i = 0; i < json_response_length; i++) {

                        //Log.i("i value", "" + i);

                        JSONObject jsonarr = response.getJSONArray("payload").getJSONObject(i);
                        LandingModel landing_model = new LandingModel();

                        if(jsonarr.getInt("Slider") == 1) {

                            landing_model.setRegion_Id(jsonarr.getInt("Region_Id"));
                            landing_model.setRegion_Name(jsonarr.getString("Region_Name"));
                            landing_model.setEnable_Flag(jsonarr.getString("Enable_Flag"));
                            landing_model.setAlias(jsonarr.getString("Alias"));
                            landing_model.setSlider(jsonarr.getInt("Slider"));
                            landing_model.setHome_Page(jsonarr.getInt("Home_Page"));
                            landing_model.setTourism_Story(jsonarr.getString("Tourism_Story"));
                            landing_model.setRegion_Story(jsonarr.getString("Region_Story"));
                            landing_model.setLeft_Alias(jsonarr.getString("Left_Alias"));
                            landing_model.setPlaces_To_Visit(jsonarr.getString("Places_To_Visit"));
                            landing_model.setPage_Title(jsonarr.getString("Page_Title"));
                            landing_model.setPage_Description(jsonarr.getString("Page_Description"));
                            landing_model.setPage_Heading(jsonarr.getString("Page_Heading"));
                            landing_model.setAdvance(jsonarr.getInt("Advance"));
                            landing_model.setIntermediate_Payment(jsonarr.getInt("Intermediate_Payment"));
                            landing_model.setDate(jsonarr.getString("Date"));
                            landing_model.setAdmin_Id(jsonarr.getString("admin_Id"));
                            landing_model.setPopular(jsonarr.getString("Popular"));
                            landingList.add(landing_model);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                    HiddenControls();
                    SwipeRefreshOrNot();
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
                    //Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
                } else if( error instanceof ServerError) {
                } else if( error instanceof AuthFailureError) {
                } else if( error instanceof ParseError) {
                } else if( error instanceof NoConnectionError) {
                    //Toast.makeText(getActivity(), "No Internet Connection" ,Toast.LENGTH_LONG).show();
                } else if( error instanceof TimeoutError) {
                }
            }
        });
        strReq.setRetryPolicy(new DefaultRetryPolicy(10000,
                5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
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
}