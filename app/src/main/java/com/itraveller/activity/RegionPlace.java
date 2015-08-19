package com.itraveller.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.itraveller.R;
import com.itraveller.adapter.RegionPlaceAdapter;
import com.itraveller.model.RegionPlaceModel;
import com.itraveller.volley.AppController;
/**
 * Created by VNK on 6/9/2015.
 */
public class RegionPlace extends ActionBarActivity {

    private String url = "http://stage.itraveller.com/backend/api/v1/itinerary/regionId/";
    private List<RegionPlaceModel> regionList = new ArrayList<RegionPlaceModel>();
    private RegionPlaceAdapter adapter;
    private ListView listView;
    private Toolbar mToolbar;
    public static final String MY_PREFS = "ScreenHeight";
    private  int _screen_height;
    int toggle =0;
    private Button filter_btn;
    private LinearLayout filter_details;
    private Bundle bundle;
    int min_value = 0, max_value = 0;
    LinearLayout no_data_lay;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.region_place_listview);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        filter_btn = (Button) findViewById(R.id.addbtnfilter);
        final TextView minval = (TextView) findViewById(R.id.minvalue);
        final TextView maxval = (TextView) findViewById(R.id.maxvalue);
        filter_details = (LinearLayout) findViewById(R.id.filterdetails);
        final TextView day_night = (TextView) findViewById(R.id.day_night);
        final ImageButton sub_btn = (ImageButton) findViewById(R.id.subbtn);
        sub_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(toggle >=  1) {
                    toggle--;
                }
                if(toggle == 0){
                    day_night.setText("All");
                }
                else if(toggle == 1) {
                    day_night.setText(toggle + " Night");
                }
                else {
                    day_night.setText(toggle + " Nights");
                }
            }
        });
        final ImageButton add_btn = (ImageButton) findViewById(R.id.addbtn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle++;
                if(toggle == 1) {
                    day_night.setText(toggle + " Night");
                }
                else {
                    day_night.setText(toggle + " Nights");
                }
            }
        });
        final Button filter_btn = (Button) findViewById(R.id.addbtnfilter);

        filter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(filter_btn.getText().toString().equalsIgnoreCase("Filter")) {
                    filter_details.setVisibility(View.VISIBLE);
                    filter_btn.setText("Apply Filter");

                    //adapter.filter(Integer.parseInt(minval.getText().toString()),Integer.parseInt(maxval.getText().toString()));
                }
                else
                {
                    filter_details.setVisibility(View.GONE);
                    filter_btn.setText("Filter");
                    adapter.getFilter().filter(minval.getText().toString() + "," + maxval.getText().toString() + "," + (toggle));
                }
            }
        });
        SharedPreferences prefs = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        _screen_height = prefs.getInt("Screen_Height", 0)-(prefs.getInt("Status_Height", 0) + prefs.getInt("ActionBar_Height", 0));
        Log.i("iTraveller", "Screen Height: " + _screen_height);
        int width = prefs.getInt("Screen_Width", 0); //0 is the default value.
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width,(_screen_height - 60));
        filter_details.setLayoutParams(lp);
        filter_details.setVisibility(View.GONE);
        final RangeSeekBar<Integer> rangeSeekBar = new RangeSeekBar<Integer>(this);
        // Set the range
        /*rangeSeekBar.setRangeValues(12000, 110000);
        rangeSeekBar.setSelectedMinValue(12000);
        rangeSeekBar.setSelectedMaxValue(110000);*/
        rangeSeekBar.setNotifyWhileDragging(true);
        // Add to layout
        LinearLayout layout = (LinearLayout) findViewById(R.id.seekbar_placeholder);
        no_data_lay = (LinearLayout) findViewById(R.id.no_data_layout);

        layout.addView(rangeSeekBar);

        rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, final Integer minValue, final Integer maxValue) {
                Log.i("SeekBar", "Min :" + minValue + " Max :" + maxValue);
                runOnUiThread(new Runnable() {
                    public void run() {
                        // Update UI elements
                        minval.setText(""+minValue);
                        maxval.setText(""+maxValue);
                    }
                });

            }
        });

        //Bundle to read value from another activity
        bundle = getIntent().getExtras();
        //Print
        System.out.println("RegionID: " + bundle.getInt("RegionID"));
        url= url + bundle.getInt("RegionID");
        getSupportActionBar().setTitle(bundle.getString("RegionName") + " Packages");

      // mToolbar = (Toolbar) findViewById(R.id.toolbar);
    //   setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        listView = (ListView) findViewById(R.id.listView);
        adapter = new RegionPlaceAdapter(this, regionList, new CustomNoDataHandler());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


            }
        });

        final ProgressDialog pDialog = new ProgressDialog(RegionPlace.this);
        pDialog.setMessage("Loading...");
        pDialog.show();
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
                        Log.i("KeyValue",""+key);
                       if ( resobj.get(key) instanceof JSONObject ) {
                           RegionPlaceModel region_adp = new RegionPlaceModel();

                           JSONObject jsonobj = new JSONObject(resobj.get(key).toString());
                           // Log.d("res1",""+xx.getJSONObject("Master"));
                           Log.d("KeyDestinationDiscount", "" + jsonobj.getInt("Discount"));

                           region_adp.setTitle(jsonobj.getJSONObject("Master").getString("Title"));
                           region_adp.setLink(jsonobj.getJSONObject("Master").getString("Link"));
                           //region_adp.setImage(jsonobj.getJSONObject("Master").getString("Image"));
                           region_adp.setImage("http://stage.itraveller.com/backend/images/packages/" + jsonobj.getJSONObject("Master").getInt("Itinerary_Id") + ".jpg");
                           Log.d("Images:", "http://stage.itraveller.com/backend/images/packages/" + jsonobj.getJSONObject("Master").getInt("Itinerary_Id") + ".jpg");
                           region_adp.setArrival_Port_Id(jsonobj.getJSONObject("Master").getInt("Arrival_Port_Id"));
                           region_adp.setDeparture_Port_Id(jsonobj.getJSONObject("Master").getInt("Departure_Port_Id"));
                           region_adp.setItinerary_Id(jsonobj.getJSONObject("Master").getInt("Itinerary_Id"));
                           region_adp.setDuration_Day(jsonobj.getJSONObject("Master").getInt("Duration_Day"));
                           region_adp.setPrice(jsonobj.getJSONObject("Master").getInt("Price"));
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

                                   i++;
                               }
                               else{
                                   destinationKeyValue = destinationKeyValue + "," + destinationKey;
                                   JSONObject destobj = jsonobj.getJSONObject("Destination").getJSONObject(destinationKey);
                                   destinationValue = destinationValue + "," + destobj.getString("name").toString();
                                   destinationCount = destinationCount + "," + destobj.getString("count").toString();
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

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
               // pDialog.hide();
               //  region_adapter.notifyDataSetChanged();
                // Update UI elements
                minval.setText(""+ (min_value - 1));
                maxval.setText(""+ (max_value + 1));
                rangeSeekBar.setRangeValues(min_value - 1, max_value + 1);
                rangeSeekBar.setSelectedMinValue(min_value - 1);
                rangeSeekBar.setSelectedMaxValue(max_value + 1);
                adapter.notifyDataSetChanged();
                pDialog.hide();
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

                    pDialog.hide();
                    Toast.makeText(RegionPlace.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                } else if( error instanceof ServerError) {
                } else if( error instanceof AuthFailureError) {
                } else if( error instanceof ParseError) {
                } else if( error instanceof NoConnectionError) {
                    pDialog.hide();
                    Toast.makeText(RegionPlace.this, "No Internet Connection" ,Toast.LENGTH_LONG).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
      /*  if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.action_search){
            Toast.makeText(getApplicationContext(), "Search action is selected!", Toast.LENGTH_SHORT).show();
            return true;
        }*/

        if(id == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        if(filter_btn.getText().toString().equalsIgnoreCase("Apply Filter"))
        {
            filter_details.setVisibility(View.GONE);
            filter_btn.setText("Filter");
        }
        else
        {
            finish();
        }
    }

    public interface NoDataInterface {
        public void DataHandler(int count);
    }

    public class CustomNoDataHandler implements NoDataInterface{

        public CustomNoDataHandler() {
        }

        @Override
        public void DataHandler(int count) {

            Log.v("Interface","CalledInMainFunction" +count);
            if(count == 0)
            no_data_lay.setVisibility(View.VISIBLE);
            else
                no_data_lay.setVisibility(View.INVISIBLE);
        }
    }
}