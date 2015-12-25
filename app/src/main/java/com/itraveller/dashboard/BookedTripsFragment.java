package com.itraveller.dashboard;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.itraveller.R;
import com.itraveller.adapter.LandingAdapter;
import com.itraveller.adapter.MyTravelAdapter;
import com.itraveller.constant.Constants;
import com.itraveller.model.LandingModel;
import com.itraveller.model.MyTravelModel;
import com.itraveller.volley.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookedTripsFragment extends Fragment {

    Date current_date;
    Date fetched_date;
    SimpleDateFormat formatter;
    ListView upcoming_trips,past_trips;
    private MyTravelAdapter upcomingAdapter;
    private MyTravelAdapter pastAdapter;
    private List<MyTravelModel> upcomingList = new ArrayList<MyTravelModel>();
    private List<MyTravelModel> pastList = new ArrayList<MyTravelModel>();
    String curr_date_time;
    String curr_date;

    public BookedTripsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.my_trips, container, false);


        ((MyTravelActivity) getActivity()).getSupportActionBar().setTitle("Your booked trips");

        upcoming_trips=(ListView) view.findViewById(R.id.upcoming_trips_list);
        past_trips=(ListView) view.findViewById(R.id.past_trips_list);

        upcomingAdapter = new MyTravelAdapter(getActivity(), upcomingList);
        upcoming_trips.setAdapter(upcomingAdapter);


        pastAdapter = new MyTravelAdapter(getActivity(), pastList);
        past_trips.setAdapter(pastAdapter);

        formatter = new SimpleDateFormat("yyyy-MM-dd");

        curr_date_time=""+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        String date_time_arr[]=curr_date_time.split(" ");
        curr_date=date_time_arr[0];
        try {
            current_date = formatter.parse(curr_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET,
                Constants.API_LandingActivity_Region, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("Response in LandingPage", "" + response.toString());
                    Log.d("Boolean", ""+response.getBoolean("success"));
                    Log.d("Error", ""+response.getJSONObject("error"));
                    Log.d("Payload_regions", ""+response.getJSONArray("payload"));

                    // JSONObject jsonobj = response.getJSONObject("payload").get;
                    // Parsing json
                    int json_response_length=response.getJSONArray("payload").length();

                    for (int i = 0; i < json_response_length; i++) {

                        //Log.i("i value", "" + i);

                        MyTravelModel my_travel_model = new MyTravelModel();

                        JSONObject jsonObj = response.getJSONArray("payload").getJSONObject(i);

                        JSONObject jsonObjItinerary=jsonObj.getJSONObject("itinerary");
                        JSONObject jsonObjRegion=jsonObj.getJSONObject("region");

                        JSONArray jsonObjHotelList=jsonObj.getJSONArray("hotellist");

                        String travel_date=jsonObjItinerary.getString("Date_Of_Travel");

                        fetched_date=formatter.parse(travel_date);

                        if (current_date.compareTo(fetched_date)<0)
                        {
                            my_travel_model.setItinerary_Id(jsonObjItinerary.getInt("Id"));
                            my_travel_model.setTravelDate(jsonObjItinerary.getString("Date_Of_Travel"));
                            my_travel_model.setNo_of_Days(jsonObjItinerary.getInt("No_Of_Days"));
                            my_travel_model.setPackageValue(jsonObjItinerary.getInt("Package_Value"));
                            my_travel_model.setBooking_Date(jsonObjItinerary.getString("Date_Of_Booking"));
                            my_travel_model.setRegion_Id(jsonObjRegion.getInt("Region_Id"));
                            my_travel_model.setRegion_Name(jsonObjRegion.getString("Region_Name"));

                        }
                        else
                        {

                        }





//                            landingList.add(landing_model);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                pDialog.hide();
//                adapter.notifyDataSetChanged();

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
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
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


        return view;
    }


}
