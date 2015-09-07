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
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import org.w3c.dom.Text;

import java.util.List;

import com.itraveller.R;
import com.itraveller.activity.ItinerarySummaryActivity;
import com.itraveller.activity.SummaryActivity;
import com.itraveller.model.FlightModel;
import com.itraveller.volley.AppController;

public class FlightAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<FlightModel> Flightitems;
    private int _screen_height;
    int index = 0;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public FlightAdapter(Activity activity, List<FlightModel> flightitems) {
        this.activity = activity;
        this.Flightitems = flightitems;
    }


    private SpannableString spanIt(String text, String queryText) {
        // search for query on text
        int startIndex = text.indexOf(queryText);
        int endIndex = startIndex + queryText.length();
        // spannable to show the search query
        SpannableString spanText = new SpannableString(text);
        if (startIndex > -1) {
            spanText.setSpan(new StyleSpan(Typeface.BOLD), 0, queryText.length(), 0);
        }
        return spanText;
    }

    public String getConvertedDate(String str) {
        String month[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        String temp[] = str.split("-");
        int temp_month = Integer.parseInt(temp[1]);
        str = month[temp_month - 1] + " " + temp[2] + "," + temp[0];
        return str;
    }

    public String getConvertedTime(String str)
    {
        String str_time[]=str.split(":");
        int hours=Integer.parseInt(str_time[0]);
        String converted_hours;
        if(hours>12)
        {
            hours=hours%12;
            converted_hours=String.valueOf(hours);
            str=converted_hours+":"+str_time[1]+"PM";
        }
        else
        {
            str=str_time[0]+":"+str_time[1]+"AM";
        }

        return str;
    }

    @Override
    public int getCount() {
        return Flightitems.size();
    }

    @Override
    public Object getItem(int location) {
        return Flightitems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<FlightModel> getList() {
        return Flightitems;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            index = 0;
            convertView = inflater.inflate(R.layout.flights_row, null);

        }


        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        TextView option_txt = (TextView) convertView.findViewById(R.id.option_txt);
        TextView flight_txt = (TextView) convertView.findViewById(R.id.flight_price);
        option_txt.setText("\tOption " + (position + 1));
        int Total_price = Integer.parseInt(Flightitems.get(position).getActualBaseFare()) + Integer.parseInt(Flightitems.get(position).getSCharge()) +
                Integer.parseInt(Flightitems.get(position).getSTax()) + Integer.parseInt(Flightitems.get(position).getTCharge()) +
                Integer.parseInt(Flightitems.get(position).getTDiscount()) + Integer.parseInt(Flightitems.get(position).getTMarkup()) +
                Integer.parseInt(Flightitems.get(position).getTPartnerCommission()) + Integer.parseInt(Flightitems.get(position).getTSdiscount()) +
                Integer.parseInt(Flightitems.get(position).getTax()) + Integer.parseInt(Flightitems.get(position).getOcTax());
        flight_txt.setText("Price: "+"\u20B9" + Total_price+"\t");
        TextView onward_head = (TextView) convertView.findViewById(R.id.onward_header);
        TextView return_head = (TextView) convertView.findViewById(R.id.return_header);
        String onward_txt = "", onward_place_txt = "", return_txt = "", return_place_txt = "";
        TableLayout tl = (TableLayout) convertView.findViewById(R.id.onward_table);
        tl.removeAllViewsInLayout();
        TableLayout tl_return = (TableLayout) convertView.findViewById(R.id.return_table);
        tl_return.removeAllViewsInLayout();

        for (int i = 0; i < Flightitems.get(position).getOnward_model().size(); i++) {

            TableRow row = new TableRow(activity);
            row = new TableRow(activity);
            row.setId(index);
            index++;
            if (i == 0) {
                String dep_date_time = Flightitems.get(position).getOnward_model().get(i).getDepartureDateTime();
                String dep_date[] = dep_date_time.split("T");
                dep_date[0] = getConvertedDate(dep_date[0]);
                dep_date[1] = getConvertedTime(dep_date[1]);
                onward_txt = "Dep: " + dep_date[0] + " " + dep_date[1];
                onward_place_txt = Flightitems.get(position).getOnward_model().get(i).getDepartureAirportCode();
            }
            if (i == (Flightitems.get(position).getOnward_model().size() - 1)) {

                String arr_date_time = "" + Flightitems.get(position).getOnward_model().get(i).getArrivalDateTime();
                String arr_date[] = arr_date_time.split("T");
                arr_date[0] = getConvertedDate(arr_date[0]);
                arr_date[1] = getConvertedTime(arr_date[1]);
                onward_txt = onward_txt + " Arr: " + arr_date[0] + " " + arr_date[1];
                onward_place_txt = onward_place_txt + " to " + Flightitems.get(position).getOnward_model().get(i).getArrivalAirportCode();
            }
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);
            TextView flight = new TextView(convertView.getContext());
            TextView arrival = new TextView(convertView.getContext());
            TextView departure = new TextView(convertView.getContext());
            //TextView price = new TextView(convertView.getContext());
            String dep_date_time = "" + Flightitems.get(position).getOnward_model().get(i).getDepartureDateTime();
            String dep_date[] = dep_date_time.split("T");
            dep_date[0] = getConvertedDate(dep_date[0]);
            dep_date[1] = getConvertedTime(dep_date[1]);

            String arr_date_time = "" + Flightitems.get(position).getOnward_model().get(i).getArrivalDateTime();
            String arr_date[] = arr_date_time.split("T");
            arr_date[0] = getConvertedDate(arr_date[0]);
            arr_date[1] = getConvertedTime(arr_date[1]);

            flight.setText(spanIt("\t" + Flightitems.get(position).getOnward_model().get(i).getOperatingAirlineName() + "\n\t" + Flightitems.get(position).getOnward_model().get(i).getFlightNumber(), "" + Flightitems.get(position).getOnward_model().get(i).getOperatingAirlineName()));
            arrival.setTextAppearance(activity, R.style.font_size);
            arrival.setText(spanIt("" + Flightitems.get(position).getOnward_model().get(i).getDepartureAirportName() + "\n" + dep_date[0] + "\n" + dep_date[1], "" + Flightitems.get(position).getOnward_model().get(i).getDepartureAirportName()));
            departure.setTextAppearance(activity, R.style.font_size);
            departure.setText(spanIt("" + Flightitems.get(position).getOnward_model().get(i).getArrivalAirportName() + "\n" + arr_date[0] + "\n" + arr_date[1], "" + Flightitems.get(position).getOnward_model().get(i).getArrivalAirportName()));

            /*int Total_price = Integer.parseInt(Flightitems.get(position).getActualBaseFare()) + Integer.parseInt(Flightitems.get(position).getSCharge()) +
                    Integer.parseInt(Flightitems.get(position).getSTax()) + Integer.parseInt(Flightitems.get(position).getTCharge()) +
                    Integer.parseInt(Flightitems.get(position).getTDiscount()) + Integer.parseInt(Flightitems.get(position).getTMarkup()) +
                    Integer.parseInt(Flightitems.get(position).getTPartnerCommission()) + Integer.parseInt(Flightitems.get(position).getTSdiscount()) +
                    Integer.parseInt(Flightitems.get(position).getTax()) + Integer.parseInt(Flightitems.get(position).getOcTax());
            if(i == 0)
            price.setText(""+Total_price);
            else
                price.setText("");*/

            row.addView(flight);
            row.addView(arrival);
            row.addView(departure);

            //row.addView(price);
            tl.addView(row);
        }

            onward_head.setTextAppearance(activity, R.style.font_size);
            onward_head.setText(spanIt("\tOnward - " + onward_place_txt, "Onward"));// + "\n" + onward_txt, "Onward"));



            for (int j = 0; j < Flightitems.get(position).getReturn_model().size(); j++) {

                TableRow r_row = new TableRow(activity);
                r_row = new TableRow(activity);
                r_row.setId(index);
                index++;
                if (j == 0) {
                    String dep_date_time2 = Flightitems.get(position).getReturn_model().get(j).getDepartureDateTime();
                    String dep_date2[] = dep_date_time2.split("T");
                    dep_date2[0] = getConvertedDate(dep_date2[0]);
//                    dep_date2[1]=getConvertedTime(dep_date2[1]);

                    return_txt = "Dep: " + dep_date2[0] + " " + dep_date2[1];
                    return_place_txt = Flightitems.get(position).getReturn_model().get(j).getDepartureAirportCode();
                }
                if (j == (Flightitems.get(position).getReturn_model().size() - 1)) {
                    String arr_date_time3 = Flightitems.get(position).getReturn_model().get(j).getArrivalDateTime();
                    String arr_date3[] = arr_date_time3.split("T");
                    arr_date3[0] = getConvertedDate(arr_date3[0]);
//                    arr_date3[1]=getConvertedTime(arr_date3[1]);
                    return_txt = return_txt + " Arr: " + arr_date3[0] + " " + arr_date3[1];
                    return_place_txt = return_place_txt + " to " + Flightitems.get(position).getReturn_model().get(j).getArrivalAirportCode();
                }
                TableRow.LayoutParams lp1 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                r_row.setLayoutParams(lp1);
                TextView r_flight = new TextView(convertView.getContext());
                TextView r_arrival = new TextView(convertView.getContext());
                TextView r_departure = new TextView(convertView.getContext());
                //TextView r_price = new TextView(convertView.getContext());
                String dep_date_time1 = "" + Flightitems.get(position).getReturn_model().get(j).getDepartureDateTime();
                String dep_date1[] = dep_date_time1.split("T");
                dep_date1[0] = getConvertedDate(dep_date1[0]);
                dep_date1[1]=getConvertedTime(dep_date1[1]);

                String arr_date_time1 = "" + Flightitems.get(position).getReturn_model().get(j).getArrivalDateTime();
                String arr_date1[] = arr_date_time1.split("T");
                arr_date1[0] = getConvertedDate(arr_date1[0]);
                arr_date1[1]=getConvertedTime(arr_date1[1]);


                r_flight.setText(spanIt("\t" + Flightitems.get(position).getReturn_model().get(j).getOperatingAirlineName() + "\n\t" + Flightitems.get(position).getReturn_model().get(j).getFlightNumber(), "" + Flightitems.get(position).getReturn_model().get(j).getOperatingAirlineName()));
 //               r_flight.setText("" + Flightitems.get(position).getReturn_model().get(i).getOperatingAirlineName() + "\n" + Flightitems.get(position).getReturn_model().get(i).getFlightNumber());
                r_arrival.setTextAppearance(activity, R.style.font_size);
               r_arrival.setText(spanIt("" + Flightitems.get(position).getReturn_model().get(j).getDepartureAirportName() + "\n" + dep_date1[0] + "\n" + dep_date1[1], "" + Flightitems.get(position).getReturn_model().get(j).getDepartureAirportName()));
//                r_arrival.setText("" + Flightitems.get(position).getReturn_model().get(i).getDepartureAirportName() + "\n" + dep_date[0] + "\n" + dep_date[1]);
                r_departure.setTextAppearance(activity, R.style.font_size);
                r_departure.setText(spanIt("" + Flightitems.get(position).getReturn_model().get(j).getArrivalAirportName() + "\n" + arr_date1[0] + "\n" + arr_date1[1], "" + Flightitems.get(position).getReturn_model().get(j).getArrivalAirportName()));
//                r_departure.setText("" + Flightitems.get(position).getReturn_model().get(i).getArrivalAirportName() + "\n" + arr_date[0] + "\n" + arr_date[1]);
            /*int Total_price = Integer.parseInt(Flightitems.get(position).getActualBaseFare()) + Integer.parseInt(Flightitems.get(position).getSCharge()) +
                    Integer.parseInt(Flightitems.get(position).getSTax()) + Integer.parseInt(Flightitems.get(position).getTCharge()) +
                    Integer.parseInt(Flightitems.get(position).getTDiscount()) + Integer.parseInt(Flightitems.get(position).getTMarkup()) +
                    Integer.parseInt(Flightitems.get(position).getTPartnerCommission()) + Integer.parseInt(Flightitems.get(position).getTSdiscount()) +
                    Integer.parseInt(Flightitems.get(position).getTax()) + Integer.parseInt(Flightitems.get(position).getOcTax());
            if(i == 0)
                r_price.setText(""+Total_price);
            else
                r_price.setText("");*/






                r_row.addView(r_flight);
                r_row.addView(r_arrival);
                r_row.addView(r_departure);
//                r_row.addView(v);
                //r_row.addView(r_price);
                tl_return.addView(r_row);
            }


            return_head.setTextAppearance(activity, R.style.font_size);
            return_head.setText(spanIt("\tReturn - " + return_place_txt,"Return"));// + "\n" + return_txt, "Return"));

            Button select_btn = (Button) convertView.findViewById(R.id.btn_select);
            select_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("ButtonClicked", "" + position);
                    SharedPreferences sharedpreferences = activity.getSharedPreferences("Itinerary", Context.MODE_PRIVATE);
                    final SharedPreferences.Editor editor = sharedpreferences.edit();
                    int ActualPrice = Integer.parseInt(Flightitems.get(position).getActualBaseFare()) * 2;
                    editor.putString("FlightPrice", "" + ActualPrice);
                    //JSON Value.
                    String flight_price = Flightitems.get(position).getActualBaseFare() + "," + Flightitems.get(position).getTax() + "," + Flightitems.get(position).getSTax() + "," + Flightitems.get(position).getTCharge() + "," + Flightitems.get(position).getSCharge() + "," + Flightitems.get(position).getTDiscount() + "," + Flightitems.get(position).getTMarkup() + "," + Flightitems.get(position).getTPartnerCommission() + "," + Flightitems.get(position).getTSdiscount() + "," + Flightitems.get(position).getOcTax() + "," + Flightitems.get(position).getId() + "," + Flightitems.get(position).getKey();
                    editor.putString("InternationalFlightPrice", "" + flight_price);
                    String flight_onward_data= "";
                    for(int i = 0; i< Flightitems.get(position).getOnward_model().size(); i++ ){
                        String flight_onward = Flightitems.get(position).getOnward_model().get(i).getAirEquipType() + "," + Flightitems.get(position).getOnward_model().get(i).getArrivalAirportCode() + "," + Flightitems.get(position).getOnward_model().get(i).getArrivalAirportName() + "," + Flightitems.get(position).getOnward_model().get(i).getArrivalDateTime() + "," + Flightitems.get(position).getOnward_model().get(i).getDepartureAirportCode() + "," + Flightitems.get(position).getOnward_model().get(i).getDepartureAirportName() + "," + Flightitems.get(position).getOnward_model().get(i).getDepartureDateTime() + "," + Flightitems.get(position).getOnward_model().get(i).getFlightNumber() + "," + Flightitems.get(position).getOnward_model().get(i).getMarketingAirlineCode() + "," + Flightitems.get(position).getOnward_model().get(i).getOperatingAirlineCode() + "," + Flightitems.get(position).getOnward_model().get(i).getOperatingAirlineName() + "," + Flightitems.get(position).getOnward_model().get(i).getOperatingAirlineFlightNumber() + "," + Flightitems.get(position).getOnward_model().get(i).getNumStops() + "," + Flightitems.get(position).getOnward_model().get(i).getLinkSellAgrmnt() + "," + Flightitems.get(position).getOnward_model().get(i).getConx() + "," + Flightitems.get(position).getOnward_model().get(i).getAirpChg() + "," + Flightitems.get(position).getOnward_model().get(i).getInsideAvailOption() + "," + Flightitems.get(position).getOnward_model().get(i).getGenTrafRestriction() + "," + Flightitems.get(position).getOnward_model().get(i).getDaysOperates() + "," + Flightitems.get(position).getOnward_model().get(i).getJrnyTm() + "," + Flightitems.get(position).getOnward_model().get(i).getEndDt() + "," + Flightitems.get(position).getOnward_model().get(i).getStartTerminal() + "," + Flightitems.get(position).getOnward_model().get(i).getEndTerminal();
                        if(i == 0){
                           flight_onward_data = flight_onward;
                        }else{
                           flight_onward_data = flight_onward_data + "-" +flight_onward;
                        }

                    }
                    editor.putString("InternationalFlightOnwardDetails", "" + flight_price);
                    String flight_return_data= "";
                    for(int i = 0; i< Flightitems.get(position).getReturn_model().size(); i++ ){
                        String flight_return = Flightitems.get(position).getReturn_model().get(i).getAirEquipType() + "," + Flightitems.get(position).getReturn_model().get(i).getArrivalAirportCode() + "," + Flightitems.get(position).getReturn_model().get(i).getArrivalAirportName() + "," + Flightitems.get(position).getReturn_model().get(i).getArrivalDateTime() + "," + Flightitems.get(position).getReturn_model().get(i).getDepartureAirportCode() + "," + Flightitems.get(position).getReturn_model().get(i).getDepartureAirportName() + "," + Flightitems.get(position).getReturn_model().get(i).getDepartureDateTime() + "," + Flightitems.get(position).getReturn_model().get(i).getFlightNumber() + "," + Flightitems.get(position).getReturn_model().get(i).getMarketingAirlineCode() + "," + Flightitems.get(position).getReturn_model().get(i).getOperatingAirlineCode() + "," + Flightitems.get(position).getReturn_model().get(i).getOperatingAirlineName() + "," + Flightitems.get(position).getReturn_model().get(i).getOperatingAirlineFlightNumber() + "," + Flightitems.get(position).getReturn_model().get(i).getNumStops() + "," + Flightitems.get(position).getReturn_model().get(i).getLinkSellAgrmnt() + "," + Flightitems.get(position).getReturn_model().get(i).getConx() + "," + Flightitems.get(position).getReturn_model().get(i).getAirpChg() + "," + Flightitems.get(position).getReturn_model().get(i).getInsideAvailOption() + "," + Flightitems.get(position).getReturn_model().get(i).getGenTrafRestriction() + "," + Flightitems.get(position).getReturn_model().get(i).getDaysOperates() + "," + Flightitems.get(position).getReturn_model().get(i).getJrnyTm() + "," + Flightitems.get(position).getReturn_model().get(i).getEndDt() + "," + Flightitems.get(position).getReturn_model().get(i).getStartTerminal() + "," + Flightitems.get(position).getReturn_model().get(i).getEndTerminal();
                        if(i == 0){
                            flight_return_data = flight_return;
                        }else{
                            flight_return_data = flight_return_data + "-" +flight_return;
                        }

                    }
                    editor.putString("InternationalFlightReturnDetails", "" + flight_price);
                    editor.commit();
                    Intent intent = new Intent(activity, ItinerarySummaryActivity.class);
                    activity.startActivity(intent);


                }
            });

            // getting data for the row
            final FlightModel m = Flightitems.get(position);
            //setListViewHeightBasedOnChildren(DragAndSort.listview);
            // title
            //  title.setText(m.getPlace());
            //  days.setText(m.getNights());



        return convertView;
    }
}
