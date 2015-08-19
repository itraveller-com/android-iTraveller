package com.itraveller.dragsort;

/**
 * Created by VNK on 6/11/2015.
 */

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.itraveller.R;
import com.itraveller.adapter.AirportAdapter;
import com.itraveller.model.AirportModel;
import com.itraveller.volley.AppController;


public class AirportList extends Activity {
/* When using Appcombat support library
   you need to extend Main Activity to ActionBarActivity.*/

    private List<AirportModel> airportList = new ArrayList<AirportModel>();
    private AirportAdapter adapter;
    private ListView listview;
    private SearchView search_airport;
    String url = "";
    private SharedPreferences.Editor editor;
    public static final String MY_PREFS = "Destination";
    private int click_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.airport_listview);

        Bundle bundle = getIntent().getExtras();
        //Print
        System.out.println("url: " + bundle.getString("Url"));
        url= bundle.getString("Url");
        click_btn = bundle.getInt("Place");

        listview = (ListView) findViewById(R.id.list);
        search_airport = (SearchView) findViewById(R.id.searchView);
        adapter = new AirportAdapter(this,airportList);
        listview.setAdapter(adapter);
        search_airport.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
        airportJSON();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                editor = getSharedPreferences(MY_PREFS, MODE_PRIVATE).edit();
               if(click_btn == 1)
               {
                   editor.putString("From_Home_Destination", airportList.get(position).getValue().toString());
                   editor.putString("To_Home_Destination",airportList.get(position).getValue().toString());
               }
                editor.commit();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    public void airportJSON ()
    {

        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.i("Test", "Testing" + response);
                    Log.d("Boolean", "" + response.getBoolean("success"));
                    Log.d("Error", ""+response.getJSONObject("error"));
                    Log.d("Payload", ""+response.getJSONArray("payload"));

                    // JSONObject jsonobj = response.getJSONObject("payload").get;
                    // Parsing json
                    for (int i = 0; i < response.getJSONArray("payload").length(); i++) {

                        JSONObject jsonarr = response.getJSONArray("payload").getJSONObject(i);
                        AirportModel airport_model = new AirportModel();

                        airport_model.setKey(jsonarr.getString("key"));
                        airport_model.setValue(jsonarr.getString("value"));
                       /* airport_model.setName(jsonarr.getString("Name"));
                        airport_model.setLat(jsonarr.getString("Lat"));
                        airport_model.setLong(jsonarr.getString("Long"));
                        airport_model.setTimezone(jsonarr.getString("Timezone"));
                        airport_model.setCity(jsonarr.getString("City"));
                        airport_model.setCountry(jsonarr.getString("Country"));
                        airport_model.setCountry_Code(jsonarr.getString("Country_Code"));
                        airport_model.setStatus(jsonarr.getInt("Status"));*/
                        airportList.add(airport_model);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    VolleyLog.d("Volley Error", "Error: " + e.getMessage());
                }
                //pDialog.hide();
                //region_adapter.notifyDataSetChanged();
                adapter.notifyDataSetChanged();


                //searchText.startAnimation(animFadein);
                //searchText.setFocusableInTouchMode(true);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Volley Error", "Error: " + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(strReq);
    }

}

