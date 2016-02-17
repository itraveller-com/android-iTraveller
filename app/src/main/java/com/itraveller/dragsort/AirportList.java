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
import com.itraveller.R;
import com.itraveller.adapter.AirportAdapter;
import com.itraveller.model.AirportModel;
import com.itraveller.volley.AppController;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class AirportList extends Activity {

    private List<AirportModel> mAirportList = new ArrayList<AirportModel>();
    private AirportAdapter mAdapter;
    private String mUrl = "";
    private SharedPreferences.Editor mEditor;
    private static final String MY_PREFS = "Destination";
    private int mClick_btn;
    private static final String TAG =AirportList.class.getName();
    private static  final String FROM_HOME_DESINATION = "From_Home_Destination";
    private static  final String TO_HOME_DESTINATION="To_Home_Destination";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.airport_listview);

        Bundle bundle = getIntent().getExtras();
        mUrl= bundle.getString("Url");
        mClick_btn = bundle.getInt("Place");

        ListView listView = (ListView) findViewById(R.id.list);
        SearchView searchAirport = (SearchView) findViewById(R.id.searchView);
        mAdapter = new AirportAdapter(this,mAirportList);
        listView.setAdapter(mAdapter);
        searchAirport.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mAdapter.getFilter().filter(s);
                return false;
            }
        });
        airportJSON();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mEditor = getSharedPreferences(MY_PREFS, MODE_PRIVATE).edit();
                if (mClick_btn == 1) {
                    mEditor.putString(FROM_HOME_DESINATION, mAirportList.get(position).getValue().toString());
                    mEditor.putString(TO_HOME_DESTINATION, mAirportList.get(position).getValue().toString());
                }
                mEditor.commit();
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

    private void airportJSON()
    {

        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET,
                mUrl, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.i(TAG, "Testing" + response);
                    Log.d(TAG, "" + response.getBoolean("success"));
                    Log.d(TAG, ""+response.getJSONObject("error"));
                    Log.d(TAG, ""+response.getJSONArray("payload"));


                    int response_JSON_arr_length=response.getJSONArray("payload").length();
                    for (int i = 0; i < response_JSON_arr_length; i++) {

                        JSONObject jsonarr = response.getJSONArray("payload").getJSONObject(i);
                        AirportModel airport_model = new AirportModel();

                        airport_model.setKey(jsonarr.getString("key"));
                        airport_model.setValue(jsonarr.getString("value"));

                        mAirportList.add(airport_model);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    VolleyLog.d("Volley Error", "Error: " + e.getMessage());
                }
                mAdapter.notifyDataSetChanged();



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

