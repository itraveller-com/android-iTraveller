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
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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

public class LoginScreenFragment extends Fragment {



    private EditText mEmailId;
    private EditText mPhoneNumber;
    private CoordinatorLayout mCoordinatorLayout;
    private RelativeLayout mLoadingSnackBar;
    private LinearLayout mItem;
    private View mSignUpView;
    private View mForgotPasswordView;
    private CallbackManager mCallbackManager;
    private View mViewItem;
    private EditText mEmailIdForgot;
    private final static String PREF = "Preferences";

    private static final int INITIAL_TIMEOUT_MS = 8000;
    private static final int MAX_NUM_RETRIES=0;
    private  static final String TAG =LoginScreenFragment.class.getName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        //setting layout of screen to login.xml file
        mViewItem = inflater.inflate(R.layout.login_main, container, false);
        //creating threads for facebook login
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //Initialise facebook SDK
        FacebookSdk.sdkInitialize(getActivity());
        mCallbackManager = CallbackManager.Factory.create();

        FragmentDrawer.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        FragmentDrawer.mDrawerToggle.setDrawerIndicatorEnabled(false);

        LoginManager.getInstance().logOut();

        //Adding Main View
        mItem = (LinearLayout) mViewItem.findViewById(R.id.flipper_view);


        final View loginView = getActivity().getLayoutInflater().inflate(R.layout.login_md, null);
        mSignUpView = getActivity().getLayoutInflater().inflate(R.layout.signup_md, null);
        mForgotPasswordView = getActivity().getLayoutInflater().inflate(R.layout.forgetpassword_md, null);

        mItem.addView(loginView);

        //LoginButton provided by Facebook
        LoginButton mFacebookLogin = (LoginButton) loginView.findViewById(R.id.login_button);
        mFacebookLogin.setFragment(LoginScreenFragment.this);

        //Setting permissions for accessing data from facebook
        mFacebookLogin.setReadPermissions(Arrays
                .asList("public_profile, email, user_birthday, user_friends"));

        //code for handling event when user clicks login button provided by facebook
        mFacebookLogin.registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        //if user successfully redirected to facebook page
                        Log.v(TAG,"API calling");
                        new fblogin().execute(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException e) {
                        Log.v(TAG,"Error- "+e.getMessage());
                        if (e.getMessage().equalsIgnoreCase("CONNECTION_FAILURE: CONNECTION_FAILURE")) {
                            retryInternet();
                        }
                    }
                });


        mEmailId = (EditText) loginView.findViewById(R.id.mail_id);
        mPhoneNumber = (EditText) loginView.findViewById(R.id.confirm_password);
        Button submit = (Button) loginView.findViewById(R.id.submit);
        mLoadingSnackBar = (RelativeLayout) mViewItem.findViewById(R.id.loading_snackbar);
        TextView mRegisterUser = (TextView) loginView.findViewById(R.id.link_to_register);
        final TextView forgotPassword = (TextView) loginView.findViewById(R.id.forgot_password);
        final TextView skipButton = (TextView) loginView.findViewById(R.id.btnunreg);

        skipButton.setVisibility(View.INVISIBLE);

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getActivity().getSharedPreferences(PREF, getActivity().MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("skipbit","0").commit();
                ((MainActivity) getActivity()).onDrawerItemSelected(mViewItem, 0);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEmailId.setText("");
                mPhoneNumber.setText("");
                mItem.addView(mForgotPasswordView);
                toLeftAnimation(loginView);
                mItem.removeView(loginView);
                fromRightAnimation(mForgotPasswordView);

                mEmailIdForgot = (EditText) mForgotPasswordView.findViewById(R.id.mail_id);
                TextView backToLogin = (TextView) mForgotPasswordView.findViewById(R.id.back_button);
                backToLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        backButtonAnimation(loginView, mForgotPasswordView);
                        mEmailIdForgot.setText("");
                    }
                });
                Button forgotBtn = (Button) mForgotPasswordView.findViewById(R.id.forgot);
                forgotBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String _emailIdForget = mEmailIdForgot.getText().toString().trim();

                        if (_emailIdForget.equalsIgnoreCase("")) {
                            mEmailIdForgot.setError("Required");
                        } else if (!isValidEmail(_emailIdForget)) {
                            customField("Invalid Email Address");
                        } else if (!Utility.isNetworkConnected(getActivity())) {

                            retryInternet();
                        } else {
                            serverRequestForget(_emailIdForget);
                            mLoadingSnackBar.setVisibility(View.VISIBLE);
                        }
                    }
                });


            }
        });

        mRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPhoneNumber.setText("");
                mItem.addView(mSignUpView);
                toLeftAnimation(loginView);
                mItem.removeView(loginView);

                fromRightAnimation(mSignUpView);

                final EditText emailIdSignUp = (EditText) mSignUpView.findViewById(R.id.mail_id);
                final EditText phoneNumberSignUp = (EditText) mSignUpView.findViewById(R.id.mobile);
                final EditText passwordSignUp = (EditText) mSignUpView.findViewById(R.id.password);
                final EditText confirmPasswordSignUp = (EditText) mSignUpView.findViewById(R.id.confirm_password);

                TextView backToLogin = (TextView) mSignUpView.findViewById(R.id.back_button);
                backToLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        backButtonAnimation(loginView, mSignUpView);
                        emailIdSignUp.setText("");
                        phoneNumberSignUp.setText("");
                        passwordSignUp.setText("");
                        confirmPasswordSignUp.setText("");
                    }
                });
                Button register = (Button) mSignUpView.findViewById(R.id.register);
                register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String emailSignUp;
                        String phoneSignUp;
                        String pwdSignUp;
                        String confirmPwdSignUp;
                        emailSignUp = emailIdSignUp.getText().toString().trim();
                        phoneSignUp = phoneNumberSignUp.getText().toString().trim();
                        pwdSignUp = passwordSignUp.getText().toString().trim();
                        confirmPwdSignUp = confirmPasswordSignUp.getText().toString().trim();

                        if (emailSignUp.equalsIgnoreCase("")) {
                            emailIdSignUp.setError("Required");
                        } else if (phoneSignUp.equalsIgnoreCase("")) {
                            phoneNumberSignUp.setError("Required");
                        } else if (!validatePhoneNumber(phoneSignUp)) {
                            hideKeyboard();
                            customField("Check the PhoneNumber");
                        } else if (pwdSignUp.equalsIgnoreCase("")) {
                            passwordSignUp.setError("Required");
                        } else if(!passwordLength(pwdSignUp)) {
                            passwordSignUp.setError("Minimum 8 Character");
                        } else if (confirmPwdSignUp.equalsIgnoreCase("")) {
                            confirmPasswordSignUp.setError("Required");
                        } else if (!pwdSignUp.equalsIgnoreCase(confirmPwdSignUp)) {
                            hideKeyboard();
                            customField("Password Incorrect");
                        } else if (!isValidEmail(emailSignUp)) {
                            hideKeyboard();
                            customField("Invalid Email Address");
                        } else if (!Utility.isNetworkConnected(getActivity())) {
                            hideKeyboard();
                            retryInternet();
                        } else {
                            hideKeyboard();
                            serverRequestSignUp(emailSignUp, phoneSignUp, pwdSignUp);
                            mLoadingSnackBar.setVisibility(View.VISIBLE);
                        }
                    }
                });

            }
        });


        mCoordinatorLayout = (CoordinatorLayout) mViewItem.findViewById(R.id.coordinatorLayout);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                 String email = mEmailId.getText().toString().trim();
                 String mPhone = mPhoneNumber.getText().toString().trim();
                if (email.equalsIgnoreCase("")) {
                    mEmailId.setError(getString(R.string.required_field));
                } else if (mPhone.equalsIgnoreCase("")) {
                    mPhoneNumber.setError(getString(R.string.required_field));
                } else if (!isValidEmail(email)) {
                    customField(getString(R.string.err_invalid_email));
                } else if (!passwordLength(mPhone)){
                    mPhoneNumber.setError(getString(R.string.err_min_characters));
                } else if (!Utility.isNetworkConnected(getActivity())) {
                    retryInternet();
                } else {
                    serverRequest();

                    mLoadingSnackBar.setVisibility(View.VISIBLE);
                }
            }
        });

        return mViewItem;
    }

    public void serverRequest() {
        HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("email", mEmailId.getText().toString().trim());
        postParams.put("password", mPhoneNumber.getText().toString().trim());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.API_login, new JSONObject(postParams),
                new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            JSONObject jobj = new JSONObject(response.toString());
                            String success = jobj.getString("success");
                            //if user enters valid details
                            mLoadingSnackBar.setVisibility(View.GONE);
                            if (success.equals("true")) {
                                JSONObject payload_object = jobj.getJSONObject("payload");
                                //store user_id and access_token received from our server
                                JSONObject userDetails = payload_object.getJSONObject("user");
                                SharedPreferences prefs = getActivity().getSharedPreferences(PREF, getActivity().MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("id", "" + userDetails.getString("user_id"));
                                editor.putString("name", "" + userDetails.getString("name"));
                                editor.putString("phone", "" + userDetails.getString("phone"));
                                editor.putString("email", "" + mEmailId.getText().toString().trim());

                                editor.putString("serverCheck", "server");
                                editor.putString("skipbit", "1");
                                editor.commit();

                                    ((MainActivity) getActivity()).onDrawerItemSelected(mViewItem, 0);
                            } else{
                                JSONObject errorobj = jobj.getJSONObject("error");
                                JSONObject validationobj = errorobj.getJSONObject("validation");
                                customField("" + validationobj.getString("message"));
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

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(INITIAL_TIMEOUT_MS, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    public void retryInternet(){
        final Snackbar snackbar = Snackbar
                .make(mCoordinatorLayout, "No internet connection!", Snackbar.LENGTH_SHORT)
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

    public void customField(String message){
        Snackbar snackbar = Snackbar
                .make(mCoordinatorLayout, message, Snackbar.LENGTH_SHORT);
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

    public void hideKeyboard(){
       View view = getActivity().getCurrentFocus();
       if (view != null) {
           InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
           imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
       }
   }

    private void fromRightAnimation(View v){
        Animation animSlideUp = AnimationUtils.loadAnimation(getActivity() , R.anim.move);
        v.setAnimation(animSlideUp);
    }

    private void toLeftAnimation(View v){
        Animation animSlideup = AnimationUtils.loadAnimation(getActivity() , R.anim.moveright);
        v.setAnimation(animSlideup);
    }

    private static boolean validatePhoneNumber(String phoneNo) {
        //validate phone numbers of format "1234567890"
        if (phoneNo.matches("\\d{10}")) {
            return true;
            //validating phone number with -, . or spaces
        } else if (phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) {
            return true;
            //validating phone number with extension length from 3 to 5
        } else if (phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) {
            return true;
        //validating phone number where area code is in braces ()
        }else if(phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) {
            return true;
            //return false if nothing matches the input
        }else {
            return false;
        }

    }

    public void serverRequestSignUp(String _emailIdValue, String _phoneNumberValue, String _passwordValue){
        HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("email", _emailIdValue);
        postParams.put("phone", _phoneNumberValue);
        postParams.put("password", _passwordValue);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.API_signup, new JSONObject(postParams),
                new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            JSONObject jobj = new JSONObject(response.toString());
                            String success = jobj.getString("success");
                            //if user enters valid details
                            mLoadingSnackBar.setVisibility(View.GONE);
                            if (success.equals("true")) {
                                JSONObject payload_object = jobj.getJSONObject("payload");


                                customField("Successfully created");
                            } else{
                                customField("Email already registered");
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

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(INITIAL_TIMEOUT_MS, MAX_NUM_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    public void serverRequestForget(String _emailIdValue){
        HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("email", _emailIdValue);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.API_forgot, new JSONObject(postParams),
                new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            JSONObject jobj = new JSONObject(response.toString());
                            String success = jobj.getString("success");
                            //if user enters valid details
                            mLoadingSnackBar.setVisibility(View.GONE);
                            if (success.equals("true")) {
                                customField("Email send successfully");
                            } else{
                                JSONObject errorobj = jobj.getJSONObject("error");
                                JSONObject validationobj = errorobj.getJSONObject("validation");
                                customField("" + validationobj.getString("message"));                            }
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

    public  void backButtonAnimation(View addView, View removeView){
        mItem.addView(addView);
        fromRightAnimation(addView);
        mItem.removeView(removeView);
        toLeftAnimation(removeView);
    }

    /* for  login using facbook */
    class fblogin extends AsyncTask<AccessToken, String, String> {

        /* Before fetching data from facebook "Please wait" message is shown to user*/
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.v(TAG, "PreExecute Function");
            mLoadingSnackBar.setVisibility(View.VISIBLE);
        }

        SharedPreferences prefs = getActivity().getSharedPreferences(PREF, getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        /* Fetching data from facebook in background and parsing it */

        protected String doInBackground(AccessToken... params) {
            GraphRequest request = GraphRequest.newMeRequest(params[0],

                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object,
                                                GraphResponse response) {

                            Log.v(TAG, response.toString());
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

        /* After fetching data  from facebook close the message "Please wait"*/
        protected void onPostExecute(String file_url) {
            mLoadingSnackBar.setVisibility(View.GONE);
        }

    }


    public boolean passwordLength(String password) {
        return password.length() > 7;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

    }

    /* Define the Handler that receives messages from the thread and update the progress*/
    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            ((MainActivity) getActivity()).onDrawerItemSelected(mViewItem,0);
        }
    };
}

