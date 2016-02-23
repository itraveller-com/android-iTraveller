package com.itraveller.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import com.itraveller.R;
import com.itraveller.model.OnwardDomesticFlightModel;
import com.itraveller.model.ReturnDomesticFlightModel;
import com.itraveller.volley.AppController;

public class FlightDomesticReturnAdapter extends BaseAdapter {
    private Activity mActivity;
    private LayoutInflater mLayoutInflater;
    private List<ReturnDomesticFlightModel> mFlightItems;
    private int mIndex=0;
    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private RadioButton mSelectedRB;
    private int mSelectedPosition = -1;
    private final SharedPreferences.Editor editor;
    public static int sCount=0;
    public static int sBaseFlightPrice;

    public FlightDomesticReturnAdapter(Activity mActivity, List<ReturnDomesticFlightModel> mFlightItems) {
        this.mActivity = mActivity;
        this.mFlightItems = mFlightItems;
        SharedPreferences sharedpreferences = mActivity.getSharedPreferences("Itinerary", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
    }

    private SpannableString spanIt(String text, String queryText) {
        // search for query on text
        int startIndex = text.indexOf(queryText);
        int endIndex = startIndex + queryText.length();
        // spannable to show the search query
        SpannableString spanText = new SpannableString(text);
        if (startIndex > -1) {
            spanText.setSpan(new StyleSpan(Typeface.BOLD), 0,queryText.length() , 0);
        }
        return spanText;
    }

    public String getConvertedDate(String str)
    {
        String month[]={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        String temp[]=str.split("-");
        int temp_month=Integer.parseInt(temp[1]);
        str=month[temp_month-1]+" "+temp[2]+","+temp[0];
        return str;
    }


    //function to convert date into our required form
    public String getConvertedTime(String str)
    {
        String str_time[]=str.split(":");
        int hours=Integer.parseInt(str_time[0]);
        String converted_hours;
        if(hours>=12) {
            if (hours == 12)
            {
                str=str_time[0]+":"+str_time[1]+" PM";

            }
            else
            {
                hours = hours % 12;
                converted_hours = String.valueOf(hours);
                str = converted_hours + ":" + str_time[1] + " PM";
            }
        }
        else
        {
            if (hours == 0)
            {
                str = "12" + ":" + str_time[1] + " AM";
            }
            else
            {
                str = str_time[0] + ":" + str_time[1] + " AM";
            }
        }

        return str;
    }



    @Override
    public int getCount() {
        return mFlightItems.size();
    }

    @Override
    public Object getItem(int location) {
        return mFlightItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<ReturnDomesticFlightModel> getList(){
        return mFlightItems;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (mLayoutInflater == null)
            mLayoutInflater = (LayoutInflater) mActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            holder = new ViewHolder();
            mIndex=0;
            convertView = mLayoutInflater.inflate(R.layout.flight_domestic_row, null);
            holder.radioButton = (RadioButton) convertView.findViewById(R.id.radiobtn);
            holder.fl_name = (TextView) convertView.findViewById(R.id.name);
            holder.fl_dep = (TextView) convertView.findViewById(R.id.dep);
            holder.fl_arr = (TextView) convertView.findViewById(R.id.arr);
            holder.fl_price = (TextView) convertView.findViewById(R.id.price);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }


        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        final ReturnDomesticFlightModel m = mFlightItems.get(position);

        int Total_flight_fare = Integer.parseInt(m.getActualBaseFare()) + Integer.parseInt(m.getTax()) +
                Integer.parseInt(m.getSTax()) + Integer.parseInt(m.getSCharge()) +
                Integer.parseInt(m.getTDiscount()) + Integer.parseInt(m.getTPartnerCommission()) +
                Integer.parseInt(m.getTCharge()) + Integer.parseInt(m.getTMarkup()) +
                Integer.parseInt(m.getTSdiscount());


        String dep_date_time=""+ m.getDepartureDateTime();
        String dep_date[]=dep_date_time.split("T");
        dep_date[0]=getConvertedDate(dep_date[0]);
        dep_date[1]=getConvertedTime(dep_date[1]);

        String arr_date_time=""+m.getArrivalDateTime();
        String arr_date[]=arr_date_time.split("T");
        arr_date[0]=getConvertedDate(arr_date[0]);
        arr_date[1]=getConvertedTime(arr_date[1]);

        holder.fl_name.setText(spanIt(""+m.getOperatingAirlineName() + "\n" + m.getFlightNumber(),""+m.getOperatingAirlineName()));
        holder.fl_arr.setTextAppearance(mActivity, R.style.font_size_1);
        holder.fl_arr.setText(arr_date[0] + "\n" + arr_date[1]);
        holder.fl_dep.setTextAppearance(mActivity, R.style.font_size_1);
        holder.fl_dep.setText(dep_date[0]+"\n"+dep_date[1]);


        ++sCount;
        if(sCount==1)
        {
            sBaseFlightPrice=Total_flight_fare;

        }

        int diff=(Total_flight_fare-sBaseFlightPrice);

        if(diff==0)
        {
            //displaying flight price
            //    flight_txt.setText("Price: " + "\u20B9" + Total_price + "\t");
            holder.fl_price.setText("At Same Price"+ "\t");
        }
        else if(diff>0)
        {
            holder.fl_price.setText("\u20B9"+diff+" more" + "\t");
        }
        else
        {
            diff=-diff;
            holder.fl_price.setText("\u20B9" + (diff) + " less" + "\t");
        }



//        holder.fl_price.setText("\u20B9"+" "+Total_flight_fare);

        holder.radioButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (position != mSelectedPosition && mSelectedRB != null) {
                    mSelectedRB.setChecked(false);
                }


                int flightcharge = Integer.parseInt(m.getActualBaseFare()) + Integer.parseInt(m.getTax()) + Integer.parseInt(m.getSTax()) +
                        Integer.parseInt(m.getTCharge()) + Integer.parseInt(m.getSCharge()) + Integer.parseInt(m.getTDiscount()) +
                        Integer.parseInt(m.getTMarkup()) + Integer.parseInt(m.getTPartnerCommission()) + Integer.parseInt(m.getTSdiscount());



                editor.putString("DomesticReturnFlightDetails", m.getActualBaseFare()+ "," + m.getTax()+ "," + m.getSTax()+ "," + m.getTCharge()+ "," +
                        m.getSCharge()+ "," + m.getTDiscount()+ "," + m.getTMarkup()+ "," + m.getTPartnerCommission()+ "," + m.getTSdiscount()+ "," +
                        m.getOcTax()+ "," + m.getId()+ "," + m.getKey()+ "," + m.getAirEquipType()+ "," + m.getArrivalAirportCode()+ "," +
                        m.getArrivalDateTime()+ "," + m.getDepartureAirportCode()+ "," + m.getDepartureDateTime()+ "," + m.getFlightNumber()+ "," +
                        m.getOperatingAirlineName());
                editor.putString("ReturnFlightPrice",""+ flightcharge);
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
        return convertView;
    }

    private class ViewHolder {
        RadioButton radioButton;
        TextView fl_name;
        TextView fl_dep;
        TextView fl_arr;
        TextView fl_price;
    }

}