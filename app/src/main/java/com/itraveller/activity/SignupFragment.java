package com.itraveller.activity;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.itraveller.R;
import com.itraveller.volley.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignupFragment extends Fragment {

    EditText email_id_,mobile;
    Button submit;
    String user_name,emailid,pass,mobile_number;
    Context context;
    String url;
    Fragment fragment;

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

        email_id_=(EditText) view.findViewById(R.id.email_id);
        mobile=(EditText) view.findViewById(R.id.mobile);


        submit=(Button) view.findViewById(R.id.submit);


        TextView loginScreen = (TextView) view.findViewById(R.id.link_to_login);


        // Listening to Login Screen link

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                emailid=email_id_.getText().toString();
                Log.d("Email check", "hi" + emailid);
                mobile_number=mobile.getText().toString();
                Log.d("Mobile check","hi"+mobile_number);

                if(( mobile_number.trim().length()>0 && emailid.trim().length()>0 && isValidEmail(emailid)) && mobile_number.trim().length()==10)
                {
                    postUsingVolley();

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
                            email_id_.setText("");
                            mobile.setText("");
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
/*                Intent i=new Intent(context,MainActivity.class);
                i.putExtra("profile","signup");
                i.putExtra("id","signup");
                i.putExtra("fname","signup");
                i.putExtra("AccessToken","signup");
                startActivity(i);
                getActivity().finish();
*/

                getActivity().onBackPressed();

            }
        });


        return view;
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
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
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


/*                                Intent i = new Intent(getActivity(), MainActivity.class);
                                i.putExtra("profile", "sign_up");
                                i.putExtra("id", "signup");
                                i.putExtra("fname", "signup");
                                i.putExtra("AccessToken", "sign_up");
                                startActivity(i);
                                getActivity().finish();
*/                                // Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_LONG).show();

                                getActivity().onBackPressed();
//                                getFragmentManager().popBackStack();
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
                                        // Write your code here to execute after dialog closed
/*                                        Intent i=new Intent(getActivity(),MainActivity.class);
                                        i.putExtra("profile","signup");
                                        i.putExtra("id","signup");
                                        i.putExtra("fname","signup");
                                        i.putExtra("AccessToken", "signup");
                                        startActivity(i);
                                        getActivity().finish();
*/
//                                        getFragmentManager().popBackStack();
                                        getActivity().onBackPressed();
                                    }
                                });

                                // Showing Alert Message
                                alertDialog.show();


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
