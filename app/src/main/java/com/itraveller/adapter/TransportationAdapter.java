package com.itraveller.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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

public class TransportationAdapter extends RecyclerView.Adapter<TransportationAdapter.ViewHolder> {
    private Activity mActivity;
    private List<TransportationModel> mTransportationItems;
    private ImageLoader mImageLoader = AppController.getInstance().getImageLoader();
    private RadioButton mSelectedRB;
    private int mSelectedPosition = -1;
    private int mFlagBit =0;

    public TransportationAdapter(Activity mActivity, List<TransportationModel> mTransportationItems) {
        this.mActivity = mActivity;
        this.mTransportationItems = mTransportationItems;
    }

    public int getCount() {
        return mTransportationItems.size();
    }

    public Object getItem(int location) {
        return mTransportationItems.get(location);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup convertView, final int position) {
        View v = LayoutInflater.from(convertView.getContext()).inflate(R.layout.transportation_row, convertView, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,  final int position) {

        SharedPreferences sharedpreferences = mActivity.getSharedPreferences("Itinerary", Context.MODE_PRIVATE);

        final SharedPreferences prefsData = mActivity.getSharedPreferences("SavedData", mActivity.MODE_PRIVATE);


        final SharedPreferences.Editor editor = sharedpreferences.edit();
        // getting data for the row
        final TransportationModel m = mTransportationItems.get(position);

        //thumbnail image
        //thumbNail.setImageUrl("http://stage.itraveller.com/backend/images/transfers/" + m.getImage() , imageLoader);
        holder.thumbNail.setImageUrl(Constants.API_TransportationAdapter_ImageURL+ m.getImage() , mImageLoader);
        holder.thumbNail.setErrorImageResId(R.drawable.no_transportation);
        //Log.i("ImageURL", "http://stage.itraveller.com/backend/images/destinations/" + m.getRegion_Name() + ".jpg");
        //title
        holder.title.setText(m.getTitle());
        holder.textView_persons.setText("Ideal for upto " + m.getMax_Person() + "persons");
        holder.textView_Km.setText("\u20B9"+"" + m.getCost());


        if(mFlagBit==0) {
            if (m.getIsCheck()) {

                editor.putString("MasterID", "" + m.getId());
                editor.putString("TransportationID", "" + m.getTransportation_Id());
                editor.putString("TransportationName", "" + m.getTitle());
                editor.putString("TransportationCost", "" + m.getCost());
                editor.commit();

                holder.radioButton.setChecked(true);
                mSelectedPosition = position;
                mSelectedRB = holder.radioButton;
                mFlagBit = 1;
            }
        }

        final ViewHolder finalHolder = holder;
        holder.radioButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                editor.putString("MasterID", "" + m.getId());
                editor.putString("TransportationID", "" + m.getTransportation_Id());
                editor.putString("TransportationName", "" + m.getTitle());
                editor.putString("TransportationCost", "" + m.getCost());
                editor.commit();

                prefsData.edit().putString("TransportationID", "" + m.getTransportation_Id()).commit();
                if(position != mSelectedPosition && mSelectedRB != null){
                    mSelectedRB.setChecked(false);
                }

                mSelectedPosition = position;
                mSelectedRB = (RadioButton)v;
            }
        });


        if(mSelectedPosition != position){
            holder.radioButton.setChecked(false);
        }else{
            holder.radioButton.setChecked(true);
            if(mSelectedRB != null && holder.radioButton != mSelectedRB){
                mSelectedRB = holder.radioButton;
            }
        }
        /*if(m.getIsCheck())
        {
            holder.radioButton.setChecked(true);
        }else {
            holder.radioButton.setChecked(false);
        }*/

        // notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mTransportationItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public RadioButton radioButton;
        public TextView title;
        public TextView textView_persons;
        public TextView textView_Km;
        public NetworkImageView thumbNail;
        public FrameLayout frame_lay;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            radioButton = (RadioButton) itemView.findViewById(R.id.radio_btn);
            title = (TextView) itemView.findViewById(R.id.title);
            textView_persons = (TextView) itemView.findViewById(R.id.textView_persons);
            textView_Km = (TextView) itemView.findViewById(R.id.textView_Km);
            thumbNail = (NetworkImageView) itemView.findViewById(R.id.thumbnail);
            frame_lay = (FrameLayout) itemView.findViewById(R.id.imgMain);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }
}