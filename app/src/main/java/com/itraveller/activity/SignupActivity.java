package com.itraveller.activity;

/**
 * Created by I TRAVELLES on 04-08-2015.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by I TRAVELLES on 03-08-2015.
 */

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.itraveller.R;
import com.itraveller.volley.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class SignupActivity extends Activity {
    EditText email_id_,mobile;
    Button submit;
    String user_name,emailid,pass,mobile_number;
    String url;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set View to register.xml
        setContentView(R.layout.sign_up);

        email_id_=(EditText) findViewById(R.id.email_id);
        mobile=(EditText) findViewById(R.id.mobile);


        submit=(Button) findViewById(R.id.submit);


        TextView loginScreen = (TextView) findViewById(R.id.link_to_login);

        // Listening to Login Screen link

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                emailid=email_id_.getText().toString();
                Log.d("Email check","hi"+emailid);
                mobile_number=mobile.getText().toString();
                Log.d("Mobile check","hi"+mobile_number);

                if(( mobile_number.trim().length()>0 && emailid.trim().length()>0 && isValidEmail(emailid)) && mobile_number.trim().length()==10)
                {
                    postUsingVolley();

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please fill all the details !!",Toast.LENGTH_LONG).show();
                }
            }
        });

        loginScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // Switching to Login Screen/closing register screen
                Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private void postUsingVolley() {
        String tag_json_obj = "json_obj_req";

        LoginActivity.count=1;
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Creating new account...");
        pDialog.show();

        url="http://stage.itraveller.com/backend/api/v1/users/ -d  email=" +
                emailid + "&name= " + user_name +"&phone=" + mobile_number + "&password="+pass  ;



        final HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("email", emailid);
        Log.d("Email server is:",""+emailid);
        postParams.put("name",user_name);
        postParams.put("phone", mobile_number);
        postParams.put("password",pass);
        emailid=emailid.substring(0,emailid.indexOf("@"));

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(postParams),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG signup", response.toString());
                        try {

                            JSONObject obj=new JSONObject(response.toString());
                            String success=obj.getString("success");
                            if(success.equals("true"))
                            {
                                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                            //    i.putExtra("profile", "sign_up");
                            //    i.putExtra("id", ""+mobile_number);
                            //    i.putExtra("fname", "" + emailid);
                            //    i.putExtra("AccessToken", "sign_up");
                                startActivity(i);
                                finish();
                                // Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_LONG).show();

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "This email has already registered !!", Toast.LENGTH_LONG).show();
                                Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                                startActivity(i);
                                finish();
                            }
                            //if (response.getBoolean("status")) {
                                pDialog.dismiss();
                           //     finish();
                           // }
                        } catch (JSONException e) {
                            Log.e("TAG", e.toString());
                        }
                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //VolleyLog.d("TAG", "Error: " + error.getMessage());
                pDialog.dismiss();
                if (ConnectionDetector.isNetworkConnection()) {
                    Toast.makeText(getApplicationContext(), "Internet Problem", Toast.LENGTH_SHORT).show();

                }
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }


        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(8000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


}