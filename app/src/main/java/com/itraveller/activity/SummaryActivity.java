package com.itraveller.activity;

/**
 * Created by VNK on 6/11/2015.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

import com.ebs.android.sdk.Config;
import com.ebs.android.sdk.EBSPayment;
import com.ebs.android.sdk.PaymentRequest;
import com.itraveller.R;
import com.itraveller.constant.Utility;
import com.itraveller.payment.BuyProduct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.itraveller.R.id.btn_confirm_payment;
import static com.itraveller.R.id.total_price;


public class SummaryActivity extends ActionBarActivity{
/* When using Appcombat support library
   you need to extend Main Activity to ActionBarActivity.*/

    private Toolbar mToolbar; // Declaring the Toolbar Object
    String onward_flight_rate="";
    String return_flight_rate="";
    int flight_rate = 0;
    int total_price =0;
    Double total_discount;


    int totalPersons;
    //Button btn_buy;
    Double amount;
    //EditText ed_quantity, ed_totalamount;

    private static String HOST_NAME = "";
    CoordinatorLayout coordinatorLayout;
	/*For Live*/
    //private static final int ACC_ID = 5128; // Provided by EBS
    //private static final String SECRET_KEY = "ebskey2";

    //private static final int ACC_ID = 5087; // Provided by EBS
    //private static final String SECRET_KEY = "fcfdfb899ccf83461ffffcc7ab8fa3bd";

    /*For Demo*/
    private static final int ACC_ID = 13872; // Provided by EBS
    private static final String SECRET_KEY = "601615509525046666e247f18b8ceb51";

    private static final int PER_UNIT_PRICE = 1;
    double totalamount;

    String user_name,user_mobile_number,user_email,user_postal_code;


    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.payment_billing);
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle("Price Summary");

            //getSupportActionBar().setDisplayShowHomeEnabled(true);
            //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            HOST_NAME = getResources().getString(R.string.hostname);


            //mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });


            final EditText nameEditText=(EditText) findViewById(R.id.customer_name);
            final EditText mobileEditText=(EditText) findViewById(R.id.customer_mobile_number);
            final EditText emailEditText=(EditText) findViewById(R.id.customer_email);
            final EditText postalEdiText=(EditText) findViewById(R.id.customer_postal_code);
            coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        Log.d("Summary", "" + coordinatorLayout);


        SharedPreferences preferences = getSharedPreferences("Preferences",MODE_PRIVATE);
        nameEditText.setText("" + preferences.getString("name", ""));
        mobileEditText.setText("" + preferences.getString("phone", ""));
        emailEditText.setText("" + preferences.getString("email", ""));

        SharedPreferences prefs1=getSharedPreferences("Itinerary",Context.MODE_PRIVATE);

        totalPersons = Integer.parseInt(prefs1.getString("Adult", "2")) + Integer.parseInt( prefs1.getString("Child", "0"));

        Log.d("Price Adult",""+prefs1.getString("Adult", "2")+" "+prefs1.getString("Child", "0"));

        Button confirm = (Button) findViewById(R.id.btn_confirm_payment);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    user_name=nameEditText.getText().toString();
                    user_mobile_number=mobileEditText.getText().toString();
                    user_email=emailEditText.getText().toString();
                    user_postal_code=postalEdiText.getText().toString();

                    if(!Utility.isNetworkConnected(getApplication())){
                        RetryInternet();
                    } else if(user_email.equalsIgnoreCase("") || user_name.equalsIgnoreCase("") || user_mobile_number.equalsIgnoreCase("") || user_postal_code.equalsIgnoreCase("")){
                        HideKeyboard();
                        CustomField("Please fill all fields");
                    }
                    else if(user_mobile_number.length()<9){//else if(!validatePhoneNumber(user_mobile_number)) {
                        HideKeyboard();
                        CustomField("Enter valid phone number");
                    } else if(!isValidEmail(user_email)){
                        HideKeyboard();
                        CustomField("Enter valid email");
                    } else if(user_postal_code.length()<5){
                        HideKeyboard();
                        CustomField("Enter valid pincode");
                    } else {
                        callEbsKit();
                    }
                }
            });
            SharedPreferences prefs = getSharedPreferences("Itinerary", MODE_PRIVATE);

            //Set<String> HotelData = prefs.getStringSet("HotelRooms", null);
            Set<String> ActivitiesData = prefs.getStringSet("ActivitiesData", null);
            String transportation_rate = ""+prefs.getString("TransportationCost", "0");

            Log.d("Price test","Transportation"+transportation_rate);

            String F_bit = ""+prefs.getString("FlightBit",null);
            int flightBit = Integer.parseInt("" + F_bit);
            if(flightBit == 0)
            {
                String flight_dom = prefs.getString("FlightPrice", "0");
                if(!flight_dom.equals("0"))
                flight_rate = Integer.parseInt(flight_dom);

                Log.d("Price test",""+flight_rate);
            }
            else
            {
                onward_flight_rate = prefs.getString("OnwardFlightPrice", null);
                return_flight_rate = prefs.getString("ReturnFlightPrice", null);

                Log.d("Price test",""+onward_flight_rate+"Flight"+return_flight_rate);

                if(!((onward_flight_rate.equals("0"))&&(return_flight_rate.equals("0")))){
                flight_rate = Integer.parseInt(onward_flight_rate) + Integer.parseInt(return_flight_rate);
                }
                else if(!onward_flight_rate.equals("0"))
                    flight_rate = Integer.parseInt(onward_flight_rate);
                else if(!return_flight_rate.equals("0"))
                    flight_rate = Integer.parseInt(return_flight_rate);
            }

        //     String HotelData = prefs.getString("HotelRooms",null);

            SharedPreferences sharedpreferences = getSharedPreferences("SavedData", Context.MODE_PRIVATE);
            String HotelData=sharedpreferences.getString("HotelDetails","");

            Log.d("Price test HD",""+HotelData);

             String[] HotelDataArray = HotelData.trim().split("-");
            //String[] HotelDataArray = HotelData.toArray(new String[HotelData.size()]);
            String[] ActivitiesDataArray = ActivitiesData.toArray(new String[ActivitiesData.size()]);

            String DayCount = prefs.getString("DestinationCount", null);
            String[] destination_day_count = DayCount.trim().split(",");
            int rate_of_rooms =0;
            for (int index = 0; index < HotelDataArray.length; index++) {
                Log.i("Hoteldataaaaaa",""+ HotelDataArray[index]);
                String[] hotel_room_Data = HotelDataArray[index].trim().split(",");
                //no fo rooms and price

                Log.d("Price test", "" + hotel_room_Data[2] + " hotel " + hotel_room_Data[3] + "Destination" + "" + destination_day_count[index]);

                int no_room_price = Integer.parseInt("" + hotel_room_Data[3]) * Integer.parseInt("" + hotel_room_Data[2]);
                int room_rate = Integer.parseInt("" + destination_day_count[index]) * no_room_price;

                if(index == 0)
                {
                    rate_of_rooms = room_rate;
                }
                else{
                    rate_of_rooms = rate_of_rooms + room_rate;
                }
                Log.d("Price test","Room Rate" +rate_of_rooms);
            }


            int activities_rate =0;
            int count_bit= 0;
            for (int index = 0; index < ActivitiesDataArray.length; index++) {   //Log.i("Hoteldataaaaaa",""+ HotelDataArray[index]);


                if(!ActivitiesDataArray[index].toString().equalsIgnoreCase("null")) {

                    String[] activities_Data = ActivitiesDataArray[index].trim().split(",");
                    Log.i("ActivityData","" +activities_Data);

                    Log.d("Price test"," Activity "+activities_Data[1]);

                    try{
                    if (count_bit == 0) {
                        activities_rate = Integer.parseInt("" + activities_Data[1]);
                        count_bit ++;
                    } else {
                        activities_rate = activities_rate + Integer.parseInt("" + activities_Data[1]);
                    }}
                    catch(Exception e){

                    }
                }
            }

           // Log.i("ActvitiesRates","" +activities_rate);
            //Log.i("TransportationRates","" +transportation_rate);


            if(flight_rate == 0)
            {
                    total_price = rate_of_rooms + activities_rate + (Integer.parseInt("" +transportation_rate) * ActivitiesDataArray.length);//no fo days;
                    Log.d("Price test","Total1"+total_price);

            }
            else
            {
                    total_price = rate_of_rooms + activities_rate + (Integer.parseInt("" + transportation_rate) * ActivitiesDataArray.length) + Integer.parseInt("" + flight_rate);
                Log.d("Price test","Total2"+total_price);
            }

            double discount_val = 0.2;
            total_discount = Double.parseDouble("" + total_price) * discount_val ;

            TextView package_v = (TextView) findViewById(R.id.price_txt);
            TextView discount_v=(TextView) findViewById(R.id.dis_price_txt);
            TextView total_p = (TextView) findViewById(R.id.total_p_txt);
            TextView adv_payment = (TextView) findViewById(R.id.booking_price_txt);

            package_v.setText("Rs " + total_price);
            discount_v.setText("Percentage "+ "5 %");
            int temp_price=total_price-(5*total_price/100);
            total_p.setText("Rs " + temp_price);

            int adv_price=(20*temp_price)/100;
            adv_payment.setText("Rs " + adv_price);
    }

    //function to check if entered email is valid or not
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private static boolean validatePhoneNumber(String phoneNo) {
        //validate phone numbers of format "1234567890"
        if (phoneNo.matches("\\d{10}")) return true;
            //validating phone number with -, . or spaces
        else if(phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) return true;
            //validating phone number with extension length from 3 to 5
        else if(phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) return true;
            //validating phone number where area code is in braces ()
        else if(phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) return true;
            //return false if nothing matches the input
        else return false;

    }

    @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            //getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();
            return super.onOptionsItemSelected(item);
        }


    private void callEbsKit() {

        /** Payment Amount Details */
        /** Mandatory */
        // Total Amount

        Log.d("Payment",""+"called kit ii");

        totalamount = Math.round( total_discount * 100.0 ) / 100.0;
        PaymentRequest.getInstance().setTransactionAmount(Math.round( total_discount * 100.0 ) / 100.0);
        /** Mandatory */
        // Reference No
        PaymentRequest.getInstance().setReferenceNo("223");
        //PaymentRequest.getInstance().
        /** Initializing the EBS Gateway */
        EBSPayment.getInstance().init(SummaryActivity.this, ACC_ID, SECRET_KEY, Config.Mode.ENV_LIVE, Config.Encryption.ALGORITHM_MD5, HOST_NAME);
    }


    public void RetryInternet(){
        final Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_SHORT)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
        // Changing message text color
        snackbar.setActionTextColor(Color.RED);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    public void CustomField(String message){

        Log.d("Summary",""+coordinatorLayout);
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, message, Snackbar.LENGTH_SHORT);
        snackbar.setActionTextColor(Color.GREEN);
        snackbar.show();
        View sbView = snackbar.getView();
        sbView.bringToFront();
    }

    public void HideKeyboard(){
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}

