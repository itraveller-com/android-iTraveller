package com.itraveller.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import com.itraveller.R;
import com.itraveller.constant.Constants;
import com.itraveller.model.HotelModel;
import com.itraveller.volley.AppController;


/**
 * Created by VNK on 6/25/2015.
 */
public class ViewPagerAdapter extends PagerAdapter {
ListViewPagerAdapter.pagerCheckBoxChangedListner mPagerCheckBoxChangedListner;

    public static int temp_hotel_id;

    public static int count=0;
    int check_bit=0;
    ArrayList<HotelModel> arrayModelClasses = new ArrayList<HotelModel>();

    @SuppressLint("NewApi")


    public ViewPagerAdapter() {

        super();

    }

    public ViewPagerAdapter(ArrayList<HotelModel> arrayModelClasses,ListViewPagerAdapter.pagerCheckBoxChangedListner mPagerCheckBoxChangedListner) {

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

        Log.d("PageSelection", "PageSelectionViewPager instatntiate " + position);
        // Inflating layout
        LayoutInflater inflater = (LayoutInflater) collection.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.hotel_viewpager_row, null);
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();


        TextView select_txt=(TextView) view.findViewById(R.id.myImageViewText);

        select_txt.setVisibility(View.GONE);

        TextView itemText = (TextView) view.findViewById(R.id.title);
        NetworkImageView image = (NetworkImageView) view.findViewById(R.id.thumbnail);
        image.setErrorImageResId(R.drawable.default_img);

        RelativeLayout rl=(RelativeLayout) view.findViewById(R.id.check);

        rl.setVisibility(View.GONE);

        CheckBox checkBox=(CheckBox)view.findViewById(R.id.checkBox);

        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.MyRating);
        //Background rate bar color
        Drawable drawable = ratingBar.getProgressDrawable();
        drawable.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);

        //Foreground rate bar color
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#ffff00"), PorterDuff.Mode.SRC_ATOP);


        try {

        //    image.setImageUrl("http://stage.itraveller.com/backend/images/hotels/"+arrayModelClasses.get(position).getHotel_Id()+".jpg", imageLoader);

            image.setImageUrl(Constants.API_ViewPagerAdapter_ImageURL + arrayModelClasses.get(position).getHotel_Id() + ".jpg", imageLoader);
            itemText.setText(arrayModelClasses.get(position).getHotel_Name());

            String rate_arr[]=(""+arrayModelClasses.get(position).getHotel_Star_Rating()).split(" ");

            ratingBar.setRating(Float.parseFloat(rate_arr[0]));

            //if(arrayModelClasses.get(position).get)
            checkBox.setChecked(false);
            Log.d("checkbox test",""+arrayModelClasses.get(position).isChecked());
            if(arrayModelClasses.get(position).isChecked())
            {
                if(check_bit == 0)
                {
                    checkBox.setChecked(true);

                    image.setColorFilter(Color.parseColor("#80000000"), PorterDuff.Mode.SRC_ATOP);
                    select_txt.setVisibility(View.VISIBLE);
                    Log.i("CheckedORNot", "checked" + position);
                }
            }
            else
            {
                checkBox.setChecked(false);
                Log.i("CheckedORNot", "Notchecked" + position);
            }

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked && buttonView.isPressed()) {
                        mPagerCheckBoxChangedListner.OnCheckedChangeListenerCustomPager(position, isChecked);
                       // mPagerCheckBoxChangedListner1.OnCheckedChangeListenerCustomPager(position, isChecked);
                        Log.i("CheckedORNot", "checked" + position);
                    }


                }
            });
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(count==0)
                    mPagerCheckBoxChangedListner.OnImageClickListenerCustomPager(position);
                    //mPagerCheckBoxChangedListner1.OnImageClickListenerCustomPager(position);

                    count++;
                }
            });

        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        ((ViewPager) collection).addView(view, 0);
       // notifyDataSetChanged();
        return view;

    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        Log.i("PageSelection", "PageSelectionViewPager destruct  " + position);
        ((ViewPager) container).removeView((View) object);
    }


}