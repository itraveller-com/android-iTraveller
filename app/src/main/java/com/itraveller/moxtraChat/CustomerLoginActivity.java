package com.itraveller.moxtraChat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itraveller.R;
import com.itraveller.volley.AppController;
import com.moxtra.binder.sdk.InviteToChatCallback;
import com.moxtra.sdk.MXAccountManager;
import com.moxtra.sdk.MXChatManager;
import com.moxtra.sdk.MXException;
import com.moxtra.sdk.MXGroupChatSession;
import com.moxtra.sdk.MXGroupChatSessionCallback;
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

import java.io.IOException;
import java.util.List;


/**
 * A login screen that offers login via email/password.
 */
public class CustomerLoginActivity extends Activity implements MXAccountManager.MXAccountLinkListener, MXChatManager.OnCreateChatListener, MXChatManager.OnOpenChatListener {

    private static final String TAG = "CustomerLoginActivity";
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    private AccessTokenModel agentAccessTokenModel=null,userAccessTokenModel=null;
    private AddUserResponse addUserResponse=null;
    private  CreateBinderResponse createBinderResponse=null;
    // UI references.
    private View mProgressView;
    String email;
    private String regid;
    private MXGroupChatSession session;
    List<MXGroupChatSession> sessions;
    private String SENDER_ID = "132433516320";
    private GoogleCloudMessaging gcm;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.moxtra_layout);
        mProgressView = findViewById(R.id.login_progress);

        // Set up the login form.


        SharedPreferences prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        email=""+prefs.getString("email","");

        String userId=PreferenceUtil.getUserGcmRegId(AppController.context);

        if(!PreferenceUtil.getUser(AppController.context).isEmpty() || PreferenceUtil.getUser(AppController.context)!=null)
        {
                    if (PreferenceUtil.getUser(AppController.context).equalsIgnoreCase(email))
                    {
                        registerInBackground();
                    }
                    else
                    {
                        PreferenceUtil.saveUser(AppController.context, email);

                        if (userId == null || userId.isEmpty())
                            registerForAccessTokenInBackground();
                        else
                            registerInBackground();
                    }

                }
                //registerInBackground();


        MXChatManager.getInstance().setOnMeetEndListener(new MXChatManager.OnEndMeetListener() {
            @Override
            public void onMeetEnded(String meetId) {
                //adapter.refreshData();
            }
        });

        MXChatManager.getInstance().setGroupChatSessionCallback(new MXGroupChatSessionCallback() {
            @Override
            public void onGroupChatSessionCreated(MXGroupChatSession session) {
                //adapter.refreshData();
            }

            @Override
            public void onGroupChatSessionUpdated(MXGroupChatSession session) {
                //adapter.refreshData();
            }

            @Override
            public void onGroupChatSessionDeleted(MXGroupChatSession session) {
                //adapter.refreshData();
            }
        });

        MXChatManager.getInstance().setChatInviteCallback(new InviteToChatCallback() {
            @Override
            public void onInviteToChat(String binderID, Bundle extras) {
                Log.d(TAG, "Invite to binder: " + binderID);
            }
        });

        //selectedUser = userLisToSelect.get(i);
        //Log.d(TAG, "Selected user: " + selectedUser.firstName);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    /**
     * Move to Moxtra Chat Window
     */
    private void startChatListActivity() {
        Log.i(TAG, "startChatListActivity");


        try {
            String strBIdner_id=PreferenceUtil.getBinderId(AppController.context);
            MXChatManager.getInstance().openChat(PreferenceUtil.getBinderId(AppController.context),  CustomerLoginActivity.this);//"BDOVcBOwued9RfCFBetl1VB"
            this.finish();
        } catch (MXException.AccountManagerIsNotValid accountManagerIsNotValid) {
            Log.e(TAG, "Error when create chat", accountManagerIsNotValid);
        }


    }

    //Link to the moxtra account
    @Override
    public void onLinkAccountDone(boolean success) {
        if (success) {
            Log.i(TAG, "Linked to moxtra account successfully.");

            startChatListActivity();
        } else {
            Toast.makeText(this, "Failed to setup moxtra user.", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Failed to setup moxtra user.");
            showProgress(false);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.moxtra.moxiechat/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.moxtra.moxiechat/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }




    private void setupMoxtraUser() {
        if (MXAccountManager.getInstance().isLinked()) {
            Log.i(TAG, "Moxtra user is already linked.");
            startChatListActivity();
        } else {
            Log.i(TAG, "Moxtra user is not linked yet.");
            regid = getRegistrationId(this);
            if (TextUtils.isEmpty(regid)) {
                Log.i(TAG, "Register in background.");
                registerInBackground();
            } else {
                registerInBackground();
            }
        }
    }


    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);


            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);

        }
    }

    @Override
    public void onCreateChatSuccess(String binderId) {
        Log.i(TAG, "Create Chat Success. The binderId = " + binderId);
    }

    @Override
    public void onCreateChatFailed(int i, String s) {
        Log.e(TAG, "Failed to create chat with code: " + i + ", msg: " + s);
        Toast.makeText(this, "Failed to create chat: " + s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onOpenChatSuccess() {
        Log.i(TAG, "Open chat success.");
    }

    @Override
    public void onOpenChatFailed(int i, String s) {
        Log.e(TAG, "Failed to open chat with code: " + i + ", msg: " + s);
        Toast.makeText(this, "Failed to open chat: " + s, Toast.LENGTH_LONG).show();
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p/>
     * Stores the registration ID and the app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (mProgressView != null) {
                    mProgressView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(CustomerLoginActivity.this);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // Save the regid in Moxtra so we can get the GCM notification.
                    try {

                        Bitmap avatar = BitmapFactory.decodeStream(CustomerLoginActivity.this.getAssets().open("FA01.png"));
                        final MXSDKConfig.MXUserInfo mxUserInfo = new MXSDKConfig.MXUserInfo(PreferenceUtil.getUser(AppController.context), MXSDKConfig.MXUserIdentityType.IdentityUniqueId);
                        final MXSDKConfig.MXProfileInfo mxProfileInfo = new MXSDKConfig.MXProfileInfo(PreferenceUtil.getUser(AppController.context), "user", avatar);
                        MXAccountManager.getInstance().setupUser(mxUserInfo, mxProfileInfo, null, regid, CustomerLoginActivity.this);
                    } catch (IOException e) {
                        Log.e(TAG, "Can't decode avatar.", e);
                    }

                    // Persist the regID - no need to register again.
                    PreferenceUtil.setUserGcmRegId(CustomerLoginActivity.this, regid);
                    PreferenceUtil.setAppVersion(CustomerLoginActivity.this, getAppVersion(CustomerLoginActivity.this));
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.e(TAG, "Error when register on GCM.", ex);
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (mProgressView != null) {
                    mProgressView.setVisibility(View.GONE);
                }
                Log.d(TAG, "Reg done: " + msg);
            }
        }.execute(null, null, null);
    }

    private String getRegistrationId(Context context) {
        String registrationId = PreferenceUtil.getUserGcmRegId(context);
        if (TextUtils.isEmpty(registrationId)) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = PreferenceUtil.getAppVersion(this);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }


    private void registerForAccessTokenInBackground() {
        new AsyncTask<Void, Void, AddUserResponse>() {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (mProgressView != null) {
                    mProgressView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected AddUserResponse doInBackground(Void... params) {
                String msg = "";
                try {



                        //create object for user accesstoken
                        userAccessTokenModel = new AccessTokenModel();
                        /**
                         * @params accessTokenModel Object, userName, firstname
                         */
                        userAccessTokenModel = getAccessToken(userAccessTokenModel, PreferenceUtil.getUser(AppController.context), PreferenceUtil.getUser(AppController.context));
//                    }
                    if(!userAccessTokenModel.getAccesstoken().isEmpty()) {
                        //create User in Binder

                        createBinderResponse = new CreateBinderResponse();
                        //create Binder method
                        //create Binder for or meet topic
                        createBinder(PreferenceUtil.getTokenId(AppController.context));
                        addUserResponse = new AddUserResponse();
                        addUserResponse = createUserInBinder(addUserResponse);
                    }

                } catch (Exception ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.e(TAG, "Error when register on GCM.", ex);
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }return addUserResponse;
            }

            @Override
            protected void onPostExecute(AddUserResponse msg) {

                if (msg.getCode().equals("RESPONSE_SUCCESS")){
                    setupMoxtraUser();
                }
                else{

                }
                if (mProgressView != null) {
                    mProgressView.setVisibility(View.GONE);


                }
            }

        }.execute(null, null, null);

    }



public AccessTokenModel getAccessToken(AccessTokenModel accessTokenModel,String firtName,String usrName){


/**
 * generate Timestamp in milisecond
 */
        long time = System.currentTimeMillis();

        try {
            //AccessToken URL
            final String url = "https://apisandbox.moxtra.com/oauth/token?client_id="+AppController.context.getString(R.string.client_id)+"&client_secret="+AppController.context.getString(R.string.client_secret)+"&grant_type=http://www.moxtra.com/auth_uniqueid&uniqueid="+usrName+"&firstname="+firtName+"&timestamp=" + time;


            /**
             * RestApi Call
             */
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());


            /**
             * Java class for Token
             */
            accessTokenModel = restTemplate.getForObject(url, AccessTokenModel.class);


            System.out.print("Token" + accessTokenModel.getAccesstoken());


        }catch (Exception e){

            return null;
        }

            return accessTokenModel;
    }


    private AddUserResponse createUserInBinder(AddUserResponse addUserResponse){


        try {


            // Simulate network access.
            // Set the Content-Type header
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(new MediaType("application", "json"));

            //User phone number json format
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject("{\"users\":[{\"user\":{\"unique_id\":"+ PreferenceUtil.getUser(AppController.context)+"}}]}");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            HttpEntity<String> requestEntity = new HttpEntity<String>(jsonObject.toString(), requestHeaders);


            //add a user to the Binder
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            final String url = "https://apisandbox.moxtra.com/v1/" + PreferenceUtil.getBinderId(AppController.context) + "/addteamuser?access_token=" + PreferenceUtil.getTokenId(AppController.context);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            String result = responseEntity.getBody();

/**
 * Gson Lib for serialize the object
 */
            Gson gson = new GsonBuilder().serializeNulls().create();
            gson = new GsonBuilder().serializeNulls().create();
            //Parse the user response
            addUserResponse = gson.fromJson(result,
                    AddUserResponse.class);




            return addUserResponse;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return addUserResponse;
    }

    private CreateBinderResponse createBinder(String mAccessToken) {
        String msg = "";


        try {
            createBinderResponse = new CreateBinderResponse();
            CreateBinderRequest createBidnerRequest = new CreateBinderRequest();
            createBidnerRequest.setName(PreferenceUtil.getUser(AppController.context));

            // Set the Content-Type header
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(new MediaType("application", "json"));
            HttpEntity<CreateBinderRequest> requestEntity = new HttpEntity<CreateBinderRequest>(createBidnerRequest, requestHeaders);


            final String url = "https://apisandbox.moxtra.com/v1/me/binders?access_token=" + mAccessToken;


            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());


            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            String result = responseEntity.getBody();




            Gson gson = new GsonBuilder().serializeNulls().create();
            gson = new GsonBuilder().serializeNulls().create();


            createBinderResponse = gson.fromJson(result,
                    CreateBinderResponse.class);
            CreateBinderResponse.BinderData binderData = createBinderResponse.new BinderData();


            binderData.setId(createBinderResponse.getData().getId());
            binderData.setName(createBinderResponse.getData().getName());
            binderData.setCreaetd_time(createBinderResponse.getData().getCreaetd_time());
            binderData.setCreaetd_time(createBinderResponse.getData().getUpdated_time());
            PreferenceUtil.setBinderId(AppController.context, binderData.getId());

            Log.d(TAG, "Binder ID================: " + binderData.getId());





        } catch (Exception e) {
            Log.e(TAG, "Error when register of binder.", e);
        }
        return  createBinderResponse;
    }

}

