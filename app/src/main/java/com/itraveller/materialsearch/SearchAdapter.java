package com.itraveller.materialsearch;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.itraveller.R;
import com.itraveller.model.RecentResults;
import com.itraveller.model.SearchBarModel;

import java.util.ArrayList;

public class SearchAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<SearchBarModel> mCountries;
    private LayoutInflater mLayoutInflater;
    private boolean mIsFilterList;

    public SearchAdapter(Context context, ArrayList<SearchBarModel> countries, boolean isFilterList) {
        this.mContext = context;
        this.mCountries =countries;
        this.mIsFilterList = isFilterList;
    }


    public void updateList(Context context, ArrayList<SearchBarModel> filterList, boolean isFilterList) {
        this.mContext = context;
        this.mCountries = filterList;
        this.mIsFilterList = isFilterList;
        notifyDataSetChanged ();
    }

    public int getCount() {
        return mCountries.size();
    }

    @Override
    public Object getItem(int i) {
        return mCountries.get(i).getValue();
    }
    public Object getItemKeyAndValue(int i) {
        return mCountries.get(i).getValue() + ":" + mCountries.get(i).getKey();
    }


    /*@Override
    public String getItem(int position) {
        return mCountries.get(position);
    }*/

    public long getItemId(int i) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder = null;
        if(v==null){

            holder = new ViewHolder();

            mLayoutInflater = (LayoutInflater)mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);

            v = mLayoutInflater.inflate(R.layout.list_item_search, parent, false);
            holder.txtCountry = (TextView)v.findViewById(R.id.txt_country);
            v.setTag(holder);
        }else{

            holder = (ViewHolder) v.getTag();
        }

        holder.txtCountry.setText(mCountries.get(position).getValue());

        Drawable searchDrawable,recentDrawable;

        if(android.os.Build.VERSION.SDK_INT >= 21){
            searchDrawable = mContext.getResources().getDrawable(R.drawable.ic_action_search, null);
            recentDrawable = mContext.getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp, null);

        } else {
            searchDrawable = mContext.getResources().getDrawable(R.drawable.ic_action_search);
            recentDrawable = mContext.getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);

        }
        if(mIsFilterList) {
            holder.txtCountry.setCompoundDrawablesWithIntrinsicBounds(searchDrawable, null, null, null);
        }else {
            holder.txtCountry.setCompoundDrawablesWithIntrinsicBounds(recentDrawable, null, null, null);

        }
        return v;
    }

}

class ViewHolder{

     TextView txtCountry;
}





