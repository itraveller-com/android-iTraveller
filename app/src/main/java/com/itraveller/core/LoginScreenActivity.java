package com.itraveller.core;

/**
 * Created by VNK on 8/16/2015.
 */

//TODO: Avoid the use of unused import statements to prevent unwanted dependencies
import android.app.Activity;
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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itraveller.R;
import com.itraveller.activity.FragmentDrawer;
import com.itraveller.activity.MainActivity;
import com.itraveller.constant.Constants;
import com.itraveller.constant.Utility;
import com.itraveller.moxtraChat.AccessTokenModel;
import com.itraveller.moxtraChat.AddUserResponse;
import com.itraveller.moxtraChat.CreateBinderRequest;
import com.itraveller.moxtraChat.CreateBinderResponse;
import com.itraveller.moxtraChat.PreferenceUtil;
import com.itraveller.volley.AppController;
import com.moxtra.sdk.MXAccountManager;
import com.moxtra.sdk.MXChatManager;
import com.moxtra.sdk.MXSDKConfig;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;

public class LoginScreenActivity extends Fragment {



    private EditText mEmailId;
    private EditText mPhoneNumber;
    private CoordinatorLayout mCoordinatorLayout;
    //TODO: Perhaps 'mEmail' could be replaced by a local variable
    private String mEmail;

    //TODO: Perhaps 'mPhone' could be replaced by a local variable
    private String mPhone;

    //TODO: Perhaps 'mAnimSlideUp' could be replaced by a local variable
    private Animation mAnimSlideUp;
    private RelativeLayout mLoadingSnackBar;

    //TODO: Perhaps 'mRegisterUser' could be replaced by a local variable
    private TextView mRegisterUser;
    private LinearLayout mItem;
    private View mSignUpView;
    private View mForgotPasswordView;

    //TODO: Perhaps 'mFacebookLogin' could be replaced by a local variable
    private LoginButton mFacebookLogin;

    private CallbackManager mCallbackManager;
    private View mViewItem;
    private EditText mEmailIdForgot;

    public static final int INITIAL_TIMEOUT_MS = 8000;
    public static final int MAX_NUM_RETRIES=0;

    //TODO: When methods are excessively long this usually indicates that the method is doing more than its name/signature might suggest. They also become challenging for others to digest since excessive scrolling causes readers to lose focus. Try to reduce the method length by creating helper methods and removing any copy/pasted code
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


        //TODO: Code containing duplicate String literals can usually be improved by declaring the String as a constant field.
        //shared preferences object for storing data in "Preferences"
        SharedPreferences prefs = getActivity().getSharedPreferences("Preferences", getActivity().MODE_PRIVATE);
        //TODO: local variable is declared, but not used
        SharedPreferences.Editor editor = prefs.edit();
        LoginManager.getInstance().logOut();

        //Adding Main View
        mItem = (LinearLayout) mViewItem.findViewById(R.id.flipper_view);


        final View loginView = getActivity().getLayoutInflater().inflate(R.layout.login_md, null);
        mSignUpView = getActivity().getLayoutInflater().inflate(R.layout.signup_md, null);
        mForgotPasswordView = getActivity().getLayoutInflater().inflate(R.layout.forgetpassword_md, null);

        mItem.addView(loginView);

        //LoginButton provided by Facebook
        mFacebookLogin = (LoginButton) loginView.findViewById(R.id.login_button);
        mFacebookLogin.setFragment(LoginScreenActivity.this);

        //Setting permissions for accessing data from facebook
        mFacebookLogin.setReadPermissions(Arrays
                .asList("public_profile, email, user_birthday, user_friends"));

        //code for handling event when user clicks login button provided by facebook
        mFacebookLogin.registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        //if user successfully redirected to facebook page
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


        mEmailId = (EditText) loginView.findViewById(R.id.mail_id);
        mPhoneNumber = (EditText) loginView.findViewById(R.id.confirm_password);
        Button submit = (Button) loginView.findViewById(R.id.submit);
        mLoadingSnackBar = (RelativeLayout) mViewItem.findViewById(R.id.loading_snackbar);
        mRegisterUser = (TextView) loginView.findViewById(R.id.link_to_register);
        final TextView forgotPassword = (TextView) loginView.findViewById(R.id.forgot_password);
        final TextView skipButton = (TextView) loginView.findViewById(R.id.btnunreg);

        skipButton.setVisibility(View.INVISIBLE);

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getActivity().getSharedPreferences("Preferences", getActivity().MODE_PRIVATE);
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
                ToLeftAnimation(loginView);
                mItem.removeView(loginView);
                FromRightAnimation(mForgotPasswordView);

                mEmailIdForgot = (EditText) mForgotPasswordView.findViewById(R.id.mail_id);
                TextView backToLogin = (TextView) mForgotPasswordView.findViewById(R.id.back_button);
                backToLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        BackButtonAnimation(loginView, mForgotPasswordView);
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
                            CustomField("Invalid Email Address");
                        } else if (!Utility.isNetworkConnected(getActivity())) {

                            RetryInternet();
                        } else {
                            ServerRequestForget(_emailIdForget);
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
                ToLeftAnimation(loginView);
                mItem.removeView(loginView);

                FromRightAnimation(mSignUpView);

                final EditText emailIdSignUp = (EditText) mSignUpView.findViewById(R.id.mail_id);
                final EditText phoneNumberSignUp = (EditText) mSignUpView.findViewById(R.id.mobile);
                final EditText passwordSignUp = (EditText) mSignUpView.findViewById(R.id.password);
                final EditText confirmPasswordSignUp = (EditText) mSignUpView.findViewById(R.id.confirm_password);

                TextView backToLogin = (TextView) mSignUpView.findViewById(R.id.back_button);
                backToLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        BackButtonAnimation(loginView, mSignUpView);
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

                        //TODO: Java allows the use of several variables declaration of the same type on one line. However, it can lead to quite messy code. follow the naming convention.
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
                HideKeyboard();
                mEmail = mEmailId.getText().toString().trim();
                mPhone = mPhoneNumber.getText().toString().trim();
                if (mEmail.equalsIgnoreCase("")) {
                    mEmailId.setError(getString(R.string.required_field));
                } else if (mPhone.equalsIgnoreCase("")) {
                    mPhoneNumber.setError(getString(R.string.required_field));
                } else if (!isValidEmail(mEmail)) {
                    CustomField(getString(R.string.err_invalid_email));
                } else if (!PasswordLength(mPhone)){
                    mPhoneNumber.setError(getString(R.string.err_min_characters));
                } else if (!Utility.isNetworkConnected(getActivity())) {
                    RetryInternet();
                } else {
                    ServerRequest();

                    mLoadingSnackBar.setVisibility(View.VISIBLE);
                }
            }
        });

        return mViewItem;
    }

    public void ServerRequest() {
        HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("email", mEmailId.getText().toString().trim());
        postParams.put("password", mPhoneNumber.getText().toString().trim());

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
                            mLoadingSnackBar.setVisibility(View.GONE);
                            if (success.equals("true")) {
                                JSONObject payload_object = jobj.getJSONObject("payload");
                                //store user_id and access_token received from our server
                                JSONObject userDetails = payload_object.getJSONObject("user");
                                SharedPreferences prefs = getActivity().getSharedPreferences("Preferences", getActivity().MODE_PRIVATE);
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
                                CustomField("" + validationobj.getString("message"));
                            }

                        } catch (JSONException e) {
                            //TODO: Avoid printStackTrace(); use a logger call instead and Handle exception properly.
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

    //TODO: Method names should always begin with a lower case character, and should not contain underscores
    public void RetryInternet(){
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

    //TODO: Method names should always begin with a lower case character, and should not contain underscores
    public void CustomField(String message){
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

    public void HideKeyboard(){
       View view = getActivity().getCurrentFocus();
       if (view != null) {
           InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
           imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
       }
   }

    private void FromRightAnimation(View v){
        mAnimSlideUp = AnimationUtils.loadAnimation(getActivity() , R.anim.move);
        v.setAnimation(mAnimSlideUp);
    }

    private void ToLeftAnimation(View v){
        Animation animSlideup = AnimationUtils.loadAnimation(getActivity() , R.anim.moveright);
        v.setAnimation(animSlideup);
    }


    //TODO : Avoid using if..else statements without using surrounding braces. If the code formatting or indentation is lost then it becomes difficult to separate the code being controlled from the rest.
    //TODO: Avoid unnecessary if-then-else statements when returning a boolean. The result of the conditional test can be returned instead.
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
                            mLoadingSnackBar.setVisibility(View.GONE);
                            if (success.equals("true")) {
                                JSONObject payload_object = jobj.getJSONObject("payload");


                                CustomField("Successfully created");
                            } else{
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

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(INITIAL_TIMEOUT_MS, MAX_NUM_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                            mLoadingSnackBar.setVisibility(View.GONE);
                            if (success.equals("true")) {
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
        mItem.addView(addView);
        FromRightAnimation(addView);
        mItem.removeView(removeView);
        ToLeftAnimation(removeView);
    }

    //TODO: To avoid mistakes if we want that a Method, Field or Nested class have a default access modifier we must add a comment at the beginning of the Method, Field or Nested class. By default the comment must be /* default */, if you want another, you have to provide.
    //for facebook login
    class fblogin extends AsyncTask<AccessToken, String, String> {

        //Before fetching data from facebook "Please wait" mesasge is shown to user
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.v("Facebook","PreExecute Function");
            mLoadingSnackBar.setVisibility(View.VISIBLE);
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
            mLoadingSnackBar.setVisibility(View.GONE);
        }

    }

//TODO: Method names should always begin with a lower case character, and should not contain underscores
    public boolean PasswordLength(String password) {
        return password.length() > 7;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

    }

    // Define the Handler that receives messages from the thread and update the progress
    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            ((MainActivity) getActivity()).onDrawerItemSelected(mViewItem,0);
        }
    };
}

