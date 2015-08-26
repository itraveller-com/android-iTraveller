package com.itraveller.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import com.itraveller.R;
import com.itraveller.model.ActivitiesModel;
import com.itraveller.volley.AppController;


/**
 * Created by VNK on 6/25/2015.
 */
public class ViewPagerActivitiesAdapter extends PagerAdapter {
ListViewPagerActivitiesAdapter.pagerCheckBoxChangedListner mPagerCheckBoxChangedListner;

    int check_bit=0;
    ArrayList<ActivitiesModel> arrayModelClasses = new ArrayList<ActivitiesModel>();

    @SuppressLint("NewApi")


    public ViewPagerActivitiesAdapter() {

        super();

    }

    public ViewPagerActivitiesAdapter(ArrayList<ActivitiesModel> arrayModelClasses, ListViewPagerActivitiesAdapter.pagerCheckBoxChangedListner mPagerCheckBoxChangedListner) {

        super();
        this.arrayModelClasses = arrayModelClasses;
        this.mPagerCheckBoxChangedListner=mPagerCheckBoxChangedListner;

    }

    @Override
    public int getCount() {

        return arrayModelClasses.size();

    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public boolean isViewFromObject(View collection, Object object) {

        return collection == ((View) object);
    }

    @Override
    public Object instantiateItem(View collection, final int position) {
        Log.i("PageSelection", "PageSelectionViewPager instatntiate " + position);
        // Inflating layout
        LayoutInflater inflater = (LayoutInflater) collection.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.activities_viewpager_row, null);
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();

        TextView itemText = (TextView) view.findViewById(R.id.title);
        NetworkImageView image = (NetworkImageView) view.findViewById(R.id.thumbnail);
        TextView cost = (TextView) view.findViewById(R.id.cost_activity);
        TextView time = (TextView) view.findViewById(R.id.time_activity);

        CheckBox checkBox=(CheckBox)view.findViewById(R.id.checkBox);

        try {

            image.setImageUrl("http://stage.itraveller.com/backend/images/activity/" + arrayModelClasses.get(position).getId() + ".jpg", imageLoader);
            itemText.setText(arrayModelClasses.get(position).getTitle());
            if(arrayModelClasses.get(position).getCost() == 0)
                cost.setText("Free");
            else
            cost.setText("" + arrayModelClasses.get(position).getCost());
            time.setText("" + arrayModelClasses.get(position).getDuration() + "HRS");

            if(arrayModelClasses.get(position).isChecked()){
                if(check_bit == 0) {
                    checkBox.setChecked(true);
                    Log.i("CheckedORNot", "checked" + position);
                }
            }
            else{
                checkBox.setChecked(false);
                Log.i("CheckedORNot", "Notchecked" + position);
            }

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked && buttonView.isPressed()) {
                        mPagerCheckBoxChangedListner.OnCheckedChangeListenerCustomPager(position, isChecked);
                        Log.i("CheckedORNot", "checked" + isChecked);
                    }
                    else
                    {
                        mPagerCheckBoxChangedListner.OnCheckedChangeListenerCustomPager(position, isChecked);
                        Log.i("CheckedORNot", "checked" + isChecked);
                    }

                }
            });
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPagerCheckBoxChangedListner.OnImageClickListenerCustomPager(position);
                }
            });

        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        ((ViewPager) collection).addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        Log.i("PageSelection", "PageSelectionViewPager destruct  " + position);
        ((ViewPager) container).removeView((View) object);
    }


}