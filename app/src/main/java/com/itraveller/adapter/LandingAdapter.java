package com.itraveller.adapter;
 
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
 
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import com.itraveller.R;
import com.itraveller.constant.Constants;
import com.itraveller.model.LandingModel;
import com.itraveller.volley.AppController;

public class LandingAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<LandingModel> LandingItems;
    public static final String MY_PREFS = "ScreenHeight";
    private  int _screen_height;


    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
 
    public LandingAdapter(Activity activity, List<LandingModel> LandingItems) {
        this.activity = activity;
        this.LandingItems = LandingItems;
    }

    //getting count of total number of Landingitems
    @Override
    public int getCount() {
        return LandingItems.size();
    }

    //getting item from given location
    @Override
    public Object getItem(int location) {
        return LandingItems.get(location);
    }

    //getting itemID
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
 
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.landing_row, null);
 
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();


        SharedPreferences prefs = activity.getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
            _screen_height = prefs.getInt("Screen_Height", 0)-(prefs.getInt("Status_Height", 0) + prefs.getInt("ActionBar_Height", 0));
            Log.i("iTraveller", "Screen Height: "+_screen_height);
            int width = prefs.getInt("Screen_Width", 0); //0 is the default value.


        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);
        FrameLayout frame_lay = (FrameLayout) convertView.findViewById(R.id.imgMain);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width,_screen_height/2);
        frame_lay.setLayoutParams(lp);

        // getting data for the row
        LandingModel m = LandingItems.get(position);
        //Log.i("model_length", );
        // thumbnail image

        thumbNail.setImageUrl(Constants.API_LandingAdapter_ImageURL+m.getRegion_Id()+".jpg", imageLoader);
        //Log.i("ImageURL", "http://stage.itraveller.com/backend/images/destinations/" + m.getRegion_Name() + ".jpg");
        // title
        title.setText(m.getRegion_Name());
 
        return convertView;
    }
 
}