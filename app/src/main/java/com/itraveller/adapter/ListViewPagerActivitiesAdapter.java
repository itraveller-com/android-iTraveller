package com.itraveller.adapter;

/**
 * Created by VNK on 6/25/2015.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import java.util.List;
import java.util.Map;

import com.itraveller.R;
import com.itraveller.activity.ActivitiesActivity;
import com.itraveller.model.ActivitiesModel;
import com.itraveller.model.ReturnDomesticFlightModel;
import com.itraveller.volley.AppController;


public class ListViewPagerActivitiesAdapter extends ArrayAdapter<String> {

    private ViewPager[] mViewPagers;

    private ImageView[] mLeftArrow;
    private ImageView[] mRightArrow;
    private RelativeLayout[] mRelLeftArrow;
    private  RelativeLayout[] mRelRightArrow;
    private Context mContext;
    private ArrayList<String> mNavigationItems;
    private Map<Integer, Integer> mPagerPositions ;
    private ViewPagerActivitiesAdapter mViewPagerAdapter;
    private ListViewPagerActivitiesAdapter mListViewPagerAdapter;
    private ArrayList<String> mActivitiesList;
    private ArrayList<String> mDayCount;

    public ListViewPagerActivitiesAdapter(Context mContext, ArrayList<String> mNavigationItems, ArrayList<String> mDayCount) {
        super(mContext, R.layout.view_pager_list_view, mNavigationItems);
        this.mContext = mContext;
        this.mNavigationItems = mNavigationItems;
        this.mDayCount = mDayCount;
        ActivitiesActivity.mActivitiesModel=new HashMap<>();
        for(int index=0;index<mNavigationItems.size();index++){
            ActivitiesActivity.mActivitiesModel.put("" + index, new ArrayList<ActivitiesModel>());

            Log.i("TestingRound","Test");
        }

        mPagerPositions= new HashMap<Integer, Integer>();
        mViewPagers=new ViewPager[mNavigationItems.size()];
        mLeftArrow = new ImageView[mNavigationItems.size()];
        mRightArrow = new ImageView[mNavigationItems.size()];
        mRelLeftArrow = new RelativeLayout[mNavigationItems.size()];
        mRelRightArrow = new RelativeLayout[mNavigationItems.size()];
    }

    @Override
    public int getCount() {
        return mNavigationItems.size();
    }

    @Override
    public String getItem(int position) {

        return mNavigationItems.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView( final int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            // setSelectedIndex(position);
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.hotel_pageviewer, null);

        }
        else
        {


        }
        mViewPagers[position] = (ViewPager) convertView.findViewById(R.id.list_pager);
        mViewPagerAdapter = new ViewPagerActivitiesAdapter(ActivitiesActivity.mActivitiesModel.get(""+position),new PagerCheckedChangeListnerCustom(position));

        mViewPagers[position].setAdapter(mViewPagerAdapter);
        TextView txtview = (TextView) convertView.findViewById(R.id.hotel_place_name);
        txtview.setText("Day" + (position+1));

        mRelLeftArrow[position]=(RelativeLayout) convertView.findViewById(R.id.rel_left_arrow);
        mRelRightArrow[position]=(RelativeLayout) convertView.findViewById(R.id.rel_right_arrow);

        mLeftArrow[position]=(ImageView) convertView.findViewById(R.id.left_arrow);
        mRightArrow[position]=(ImageView) convertView.findViewById(R.id.right_arrow);
        //vp[position].setTag(position);
        mViewPagers[position].setOnClickListener(new ViewPagerClickListner(position));
        mViewPagers[position].setOnPageChangeListener(new ViewPageChangeListner(position));
        if(mPagerPositions.get(position)!=null){
            //  Log.e("Pager position ", "parent " + position + "child position " + mPagerPositions.get(position));
            mViewPagers[position].setCurrentItem(mPagerPositions.get(position));
        }


        mLeftArrow[position].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Left arrow","hi");
                mViewPagers[position].setCurrentItem(mViewPagers[position].getCurrentItem() - 1);
            }
        });

        mRightArrow[position].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Right arrow","bye");
                mViewPagers[position].setCurrentItem(mViewPagers[position].getCurrentItem() + 1);
            }
        });

        if(ActivitiesActivity.mActivitiesModel.get(""+position).size()<1){
            Log.i("TestingRound",""+mNavigationItems.get(position));
            ArrayList<ActivitiesModel> modelRow=ActivitiesActivity.mActivitiesModel.get("" + position);


        }else
        {

        }
        return convertView;
    }

    public void SetAdapterListview(ListViewPagerActivitiesAdapter mListViewPagerAdapter, ArrayList<String> mActivitiesList) {
        this.mListViewPagerAdapter = mListViewPagerAdapter;
        this.mActivitiesList = mActivitiesList;
    }

    private class ViewPagerClickListner implements ViewPager.OnClickListener{

        int postionClicked;
        public ViewPagerClickListner(int postionClick)
        {
            this.postionClicked = postionClick;
        }

        @Override
        public void onClick(View view) {

            Log.e("Pager position ", "parent " + postionClicked + "child position " + mPagerPositions.get(postionClicked) + "Viewpager.Currentpostion" +mViewPagers[postionClicked].getCurrentItem());
            mViewPagers[postionClicked].getCurrentItem();

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
        public void OnImageClickListenerCustomPager(int childpostion);
    }


    private class PagerCheckedChangeListnerCustom implements pagerCheckBoxChangedListner{
        int groupPosition;

        public PagerCheckedChangeListnerCustom(int groupPosition) {
            this.groupPosition = groupPosition;
        }

        @Override
        public void OnCheckedChangeListenerCustomPager(int childPosition,boolean isChecked) {
            ArrayList<ActivitiesModel> modelRow=ActivitiesActivity.mActivitiesModel.get(""+groupPosition);
            modelRow.get(childPosition).setChecked(isChecked);

        }

        @Override
        public void OnImageClickListenerCustomPager(int childpostion) {
            ArrayList<ActivitiesModel> modelRow=ActivitiesActivity.mActivitiesModel.get(""+groupPosition);
        }
    }
}