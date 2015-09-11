package com.itraveller.activity;

/**
 * Created by VNK on 6/25/2015.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.facebook.internal.Utility;
import com.itraveller.R;
import com.itraveller.adapter.HotelRoomAdapter;
import com.itraveller.adapter.ListViewPagerActivitiesAdapter;
import com.itraveller.adapter.ListViewPagerAdapter;
import com.itraveller.adapter.ViewPagerAdapter;
import com.itraveller.constant.Constants;
import com.itraveller.model.ActivitiesModel;
import com.itraveller.model.HotelModel;
import com.itraveller.model.HotelRoomModel;
import com.itraveller.volley.AppController;

public class  HotelActivity extends ActionBarActivity {

    // Declare Variable
    public static ListViewPagerAdapter listViewPagerAdapter;
    int ListItemPostion;
    int heightHotelList = 5;
    Toolbar mToolbar;
    private ArrayList<String> hotelList;
    private ArrayList<String> lowesthotelList;
    String[] hotel_destination, destination_date;
    ListView lv1;
    LinearLayout second;
    ///HOTEL ROOM ACTIVITY
    int[] value = new int[10];
    private ListView listView;
    private List<HotelRoomModel> roomList ;
    private HotelRoomAdapter adapter;
    int check_bit=0;
    String[] HotelRoomData;
    int cposition, gposition;
    String lowest_hotel_url = Constants.API_HotelActivity_Lowest_Hotel; //"http://stage.itraveller.com/backend/api/v1/lowesthotel?destinationId=";
    String Region_ID;
    Button activites;
    ProgressDialog pDialog;
    CheckBox chk_breakfast,chk_lunch,chk_dinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from viewpager_main.xml
        setContentView(R.layout.view_pager_list_view);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle("Hotels");

        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        chk_breakfast = (CheckBox) findViewById(R.id.breakfast);
        chk_lunch = (CheckBox) findViewById(R.id.lunch);
        chk_dinner = (CheckBox) findViewById(R.id.dinner);
        pDialog = new ProgressDialog(HotelActivity.this);
        pDialog.setMessage("Loading...");

        Bundle bundle = getIntent().getExtras();
        String IDS_value = bundle.getString("DestinationsIDs");
        SharedPreferences prfs = getSharedPreferences("Itinerary", Context.MODE_PRIVATE);
        String Date_value = prfs.getString("DestinationDate", "");
        Region_ID= prfs.getString("RegionID", "");

        //Destination Count



        hotel_destination = IDS_value.trim().split(",");
        destination_date = Date_value.trim().split(",");
        HotelRoomData = new String[hotel_destination.length];
        second = (LinearLayout) findViewById(R.id.room_type_full);
        lv1 = (ListView) findViewById(R.id.campaignListView);
        // lv1.setAdapter(new ArrayAdapter<String>(getActivity(),
        // android.R.layout.simple_list_item_1,aList));
        SharedPreferences sharedpreferences = getSharedPreferences("Itinerary", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();
        setData();
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.i("TestDATA", "Testing" + position);
            }
        });

        activites = (Button) findViewById(R.id.to_activities);
        activites.setEnabled(false);
        activites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<String> set = new HashSet<String>();
                String hotel_string_main = "";
                String itinerary_hotel = "";
              /*  for(int i =0; i< HotelRoomData.length;i++)
                {
                    Log.i("Hotel Room "+i,""+ HotelRoomData[i]);
                    set.add("" + HotelRoomData[i]);
                }*/
                //HashMap<String,ArrayList<ActivitiesModel>> mActivitiesModel = new HashMap<String, ArrayList<ActivitiesModel>>();



                for(int i =0; i< lowesthotelList.size();i++)
                {
                    Log.d("Destination day 3",""+i);
                    String hotel_string = "";
                    String datas = ""+ HotelRoomData[i];
                    Log.i("lowestHotelData1 " + i, "" + lowesthotelList.get(i));
                    Log.i("HotelData1 " + i, "" + HotelRoomData[i]);
                    String[] hotel_room_Data = lowesthotelList.get(i).trim().split(",");

                    for(int j = 0 ;j < lowesthotelList.size(); j++) {
                        ArrayList<HotelModel> modelRow = ListViewPagerAdapter.mHotelModels.get("" + j);
                        for(int k = 0;k< modelRow.size();k++) {
                            if (modelRow.get(k).getHotel_Id() == Integer.parseInt(hotel_room_Data[0])) {
                               //if(k == 0)
                                 hotel_string = ""+ modelRow.get(k).getHotel_Name() + "," +  modelRow.get(k).getHotel_Description() + "," +  modelRow.get(k).getHotel_Id();
                               /* else
                                   hotel_string = ""+hotel_string + "-" + modelRow.get(k).getHotel_Name() + "," +  modelRow.get(k).getHotel_Description() + "," +  modelRow.get(k).getHotel_Id();*/
                            }
                        }
                    }

                    //if(datas.equalsIgnoreCase("null")) {
                    set.add("" + lowesthotelList.get(i));
                    if(i == 0) {
                        hotel_string_main = "" + hotel_string;
                        itinerary_hotel = "" + lowesthotelList.get(i);
                    }
                    else{
                        hotel_string_main = hotel_string_main + "-" + hotel_string;
                        itinerary_hotel = itinerary_hotel + "-" + lowesthotelList.get(i);
                    }

                }

                    /*}
                    else{
                    set.add("" + HotelRoomData[i]);}*/
                    //Log.i("Hotel Room 123" + i, "" + set.toArray()[i]);
                editor.putString("Hotels", hotel_string_main);
                editor.putString("HotelRooms", itinerary_hotel);
                editor.putString("ItineraryHotelRooms", itinerary_hotel);

                editor.commit();

                Intent intent = new Intent(HotelActivity.this, ActivitiesActivity.class);
                startActivity(intent);
            }
        });

        //String url_checkroom = "http://stage.itraveller.com/backend/api/v1/roomtariff?region=7&room=52&checkInDate=2015-07-26";
        //url = "http://stage.itraveller.com/backend/api/v1/internationalflight?travelFrom=BOM&arrivalPort=MRU&departDate=2015-07-26&returnDate=2015-08-01&adults=2&children=0&infants=0&departurePort=MRU&travelTo=BOM";
        //hotelRoomsCheck(url_checkroom);



    }

    public interface RadiobuttonListener {
        public void RadioChangeListenerCustom(String position);
    }

    public interface pagerCheckBoxChangedListner1 {
        public void OnCheckedChangeListenerCustomPager(int childPosition, boolean isChecked);

        public void OnImageClickListenerCustomPager(int childpostion, int grouppostion);
    }

    private void setData() {
        hotelList = new ArrayList<>();
        lowesthotelList = new ArrayList<>();
        for (int index = 0; index < hotel_destination.length; index++) {
        //    hotelList.add("http://stage.itraveller.com/backend/api/v1/hotel/destintionId/" + hotel_destination[index]);
            hotelList.add(Constants.API_HotelActivity_HotelList + hotel_destination[index]);
            //Default Hotel Set Value
            DefaultHotelRoomSet(lowest_hotel_url + hotel_destination[index] + "&checkInDate=" + destination_date[index] + "&regionId=" + Region_ID, index);
            Log.i("HotelURL", "" + "http://stage.itraveller.com/backend/api/v1/hotel/destintionId/" + hotel_destination[index]);
        }

    }


    public void hotelRooms(String url, final String checkinDate) {

        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("Boolean", "" + response.getBoolean("success"));
                    Log.d("Error", "" + response.getJSONObject("error"));
                    Log.d("Payload", "" + response.getJSONObject("payload"));

                    // JSONObject jsonobj = response.getJSONObject("payload").getJSONObject()
                    // Parsing json
                    for (int i = 0; i < response.getJSONObject("payload").length(); i++) {
                        Iterator<?> destinationKeys = response.getJSONObject("payload").keys();

                        while (destinationKeys.hasNext()) {
                            String destinationKey = (String) destinationKeys.next();
                            JSONArray RoomObj = response.getJSONObject("payload").getJSONArray(destinationKey);
                            // destinationValue = destobj.getString("name");
                            Log.d("Room_Type", "" + RoomObj.length());
                            for (int inc = 0; inc < RoomObj.length(); inc++) {
                                Log.d("Room_Type", "Test" + RoomObj.getJSONObject(inc).getString("Hotel_Room_Id"));
                                value[inc] = RoomObj.getJSONObject(inc).getInt("Hotel_Room_Id");

                            //    String url_checkroom = "http://stage.itraveller.com/backend/api/v1/roomtariff?region="+ Region_ID +"&room=" + value[inc] + "&checkInDate=" + checkinDate;
                                String url_checkroom=Constants.API_HotelActivity_Checkroom+ Region_ID +"&room=" + value[inc] + "&checkInDate=" + checkinDate;
                                hotelRoomsCheck(url_checkroom,RoomObj.length(), inc);
                            }
                        }

                        /// /JSONObject jsonarr1 =
                        //Log.d("Room_Type", "" + jsonarr.);
                    }

                } catch (JSONException e) {
                    Log.d("Error Catched", "" + e.getMessage());
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

                    Toast.makeText(HotelActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                } else if( error instanceof ServerError) {
                } else if( error instanceof AuthFailureError) {
                } else if( error instanceof ParseError) {
                } else if( error instanceof NoConnectionError) {

                    Toast.makeText(HotelActivity.this, "No Internet Connection" ,Toast.LENGTH_LONG).show();
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


    public void hotelRoomsCheck(String url, final int totalcount, final int index) {
        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("Boolean", "" + response.getBoolean("success"));
                    Log.d("Error", "" + response.getJSONObject("error"));
                    Log.d("Payload_RoomRate", "" + response.getJSONArray("payload"));
                    int flag_bit = 0;
                    for (int i = 0; i < response.getJSONArray("payload").length(); i++) {
                        JSONObject jsonarr = response.getJSONArray("payload").getJSONObject(i);
                        HotelRoomModel hrm = new HotelRoomModel();
                        if(flag_bit == 0){
                        for(int index = 0;index < lowesthotelList.size();index++) {
                            hrm.setHotel_Room_Id(jsonarr.getInt("Hotel_Room_Id"));
                            hrm.setHotel_Id(jsonarr.getInt("Hotel_Id"));
                            hrm.setRoom_Status(jsonarr.getInt("Room_Status"));
                            hrm.setRack_Rate(jsonarr.getInt("Rack_Rate"));
                            hrm.setDefault_Number(jsonarr.getInt("Default_Number"));
                            hrm.setMaximum_Number(jsonarr.getInt("Maximum_Number"));
                            hrm.setHotel_Room_Tariff_Id(jsonarr.getInt("Hotel_Room_Tariff_Id"));
                            hrm.setTAC(jsonarr.getInt("TAC"));
                            hrm.setCost(jsonarr.getInt("Cost"));
                            hrm.setMark_Up(jsonarr.getInt("Mark_Up"));
                            hrm.setDisplay_Tariff(jsonarr.getInt("Display_Tariff"));
                            hrm.setCompany_Id(jsonarr.getInt("Company_Id"));
                            hrm.setRoom_Type(jsonarr.getString("Room_Type"));
                            hrm.setRoom_Description(jsonarr.getString("Room_Description"));
                            hrm.setFrom(jsonarr.getString("From"));
                            hrm.setTo(jsonarr.getString("To"));
                            String[] value = lowesthotelList.get(index).trim().split(",");
                            if (Integer.parseInt("" + value[0]) == jsonarr.getInt("Hotel_Id")) {
                                if (Integer.parseInt("" + value[1]) == jsonarr.getInt("Hotel_Room_Id")) {
                                    hrm.setCheck(true);
                                    flag_bit = 1;
                                }
                                else{
                                    //hrm.setCheck(false);
                                }
                            }
                        }
                        }
                        roomList.add(hrm);
                        flag_bit =0;
                    }
                    if(index == (totalcount-1)) {
                        adapter.notifyDataSetChanged();
                        //com.itraveller.constant.Utility.setListViewHeightBasedOnChildren(listView);}
                    }

                } catch (JSONException e) {
                    Log.d("Error Catched", "" + e.getMessage());
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.d("Volley Error", "Error: " + error.networkResponse);

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }

    public void DefaultHotelRoomSet(String url, final int depth) {
        Log.d("LowestHotelURL", "" + url);

        if(depth==0){
        pDialog.show();}
        else{
            pDialog.hide();
        }
        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("Boolean", "" + response.getBoolean("success"));
                    Log.d("Error", "" + response.getJSONObject("error"));
                    Log.d("Payload_RoomRate", "" + response.getJSONObject("payload"));

                    //for (int i = 0; i < response.getJSONObject("payload").length(); i++) {

                        JSONObject jsonarr = response.getJSONObject("payload");
                        Log.i("DefaultData",""+ jsonarr.getString("Hotel_Id") + "," + jsonarr.getString("Hotel_Room_Id") + "," + jsonarr.getString("Display_Tariff") +",1");
                    //here no of rooms add defaul
                    lowesthotelList.add(jsonarr.getString("Hotel_Id") + "," + jsonarr.getString("Hotel_Room_Id") + "," + jsonarr.getString("Display_Tariff") +",1" );
                        //roomList.add();
                    //}
                   // if(depth==(hotel_destination.length-1))
                   {
                        listViewPagerAdapter = new ListViewPagerAdapter(HotelActivity.this, hotelList, lowesthotelList, new pagerCheckBoxChangedListner1() {
                            @Override
                            public void OnCheckedChangeListenerCustomPager(int childPosition, boolean isChecked) {
                            }

                            @Override
                            public void OnImageClickListenerCustomPager(final int childpostion, final int groupPosition) {
                                cposition = childpostion;
                                gposition = groupPosition;
                                second.setVisibility(View.VISIBLE);
                                activites.setVisibility(View.GONE);
                                lv1.setVisibility(View.GONE);
                                roomList = new ArrayList<HotelRoomModel>();
                                listView = (ListView) findViewById(R.id.room_type);
                                listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                                chk_breakfast.setChecked(true);
                                chk_lunch.setChecked(false);
                                chk_dinner.setChecked(false);
                                final ArrayList<HotelModel> modelRow = ListViewPagerAdapter.mHotelModels.get("" + groupPosition);
                                /*String meal_plan = modelRow.get(childpostion).getHotel_Meal_Plan();
                                String[] meal_plan_data = meal_plan.split(",");
                                if()
                                            if(modelRow.get(childpostion).getLunch()!=0)
                                    {
                                        chk_lunch.setChecked(true);
                                    }
                                    if(modelRow.get(childpostion).getDinner()!=0)
                                    {
                                        chk_dinner.setChecked(true);
                                    }
                                    chk_lunch.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if(chk_lunch.isChecked())
                                                modelRow.get(childpostion).setLunch(1);
                                            else
                                                modelRow.get(childpostion).setLunch(0);
                                        }
                                });
                                chk_dinner.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if(chk_dinner.isChecked())
                                            modelRow.get(childpostion).setDinner(1);
                                        else
                                            modelRow.get(childpostion).setDinner(0);
                                    }
                                });*/
                                adapter = new HotelRoomAdapter(HotelActivity.this, roomList, new RadiobuttonListener() {
                                    @Override
                                    public void RadioChangeListenerCustom(String Value) {


                                        Log.i("TestPostion",""+ Value);
                                        Log.i("TestPostionGroup",""+groupPosition);
                                        lowesthotelList.set(groupPosition,""+Value);
                                        HotelRoomData[gposition] = Value;
                                        final ArrayList<HotelModel> modelRow = ListViewPagerAdapter.mHotelModels.get("" + gposition);
                                        for(int index =0 ; index<modelRow.size();index++) {
                                            if(index==cposition) {
                                                modelRow.get(cposition).setChecked(true);
                                            }
                                            else {
                                                modelRow.get(index).setChecked(false);
                                            }
                                        }
                                        ListViewPagerAdapter.mViewPagerAdapter.notifyDataSetChanged();
                                        listViewPagerAdapter.notifyDataSetChanged();
                                        //notifyAll();
                                    }


                                });
                                listView.setAdapter(adapter);
                                //ArrayList<HotelModel> modelRow = ListViewPagerAdapter.mHotelModels.get("" + groupPosition);
                                //modelRow.get(childpostion).
                                Log.i("PagerView Clicked", groupPosition + "Clicked" + childpostion + " Check " + modelRow.get(childpostion).getHotel_Name());
                            //    String url = "http://stage.itraveller.com/backend/api/v1/hotelRoom?regionId="+Region_ID+"&hotelIds=["+ modelRow.get(childpostion).getHotel_Id() +"]&checkInDate=" + destination_date[groupPosition];
                                String url=Constants.API_HotelActivity_HOTEL_ROOMS+Region_ID+"&hotelIds=["+ modelRow.get(childpostion).getHotel_Id() +"]&checkInDate=" + destination_date[groupPosition];
                                Log.i("URLForRooms", "" + groupPosition + " Url :" +url);
                                hotelRooms(url,destination_date[groupPosition] );
                                check_bit=1;
                            }
                        });

                        pDialog.hide();
                        activites.setEnabled(true);
                        // listViewPagerAdapter.add(null);
                        lv1.setAdapter(listViewPagerAdapter);

                    }
                } catch (JSONException e) {
                    Log.d("Error Catched", "" + e.getMessage());
                }
                catch (Exception e){
                    Log.d("Error Catched", "" + e.getMessage());
                }

            }
        },  new Response.ErrorListener() {

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
                    Toast.makeText(HotelActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                } else if( error instanceof ServerError) {
                    //Toast.makeText(HotelActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                } else if( error instanceof AuthFailureError) {
                } else if( error instanceof ParseError) {
                } else if( error instanceof NoConnectionError) {
                    pDialog.hide();
                    Toast.makeText(HotelActivity.this, "No Internet Connection" ,Toast.LENGTH_LONG).show();
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

    public void onBackPressed() {

        if(check_bit==0)
        {
            finish();
        }
        else
        {
           lv1.setVisibility(View.VISIBLE);
            second.setVisibility(View.GONE);
            activites.setVisibility(View.VISIBLE);
            check_bit=0;
        }
    }

}
