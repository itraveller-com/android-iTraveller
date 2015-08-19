package com.itraveller.activity;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import com.itraveller.R;


public class LoginFragmentA extends Fragment {

    TextView registerScreen;

    Boolean isInternetPresent = false;

    // Connection detector class
    ConnectionDetector cd;
    /*login form
        end
     */

    private CallbackManager callbackManager;
    String emailid, gender, bday, firstname;
    private LoginButton loginButton;

    ProfilePictureView profilePictureView;
    private TextView greeting;
    Button btnunreg;
    TextView info;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private ProgressDialog pDialog;

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

        //object of ConnectionDetector class used to check internet connection
        cd = new ConnectionDetector(context);

        //TextView used as a link to registration form
        registerScreen=(TextView) view.findViewById(R.id.link_to_register);

        //LoginButton provided by Facebook
        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setFragment(LoginFragmentA.this);

        //Button used for allowing user to continue without logging in or unregistered
        btnunreg=(Button) view.findViewById(R.id.btnunreg);


        Log.d("isp", "" + isInternetPresent);

        //Setting permissions for accessing data from facebook
        loginButton.setReadPermissions(Arrays
                .asList("public_profile, email, user_birthday, user_friends"));

        //profileTracker is used to keep track of user profile
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                Log.d("ProfileTrack11","Profile");
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
                    Intent i = new Intent(context, MainActivity.class);
                    String str1 = "x";
                    String str2 = "x";
                    String str3 = "x";
                    i.putExtra("id", str1);
                    i.putExtra("fname", str2);
                    i.putExtra("profile", str3);
                    i.putExtra("AccessToken","y");
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
        //code for handling event when user clicks login button provided by facebook
        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        //if user successfully redirected to facebook page
                        getActivity().finish();
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

        return view;

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

                            Log.v("LoginActivity111", response.toString());

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

        LoginActivity.profile = Profile.getCurrentProfile();

        if (enableButtons && LoginActivity.profile != null) {

            String id=LoginActivity.profile.getId();
            String fname=LoginActivity.profile.getFirstName();
            AccessToken at=AccessToken.getCurrentAccessToken();

            Log.d("LoginFragmentAT",""+at);
            Intent i=new Intent(context,MainActivity.class);

            i.putExtra("id",id);
            i.putExtra("fname", fname);
            i.putExtra("profile",LoginActivity.profile);
            i.putExtra("AccessToken",at);
            startActivity(i);
            getActivity().finish();


        }
    }




}
