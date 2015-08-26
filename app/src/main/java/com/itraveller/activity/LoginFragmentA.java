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
import android.view.Menu;
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



public class LoginFragmentA extends Fragment {

    SharedPreferences prefs;
    TextView registerScreen;

    Boolean isInternetPresent = false;

    // Connection detector class
    ConnectionDetector cd;
    /*login form
        end
     */
    public static String access_token,user_id;


    String email_id,mobile_number,login_url;
    Fragment fragment;
    String title;

    EditText _email_id,mobile;
    private CallbackManager callbackManager;
    String emailid, gender, bday, firstname;
    private LoginButton loginButton;

    ProfilePictureView profilePictureView;
    private TextView greeting;
    Button btnunreg,login;
    TextView info;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private ProgressDialog pDialog;
    public static Profile profile;

    private ShareDialog shareDialog;
    Context context;

    public void setContextValue(Context context)
    {
        this.context = context;

    }

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

            new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setMessage(alertMessage)
                    .setPositiveButton(R.string.ok, null)
                    .show();
        }
    };



    public LoginFragmentA() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.login, container, false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //Initialise facebook SDK
        FacebookSdk.sdkInitialize(context);

        callbackManager = CallbackManager.Factory.create();

        prefs = context.getSharedPreferences("Preferences",context.MODE_PRIVATE);

        //object of ConnectionDetector class used to check internet connection

        if(String.valueOf(prefs.getInt("flag", 0)).equals(null))
        {
            SharedPreferences.Editor editor=prefs.edit();
            editor.putInt("flag",0);
            editor.commit();
        }
        SharedPreferences.Editor editor=prefs.edit();
        editor.putInt("count",2);
        editor.commit();


        if(prefs.getInt("flag",0)==1) {
            if (!(prefs.getString("u_name", null).equals("user"))) {
/*                Log.d("Entered into login2", "hi");
                Intent i = new Intent(context, MainActivity.class);
                i.putExtra("id", "login_from_server");
                i.putExtra("profile", "login_from_server");
                i.putExtra("fname", "" + prefs.getString("u_name", null));
                i.putExtra("AccessToken", "login_from_server");
                startActivity(i); */
                fragment = new LandingActivity();
                title = getString(R.string.title_home);

            }
        }
        else {

            editor = prefs.edit();
            editor.putInt("flag", 0);
            editor.commit();


            cd = new ConnectionDetector(context);


            _email_id = (EditText) view.findViewById(R.id.email_id);
            mobile = (EditText) view.findViewById(R.id.mobile);
            login = (Button) view.findViewById(R.id.submit);
            //TextView used as a link to registration form
            registerScreen = (TextView) view.findViewById(R.id.link_to_register);

            //LoginButton provided by Facebook
            loginButton = (LoginButton) view.findViewById(R.id.login_button);
            loginButton.setFragment(LoginFragmentA.this);

            //Button used for allowing user to continue without logging in or unregistered
            btnunreg = (Button) view.findViewById(R.id.btnunreg);


            Log.d("isp", "" + isInternetPresent);

            //Setting permissions for accessing data from facebook
            loginButton.setReadPermissions(Arrays
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


            shareDialog = new ShareDialog(getActivity());
            shareDialog.registerCallback(callbackManager, shareCallback);


            //user redirected to Homepage of our app without registration or login
            btnunreg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //checking if internet is present or not
                    isInternetPresent = cd.isNetworkConnection();
                    if (isInternetPresent) {
                        // Internet Connection is Present
                        // make HTTP requests
                        SharedPreferences.Editor editor=prefs.edit();
                        editor.putString("f_name","user");
                        editor.commit();

                    /*    LandingActivity fragment1 = new LandingActivity();
                        title = getString(R.string.title_home);
                        fragment=fragment1;
*/

                        Intent i = new Intent(context, MainActivity.class);
                        i.putExtra("id", "unregistered");
                        i.putExtra("fname", "unregistered");
                        i.putExtra("profile", "unregistered");
                        i.putExtra("AccessToken", "unregistered");
                        startActivity(i);

                        getActivity().finish();



                    } else {
                        // Internet connection is not present
                        // Ask user to connect to Internet
                        showAlertDialog(context, "No Internet Connection",
                                "You don't have internet connection.", false);
                    }

                }
            });

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    isInternetPresent = cd.isNetworkConnection();
                    if (isInternetPresent) {
                        // Internet Connection is Present
                        // make HTTP requests
                        email_id = _email_id.getText().toString();
                        mobile_number = mobile.getText().toString();

                        if (isValidEmail(email_id) && email_id.trim().length() > 0 && mobile_number.trim().length() > 0) {
                            JSONParsing();
                        }
                        else
                        {
                            AlertDialog alertDialog = new AlertDialog.Builder(context).create();

                            // Setting Dialog Title
                            alertDialog.setTitle("Login Failed   !!");

                            // Setting Dialog Message
                            alertDialog.setMessage("Plese enter valid data ");

                            // Setting Icon to Dialog
                            alertDialog.setIcon(R.drawable.fail);

                            // Setting OK Button
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to execute after dialog closed
                                    mobile.setText("");
                                }
                            });

                            // Showing Alert Message
                            alertDialog.show();

                        }



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


            //code for handling event when user clicks login button provided by facebook
            loginButton.registerCallback(callbackManager,
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
            registerScreen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    isInternetPresent = cd.isNetworkConnection();
                    if (isInternetPresent) {
                        // Internet Connection is Present
                        // make HTTP requests
                        Intent i = new Intent(getActivity(), SignupActivity.class);
                        startActivity(i);
                        getActivity().finish();

                    } else {
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

    public void JSONParsing()
    {

        String tag_json_obj = "json_obj_req";

        final ProgressDialog pDialog = new ProgressDialog(context);
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
                new com.android.volley.Response.Listener<JSONObject>() {

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

                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                                    // Setting Dialog Title
                                    alertDialog.setTitle("Login Failed   !!");

                                    // Setting Dialog Message
                                    alertDialog.setMessage("You are not a registered user !!!");

                                    // Setting Icon to Dialog
                                    alertDialog.setIcon(R.drawable.fail);

                                    // Setting Positive "Yes" Button
                                    alertDialog.setPositiveButton("Sign up", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int which) {
                                            Intent i=new Intent(context,SignupActivity.class);
                                            startActivity(i);
                                            getActivity().finish();
                                            // Write your code here to invoke YES event
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


                                } else {


                                    JSONObject payload_object= jobj.getJSONObject("payload");
                                    user_id=payload_object.getString("user_id");
                                    access_token=payload_object.getString("key");

                                    Log.d("User id is login", "" + user_id);
                                    SharedPreferences.Editor editor=prefs.edit();
                                    editor.putString("user_id_string",""+user_id);
                                    editor.putString("access_token_string",""+access_token);
                                    editor.putString("f_name",""+email_id);
                                    editor.putString("f_name",""+email_id);
                                    editor.putInt("temp", 1);
                                    editor.putInt("flag",1);
                                    editor.commit();


                                    JSONObject user_object=payload_object.getJSONObject("user");


                                    Intent i = new Intent(context, MainActivity.class);
                                    i.putExtra("id", "login_from_server");
                                    i.putExtra("profile", "login_from_server");
                                    i.putExtra("fname", "" + email_id);
                                    i.putExtra("AccessToken", "login_from_server");
                                    startActivity(i);
                                    getActivity().finish();
                                }
                            }
                            else
                            {
                                AlertDialog alertDialog = new AlertDialog.Builder(context).create();

                                // Setting Dialog Title
                                alertDialog.setTitle("Login Failed   !!");

                                // Setting Dialog Message
                                alertDialog.setMessage("You entered wrong password");

                                // Setting Icon to Dialog
                                alertDialog.setIcon(R.drawable.fail);

                                // Setting OK Button
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Write your code here to execute after dialog closed
                                        mobile.setText("");
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
                }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //VolleyLog.d("TAG", "Error: " + error.getMessage());
                pDialog.dismiss();
                if (ConnectionDetector.isNetworkConnection()) {
                    Toast.makeText(context, "Internet Problem", Toast.LENGTH_SHORT).show();

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
    public void onResume() {
        super.onResume();

        // Call the 'activateApp' method to log an app event for use in analytics and advertising
        // reporting.  Do so in the onResume methods of the primary Activities that an app may be
        // launched into.
        AppEventsLogger.activateApp(context);

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
        AppEventsLogger.deactivateApp(context);
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

    class fblogin extends AsyncTask<AccessToken, String, String> {

        //Before fetching data from facebook "Please wait" mesasge is shown to user
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(context);
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

                                firstname = object.getString("first_name");

                                emailid = object.getString("email");
                                Log.d("Email",""+emailid);

                                gender = object.getString("gender");

//                                bday = object.getString("birthday");


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
        Log.d("Profile in fragment is",""+profile);

        if (enableButtons && profile != null) {

            String id=profile.getId();
            String fname=profile.getFirstName();
            AccessToken at=AccessToken.getCurrentAccessToken();

            SharedPreferences.Editor editor=prefs.edit();
            editor.putString("f_name", "" + fname);
            editor.putString("fb_id", "" + id);
            //   editor.putString("email_id", "" + emailid);
            editor.putString("var", "y");
            editor.putInt("temp", 1);
            editor.commit();

            Log.d("LoginFragmentAT", "" + at);
            Intent i=new Intent(context,MainActivity.class);

            i.putExtra("id",id);
            i.putExtra("fname", "y");
            i.putExtra("profile",profile);
            i.putExtra("AccessToken",at);
            startActivity(i);
            getActivity().finish();


        }
    }




}
