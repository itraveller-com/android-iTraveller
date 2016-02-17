package com.itraveller.dragsort;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
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
import com.itraveller.activity.HotelActivity;
import com.itraveller.adapter.AirportAdapter;
import com.itraveller.adapter.PortAndLocAdapter;
import com.itraveller.adapter.RearrangePlaceAdapter;
import com.itraveller.constant.Utility;
import com.itraveller.model.AirportModel;
import com.itraveller.model.PortAndLocModel;
import com.itraveller.model.RearrangePlaceModel;
import com.itraveller.volley.AppController;
import com.melnykov.fab.FloatingActionButton;
import com.mobeta.android.dslv.DragNDropSortListView;
import com.mobeta.android.dslv.DragSortController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DragAndSort extends AppCompatActivity
{

    public Intent intent;
    public static String sDestinationValue;
    public static String sDestinationDate;

    private SharedPreferences mSharedPreferences;
    private DragNDropSortListView mListView;
    private FloatingActionButton mFab;

    private ImageLoader mImageLoader = AppController.getInstance().getImageLoader();
    ////// New Page
    private List<AirportModel> mAirportList = new ArrayList<AirportModel>();
    private AirportAdapter mAdapterAirport;
    //Adapter for port and locations
    private List<PortAndLocModel> mPortandLocList = new ArrayList<PortAndLocModel>();
    private PortAndLocAdapter mAdapterPortandLoc;
    //Adapter for places and rearrange
    private List<RearrangePlaceModel> mRearrangeList = new ArrayList<RearrangePlaceModel>();
    private RearrangePlaceAdapter mAdapterRearrange;


    public static ListView sListView;
    private TextView mFromHome;
    private TextView mFromTravel;
    private TextView mToHome;
    private TextView mToTravel;
    public String mUrl;
    public static String sRegionId;
    private int mCheckBitNew = 0;
    private Button mHotelBook;
    private int mArrivalId;
    private int mDepartureId;
    private String mTravelFrom ;
    private String mTravelTo;
    private View mIncludeLoading;
    private LinearLayout mDestinationPage;
    private RelativeLayout mAirportListPage;

    private static final String TAG =DragAndSort.class.getName();
    private static final String ITINERARY="Itinerary";
    private static final String REGION_ID= "RegionID";
    private static final String SAVED_DATA= "SavedData";
    private static final String REGION_STRING="RegionString";
    private static final String TRAVEL_DATE="TravelDate";
    private static final String DATE_FORMAT="yyyy-MM-dd";
    private static final String DATE_FORMAT2="dd-MM-yyyy";
    private static  final String  ACTIVITIES="Activities";
    private static  final String TRANSPORTATION_ID="TransportationID";

   private DragNDropSortListView.DropListener onDrop = new DragNDropSortListView.DropListener()
    {
        @Override
        public void drop(int from, int to)
        {
            if (from != to)
            {

                RearrangePlaceModel item = mAdapterRearrange
                        .getList().get(from);
                mAdapterRearrange.remove(item);
                mAdapterRearrange.insert(item, to);
            }
        }
    };

    private DragNDropSortListView.RemoveListener onRemove = new DragNDropSortListView.RemoveListener()
    {
        @Override
        public void remove(int which)
        {
            mAdapterRearrange.remove(mAdapterRearrange.getList().get(which));
            Utility.setListViewHeightBasedOnChildren(mListView);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.destination);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Travel Destinations");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Saving the data for previous use
        SharedPreferences prefsSaved = getSharedPreferences(SAVED_DATA, MODE_PRIVATE);
        SharedPreferences.Editor editorSaved = prefsSaved.edit();
        editorSaved.putString(ACTIVITIES, null);
        editorSaved.putString(TRANSPORTATION_ID, null);
        editorSaved.commit();

        //Loading Layout View
        mIncludeLoading = findViewById(R.id.include_loading);
        mIncludeLoading.setVisibility(View.GONE);

        //Main Layout
        mDestinationPage = (LinearLayout) findViewById(R.id.destination_page);
        mAirportListPage = (RelativeLayout) findViewById(R.id.airport_listview);

        LinearLayout from_airport_sp = (LinearLayout) findViewById(R.id.from_home_airport);
        LinearLayout from_port_sp = (LinearLayout) findViewById(R.id.from_travel_port);
        LinearLayout to_port_sp = (LinearLayout) findViewById(R.id.to_travel_port);
        LinearLayout to_airport_sp = (LinearLayout) findViewById(R.id.to_home_airport);

        mSharedPreferences = getSharedPreferences(ITINERARY, Context.MODE_PRIVATE);
        sRegionId = "" + mSharedPreferences.getString(REGION_ID, null);


        //Fab Floating Button
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIncludeLoading.setVisibility(View.VISIBLE);
                mDestinationPage.setVisibility(View.GONE);
                mFab.setVisibility(View.GONE);
                mAirportListPage.setVisibility(View.VISIBLE);
                mHotelBook.setVisibility(View.GONE);
                String regionString = mSharedPreferences.getString(REGION_STRING,"");
                if(regionString.equalsIgnoreCase("") || regionString.equalsIgnoreCase(null) || regionString.length()==0) {
                    mUrl = "http://stage.itraveller.com/backend/api/v1/destination?regionId=" + sRegionId + "&port=0";
                    Log.v(TAG,"" +sRegionId);
                } else {
                    mUrl = "http://stage.itraveller.com/backend/api/v1/destination?regionId=" + regionString + "&port=0";
                    Log.v(TAG, "" + regionString);
                }
                Log.d(TAG,""+mUrl);
                airportJSON(mUrl, false);
                mCheckBitNew = 5;
            }
        });

        NetworkImageView img = (NetworkImageView) findViewById(R.id.thumbnail);
        TextView title_sp = (TextView) findViewById(R.id.title_rp);
        TextView duration_sp = (TextView) findViewById(R.id.duration_rp);
        mFromHome = (TextView) findViewById(R.id.from_home_destination);
        mFromTravel = (TextView) findViewById(R.id.from_travel_destination);
        mToHome = (TextView) findViewById(R.id.to_home_destination);
        mToTravel = (TextView) findViewById(R.id.to_travel_destination);

        mHotelBook = (Button) findViewById(R.id.book_hotel);

        //Default value set

        mFromHome.setText("MUMBAI");
        mTravelFrom = "BOM";
        mToHome.setText("MUMBAI");
        mTravelTo = "BOM";
        Bundle bundle = getIntent().getExtras();
        img.setImageUrl(bundle.getString("Image"), mImageLoader);
        img.setErrorImageResId(R.drawable.default_img);
        duration_sp.setText((bundle.getInt("Duration") - 1) + " Nights / " + bundle.getInt("Duration") + " Days");
        title_sp.setText(bundle.getString("Title"));



        String TestValue = bundle.getString("Destinations");
        final String Destination_Id = bundle.getString("DestinationsID");
        final String Destination_Count = bundle.getString("DestinationsCount");

        mUrl = "http://stage.itraveller.com/backend/api/v1/destination?regionId=" + sRegionId + "&port=1";

        Log.i(TAG,""+bundle.getInt("ArrivalPort"));
        Log.i(TAG,""+bundle.getInt("DeparturePort"));

            airportJSONForText("http://stage.itraveller.com/backend/api/v1/destination?regionId=" + sRegionId + "&port=1", bundle.getInt("ArrivalPort"), bundle.getInt("DeparturePort"));

        mListView = (DragNDropSortListView) findViewById(R.id.listview);
        String[] names = TestValue.trim().split(",");
        String[] destinationId = Destination_Id.trim().split(",");
        String[] mDestinationCount = Destination_Count.trim().split(",");

        mAdapterRearrange = new RearrangePlaceAdapter(this,mRearrangeList);
        mListView.setAdapter(mAdapterRearrange);
        mFab.attachToListView(mListView);

        for(int i = 0; i< names.length ; i++)
        {
            RearrangePlaceModel m = new RearrangePlaceModel();
            m.setPlace(names[i].toString().replace("\n"," "));
            m.setPlaceID(Integer.parseInt(destinationId[i]) );
            m.setNights(mDestinationCount[i]);
            mRearrangeList.add(m);
        }

        Utility.setListViewHeightBasedOnChildren(mListView);

        mAdapterRearrange.notifyDataSetChanged();

        mListView.setDropListener(onDrop);
        mListView.setRemoveListener(onRemove);
        final DragSortController controller = new DragSortController(mListView);
        controller.setDragHandleId(R.id.drag_handle);
        controller.setRemoveEnabled(true);
        controller.setSortEnabled(true);
        controller.setDragInitMode(1);
        mListView.setFloatViewManager(controller);


        mHotelBook.setEnabled(false);
        mHotelBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Destination_Count = null;
                String Destination_Name = null;
                String CheckDate = null;
                int TotalCount = 0;
                SharedPreferences prefsData = getSharedPreferences(ITINERARY, MODE_PRIVATE);

                String no_of_nights_str="";

                int rearrangeList_size=mRearrangeList.size();
                for(int i = 0; i< rearrangeList_size;i++) {
                    if(i==0) {
                        sDestinationValue = "" + mRearrangeList.get(i).getPlaceID();
                        Destination_Count = "" + mRearrangeList.get(i).getNights();
                        TotalCount = Integer.parseInt(Destination_Count);
                        Destination_Name = "" + mRearrangeList.get(i).getPlace();
                        CheckDate = Utility.addDays(prefsData.getString("DefaultDate", null).toString(), 0,DATE_FORMAT2,DATE_FORMAT);
                        sDestinationDate = "" + CheckDate ;
                    }
                    else
                    {
                        String Date_ = Utility.addDays(CheckDate,0,DATE_FORMAT,DATE_FORMAT2);
                        CheckDate  = Utility.addDays(Date_, Integer.parseInt(mRearrangeList.get(i-1).getNights()),DATE_FORMAT2,DATE_FORMAT);
                        sDestinationDate = sDestinationDate + "," + CheckDate ;
                        TotalCount = TotalCount + Integer.parseInt(mRearrangeList.get(i).getNights());
                        sDestinationValue = sDestinationValue + "," + mRearrangeList.get(i).getPlaceID();
                        Destination_Count = Destination_Count + "," + mRearrangeList.get(i).getNights();
                        Destination_Name = Destination_Name + "," + mRearrangeList.get(i).getPlace();
                    }
                    Log.v(TAG, "Date :" + sDestinationDate);
                    Log.d(TAG, "" + mRearrangeList.get(i).getNights());
                    no_of_nights_str+=""+mRearrangeList.get(i).getNights()+",";

                }

                Log.d(TAG, "" + no_of_nights_str);


                if(mAdapterRearrange.getCount() != 0) {
                    SharedPreferences sharedpreferences = getSharedPreferences(ITINERARY, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();

                    String no_of_dest[]=sDestinationValue.split(",");

                    editor.putInt("No_of_Destinations", no_of_dest.length);
                    editor.putString("DestinationID", sDestinationValue);
                    editor.putString("DestinationCount", Destination_Count);
                    editor.putString("DestinationName", Destination_Name);
                    editor.putString("DestinationDate", sDestinationDate);
                    editor.putString("EndDate", Utility.addDays(sharedpreferences.getString(TRAVEL_DATE, null), TotalCount, DATE_FORMAT, DATE_FORMAT));
                    Log.i(TAG, "" + Utility.addDays(sharedpreferences.getString(TRAVEL_DATE, null), TotalCount, DATE_FORMAT, DATE_FORMAT));
                    editor.putString("ArrivalAirport", "" + mTravelFrom);
                    editor.putString("DepartureAirport", "" + mTravelTo);
                    editor.putString("ArrivalPort", "" + mArrivalId);
                    editor.putString("DeparturePort", "" + mDepartureId);
                    editor.putString("TotalNightCount", "" + TotalCount);

                    //For Itinerary Summaray
                    editor.putString("ArrivalAirportString", "" + mFromHome.getText().toString().replace("\n"," "));
                    editor.putString("DepartureAirportString", "" + mToHome.getText().toString().replace("\n", " "));
                    editor.putString("ArrivalPortString", "" + mFromTravel.getText().toString().replace("\n", " "));
                    editor.putString("DeparturePortString", "" + mToTravel.getText().toString().replace("\n", " "));

                    editor.commit();

                    Log.d(TAG, "" + mToHome.getText());
                    Log.d(TAG,""+ mFromTravel.getText());
                    if(  !(""+ mFromTravel.getText()).equals("")) {
                        intent = new Intent(DragAndSort.this, HotelActivity.class);
                        intent.putExtra("DestinationsIDs", sDestinationValue);

                        editor.putString("No_of_nights_dest", "" + no_of_nights_str);
                        Log.d(TAG, "" + no_of_nights_str);

                        editor.commit();
                        Log.d(TAG, "" + sRegionId);

                        startActivity(intent);

                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Please Add Arrival and Departure Destination", Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(), "Please Add Destination", Toast.LENGTH_LONG).show();
                }
            }
        });

        from_airport_sp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIncludeLoading.setVisibility(View.VISIBLE);
                sListView.setAdapter(null);
                mDestinationPage.setVisibility(View.GONE);
                mFab.setVisibility(View.GONE);
                mAirportListPage.setVisibility(View.VISIBLE);
                mHotelBook.setVisibility(View.GONE);
                mUrl = "http://stage.itraveller.com/backend/api/v1/airport";
                airportJSON(mUrl, true);
                mCheckBitNew = 1;
            }
        });
        from_port_sp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIncludeLoading.setVisibility(View.VISIBLE);
                sListView.setAdapter(null);
                mDestinationPage.setVisibility(View.GONE);
                mFab.setVisibility(View.GONE);
                mAirportListPage.setVisibility(View.VISIBLE);
                mHotelBook.setVisibility(View.GONE);
                mUrl = "http://stage.itraveller.com/backend/api/v1/destination?regionId=" + sRegionId + "&port=1";
                airportJSON(mUrl, false);
                mCheckBitNew = 2;
            }
        });
        to_port_sp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIncludeLoading.setVisibility(View.VISIBLE);
                sListView.setAdapter(null);
                mDestinationPage.setVisibility(View.GONE);
                mFab.setVisibility(View.GONE);
                mAirportListPage.setVisibility(View.VISIBLE);
                mHotelBook.setVisibility(View.GONE);
                mUrl = "http://stage.itraveller.com/backend/api/v1/destination?regionId=" + sRegionId + "&port=1";
                airportJSON(mUrl, false);
                mCheckBitNew = 3;
            }
        });
        to_airport_sp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIncludeLoading.setVisibility(View.VISIBLE);
                sListView.setAdapter(null);//2
                mDestinationPage.setVisibility(View.GONE);
                mFab.setVisibility(View.GONE);
                mAirportListPage.setVisibility(View.VISIBLE);
                mHotelBook.setVisibility(View.GONE);
                mUrl = "http://stage.itraveller.com/backend/api/v1/airport";
                airportJSON(mUrl,true);
                mCheckBitNew = 4;
            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////////////
        // PLACE FIND ACTIVITY//////////////////////////////////////////////////////////////////////////////
        sListView = (ListView) findViewById(R.id.list);/////1
        SearchView searchAirport = (SearchView) findViewById(R.id.searchView);
        mAdapterAirport = new AirportAdapter(this,mAirportList);
        mAdapterPortandLoc = new PortAndLocAdapter(this, mPortandLocList);

        searchAirport.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if ((mCheckBitNew == 1) || (mCheckBitNew == 4))
                    mAdapterAirport.getFilter().filter(s);
                else
                    mAdapterPortandLoc.getFilter().filter(s);

                return false;
            }
        });

        sListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Log.i(TAG,"Test "+mCheckBitNew);
             if(mCheckBitNew == 1) {
                 mFromHome.setText(AirportAdapter.AirportItems.get(position).getValue().replace(" ","\n"));
                 mTravelFrom = "" +AirportAdapter.AirportItems.get(position).getKey();
                 mToHome.setText(AirportAdapter.AirportItems.get(position).getValue().replace(" ", "\n"));
                 mTravelTo = "" +AirportAdapter.AirportItems.get(position).getKey();

             }
                else if(mCheckBitNew == 2)
             {
                  mFromTravel.setText(PortAndLocAdapter.PortandLocItems.get(position).getValue().replace(" ", "\n"));
                 mArrivalId = PortAndLocAdapter.PortandLocItems.get(position).getKey();
                 mToTravel.setText(PortAndLocAdapter.PortandLocItems.get(position).getValue().replace(" ","\n"));
                 mDepartureId = PortAndLocAdapter.PortandLocItems.get(position).getKey();

             }
                else if(mCheckBitNew == 3)
             {
                 mToTravel.setText(PortAndLocAdapter.PortandLocItems.get(position).getValue().replace(" ","\n"));
                 mDepartureId = PortAndLocAdapter.PortandLocItems.get(position).getKey();

             }
                else if(mCheckBitNew == 4)
             {
                 mToHome.setText(AirportAdapter.AirportItems.get(position).getValue().replace(" ","\n"));
                 mTravelTo = ""+AirportAdapter.AirportItems.get(position).getKey();

             }
                else if(mCheckBitNew == 5){

                 RearrangePlaceModel m = new RearrangePlaceModel();
                 m.setPlace(PortAndLocAdapter.PortandLocItems.get(position).getValue());
                 m.setPlaceID(PortAndLocAdapter.PortandLocItems.get(position).getKey());
                 m.setNights("1");
                 mRearrangeList.add(m);
                 mAdapterRearrange.notifyDataSetChanged();
                 Utility.setListViewHeightBasedOnChildren(mListView);
             }
                mCheckBitNew=0;
                mDestinationPage.setVisibility(View.VISIBLE);
                mFab.setVisibility(View.VISIBLE);
                mAirportListPage.setVisibility(View.GONE);
                mHotelBook.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void airportJSON (String url ,final Boolean airport)
    {
        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.i(TAG, "Testing" + response);
                    Log.d(TAG, "" + response.getBoolean("success"));
                    Log.d(TAG, "" + response.getJSONObject("error"));
                    Log.d(TAG, ""+ airport);
                    mAirportList.clear();
                    mPortandLocList.clear();
                    int response_JSON_arr_length=response.getJSONArray("payload").length();
                    for (int i = 0; i < response_JSON_arr_length; i++) {
                        JSONObject jsonarr = response.getJSONArray("payload").getJSONObject(i);
                        if(airport) {

                            AirportModel airport_model = new AirportModel();

                            Log.i(TAG,""+jsonarr.getString("value"));
                            airport_model.setKey(jsonarr.getString("key"));
                            airport_model.setValue(jsonarr.getString("value"));
                            mAirportList.add(airport_model);
                            sListView.setAdapter(mAdapterAirport);
                        }
                        else
                        {
                            PortAndLocModel portandloc_model = new PortAndLocModel();

                            portandloc_model.setValue(jsonarr.getString("value"));
                            portandloc_model.setKey(jsonarr.getInt("key"));

                            mPortandLocList.add(portandloc_model);

                            sListView.setAdapter(mAdapterPortandLoc);

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    VolleyLog.d("Volley Error", "Error: " + e.getMessage());
                }
                mAdapterAirport.notifyDataSetChanged();
                mAdapterPortandLoc.notifyDataSetChanged();
                mIncludeLoading.setVisibility(View.GONE);

            }
        },  new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Volley Error", "Error: " + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(strReq);
    }



    public void airportJSONForText (String url ,final int arrival, final int departure)
    {

        final ProgressDialog pDialog = new ProgressDialog(DragAndSort.this);
        pDialog.setMessage("Loading...");
        pDialog.show();


        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.i(TAG, "Testing" + response);
                    Log.d(TAG, "" + response.getBoolean("success"));
                    Log.d(TAG, "" + response.getJSONObject("error"));
                    Log.d(TAG, ""+response.getJSONArray("payload"));
                    Log.d(TAG, "Arr"+arrival);

                    int response_JSON_arr_length=response.getJSONArray("payload").length();
                    for (int i = 0; i < response_JSON_arr_length; i++) {
                        JSONObject jsonarr = response.getJSONArray("payload").getJSONObject(i);
                        Log.d(TAG, ""+jsonarr.getInt("key"));
                         if(jsonarr.getInt("key") == arrival)
                         {
                             mArrivalId = arrival;
                             mDepartureId = departure;
                              mFromTravel.setText(jsonarr.getString("value").replace(" ", "\n"));
                             mToTravel.setText(jsonarr.getString("value").replace(" ", "\n"));
                         }
                        else if(jsonarr.getInt("key") == departure)
                         {
                             mDepartureId = departure;
                             mToTravel.setText(jsonarr.getString("value").replace(" ","\n"));
                         }
                     }
                    pDialog.dismiss();
                    mHotelBook.setEnabled(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                    VolleyLog.d("Volley Error", "Error: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                // Handle your error types accordingly.For Timeout & No connection error, you can show 'retry' button.
                // For AuthFailure, you can re login with user credentials.
                // For ClientError, 400 & 401, Errors happening on client side when sending api request.
                // In this case you can check how client is forming the api and debug accordingly.
                // For ServerError 5xx, you can do retry or handle accordingly.
                if( error instanceof NetworkError) {

                    pDialog.hide();
                    Toast.makeText(DragAndSort.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                } else if( error instanceof ServerError) {
                } else if( error instanceof AuthFailureError) {
                } else if( error instanceof ParseError) {
                } else if( error instanceof NoConnectionError) {
                    pDialog.hide();
                    Toast.makeText(DragAndSort.this, "No Internet Connection" ,Toast.LENGTH_LONG).show();
                } else if( error instanceof TimeoutError) {
                }
            }
        }) {
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(10000,
                5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq);
    }

    public void onBackPressed() {
        Log.i(TAG, "" + mCheckBitNew);
        if(mCheckBitNew==0)
        {
            finish();
        }
        else
        {
            mIncludeLoading.setVisibility(View.GONE);
            sListView.setAdapter(null);
            mDestinationPage.setVisibility(View.VISIBLE);
            mFab.setVisibility(View.VISIBLE);
            mAirportListPage.setVisibility(View.GONE);
            mHotelBook.setVisibility(View.VISIBLE);
            mCheckBitNew = 0;
        }
    }

}

