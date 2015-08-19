package com.itraveller.activity;

/**
 * Created by VNK on 6/11/2015.
 */

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;


import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.itraveller.R;
import com.itraveller.constant.Utility;
import com.itraveller.dragsort.DragAndSort;
import com.itraveller.volley.AppController;


public class PlanTrip extends ActionBarActivity implements OnClickListener {

    Toolbar mToolbar;// Declaring the Toolbar Object
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    Context context;
    ImageButton adult_plus, adult_minus, children_plus, children_minus, child_plus, child_minus, bady_plus, bady_minus;
    Button adult_btn, children_btn, child_btn, baby_btn;
    int var_adult = 2, var_children = 0, var_child = 0, var_baby = 0;
    private int myear;
    private int mmonth;
    private int mday;
    Date d;
    Button travelDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_your_trip_test);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Travel Data");

        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        NetworkImageView img = (NetworkImageView) findViewById(R.id.thumbnail);
        TextView title_sp = (TextView) findViewById(R.id.title_rp);
        TextView duration_sp = (TextView) findViewById(R.id.duration_rp);
        Bundle bundle = getIntent().getExtras();
        img.setImageUrl(bundle.getString("Image"), imageLoader);
        duration_sp.setText((bundle.getInt("Duration") - 1) + " Nights / " + bundle.getInt("Duration") + " Days");
        title_sp.setText(bundle.getString("Title"));

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

        //Calander
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        adult_plus = (ImageButton) findViewById(R.id.adultplus);
        adult_plus.setOnClickListener(this);
        adult_minus = (ImageButton) findViewById(R.id.adultminus);
        adult_minus.setOnClickListener(this);
        children_plus = (ImageButton) findViewById(R.id.childremplus);
        children_plus.setOnClickListener(this);
        children_minus = (ImageButton) findViewById(R.id.childrenminus);
        children_minus.setOnClickListener(this);
        child_plus = (ImageButton) findViewById(R.id.childplus);
        child_plus.setOnClickListener(this);
        child_minus = (ImageButton) findViewById(R.id.childminus);
        child_minus.setOnClickListener(this);
        bady_plus = (ImageButton) findViewById(R.id.babyplus);
        bady_plus.setOnClickListener(this);
        bady_minus = (ImageButton) findViewById(R.id.babyminus);
        bady_minus.setOnClickListener(this);

        adult_btn = (Button) findViewById(R.id.adultbtn);
        children_btn = (Button) findViewById(R.id.childrenbtn);
        child_btn = (Button) findViewById(R.id.childbtn);
        baby_btn = (Button) findViewById(R.id.babybtn);


        Button addDestination = (Button) findViewById(R.id.addbtnfilter);
        addDestination.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedpreferences = getSharedPreferences("Itinerary", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString("RegionID", "" + region_id);

                //editor.putString("DestinationID", destination_value_id);
                //editor.putString("DestinationCount", destination_value_count);
                editor.putString("Adults", adult_btn.getText().toString());
                editor.putString("Children_12_5", children_btn.getText().toString());
                editor.putString("Children_5_2", child_btn.getText().toString());
                editor.putString("Children_2_0", baby_btn.getText().toString());
                editor.putInt("ItineraryID", itinerary_id);
                editor.putInt("Duration", duration);
                DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                try{
                    d = df.parse(travelDate.getText().toString());
                }
                catch(Exception e){

                }

                //Log.i("StartDate", "Date :" + Utility.addDays(travelDate.getText().toString(), duration - 1,"dd-MM-yyyy", "yyyy-MM-dd"));
                Log.i("StartDate", "Date :" + Utility.addDays(travelDate.getText().toString(), 0,"dd-MM-yyyy", "dd-MM-yyyy"));

                editor.putString("DefaultDate", "" + Utility.addDays(travelDate.getText().toString(), 0,"dd-MM-yyyy", "dd-MM-yyyy"));
                editor.putString("TravelDate", "" + Utility.addDays(travelDate.getText().toString(), 0, "dd-MM-yyyy","yyyy-MM-dd"));
                //editor.putString("EndDate", Utility.addDays(travelDate.getText().toString(), duration - 1,"dd-MM-yyyy","yyyy-MM-dd"));

                editor.commit();

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
            }
        });
        travelDate = (Button) findViewById(R.id.travel_date);
        travelDate.setText(mDay + "-" + (mMonth + 1) + "-" + mYear);
        travelDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DialogFragment picker = new DatePicker();
                picker.show(PlanTrip.this.getFragmentManager(), "datePicker");
            }
        });
    }

    @Override
    public void onClick(View view) {
        Log.i("ClickTest", "" + view);
        if (view == adult_plus) {
            var_adult++;
            adult_btn.setText("" + var_adult);
        } else if (view == adult_minus) {
            if (var_adult != 0)
                var_adult--;
            adult_btn.setText("" + var_adult);
        } else if (view == children_plus) {
            var_children++;
            children_btn.setText("" + var_children);
        } else if (view == children_minus) {
            if (var_children != 0)
                var_children--;
            children_btn.setText("" + var_children);
        } else if (view == child_plus) {
            var_child++;
            child_btn.setText("" + var_child);
        } else if (view == child_minus) {
            if (var_child != 0)
                var_child--;
            child_btn.setText("" + var_child);
        } else if (view == bady_plus) {
            var_baby++;
            baby_btn.setText("" + var_baby);
        } else if (view == bady_minus) {
            if (var_baby != 0)
                var_baby--;
            baby_btn.setText("" + var_baby);
        }


    }


   /* @SuppressLint("ValidFragment")
    public class DatePickerFrom extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final Calendar c = Calendar.getInstance();
            myear = c.get(Calendar.YEAR);
            mmonth = c.get(Calendar.MONTH);
            mday = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog _date = new DatePickerDialog(getActivity(), this,
                    myear, mmonth, mday) {
                public void onDateChanged(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                    if (year < myear)
                        view.updateDate(myear, mmonth, mday);

                    if (monthOfYear < mmonth && year == myear)
                        view.updateDate(myear, mmonth, mday);

                    if (dayOfMonth < mday && year == myear
                            && monthOfYear == mmonth)
                        view.updateDate(myear, mmonth, mday);
                }
            };
            return _date;
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            myear = yy;
            mmonth = mm;
            mday = dd;
            travelDate.setText(dd + "-" + (mm + 1) + "-" + yy);
        }

    }*/

    // ***************************************************************************************************//
    // *********************************************DATE PICKER*******************************************//
    // ***************************************************************************************************//

    @SuppressLint("ValidFragment")
    public class DatePicker extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final Calendar c = Calendar.getInstance();
            myear = c.get(Calendar.YEAR);
            mmonth = c.get(Calendar.MONTH);
            mday = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog _date = new DatePickerDialog(getActivity(), this,
                    myear, mmonth, mday) {
                public void onDateChanged(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                    if (year < myear)
                        updateDate(myear, mmonth, mday);

                    if (monthOfYear < mmonth && year == myear)
                        updateDate(myear, mmonth, mday);

                    if (dayOfMonth < mday && year == myear
                            && monthOfYear == mmonth)
                        updateDate(myear, mmonth, mday);
                }
            };
            final Calendar cc = Calendar.getInstance();
            cc.get(Calendar.YEAR);
            cc.get(Calendar.MONTH);
            cc.get(Calendar.DAY_OF_MONTH);
            return _date;
        }

        @Override
        public void onDateSet(android.widget.DatePicker datePicker, int yy, int mm, int dd) {
            myear = yy;
            mmonth = mm;
            mday = dd;
            travelDate.setText(dd + "-" + (mm + 1) + "-" + yy);
        }
    }

    public void onBackPressed() {
        finish();
    }
}

