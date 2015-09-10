package com.itraveller.activity;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.widget.ShareDialog;
import com.itraveller.R;
import com.itraveller.constant.Constants;
import com.itraveller.volley.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;



public class LoginFragment_Before_Payment extends Fragment {

    SharedPreferences prefs;
    //Textview for going to signup screen
    TextView link_to_signup;

    //variable for detecting internet status
    Boolean isInternetPresent = false;

    // Connection detector class
    ConnectionDetector cd;

    //access-token and user id receeived from facebook
    public static String access_token,user_id;

    //storing values for login from our server
    String email_id_from_our_server,mobile_number,login_url,email_id1;


    //fragment class object for using fragment
    Fragment fragment;

    //title of the fragment
    String title;

    //edittext for taking user email and mobile number in login form
    public static EditText email_id_Edittext,mobile_Edittext;

    private CallbackManager callbackManager;

    //variables for storing data received from facebook
    String email_id_from_facebook, firstname_from_facebook;

    //LoginButton provided by facebook
    private LoginButton facebook_loginButton;

    //Buttons for login user and for continuing without login
    Button server_loginButton;

    //profiletracker for keeping track of user profile on facebook
    private ProfileTracker profileTracker;
    private ProgressDialog pDialog;
    public static Profile profile;

    private ShareDialog shareDialog;
    Context context;

    public void setContextValue(Context context)
    {
        this.context = context;

    }

    public LoginFragment_Before_Payment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //setting layout of screen to login.xml file
        View view=inflater.inflate(R.layout.login_before_payment, container, false);

        //creating threads for facebook login
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Initialise facebook SDK
        FacebookSdk.sdkInitialize(getActivity());

        callbackManager = CallbackManager.Factory.create();

        //shared preferences object for storing data in "Preferences"
        prefs = getActivity().getSharedPreferences("Preferences",Context.MODE_PRIVATE);

        //check if user is already logged in or not viz. if flag=0 then user is not logged in and 1 for logged in
        if(String.valueOf(prefs.getInt("flag", 0)).equals(null))
        {
            SharedPreferences.Editor editor=prefs.edit();
            editor.putInt("flag",0);
            editor.commit();
        }


        //if user is already logged in then redirect user to homepage of our app
        if(prefs.getInt("flag",0)==1)
        {
            if (!(prefs.getString("u_name", null).equals("user")))
            {
                fragment = new LandingActivity();
                title = getString(R.string.title_home);
            }
        }
        else
        {

            SharedPreferences.Editor editor=prefs.edit();
            editor = prefs.edit();
            editor.putInt("flag", 0);
            editor.commit();

            cd = new ConnectionDetector(getActivity());

            //initialise components of login form
            email_id_Edittext = (EditText) view.findViewById(R.id.email_id);
            mobile_Edittext = (EditText) view.findViewById(R.id.mobile);
            server_loginButton = (Button) view.findViewById(R.id.submit);

            //TextView used as a link to registration form
            link_to_signup = (TextView) view.findViewById(R.id.link_to_register);

            //LoginButton provided by Facebook
            facebook_loginButton = (LoginButton) view.findViewById(R.id.login_button);
            facebook_loginButton.setFragment(LoginFragment_Before_Payment.this);


            //Setting permissions for accessing data from facebook
            facebook_loginButton.setReadPermissions(Arrays
                    .asList("public_profile, email, user_birthday, user_friends"));

            //profileTracker is used to keep track of user profile
            profileTracker = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                    Log.d("ProfileTrack11", "Profile");
                    //update UI if there is some change in user profile
                    updateUI();

                }
            };

            //listener after clicking login button
            server_loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    isInternetPresent = cd.isNetworkConnection();
                    if (isInternetPresent) {

                        //getting data from login form
                        email_id_from_our_server = email_id_Edittext.getText().toString();
                        email_id1 = email_id_Edittext.getText().toString();
                        mobile_number = mobile_Edittext.getText().toString();


                        //checking if user entered valid data
                        if (isValidEmail(email_id_from_our_server) && email_id_from_our_server.trim().length() > 0 && mobile_number.trim().length() > 0) {

                            SharedPreferences.Editor editor=prefs.edit();
                            editor.putString("email_from_our_server",""+email_id_from_our_server);
                            editor.putString("mobile_number_from_our_server",""+mobile_number);
                            editor.commit();

                            login_from_server();
                        }
                        else
                        {
                            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

                            // Setting Dialog Title
                            alertDialog.setTitle("Login Failed");

                            // Setting Dialog Message
                            alertDialog.setMessage("Please enter valid data ");

                            // Setting Icon to Dialog
                            alertDialog.setIcon(R.drawable.fail);

                            // Setting OK Button
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to execute after dialog closed
                                    mobile_Edittext.setText("");
                                }
                            });

                            // Showing Alert Message
                            alertDialog.show();

                        }
                    } else {
                        // Internet connection is not present
                        // Ask user to connect to Internet
                        showAlertDialog(getActivity(), "No Internet Connection",
                                "You don't have internet connection.", false);
                    }
                }
            });


            //code for handling event when user clicks login button provided by facebook
            facebook_loginButton.registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {

                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            //if user successfully redirected to facebook page
//                            getActivity().finish();
                            new fblogin().execute(loginResult.getAccessToken());
                        }

                        @Override
                        public void onCancel() {

                        }

                        @Override
                        public void onError(FacebookException e) {

                        }


                    });

            //code for link to registration form
            link_to_signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    isInternetPresent = cd.isNetworkConnection();
                    if (isInternetPresent) {


                        //call signup fragment on clicking link
                        SignupFragment fragment2=new SignupFragment();
                        android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();

                        // Replace whatever is in the fragment_container view with this fragment,
                        // and add the transaction to the back stack
                        transaction.replace(android.R.id.content, fragment2);
                        transaction.addToBackStack(null);

                        // Commit the transaction
                        transaction.commit();

                    }
                    else
                    {
                        // Internet connection is not present
                        // Ask user to connect to Internet
                        showAlertDialog(getActivity(), "No Internet Connection",
                                "You don't have internet connection.", false);
                    }


                }
            });
        }
        return view;

    }

    //function for logging in user from our server
    public void login_from_server()
    {

        String tag_json_obj = "json_obj_req";

        //this message will be displayed to user while logging in user
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Signing in...");
        pDialog.show();


        //login API
        login_url=Constants.API_login + email_id_from_our_server + "&password=" + mobile_number  ;

        final HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("email", email_id_from_our_server);
        Log.d("Email server is:", "" + email_id_from_our_server);
        postParams.put("phone", mobile_number);
        email_id_from_our_server=email_id_from_our_server.substring(0,email_id_from_our_server.indexOf("@"));



        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                login_url, new JSONObject(postParams),
                new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response signin", response.toString());
                        JSONObject jobj= null;
                        try
                        {
                            jobj = new JSONObject(response.toString());

                            String success=jobj.getString("success");
                            //  phone_number=user_object.getString("phone");
                            // Log.d("Phone number",""+phone_number);

                            //    name=user_object.getString("name");


                            Log.d("Boolean",""+success.equals("true"));
                            //if user enters valid details
                            if(success.equals("true"))
                            {
                                //if user is not a registered user
                                if (jobj.getString("payload").equalsIgnoreCase("User is not registered."))
                                {
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

                                    // Setting Dialog Title
                                    alertDialog.setTitle("Login Failed ");

                                    // Setting Dialog Message
                                    alertDialog.setMessage("You are not a registered user !!!");

                                    // Setting Icon to Dialog
                                    alertDialog.setIcon(R.drawable.fail);

                                    // Setting Positive "Yes" Button
                                    alertDialog.setPositiveButton("Sign up", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int which) {

                                            //storing email and mobile number of user for fuethur use
                                            SharedPreferences.Editor editor=prefs.edit();
                                            editor.putString("email_id1", "" + email_id1);
                                            editor.putString("mobile_number1", "" + mobile_number);
                                            editor.commit();

                                            //then call signup fragment
                                            SignupFragment fragment2=new SignupFragment();
                                            android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                            // Replace whatever is in the fragment_container view with this fragment,
                                            // and add the transaction to the back stack
                                            transaction.replace(android.R.id.content, fragment2);
                                            transaction.addToBackStack(null);

                                            // Commit the transaction
                                            transaction.commit();

                                        }
                                    });

                                    // Setting Negative "NO" Button
                                    alertDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Write your code here to invoke NO event
                                            dialog.cancel();
                                        }
                                    });

                                    // Showing Alert Message
                                    alertDialog.show();

                                }
                                //if user is registered user
                                else
                                {


                                    JSONObject payload_object= jobj.getJSONObject("payload");
                                    //store user_id and access_token received from our server
                                    user_id=payload_object.getString("user_id");
                                    access_token=payload_object.getString("key");

                                    Log.d("User id is login", "" + user_id);
                                    SharedPreferences.Editor editor=prefs.edit();
                                    editor.putString("user_id_string",""+user_id);
                                    editor.putString("access_token_string",""+access_token);
                                    editor.putString("email_id1",""+email_id1);
                                    editor.putString("f_name",""+email_id_from_our_server);
                                    editor.putString("mobile_number1",""+mobile_number);
                                    editor.putInt("temp", 1);
                                    editor.putInt("flag",1);
                                    editor.commit();


                                    JSONObject user_object=payload_object.getJSONObject("user");


                                    Intent i = new Intent(getActivity(), SummaryActivity.class);
                                    startActivity(i);
                                    getActivity().finish();
                                }
                            }
                            //if user entered invalid details
                            else
                            {
                                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

                                // Setting Dialog Title
                                alertDialog.setTitle("Login Failed ");

                                // Setting Dialog Message
                                alertDialog.setMessage("You entered wrong password");

                                // Setting Icon to Dialog
                                alertDialog.setIcon(R.drawable.fail);

                                // Setting OK Button
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Write your code here to execute after dialog closed
                                        mobile_Edittext.setText("");
                                    }
                                });

                                // Showing Alert Message
                                alertDialog.show();
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        pDialog.dismiss();
                    }
                }, new com.android.volley.Response.ErrorListener() {

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

    //function to check if entered email is valid or not
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    //display dialog box for internet connection
    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting alert dialog icon
        alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();

        // Call the 'activateApp' method to log an app event for use in analytics and advertising
        // reporting.  Do so in the onResume methods of the primary Activities that an app may be
        // launched into.
        AppEventsLogger.activateApp(getActivity());

        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }


    @Override
    public void onPause() {
        super.onPause();

        // Call the 'deactivateApp' method to log an app event for use in analytics and advertising
        // reporting.  Do so in the onPause methods of the primary Activities that an app may be
        // launched into.
        AppEventsLogger.deactivateApp(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    //for facebook login
    class fblogin extends AsyncTask<AccessToken, String, String> {

        //Before fetching data from facebook "Please wait" mesasge is shown to user
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //mesage will be displayed to the user while logging in user
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait.....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();


        }

        //fetching data from facebook in background and parsing it
        protected String doInBackground(AccessToken... params) {
            GraphRequest request = GraphRequest.newMeRequest(params[0],

                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object,
                                                GraphResponse response) {

                            Log.v("Resonse", response.toString());

                            try {

                                firstname_from_facebook = object.getString("first_name");

                                email_id_from_facebook = object.getString("email");
                                Log.d("Email from fb",""+email_id_from_facebook);


                            } catch (JSONException e) {
                                // TODO Auto-generated catch
                                // block
                                e.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields",
                    "id,first_name,email,gender,birthday");
            request.setParameters(parameters);
            request.executeAndWait();
            return null;
        }

        //after feching data  from facebook close the message "Please wait"
        protected void onPostExecute(String file_url) {

            pDialog.dismiss();

        }

    }

    //for updating UI  if there is some change detected
    public void updateUI()
    {
        boolean enableButtons = AccessToken.getCurrentAccessToken() != null;

        profile = Profile.getCurrentProfile();
        Log.d("Profile in fragment is",""+profile);

        if (enableButtons && profile != null)
        {

            String id=profile.getId();
            String fname=profile.getFirstName();
            AccessToken at=AccessToken.getCurrentAccessToken();

            SharedPreferences.Editor editor=prefs.edit();
            editor.putString("f_name", "" + fname);
            editor.putString("user_id_string", "" + id);
            editor.putString("access_token_string", "" + at);
            Log.d("email check 2",""+email_id_from_facebook);
            editor.putString("email_id1",""+email_id_from_facebook);
            editor.putString("mobile_number1","0");
            editor.putString("var", "y");
            editor.putInt("temp", 1);
            editor.putInt("flag",1);
            editor.commit();

            Log.d("LoginFragmentAT", "" + at);

            Intent i=new Intent(getActivity(),SummaryActivity.class);
            startActivity(i);
            getActivity().finish();
        }
    }
}
