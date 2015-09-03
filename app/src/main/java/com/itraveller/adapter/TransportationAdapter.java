package com.itraveller.adapter;

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
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import com.itraveller.R;
import com.itraveller.activity.TransportationActivity;
import com.itraveller.constant.Constants;
import com.itraveller.model.TransportationModel;
import com.itraveller.volley.AppController;

public class TransportationAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<TransportationModel> TransportationItems;
    public static final String MY_PREFS = "ScreenHeight";
    private  int _screen_height;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private RadioButton mSelectedRB;
    private int mSelectedPosition = -1;
    public static String temp_image_id;

    public TransportationAdapter(Activity activity, List<TransportationModel> TransportationItems) {
        this.activity = activity;
        this.TransportationItems = TransportationItems;
    }
 
    @Override
    public int getCount() {
        return TransportationItems.size();
    }
 
    @Override
    public Object getItem(int location) {
        return TransportationItems.get(location);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
 
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.transportation_row, null);
            holder.radioButton = (RadioButton) convertView.findViewById(R.id.radio_btn);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.textView_persons = (TextView) convertView.findViewById(R.id.textView_persons);
            holder.textView_Km = (TextView) convertView.findViewById(R.id.textView_Km);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();


        SharedPreferences prefs = activity.getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
            _screen_height = prefs.getInt("Screen_Height", 0)-
                            (prefs.getInt("Status_Height", 0) + prefs.getInt("ActionBar_Height", 0));
            Log.i("iTraveller", "Screen Height: "+_screen_height);
            int width = prefs.getInt("Screen_Width", 0); //0 is the default value.

        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);

        FrameLayout frame_lay = (FrameLayout) convertView.findViewById(R.id.imgMain);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width,_screen_height/2);
        frame_lay.setLayoutParams(lp);
        SharedPreferences sharedpreferences = activity.getSharedPreferences("Itinerary", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();
        // getting data for the row
        final TransportationModel m = TransportationItems.get(position);

        // thumbnail image
        temp_image_id=m.getImage();
        thumbNail.setImageUrl(Constants.API_TransportationAdapter_ImageURL+m.getImage(), imageLoader);
        //Log.i("ImageURL", "http://stage.itraveller.com/backend/images/destinations/" + m.getRegion_Name() + ".jpg");
        // title
        holder.title.setText(m.getTitle());
        holder.textView_persons.setText("Ideal for upto " + m.getMax_Person() + "persons");
        holder.textView_Km.setText("" + m.getCost());

        holder.radioButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (position != mSelectedPosition && mSelectedRB != null) {
                    mSelectedRB.setChecked(false);
                }

                editor.putString("TransportationName",""+ m.getTitle());
                editor.putString("TransportationCost",""+ m.getCost());
                editor.commit();

                mSelectedPosition = position;
                mSelectedRB = (RadioButton) v;
            }
        });

        /*if(mSelectedPosition != position){
            holder.radioButton.setChecked(false);
        }else{
            holder.radioButton.setChecked(true);
            if(mSelectedRB != null && holder.radioButton != mSelectedRB){
                mSelectedRB = holder.radioButton;
            }
        }*/
        if(mSelectedPosition != position){
            holder.radioButton.setChecked(false);
        }else{
            holder.radioButton.setChecked(true);
            if(mSelectedRB != null && holder.radioButton != mSelectedRB){
                mSelectedRB = holder.radioButton;
            }

        }
        if(m.getCost()== TransportationActivity.lowest_trans)
        {
            editor.putString("TransportationName",""+ m.getTitle());
            editor.putString("TransportationCost",""+ m.getCost());
            editor.commit();
            holder.radioButton.setChecked(true);
             mSelectedRB = holder.radioButton;
        }
        return convertView;
    }

    private class ViewHolder {
        RadioButton radioButton;
        TextView title;
        TextView textView_persons;
        TextView textView_Km;
    }
}