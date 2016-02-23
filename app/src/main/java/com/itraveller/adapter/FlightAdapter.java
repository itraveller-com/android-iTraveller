package com.itraveller.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import com.itraveller.R;
import com.itraveller.activity.ItinerarySummaryActivity;
import com.itraveller.model.FlightModel;
import com.itraveller.volley.AppController;

public class FlightAdapter extends BaseAdapter {
    private Activity mActivity;
    private LayoutInflater mLayoutInflater;
    private List<FlightModel> mFlightItems;
    private int _screen_height;
    int index = 0;
    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    private RadioButton mSelectedRB;
    private int mSelectedPosition = -1;

    public static int base_flight_price;
    public static int count=0;

    public FlightAdapter(Activity mActivity, List<FlightModel> mFlightItems) {
        this.mActivity = mActivity;
        this.mFlightItems = mFlightItems;
    }

    //function to make  BOLD text
    private SpannableString spanIt(String text, String queryText) {
        // search for query on text
        int startIndex = text.indexOf(queryText);
        // spannable to show the search query
        SpannableString spanText = new SpannableString(text);
        if (startIndex > -1) {
            spanText.setSpan(new StyleSpan(Typeface.BOLD), 0, queryText.length(), 0);
        }
        return spanText;
    }

    //function to convert date into our required form
    public String getConvertedDate(String str) {
        String month[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        String temp[] = str.split("-");
        int temp_month = Integer.parseInt(temp[1]);
        str = month[temp_month - 1] + " " + temp[2] + "," + temp[0];
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


    //getting total number of flight items
    @Override
    public int getCount() {
        return mFlightItems.size();
    }

    //getting item from given location
    @Override
    public Object getItem(int location) {
        return mFlightItems.get(location);
    }

    //getting item id
    @Override
    public long getItemId(int position) {
        return position;
    }

    //getting flight list
    public List<FlightModel> getList() {
        return mFlightItems;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        if (mLayoutInflater == null)
            mLayoutInflater = (LayoutInflater) mActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            index = 0;
            convertView = mLayoutInflater.inflate(R.layout.flights_row, null);

        }

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        TextView option_txt = (TextView) convertView.findViewById(R.id.option_txt);
        TextView flight_txt = (TextView) convertView.findViewById(R.id.flight_price);
        option_txt.setText("\tOption " + (position + 1));
        int Total_price = Integer.parseInt(mFlightItems.get(position).getActualBaseFare()) + Integer.parseInt(mFlightItems.get(position).getSCharge()) +
                Integer.parseInt(mFlightItems.get(position).getSTax()) + Integer.parseInt(mFlightItems.get(position).getTCharge()) +
                Integer.parseInt(mFlightItems.get(position).getTDiscount()) + Integer.parseInt(mFlightItems.get(position).getTMarkup()) +
                Integer.parseInt(mFlightItems.get(position).getTPartnerCommission()) + Integer.parseInt(mFlightItems.get(position).getTSdiscount()) +
                Integer.parseInt(mFlightItems.get(position).getTax()) + Integer.parseInt(mFlightItems.get(position).getOcTax());

        ++count;
        if(count==1)
        {
            base_flight_price=Total_price;

        }

        int diff=(Total_price-base_flight_price);

        if(diff==0)
        {
            //displaying flight price
        //    flight_txt.setText("Price: " + "\u20B9" + Total_price + "\t");
            flight_txt.setText("At Same Price"+ "\t");
        }
        else if(diff>0)
        {
            flight_txt.setText("\u20B9"+diff+" more" + "\t");
        }
        else
        {
            diff=-diff;
            flight_txt.setText("\u20B9"+(diff)+" less" + "\t");
        }

        TextView onward_head = (TextView) convertView.findViewById(R.id.onward_header);
        TextView return_head = (TextView) convertView.findViewById(R.id.return_header);
        String onward_txt = "", onward_place_txt = "", return_txt = "", return_place_txt = "";
        TableLayout tl = (TableLayout) convertView.findViewById(R.id.onward_table);
        tl.removeAllViewsInLayout();
        TableLayout tl_return = (TableLayout) convertView.findViewById(R.id.return_table);
        tl_return.removeAllViewsInLayout();

        final int Flightitems_size=mFlightItems.get(position).getOnward_model().size();
        for (int i = (Flightitems_size-1); i>=0; i--) {

            TableRow row = new TableRow(mActivity);
            row = new TableRow(mActivity);
            row.setId(index);
            index++;
            if (i == 0) {
                String arr_date_time = "" + mFlightItems.get(position).getOnward_model().get(i).getArrivalDateTime();
                String arr_date[] = arr_date_time.split("T");
                //converting arrival date
                arr_date[0] = getConvertedDate(arr_date[0]);
                //converting arrival time
                arr_date[1] = getConvertedTime(arr_date[1]);
                onward_txt = onward_txt + " Arr: " + arr_date[0] + " " + arr_date[1];
                onward_place_txt = onward_place_txt + " to " + mFlightItems.get(position).getOnward_model().get(i).getArrivalAirportCode();
            }
            if (i == (mFlightItems.get(position).getOnward_model().size() - 1)) {


                String dep_date_time = mFlightItems.get(position).getOnward_model().get(i).getDepartureDateTime();
                String dep_date[] = dep_date_time.split("T");
                //converting departure date
                dep_date[0] = getConvertedDate(dep_date[0]);
                //converting departure time
                dep_date[1] = getConvertedTime(dep_date[1]);

                onward_txt = onward_txt + " Arr: " + dep_date[0] + " " + dep_date[1];
                onward_place_txt = mFlightItems.get(position).getOnward_model().get(i).getDepartureAirportCode() + " to " +onward_place_txt ;

                //removed
            //    onward_txt = "Dep: " + dep_date[0] + " " + dep_date[1];
            //    onward_place_txt = Flightitems.get(position).getOnward_model().get(i).getDepartureAirportCode();

            }
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);

            TextView flight = new TextView(convertView.getContext());
            TextView arrival = new TextView(convertView.getContext());
            TextView departure = new TextView(convertView.getContext());
            //TextView price = new TextView(convertView.getContext());
            String dep_date_time = "" + mFlightItems.get(position).getOnward_model().get(i).getDepartureDateTime();
            String dep_date[] = dep_date_time.split("T");
            //converting departure date
            dep_date[0] = getConvertedDate(dep_date[0]);
            //converting departure time
            dep_date[1] = getConvertedTime(dep_date[1]);

            String arr_date_time = "" + mFlightItems.get(position).getOnward_model().get(i).getArrivalDateTime();
            String arr_date[] = arr_date_time.split("T");
            //converting arrival date
            arr_date[0] = getConvertedDate(arr_date[0]);
            //converting arrival time
            arr_date[1] = getConvertedTime(arr_date[1]);

            //displaying flight name and number
            flight.setText(spanIt("\t" + mFlightItems.get(position).getOnward_model().get(i).getOperatingAirlineName() + "\n\t" + mFlightItems.get(position).getOnward_model().get(i).getFlightNumber(), "" + mFlightItems.get(position).getOnward_model().get(i).getOperatingAirlineName()));

            arrival.setTextAppearance(mActivity, R.style.font_size);
            //displaying departure date and time
            arrival.setText(spanIt("" + mFlightItems.get(position).getOnward_model().get(i).getDepartureAirportName() + "\n" + dep_date[0] + "\n" + dep_date[1], "" + mFlightItems.get(position).getOnward_model().get(i).getDepartureAirportName()));

            departure.setTextAppearance(mActivity, R.style.font_size);
            //displaying arrival date and time
            departure.setText(spanIt("" + mFlightItems.get(position).getOnward_model().get(i).getArrivalAirportName() + "\n" + arr_date[0] + "\n" + arr_date[1], "" + mFlightItems.get(position).getOnward_model().get(i).getArrivalAirportName()));

            row.addView(flight);
            row.addView(arrival);
            row.addView(departure);

            tl.addView(row);
        }

        //displaying onward text
        onward_head.setTextAppearance(mActivity, R.style.font_size);
        onward_head.setText(spanIt("\tOnward - " + onward_place_txt, "Onward"));// + "\n" + onward_txt, "Onward"));



        int Flightitems1_size=mFlightItems.get(position).getReturn_model().size();
        for (int j = (Flightitems1_size-1); j >=0; j--) {

            TableRow r_row = new TableRow(mActivity);
            r_row = new TableRow(mActivity);
            r_row.setId(index);
            index++;
            if (j == 0) {

                String arr_date_time3 = mFlightItems.get(position).getReturn_model().get(j).getArrivalDateTime();
                String arr_date3[] = arr_date_time3.split("T");
                //converting arrival date
                arr_date3[0] = getConvertedDate(arr_date3[0]);
                //converting arrival time
                arr_date3[1]=getConvertedTime(arr_date3[1]);

                return_txt = return_txt + " Arr: " + arr_date3[0] + " " + arr_date3[1];
                return_place_txt = return_place_txt + " to " + mFlightItems.get(position).getReturn_model().get(j).getArrivalAirportCode();
            }
            if (j == (mFlightItems.get(position).getReturn_model().size() - 1)) {

                String dep_date_time2 = mFlightItems.get(position).getReturn_model().get(j).getDepartureDateTime();
                String dep_date2[] = dep_date_time2.split("T");

                //converting departure date
                dep_date2[0] = getConvertedDate(dep_date2[0]);

                //converting departure time
                dep_date2[1]=getConvertedTime(dep_date2[1]);

                return_txt = return_txt + " Arr: " + dep_date2[0] + " " + dep_date2[1];
                return_place_txt = mFlightItems.get(position).getReturn_model().get(j).getDepartureAirportCode() + " to " + return_place_txt;

                //remove
//                return_txt = "Dep: " + dep_date2[0] + " " + dep_date2[1];
//                return_place_txt = Flightitems.get(position).getReturn_model().get(j).getDepartureAirportCode();


            }
            TableRow.LayoutParams lp1 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            r_row.setLayoutParams(lp1);

            TextView r_flight = new TextView(convertView.getContext());
            TextView r_arrival = new TextView(convertView.getContext());
            TextView r_departure = new TextView(convertView.getContext());

            String dep_date_time1 = "" + mFlightItems.get(position).getReturn_model().get(j).getDepartureDateTime();
            String dep_date1[] = dep_date_time1.split("T");
            //converting departure date
            dep_date1[0] = getConvertedDate(dep_date1[0]);
            //converting departure time
            dep_date1[1]=getConvertedTime(dep_date1[1]);

            String arr_date_time1 = "" + mFlightItems.get(position).getReturn_model().get(j).getArrivalDateTime();
            String arr_date1[] = arr_date_time1.split("T");
            //CONVERTING arrival date
            arr_date1[0] = getConvertedDate(arr_date1[0]);
            //converting arrrival time
            arr_date1[1]=getConvertedTime(arr_date1[1]);


            //displaying return flight name and number
            r_flight.setText(spanIt("\t" + mFlightItems.get(position).getReturn_model().get(j).getOperatingAirlineName() + "\n\t" + mFlightItems.get(position).getReturn_model().get(j).getFlightNumber(), "" + mFlightItems.get(position).getReturn_model().get(j).getOperatingAirlineName()));
            //displaying return flight departure time
            r_arrival.setTextAppearance(mActivity, R.style.font_size);
            r_arrival.setText(spanIt("" + mFlightItems.get(position).getReturn_model().get(j).getDepartureAirportName() + "\n" + dep_date1[0] + "\n" + dep_date1[1], "" + mFlightItems.get(position).getReturn_model().get(j).getDepartureAirportName()));
            //displaying return flight arrival time
            r_departure.setTextAppearance(mActivity, R.style.font_size);
            r_departure.setText(spanIt("" + mFlightItems.get(position).getReturn_model().get(j).getArrivalAirportName() + "\n" + arr_date1[0] + "\n" + arr_date1[1], "" + mFlightItems.get(position).getReturn_model().get(j).getArrivalAirportName()));


            r_row.addView(r_flight);
            r_row.addView(r_arrival);
            r_row.addView(r_departure);
            tl_return.addView(r_row);
        }

        //displaying return text
        return_head.setTextAppearance(mActivity, R.style.font_size);
        return_head.setText(spanIt("\tReturn - " + return_place_txt,"Return"));// + "\n" + return_txt, "Return"));

        //select button for selecting flight
        //final Button select_btn = (Button) convertView.findViewById(R.id.btn_select);
        final RadioButton radioButton= (RadioButton) convertView.findViewById(R.id.btn_select);

        radioButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.d("Testing", "Hello user6" + " " + position);
                Log.d("Testing", "Hello user7" + " " + mSelectedPosition);


                if (position != mSelectedPosition && mSelectedRB != null)
                {
                    mSelectedRB.setChecked(false);

                    mSelectedRB.setBackgroundResource(R.drawable.button_select);

//                    finalHolder.radioButton.setBackgroundResource(R.drawable.selected_button);

                    Log.d("Testing", "Hello user1" + " " + position);
                }

                radioButton.setBackgroundResource(R.drawable.button_selected);

                mSelectedPosition = position;
                mSelectedRB = (RadioButton) v;

                Log.i("ButtonClicked", "" + position);
                SharedPreferences sharedpreferences = mActivity.getSharedPreferences("Itinerary", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                int ActualPrice = Integer.parseInt(mFlightItems.get(position).getActualBaseFare()) * 2;
                editor.putString("FlightPrice", "" + ActualPrice);
                //JSON Value.
                String flight_price = mFlightItems.get(position).getActualBaseFare() + "," + mFlightItems.get(position).getTax() + "," + mFlightItems.get(position).getSTax() + "," + mFlightItems.get(position).getTCharge() + "," + mFlightItems.get(position).getSCharge() + "," + mFlightItems.get(position).getTDiscount() + "," + mFlightItems.get(position).getTMarkup() + "," + mFlightItems.get(position).getTPartnerCommission() + "," + mFlightItems.get(position).getTSdiscount() + "," + mFlightItems.get(position).getOcTax() + "," + mFlightItems.get(position).getId() + "," + mFlightItems.get(position).getKey();
                editor.putString("InternationalFlightPrice", "" + flight_price);
                String flight_onward_data= "";
                int flight_onward_size=mFlightItems.get(position).getOnward_model().size();
                for(int i = 0; i< flight_onward_size; i++ ){
                    String flight_onward = mFlightItems.get(position).getOnward_model().get(i).getAirEquipType() + "," + mFlightItems.get(position).getOnward_model().get(i).getArrivalAirportCode() + "," + mFlightItems.get(position).getOnward_model().get(i).getArrivalAirportName() + "," + mFlightItems.get(position).getOnward_model().get(i).getArrivalDateTime() + "," + mFlightItems.get(position).getOnward_model().get(i).getDepartureAirportCode() + "," + mFlightItems.get(position).getOnward_model().get(i).getDepartureAirportName() + "," + mFlightItems.get(position).getOnward_model().get(i).getDepartureDateTime() + "," + mFlightItems.get(position).getOnward_model().get(i).getFlightNumber() + "," + mFlightItems.get(position).getOnward_model().get(i).getMarketingAirlineCode() + "," + mFlightItems.get(position).getOnward_model().get(i).getOperatingAirlineCode() + "," + mFlightItems.get(position).getOnward_model().get(i).getOperatingAirlineName() + "," + mFlightItems.get(position).getOnward_model().get(i).getOperatingAirlineFlightNumber() + "," + mFlightItems.get(position).getOnward_model().get(i).getNumStops() + "," + mFlightItems.get(position).getOnward_model().get(i).getLinkSellAgrmnt() + "," + mFlightItems.get(position).getOnward_model().get(i).getConx() + "," + mFlightItems.get(position).getOnward_model().get(i).getAirpChg() + "," + mFlightItems.get(position).getOnward_model().get(i).getInsideAvailOption() + "," + mFlightItems.get(position).getOnward_model().get(i).getGenTrafRestriction() + "," + mFlightItems.get(position).getOnward_model().get(i).getDaysOperates() + "," + mFlightItems.get(position).getOnward_model().get(i).getJrnyTm() + "," + mFlightItems.get(position).getOnward_model().get(i).getEndDt() + "," + mFlightItems.get(position).getOnward_model().get(i).getStartTerminal() + "," + mFlightItems.get(position).getOnward_model().get(i).getEndTerminal();
                    if(i == 0){
                        flight_onward_data = flight_onward;
                    }else{
                        flight_onward_data = flight_onward_data + "-" +flight_onward;
                    }

                }
                editor.putString("InternationalFlightOnwardDetails", "" + flight_price);
                String flight_return_data= "";
                int flight_return_size=mFlightItems.get(position).getReturn_model().size();
                for(int i = 0; i< flight_return_size; i++ ){
                    String flight_return = mFlightItems.get(position).getReturn_model().get(i).getAirEquipType() + "," + mFlightItems.get(position).getReturn_model().get(i).getArrivalAirportCode() + "," + mFlightItems.get(position).getReturn_model().get(i).getArrivalAirportName() + "," + mFlightItems.get(position).getReturn_model().get(i).getArrivalDateTime() + "," + mFlightItems.get(position).getReturn_model().get(i).getDepartureAirportCode() + "," + mFlightItems.get(position).getReturn_model().get(i).getDepartureAirportName() + "," + mFlightItems.get(position).getReturn_model().get(i).getDepartureDateTime() + "," + mFlightItems.get(position).getReturn_model().get(i).getFlightNumber() + "," + mFlightItems.get(position).getReturn_model().get(i).getMarketingAirlineCode() + "," + mFlightItems.get(position).getReturn_model().get(i).getOperatingAirlineCode() + "," + mFlightItems.get(position).getReturn_model().get(i).getOperatingAirlineName() + "," + mFlightItems.get(position).getReturn_model().get(i).getOperatingAirlineFlightNumber() + "," + mFlightItems.get(position).getReturn_model().get(i).getNumStops() + "," + mFlightItems.get(position).getReturn_model().get(i).getLinkSellAgrmnt() + "," + mFlightItems.get(position).getReturn_model().get(i).getConx() + "," + mFlightItems.get(position).getReturn_model().get(i).getAirpChg() + "," + mFlightItems.get(position).getReturn_model().get(i).getInsideAvailOption() + "," + mFlightItems.get(position).getReturn_model().get(i).getGenTrafRestriction() + "," + mFlightItems.get(position).getReturn_model().get(i).getDaysOperates() + "," + mFlightItems.get(position).getReturn_model().get(i).getJrnyTm() + "," + mFlightItems.get(position).getReturn_model().get(i).getEndDt() + "," + mFlightItems.get(position).getReturn_model().get(i).getStartTerminal() + "," + mFlightItems.get(position).getReturn_model().get(i).getEndTerminal();
                    if(i == 0){
                        flight_return_data = flight_return;
                    }else{
                        flight_return_data = flight_return_data + "-" +flight_return;
                    }

                }
                editor.putString("InternationalFlightReturnDetails", "" + flight_price);
                editor.commit();

                SharedPreferences prefs=mActivity.getSharedPreferences("Preferences",Context.MODE_PRIVATE);

                Intent in = new Intent(mActivity, ItinerarySummaryActivity.class);

                editor = prefs.edit();
                editor.putInt("Skip_Flight_Bit", 0);
                editor.putInt("No_Flights",1);
                editor.commit();

                mActivity.startActivity(in);



                Log.d("Testing", "Hello user4" + " " + mSelectedRB);

            }
        });

        if(mSelectedPosition != position)
        {
            radioButton.setChecked(false);

            Log.d("Testing","Hello user2");
        }
        else
        {
            Log.d("Testing","Hello user3");

            radioButton.setBackgroundResource(R.drawable.button_selected);

            radioButton.setChecked(true);
            if(mSelectedRB != null && radioButton != mSelectedRB)
            {
                mSelectedRB = radioButton;
            }
        }

        // getting data for the row
        final FlightModel m = mFlightItems.get(position);

        return convertView;
    }
}
