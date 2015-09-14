package com.itraveller.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.itraveller.R;
import com.itraveller.constant.Constants;
import com.itraveller.volley.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignupFragment extends Fragment {



    //textbox for taking user input
    public static EditText email_id_Edittext_,mobile_Edittext_;
    //button for sign up user
    Button submit;
    //used for storing user details
    String user_name,emailid,pass,mobile_number;
    Context context;
    //API
    String url;
    Fragment fragment;
    SharedPreferences prefs;

    public void setContextValue(Context context)
    {
        this.context = context;

    }


    public SignupFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.sign_up,container,false);

        prefs=getActivity().getSharedPreferences("Preferences",Context.MODE_PRIVATE);

        //initialise the components of signup form
        email_id_Edittext_=(EditText) view.findViewById(R.id.email_id);
        mobile_Edittext_=(EditText) view.findViewById(R.id.mobile);


        submit=(Button) view.findViewById(R.id.submit);


        TextView loginScreen = (TextView) view.findViewById(R.id.link_to_login);


        // Listening to Login Screen link

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //reading data from textbox on submitting form
                emailid=email_id_Edittext_.getText().toString();
                Log.d("Email check", "hi" + emailid);
                user_name=emailid.substring(0,emailid.indexOf("@"));
                mobile_number=mobile_Edittext_.getText().toString();
                Log.d("Mobile check","hi"+mobile_number);

                //check if user entered valid details or not
                if(( mobile_number.trim().length()>0 && emailid.trim().length()>0 && isValidEmail(emailid)) && mobile_number.trim().length()==10)
                {
                    SharedPreferences.Editor editor=prefs.edit();
                    editor.putString("email", "" + emailid);
                    editor.putString("user_name", "" + user_name);
                    editor.putString("mobile", "" + mobile_number);
                    editor.commit();

                    sign_up_user();

                }

                else if((mobile_number.trim().length()<10 || mobile_number.trim().length()==0) && (emailid.trim().length()>0 && isValidEmail(emailid)))
                {

                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("Login Failed");

                    // Setting Dialog Message
                    alertDialog.setMessage("Please enter valid mobile number ");

                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.fail);

                    // Setting OK Button
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog closed
                            mobile_Edittext_.setText("");
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();

                }
                else if((!isValidEmail(emailid) || emailid.trim().length() == 0) && mobile_number.trim().length()==10)
                {

                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("Login Failed");

                    // Setting Dialog Message
                    alertDialog.setMessage("Please enter valid email id ");

                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.fail);

                    // Setting OK Button
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog closed

                            email_id_Edittext_.setText("");
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();

                }

                else
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("Sign Up failed ");

                    // Setting Dialog Message
                    alertDialog.setMessage("Please enter all the valid details");

                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.fail);

                    // Setting OK Button
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            email_id_Edittext_.setText("");
                            mobile_Edittext_.setText("");
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();

                    //Toast.makeText(getActivity(), "Please fill all the details !!", Toast.LENGTH_LONG).show();
//                        Log.d("Failure","failde");
                }
            }
        });

        loginScreen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // Switching to Login Screen/closing register screen
                getActivity().onBackPressed();

            }
        });
        return view;
    }

    //function to check if user entered valid email or not
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    //function to sign up user
    private void sign_up_user() {
        String tag_json_obj = "json_obj_req";

        //message to be displayed to user while signing up user
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Creating new account...");
        pDialog.show();



        //sign up API
        url=Constants.API_signup + emailid + "&name= " + user_name +"&phone=" + mobile_number + "&password="+pass  ;

        Log.d("Signup URL","http://stage.itraveller.com/backend/api/v1/users/ -d  email=" +
                emailid + "&name= " + user_name +"&phone=" + mobile_number + "&password="+pass );

        final HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("email", emailid);
        Log.d("Email server is:",""+emailid);
        postParams.put("name",user_name);
        postParams.put("phone", mobile_number);
        postParams.put("password",pass);
        user_name=emailid.substring(0,emailid.indexOf("@"));

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.API_signup, new JSONObject(postParams),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG signup", response.toString());
                        try
                        {

                            JSONObject obj=new JSONObject(response.toString());
                            String success=obj.getString("success");
                            if(success.equals("true"))
                            {

                                SharedPreferences.Editor editor=prefs.edit();
                                editor.putString("email_id1", "" + emailid);
                                editor.putString("mobile_number1", "" + mobile_number);
                                editor.commit();

                                Log.d("Email received",""+prefs.getString("email_id1", null));

                                LoginFragment.email_id_Edittext.setText(""+prefs.getString("email_id1", null));
                                LoginFragment.mobile_Edittext.setText(""+prefs.getString("mobile_number1", null));

                                getActivity().onBackPressed();
                            }
                            else
                            {
                                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

                                // Setting Dialog Title
                                alertDialog.setTitle("Already exists...");

                                // Setting Dialog Message
                                alertDialog.setMessage("This email is already registered");

                                // Setting Icon to Dialog
                                alertDialog.setIcon(R.drawable.fail);

                                // Setting OK Button
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        getActivity().onBackPressed();
                                    }
                                });

                                // Showing Alert Message
                                alertDialog.show();


                            }
                            pDialog.dismiss();
                        }
                        catch (JSONException e)
                        {
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
                    Toast.makeText(getActivity(), "Internet Problem", Toast.LENGTH_SHORT).show();

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