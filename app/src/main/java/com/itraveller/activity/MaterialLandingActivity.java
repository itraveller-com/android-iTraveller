package com.itraveller.activity;

/**
 * Created by iTraveller on 10/28/2015.
 */

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.emilsjolander.components.StickyScrollViewItems.StickyScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.itraveller.R;
import com.itraveller.constant.Constants;
import com.itraveller.constant.Utility;
import com.itraveller.materialadapter.CardAdapater;
import com.itraveller.materialadapter.CardAdapaterLanding;
import com.itraveller.materialsearch.SearchAdapter;
import com.itraveller.materialsearch.SharedPreference;
import com.itraveller.materialsearch.Utils;
import com.itraveller.model.LandingModel;
import com.itraveller.model.SearchBarModel;
import com.itraveller.volley.AppController;
import com.melnykov.fab.FloatingActionButton;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MaterialLandingActivity extends Fragment implements ObservableScrollViewCallbacks {

    ObservableRecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    FloatingActionButton fab;
    ProgressBar progessbar;
    private List<LandingModel> landingList = new ArrayList<LandingModel>();
    private ArrayList<String> region_;
    private ArrayList<SearchBarModel> Filterregion_;
    SearchAdapter searchAdapter;
    private int mBaseTranslationY;
    private View mHeaderView;
    private View mToolbarView;
    private TextView toolSearch;
    private LinearLayout toolbarSearch;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.material_landing_listview, container, false);
        //Destination fetching URL
        mHeaderView = rootView.findViewById(R.id.header);
        mToolbarView = rootView.findViewById(R.id.toolbarnew);
        toolbarSearch = (LinearLayout) rootView.findViewById(R.id.search_bar);
        toolSearch = (TextView) rootView.findViewById(R.id.edt_tool_search);
        toolSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadToolBarSearch();
                toolbarSearch.setVisibility(View.GONE);
            }
        });


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
        /*fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.hide();
               loadToolBarSearch();
            }
        });*/

        //For Search Bar
        Filterregion_ = new ArrayList<SearchBarModel>();
        region_ = new ArrayList<String>();

        //Adding RecyclerView Object
        mRecyclerView = (ObservableRecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setScrollViewCallbacks(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(false);

        /*mLayoutManager = new LinearLayoutManager(getActivity());
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);*/

        mAdapter = new CardAdapaterLanding(landingList, getActivity());
        mRecyclerView.setAdapter(mAdapter);


       // fab.attachToRecyclerView(mRecyclerView);

        searchJson(Constants.API_LandingActivity_Search_Region);
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void loadToolBarSearch() {


        /*Window window = getActivity().getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(getActivity().getResources().getColor(R.color.orange));*/


        View view = getActivity().getLayoutInflater().inflate(R.layout.view_toolbar_search, null);
        LinearLayout parentToolbarSearch = (LinearLayout) view.findViewById(R.id.parent_toolbar_search);
        ImageView imgToolBack = (ImageView) view.findViewById(R.id.img_tool_back);
        final EditText edtToolSearch = (EditText) view.findViewById(R.id.edt_tool_search);
        ImageView imgToolMic = (ImageView) view.findViewById(R.id.img_tool_mic);
        final ListView listSearch = (ListView) view.findViewById(R.id.list_search);
        final TextView txtEmpty = (TextView) view.findViewById(R.id.txt_empty);

        Utils.setListViewHeightBasedOnChildren(listSearch);

        edtToolSearch.setHint("Search your Destination");

        final Dialog toolbarSearchDialog = new Dialog(getActivity(), R.style.MaterialSearch);
        toolbarSearchDialog.setContentView(view);
        toolbarSearchDialog.setCancelable(false);
        toolbarSearchDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        toolbarSearchDialog.getWindow().setGravity(Gravity.BOTTOM);
        toolbarSearchDialog.show();


        toolbarSearchDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        searchAdapter = new SearchAdapter(getActivity(), region_, false);

        listSearch.setVisibility(View.VISIBLE);
        listSearch.setAdapter(searchAdapter);

        //Shared Preference for checking flight(Domestic or International)
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("Itinerary", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();

        listSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.i("OnCLick", "Clicked" + searchAdapter.getItem(position));
                int Filterregion__size=Filterregion_.size();
                edtToolSearch.setText( searchAdapter.getItem(position).toString() );
                for(int i=0;i<Filterregion__size;i++)
                {
                    if(Filterregion_.get(i).getValue().equalsIgnoreCase(edtToolSearch.getText().toString()))
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
                listSearch.setVisibility(View.GONE);


            }
        });
        edtToolSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                //String[] country = getResources().getStringArray(R.array.countries_array);
                //mCountries = new ArrayList<String>(Arrays.asList(country));
                listSearch.setVisibility(View.VISIBLE);
                searchAdapter.updateList(region_, true);


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<String> filterList = new ArrayList<String>();
                boolean isNodata = false;
                if (s.length() > 0) {
                    for (int i = 0; i < region_.size(); i++) {


                        if (region_.get(i).toLowerCase().startsWith(s.toString().trim().toLowerCase())) {

                            filterList.add(region_.get(i));

                            listSearch.setVisibility(View.VISIBLE);
                            searchAdapter.updateList(filterList, true);
                            isNodata = true;
                        }
                    }
                    if (!isNodata) {
                        listSearch.setVisibility(View.GONE);
                        txtEmpty.setVisibility(View.VISIBLE);
                        txtEmpty.setText("No data found");
                    }
                } else {
                    listSearch.setVisibility(View.GONE);
                    txtEmpty.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolbarSearchDialog.dismiss();
                toolbarSearch.setVisibility(View.VISIBLE);
                //fab.show();
            }
        });

        imgToolMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edtToolSearch.setText("");

            }
        });
    }

    public void searchJson(String url)
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
               // searchAdapter.notifyDataSetChanged();
                //fab.setVisibility(View.VISIBLE);
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
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        //if (dragging) {
            int toolbarHeight = mToolbarView.getHeight();
            if (firstScroll) {
                float currentHeaderTranslationY = ViewHelper.getTranslationY(mHeaderView);
                if (-toolbarHeight < currentHeaderTranslationY) {
                    mBaseTranslationY = scrollY;
                }
            }
            float headerTranslationY = getFloat(-(scrollY - mBaseTranslationY), -toolbarHeight, 0);
            ViewPropertyAnimator.animate(mHeaderView).cancel();
            ViewHelper.setTranslationY(mHeaderView, headerTranslationY);
       // }
    }

    public static float getFloat(final float value, final float minValue, final float maxValue) {
        return Math.min(maxValue, Math.max(minValue, value));
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        mBaseTranslationY = 0;

        if (scrollState == ScrollState.DOWN) {
            showToolbar();
        } else if (scrollState == ScrollState.UP) {
            int toolbarHeight = mToolbarView.getHeight();
            int scrollY = mRecyclerView.getCurrentScrollY();
            if (toolbarHeight <= scrollY) {
                hideToolbar();
            } else {
                showToolbar();
            }
        } else {
            // Even if onScrollChanged occurs without scrollY changing, toolbar should be adjusted
            if (!toolbarIsShown() && !toolbarIsHidden()) {
                // Toolbar is moving but doesn't know which to move:
                // you can change this to hideToolbar()
                showToolbar();
            }
        }
    }

    private boolean toolbarIsShown() {
        return ViewHelper.getTranslationY(mHeaderView) == 0;
    }

    private boolean toolbarIsHidden() {
        return ViewHelper.getTranslationY(mHeaderView) == -mToolbarView.getHeight();
    }

    private void showToolbar() {
        float headerTranslationY = ViewHelper.getTranslationY(mHeaderView);
        if (headerTranslationY != 0) {
            ViewPropertyAnimator.animate(mHeaderView).cancel();
            ViewPropertyAnimator.animate(mHeaderView).translationY(0).setDuration(200).start();
        }
    }

    private void hideToolbar() {
        float headerTranslationY = ViewHelper.getTranslationY(mHeaderView);
        int toolbarHeight = mToolbarView.getHeight();
        if (headerTranslationY != -toolbarHeight) {
            ViewPropertyAnimator.animate(mHeaderView).cancel();
            ViewPropertyAnimator.animate(mHeaderView).translationY(-toolbarHeight).setDuration(200).start();
        }
    }
}