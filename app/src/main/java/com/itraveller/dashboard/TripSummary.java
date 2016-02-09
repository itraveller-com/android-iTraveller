package com.itraveller.dashboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.itraveller.R;
import com.itraveller.dashboard.MyTravelActivity;
import com.itraveller.model.MyTravelModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rohan bundelkhandi on 05/02/2016.
 */
public class TripSummary extends AppCompatActivity {

    TextView nameText, placesText, destinationText, arr_dateText, dep_dateText, daysText, adultsText, child_5_12_Text, child_below_5_Text;
    TextView bookingDateText,bookingModeText, arrAtText, dateDisplayText, roomDisplayText, totalPriceText;
    TextView discountPriceText, priceAdvanceText, remainingPriceText, departureText, transportationText,emailText,TravelIDText;

    Bundle bundle;
    int selected_position;
    Toolbar mToolbar;

    BookedTripsFragment bookedTripsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.trip_summary);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().show();
        getSupportActionBar().setTitle("Your Trip Details");

        bookedTripsFragment=new BookedTripsFragment();

        Log.d("List size", "" + BookedTripsFragment.upcomingList.size());

        TravelIDText=(TextView) findViewById(R.id.travel_id_value);
        nameText=(TextView) findViewById(R.id.name_value);
        emailText=(TextView) findViewById(R.id.email_value);
        placesText=(TextView) findViewById(R.id.places_value);
        destinationText=(TextView) findViewById(R.id.destinations_value);
        bookingDateText=(TextView) findViewById(R.id.date_of_booking_value);
        arr_dateText=(TextView) findViewById(R.id.date_of_arrival_value);
        dep_dateText=(TextView) findViewById(R.id.date_of_departure_value);
        daysText=(TextView) findViewById(R.id.no_of_days_value);
        adultsText=(TextView) findViewById(R.id.no_of_adults_value);
        child_5_12_Text=(TextView) findViewById(R.id.no_of_children_5_12_value);
        child_below_5_Text=(TextView) findViewById(R.id.no_of_children_below_5_value);
        totalPriceText=(TextView) findViewById(R.id.total_price_value);
        discountPriceText=(TextView) findViewById(R.id.disount_value);
        remainingPriceText=(TextView) findViewById(R.id.price_after_discount_value);
        priceAdvanceText=(TextView) findViewById(R.id.booking_advance_value);
        bookingModeText=(TextView) findViewById(R.id.booking_mode_value);
        arrAtText=(TextView) findViewById(R.id.arrival_at_value);
        dateDisplayText=(TextView) findViewById(R.id.date_of_arrival_display);
        roomDisplayText=(TextView) findViewById(R.id.room_type_display);
        departureText=(TextView) findViewById(R.id.departure_from_text_value);
        transportationText=(TextView) findViewById(R.id.transportation_text_value);

        bundle=getIntent().getExtras();

        selected_position=bundle.getInt("ItineraryData");

        TravelIDText.setText(""+BookedTripsFragment.upcomingList.get(selected_position).getItinerary_Main_Id());
        nameText.setText("" + BookedTripsFragment.upcomingList.get(selected_position).getCustomer_Name());
        emailText.setText("" + BookedTripsFragment.upcomingList.get(selected_position).getCustomer_Email());
        placesText.setText(""+BookedTripsFragment.upcomingList.get(selected_position).getRegion_Name());

        bookingDateText.setText(""+BookedTripsFragment.upcomingList.get(selected_position).getBooking_Date());
        arr_dateText.setText(""+BookedTripsFragment.upcomingList.get(selected_position).getTravelDate());

        daysText.setText(""+BookedTripsFragment.upcomingList.get(selected_position).getNo_of_Days());

        adultsText.setText(""+BookedTripsFragment.upcomingList.get(selected_position).getNo_of_Adults());
        child_5_12_Text.setText(""+BookedTripsFragment.upcomingList.get(selected_position).getNo_of_Child());
        child_below_5_Text.setText(""+BookedTripsFragment.upcomingList.get(selected_position).getNo_of_Infant());

        totalPriceText.setText(""+BookedTripsFragment.upcomingList.get(selected_position).getPackageValue());
    }
}