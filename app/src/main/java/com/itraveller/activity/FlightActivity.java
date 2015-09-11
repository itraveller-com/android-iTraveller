package com.itraveller.activity;

/**
 * Created by VNK on 6/11/2015.
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


import com.itraveller.R;
import com.itraveller.adapter.FlightAdapter;
import com.itraveller.constant.Constants;
import com.itraveller.constant.CustomLoading;
import com.itraveller.model.FlightModel;
import com.itraveller.model.OnwardFlightModel;
import com.itraveller.model.ReturnFlightModel;
import com.itraveller.volley.AppController;


public class FlightActivity extends ActionBarActivity {

    int Total_Price;

    public static Activity fa;

    private Toolbar mtoolbar; // Declaring the Toolbar Object
    private ArrayList<FlightModel> flight_model = new ArrayList<FlightModel>();

    FlightAdapter adapter;
    ListView listview;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.flights_list);

            fa=this;

            mtoolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mtoolbar);
            getSupportActionBar().setTitle("Flight");

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            CustomLoading.LoadingScreen(FlightActivity.this, false);
            listview = (ListView) findViewById(R.id.flights_listview);
            adapter = new FlightAdapter(this, flight_model);
            listview.setAdapter(adapter);

            SharedPreferences prefs = getSharedPreferences("Itinerary", MODE_PRIVATE);
            //String url = "http://stage.itraveller.com/backend/api/v1/internationalflight?travelFrom=BOM&arrivalPort=MRU&departDate=2015-07-26&returnDate=2015-08-01&adults=2&children=0&infants=0&departurePort=MRU&travelTo=BOM";
        //    String url ="http://stage.itraveller.com/backend/api/v1/internationalflight?" +
            String url= Constants.API_International_Flights +
                    "travelFrom=" + prefs.getString("ArrivalAirport", null) +
                    "&arrivalPort=" + prefs.getString("TravelFrom", null) +
                    "&departDate=" + prefs.getString("TravelDate", null) +
                    "&returnDate=" + prefs.getString("EndDate", null) +
                    "&adults=" + prefs.getString("Adults", "0") +
                    "&children=" + prefs.getString("Children_12_5", "0") +
                    "&infants=" + prefs.getString("Children_5_2", "0") +
                    "&departurePort=" + prefs.getString("TravelTo", null) +
                    "&travelTo=" + prefs.getString("DepartureAirport", null);

            Log.i("Flight URL","" + Constants.API_International_Flights);

            Log.i("Transportation_Cost","" + prefs.getString("TransportationCost", null));

            JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET,
                    url, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //Log.d("Boolean", "" + response.getBoolean("success"));
                        //Log.d("Error", ""+response.getJSONObject("error"));
                        //Log.d("Payload", ""+response.getJSONObject("payload"));
                        JSONObject jsonobj = response.getJSONObject("payload").getJSONObject("AvailResponse").getJSONObject("OriginDestinationOptions");
                        JSONArray jsonarr = jsonobj.getJSONArray("OriginDestinationOption");
                        int jsonarr_length=jsonarr.length();
                        for (int i = 0; i < jsonarr_length; i++) {
                            ArrayList<OnwardFlightModel> onward_model = new ArrayList<OnwardFlightModel>();
                            ArrayList<ReturnFlightModel> return_model = new ArrayList<ReturnFlightModel>();

                            FlightModel mflight = new FlightModel();
                            JSONObject flight_fare = jsonarr.getJSONObject(i).getJSONObject("FareDetails");
                            mflight.setActualBaseFare(flight_fare.getString("ActualBaseFare").toString());
                            mflight.setTax(flight_fare.getString("Tax").toString());
                            mflight.setSTax(flight_fare.getString("STax").toString());
                            mflight.setTCharge(flight_fare.getString("TCharge").toString());
                            mflight.setSCharge(flight_fare.getString("SCharge").toString());
                            mflight.setTDiscount(flight_fare.getString("TDiscount").toString());
                            mflight.setTMarkup(flight_fare.getString("TMarkup").toString());
                            mflight.setTPartnerCommission(flight_fare.getString("TPartnerCommission").toString());
                            mflight.setTSdiscount(flight_fare.getString("TSdiscount").toString());
                            mflight.setOcTax(flight_fare.getString("ocTax").toString());



                            Total_Price=Integer.parseInt(flight_fare.getString("ActualBaseFare").toString())+Integer.parseInt(flight_fare.getString("Tax").toString())
                            +Integer.parseInt(flight_fare.getString("Tax").toString())+Integer.parseInt(flight_fare.getString("STax").toString())+
                            Integer.parseInt(flight_fare.getString("TCharge").toString())+Integer.parseInt(flight_fare.getString("SCharge").toString())+
                            Integer.parseInt(flight_fare.getString("TDiscount").toString())+Integer.parseInt(flight_fare.getString("TMarkup").toString())+
                            Integer.parseInt(flight_fare.getString("TPartnerCommission").toString())+Integer.parseInt(flight_fare.getString("TSdiscount").toString())+
                            Integer.parseInt(flight_fare.getString("ocTax").toString());

                            Log.d("Total price test",""+Total_Price);

                            mflight.setTotal_Price(Total_Price);

                            JSONObject flight_onward = jsonarr.getJSONObject(i).getJSONObject("onward").getJSONObject("FlightSegments");
                            try{
                                JSONArray onward_arr = flight_onward.getJSONArray("FlightSegment");
                                int onward_arr_length=onward_arr.length();
                                for (int j = 0; j < onward_arr_length; j++) {
                                    OnwardFlightModel monward = new OnwardFlightModel();
                                    monward.setAirEquipType(onward_arr.getJSONObject(j).getString("AirEquipType").toString());
                                    monward.setArrivalAirportCode(onward_arr.getJSONObject(j).getString("ArrivalAirportCode").toString());
                                    monward.setArrivalAirportName(onward_arr.getJSONObject(j).getString("ArrivalAirportName").toString());
                                    monward.setArrivalDateTime(onward_arr.getJSONObject(j).getString("ArrivalDateTime").toString());
                                    monward.setDepartureAirportCode(onward_arr.getJSONObject(j).getString("DepartureAirportCode").toString());
                                    monward.setDepartureAirportName(onward_arr.getJSONObject(j).getString("DepartureAirportName").toString());
                                    monward.setDepartureDateTime(onward_arr.getJSONObject(j).getString("DepartureDateTime").toString());
                                    monward.setFlightNumber(onward_arr.getJSONObject(j).getString("FlightNumber").toString());
                                    monward.setMarketingAirlineCode(onward_arr.getJSONObject(j).getString("MarketingAirlineCode").toString());
                                    monward.setOperatingAirlineCode(onward_arr.getJSONObject(j).getString("OperatingAirlineCode").toString());
                                    monward.setOperatingAirlineName(onward_arr.getJSONObject(j).getString("OperatingAirlineName").toString());
                                    monward.setOperatingAirlineFlightNumber(onward_arr.getJSONObject(j).getString("OperatingAirlineFlightNumber").toString());
                                    monward.setNumStops(onward_arr.getJSONObject(j).getString("NumStops").toString());
                                    monward.setLinkSellAgrmnt(onward_arr.getJSONObject(j).getString("LinkSellAgrmnt").toString());
                                    monward.setConx(onward_arr.getJSONObject(j).getString("Conx").toString());
                                    monward.setAirpChg(onward_arr.getJSONObject(j).getString("AirpChg").toString());
                                    monward.setInsideAvailOption(onward_arr.getJSONObject(j).getString("InsideAvailOption").toString());
                                    monward.setGenTrafRestriction(onward_arr.getJSONObject(j).getString("GenTrafRestriction").toString());
                                    monward.setDaysOperates(onward_arr.getJSONObject(j).getString("DaysOperates").toString());
                                    monward.setJrnyTm(onward_arr.getJSONObject(j).getString("JrnyTm").toString());
                                    monward.setEndDt(onward_arr.getJSONObject(j).getString("EndDt").toString());
                                    monward.setStartTerminal(onward_arr.getJSONObject(j).getString("StartTerminal").toString());
                                    monward.setEndTerminal(onward_arr.getJSONObject(j).getString("EndTerminal").toString());
                                    onward_model.add(monward);
                                }

                                JSONObject flight_return = jsonarr.getJSONObject(i).getJSONObject("Return").getJSONObject("FlightSegments");
                                JSONArray return_arr = flight_return.getJSONArray("FlightSegment");
                                int return_arr_length=return_arr.length();
                                for (int j = 0; j < return_arr_length; j++) {
                                    ReturnFlightModel mreturn = new ReturnFlightModel();
                                    mreturn.setAirEquipType(return_arr.getJSONObject(j).getString("AirEquipType").toString());
                                    mreturn.setArrivalAirportCode(return_arr.getJSONObject(j).getString("ArrivalAirportCode").toString());
                                    mreturn.setArrivalAirportName(return_arr.getJSONObject(j).getString("ArrivalAirportName").toString());
                                    mreturn.setArrivalDateTime(return_arr.getJSONObject(j).getString("ArrivalDateTime").toString());
                                    mreturn.setDepartureAirportCode(return_arr.getJSONObject(j).getString("DepartureAirportCode").toString());
                                    mreturn.setDepartureAirportName(return_arr.getJSONObject(j).getString("DepartureAirportName").toString());
                                    mreturn.setDepartureDateTime(return_arr.getJSONObject(j).getString("DepartureDateTime").toString());
                                    mreturn.setFlightNumber(return_arr.getJSONObject(j).getString("FlightNumber").toString());
                                    mreturn.setMarketingAirlineCode(return_arr.getJSONObject(j).getString("MarketingAirlineCode").toString());
                                    mreturn.setOperatingAirlineCode(return_arr.getJSONObject(j).getString("OperatingAirlineCode").toString());
                                    mreturn.setOperatingAirlineName(return_arr.getJSONObject(j).getString("OperatingAirlineName").toString());
                                    mreturn.setOperatingAirlineFlightNumber(return_arr.getJSONObject(j).getString("OperatingAirlineFlightNumber").toString());
                                    mreturn.setNumStops(return_arr.getJSONObject(j).getString("NumStops").toString());
                                    mreturn.setLinkSellAgrmnt(return_arr.getJSONObject(j).getString("LinkSellAgrmnt").toString());
                                    mreturn.setConx(return_arr.getJSONObject(j).getString("Conx").toString());
                                    mreturn.setAirpChg(return_arr.getJSONObject(j).getString("AirpChg").toString());
                                    mreturn.setInsideAvailOption(return_arr.getJSONObject(j).getString("InsideAvailOption").toString());
                                    mreturn.setGenTrafRestriction(return_arr.getJSONObject(j).getString("GenTrafRestriction").toString());
                                    mreturn.setDaysOperates(return_arr.getJSONObject(j).getString("DaysOperates").toString());
                                    mreturn.setJrnyTm(return_arr.getJSONObject(j).getString("JrnyTm").toString());
                                    mreturn.setEndDt(return_arr.getJSONObject(j).getString("EndDt").toString());
                                    mreturn.setStartTerminal(return_arr.getJSONObject(j).getString("StartTerminal").toString());
                                    mreturn.setEndTerminal(return_arr.getJSONObject(j).getString("EndTerminal").toString());
                                    return_model.add(mreturn);
                                }
                            } catch(JSONException jsonex) {
                                JSONObject onward_arr = flight_onward.getJSONObject("FlightSegment");
                                OnwardFlightModel monward = new OnwardFlightModel();
                                monward.setAirEquipType(onward_arr.getString("AirEquipType").toString());
                                monward.setArrivalAirportCode(onward_arr.getString("ArrivalAirportCode").toString());
                                monward.setArrivalAirportName(onward_arr.getString("ArrivalAirportName").toString());
                                monward.setArrivalDateTime(onward_arr.getString("ArrivalDateTime").toString());
                                monward.setDepartureAirportCode(onward_arr.getString("DepartureAirportCode").toString());
                                monward.setDepartureAirportName(onward_arr.getString("DepartureAirportName").toString());
                                monward.setDepartureDateTime(onward_arr.getString("DepartureDateTime").toString());
                                monward.setFlightNumber(onward_arr.getString("FlightNumber").toString());
                                monward.setMarketingAirlineCode(onward_arr.getString("MarketingAirlineCode").toString());
                                monward.setOperatingAirlineCode(onward_arr.getString("OperatingAirlineCode").toString());
                                monward.setOperatingAirlineName(onward_arr.getString("OperatingAirlineName").toString());
                                monward.setOperatingAirlineFlightNumber(onward_arr.getString("OperatingAirlineFlightNumber").toString());
                                monward.setNumStops(onward_arr.getString("NumStops").toString());
                                monward.setLinkSellAgrmnt(onward_arr.getString("LinkSellAgrmnt").toString());
                                monward.setConx(onward_arr.getString("Conx").toString());
                                monward.setAirpChg(onward_arr.getString("AirpChg").toString());
                                monward.setInsideAvailOption(onward_arr.getString("InsideAvailOption").toString());
                                monward.setGenTrafRestriction(onward_arr.getString("GenTrafRestriction").toString());
                                monward.setDaysOperates(onward_arr.getString("DaysOperates").toString());
                                monward.setJrnyTm(onward_arr.getString("JrnyTm").toString());
                                monward.setEndDt(onward_arr.getString("EndDt").toString());
                                monward.setStartTerminal(onward_arr.getString("StartTerminal").toString());
                                monward.setEndTerminal(onward_arr.getString("EndTerminal").toString());
                                onward_model.add(monward);

                                JSONObject flight_return = jsonarr.getJSONObject(i).getJSONObject("Return").getJSONObject("FlightSegments");
                                JSONObject return_arr = flight_return.getJSONObject("FlightSegment");
                                    ReturnFlightModel mreturn = new ReturnFlightModel();
                                    mreturn.setAirEquipType(return_arr.getString("AirEquipType").toString());
                                    mreturn.setArrivalAirportCode(return_arr.getString("ArrivalAirportCode").toString());
                                    mreturn.setArrivalAirportName(return_arr.getString("ArrivalAirportName").toString());
                                    mreturn.setArrivalDateTime(return_arr.getString("ArrivalDateTime").toString());
                                    mreturn.setDepartureAirportCode(return_arr.getString("DepartureAirportCode").toString());
                                    mreturn.setDepartureAirportName(return_arr.getString("DepartureAirportName").toString());
                                    mreturn.setDepartureDateTime(return_arr.getString("DepartureDateTime").toString());
                                    mreturn.setFlightNumber(return_arr.getString("FlightNumber").toString());
                                    mreturn.setMarketingAirlineCode(return_arr.getString("MarketingAirlineCode").toString());
                                    mreturn.setOperatingAirlineCode(return_arr.getString("OperatingAirlineCode").toString());
                                    mreturn.setOperatingAirlineName(return_arr.getString("OperatingAirlineName").toString());
                                    mreturn.setOperatingAirlineFlightNumber(return_arr.getString("OperatingAirlineFlightNumber").toString());
                                    mreturn.setNumStops(return_arr.getString("NumStops").toString());
                                    mreturn.setLinkSellAgrmnt(return_arr.getString("LinkSellAgrmnt").toString());
                                    mreturn.setConx(return_arr.getString("Conx").toString());
                                    mreturn.setAirpChg(return_arr.getString("AirpChg").toString());
                                    mreturn.setInsideAvailOption(return_arr.getString("InsideAvailOption").toString());
                                    mreturn.setGenTrafRestriction(return_arr.getString("GenTrafRestriction").toString());
                                    mreturn.setDaysOperates(return_arr.getString("DaysOperates").toString());
                                    mreturn.setJrnyTm(return_arr.getString("JrnyTm").toString());
                                    mreturn.setEndDt(return_arr.getString("EndDt").toString());
                                    mreturn.setStartTerminal(return_arr.getString("StartTerminal").toString());
                                    mreturn.setEndTerminal(return_arr.getString("EndTerminal").toString());
                                    return_model.add(mreturn);
                            }
                            Log.i("Onward Value", "" + onward_model.size());


                            mflight.setOnward_model(onward_model);
                            mflight.setReturn_model(return_model);



                            String flight_id = jsonarr.getJSONObject(i).getString("id").toString();
                            String flight_key = jsonarr.getJSONObject(i).getString("key").toString();

                            mflight.setId(flight_id);
                            mflight.setKey(flight_key);



                            flight_model.add(mflight);

                        }
                    } catch (JSONException e) {
                        Log.d("Error Catched","" +e.getMessage());
                    }

                    adapter.notifyDataSetChanged();
                    CustomLoading.LoadingHide();
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    System.err.println(error);
                    // TODO Auto-generated method stub
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


        public FlightModel getFlightModel( FlightModel model)
        {

            Collections.sort((List<FlightModel>) model, new Comparator<FlightModel>() {
                @TargetApi(Build.VERSION_CODES.KITKAT)
                @Override public int compare(FlightModel x, FlightModel y) {
                    return Integer.compare(x.getTotal_Price(), y.getTotal_Price());
                }
            });
            return model;
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            return super.onOptionsItemSelected(item);
        }
    }

