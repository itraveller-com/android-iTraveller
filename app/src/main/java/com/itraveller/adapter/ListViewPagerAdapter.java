package com.itraveller.adapter;

/**
 * Created by VNK on 6/25/2015.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.itraveller.R;
import com.itraveller.activity.HotelActivity;
import com.itraveller.model.ActivitiesModel;
import com.itraveller.model.HotelModel;
import com.itraveller.volley.AppController;


public class ListViewPagerAdapter extends ArrayAdapter<String> {
    ViewPager[] vp;
    public static ViewPagerAdapter mViewPagerAdapter;
    int swap_value = 0;
    ImageView left_arrow,right_arrow;
    int single_loop_bit =0;
   //ViewPagerAdapter viewpageradapter;

  HotelActivity.pagerCheckBoxChangedListner1 ListviewChangedListener;

    private Context context;
    private ArrayList<String> navigationItems;
    private ArrayList<String> defaultHotelRoom;
    public static HashMap<String,ArrayList<HotelModel>> mHotelModels;
  //  private int selectedIndex;
    private Map<Integer, Integer> mPagerPositions ;
    String[] hotel_destination;

    public ListViewPagerAdapter(Context context, ArrayList<String> navigationItems, ArrayList<String> defaultHotelRoom, HotelActivity.pagerCheckBoxChangedListner1 pagerviewlistener) {
        super(context, R.layout.view_pager_list_view, navigationItems);
        this.context = context;
        this.navigationItems = navigationItems;
        this.defaultHotelRoom = defaultHotelRoom;
        this.ListviewChangedListener = pagerviewlistener;
        mHotelModels=new HashMap<>();
        for(int index=0;index<navigationItems.size();index++){
            mHotelModels.put(""+index,new ArrayList<HotelModel>());
        }

        mPagerPositions= new HashMap<Integer, Integer>();
     //mViewPagerAdapter=new ViewPagerAdapter[navigationItems.size()];
       vp=new ViewPager[navigationItems.size()];
    }

    @Override
    public int getCount() {
        return navigationItems.size();
    }

//    public void setSelectedIndex(int position) {
//        selectedIndex = position;
//       // notifyDataSetChanged();
//    }

    @Override
    public String getItem(int position) {

        return navigationItems.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView( final int position, View convertView, ViewGroup parent) {
       // ViewPager vp;

        if (convertView == null) {
          // setSelectedIndex(position);
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.hotel_pageviewer, null);

           // mPagerPositions.put(position,0);

        }else{

        }
        SharedPreferences mysettings= context.getSharedPreferences("Itinerary", 0);
        String Destintion_NAME = mysettings.getString("DestinationName", null);
        hotel_destination = Destintion_NAME.trim().split(",");
        TextView txtview = (TextView) convertView.findViewById(R.id.hotel_place_name);
        txtview.setText("" + hotel_destination[position]);
        vp[position] = (ViewPager) convertView.findViewById(R.id.list_pager);
        mViewPagerAdapter = new ViewPagerAdapter(mHotelModels.get(""+position),new PagerCheckedChangeListnerCustom(position));

        vp[position].setAdapter(mViewPagerAdapter);


        left_arrow=(ImageView) convertView.findViewById(R.id.left_arrow);   //code added by rohan
        right_arrow=(ImageView) convertView.findViewById(R.id.right_arrow);

        //vp[position].setTag(position);
        vp[position].setOnClickListener(new ViewPagerClickListner(position));
        vp[position].setOnPageChangeListener(new ViewPageChangeListner(position));
        if(mPagerPositions.get(position)!=null){
          //  Log.e("Pager position ", "parent " + position + "child position " + mPagerPositions.get(position));
             vp[position].setCurrentItem(mPagerPositions.get(position));
        }
     /*   vp[position].setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int childposition) {
                      Log.d("PAGER ", "PAGER SCROLL PARENT POSITION " + childposition + "parent position " + position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/

        left_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Log.d("Left arrow","hi");
                vp[position].setCurrentItem(vp[position].getCurrentItem() - 1);
            }
        });

        right_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Right arrow","bye");
                vp[position].setCurrentItem(vp[position].getCurrentItem() + 1);
            }
        });


        if(mHotelModels.get(""+position).size()<1){

         airportJSONForText(navigationItems.get(position), position);
            Log.i("TestingRound","Testing123");
           /* ArrayList<HotelModel> modelRow=mHotelModels.get("" + position);
            if(modelRow.size() == 0){
                vp[position].setVisibility(View.GONE);
            }
            else{
                vp[position].setVisibility(View.VISIBLE);
            }*/





//         vp[position].setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//             @Override
//             public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//             }
//
//             @Override
//             public void onPageSelected(int positionChild) {
//                // mPagerPositions.put(position, positionChild);
//             }
//
//             @Override
//             public void onPageScrollStateChanged(int state) {
//
//             }
//         });

     }else{
        // mViewPagerAdapter[position] = new ViewPagerAdapter(mHotelModels.get(""+position));
        // vp[position].setAdapter(mViewPagerAdapter[position]);
        // for(int index=0;index<navigationItems.size();index++) {
          //   if(mViewPagerAdapter[index]!=null) {
                //mViewPagerAdapter[position].notifyDataSetChanged();
                // vp[position].setAdapter(mViewPagerAdapter[position]);
            // }
//             if (mPagerPositions.get(position) != null)
//                 vp[position].setCurrentItem(mPagerPositions.get(position));
         //}
     }

        //Log.i("PageSelection" , "PageSelection" + position);

       // vp[position].setTag(position);


       // if(mViewPagerAdapter[position]==null)

//        Integer pagerPosition = mPagerPositions.get(position);
//        if (pagerPosition != null) {
//            vp.setCurrentItem(pagerPosition);
//        }
//        Log.i("PagerPosition",""+pagerPosition);
        /*//Integer pagerPosition = selectedIndex;

        if (pagerPosition != null) {
            vp.setCurrentItem(pagerPosition);
        }*/
        //convertView.setTag(position);
        return convertView;
    }

//    private class MyPageChangeListener extends
//            ViewPager.SimpleOnPageChangeListener {
//
//        private int currentPage;
//
//        @Override
//        public void onPageSelected(int position) {
//            //currentPage = position;
//            if (vp.isShown()) {
//               // Log.i("PageSelection", "CurrentPage" + position + "Index" + selectedIndex);
//                mPagerPositions.put(selectedIndex, position);
//            }
//        }

//        public final int getCurrentPage() {
//            return currentPage;
//        }
//    }

    public void airportJSONForText (String url, final int position )
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

                    if(response.getJSONArray("payload").length()==0){
                        single_loop_bit =1;
                    }else{
                        single_loop_bit =0;
                    }
                    // JSONObject jsonobj = response.getJSONObject("payload").get;
                    // Parsing json
                    int flag_bit = 0;
                    ArrayList hotelList=new ArrayList();
                    for (int i = 0; i < response.getJSONArray("payload").length(); i++) {
                        JSONObject jsonarr = response.getJSONArray("payload").getJSONObject(i);

                        HotelModel hotel_model = new HotelModel();
                        for(int index = 0;index < defaultHotelRoom.size();index++) {
                            if(flag_bit  == 0) {
                            hotel_model.setHotel_Id(jsonarr.getInt("Hotel_Id"));
                            hotel_model.setRegion_Id(jsonarr.getString("Region_Id"));
                            hotel_model.setDestination_Id(jsonarr.getString("Destination_Id"));
                            hotel_model.setHotel_Name(jsonarr.getString("Hotel_Name"));
                            hotel_model.setHotel_Email(jsonarr.getString("Hotel_Email"));
                            hotel_model.setHotel_Description(jsonarr.getString("Hotel_Description"));
                            hotel_model.setHotel_Tripadvisor(jsonarr.getString("Hotel_Tripadvisor"));
                            hotel_model.setHotel_Meal_Plan(jsonarr.getString("Hotel_Meal_Plan"));
                            hotel_model.setHotel_Image(jsonarr.getString("Hotel_Image"));
                            hotel_model.setHotel_Status(jsonarr.getInt("Hotel_Status"));
                            hotel_model.setHotel_Star_Rating(jsonarr.getString("Hotel_Star_Rating"));
                            hotel_model.setHotel_Address(jsonarr.getString("Hotel_Address"));
                            hotel_model.setHotel_Latitude(jsonarr.getString("Hotel_Latitude"));
                            hotel_model.setHotel_Longitude(jsonarr.getString("Hotel_Longitude"));
                            hotel_model.setHotel_URL(jsonarr.getString("Hotel_URL"));
                            hotel_model.setHotel_Number(jsonarr.getString("Hotel_Number"));
                            hotel_model.setDistrict(jsonarr.getString("District"));
                            hotel_model.setState(jsonarr.getString("State"));
                            hotel_model.setCountry(jsonarr.getString("Country"));
                            hotel_model.setPincode(jsonarr.getString("Pincode"));
                            hotel_model.setDinner(jsonarr.getInt("Dinner"));
                            hotel_model.setLunch(jsonarr.getInt("Lunch"));
                            hotel_model.setExtra_Adult(jsonarr.getInt("Extra_Adult"));
                            hotel_model.setVisibility(jsonarr.getInt("Visibility"));
                            hotel_model.setWebsite(jsonarr.getString("Website"));
                            hotel_model.setB2C_Flag(jsonarr.getInt("B2C_Flag"));
                            hotel_model.setTrip_Image(jsonarr.getString("Trip_Image"));
                            hotel_model.setTrip_Script(jsonarr.getString("Trip_Script"));
                            hotel_model.setAccount_Holder(jsonarr.getString("Account_Holder"));
                            hotel_model.setAccount_Number(jsonarr.getString("Account_Number"));
                            hotel_model.setBank(jsonarr.getString("Bank"));
                            hotel_model.setIFSC_Code(jsonarr.getString("IFSC_Code"));
                            hotel_model.setDate(jsonarr.getString("Date"));
                            hotel_model.setAdmin_Id(jsonarr.getString("admin_Id"));

                            String[] value = defaultHotelRoom.get(index).trim().split(",");

                                if (Integer.parseInt("" + value[0]) == jsonarr.getInt("Hotel_Id")) {
                                    hotel_model.setChecked(true);
                                    swap_value = i;
                                    flag_bit = 1;
                                } else {
                                    hotel_model.setChecked(false);
                                }
                            }
                            //hotel_model.setChecked(true);
                        }
                      //
                        hotelList.add(hotel_model);
                        flag_bit =0;
                    }
                    if(response.getJSONArray("payload").length()!=0){
                    Collections.swap(hotelList, 0, swap_value);

                    //added on 12/08/2015
                    //HotelActivity.listViewPagerAdapter.notifyDataSetChanged();
                    mViewPagerAdapter.notifyDataSetChanged();
                    }
                    mHotelModels.put(position + "", hotelList);


                } catch (JSONException e) {
                    e.printStackTrace();
                    VolleyLog.d("Volley Error", "Error: " + e.getMessage());
                }
                if(single_loop_bit!=1) {
                    HotelActivity.listViewPagerAdapter.notifyDataSetChanged();
                    mViewPagerAdapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Volley Error", "Error: " + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(strReq);
    }

    private class ViewPagerClickListner implements ViewPager.OnClickListener{

        int postionClicked;
        public ViewPagerClickListner(int postionClick)
        {
            this.postionClicked = postionClick;
        }

        @Override
        public void onClick(View view) {

          Log.e("Pager position ", "parent " + postionClicked + "child position " + mPagerPositions.get(postionClicked) + "Viewpager.Currentpostion" +vp[postionClicked].getCurrentItem());
            vp[postionClicked].getCurrentItem();

        }
    }
    private class ViewPageChangeListner implements ViewPager.OnPageChangeListener{
        private int selectPosition;
        public ViewPageChangeListner(int selectPosition){
            this.selectPosition=selectPosition;


        }
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Log.d("PAGER ", "PAGER SCROLL child POSITION " + position + "parent position " + selectPosition);
            mPagerPositions.put(selectPosition,position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

     interface pagerCheckBoxChangedListner{
        public void OnCheckedChangeListenerCustomPager(int childPosition, boolean isChecked);
         public  void OnImageClickListenerCustomPager(int childpostion);
    }


    public class PagerCheckedChangeListnerCustom implements pagerCheckBoxChangedListner{
        int groupPosition;

        public PagerCheckedChangeListnerCustom(int groupPosition) {
            this.groupPosition = groupPosition;
        }

        @Override
        public void OnCheckedChangeListenerCustomPager(int childPosition,boolean isChecked) {
            ListviewChangedListener.OnCheckedChangeListenerCustomPager(childPosition, isChecked);
            ArrayList<HotelModel> modelRow=mHotelModels.get(""+groupPosition);
            for(int index =0 ; index<modelRow.size();index++) {
                if(childPosition==index) {
                    modelRow.get(index).setChecked(isChecked);
                    mHotelModels.put("" + groupPosition, modelRow);
                }
                else
                    modelRow.get(index).setChecked(false);

            }
            notifyDataSetChanged();
        }

        @Override
        public void OnImageClickListenerCustomPager(int childpostion) {
            ListviewChangedListener.OnImageClickListenerCustomPager(childpostion, groupPosition );
            //ArrayList<HotelModel> modelRow=mHotelModels.get(""+groupPosition);

          //  Log.i("PagerView Clicked",groupPosition+"Clicked"+childpostion+ " Check "+  modelRow.get(childpostion).getHotel_Name());


        }
    }

}