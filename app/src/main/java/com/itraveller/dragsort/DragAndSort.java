package com.itraveller.dragsort;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
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
import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;

import org.json.JSONException;
import org.json.JSONObject;

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

public class DragAndSort extends ActionBarActivity
{
    DragSortListView listView;
    String[] names;
   // ArrayAdapter<String> adapter;
    Toolbar mToolbar;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    ////// New Page
    private List<AirportModel> airportList = new ArrayList<AirportModel>();
    private AirportAdapter adapter_airport;
    //Adapter for port and locations
    private List<PortAndLocModel> portandLocList = new ArrayList<PortAndLocModel>();
    private PortAndLocAdapter adapter_portandLoc;
    //Adapter for places and rearrange
    private List<RearrangePlaceModel> rearrangeList = new ArrayList<RearrangePlaceModel>();
    private RearrangePlaceAdapter adapter_rearrange;

    public static ListView listview;
    private SearchView search_airport;
    private TextView from_home , from_travel , to_home ,to_travel;
    private String url;
    private String region_id;
    private int check_bit_new = 0;
    private ScrollView scroll;
    private Button hotelBook;
    private String[] destination_ID;
    private String[] destination_Count;
    private int arrival_id,departure_id;
    private String travelfrom , travelto;


    private LinearLayout destination_page;
    private LinearLayout airportlist_page;

   private DragSortListView.DropListener onDrop = new DragSortListView.DropListener()
    {
        @Override
        public void drop(int from, int to)
        {
            if (from != to)
            {
                Log.i("TestData1","Test");
                RearrangePlaceModel item = adapter_rearrange.getList().get(from);
                adapter_rearrange.remove(item);
                adapter_rearrange.insert(item, to);
            }
        }
    };

    private DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener()
    {
        @Override
        public void remove(int which)
        {
            adapter_rearrange.remove(adapter_rearrange.getList().get(which));
         //   LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, rearrangeList.size()* (dpToPx(70)));
          //  listView.setLayoutParams(lp);
            Utility.setListViewHeightBasedOnChildren(listView);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.destination);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle("Travel Destinations");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Main Layout
        destination_page = (LinearLayout) findViewById(R.id.destination_page);
        airportlist_page = (LinearLayout) findViewById(R.id.airport_listview);

        LinearLayout from_airport_sp = (LinearLayout) findViewById(R.id.from_home_airport);
        LinearLayout from_port_sp = (LinearLayout) findViewById(R.id.from_travel_port);
        LinearLayout to_port_sp = (LinearLayout) findViewById(R.id.to_travel_port);
        LinearLayout to_airport_sp = (LinearLayout) findViewById(R.id.to_home_airport);
        ImageButton add_new = (ImageButton) findViewById(R.id.imageButton);


        NetworkImageView img = (NetworkImageView) findViewById(R.id.thumbnail);
        TextView title_sp = (TextView) findViewById(R.id.title_rp);
        TextView duration_sp = (TextView) findViewById(R.id.duration_rp);
        scroll = (ScrollView) findViewById(R.id.scrollView);
        from_home = (TextView) findViewById(R.id.from_home_destination);
        from_travel = (TextView) findViewById(R.id.from_travel_destination);
        to_home = (TextView) findViewById(R.id.to_home_destination);
        to_travel = (TextView) findViewById(R.id.to_travel_destination);

        hotelBook = (Button) findViewById(R.id.book_hotel);

        //Default value set
        from_home.setText("MUMBAI");
        travelfrom = "BOM";
        to_home.setText("MUMBAI");
        travelto = "BOM";
        Bundle bundle = getIntent().getExtras();
        img.setImageUrl(bundle.getString("Image"), imageLoader);
        duration_sp.setText((bundle.getInt("Duration") - 1) + " Nights / " + bundle.getInt("Duration") + " Days");
        title_sp.setText(bundle.getString("Title"));
        //region_id = bundle.getInt("RegionID");

        SharedPreferences sharedpreferences = getSharedPreferences("Itinerary", Context.MODE_PRIVATE);

        region_id = "" + sharedpreferences.getString("RegionID", null);
        String TestValue = bundle.getString("Destinations");
        final String Destination_Id = bundle.getString("DestinationsID");
        final String Destination_Count = bundle.getString("DestinationsCount");

        url = "http://stage.itraveller.com/backend/api/v1/destination?regionId=" + region_id + "&port=1";

        Log.i("PortID",""+bundle.getInt("ArrivalPort"));
        Log.i("PortID",""+bundle.getInt("DeparturePort"));

        airportJSONForText("http://stage.itraveller.com/backend/api/v1/destination?regionId=" + region_id + "&port=1", bundle.getInt("ArrivalPort"), bundle.getInt("DeparturePort"));

        listView = (DragSortListView) findViewById(R.id.listview);
        names = TestValue.trim().split(",");
        destination_ID = Destination_Id.trim().split(",");
        destination_Count = Destination_Count.trim().split(",");

        adapter_rearrange = new RearrangePlaceAdapter(this,rearrangeList);
        listView.setAdapter(adapter_rearrange);

        /*SharedPreferences sharedpreferences = getSharedPreferences("Itinerary", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();*/

        //listView.setAdapter(adapter);

        for(int i = 0; i<names.length ; i++)
        {
            RearrangePlaceModel m = new RearrangePlaceModel();
            m.setPlace(names[i]);
            m.setPlaceID(Integer.parseInt(destination_ID[i]));
            m.setNights(destination_Count[i]);
            rearrangeList.add(m);
        }
        add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                destination_page.setVisibility(View.GONE);
                airportlist_page.setVisibility(View.VISIBLE);
                url = "http://stage.itraveller.com/backend/api/v1/destination?regionId=" + region_id + "&port=0";
                airportJSON(url, false);
                check_bit_new = 5;
            }
        });

        //LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, rearrangeList.size()* (dpToPx(70)) );
        //listView.setLayoutParams(lp);
        Utility.setListViewHeightBasedOnChildren(listView);

        adapter_rearrange.notifyDataSetChanged();

        listView.setDropListener(onDrop);
        listView.setRemoveListener(onRemove);
        final DragSortController controller = new DragSortController(listView);
        controller.setDragHandleId(R.id.drag_handle);
        //controller.setClickRemoveId(R.id.);
        controller.setRemoveEnabled(true);
        controller.setSortEnabled(true);
        controller.setDragInitMode(1);

        //controller.setRemoveMode(removeMode);

        listView.setFloatViewManager(controller);
        //listView.setOnTouchListener(controller);
        //listView.setOnLongClickListener(controller);
        listView.setDragEnabled(true);
       /* listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                controller.onLongPress(motionEvent);
                return false;
            }
        });*/
        hotelBook.setEnabled(false);
        hotelBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Destination_Value = null;
                String Destination_Count = null;
                String Destination_Name = null;
                String Destination_Date = null;
                String CheckDate = null;
                int TotalCount = 0;
                SharedPreferences prefsData = getSharedPreferences("Itinerary", MODE_PRIVATE);
                int rearrangeList_size=rearrangeList.size();
                for(int i = 0; i< rearrangeList_size;i++) {
                   /* Log.v("TestData","Data :"+rearrangeList.get(i).getPlace());
                    Log.v("TestData","Data :"+rearrangeList.get(i).getPlaceID());*/
                    if(i==0) {
                        Destination_Value = "" + rearrangeList.get(i).getPlaceID();
                        Destination_Count = "" + rearrangeList.get(i).getNights();
                        TotalCount = Integer.parseInt(Destination_Count.toString());
                        Destination_Name = "" + rearrangeList.get(i).getPlace();
                        CheckDate = Utility.addDays(prefsData.getString("DefaultDate", null).toString(), 0,"dd-MM-yyyy","yyyy-MM-dd");
                        Destination_Date = "" + CheckDate ;
                    }
                    else
                    {
                        String Date_ = Utility.addDays(CheckDate,0,"yyyy-MM-dd","dd-MM-yyyy");
                        CheckDate  = Utility.addDays(Date_, Integer.parseInt(rearrangeList.get(i-1).getNights()),"dd-MM-yyyy","yyyy-MM-dd");
                        Destination_Date = Destination_Date + "," + CheckDate ;
                        TotalCount = TotalCount + Integer.parseInt(rearrangeList.get(i).getNights());
                        Destination_Value = Destination_Value + "," + rearrangeList.get(i).getPlaceID();
                        Destination_Count = Destination_Count + "," + rearrangeList.get(i).getNights();
                        Destination_Name = Destination_Name + "," + rearrangeList.get(i).getPlace();
                    }
                    Log.v("TestDataDate","Date :"+Destination_Date);
                }

                if(adapter_rearrange.getCount() != 0) {
                    SharedPreferences sharedpreferences = getSharedPreferences("Itinerary", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();

                    editor.putString("DestinationID", Destination_Value);
                    editor.putString("DestinationCount", Destination_Count);
                    editor.putString("DestinationName", Destination_Name);
                    editor.putString("DestinationDate", Destination_Date);
                    editor.putString("EndDate", Utility.addDays(sharedpreferences.getString("TravelDate", null), TotalCount, "yyyy-MM-dd", "yyyy-MM-dd"));
                    Log.i("EndDate", "" + Utility.addDays(sharedpreferences.getString("TravelDate", null), TotalCount, "yyyy-MM-dd", "yyyy-MM-dd"));
                    editor.putString("ArrivalAirport", "" + travelfrom);
                    editor.putString("DepartureAirport", "" + travelto);
                    editor.putString("ArrivalPort", "" + arrival_id);
                    editor.putString("DeparturePort", "" + departure_id);
                    //For Itinerary Summaray
                    editor.putString("ArrivalAirportString", "" + from_home.getText());
                    editor.putString("DepartureAirportString", "" + to_home.getText());
                    editor.putString("ArrivalPortString", "" + from_travel.getText());
                    editor.putString("DeparturePortString", "" + to_travel.getText());
                    editor.commit();
                    Log.d("Test dest",""+to_home.getText());
                    Log.d("Test dest1",""+from_travel.getText());
                    if(  !(""+from_travel.getText()).equals("")) {
                        Intent intent = new Intent(DragAndSort.this, HotelActivity.class);
                        intent.putExtra("DestinationsIDs", Destination_Value);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Please Add Destination", Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(), "Please Add Destination", Toast.LENGTH_LONG).show();
                }
            }
        });

        from_airport_sp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("ValueData", "" + rearrangeList.get(0).getPlace());
                /*final Intent i = new Intent(DragAndSort.this, AirportList.class);
                i.putExtra("Url", "http://stage.itraveller.com/backend/api/v1/airport");
                i.putExtra("Place" , 1);
                startActivity(i);*/
                destination_page.setVisibility(View.GONE);
                airportlist_page.setVisibility(View.VISIBLE);
                url = "http://stage.itraveller.com/backend/api/v1/airport";
                airportJSON(url, true);
                check_bit_new = 1;
            }
        });
        from_port_sp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                destination_page.setVisibility(View.GONE);
                airportlist_page.setVisibility(View.VISIBLE);
                url = "http://stage.itraveller.com/backend/api/v1/destination?regionId=" + region_id + "&port=1";
                airportJSON(url, false);
                check_bit_new = 2;
            }
        });
        to_port_sp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                destination_page.setVisibility(View.GONE);
                airportlist_page.setVisibility(View.VISIBLE);
                url = "http://stage.itraveller.com/backend/api/v1/destination?regionId=" + region_id + "&port=1";
                airportJSON(url, false);
                check_bit_new = 3;
            }
        });
        to_airport_sp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                destination_page.setVisibility(View.GONE);
                airportlist_page.setVisibility(View.VISIBLE);
                url = "http://stage.itraveller.com/backend/api/v1/airport";
                airportJSON(url,true);
                check_bit_new = 4;
            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////////////
        // PLACE FIND ACTIVITY//////////////////////////////////////////////////////////////////////////////
        listview = (ListView) findViewById(R.id.list);
        search_airport = (SearchView) findViewById(R.id.searchView);
        adapter_airport = new AirportAdapter(this,airportList);
        adapter_portandLoc = new PortAndLocAdapter(this, portandLocList);

        search_airport.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if ((check_bit_new == 1) || (check_bit_new == 4))
                    adapter_airport.getFilter().filter(s);
                else
                    adapter_portandLoc.getFilter().filter(s);

                return false;
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                //view.getId();
                Log.i("Hellon","Test "+check_bit_new);
             if(check_bit_new == 1) {
                 from_home.setText(AirportAdapter.AirportItems.get(position).getValue());
                 travelfrom = "" +AirportAdapter.AirportItems.get(position).getKey();
                 to_home.setText(AirportAdapter.AirportItems.get(position).getValue());
                 travelto = "" +AirportAdapter.AirportItems.get(position).getKey();

             }
                else if(check_bit_new == 2)
             {
                 from_travel.setText(PortAndLocAdapter.PortandLocItems.get(position).getValue());
                 arrival_id = PortAndLocAdapter.PortandLocItems.get(position).getKey();
                 to_travel.setText(PortAndLocAdapter.PortandLocItems.get(position).getValue());
                 departure_id = PortAndLocAdapter.PortandLocItems.get(position).getKey();

             }
                else if(check_bit_new == 3)
             {
                 to_travel.setText(PortAndLocAdapter.PortandLocItems.get(position).getValue());
                 departure_id = PortAndLocAdapter.PortandLocItems.get(position).getKey();

             }
                else if(check_bit_new == 4)
             {
                 to_home.setText(AirportAdapter.AirportItems.get(position).getValue());
                 travelto = ""+AirportAdapter.AirportItems.get(position).getKey();

             }
                else if(check_bit_new == 5){

                 RearrangePlaceModel m = new RearrangePlaceModel();
                 m.setPlace(PortAndLocAdapter.PortandLocItems.get(position).getValue());
                 m.setPlaceID(PortAndLocAdapter.PortandLocItems.get(position).getKey());
                 m.setNights("1");
                 rearrangeList.add(m);
                 adapter_rearrange.notifyDataSetChanged();
                // LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, rearrangeList.size()*70 );
                // listView.setLayoutParams(lp);
                 Utility.setListViewHeightBasedOnChildren(listView);
             }
                check_bit_new=0;
                destination_page.setVisibility(View.VISIBLE);
                airportlist_page.setVisibility(View.GONE);
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
                    Log.i("Test", "Testing" + response);
                    Log.d("Boolean", "" + response.getBoolean("success"));
                    Log.d("Error", "" + response.getJSONObject("error"));
                    Log.d("PayloadValue", ""+ airport);
                    airportList.clear();
                    portandLocList.clear();
                    // JSONObject jsonobj = response.getJSONObject("payload").get;
                    // Parsing json
                    int response_JSON_arr_length=response.getJSONArray("payload").length();
                    for (int i = 0; i < response_JSON_arr_length; i++) {
                        JSONObject jsonarr = response.getJSONArray("payload").getJSONObject(i);
                        if(airport == true) {

                            AirportModel airport_model = new AirportModel();

                            Log.i("AirportValue",""+jsonarr.getString("value"));
                            airport_model.setKey(jsonarr.getString("key"));
                            //airport_model.setCode(jsonarr.getString("Code"));
                            airport_model.setValue(jsonarr.getString("value"));
                            /*airport_model.setLat(jsonarr.getString("Lat"));
                            airport_model.setLong(jsonarr.getString("Long"));
                            airport_model.setTimezone(jsonarr.getString("Timezone"));
                            airport_model.setCity(jsonarr.getString("City"));
                            airport_model.setCountry(jsonarr.getString("Country"));
                            airport_model.setCountry_Code(jsonarr.getString("Country_Code"));
                            airport_model.setStatus(jsonarr.getInt("Status"));*/
                            airportList.add(airport_model);
                            listview.setAdapter(adapter_airport);
                        }
                        else
                        {
                            PortAndLocModel portandloc_model = new PortAndLocModel();

                            portandloc_model.setValue(jsonarr.getString("value"));
                            portandloc_model.setKey(jsonarr.getInt("key"));
                            /*portandloc_model.setDestination_Id(jsonarr.getInt("Destination_Id"));
                            portandloc_model.setDestination_Name(jsonarr.getString("Destination_Name"));
                            portandloc_model.setDistrict(jsonarr.getString("District"));
                            portandloc_model.setState(jsonarr.getString("State"));
                            portandloc_model.setPivot_Latitude(jsonarr.getInt("Pivot_Latitude"));
                            portandloc_model.setPivot_Longitude(jsonarr.getInt("Pivot_Longitude"));
                            portandloc_model.setEnable_Flag(jsonarr.getInt("Enable_Flag"));
                            portandloc_model.setAlias(jsonarr.getString("Alias"));
                            portandloc_model.setStory(jsonarr.getString("Story"));
                            portandloc_model.setPage_Title(jsonarr.getString("Page_Title"));
                            portandloc_model.setPage_Description(jsonarr.getString("Page_Description"));
                            portandloc_model.setPage_Heading(jsonarr.getString("Page_Heading"));
                            portandloc_model.setMin_Stay(jsonarr.getInt("Min_Stay"));
                            portandloc_model.setPort(jsonarr.getInt("Port"));
                            portandloc_model.setCode(jsonarr.getString("Code"));
                            portandloc_model.setSight_Seeing(jsonarr.getInt("Sight_Seeing"));
                            portandloc_model.setSealevel(jsonarr.getString("Sealevel"));
                            portandloc_model.setType(jsonarr.getString("Type"));
                            portandloc_model.setSummer_Temp(jsonarr.getString("Summer_Temp"));
                            portandloc_model.setWinter_Temp(jsonarr.getString("Winter_Temp"));
                            portandloc_model.setRegion(jsonarr.getInt("Region"));
                            portandloc_model.setDate(jsonarr.getString("Date"));
                            portandloc_model.setAdmin_Id(jsonarr.getString("admin_Id"));*/

                            portandLocList.add(portandloc_model);

                            listview.setAdapter(adapter_portandLoc);

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    VolleyLog.d("Volley Error", "Error: " + e.getMessage());
                }
                adapter_airport.notifyDataSetChanged();
                adapter_portandLoc.notifyDataSetChanged();


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
                    Log.i("Test", "Testing" + response);
                    Log.d("Boolean", "" + response.getBoolean("success"));
                    Log.d("Error", "" + response.getJSONObject("error"));
                    Log.d("Payload", ""+response.getJSONArray("payload"));
                    Log.d("KeyValueKey", "Arr"+arrival);
                    // JSONObject jsonobj = response.getJSONObject("payload").get;
                    // Parsing json
                    int response_JSON_arr_length=response.getJSONArray("payload").length();
                    for (int i = 0; i < response_JSON_arr_length; i++) {
                        JSONObject jsonarr = response.getJSONArray("payload").getJSONObject(i);
                        Log.d("KeyValueKey", ""+jsonarr.getInt("key"));
                         if(jsonarr.getInt("key") == arrival)
                         {
                             arrival_id = arrival;
                             departure_id = departure;
                             from_travel.setText(jsonarr.getString("value"));
                             to_travel.setText(jsonarr.getString("value"));
                         }
                        else if(jsonarr.getInt("key") == departure)
                         {
                             departure_id = departure;
                             to_travel.setText(jsonarr.getString("value"));
                         }
                     }
                    pDialog.dismiss();
                    hotelBook.setEnabled(true);
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
        Log.i("CheckBitValue",""+check_bit_new);
        if(check_bit_new==0)
        {
            finish();
        }
        else
        {
            destination_page.setVisibility(View.VISIBLE);
            airportlist_page.setVisibility(View.GONE);
        }
    }
}

