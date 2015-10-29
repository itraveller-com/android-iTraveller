package com.itraveller.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

import com.itraveller.adapter.LandingAdapter;
import com.itraveller.constant.Constants;
import com.itraveller.model.LandingModel;
import com.itraveller.model.SearchBarModel;
import com.itraveller.volley.AppController;
import com.itraveller.R;


public class LandingActivity extends Fragment {

    private ListView listView;
    private LandingAdapter adapter;
    private List<LandingModel> landingList = new ArrayList<LandingModel>();
    private AutoCompleteTextView searchText;
    private ArrayList<String> region_;
    private ArrayList<SearchBarModel> Filterregion_;
    private ArrayAdapter<String> region_adapter;
    private int preLast = 0;
    // Animation
    Animation animSlideup,animSlidedown;

    public LandingActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.landing_listview, container, false);

        String url = Constants.API_LandingActivity_Region; //"http://stage.itraveller.com/backend/api/v1/region";
        String search_url = Constants.API_LandingActivity_Search_Region; //"http://stage.itraveller.com/backend/api/v1/region/places";

        Filterregion_ = new ArrayList<SearchBarModel>();
        region_ = new ArrayList<String>();
        listView = (ListView) rootView.findViewById(R.id.list);

        //Shared Preference for checking flight(Domestic or International)
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("Itinerary", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();


        searchText = (AutoCompleteTextView) rootView.findViewById(R.id.searchtext);
        //searchText.setFocusable(false);
        region_adapter = new ArrayAdapter<String>(getActivity() ,android.R.layout.simple_list_item_1, region_);
        searchText.setThreshold(1);//will start working from first character
        searchText.setAdapter(region_adapter);//setting the adapter data into the AutoCompleteTextView
        //searchText.setTextColor(Color.RED);
        searchText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int postion, long l) {

                int Filterregion__size=Filterregion_.size();
                for(int i=0;i<Filterregion__size;i++)
                {
                    if(Filterregion_.get(i).getValue().equalsIgnoreCase(searchText.getText().toString()))
                    {
                        //Log.i("OnCLick", "Clicked" + Filterregion_.get(i).getKey());
                        String[] Region_id = Filterregion_.get(i).getKey().trim().split("/");
                        int length = Filterregion_.get(i).getKey().trim().split("/").length;
                        Log.i("OnCLick", "Clicked" + Integer.parseInt(Region_id[length-1]));

                        final Intent in = new Intent(getActivity(), RegionPlace.class);
                        in.putExtra("RegionID", Integer.parseInt(Region_id[length-1]));
                        in.putExtra("RegionName", Filterregion_.get(i).getValue());
                        String flightBit="";
                        int landingList_size=landingList.size();
                        for(int index = 0; index < landingList_size ; index ++)
                        {
                            if(Integer.parseInt(Region_id[length-1]) == landingList.get(index).getRegion_Id()){
                                flightBit = ""+landingList.get(index).getHome_Page();
                            }
                        }
                        editor.putString("FlightBit", flightBit);
                        editor.commit();
                        startActivity(in);
                        getActivity().finish();
                    }
                }
            }
        });
        adapter = new LandingAdapter(getActivity(), landingList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final Intent i = new Intent(getActivity(), RegionPlace.class);
                i.putExtra("RegionID", landingList.get(position).getRegion_Id());
                i.putExtra("RegionName", landingList.get(position).getRegion_Name());
                editor.putString("FlightBit", "" + landingList.get(position).getHome_Page());
                editor.commit();
                startActivity(i);
                getActivity().finish();
            }
        });

        // load the animation
        animSlidedown = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_down);
        animSlideup = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_up);

        // set animation listener
        animSlideup.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


            }
        });

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET,
                Constants.API_LandingActivity_Region, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("Response in LandingPage",""+response.toString());
                    Log.d("Boolean", ""+response.getBoolean("success"));
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
                            Log.d("Response in Lan1",""+jsonarr.getInt("Region_Id"));
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

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pDialog.hide();
                adapter.notifyDataSetChanged();

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
                    Toast.makeText(getActivity(), "No Internet Connection" ,Toast.LENGTH_LONG).show();
                } else if( error instanceof ServerError) {
                } else if( error instanceof AuthFailureError) {
                } else if( error instanceof ParseError) {
                } else if( error instanceof NoConnectionError) {
                    pDialog.hide();
                    Toast.makeText(getActivity(), "No Internet Connection" ,Toast.LENGTH_LONG).show();
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
        // Inflate the layout for this fragment
        serachJson(Constants.API_LandingActivity_Search_Region);
        return rootView;
    }

    public void serachJson(String url)
    {
        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("Boolean", ""+response.getBoolean("success"));
                    Log.d("Error", ""+response.getJSONObject("error"));
                    Log.d("Payload", ""+response.getJSONArray("payload"));

                    // Parsing json
                    for (int i = 0; i < response.getJSONArray("payload").length(); i++) {

                        JSONObject jsonarr = response.getJSONArray("payload").getJSONObject(i);
                        SearchBarModel search_bar = new SearchBarModel();
                        search_bar.setValue(jsonarr.getString("value"));
                        search_bar.setKey(jsonarr.getString("key"));
                        Filterregion_.add(search_bar);
                        region_.add(jsonarr.getString("value"));


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                region_adapter.notifyDataSetChanged();
                searchText.setFocusableInTouchMode(true);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Volley Error", "Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStop() {
        super.onStop();
        //EasyTracker.getInstance(this).activityStop(this);
    }



}

