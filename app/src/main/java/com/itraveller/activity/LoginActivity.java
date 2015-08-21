package com.itraveller.activity;

/**
 * Created by I TRAVELLES on 04-08-2015.
 */
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
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
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.Sharer;
import com.facebook.share.widget.ShareDialog;
import com.itraveller.R;
import com.itraveller.activity.ConnectionDetector;
import com.itraveller.volley.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.StandardExceptionParser;



public class LoginActivity extends ActionBarActivity {

    /*login form
        start
     */
    TextView registerScreen;

    Boolean isInternetPresent = false;

    // Connection detector class
    ConnectionDetector cd;
    /*login form
        end
     */
    public static String access_token,user_id;
    public static Profile profile;
    public static int count=0;
    String email_id,mobile_number,login_url;

    private CallbackManager callbackManager;
    String emailid, gender, bday, firstname;
    private LoginButton loginButton;

    EditText _email_id,mobile;
    ProfilePictureView profilePictureView;
    private TextView greeting;
    Button btnunreg,login;
    TextView info;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private ProgressDialog pDialog;

    private ShareDialog shareDialog;

    public FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onCancel() {
            Log.d("HelloFacebook", "Canceled");
        }

        @Override
        public void onError(FacebookException error) {
            Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
            String title = getString(R.string.error);
            String alertMessage = error.getMessage();
            showResult(title, alertMessage);
        }



        @Override
        public void onSuccess(Sharer.Result result) {
            Log.d("HelloFacebook", "Success!");
            if (result.getPostId() != null) {
                String title = getString(R.string.success);
                String id = result.getPostId();
                String alertMessage = getString(R.string.successfully_posted_post, id);
                showResult(title, alertMessage);
            }
        }

        private void showResult(String title, String alertMessage) {

            new AlertDialog.Builder(LoginActivity.this)
                    .setTitle(title)
                    .setMessage(alertMessage)
                    .setPositiveButton(R.string.ok, null)
                    .show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);


        //Facebook sdk initialization
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        callbackManager = CallbackManager.Factory.create();


        //setting layout to login.xml
        setContentView(R.layout.login);

        cd = new ConnectionDetector(getApplicationContext());

        _email_id=(EditText) findViewById(R.id.email_id);
       mobile=(EditText) findViewById(R.id.mobile);
        login=(Button) findViewById(R.id.submit);


//        url="http://stage.itraveller.com/backend/api/v1/users/auth -d email="+email_id+"&password"+mobile_number;
        //Textview used as a link to registration form
        registerScreen=(TextView) findViewById(R.id.link_to_register);

        //Login Button provided by facebook
        loginButton = (LoginButton) findViewById(R.id.login_button);

        //Button for user to continue without login/registration
        btnunreg=(Button) findViewById(R.id.btnunreg);


        Log.d("isp", "" + isInternetPresent);

        // check for Internet status


        //setting permission for accessing data from facebook
        loginButton.setReadPermissions(Arrays
                .asList("public_profile, email, user_birthday, user_friends"));


        //profiletracker used for keeping track of user profile
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                //update UI if there is some change in user profile
                updateUI();
            }
        };


        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, shareCallback);

        //user redirected to Homepage of our app without registration or login
        btnunreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //createEvent("your_action","button_name", "screen_name")
                EasyTracker.getInstance(LoginActivity.this).send(MapBuilder.createEvent("Button Clicked",
                        "Unregistered", "Login Activity SS1", null).build());
                //checking if there is internet connection or not
                isInternetPresent = cd.isNetworkConnection();
                if (isInternetPresent) {
                    // Internet Connection is Present
                    // make HTTP requests
                    Intent i=new Intent(getApplicationContext(),MainActivity.class);
                    String str1="unregistered";
                    String str2="unregistered";
                    String str3="unregistered";
                    i.putExtra("id",str1);
                    i.putExtra("fname",str2);
                    i.putExtra("profile",str3);
                    i.putExtra("AccessToken","unregistered");

                    startActivity(i);
                    finish();

                } else {
                    // Internet connection is not present
                    // Ask user to connect to Internet
                    showAlertDialog(LoginActivity.this, "No Internet Connection",
                            "You don't have internet connection.", false);
                }

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email_id=_email_id.getText().toString();
                mobile_number=mobile.getText().toString();
                //createEvent("your_action","button_name", "screen_name")
                EasyTracker.getInstance(LoginActivity.this).send(MapBuilder.createEvent("Button Clicked",
                        "Login", "Login Activity SS1", null).build());
                if(isValidEmail(email_id) && email_id.trim().length()>0 && mobile_number.trim().length()>0 && mobile_number.trim().length()==(10))
                {
                    JSONParsing();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please enter valid data!!",Toast.LENGTH_LONG).show();

                }
            }
        });



        //code for handling event when user clicks login button provided by facebook
        loginButton.registerCallback(callbackManager,new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        //if user successfully redirected to facebook page
                        //createEvent("your_action","button_name", "screen_name")
                        EasyTracker.getInstance(LoginActivity.this).send(MapBuilder.createEvent("Button Clicked",
                                "Facebook Login", "Login Activity SS1", null).build());
                        finish();
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
        registerScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isInternetPresent = cd.isNetworkConnection();
                if (isInternetPresent) {
                    // Internet Connection is Present
                    // make HTTP requests
                    Intent i=new Intent(getApplicationContext(),SignupActivity.class);
                    startActivity(i);
                    finish();


                } else {
                    // Internet connection is not present
                    // Ask user to connect to Internet
                    showAlertDialog(LoginActivity.this, "No Internet Connection",
                            "You don't have internet connection.", false);
                }



            }
        });

    }

    public void JSONParsing()
    {
        count=1;
        String tag_json_obj = "json_obj_req";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Signing in...");
        pDialog.show();

        login_url="http://stage.itraveller.com/backend/api/v1/users/auth?email=" +
                email_id + "&password=" + mobile_number  ;

        final HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("email", email_id);
        Log.d("Email server is:", "" + email_id);
        postParams.put("phone", mobile_number);
        email_id=email_id.substring(0,email_id.indexOf("@"));


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                login_url, new JSONObject(postParams),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG signin", response.toString());
                        JSONObject jobj= null;
                        try {
                            jobj = new JSONObject(response.toString());

                        String success=jobj.getString("success");
                      //  phone_number=user_object.getString("phone");
                       // Log.d("Phone number",""+phone_number);

                    //    name=user_object.getString("name");


                        Log.d("Boolean",""+success.equals("true"));
                        if(success.equals("true")) {

                            if (jobj.getString("payload").equalsIgnoreCase("User is not registered.")) {

                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);

                                // Setting Dialog Title
                                alertDialog.setTitle("Error");

                                // Setting Dialog Message
                                alertDialog.setMessage("You are not a registered user !!!");

                                // Setting Icon to Dialog
                                alertDialog.setIcon(R.drawable.fail);

                                // Setting Positive "Yes" Button
                                alertDialog.setPositiveButton("Sign up", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int which) {
                                        Intent i=new Intent(getApplicationContext(),SignupActivity.class);
                                        startActivity(i);
                                        finish();
                                        // Write your code here to invoke YES event
                                        //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                // Setting Negative "NO" Button
                                alertDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Write your code here to invoke NO event
                                        //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                                        dialog.cancel();
                                    }
                                });

                                // Showing Alert Message
                                alertDialog.show();


                            } else {

                                JSONObject payload_object= jobj.getJSONObject("payload");
                                user_id=payload_object.getString("user_id");
                                access_token=payload_object.getString("key");

                                JSONObject user_object=payload_object.getJSONObject("user");


                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                i.putExtra("id", "login_from_server");
                                i.putExtra("profile", "login_from_server");
                                i.putExtra("fname", ""+email_id);
                                i.putExtra("AccessToken", "login_from_server");
                                startActivity(i);

                            }
                        }
                        else
                        {
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    LoginActivity.this).create();

                            // Setting Dialog Title
                            alertDialog.setTitle("Alert Dialog");

                            // Setting Dialog Message
                            alertDialog.setMessage("You entered wrong password");

                            // Setting Icon to Dialog
                            alertDialog.setIcon(R.drawable.fail);

                            // Setting OK Button
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to execute after dialog closed
                                    mobile.setText("");
                                   // Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                                }
                            });

                            // Showing Alert Message
                            alertDialog.show();
                                                   }
                    } catch (JSONException e) {
                        e.printStackTrace();
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


    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


    //code for showing user alert box after checking internet connection
    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

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
    protected void onResume() {
        super.onResume();

        // Call the 'activateApp' method to log an app event for use in analytics and advertising
        // reporting.  Do so in the onResume methods of the primary Activities that an app may be
        // launched into.
        AppEventsLogger.activateApp(this);
        updateUI();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);
    }


    @Override
    public void onPause() {
        super.onPause();

        // Call the 'deactivateApp' method to log an app event for use in analytics and advertising
        // reporting.  Do so in the onPause methods of the primary Activities that an app may be
        // launched into.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    class fblogin extends AsyncTask<AccessToken, String, String> {

        //Before fetching data from facebook "Please wait" mesasge is shown to user
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
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

                            Log.v("LoginActivity", response.toString());

                            try {

                                firstname = object.getString("first_name");

                                emailid = object.getString("email");
                                Log.d("Email",""+emailid);

                                gender = object.getString("gender");

                                bday = object.getString("birthday");

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
    public void updateUI() {
        boolean enableButtons = AccessToken.getCurrentAccessToken() != null;

        profile = Profile.getCurrentProfile();
        Log.d("Profile",""+profile);
        if (enableButtons && profile != null) {
            Log.d("updateUI", "Inside update");
            String id=profile.getId();
            Log.d("id", "" + id);
            String fname=profile.getFirstName();
            Log.d("Fname", "" + fname);

            Intent i=new Intent(getApplicationContext(),MainActivity.class);

            i.putExtra("id",id);
            i.putExtra("fname", fname);
            i.putExtra("profile", profile);
            i.putExtra("AccessToken",AccessToken.getCurrentAccessToken());
            startActivity(i);
            finish();


        }
    }

}

