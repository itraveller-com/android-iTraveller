package com.itraveller.dashboard;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itraveller.R;
import com.itraveller.adapter.MyTravelAdapter;
import com.itraveller.constant.Utility;
import com.itraveller.model.MyTravelModel;
import com.itraveller.moxtraChat.AccessTokenModel;
import com.itraveller.moxtraChat.AddUserResponse;
import com.itraveller.moxtraChat.CreateBinderRequest;
import com.itraveller.moxtraChat.CreateBinderResponse;
import com.itraveller.moxtraChat.PreferenceUtil;
import com.itraveller.moxtraChat.SingleChatActivity;
import com.itraveller.moxtraChat.GroupChatActivity;
import com.itraveller.moxtraChat.TravelDocsActivity;
import com.itraveller.volley.AppController;
import com.moxtra.sdk.MXAccountManager;
import com.moxtra.sdk.MXSDKConfig;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookedTripsFragment extends Fragment implements MXAccountManager.MXAccountLinkListener{

    //MOXTRA start

    private String regid;
    private AccessTokenModel agentAccessTokenModel=null,userAccessToken;
    private CreateBinderResponse createBinderResponse=null;
    private AddUserResponse addUserResponse=null;

    private String SENDER_ID = "132433516320";
    private GoogleCloudMessaging gcm;
    private GoogleApiClient client;

    private static final String TAG = "TripSummary";
    private static final String Binder_ID="BdVZvNgCvTXHzO7klSsivTC";

    String EmailArray[]=new String[5];
    //MOXTRA end


    public static String sFlag="group";

    private Button mGroupBtn;
    private Button mTravelDocsBtn;
    private Button mSingleBtn;
    private SharedPreferences mPrefs;
    private String mUserEmail;
    private Date mCurrentDate;
    private Date mFetchedDate;
    private SimpleDateFormat mFormatter;
    private ListView mUpcomingTrips,mPastTrips;
    private MyTravelAdapter mUpComingAdapter;
    private MyTravelAdapter mPastAdapter;

    public static List<MyTravelModel> sUpcomingList = new ArrayList<MyTravelModel>();
    public static List<MyTravelModel> sPastList = new ArrayList<MyTravelModel>();

    private String mCurrentDateTime;
    private String mCurrentDateStr;
    private String mFetchedDateStr;


    public static List<MyTravelHotelModel> sHotelList = new ArrayList<>();
    public static List<MyTravelActivityModel> sActivityList = new ArrayList<>();

    public static List<MyTravelHotelModel> sHotelListPast = new ArrayList<>();
    public static List<MyTravelActivityModel> sActivityListPast = new ArrayList<>();


    private static final String EMAIL = "email";
    private static final String ITINERARY_DATA = "ItineraryData";
    private static final String MATCHER_STRING = "support@itraveller.com";


    public static List<Integer> hotelSize=new ArrayList<>();
    public static List<Integer> activitySize=new ArrayList<>();

    public static List<Integer> hotelSizeP=new ArrayList<>();
    public static List<Integer> activitySizeP=new ArrayList<>();

    //public static HotelAdapter hotelAdapter;
    //public static ActivityAdapter activityAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.my_trips, container, false);

        ((MyTravelActivity) getActivity()).getSupportActionBar().setTitle(R.string.booked_fragment_title_string);

        mPrefs=getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        //MOXTRA code start

        EmailArray[0]="sudhilal.support@itraveller.com";
        EmailArray[1]="sachin.support@itraveller.com";
        EmailArray[2]="jinse.support@itraveller.com";
        EmailArray[3]="kapil.support@itraveller.com";
        EmailArray[4]=""+mPrefs.getString("email","");

        //MOXTA code end

        activitySize.clear();
        activitySizeP.clear();
        hotelSizeP.clear();
        hotelSize.clear();
        sUpcomingList.clear();
        sHotelListPast.clear();
        sHotelList.clear();
        sActivityListPast.clear();
        sActivityList.clear();

        mGroupBtn = (Button) view.findViewById(R.id.group_chat_btn);
        mTravelDocsBtn = (Button) view.findViewById(R.id.travel_docs_btn);
        mSingleBtn = (Button) view.findViewById(R.id.single_chat_btn);

        mUpcomingTrips=(ListView) view.findViewById(R.id.upcoming_trips_list);
        mPastTrips=(ListView) view.findViewById(R.id.past_trips_list);

        mUpComingAdapter = new MyTravelAdapter(getActivity(), sUpcomingList);
        mUpcomingTrips.setAdapter(mUpComingAdapter);

        mPastAdapter = new MyTravelAdapter(getActivity(), sPastList);
        mPastTrips.setAdapter(mPastAdapter);


//        hotelAdapter = new HotelAdapter(getActivity(), hotelList);

//        activityAdapter = new ActivityAdapter(getActivity(), activityList);


    /*    if(upcomingAdapter.getCount()>0)
        {
            group_btn.setVisibility(View.VISIBLE);
            single_btn.setVisibility(View.VISIBLE);


        }
    */

        if((""+mPrefs.getString(EMAIL,"")).contains(MATCHER_STRING))
        {
            mGroupBtn.setText(R.string.group_btn_text);
            mSingleBtn.setVisibility(View.GONE);
            mTravelDocsBtn.setVisibility(View.GONE);
            mUpcomingTrips.setVisibility(View.GONE);
            mPastTrips.setVisibility(View.GONE);
        }

        mGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent mIntent = new Intent(getActivity(), GroupChatActivity.class);
                    startActivity(mIntent);
            }


        });

        mTravelDocsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(getActivity(), TravelDocsActivity.class);
                startActivity(mIntent);
            }
        });

        mSingleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent mIntent = new Intent(getActivity(), SingleChatActivity.class);
                    startActivity(mIntent);
            }
        });


        mUpcomingTrips.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent mIntent=new Intent(getActivity(),TripSummary.class);
                mIntent.putExtra("ItineraryData", position);
                mIntent.putExtra("LFlag","upcoming");
                startActivity(mIntent);
            }
        });

        mPastTrips.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(getActivity(), GroupChatActivity.class);
                i.putExtra("ItineraryData", position);
                i.putExtra("LFlag","past");
                startActivity(i);
            }
       });


        mFormatter = new SimpleDateFormat("yyyy-MM-dd");

        mCurrentDateTime=""+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        String dateTimeArr[]=mCurrentDateTime.split(" ");
        mCurrentDateStr=dateTimeArr[0];

        try {
            mCurrentDate = mFormatter.parse(mCurrentDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Fetching Your Trips...");
        pDialog.setCancelable(false);
        pDialog.show();

        mUserEmail=""+mPrefs.getString(EMAIL,"");

        String url = "http://stage.itraveller.com/backend/api/v1/customerpackages?email="+mUserEmail+"&booked=1";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("Response is", "" + response);

                try {
                        Log.d("Boolean", "" + response.getBoolean("success"));
                        Log.d("Error", ""+response.getJSONObject("error"));
                        Log.d("Payload_regions", ""+response.getJSONObject("payload"));
                        Iterator<?> keys = response.getJSONObject("payload").keys();
                        while(keys.hasNext() )
                        {

                            MyTravelModel myTravelModel=new MyTravelModel();


                            String key = (String) keys.next();
                            if (response.getJSONObject("payload").get(key) instanceof JSONObject)
                            {

                                JSONObject jsonobj = new JSONObject(response.getJSONObject("payload").get(key).toString());
                                Log.v("ID","" + jsonobj.getJSONObject("itinerary").getString("Id"));

                                mFetchedDateStr="" + jsonobj.getJSONObject("itinerary").get("Date_Of_Travel");

                                mFetchedDate = mFormatter.parse(mFetchedDateStr);

                                if(mFetchedDate.compareTo(mCurrentDate)>0)
                                {
                                    myTravelModel.setItinerary_Id(Integer.parseInt("" + jsonobj.getJSONObject("itinerary").get("Id")));
                                    myTravelModel.setItinerary_Main_Id("" + jsonobj.getJSONObject("itinerary").get("Travel_Id"));
                                    myTravelModel.setPackageValue(Integer.parseInt("" + jsonobj.getJSONObject("itinerary").get("Package_Value")));
                                    myTravelModel.setTravelDate("" + jsonobj.getJSONObject("itinerary").get("Date_Of_Travel"));
                                    myTravelModel.setNo_of_Days(Integer.parseInt("" + jsonobj.getJSONObject("itinerary").get("No_Of_Days")));
                                    myTravelModel.setBooking_Date("" + jsonobj.getJSONObject("itinerary").get("Date_Of_Booking"));
                                    myTravelModel.setNo_of_Adults(Integer.parseInt("" + jsonobj.getJSONObject("itinerary").get("Adults_Pax")));
                                    myTravelModel.setNo_of_Child(Integer.parseInt("" + jsonobj.getJSONObject("itinerary").get("Children_Pax")));
                                    myTravelModel.setNo_of_Infant(Integer.parseInt("" + jsonobj.getJSONObject("itinerary").get("Infants_Pax")));

                                    if(("" + jsonobj.getJSONObject("itinerary").get("Discount")).equals("null"))
                                    {
                                        myTravelModel.setDiscount_value("0");
                                    }
                                    else
                                    {
                                        myTravelModel.setDiscount_value("" + jsonobj.getJSONObject("itinerary").get("Discount"));
                                    }

                                    if(("" + jsonobj.getJSONObject("itinerary").get("Advance_Pmt_Amount")).equals("null"))
                                    {
                                        myTravelModel.setAdv_amt("0");
                                    }
                                    else
                                    {
                                        myTravelModel.setAdv_amt("" + jsonobj.getJSONObject("itinerary").get("Advance_Pmt_Amount"));
                                    }

                                    if(("" + jsonobj.getJSONObject("itinerary").get("Intermediate_Pmt_Amount")).equals("null"))
                                    {
                                        myTravelModel.setInter_amt("0");
                                    }
                                    else
                                    {
                                        myTravelModel.setInter_amt("" + jsonobj.getJSONObject("itinerary").get("Intermediate_Pmt_Amount"));
                                    }

                                    if(("" + jsonobj.getJSONObject("itinerary").get("Final_Pmt_Amount")).equals("null"))
                                    {
                                        myTravelModel.setFinal_amt("0");
                                    }
                                    else
                                    {
                                        myTravelModel.setFinal_amt("" + jsonobj.getJSONObject("itinerary").get("Final_Pmt_Amount"));
                                    }


                                    if(("" + jsonobj.getJSONObject("itinerary").get("Flight_Price")).equals("null"))
                                    {
                                        myTravelModel.setFlight_price("0");
                                    }
                                    else
                                    {
                                        myTravelModel.setFlight_price("" + jsonobj.getJSONObject("itinerary").get("Flight_Price"));
                                    }
                                    myTravelModel.setRegion_Id(Integer.parseInt("" + jsonobj.getJSONObject("region").get("Region_Id")));
                                    myTravelModel.setRegion_Name("" + jsonobj.getJSONObject("region").get("Region_Name"));

                                    Log.v("RegionName", "" + jsonobj.getJSONObject("region").getString("Region_Name"));


                                    hotelSize.add(jsonobj.getJSONArray("hotelList").length());
                                    for(int i = 0; i < jsonobj.getJSONArray("hotelList").length() ; i++)
                                    {

                                        MyTravelHotelModel hotelModel=new MyTravelHotelModel();

                                        JSONObject jsonObjH = (JSONObject) jsonobj.getJSONArray("hotelList").get(i);

                                        myTravelModel.setHotel_Id(Integer.parseInt("" + jsonObjH.get("Hotel_Id")));
                                        myTravelModel.setHotel_Name("" + jsonObjH.get("Hotel_Name"));
                                        myTravelModel.setDestination_Id("" + jsonObjH.get("Destination_Id"));
                                        myTravelModel.setHotel_Star_Rating("" + jsonObjH.get("Hotel_Star_Rating"));
                                        myTravelModel.setDestination_name("" + jsonObjH.get("Destination_Name"));

                                        hotelModel.setHotelName("" + jsonObjH.get("Hotel_Name"));
                                        hotelModel.setDestinationName("" + jsonObjH.get("Destination_Name"));

                                        JSONObject hotelObj=jsonObjH.getJSONObject("hotelbooking");

                                        myTravelModel.setHotel_Room_Type("" + jsonObjH.get("RoomName"));
                                        myTravelModel.setCheckInDate("" + hotelObj.get("Check_In"));
                                        myTravelModel.setCheckOutDate("" + hotelObj.get("Check_Out"));
                                        myTravelModel.setNo_of_Room_Days(Integer.parseInt("" + hotelObj.get("Num_Of_Days")));
                                        myTravelModel.setNo_of_Rooms(Integer.parseInt("" + hotelObj.get("Num_Of_Rooms")));

                                        hotelModel.setHotelRoomType("" + jsonObjH.get("RoomName"));
                                        hotelModel.setCheckInDate("" + hotelObj.get("Check_In"));
                                        hotelModel.setCheckOutDate("" + hotelObj.get("Check_Out"));
                                        hotelModel.setNoOfRoomDays(Integer.parseInt("" + hotelObj.get("Num_Of_Days")));
                                        hotelModel.setNoOfRooms(Integer.parseInt("" + hotelObj.get("Num_Of_Rooms")));

                                        sHotelList.add(hotelModel);

                                        Log.v("HotelID", "" + jsonObjH.get("Hotel_Id"));
                                    }

                                    for(int i = 0; i < jsonobj.getJSONArray("transportation").length() ; i++)
                                    {
                                        JSONObject jsonObj = (JSONObject) jsonobj.getJSONArray("transportation").get(i);

                                        myTravelModel.setTransportation_Name(""+jsonObj.get("Vehicle"));

                                        Log.v("TransportationID", "" + jsonObj.get("connect_Transportation_Booking_Id"));
                                    }

                                    activitySize.add(jsonobj.getJSONArray("inclusion").length());
                                    for(int i = 0; i < jsonobj.getJSONArray("inclusion").length() ; i++)
                                    {
                                        JSONObject jsonObj = (JSONObject) jsonobj.getJSONArray("inclusion").get(i);

                                        myTravelModel.setActivity_Name("" + jsonObj.get("Inclusion_Title"));
                                        myTravelModel.setActivity_Date("" + jsonObj.get("Inclusion_Date"));

                                        MyTravelActivityModel activityModel=new MyTravelActivityModel();

                                        activityModel.setActivityName("" + jsonObj.get("Inclusion_Title"));
                                        activityModel.setActivityDate("" + jsonObj.get("Inclusion_Date"));

                                        Log.d("InculsionName", "" + jsonObj.get("Inclusion_Date"));
                                        sActivityList.add(activityModel);
                                    }
                                    for(int i = 0; i < jsonobj.getJSONArray("customer").length() ; i++)
                                    {
                                        JSONObject jsonObj = (JSONObject) jsonobj.getJSONArray("customer").get(i);

                                        myTravelModel.setCustomer_Id(Integer.parseInt("" + jsonObj.get("Customer_Id")));
                                        myTravelModel.setCustomer_Name("" + jsonObj.get("Customer_Name"));
                                        myTravelModel.setCustomer_Email("" + jsonObj.get("Email_Id"));
                                        myTravelModel.setCustomer_Phone_Number(""+jsonObj.get("Phone_Num"));

                                        Log.v("CustomerID", "" + jsonObj.get("Customer_Id"));
                                    }

                                    sUpcomingList.add(myTravelModel);
                                }
                                else
                                {
                                    myTravelModel.setItinerary_Id(Integer.parseInt("" + jsonobj.getJSONObject("itinerary").get("Id")));
                                    myTravelModel.setItinerary_Main_Id("" + jsonobj.getJSONObject("itinerary").get("Travel_Id"));
                                    myTravelModel.setPackageValue(Integer.parseInt("" + jsonobj.getJSONObject("itinerary").get("Package_Value")));
                                    myTravelModel.setTravelDate("" + jsonobj.getJSONObject("itinerary").get("Date_Of_Travel"));
                                    myTravelModel.setNo_of_Days(Integer.parseInt("" + jsonobj.getJSONObject("itinerary").get("No_Of_Days")));
                                    myTravelModel.setBooking_Date("" + jsonobj.getJSONObject("itinerary").get("Date_Of_Booking"));
                                    myTravelModel.setNo_of_Adults(Integer.parseInt("" + jsonobj.getJSONObject("itinerary").get("Adults_Pax")));
                                    myTravelModel.setNo_of_Child(Integer.parseInt("" + jsonobj.getJSONObject("itinerary").get("Children_Pax")));
                                    myTravelModel.setNo_of_Infant(Integer.parseInt("" + jsonobj.getJSONObject("itinerary").get("Infants_Pax")));


                                    if(("" + jsonobj.getJSONObject("itinerary").get("Discount")).equals("null"))
                                    {
                                        myTravelModel.setDiscount_value("0");
                                    }
                                    else
                                    {
                                        myTravelModel.setDiscount_value("" + jsonobj.getJSONObject("itinerary").get("Discount"));
                                    }

                                    if(("" + jsonobj.getJSONObject("itinerary").get("Advance_Pmt_Amount")).equals("null"))
                                    {
                                        myTravelModel.setAdv_amt("0");
                                    }
                                    else
                                    {
                                        myTravelModel.setAdv_amt("" + jsonobj.getJSONObject("itinerary").get("Advance_Pmt_Amount"));
                                    }

                                    if(("" + jsonobj.getJSONObject("itinerary").get("Intermediate_Pmt_Amount")).equals("null"))
                                    {
                                        myTravelModel.setInter_amt("0");
                                    }
                                    else
                                    {
                                        myTravelModel.setInter_amt("" + jsonobj.getJSONObject("itinerary").get("Intermediate_Pmt_Amount"));
                                    }

                                    if(("" + jsonobj.getJSONObject("itinerary").get("Final_Pmt_Amount")).equals("null"))
                                    {
                                        myTravelModel.setFinal_amt("0");
                                    }
                                    else
                                    {
                                        myTravelModel.setFinal_amt("" + jsonobj.getJSONObject("itinerary").get("Final_Pmt_Amount"));
                                    }

                                    if(("" + jsonobj.getJSONObject("itinerary").get("Flight_Price")).equals("null"))
                                    {
                                        myTravelModel.setFlight_price("0");
                                    }
                                    else
                                    {
                                        myTravelModel.setFlight_price("" + jsonobj.getJSONObject("itinerary").get("Flight_Price"));
                                    }

                                    myTravelModel.setRegion_Id(Integer.parseInt("" + jsonobj.getJSONObject("region").get("Region_Id")));
                                    myTravelModel.setRegion_Name("" + jsonobj.getJSONObject("region").get("Region_Name"));

                                    Log.v("RegionName", "" + jsonobj.getJSONObject("region").getString("Region_Name"));


                                    hotelSizeP.add(jsonobj.getJSONArray("hotelList").length());
                                    for(int i = 0; i < jsonobj.getJSONArray("hotelList").length() ; i++)
                                    {

                                        MyTravelHotelModel hotelModel=new MyTravelHotelModel();

                                        JSONObject jsonObjH = (JSONObject) jsonobj.getJSONArray("hotelList").get(i);

                                        myTravelModel.setHotel_Id(Integer.parseInt("" + jsonObjH.get("Hotel_Id")));
                                        myTravelModel.setHotel_Name("" + jsonObjH.get("Hotel_Name"));
                                        myTravelModel.setDestination_Id("" + jsonObjH.get("Destination_Id"));
                                        myTravelModel.setHotel_Star_Rating("" + jsonObjH.get("Hotel_Star_Rating"));
                                        myTravelModel.setDestination_name("" + jsonObjH.get("Destination_Name"));

                                        hotelModel.setHotelName("" + jsonObjH.get("Hotel_Name"));
                                        hotelModel.setDestinationName("" + jsonObjH.get("Destination_Name"));

                                        JSONObject hotelObj=jsonObjH.getJSONObject("hotelbooking");

                                        myTravelModel.setHotel_Room_Type("" + jsonObjH.get("RoomName"));
                                        myTravelModel.setCheckInDate("" + hotelObj.get("Check_In"));
                                        myTravelModel.setCheckOutDate("" + hotelObj.get("Check_Out"));
                                        myTravelModel.setNo_of_Room_Days(Integer.parseInt("" + hotelObj.get("Num_Of_Days")));
                                        myTravelModel.setNo_of_Rooms(Integer.parseInt("" + hotelObj.get("Num_Of_Rooms")));


                                        hotelModel.setHotelRoomType("" + jsonObjH.get("RoomName"));
                                        hotelModel.setCheckInDate("" + hotelObj.get("Check_In"));
                                        hotelModel.setCheckOutDate("" + hotelObj.get("Check_Out"));
                                        hotelModel.setNoOfRoomDays(Integer.parseInt("" + hotelObj.get("Num_Of_Days")));
                                        hotelModel.setNoOfRooms(Integer.parseInt("" + hotelObj.get("Num_Of_Rooms")));

                                        sHotelListPast.add(hotelModel);

                                        Log.v("HotelID", "" + jsonObjH.get("Hotel_Id"));
                                    }
                                    for(int i = 0; i < jsonobj.getJSONArray("transportation").length() ; i++)
                                    {
                                        JSONObject jsonObj = (JSONObject) jsonobj.getJSONArray("transportation").get(i);

                                        myTravelModel.setTransportation_Name(""+jsonObj.get("Vehicle"));

                                        Log.v("TransportationID", "" + jsonObj.get("connect_Transportation_Booking_Id"));
                                    }

                                    activitySizeP.add(jsonobj.getJSONArray("inclusion").length());
                                    for(int i = 0; i < jsonobj.getJSONArray("inclusion").length() ; i++)
                                    {
                                        JSONObject jsonObj = (JSONObject) jsonobj.getJSONArray("inclusion").get(i);

                                        myTravelModel.setActivity_Name(""+jsonObj.get("Inclusion_Title"));
                                        myTravelModel.setActivity_Date(""+jsonObj.get("Inclusion_Date"));


                                        MyTravelActivityModel activityModel=new MyTravelActivityModel();

                                        activityModel.setActivityName("" + jsonObj.get("Inclusion_Title"));
                                        activityModel.setActivityDate("" + jsonObj.get("Inclusion_Date"));

                                        sActivityListPast.add(activityModel);

                                        Log.v("InculsionID", "" + jsonObj.get("connect_Inculsion_Booking_Id"));
                                    }
                                    for(int i = 0; i < jsonobj.getJSONArray("customer").length() ; i++)
                                    {
                                        JSONObject jsonObj = (JSONObject) jsonobj.getJSONArray("customer").get(i);

                                        myTravelModel.setCustomer_Id(Integer.parseInt("" + jsonObj.get("Customer_Id")));
                                        myTravelModel.setCustomer_Name("" + jsonObj.get("Customer_Name"));
                                        myTravelModel.setCustomer_Email("" + jsonObj.get("Email_Id"));
                                        myTravelModel.setCustomer_Phone_Number("" + jsonObj.get("Phone_Num"));

                                        Log.v("CustomerID", "" + jsonObj.get("Customer_Id"));
                                    }
                                    sPastList.add(myTravelModel);
                                }

                            }
                        }
                        if(mUpComingAdapter.getCount()==0 && mPastTrips.getCount()==0)
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

                //hotelAdapter.notifyDataSetChanged();
                //activityAdapter.notifyDataSetChanged();
                mUpComingAdapter.notifyDataSetChanged();
                mPastAdapter.notifyDataSetChanged();
                Utility.setListViewHeightBasedOnChildren(mUpcomingTrips);
                Utility.setListViewHeightBasedOnChildren(mPastTrips);

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

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(jsonObjectRequest);

            return view;
    }


    //MOXTRA registration code start
    public void registerMOXTRAGroupChat()
    {
        new AsyncTask<Void, Void, AddUserResponse>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                if (mProgressView != null) {
//                    mProgressView.setVisibility(View.VISIBLE);
//                }
            }

            @Override
            protected AddUserResponse doInBackground(Void... params) {
                String msg = "";
                try {

                    //create object for user accesstoken
                    agentAccessTokenModel = new AccessTokenModel();
                    /**
                     * @params accessTokenModel Object, userName, firstname
                     */
                    userAccessToken = new AccessTokenModel();
                    agentAccessTokenModel = getAccessTokenGroup(agentAccessTokenModel, "Rohan_admin", "Rohan_admin");

                    if(!agentAccessTokenModel.getAccesstoken().isEmpty())
                    {
                        PreferenceUtil.setGroupTokenId(AppController.context, agentAccessTokenModel.getAccesstoken());

                        userAccessToken = getAccessTokenGroup(agentAccessTokenModel, ""+mPrefs.getString("name",""),""+mPrefs.getString("email",""));

                        addUserResponse = new AddUserResponse();

                        addUserResponse = createUserInBinderGroup(addUserResponse);

                        PreferenceUtil.setGroupBinderId(AppController.context, Binder_ID);
                    }

                } catch (Exception ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.e(TAG, "Error when register on GCM.", ex);
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }return addUserResponse;
            }

            @Override
            protected void onPostExecute(AddUserResponse msg) {

                if(msg.getCode()==null){

                    registerMOXTRASingleChat();
                    //setupMoxtraUser();
                }
                else
                {
                    if (msg.getCode().equalsIgnoreCase("RESPONSE_SUCCESS")) {
                        registerMOXTRASingleChat();
                        //setupMoxtraUser();
                    } else if (msg.getCode().equalsIgnoreCase("RESPONSE_ERROR_INVALID_REQUEST")) {
                        Log.d("MOXTRA", "Hello");
                    }
                }
//                if (mProgressView != null) {
//                    mProgressView.setVisibility(View.GONE);
//                }
            }

        }.execute(null, null, null);
    }
    public AccessTokenModel getAccessTokenGroup(AccessTokenModel accessTokenModel,String firtName,String usrName)
    {

/**
 * generate Timestamp in milisecond
 */
        long time = System.currentTimeMillis();

        try {
            //AccessToken URL
            final String url = "https://apisandbox.moxtra.com/oauth/token?client_id="+AppController.context.getString(R.string.client_id)+"&client_secret="+AppController.context.getString(R.string.client_secret)+"&grant_type=http://www.moxtra.com/auth_uniqueid&uniqueid="+usrName+"&firstname="+firtName+"&orgid"+AppController.context.getResources().getString( R.string.orgId)+"&timestamp=" + time;


            /**
             * RestApi Call
             */
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            /**
             * Java class for Token
             */
            accessTokenModel = restTemplate.getForObject(url, AccessTokenModel.class);

            System.out.print("Token" + accessTokenModel.getAccesstoken());
        }
        catch (Exception e)
        {
            return null;
        }

        return accessTokenModel;
    }

    private AddUserResponse createUserInBinderGroup(AddUserResponse addUserResponse){


        try
        {
            // Simulate network access.
            // Set the Content-Type header
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(new MediaType("application", "json"));

            //User phone number json format
            JSONObject jsonObject = null;
            try
            {
                jsonObject = new JSONObject("{\"users\":[{\"user\":{\"unique_id\":"+"\"" +""+mPrefs.getString("email","")+"\""+"}}]}");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            HttpEntity<String> requestEntity = new HttpEntity<String>(jsonObject.toString(), requestHeaders);

            //add a user to the Binder
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            final String url = "https://apisandbox.moxtra.com/v1/" + Binder_ID + "/addteamuser?access_token=" + PreferenceUtil.getGroupTokenId(AppController.context);

            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            String result = responseEntity.getBody();

/**
 * Gson Lib for serialize the object
 */
            Gson gson = new GsonBuilder().serializeNulls().create();
            gson = new GsonBuilder().serializeNulls().create();
            //Parse the user response
            addUserResponse = gson.fromJson(result, AddUserResponse.class);

            return addUserResponse;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return addUserResponse;
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                if (mProgressView != null) {
//                    mProgressView.setVisibility(View.VISIBLE);
//                }
            }

            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getActivity());
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // Save the regid in Moxtra so we can get the GCM notification.
                    try {

                        Bitmap avatar = BitmapFactory.decodeStream(getActivity().getAssets().open("FA01.png"));
                        final MXSDKConfig.MXUserInfo mxUserInfo = new MXSDKConfig.MXUserInfo(""+mPrefs.getString("email",""), MXSDKConfig.MXUserIdentityType.IdentityUniqueId);
                        final MXSDKConfig.MXProfileInfo mxProfileInfo = new MXSDKConfig.MXProfileInfo(""+mPrefs.getString("name",""), ""+mPrefs.getString("name",""), avatar);


                        //commented by ROHAN
                    //    MXAccountManager.getInstance().setupUser(mxUserInfo, mxProfileInfo,AppController.context.getResources().getString( R.string.orgId), regid, this);

                        registerItraveller();

                    } catch (IOException e) {
                        Log.e(TAG, "Can't decode avatar.", e);
                    }

                    // Persist the regID - no need to register again.
                    PreferenceUtil.setGcmRegId(getActivity(), regid);
                    PreferenceUtil.setAppVersion(getActivity(), getAppVersion(getActivity()));
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.e(TAG, "Error when register on GCM.", ex);
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
//                if (mProgressView != null) {
//                    mProgressView.setVisibility(View.GONE);
//                }
                Log.d(TAG, "Reg done: " + msg);
            }
        }.execute(null, null, null);
    }

    public void registerMOXTRASingleChat()
    {
        new AsyncTask<Void, Void, AddUserResponse>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                if (mProgressView != null) {
//                    mProgressView.setVisibility(View.VISIBLE);
//                }
            }

            @Override
            protected AddUserResponse doInBackground(Void... params) {
                String msg = "";
                try {

                    //create object for user accesstoken
                    agentAccessTokenModel = new AccessTokenModel();
                    /**
                     * @params accessTokenModel Object, userName, firstname
                     */

                    userAccessToken = new AccessTokenModel();
                    agentAccessTokenModel = getAccessTokenSingle(agentAccessTokenModel, "Rohan_admin1", "Rohan_admin1");

                    if(!agentAccessTokenModel.getAccesstoken().isEmpty())
                    {
                        PreferenceUtil.setSingleTokenId(AppController.context, agentAccessTokenModel.getAccesstoken());

                        for(int i=0;i<EmailArray.length;i++)
                            userAccessToken = getAccessTokenSingle(userAccessToken, EmailArray[i],EmailArray[i]);
                        //userAccessToken = getAccessToken(agentAccessTokenModel, ""+prefs.getString("name",""),""+prefs.getString("name",""));

                        //if(!PreferenceUtil.getBinderId(AppController.context).isEmpty()) {
                        createBinderResponse = new CreateBinderResponse();
                        //create Binder method
                        //create Binder for or meet topic
                        createBinderSingle(PreferenceUtil.getSingleTokenId(AppController.context));
                        //}

                        addUserResponse = new AddUserResponse();

                        for(int i=0;i<EmailArray.length;i++)
                            addUserResponse = createUserInBinderSingle(addUserResponse, EmailArray[i]);
                    }

                } catch (Exception ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.e(TAG, "Error when register on GCM.", ex);
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }return addUserResponse;
            }

            @Override
            protected void onPostExecute(AddUserResponse msg) {

                if (msg.getCode().equalsIgnoreCase("RESPONSE_SUCCESS")){
                    registerMOXTRATravelDocs();
                    //setupMoxtraUser();
                }
                else{
                    Log.d("MOXTRA","Hello");
                }
//                if (mProgressView != null) {
//                    mProgressView.setVisibility(View.GONE);
//
//
//                }
            }

        }.execute(null, null, null);


    }

    public AccessTokenModel getAccessTokenSingle(AccessTokenModel accessTokenModel,String firtName,String usrName)
    {
        /**
         * generate Timestamp in milisecond
         */
        long time = System.currentTimeMillis();

        try {
            //AccessToken URL
            final String url = "https://apisandbox.moxtra.com/oauth/token?client_id="+AppController.context.getString(R.string.client_id)+"&client_secret="+AppController.context.getString(R.string.client_secret)+"&grant_type=http://www.moxtra.com/auth_uniqueid&uniqueid="+usrName+"&firstname="+firtName+"&orgid="+AppController.context.getResources().getString( R.string.orgId)+"&timestamp=" + time;

            /**
             * RestApi Call
             */
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            /**
             * Java class for Token
             */
            accessTokenModel = restTemplate.getForObject(url, AccessTokenModel.class);

            System.out.print("Token" + accessTokenModel.getAccesstoken());
        }
        catch (Exception e)
        {
            return null;
        }

        return accessTokenModel;
    }

    private AddUserResponse createUserInBinderSingle(AddUserResponse addUserResponse,String email){


        try
        {
            // Simulate network access.
            // Set the Content-Type header
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(new MediaType("application", "json"));

            //User phone number json format
            JSONObject jsonObject = null;
            try
            {
                jsonObject = new JSONObject("{\"users\":[{\"user\":{\"unique_id\":"+ "\""+email+"\""+"}}]}");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            HttpEntity<String> requestEntity = new HttpEntity<String>(jsonObject.toString(), requestHeaders);


            //add a user to the Binder
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            final String url = "https://apisandbox.moxtra.com/v1/" + PreferenceUtil.getSingleBinderId(AppController.context) + "/addteamuser?access_token=" + PreferenceUtil.getSingleTokenId(AppController.context);

            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            String result = responseEntity.getBody();

            /**
             * Gson Lib for serialize the object
             */
            Gson gson = new GsonBuilder().serializeNulls().create();
            gson = new GsonBuilder().serializeNulls().create();
            //Parse the user response
            addUserResponse = gson.fromJson(result, AddUserResponse.class);

            return addUserResponse;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return addUserResponse;
    }

    private CreateBinderResponse createBinderSingle(String mAccessToken) {
        String msg = "";


        try {
            createBinderResponse = new CreateBinderResponse();
            CreateBinderRequest createBidnerRequest = new CreateBinderRequest();
            String email=""+mPrefs.getString("email","");
            createBidnerRequest.setName(PreferenceUtil.getUser(AppController.context)+" "+"ItrTravelSnaps "+email);

            // Set the Content-Type header
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(new MediaType("application", "json"));
            HttpEntity<CreateBinderRequest> requestEntity = new HttpEntity<CreateBinderRequest>(createBidnerRequest, requestHeaders);

            final String url = "https://apisandbox.moxtra.com/v1/me/binders?access_token=" + mAccessToken;

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            String result = responseEntity.getBody();

            Gson gson = new GsonBuilder().serializeNulls().create();
            gson = new GsonBuilder().serializeNulls().create();

            createBinderResponse = gson.fromJson(result,
                    CreateBinderResponse.class);
            CreateBinderResponse.BinderData binderData = createBinderResponse.new BinderData();

            binderData.setId(createBinderResponse.getData().getId());
            binderData.setName(createBinderResponse.getData().getName());
            binderData.setCreaetd_time(createBinderResponse.getData().getCreaetd_time());
            binderData.setCreaetd_time(createBinderResponse.getData().getUpdated_time());
            PreferenceUtil.setSingleBinderId(AppController.context, binderData.getId());

            Log.d(TAG, "Binder ID================: " + binderData.getId());
        }
        catch (Exception e)
        {
            Log.e(TAG, "Error when register of binder.", e);
        }
        return  createBinderResponse;
    }

    public void registerMOXTRATravelDocs()
    {
        new AsyncTask<Void, Void, AddUserResponse>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                if (mProgressView != null) {
//                    mProgressView.setVisibility(View.VISIBLE);
//                }
            }

            @Override
            protected AddUserResponse doInBackground(Void... params) {
                String msg = "";
                try {

                    //create object for user accesstoken
                    agentAccessTokenModel = new AccessTokenModel();
                    /**
                     * @params accessTokenModel Object, userName, firstname
                     */

                    userAccessToken = new AccessTokenModel();
                    agentAccessTokenModel = getAccessTokenTravelDocs(agentAccessTokenModel, "Rohan_admin1", "Rohan_admin1");

                    if(!agentAccessTokenModel.getAccesstoken().isEmpty())
                    {
                        PreferenceUtil.setTravelTokenId(AppController.context, agentAccessTokenModel.getAccesstoken());

                        for(int i=0;i<EmailArray.length;i++)
                            userAccessToken = getAccessTokenTravelDocs(userAccessToken, EmailArray[i],EmailArray[i]);
                        //userAccessToken = getAccessToken(agentAccessTokenModel, ""+prefs.getString("name",""),""+prefs.getString("name",""));

                        //if(!PreferenceUtil.getBinderId(AppController.context).isEmpty()) {
                        createBinderResponse = new CreateBinderResponse();
                        //create Binder method
                        //create Binder for or meet topic
                        createBinderTravelDocs(PreferenceUtil.getTravelTokenId(AppController.context));
                        //}

                        addUserResponse = new AddUserResponse();

                        for(int i=0;i<EmailArray.length-1;i++)
                            addUserResponse = createUserInBinderTravelDocs(addUserResponse,EmailArray[i]);

                        addUserResponse= createUserInBinderViewerTravelDocs(addUserResponse,EmailArray[(EmailArray.length-1)]);
                    }

                } catch (Exception ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.e(TAG, "Error when register on GCM.", ex);
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }return addUserResponse;
            }

            @Override
            protected void onPostExecute(AddUserResponse msg) {

                if (msg.getCode().equalsIgnoreCase("RESPONSE_SUCCESS")){

                    registerInBackground();
                    //regregisterItraveller();
                    //setupMoxtraUser();
                }
                else{
                    Log.d("MOXTRA","Hello");
                }
//                if (mProgressView != null) {
//                    mProgressView.setVisibility(View.GONE);
//
//
//                }
            }

        }.execute(null, null, null);
    }

    public AccessTokenModel getAccessTokenTravelDocs(AccessTokenModel accessTokenModel,String firtName,String usrName)
    {
        /**
         * generate Timestamp in milisecond
         */
        long time = System.currentTimeMillis();

        try {
            //AccessToken URL
            final String url = "https://apisandbox.moxtra.com/oauth/token?client_id="+AppController.context.getString(R.string.client_id)+"&client_secret="+AppController.context.getString(R.string.client_secret)+"&grant_type=http://www.moxtra.com/auth_uniqueid&uniqueid="+usrName+"&firstname="+firtName+"&orgid="+AppController.context.getResources().getString( R.string.orgId)+"&timestamp=" + time;
            /**
             * RestApi Call
             */
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            /**
             * Java class for Token
             */
            accessTokenModel = restTemplate.getForObject(url, AccessTokenModel.class);

            System.out.print("Token" + accessTokenModel.getAccesstoken());
        }
        catch (Exception e)
        {
            return null;
        }

        return accessTokenModel;
    }

    private CreateBinderResponse createBinderTravelDocs(String mAccessToken) {
        String msg = "";


        try {
            createBinderResponse = new CreateBinderResponse();
            CreateBinderRequest createBidnerRequest = new CreateBinderRequest();
            String email=""+mPrefs.getString("email","");
            createBidnerRequest.setName(PreferenceUtil.getUser(AppController.context)+" "+"ItrTravelDocs "+email);

            // Set the Content-Type header
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(new MediaType("application", "json"));
            HttpEntity<CreateBinderRequest> requestEntity = new HttpEntity<CreateBinderRequest>(createBidnerRequest, requestHeaders);

            final String url = "https://apisandbox.moxtra.com/v1/me/binders?access_token=" + mAccessToken;

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            String result = responseEntity.getBody();

            Gson gson = new GsonBuilder().serializeNulls().create();
            gson = new GsonBuilder().serializeNulls().create();

            createBinderResponse = gson.fromJson(result,
                    CreateBinderResponse.class);
            CreateBinderResponse.BinderData binderData = createBinderResponse.new BinderData();

            binderData.setId(createBinderResponse.getData().getId());
            binderData.setName(createBinderResponse.getData().getName());
            binderData.setCreaetd_time(createBinderResponse.getData().getCreaetd_time());
            binderData.setCreaetd_time(createBinderResponse.getData().getUpdated_time());
            PreferenceUtil.setTravelBinderId(AppController.context, binderData.getId());

            Log.d(TAG, "Binder ID================: " + binderData.getId());
        }
        catch (Exception e)
        {
            Log.e(TAG, "Error when register of binder.", e);
        }
        return  createBinderResponse;
    }

    private AddUserResponse createUserInBinderTravelDocs(AddUserResponse addUserResponse,String email){


        try
        {
            // Simulate network access.
            // Set the Content-Type header
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(new MediaType("application", "json"));

            //User phone number json format
            JSONObject jsonObject = null;
            try
            {
                jsonObject = new JSONObject("{\"users\":[{\"user\":{\"unique_id\":"+ "\""+email+"\""+"}}]}");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            HttpEntity<String> requestEntity = new HttpEntity<String>(jsonObject.toString(), requestHeaders);


            //add a user to the Binder
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            final String url = "https://apisandbox.moxtra.com/v1/" + PreferenceUtil.getTravelBinderId(AppController.context) + "/addteamuser?access_token=" + PreferenceUtil.getTravelTokenId(AppController.context);

            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            String result = responseEntity.getBody();

            /**
             * Gson Lib for serialize the object
             */
            Gson gson = new GsonBuilder().serializeNulls().create();
            gson = new GsonBuilder().serializeNulls().create();
            //Parse the user response
            addUserResponse = gson.fromJson(result, AddUserResponse.class);

            return addUserResponse;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return addUserResponse;
    }

    private AddUserResponse createUserInBinderViewerTravelDocs(AddUserResponse addUserResponse,String email){


        try
        {
            // Simulate network access.
            // Set the Content-Type header
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(new MediaType("application", "json"));

            //User phone number json format
            JSONObject jsonObject = null;
            try
            {
                jsonObject = new JSONObject("{\"users\":[{\"read_only\": true,\"user\":{\"unique_id\":"+ "\""+email+"\""+"}}]}");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            HttpEntity<String> requestEntity = new HttpEntity<String>(jsonObject.toString(), requestHeaders);


            //add a user to the Binder
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            final String url = "https://apisandbox.moxtra.com/v1/" + PreferenceUtil.getTravelBinderId(AppController.context) + "/addteamuser?access_token=" + PreferenceUtil.getTravelTokenId(AppController.context);

            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            String result = responseEntity.getBody();

            /**
             * Gson Lib for serialize the object
             */
            Gson gson = new GsonBuilder().serializeNulls().create();
            gson = new GsonBuilder().serializeNulls().create();
            //Parse the user response
            addUserResponse = gson.fromJson(result, AddUserResponse.class);

            return addUserResponse;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return addUserResponse;
    }

    public void registerItraveller()
    {
        String email=""+mPrefs.getString("email","");
        String group_binder_id=PreferenceUtil.getGroupBinderId(AppController.context);
        String single_binder_id=PreferenceUtil.getSingleBinderId(AppController.context);
        String travel_binder_id=PreferenceUtil.getTravelBinderId(AppController.context);
        String gcm_reg_id=""+PreferenceUtil.getGcmRegId(AppController.context);

        String url="http://stage.itraveller.com/backend/api/v1/binder/";

        HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("email",""+email);
        postParams.put("group_binder_id",""+group_binder_id);
        postParams.put("single_binder_id",""+single_binder_id);
        postParams.put("travel_binder_id",""+travel_binder_id);
        postParams.put("gcm_reg_id",""+gcm_reg_id);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(postParams),
                new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response Update", response.toString());
                        try {
                            JSONObject jobj = new JSONObject(response.toString());
                            String success = jobj.getString("success");
                            //if user enters valid details
                            if (success.equals("true"))
                            {

                                //    Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                JSONObject errorobj = jobj.getJSONObject("error");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }})
        {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(8000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private String getRegistrationId(Context context) {
        String registrationId = PreferenceUtil.getGcmRegId(context);
        if (TextUtils.isEmpty(registrationId)) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = PreferenceUtil.getAppVersion(getActivity());
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    //Link to the moxtra account
    @Override
    public void onLinkAccountDone(boolean success) {
        if (success) {
            Log.i(TAG, "Linked to moxtra account successfully.");

        } else {
            Toast.makeText(getActivity(), "Failed to setup moxtra user.", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Failed to setup moxtra user.");
            //    showProgress(false);
        }
    }
}
