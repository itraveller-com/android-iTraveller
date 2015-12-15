package com.itraveller.activity;

/**
 * Created by VNK on 6/11/2015.
 */

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.itraveller.R;
import com.itraveller.constant.Utility;
import com.itraveller.dragsort.DragAndSort;
import com.itraveller.volley.AppController;
import com.squareup.timessquare.CalendarPickerView;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class PlanTrip extends ActionBarActivity{

    String date_str;
    SharedPreferences prefs;
    SharedPreferences preferences;
    Toolbar mToolbar;// Declaring the Toolbar Object
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    Context context;
    TextView adult_, child_, infants_;
    int var_adult = 2, var_child = 0, var_infants = 0;
    Date d;
    TextView travelDate;
    Calendar nextYear;
    int count = 0;
    SharedPreferences post_prefs;
    LinearLayout paxLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_your_trip_test2);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        String fontPath = "fonts/ProximaNova-Bold.otf";
        // Loading Font Face
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);

        getSupportActionBar().setTitle("Travel Data");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        post_prefs = getSharedPreferences("PostDate", MODE_PRIVATE);

        NetworkImageView img = (NetworkImageView) findViewById(R.id.thumbnail);
        TextView title_sp = (TextView) findViewById(R.id.title_rp);
        TextView duration_sp = (TextView) findViewById(R.id.duration_rp);
        Bundle bundle = getIntent().getExtras();
        img.setImageUrl(bundle.getString("Image"), imageLoader);
        img.setErrorImageResId(R.drawable.default_img);
        duration_sp.setText((bundle.getInt("Duration") - 1) + " Nights / " + bundle.getInt("Duration") + " Days");
        title_sp.setText(bundle.getString("Title"));

        // Applying font
        //title_sp.setTypeface(tf);
        //duration_sp.setTypeface(tf);

        final String imageurl = bundle.getString("Image");
        final int duration = bundle.getInt("Duration");
        final String title = bundle.getString("Title");
        final int region_id = bundle.getInt("RegionID");
        final String destination_value = bundle.getString("Destinations");
        final String destination_value_id = bundle.getString("DestinationsID");
        final String destination_value_count = bundle.getString("DestinationsCount");
        final int arrival_port = bundle.getInt("ArrivalPort");
        final int dep_port = bundle.getInt("DeparturePort");
        final int itinerary_id = bundle.getInt("ItineraryID");
        final String regionString = bundle.getString("RegionString");

        preferences = getSharedPreferences("Preferences", MODE_PRIVATE);

        prefs = getSharedPreferences("Itinerary", MODE_PRIVATE);
        //Calander
        Calendar c = Calendar.getInstance();
        if (("" + prefs.getString("FlightBit", null)).equals("1"))
            c.add(Calendar.DAY_OF_YEAR, 1);
        else
            c.add(Calendar.DAY_OF_YEAR, 7);

        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);


        paxLayout = (LinearLayout) findViewById(R.id.pax_layout);
        paxLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                PaxView();
            }
        });

        adult_ = (TextView) findViewById(R.id.txt_adult);
        child_ = (TextView) findViewById(R.id.txt_child);
        infants_ = (TextView) findViewById(R.id.txt_infants);

        Button addDestination = (Button) findViewById(R.id.addbtnfilter);
        addDestination.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedpreferences = getSharedPreferences("Itinerary", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString("RegionID", "" + region_id);
                editor.putString("RegionString", "" + regionString);
                Log.v("RegionString",""+regionString);

                //editor.putString("DestinationID", destination_value_id);
                //editor.putString("DestinationCount", destination_value_count);
                editor.putString("Adult", adult_.getText().toString());
                editor.putString("Child", child_.getText().toString());
                editor.putString("Infants", infants_.getText().toString());
                editor.putInt("ItineraryID", itinerary_id);
                editor.putInt("Duration", duration);
                DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    d = df.parse(travelDate.getText().toString());
                } catch (Exception e) {

                }

                Log.i("StartDate", "Date :" + Utility.addDays(travelDate.getText().toString(), duration - 1, "dd-MM-yyyy", "yyyy-MM-dd"));
                Log.i("StartDate", "Date :" + Utility.addDays(travelDate.getText().toString(), 0, "dd-MM-yyyy", "dd-MM-yyyy"));

                editor.putString("DefaultDate", "" + Utility.addDays(travelDate.getText().toString(), 0, "dd-MM-yyyy", "dd-MM-yyyy"));
                editor.putString("TravelDate", "" + Utility.addDays(travelDate.getText().toString(), 0, "dd-MM-yyyy", "yyyy-MM-dd"));
                editor.putString("EndDate", Utility.addDays(travelDate.getText().toString(), duration - 1, "dd-MM-yyyy", "yyyy-MM-dd"));

                editor.commit();


                if ((Integer.parseInt(adult_.getText().toString()) != 0)) {
                    final Intent i = new Intent(PlanTrip.this, DragAndSort.class);
                    i.putExtra("Image", imageurl);
                    i.putExtra("Duration", duration);
                    i.putExtra("Title", title);
                    i.putExtra("Destinations", destination_value);
                    i.putExtra("DestinationsID", destination_value_id);
                    i.putExtra("DestinationsCount", destination_value_count);
                    i.putExtra("ArrivalPort", arrival_port);
                    i.putExtra("DeparturePort", dep_port);
                    i.putExtra("RegionID", region_id);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "Atleast One adult should be there", Toast.LENGTH_LONG).show();
                }
            }
        });

        travelDate = (TextView) findViewById(R.id.travel_date);
        travelDate.setText(mDay + "-" + (mMonth + 1) + "-" + mYear);
        //travelDate.setTypeface(tf);

        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        String weekday = new DateFormatSymbols().getShortWeekdays()[dayOfWeek];
        Log.d("Day of week test", "" + weekday);

        date_str = weekday + "-" + mDay + "-" + (mMonth + 1) + "-" + mYear;


        Log.d("Date str=", "" + date_str);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Date_str", "" + date_str);
        editor.commit();


        travelDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

        /*        final Dialog dialog = new Dialog(PlanTrip.this);
                dialog.setContentView(R.layout.time_square_calendar);
                dialog.setTitle("Select Date...");

                final CalendarPickerView calendar = (CalendarPickerView) dialog.findViewById(R.id.calendar_view_);
                final Date today = new Date();
                Log.d("Today", "" + calendar);
                calendar.init(today, nextYear.getTime())
                        .withSelectedDate(today);
                //            .inMode(CalendarPickerView.SelectionMode.RANGE);

                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                Button cancelButton=(Button) dialog.findViewById(R.id.dialogButtonCancel);

                cancelButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String date_=""+calendar.getSelectedDate();
                        String date_arr[]=date_.split(" ");
                        int day=Integer.parseInt(date_arr[2]);
                        String day_str=String.valueOf(day);
                        int month = 0;
                        switch(date_arr[1])
                        {
                            case "Jan":
                                month=1;
                                break;
                            case "Feb":
                                month=2;
                                break;
                            case "Mar":
                                month=3;
                                break;
                            case "Apr":
                                month=4;
                                break;
                            case "May":
                                month=5;
                                break;
                            case "Jun":
                                month=6;
                                break;
                            case "Jul":
                                month=7;
                                break;
                            case "Aug":
                                month=8;
                                break;
                            case "Sep":
                                month=9;
                                break;
                            case "Oct":
                                month=10;
                                break;
                            case "Nov":
                                month=11;
                                break;
                            case "Dec":
                                month=12;
                                break;
                        }
                        String month_str=String.valueOf(month);

                        travelDate.setText(day_str+"-"+month_str+"-"+date_arr[5]);
                        dialog.dismiss();
                    }
                });

                dialog.show();


         */
                if (count == 0) {
                    function1();
                    count++;
                } else
                    function2();


            }
        });
    }


    public void function1() {
        final Dialog dialog = new Dialog(PlanTrip.this);
        dialog.setContentView(R.layout.time_square_calendar);
        dialog.setTitle("Select Date...");

        final CalendarPickerView calendar = (CalendarPickerView) dialog.findViewById(R.id.calendar_view_);
        final Date today = new Date();
        Log.d("Today", "" + today);
        Date temp;
        if (("" + prefs.getString("FlightBit", null)).equals("1"))
            temp = new Date(today.getTime() + 86400000L);
        else
            temp = new Date(today.getTime() + 604800000L);

        calendar.init(temp, nextYear.getTime())
                .withSelectedDate(temp);
        //            .inMode(CalendarPickerView.SelectionMode.RANGE);

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        Button cancelButton = (Button) dialog.findViewById(R.id.dialogButtonCancel);

        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String date_ = "" + calendar.getSelectedDate();

                String date_array[] = date_.split(" ");

                Log.d("Date format test", "" + date_);

                String date_arr[] = date_.split(" ");
                int day = Integer.parseInt(date_arr[2]);
                String day_str = String.valueOf(day);
                int month = 0;
                switch (date_arr[1]) {
                    case "Jan":
                        month = 1;
                        break;
                    case "Feb":
                        month = 2;
                        break;
                    case "Mar":
                        month = 3;
                        break;
                    case "Apr":
                        month = 4;
                        break;
                    case "May":
                        month = 5;
                        break;
                    case "Jun":
                        month = 6;
                        break;
                    case "Jul":
                        month = 7;
                        break;
                    case "Aug":
                        month = 8;
                        break;
                    case "Sep":
                        month = 9;
                        break;
                    case "Oct":
                        month = 10;
                        break;
                    case "Nov":
                        month = 11;
                        break;
                    case "Dec":
                        month = 12;
                        break;
                }
                String month_str = String.valueOf(month);
                date_str = date_array[0] + "-" + day_str + "-" + month_str + "-" + date_arr[5];

                Log.d("Date str=", "" + date_str);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("Date_str", "" + date_str);
                editor.commit();


                travelDate.setText(day_str + "-" + month_str + "-" + date_arr[5]);
                dialog.dismiss();
            }
        });

        dialog.show();


    }


    public Date getDate(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String dateInString = str;
        Date date = null;
        try {
            date = sdf.parse(dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("Date is", "" + date);
        return date;
    }

    public void function2() {


        final Dialog dialog = new Dialog(PlanTrip.this);
        dialog.setContentView(R.layout.time_square_calendar);
        dialog.setTitle("Select Date...");

        final CalendarPickerView calendar = (CalendarPickerView) dialog.findViewById(R.id.calendar_view_);
        final Date today = new Date();

        String str = travelDate.getText().toString();
        Log.d("Today2", "" + str);
        Date temp = getDate(str);

        Date temp1;

        if (("" + prefs.getString("FlightBit", null)).equals("1"))
            temp1 = new Date(today.getTime() + 86400000L);
        else
            temp1 = new Date(today.getTime() + 604800000L);


        Log.d("Today1", "" + calendar);
        calendar.init(temp1, nextYear.getTime())
                .withSelectedDate(temp);
        //            .inMode(CalendarPickerView.SelectionMode.RANGE);

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        Button cancelButton = (Button) dialog.findViewById(R.id.dialogButtonCancel);

        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String date_ = "" + calendar.getSelectedDate();

                String date_array[] = date_.split(" ");

                String date_arr[] = date_.split(" ");
                int day = Integer.parseInt(date_arr[2]);
                String day_str = String.valueOf(day);
                int month = 0;
                switch (date_arr[1]) {
                    case "Jan":
                        month = 1;
                        break;
                    case "Feb":
                        month = 2;
                        break;
                    case "Mar":
                        month = 3;
                        break;
                    case "Apr":
                        month = 4;
                        break;
                    case "May":
                        month = 5;
                        break;
                    case "Jun":
                        month = 6;
                        break;
                    case "Jul":
                        month = 7;
                        break;
                    case "Aug":
                        month = 8;
                        break;
                    case "Sep":
                        month = 9;
                        break;
                    case "Oct":
                        month = 10;
                        break;
                    case "Nov":
                        month = 11;
                        break;
                    case "Dec":
                        month = 12;
                        break;
                }
                String month_str = String.valueOf(month);
                date_str = date_array[0] + "-" + day_str + "-" + month_str + "-" + date_arr[5];
                travelDate.setText(day_str + "-" + month_str + "-" + date_arr[5]);

                Log.d("Date str=", "" + date_str);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("Date_str", "" + date_str);
                editor.commit();


                dialog.dismiss();
            }
        });

        dialog.show();

    }

    /*@Override
    public void onClick(View view) {
        Log.i("ClickTest", "" + view);
        if (view == adult_plus) {
            if (var_adult < 50)
                var_adult++;
            adult_btn.setText("" + var_adult);
        } else if (view == adult_minus) {
            if (var_adult != 0)
                var_adult--;
            adult_btn.setText("" + var_adult);
        } else if (view == children_plus) {
            if (var_children < 50)
                var_children++;
            children_btn.setText("" + var_children);
        } else if (view == children_minus) {
            if (var_children != 0)
                var_children--;
            children_btn.setText("" + var_children);
        } else if (view == child_plus) {
            if (var_child < 20)
                var_child++;
            child_btn.setText("" + var_child);
        } else if (view == child_minus) {
            if (var_child != 0)
                var_child--;
            child_btn.setText("" + var_child);
        } else if (view == bady_plus) {
            if (var_baby < 20)
                var_baby++;
            baby_btn.setText("" + var_baby);
        } else if (view == bady_minus) {
            if (var_baby != 0)
                var_baby--;
            baby_btn.setText("" + var_baby);
        }
    }*/


    public void onBackPressed() {
        finish();
    }

    public void PaxView() {
        final Dialog dialog;
        // create a Dialog component
        dialog = new Dialog(this);
        // tell the Dialog to use the dialog.xml as it's layout description
        dialog.setTitle("Flight Details");
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.traveldatapopup);
        LinearLayout plusAdult = (LinearLayout) dialog.findViewById(R.id.btn_plus_adults);
        LinearLayout plusChild = (LinearLayout) dialog.findViewById(R.id.btn_plus_child);
        LinearLayout plusInfants = (LinearLayout) dialog.findViewById(R.id.btn_plus_infants);

        LinearLayout minusAdult = (LinearLayout) dialog.findViewById(R.id.btn_minus_adult);
        LinearLayout minusChild = (LinearLayout) dialog.findViewById(R.id.btn_minus_child);
        LinearLayout minusInfants = (LinearLayout) dialog.findViewById(R.id.btn_minus_infants);

        final TextView txtAdult = (TextView) dialog.findViewById(R.id.txt_adults);
        final TextView txtChild = (TextView) dialog.findViewById(R.id.txt_child);
        final TextView txtInfants = (TextView) dialog.findViewById(R.id.txt_infants);

        Button btnDone = (Button) dialog.findViewById(R.id.btn_done);
        Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);

        txtAdult.setText(adult_.getText());
        txtChild.setText(child_.getText());
        txtInfants.setText(infants_.getText());

        btnDone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                adult_.setText(""+var_adult);
                child_.setText(""+var_child);
                infants_.setText(""+var_infants);
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        plusAdult.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                var_adult++;
                txtAdult.setText(""+var_adult);
            }
        });
        plusChild.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                var_child++;
                txtChild.setText(""+var_child);
            }
        });
        plusInfants.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                var_infants++;
                txtInfants.setText(""+var_infants);
            }
        });

        minusAdult.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(var_adult !=1){
                    var_adult--;
                    txtAdult.setText(""+var_adult);
                }
            }
        });
        minusChild.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(var_child != 0){
                    var_child--;
                    txtChild.setText(""+var_child);
                }
            }
        });
        minusInfants.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(var_infants != 0){
                    var_infants--;
                    txtInfants.setText(""+var_infants);
                }
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}

