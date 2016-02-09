package com.itraveller.dashboard;


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
import android.widget.AdapterView;
import android.widget.Button;
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
import com.itraveller.constant.Utility;
import com.itraveller.model.LandingModel;
import com.itraveller.model.MyTravelModel;
import com.itraveller.moxtraChat.AgentLoginActivity;
import com.itraveller.moxtraChat.MoxtraActivity;
import com.itraveller.moxtraChat.MoxtraMainActivity;
import com.itraveller.volley.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookedTripsFragment extends Fragment {

    Button group_btn,single_btn,snap_btn;
    SharedPreferences prefs;
    String user_email;
    Date current_date;
    Date fetched_date;
    SimpleDateFormat formatter;
    ListView upcoming_trips,past_trips;
    private MyTravelAdapter upcomingAdapter;
    private MyTravelAdapter pastAdapter;
    public static List<MyTravelModel> upcomingList = new ArrayList<MyTravelModel>();
    public List<MyTravelModel> pastList = new ArrayList<MyTravelModel>();
    String curr_date_time;
    String curr_date_str,fetched_date_str;

    public BookedTripsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.my_trips, container, false);

        ((MyTravelActivity) getActivity()).getSupportActionBar().setTitle("Your booked trips");

        upcomingList.clear();

        prefs=getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        group_btn=(Button) view.findViewById(R.id.group_chat_btn);
        single_btn=(Button) view.findViewById(R.id.single_chat_btn);
        snap_btn=(Button) view.findViewById(R.id.snaps_btn);

        upcoming_trips=(ListView) view.findViewById(R.id.upcoming_trips_list);
        past_trips=(ListView) view.findViewById(R.id.past_trips_list);


        upcomingAdapter = new MyTravelAdapter(getActivity(), upcomingList);
        upcoming_trips.setAdapter(upcomingAdapter);

        pastAdapter = new MyTravelAdapter(getActivity(), pastList);
        past_trips.setAdapter(pastAdapter);

        group_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), MoxtraActivity.class);
                startActivity(i);

            }
        });

        single_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(getActivity(),AgentLoginActivity.class);
                startActivity(i);
            }
        });

        snap_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        upcoming_trips.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i=new Intent(getActivity(),TripSummary.class);
                i.putExtra("ItineraryData",position);
                startActivity(i);
            }
        });

        past_trips.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                final Intent i = new Intent(getActivity(), MoxtraActivity.class);
//                startActivity(i);
            }
       });


        formatter = new SimpleDateFormat("yyyy-MM-dd");

        curr_date_time=""+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        String date_time_arr[]=curr_date_time.split(" ");
        curr_date_str=date_time_arr[0];

        try {
            current_date = formatter.parse(curr_date_str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Fetching Your Trips...");
        pDialog.setCancelable(false);
        pDialog.show();

        user_email=""+prefs.getString("email","");

        String url = "http://stage.itraveller.com/backend/api/v1/customerpackages?email="+user_email+"&booked=1";

        Log.d("URL call test",""+url);

        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("Response is", "" + response);

                try
                    {
                        Log.d("Boolean", "" + response.getBoolean("success"));
                        Log.d("Error", ""+response.getJSONObject("error"));
                        Log.d("Payload_regions", ""+response.getJSONObject("payload"));
                        Iterator<?> keys = response.getJSONObject("payload").keys();
                        while(keys.hasNext() )
                        {

                            MyTravelModel travelModel=new MyTravelModel();

                            String key = (String) keys.next();
                            if (response.getJSONObject("payload").get(key) instanceof JSONObject)
                            {

                                JSONObject jsonobj = new JSONObject(response.getJSONObject("payload").get(key).toString());
                                Log.v("ID","" + jsonobj.getJSONObject("itinerary").getString("Id"));

                                fetched_date_str="" + jsonobj.getJSONObject("itinerary").get("Date_Of_Travel");

                                fetched_date = formatter.parse(fetched_date_str);

                                Log.d("Fetched Date",""+fetched_date);
                                Log.d("Current Date",""+current_date);

                                if(fetched_date.compareTo(current_date)>0)
                                {
                                    travelModel.setItinerary_Id(Integer.parseInt("" + jsonobj.getJSONObject("itinerary").get("Id")));
                                    travelModel.setItinerary_Main_Id("" + jsonobj.getJSONObject("itinerary").get("Travel_Id"));
                                    travelModel.setPackageValue(Integer.parseInt("" + jsonobj.getJSONObject("itinerary").get("Package_Value")));
                                    travelModel.setTravelDate("" + jsonobj.getJSONObject("itinerary").get("Date_Of_Travel"));
                                    travelModel.setNo_of_Days(Integer.parseInt("" + jsonobj.getJSONObject("itinerary").get("No_Of_Days")));
                                    travelModel.setBooking_Date("" + jsonobj.getJSONObject("itinerary").get("Date_Of_Booking"));
                                    travelModel.setBooking_Mode("" + jsonobj.getJSONObject("itinerary").get("Advance_Pmt_Mode"));
                                    travelModel.setBooking_Mode("" + jsonobj.getJSONObject("itinerary").get("Adults_Pax"));
                                    travelModel.setBooking_Mode("" + jsonobj.getJSONObject("itinerary").get("Children_Pax"));
                                    travelModel.setBooking_Mode(""+jsonobj.getJSONObject("itinerary").get("Infants_Pax"));


                                    travelModel.setRegion_Id(Integer.parseInt("" + jsonobj.getJSONObject("region").get("Region_Id")));
                                    travelModel.setRegion_Name("" + jsonobj.getJSONObject("region").get("Region_Name"));

                                    Log.v("RegionName","" + jsonobj.getJSONObject("region").getString("Region_Name"));
                                    for(int i = 0; i < jsonobj.getJSONArray("hotelList").length() ; i++)
                                    {
                                        JSONObject jsonObj = (JSONObject) jsonobj.getJSONArray("hotelList").get(i);

                                        travelModel.setHotel_Id(Integer.parseInt("" + jsonObj.get("Hotel_Id")));
                                        travelModel.setHotel_Name("" + jsonObj.get("Hotel_Name"));
                                        travelModel.setDestination_Id("" + jsonObj.get("Destination_Id"));
                                        travelModel.setHotel_Star_Rating("" + jsonObj.get("Hotel_Star_Rating"));

//                                    travelModel.setCheckInDate("" + jsonobj.getJSONObject("hotelbooking").get("Check_In"));
//                                    travelModel.setCheckOutDate("" + jsonobj.getJSONObject("hotelbooking").get("Check_Out"));
//                                    travelModel.setNo_of_Room_Days(Integer.parseInt("" + jsonobj.getJSONObject("hotelbooking").get("Num_Of_Roooms")));

                                        Log.v("HotelID", "" + jsonObj.get("Hotel_Id"));
                                    }
                                    for(int i = 0; i < jsonobj.getJSONArray("transportation").length() ; i++)
                                    {
                                        JSONObject jsonObj = (JSONObject) jsonobj.getJSONArray("transportation").get(i);

                                        travelModel.setTransportation_Travel_Id(Integer.parseInt("" + jsonObj.get("Travel_Id")));
                                        travelModel.setTransportation_Start_Date("" + jsonObj.get("Start_Date"));
                                        travelModel.setTransportation_End_Date("" + jsonObj.get("End_Date"));
                                        travelModel.setTransportation_No_of_Days(Integer.parseInt("" + jsonObj.get("Num_Of_Days")));

                                        Log.v("TransportationID", "" + jsonObj.get("connect_Transportation_Booking_Id"));
                                    }
                                    for(int i = 0; i < jsonobj.getJSONArray("inclusion").length() ; i++)
                                    {
                                        JSONObject jsonObj = (JSONObject) jsonobj.getJSONArray("inclusion").get(i);
                                        Log.v("InculsionID", "" + jsonObj.get("connect_Inculsion_Booking_Id"));
                                    }
                                    for(int i = 0; i < jsonobj.getJSONArray("customer").length() ; i++)
                                    {
                                        JSONObject jsonObj = (JSONObject) jsonobj.getJSONArray("customer").get(i);

                                        travelModel.setCustomer_Id(Integer.parseInt("" + jsonObj.get("Customer_Id")));
                                        travelModel.setCustomer_Name("" + jsonObj.get("Customer_Name"));
                                        travelModel.setCustomer_Email("" + jsonObj.get("Email_Id"));

                                        Log.v("CustomerID", "" + jsonObj.get("Customer_Id"));
                                    }
                                    upcomingList.add(travelModel);
                                } else {
                                    travelModel.setItinerary_Id(Integer.parseInt("" + jsonobj.getJSONObject("itinerary").get("Id")));
                                    travelModel.setItinerary_Main_Id("" + jsonobj.getJSONObject("itinerary").get("Travel_Id"));
                                    travelModel.setPackageValue(Integer.parseInt("" + jsonobj.getJSONObject("itinerary").get("Package_Value")));
                                    travelModel.setTravelDate("" + jsonobj.getJSONObject("itinerary").get("Date_Of_Travel"));
                                    travelModel.setNo_of_Days(Integer.parseInt("" + jsonobj.getJSONObject("itinerary").get("No_Of_Days")));
                                    travelModel.setBooking_Date("" + jsonobj.getJSONObject("itinerary").get("Date_Of_Booking"));
                                    travelModel.setBooking_Mode("" + jsonobj.getJSONObject("itinerary").get("Advance_Pmt_Mode"));
                                    travelModel.setBooking_Mode(""+jsonobj.getJSONObject("itinerary").get("Adults_Pax"));
                                    travelModel.setBooking_Mode(""+jsonobj.getJSONObject("itinerary").get("Children_Pax"));
                                    travelModel.setBooking_Mode(""+jsonobj.getJSONObject("itinerary").get("Infants_Pax"));

                                    travelModel.setRegion_Id(Integer.parseInt("" + jsonobj.getJSONObject("region").get("Region_Id")));
                                    travelModel.setRegion_Name("" + jsonobj.getJSONObject("region").get("Region_Name"));

                                    Log.v("RegionName","" + jsonobj.getJSONObject("region").getString("Region_Name"));
                                    for(int i = 0; i < jsonobj.getJSONArray("hotelList").length() ; i++)
                                    {
                                        JSONObject jsonObj = (JSONObject) jsonobj.getJSONArray("hotelList").get(i);

                                        travelModel.setHotel_Id(Integer.parseInt("" + jsonObj.get("Hotel_Id")));
                                        travelModel.setHotel_Name("" + jsonObj.get("Hotel_Name"));
                                        travelModel.setDestination_Id("" + jsonObj.get("Destination_Id"));
                                        travelModel.setHotel_Star_Rating("" + jsonObj.get("Hotel_Star_Rating"));

//                                    travelModel.setCheckInDate("" + jsonobj.getJSONObject("hotelbooking").get("Check_In"));
//                                    travelModel.setCheckOutDate("" + jsonobj.getJSONObject("hotelbooking").get("Check_Out"));
//                                    travelModel.setNo_of_Room_Days(Integer.parseInt("" + jsonobj.getJSONObject("hotelbooking").get("Num_Of_Roooms")));

                                        Log.v("HotelID", "" + jsonObj.get("Hotel_Id"));
                                    }
                                    for(int i = 0; i < jsonobj.getJSONArray("transportation").length() ; i++)
                                    {
                                        JSONObject jsonObj = (JSONObject) jsonobj.getJSONArray("transportation").get(i);

                                        travelModel.setTransportation_Travel_Id(Integer.parseInt("" + jsonObj.get("Travel_Id")));
                                        travelModel.setTransportation_Start_Date("" + jsonObj.get("Start_Date"));
                                        travelModel.setTransportation_End_Date("" + jsonObj.get("End_Date"));
                                        travelModel.setTransportation_No_of_Days(Integer.parseInt("" + jsonObj.get("Num_Of_Days")));

                                        Log.v("TransportationID", "" + jsonObj.get("connect_Transportation_Booking_Id"));
                                    }
                                    for(int i = 0; i < jsonobj.getJSONArray("inclusion").length() ; i++)
                                    {
                                        JSONObject jsonObj = (JSONObject) jsonobj.getJSONArray("inclusion").get(i);
                                        Log.v("InculsionID", "" + jsonObj.get("connect_Inculsion_Booking_Id"));
                                    }
                                    for(int i = 0; i < jsonobj.getJSONArray("customer").length() ; i++)
                                    {
                                        JSONObject jsonObj = (JSONObject) jsonobj.getJSONArray("customer").get(i);

                                        travelModel.setCustomer_Id(Integer.parseInt("" + jsonObj.get("Customer_Id")));
                                        travelModel.setCustomer_Name("" + jsonObj.get("Customer_Name"));
                                        travelModel.setCustomer_Email("" + jsonObj.get("Email_Id"));

                                        Log.v("CustomerID", "" + jsonObj.get("Customer_Id"));
                                    }
                                    pastList.add(travelModel);
                                }

                            }
                        }
                        if(upcomingAdapter.getCount()==0 && past_trips.getCount()==0)
                        {
                            Toast.makeText(getActivity(),"No Trips Available",Toast.LENGTH_LONG).show();
                        }


                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    catch (ParseException e)
                    {
                        e.printStackTrace();
                    }

                pDialog.hide();

                upcomingAdapter.notifyDataSetChanged();
                pastAdapter.notifyDataSetChanged();
                Utility.setListViewHeightBasedOnChildren(upcoming_trips);
                Utility.setListViewHeightBasedOnChildren(past_trips);

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

            strReq.setRetryPolicy(new DefaultRetryPolicy(10000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq);

            return view;
    }
}
