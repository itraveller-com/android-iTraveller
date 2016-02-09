package com.itraveller.activity;

/**
 * Created by VNK on 6/11/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.itraveller.R;
import com.itraveller.adapter.FlightViewPagerAdapter;
import com.itraveller.constant.Constants;
import com.itraveller.constant.CustomLoading;
import com.itraveller.model.FlightModel;
import com.itraveller.model.OnwardDomesticFlightModel;
import com.itraveller.model.OnwardFlightModel;
import com.itraveller.model.ReturnDomesticFlightModel;
import com.itraveller.volley.AppController;


public class FlightDomesticActivity extends ActionBarActivity{
    /* When using Appcombat support library
       you need to extend Main Activity to ActionBarActivity.*/
    public static Activity fda;

    public static Object volley_obj;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;


    int Total_Price;
    private Toolbar mToolbar; // Declaring the Toolbar Object
    private ViewPager pager;
    private FlightViewPagerAdapter adapter = null;
    public SlidingTabLayout tabs;
    private String[] Titles = { "Onward", "Return" };
    ArrayList<OnwardDomesticFlightModel> onward_domestic_model = new ArrayList<OnwardDomesticFlightModel>();
    ArrayList<ReturnDomesticFlightModel> return_domestic_model = new ArrayList<ReturnDomesticFlightModel>();
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flight_domestic);
        //String url ="http://stage.itraveller.com/backend/api/v1/domesticflight?travelFrom=BOM&arrivalPort=BLR&departDate=2015-08-05&returnDate=2015-08-08e&adults=2&children=0&infants=0&departurePort=BLR&travelTo=BOM";
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Flight");


        fda=this;
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

       /* mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        setupDrawer();*/


        SharedPreferences prefs = getSharedPreferences("Itinerary", Context.MODE_PRIVATE);
        prefs.edit().putString("OnwardFlightPrice","0").commit();
        prefs.edit().putString("ReturnFlightPrice", "0").commit();

        prefs.edit().putString("Flight_flag","0").commit();

        CustomLoading.LoadingScreen(FlightDomesticActivity.this, false);
        next = (Button) findViewById(R.id.button);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(FlightDomesticActivity.this, ItinerarySummaryActivity.class);
                SharedPreferences prefs=getSharedPreferences("Preferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor;
                editor = prefs.edit();
                editor.putInt("Skip_Flight_Bit", 0);
                editor.commit();
                startActivity(in);
        //        finish();
                //FlightReturnDomestic.adapter.
            }
        });


        //    String url ="http://stage.itraveller.com/backend/api/v1/domesticflight?" +
        String url= Constants.API_Domestic_Flights+
                "travelFrom=" + prefs.getString("ArrivalAirport", null) +
                "&arrivalPort=" + prefs.getString("TravelFrom", null) +
                "&departDate=" + prefs.getString("TravelDate", null) +
                "&returnDate=" + prefs.getString("EndDate", null) +
                "&adults=" + prefs.getString("Adult", "0") +
                "&children=" + prefs.getString("Child", "0") +
                "&infants=" + prefs.getString("Infants", "0") +
                "&departurePort=" + prefs.getString("TravelTo", null) +
                "&travelTo=" + prefs.getString("DepartureAirport", null);
        Log.i("Domestic URL",""+url);

        // Creating The Toolbar and setting it as the Toolbar for the activity
        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new FlightViewPagerAdapter(getSupportFragmentManager(),Titles,Titles.length,onward_domestic_model,return_domestic_model);
        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width
        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.white);
            }
        });


        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
        xmlparseflight(url);

    }

    /*private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            *//** Called when a drawer has settled in a completely open state. *//*
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Summary Data");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            *//** Called when a drawer has settled in a completely closed state. *//*
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle("Flights");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }


        };

        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }*/

    /*@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        int menuToUse = R.menu.right_side_menu;

        MenuInflater inflater = getMenuInflater();


        inflater.inflate(menuToUse, menu);

        return super.onCreateOptionsMenu(menu);
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        if (item != null && item.getItemId() == R.id.btnMyMenu) {
            if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
            } else {
                mDrawerLayout.openDrawer(Gravity.RIGHT);
            }
        }


        return false;
    }*/



    public void xmlparseflight(String url)
    {
        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("Boolean", "" + response.getBoolean("success"));
                    Log.d("Error", ""+response.getJSONObject("error"));
                    Log.d("Payload", ""+response.getJSONObject("payload").getString("onward"));

                    try {
                        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                        InputSource is = new InputSource();
                        is.setCharacterStream(new StringReader(""+response.getJSONObject("payload").getString("onward")));

                        Document doc = db.parse(is);
                        NodeList nodes = doc.getElementsByTagName("OriginDestinationOption");

                        for (int i = 0; i < nodes.getLength(); i++) {
                            Element element = (Element) nodes.item(i);
                            OnwardDomesticFlightModel monward = new OnwardDomesticFlightModel();

                            NodeList name_fl_1 = element.getElementsByTagName("ActualBaseFare");
                            monward.setActualBaseFare(""+getCharacterDataFromElement((Element) name_fl_1.item(0)));
                            NodeList name_fl_2 = element.getElementsByTagName("Tax");
                            monward.setTax("" + getCharacterDataFromElement((Element) name_fl_2.item(0)));
                            NodeList name_fl_3 = element.getElementsByTagName("STax");
                            monward.setSTax("" + getCharacterDataFromElement((Element) name_fl_3.item(0)));
                            NodeList name_fl_4 = element.getElementsByTagName("TCharge");
                            monward.setTCharge("" + getCharacterDataFromElement((Element) name_fl_4.item(0)));
                            NodeList name_fl_5 = element.getElementsByTagName("SCharge");
                            monward.setSCharge("" + getCharacterDataFromElement((Element) name_fl_5.item(0)));
                            NodeList name_fl_6 = element.getElementsByTagName("TDiscount");
                            monward.setTDiscount("" + getCharacterDataFromElement((Element) name_fl_6.item(0)));
                            NodeList name_fl_7 = element.getElementsByTagName("TMarkup");
                            monward.setTMarkup("" + getCharacterDataFromElement((Element) name_fl_7.item(0)));
                            NodeList name_fl_8 = element.getElementsByTagName("TPartnerCommission");
                            monward.setTPartnerCommission("" + getCharacterDataFromElement((Element) name_fl_8.item(0)));
                            NodeList name_fl_9 = element.getElementsByTagName("TSdiscount");
                            monward.setTSdiscount("" + getCharacterDataFromElement((Element) name_fl_9.item(0)));



                            Total_Price=Integer.parseInt(""+getCharacterDataFromElement((Element) name_fl_1.item(0)))+
                                    Integer.parseInt("" + getCharacterDataFromElement((Element) name_fl_2.item(0)))+
                                    Integer.parseInt("" + getCharacterDataFromElement((Element) name_fl_3.item(0)))+
                                    Integer.parseInt("" + getCharacterDataFromElement((Element) name_fl_4.item(0)))+
                                    Integer.parseInt("" + getCharacterDataFromElement((Element) name_fl_5.item(0)))+
                                    Integer.parseInt("" + getCharacterDataFromElement((Element) name_fl_6.item(0)))+
                                    Integer.parseInt("" + getCharacterDataFromElement((Element) name_fl_7.item(0)))+
                                    Integer.parseInt("" + getCharacterDataFromElement((Element) name_fl_8.item(0)))+
                                    Integer.parseInt("" + getCharacterDataFromElement((Element) name_fl_9.item(0)));

                            //NodeList name_fl_10 = element.getElementsByTagName("ocTax");
                            //mflight.setOcTax("" + getCharacterDataFromElement((Element) name_fl_10.item(0)));

                                /*Document doc1 = db.parse(is);
                                NodeList nodes1 = doc1.getElementsByTagName("FlightSegment");
                                Element element1 = (Element) nodes1.item(i);*/

                            NodeList name = element.getElementsByTagName("AirEquipType");
                            monward.setAirEquipType("" + getCharacterDataFromElement((Element) name.item(0)));
                            NodeList name1 = element.getElementsByTagName("ArrivalAirportCode");
                            monward.setArrivalAirportCode("" + getCharacterDataFromElement((Element) name1.item(0)));
                            NodeList name2 = element.getElementsByTagName("airLineName");
                            monward.setOperatingAirlineName("" + getCharacterDataFromElement((Element) name2.item(0)));
                            NodeList name3 = element.getElementsByTagName("ArrivalDateTime");
                            monward.setArrivalDateTime("" + getCharacterDataFromElement((Element) name3.item(0)));
                            NodeList name4 = element.getElementsByTagName("DepartureAirportCode");
                            monward.setDepartureAirportCode("" + getCharacterDataFromElement((Element) name4.item(0)));
                                /*NodeList name5 = element.getElementsByTagName("DepartureAirportName");
                                monward.setDepartureAirportName(""+getCharacterDataFromElement((Element) name5.item(0)));*/
                            NodeList name6 = element.getElementsByTagName("DepartureDateTime");
                            monward.setDepartureDateTime("" + getCharacterDataFromElement((Element) name6.item(0)));
                            NodeList name7 = element.getElementsByTagName("FlightNumber");
                            monward.setFlightNumber(""+getCharacterDataFromElement((Element) name7.item(0)));

                            monward.setTotal_Price(Total_Price);
                                /*NodeList name8 = element.getElementsByTagName("MarketingAirlineCode");
                                monward.setMarketingAirlineCode(""+getCharacterDataFromElement((Element) name8.item(0)));
                                NodeList name9 = element.getElementsByTagName("OperatingAirlineCode");
                                monward.setOperatingAirlineCode(""+getCharacterDataFromElement((Element) name9.item(0)));
                                NodeList name10 = element.getElementsByTagName("OperatingAirlineName");
                                monward.setOperatingAirlineName(""+getCharacterDataFromElement((Element) name10.item(0)));
                                NodeList name11 = element.getElementsByTagName("OperatingAirlineFlightNumber");
                                monward.setOperatingAirlineFlightNumber(""+getCharacterDataFromElement((Element) name11.item(0)));
                                NodeList name12 = element.getElementsByTagName("NumStops");
                                monward.setNumStops(""+getCharacterDataFromElement((Element) name12.item(0)));
                                NodeList name13 = element.getElementsByTagName("LinkSellAgrmnt");
                                monward.setLinkSellAgrmnt(""+getCharacterDataFromElement((Element) name13.item(0)));*/

                            onward_domestic_model.add(monward);

                            Collections.sort(onward_domestic_model,new PriceComparison());
                        }


                        DocumentBuilder db1 = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                        InputSource is1 = new InputSource();
                        is1.setCharacterStream(new StringReader(""+response.getJSONObject("payload").getString("return")));

                        Document doc1 = db1.parse(is1);
                        NodeList nodes1 = doc1.getElementsByTagName("OriginDestinationOption");

                        for (int i = 0; i < nodes1.getLength(); i++) {
                            Element element = (Element) nodes1.item(i);
                            ReturnDomesticFlightModel monward = new ReturnDomesticFlightModel();

                            NodeList name_fl_1 = element.getElementsByTagName("ActualBaseFare");
                            monward.setActualBaseFare(""+getCharacterDataFromElement((Element) name_fl_1.item(0)));
                            NodeList name_fl_2 = element.getElementsByTagName("Tax");
                            monward.setTax("" + getCharacterDataFromElement((Element) name_fl_2.item(0)));
                            NodeList name_fl_3 = element.getElementsByTagName("STax");
                            monward.setSTax("" + getCharacterDataFromElement((Element) name_fl_3.item(0)));
                            NodeList name_fl_4 = element.getElementsByTagName("TCharge");
                            monward.setTCharge("" + getCharacterDataFromElement((Element) name_fl_4.item(0)));
                            NodeList name_fl_5 = element.getElementsByTagName("SCharge");
                            monward.setSCharge("" + getCharacterDataFromElement((Element) name_fl_5.item(0)));
                            NodeList name_fl_6 = element.getElementsByTagName("TDiscount");
                            monward.setTDiscount("" + getCharacterDataFromElement((Element) name_fl_6.item(0)));
                            NodeList name_fl_7 = element.getElementsByTagName("TMarkup");
                            monward.setTMarkup("" + getCharacterDataFromElement((Element) name_fl_7.item(0)));
                            NodeList name_fl_8 = element.getElementsByTagName("TPartnerCommission");
                            monward.setTPartnerCommission("" + getCharacterDataFromElement((Element) name_fl_8.item(0)));
                            NodeList name_fl_9 = element.getElementsByTagName("TSdiscount");
                            monward.setTSdiscount("" + getCharacterDataFromElement((Element) name_fl_9.item(0)));


                            Total_Price=Integer.parseInt(""+getCharacterDataFromElement((Element) name_fl_1.item(0)))+
                                    Integer.parseInt("" + getCharacterDataFromElement((Element) name_fl_2.item(0)))+
                                    Integer.parseInt("" + getCharacterDataFromElement((Element) name_fl_3.item(0)))+
                                    Integer.parseInt("" + getCharacterDataFromElement((Element) name_fl_4.item(0)))+
                                    Integer.parseInt("" + getCharacterDataFromElement((Element) name_fl_5.item(0)))+
                                    Integer.parseInt("" + getCharacterDataFromElement((Element) name_fl_6.item(0)))+
                                    Integer.parseInt("" + getCharacterDataFromElement((Element) name_fl_7.item(0)))+
                                    Integer.parseInt("" + getCharacterDataFromElement((Element) name_fl_8.item(0)))+
                                    Integer.parseInt("" + getCharacterDataFromElement((Element) name_fl_9.item(0)));


                            //NodeList name_fl_10 = element.getElementsByTagName("ocTax");
                            //mflight.setOcTax("" + getCharacterDataFromElement((Element) name_fl_10.item(0)));

                                /*Document doc1 = db.parse(is);
                                NodeList nodes1 = doc1.getElementsByTagName("FlightSegment");
                                Element element1 = (Element) nodes1.item(i);*/

                            NodeList name = element.getElementsByTagName("AirEquipType");
                            monward.setAirEquipType("" + getCharacterDataFromElement((Element) name.item(0)));
                            NodeList name1 = element.getElementsByTagName("ArrivalAirportCode");
                            monward.setArrivalAirportCode("" + getCharacterDataFromElement((Element) name1.item(0)));
                            NodeList name2 = element.getElementsByTagName("airLineName");
                            monward.setOperatingAirlineName("" + getCharacterDataFromElement((Element) name2.item(0)));
                            NodeList name3 = element.getElementsByTagName("ArrivalDateTime");
                            monward.setArrivalDateTime("" + getCharacterDataFromElement((Element) name3.item(0)));
                            NodeList name4 = element.getElementsByTagName("DepartureAirportCode");
                            monward.setDepartureAirportCode("" + getCharacterDataFromElement((Element) name4.item(0)));
                               /* NodeList name5 = element.getElementsByTagName("DepartureAirportName");
                                monward.setDepartureAirportName(""+getCharacterDataFromElement((Element) name5.item(0)));*/
                            NodeList name6 = element.getElementsByTagName("DepartureDateTime");
                            monward.setDepartureDateTime("" + getCharacterDataFromElement((Element) name6.item(0)));
                            NodeList name7 = element.getElementsByTagName("FlightNumber");
                            monward.setFlightNumber("" + getCharacterDataFromElement((Element) name7.item(0)));

                            monward.setTotal_Price(Total_Price);
                               /* NodeList name8 = element.getElementsByTagName("MarketingAirlineCode");
                                monward.setMarketingAirlineCode(""+getCharacterDataFromElement((Element) name8.item(0)));
                                NodeList name9 = element.getElementsByTagName("OperatingAirlineCode");
                                monward.setOperatingAirlineCode(""+getCharacterDataFromElement((Element) name9.item(0)));
                                NodeList name10 = element.getElementsByTagName("OperatingAirlineName");
                                monward.setOperatingAirlineName(""+getCharacterDataFromElement((Element) name10.item(0)));
                                NodeList name11 = element.getElementsByTagName("OperatingAirlineFlightNumber");
                                monward.setOperatingAirlineFlightNumber(""+getCharacterDataFromElement((Element) name11.item(0)));
                                NodeList name12 = element.getElementsByTagName("NumStops");
                                monward.setNumStops(""+getCharacterDataFromElement((Element) name12.item(0)));
                                NodeList name13 = element.getElementsByTagName("LinkSellAgrmnt");
                                monward.setLinkSellAgrmnt(""+getCharacterDataFromElement((Element) name13.item(0)));*/

                            return_domestic_model.add(monward);

                            Collections.sort(return_domestic_model,new PriceComparisonReturn());
                        }

                    }
                    catch (Exception e){

                    }
                    FlightOnwardDomestic.adapter.notifyDataSetChanged();

                    FlightReturnDomestic.adapter.notifyDataSetChanged();
                    CustomLoading.LoadingHide();
                } catch (JSONException e) {
                    Log.d("Error Catched","" +e.getMessage());
                }


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


        volley_obj=strReq;
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);

    }

    public static String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "";
    }


    class PriceComparison implements Comparator<OnwardDomesticFlightModel> {

        @Override
        public int compare(OnwardDomesticFlightModel o1,OnwardDomesticFlightModel o2) {
            if(o1.getTotal_Price() > o2.getTotal_Price()){
                return 1;
            } else {
                return -1;
            }
        }

    }

    class PriceComparisonReturn implements Comparator<ReturnDomesticFlightModel> {

        @Override
        public int compare(ReturnDomesticFlightModel o1,ReturnDomesticFlightModel o2) {
            if(o1.getTotal_Price() > o2.getTotal_Price()){
                return 1;
            } else {
                return -1;
            }
        }

    }



    public void onBackPressed() {
        finish();
    }
}

