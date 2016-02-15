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
import com.itraveller.adapter.MyTravelAdapter;
import com.itraveller.constant.Utility;
import com.itraveller.model.MyTravelModel;
import com.itraveller.moxtraChat.AgentLoginActivity;
import com.itraveller.moxtraChat.MoxtraActivity;
import com.itraveller.volley.AppController;

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

    //TODO: Never declare multiple variable on a single line. Use explicit scoping for the variable. we can declare button variables locally.
    Button mGroupBtn, mSingleBtn;

    //TODO: Use explicit scoping for the variables.
    SharedPreferences mPrefs;
    String mUserEmail;
    Date mCurrentDate;
    Date mFetchedDate;
    SimpleDateFormat mFormatter;
    ListView mUpcomingTrips, mPastTrips;
    private MyTravelAdapter mUpComingAdapter;
    private MyTravelAdapter mPastAdapter;
    public static List<MyTravelModel> sUpcomingList = new ArrayList<MyTravelModel>();
    public List<MyTravelModel> mPastList = new ArrayList<MyTravelModel>();
    String mCurrentDateTime;
    String mCurrentDateStr, mFetchedDateStr;

    //TODO: Do we need empty constructor?
    public BookedTripsFragment() {
        // Required empty public constructor
    }

    //TODO: must have fragmented methods.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.my_trips, container, false);

        //TODO: String constants must be declared inside strings.xml
        ((MyTravelActivity) getActivity()).getSupportActionBar().setTitle("Your booked trips");

        sUpcomingList.clear();

        //TODO: Shared prefrence should have a separate wrapper class. Common key should go into constant file.
        mPrefs = getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        mGroupBtn = (Button) mView.findViewById(R.id.group_chat_btn);
        mSingleBtn = (Button) mView.findViewById(R.id.single_chat_btn);

        mUpcomingTrips = (ListView) mView.findViewById(R.id.upcoming_trips_list);
        mPastTrips = (ListView) mView.findViewById(R.id.past_trips_list);


        mUpComingAdapter = new MyTravelAdapter(getActivity(), sUpcomingList);
        mUpcomingTrips.setAdapter(mUpComingAdapter);

        mPastAdapter = new MyTravelAdapter(getActivity(), mPastList);
        mPastTrips.setAdapter(mPastAdapter);


    /*    if(upcomingAdapter.getCount()>0)
        {
            group_btn.setVisibility(View.VISIBLE);
            single_btn.setVisibility(View.VISIBLE);


        }
    */

        mGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(getActivity(), MoxtraActivity.class);
                startActivity(mIntent);
            }


        });

        mSingleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(getActivity(), AgentLoginActivity.class);
                startActivity(mIntent);
            }
        });


        //TODO: never use hardcoded strings as a key. Declare a separate constants.
        mUpcomingTrips.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent mIntent = new Intent(getActivity(), TripSummary.class);
                mIntent.putExtra("ItineraryData", position);
                startActivity(mIntent);
            }
        });

        //TODO: dead code??
        mPastTrips.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                final Intent i = new Intent(getActivity(), MoxtraActivity.class);
//                startActivity(i);
            }
        });


        mFormatter = new SimpleDateFormat("yyyy-MM-dd");

        mCurrentDateTime = "" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        String mDateTimeArr[] = mCurrentDateTime.split(" ");
        mCurrentDateStr = mDateTimeArr[0];

        try {
            mCurrentDate = mFormatter.parse(mCurrentDateStr);
        } catch (ParseException e) {
            //TODO: handle the exception.
            e.printStackTrace();
        }

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Fetching Your Trips...");
        pDialog.setCancelable(false);
        pDialog.show();

        mUserEmail = "" + mPrefs.getString("email", "");

        //TODO: must have separate method to communicate with server.
        String mURL = "http://stage.itraveller.com/backend/api/v1/customerpackages?email=" + mUserEmail + "&booked=1";

        JsonObjectRequest mJSONReq = new JsonObjectRequest(Request.Method.GET, mURL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("Response is", "" + response);

                //TODO: Parsing logic must be separate.
                try {
                    Log.d("Boolean", "" + response.getBoolean("success"));
                    Log.d("Error", "" + response.getJSONObject("error"));
                    Log.d("Payload_regions", "" + response.getJSONObject("payload"));
                    Iterator<?> keys = response.getJSONObject("payload").keys();
                    while (keys.hasNext()) {

                        MyTravelModel mMyTravelModel = new MyTravelModel();

                        String key = (String) keys.next();
                        if (response.getJSONObject("payload").get(key) instanceof JSONObject) {

                            JSONObject jsonobj = new JSONObject(response.getJSONObject("payload").get(key).toString());
                            //TODO: There should be a TAG declared for class while logging.
                            Log.v("ID", "" + jsonobj.getJSONObject("itinerary").getString("Id"));

                            mFetchedDateStr = "" + jsonobj.getJSONObject("itinerary").get("Date_Of_Travel");

                            mFetchedDate = mFormatter.parse(mFetchedDateStr);
                            //TODO: Declare a separate constants for keys.
                            if (mFetchedDate.compareTo(mCurrentDate) > 0) {
                                mMyTravelModel.setItinerary_Id(Integer.parseInt("" + jsonobj.getJSONObject("itinerary").get("Id")));
                                mMyTravelModel.setItinerary_Main_Id("" + jsonobj.getJSONObject("itinerary").get("Travel_Id"));
                                mMyTravelModel.setPackageValue(Integer.parseInt("" + jsonobj.getJSONObject("itinerary").get("Package_Value")));
                                mMyTravelModel.setTravelDate("" + jsonobj.getJSONObject("itinerary").get("Date_Of_Travel"));
                                mMyTravelModel.setNo_of_Days(Integer.parseInt("" + jsonobj.getJSONObject("itinerary").get("No_Of_Days")));
                                mMyTravelModel.setBooking_Date("" + jsonobj.getJSONObject("itinerary").get("Date_Of_Booking"));
                                mMyTravelModel.setBooking_Mode("" + jsonobj.getJSONObject("itinerary").get("Advance_Pmt_Mode"));
                                mMyTravelModel.setBooking_Mode("" + jsonobj.getJSONObject("itinerary").get("Adults_Pax"));
                                mMyTravelModel.setBooking_Mode("" + jsonobj.getJSONObject("itinerary").get("Children_Pax"));
                                mMyTravelModel.setBooking_Mode("" + jsonobj.getJSONObject("itinerary").get("Infants_Pax"));


                                mMyTravelModel.setRegion_Id(Integer.parseInt("" + jsonobj.getJSONObject("region").get("Region_Id")));
                                mMyTravelModel.setRegion_Name("" + jsonobj.getJSONObject("region").get("Region_Name"));

                                Log.v("RegionName", "" + jsonobj.getJSONObject("region").getString("Region_Name"));
                                for (int i = 0; i < jsonobj.getJSONArray("hotelList").length(); i++) {
                                    JSONObject jsonObj = (JSONObject) jsonobj.getJSONArray("hotelList").get(i);

                                    mMyTravelModel.setHotel_Id(Integer.parseInt("" + jsonObj.get("Hotel_Id")));
                                    mMyTravelModel.setHotel_Name("" + jsonObj.get("Hotel_Name"));
                                    mMyTravelModel.setDestination_Id("" + jsonObj.get("Destination_Id"));
                                    mMyTravelModel.setHotel_Star_Rating("" + jsonObj.get("Hotel_Star_Rating"));

//                                    travelModel.setCheckInDate("" + jsonobj.getJSONObject("hotelbooking").get("Check_In"));
//                                    travelModel.setCheckOutDate("" + jsonobj.getJSONObject("hotelbooking").get("Check_Out"));
//                                    travelModel.setNo_of_Room_Days(Integer.parseInt("" + jsonobj.getJSONObject("hotelbooking").get("Num_Of_Roooms")));

                                    Log.v("HotelID", "" + jsonObj.get("Hotel_Id"));
                                }
                                for (int i = 0; i < jsonobj.getJSONArray("transportation").length(); i++) {
                                    JSONObject jsonObj = (JSONObject) jsonobj.getJSONArray("transportation").get(i);

                                    mMyTravelModel.setTransportation_Travel_Id(Integer.parseInt("" + jsonObj.get("Travel_Id")));
                                    mMyTravelModel.setTransportation_Start_Date("" + jsonObj.get("Start_Date"));
                                    mMyTravelModel.setTransportation_End_Date("" + jsonObj.get("End_Date"));
                                    mMyTravelModel.setTransportation_No_of_Days(Integer.parseInt("" + jsonObj.get("Num_Of_Days")));

                                    Log.v("TransportationID", "" + jsonObj.get("connect_Transportation_Booking_Id"));
                                }
                                for (int i = 0; i < jsonobj.getJSONArray("inclusion").length(); i++) {
                                    JSONObject jsonObj = (JSONObject) jsonobj.getJSONArray("inclusion").get(i);
                                    Log.v("InculsionID", "" + jsonObj.get("connect_Inculsion_Booking_Id"));
                                }
                                for (int i = 0; i < jsonobj.getJSONArray("customer").length(); i++) {
                                    JSONObject jsonObj = (JSONObject) jsonobj.getJSONArray("customer").get(i);

                                    mMyTravelModel.setCustomer_Id(Integer.parseInt("" + jsonObj.get("Customer_Id")));
                                    mMyTravelModel.setCustomer_Name("" + jsonObj.get("Customer_Name"));
                                    mMyTravelModel.setCustomer_Email("" + jsonObj.get("Email_Id"));

                                    Log.v("CustomerID", "" + jsonObj.get("Customer_Id"));
                                }
                                sUpcomingList.add(mMyTravelModel);
                            } else {
                                mMyTravelModel.setItinerary_Id(Integer.parseInt("" + jsonobj.getJSONObject("itinerary").get("Id")));
                                mMyTravelModel.setItinerary_Main_Id("" + jsonobj.getJSONObject("itinerary").get("Travel_Id"));
                                mMyTravelModel.setPackageValue(Integer.parseInt("" + jsonobj.getJSONObject("itinerary").get("Package_Value")));
                                mMyTravelModel.setTravelDate("" + jsonobj.getJSONObject("itinerary").get("Date_Of_Travel"));
                                mMyTravelModel.setNo_of_Days(Integer.parseInt("" + jsonobj.getJSONObject("itinerary").get("No_Of_Days")));
                                mMyTravelModel.setBooking_Date("" + jsonobj.getJSONObject("itinerary").get("Date_Of_Booking"));
                                mMyTravelModel.setBooking_Mode("" + jsonobj.getJSONObject("itinerary").get("Advance_Pmt_Mode"));
                                mMyTravelModel.setBooking_Mode("" + jsonobj.getJSONObject("itinerary").get("Adults_Pax"));
                                mMyTravelModel.setBooking_Mode("" + jsonobj.getJSONObject("itinerary").get("Children_Pax"));
                                mMyTravelModel.setBooking_Mode("" + jsonobj.getJSONObject("itinerary").get("Infants_Pax"));

                                mMyTravelModel.setRegion_Id(Integer.parseInt("" + jsonobj.getJSONObject("region").get("Region_Id")));
                                mMyTravelModel.setRegion_Name("" + jsonobj.getJSONObject("region").get("Region_Name"));

                                Log.v("RegionName", "" + jsonobj.getJSONObject("region").getString("Region_Name"));
                                for (int i = 0; i < jsonobj.getJSONArray("hotelList").length(); i++) {
                                    JSONObject jsonObj = (JSONObject) jsonobj.getJSONArray("hotelList").get(i);

                                    mMyTravelModel.setHotel_Id(Integer.parseInt("" + jsonObj.get("Hotel_Id")));
                                    mMyTravelModel.setHotel_Name("" + jsonObj.get("Hotel_Name"));
                                    mMyTravelModel.setDestination_Id("" + jsonObj.get("Destination_Id"));
                                    mMyTravelModel.setHotel_Star_Rating("" + jsonObj.get("Hotel_Star_Rating"));

//                                    travelModel.setCheckInDate("" + jsonobj.getJSONObject("hotelbooking").get("Check_In"));
//                                    travelModel.setCheckOutDate("" + jsonobj.getJSONObject("hotelbooking").get("Check_Out"));
//                                    travelModel.setNo_of_Room_Days(Integer.parseInt("" + jsonobj.getJSONObject("hotelbooking").get("Num_Of_Roooms")));

                                    Log.v("HotelID", "" + jsonObj.get("Hotel_Id"));
                                }
                                for (int i = 0; i < jsonobj.getJSONArray("transportation").length(); i++) {
                                    JSONObject jsonObj = (JSONObject) jsonobj.getJSONArray("transportation").get(i);

                                    mMyTravelModel.setTransportation_Travel_Id(Integer.parseInt("" + jsonObj.get("Travel_Id")));
                                    mMyTravelModel.setTransportation_Start_Date("" + jsonObj.get("Start_Date"));
                                    mMyTravelModel.setTransportation_End_Date("" + jsonObj.get("End_Date"));
                                    mMyTravelModel.setTransportation_No_of_Days(Integer.parseInt("" + jsonObj.get("Num_Of_Days")));

                                    Log.v("TransportationID", "" + jsonObj.get("connect_Transportation_Booking_Id"));
                                }
                                for (int i = 0; i < jsonobj.getJSONArray("inclusion").length(); i++) {
                                    JSONObject jsonObj = (JSONObject) jsonobj.getJSONArray("inclusion").get(i);
                                    Log.v("InculsionID", "" + jsonObj.get("connect_Inculsion_Booking_Id"));
                                }
                                for (int i = 0; i < jsonobj.getJSONArray("customer").length(); i++) {
                                    JSONObject jsonObj = (JSONObject) jsonobj.getJSONArray("customer").get(i);

                                    mMyTravelModel.setCustomer_Id(Integer.parseInt("" + jsonObj.get("Customer_Id")));
                                    mMyTravelModel.setCustomer_Name("" + jsonObj.get("Customer_Name"));
                                    mMyTravelModel.setCustomer_Email("" + jsonObj.get("Email_Id"));

                                    Log.v("CustomerID", "" + jsonObj.get("Customer_Id"));
                                }
                                mPastList.add(mMyTravelModel);
                            }

                        }
                    }
                    if (mUpComingAdapter.getCount() == 0 && mPastTrips.getCount() == 0) {
                        Toast.makeText(getActivity(), "No Trips Available", Toast.LENGTH_LONG).show();
                    }

                //TODO: Handle exceptions properly.
                } catch (JSONException e) {

                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                pDialog.hide();

                mUpComingAdapter.notifyDataSetChanged();
                mPastAdapter.notifyDataSetChanged();
                Utility.setListViewHeightBasedOnChildren(mUpcomingTrips);
                Utility.setListViewHeightBasedOnChildren(mPastTrips);

            }
        }, new Response.ErrorListener() {

            //TODO: Error handling is incomplete. Empty if statements?
            @Override
            public void onErrorResponse(VolleyError error) {
                //System.err.println(error);
                // Handle your error types accordingly.For Timeout & No connection error, you can show 'retry' button.
                // For AuthFailure, you can re login with user credentials.
                // For ClientError, 400 & 401, Errors happening on client side when sending api request.
                // In this case you can check how client is forming the api and debug accordingly.
                // For ServerError 5xx, you can do retry or handle accordingly.
                if (error instanceof NetworkError) {
                    //Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                } else if (error instanceof AuthFailureError) {
                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                    //Toast.makeText(getActivity(), "No Internet Connection" ,Toast.LENGTH_LONG).show();
                } else if (error instanceof TimeoutError) {
                }
            }
        });

        //TODO: Never use magic numbers, declare constants with relative names.
        mJSONReq.setRetryPolicy(new DefaultRetryPolicy(10000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(mJSONReq);

        return mView;
    }

}
