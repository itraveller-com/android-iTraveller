package com.itraveller.core;

/**
 * Created by VNK on 8/16/2015.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.itraveller.R;
import com.itraveller.activity.FragmentDrawer;
import com.itraveller.activity.MainActivity;
import com.itraveller.constant.Constants;
import com.itraveller.constant.Utility;
import com.itraveller.volley.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

public class LoginScreenActivity extends Fragment {

    EditText emailId;
    EditText phoneNumber;
    CoordinatorLayout coordinatorLayout;
    Button btnSimpleSnackbar;
    String _emailId, _phoneNumber;
    Animation animSlideup;
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    RelativeLayout loadingSnackBar;
    TextView registerUser;
    private LinearLayout item;
    private View signUpView;
    private View forgotPasswordView;
    private View loginView;
    private LoginButton facebookLogin;
    private CallbackManager callbackManager;
    View viewItem;
    ProfileTracker profileTracker;
    EditText emailIdForgot;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    //    if(MainActivity.active==true)
    //        ((MainActivity) getActivity()).getSupportActionBar().hide();
    //    else if(MyTravelActivity.isActive==true)
    //        ((MyTravelActivity) getActivity()).getSupportActionBar().hide();

        //setting layout of screen to login.xml file
        viewItem = inflater.inflate(R.layout.login_main, container, false);
        //creating threads for facebook login
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //Initialise facebook SDK
        FacebookSdk.sdkInitialize(getActivity());
        callbackManager = CallbackManager.Factory.create();

        FragmentDrawer.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        FragmentDrawer.mDrawerToggle.setDrawerIndicatorEnabled(false);

        //shared preferences object for storing data in "Preferences"
        SharedPreferences prefs = getActivity().getSharedPreferences("Preferences", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        LoginManager.getInstance().logOut();
        //Remove title bar
        //getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        //getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Adding Main View
        item = (LinearLayout) viewItem.findViewById(R.id.flipper_view);
        final ImageView itraveller = (ImageView) viewItem.findViewById(R.id.itraveller_logo_ic);

        final View loginView = getActivity().getLayoutInflater().inflate(R.layout.login_md, null);
        signUpView = getActivity().getLayoutInflater().inflate(R.layout.signup_md, null);
        forgotPasswordView = getActivity().getLayoutInflater().inflate(R.layout.forgetpassword_md, null);

        item.addView(loginView);

        //LoginButton provided by Facebook
        facebookLogin = (LoginButton) loginView.findViewById(R.id.login_button);
        facebookLogin.setFragment(LoginScreenActivity.this);

        //Setting permissions for accessing data from facebook
        facebookLogin.setReadPermissions(Arrays
                .asList("public_profile, email, user_birthday, user_friends"));

        //code for handling event when user clicks login button provided by facebook
        facebookLogin.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        //if user successfully redirected to facebook page
//                            getActivity().finish();
                        Log.v("Facebook","API calling");
                        new fblogin().execute(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException e) {
                        Log.v("Facebook","Error- "+e.getMessage());
                        if (e.getMessage().equalsIgnoreCase("CONNECTION_FAILURE: CONNECTION_FAILURE")) {
                            RetryInternet();
                        }
                    }
                });


        emailId = (EditText) loginView.findViewById(R.id.mail_id);
        phoneNumber = (EditText) loginView.findViewById(R.id.confirm_password);
        Button submit = (Button) loginView.findViewById(R.id.submit);
        loadingSnackBar = (RelativeLayout) viewItem.findViewById(R.id.loading_snackbar);
        registerUser = (TextView) loginView.findViewById(R.id.link_to_register);
        final TextView forgotPassword = (TextView) loginView.findViewById(R.id.forgot_password);
        final TextView skipButton = (TextView) loginView.findViewById(R.id.btnunreg);

        skipButton.setVisibility(View.INVISIBLE);

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getActivity().getSharedPreferences("Preferences", getActivity().MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("skipbit","0").commit();
                /*Intent intent = new Intent(getActivity(),MainActivity.class);
                startActivity(intent);*/
                ((MainActivity) getActivity()).onDrawerItemSelected(viewItem, 0);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailId.setText("");
                phoneNumber.setText("");
                item.addView(forgotPasswordView);
                ToLeftAnimation(loginView);
                item.removeView(loginView);
                FromRightAnimation(forgotPasswordView);

                emailIdForgot = (EditText) forgotPasswordView.findViewById(R.id.mail_id);
                TextView backToLogin = (TextView) forgotPasswordView.findViewById(R.id.back_button);
                backToLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        BackButtonAnimation(loginView, forgotPasswordView);
                        emailIdForgot.setText("");
                    }
                });
                Button forgotBtn = (Button) forgotPasswordView.findViewById(R.id.forgot);
                forgotBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String _emailIdForget = emailIdForgot.getText().toString().trim();

                        if (_emailIdForget.equalsIgnoreCase("")) {
                            emailIdForgot.setError("Required");
                        } else if (!isValidEmail(_emailIdForget)) {
                            CustomField("Invalid Email Address");
                        } else if (!Utility.isNetworkConnected(getActivity())) {
                            RetryInternet();
                        } else {
                            ServerRequestForget(_emailIdForget);
                            loadingSnackBar.setVisibility(View.VISIBLE);
                        }
                    }
                });


            }
        });
        new Handler().postDelayed(new Runnable() {

            /* Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company*/
            @Override
            public void run() {
                //itraveller.setAnimation(animSlideup);
            }
        }, SPLASH_TIME_OUT);

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNumber.setText("");
                item.addView(signUpView);
                ToLeftAnimation(loginView);
                item.removeView(loginView);

                FromRightAnimation(signUpView);

                final EditText emailIdSignUp = (EditText) signUpView.findViewById(R.id.mail_id);
                final EditText phoneNumberSignUp = (EditText) signUpView.findViewById(R.id.mobile);
                final EditText passwordSignUp = (EditText) signUpView.findViewById(R.id.password);
                final EditText confirmPasswordSignUp = (EditText) signUpView.findViewById(R.id.confirm_password);

                TextView backToLogin = (TextView) signUpView.findViewById(R.id.back_button);
                backToLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        BackButtonAnimation(loginView, signUpView);
                        emailIdSignUp.setText("");
                        phoneNumberSignUp.setText("");
                        passwordSignUp.setText("");
                        confirmPasswordSignUp.setText("");
                    }
                });
                Button register = (Button) signUpView.findViewById(R.id.register);
                register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String _emailIdSignUp, _phoneNumberSignUp, _passwordSignUp, _confirmPasswordSignUp;
                        _emailIdSignUp = emailIdSignUp.getText().toString().trim();
                        _phoneNumberSignUp = phoneNumberSignUp.getText().toString().trim();
                        _passwordSignUp = passwordSignUp.getText().toString().trim();
                        _confirmPasswordSignUp = confirmPasswordSignUp.getText().toString().trim();

                        if (_emailIdSignUp.equalsIgnoreCase("")) {
                            emailIdSignUp.setError("Required");
                        } else if (_phoneNumberSignUp.equalsIgnoreCase("")) {
                            phoneNumberSignUp.setError("Required");
                        } else if (!validatePhoneNumber(_phoneNumberSignUp)) {
                            HideKeyboard();
                            CustomField("Check the PhoneNumber");
                        } else if (_passwordSignUp.equalsIgnoreCase("")) {
                            passwordSignUp.setError("Required");
                        } else if(!PasswordLength(_passwordSignUp)) {
                            passwordSignUp.setError("Minimum 8 Character");
                        } else if (_confirmPasswordSignUp.equalsIgnoreCase("")) {
                            confirmPasswordSignUp.setError("Required");
                        } else if (!_passwordSignUp.equalsIgnoreCase(_confirmPasswordSignUp)) {
                            HideKeyboard();
                            CustomField("Password Incorrect");
                        } else if (!isValidEmail(_emailIdSignUp)) {
                            HideKeyboard();
                            CustomField("Invalid Email Address");
                        } else if (!Utility.isNetworkConnected(getActivity())) {
                            HideKeyboard();
                            RetryInternet();
                        } else {
                            HideKeyboard();
                            ServerRequestSignUp(_emailIdSignUp, _phoneNumberSignUp, _passwordSignUp);
                            loadingSnackBar.setVisibility(View.VISIBLE);
                        }
                    }
                });

            }
        });

        //Displaying TextInputLayout Error
        final TextInputLayout lEmailLayout = (TextInputLayout) viewItem.findViewById(R.id
                .lEmailLayout);
        //lEmailLayout.setError("Please enter Email");

        final TextInputLayout lPhoneLayout = (TextInputLayout) viewItem.findViewById(R.id
                .lMobileLayout);
        //lPhoneLayout.setError("Please enter Phone Number");

        coordinatorLayout = (CoordinatorLayout) viewItem.findViewById(R.id.coordinatorLayout);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideKeyboard();
                _emailId = emailId.getText().toString().trim();
                _phoneNumber = phoneNumber.getText().toString().trim();
                //Displaying EditText Error
                //RetryInternet();
                if (_emailId.equalsIgnoreCase("")) {
                    emailId.setError("Required");
                } else if (_phoneNumber.equalsIgnoreCase("")) {
                    phoneNumber.setError("Required");
                } else if (!isValidEmail(_emailId)) {
                    CustomField("Invalid Email Address");
                } else if (!PasswordLength(_phoneNumber)){
                    phoneNumber.setError("Minimum 8 Characters");
                } else if (!Utility.isNetworkConnected(getActivity())) {
                    RetryInternet();
                } else {
                    ServerRequest();
                    loadingSnackBar.setVisibility(View.VISIBLE);
                }
            }
        });

        return viewItem;
    }

    public void ServerRequest() {
        HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("email", emailId.getText().toString().trim());
        postParams.put("password", phoneNumber.getText().toString().trim());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.API_login, new JSONObject(postParams),
                new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        try {
                            JSONObject jobj = new JSONObject(response.toString());
                            String success = jobj.getString("success");
                            //if user enters valid details
                            loadingSnackBar.setVisibility(View.GONE);
                            if (success.equals("true")) {
                                JSONObject payload_object = jobj.getJSONObject("payload");
                                //store user_id and access_token received from our server
                                //payload_object.getString("user_id");
                                JSONObject userDetails = payload_object.getJSONObject("user");
                                SharedPreferences prefs = getActivity().getSharedPreferences("Preferences", getActivity().MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("id", "" + userDetails.getString("user_id"));
                                editor.putString("name", "" + userDetails.getString("name"));
                                editor.putString("phone", "" + userDetails.getString("phone"));
                                editor.putString("email", "" + emailId.getText().toString().trim());
                                editor.putString("serverCheck", "server");
                                editor.putString("skipbit", "1");
                                editor.commit();
                                //userDetails.getString("facebook_id");
                                //userDetails.getString("phone");
                                    ((MainActivity) getActivity()).onDrawerItemSelected(viewItem, 0);
                            } else{
                                JSONObject errorobj = jobj.getJSONObject("error");
                                JSONObject validationobj = errorobj.getJSONObject("validation");
                                CustomField("" + validationobj.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }})
        {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(8000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
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
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, message, Snackbar.LENGTH_SHORT);
        snackbar.setActionTextColor(Color.GREEN);
        snackbar.show();
        View sbView = snackbar.getView();
        sbView.bringToFront();
    }

    //function to check if entered email is valid or not
    public boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public void HideKeyboard(){
       View view = getActivity().getCurrentFocus();
       if (view != null) {
           InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
           imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
       }
   }

    private void FromRightAnimation(View v){
        animSlideup = AnimationUtils.loadAnimation(getActivity() , R.anim.move);
        v.setAnimation(animSlideup);
    }

    private void ToLeftAnimation(View v){
        Animation animSlideup = AnimationUtils.loadAnimation(getActivity() , R.anim.moveright);
        v.setAnimation(animSlideup);
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

    public void ServerRequestSignUp(String _emailIdValue, String _phoneNumberValue, String _passwordValue){
        HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("email", _emailIdValue);
        postParams.put("phone", _phoneNumberValue);
        postParams.put("password", _passwordValue);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.API_signup, new JSONObject(postParams),
                new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        try {
                            JSONObject jobj = new JSONObject(response.toString());
                            String success = jobj.getString("success");
                            //if user enters valid details
                            loadingSnackBar.setVisibility(View.GONE);
                            if (success.equals("true")) {
                                JSONObject payload_object = jobj.getJSONObject("payload");
                                //store user_id and access_token received from our server
                                //payload_object.getString("user_id");
                                CustomField("Successfully created");
                            } else{
                                /*JSONObject errorobj = jobj.getJSONObject("error");
                                JSONObject validationobj = errorobj.getJSONObject("validation");
                                JSONArray message = validationobj.getJSONArray("email")*/;
                                CustomField("Email already registered");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }})
        {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(8000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    public void ServerRequestForget(String _emailIdValue){
        HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("email", _emailIdValue);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.API_forgot, new JSONObject(postParams),
                new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        try {
                            JSONObject jobj = new JSONObject(response.toString());
                            String success = jobj.getString("success");
                            //if user enters valid details
                            loadingSnackBar.setVisibility(View.GONE);
                            if (success.equals("true")) {
                                JSONObject payload_object = jobj.getJSONObject("payload");
                                //store user_id and access_token received from our server
                                //payload_object.getString("user_id");
                                /*JSONObject userDetails = payload_object.getJSONObject("user");
                                userDetails.getString("user_id");
                                userDetails.getString("name");
                                userDetails.getString("facebook_id");
                                userDetails.getString("phone");*/
                                CustomField("Email send successfully");
                            } else{
                                JSONObject errorobj = jobj.getJSONObject("error");
                                JSONObject validationobj = errorobj.getJSONObject("validation");
                                CustomField("" + validationobj.getString("message"));                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }})
        {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(8000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

    public  void BackButtonAnimation(View addView, View removeView){
        item.addView(addView);
        FromRightAnimation(addView);
        item.removeView(removeView);
        ToLeftAnimation(removeView);
    }

    //for facebook login
    class fblogin extends AsyncTask<AccessToken, String, String> {

        //Before fetching data from facebook "Please wait" mesasge is shown to user
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.v("Facebook","PreExecute Function");
            loadingSnackBar.setVisibility(View.VISIBLE);
        }

        SharedPreferences prefs = getActivity().getSharedPreferences("Preferences", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        //fetching data from facebook in background and parsing it
        protected String doInBackground(AccessToken... params) {
            GraphRequest request = GraphRequest.newMeRequest(params[0],

                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object,
                                                GraphResponse response) {

                            Log.v("Response", response.toString());
                            try {
                                editor.putString("email", "" + object.getString("email"));
                                editor.putString("name", "" + object.getString("first_name"));
                                editor.putString("id", "" + object.getString("id"));
                                editor.putString("serverCheck", "facebook");
                                editor.putString("skipbit", "1");
                                editor.commit();
                                handler.sendEmptyMessage(0);
                            } catch (JSONException e) {
                                // TODO Auto-generated catch
                                // block
                                e.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields","id,first_name,email,gender,birthday");
            request.setParameters(parameters);
            request.executeAndWait();
            return null;
        }

        //after feching data  from facebook close the message "Please wait"
        protected void onPostExecute(String file_url) {
            loadingSnackBar.setVisibility(View.GONE);
        }

    }


    public boolean PasswordLength(String password) {
        return password.length() > 7;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    // Define the Handler that receives messages from the thread and update the progress
    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            ((MainActivity) getActivity()).onDrawerItemSelected(viewItem,0);
        }
    };

}

