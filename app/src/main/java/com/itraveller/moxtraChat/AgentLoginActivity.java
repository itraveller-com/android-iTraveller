package com.itraveller.moxtraChat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itraveller.R;
import com.itraveller.moxtraChat.AccessTokenModel;
import com.itraveller.moxtraChat.AddUserResponse;
import com.itraveller.moxtraChat.CreateBinderResponse;
import com.itraveller.volley.AppController;
import com.moxtra.sdk.MXAccountManager;
import com.moxtra.sdk.MXAccountManager.MXAccountUnlinkListener;
import com.moxtra.sdk.MXChatManager;
import com.moxtra.sdk.MXException;
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


/**
 * A login screen that offers login via email/password.
 */
public class AgentLoginActivity extends Activity  implements  MXAccountUnlinkListener,MXAccountManager.MXAccountLinkListener, MXChatManager.OnCreateChatListener, MXChatManager.OnOpenChatListener {

    SharedPreferences prefs;


    private static final String TAG = "AgentLoginActivity";

    private View mProgressView;

    private String regid;
    private AccessTokenModel agentAccessTokenModel=null,userAccessToken;
    private CreateBinderResponse createBinderResponse=null;
    private AddUserResponse addUserResponse=null;

    private String SENDER_ID = "132433516320";
    private GoogleCloudMessaging gcm;
    private GoogleApiClient client;

    String nameArray[]=new String[3];

    //String emailArray[]={"ciju2.support@itraveller.com","swetha2.support@itraveller.com"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.moxtra_layout);
        mProgressView = findViewById(R.id.login_progress);

        prefs=getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        nameArray[0]="Ciju22";
        nameArray[1]="Swetha22";
        nameArray[2]=""+prefs.getString("email","");


        if(PreferenceUtil.getBinderId(AppController.context).isEmpty())
            registerForAccessTokenInBackground();
        else
            setupMoxtraUser();


        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onOpenChatFailed(int i, String s) {
        Toast.makeText(AgentLoginActivity.this,"OnOPenChatFailed",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onOpenChatSuccess() {

    }

    @Override
    public void onCreateChatFailed(int i, String s) {
        Toast.makeText(AgentLoginActivity.this,"OnCreateChatFailed",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreateChatSuccess(String s) {

    }

    /**
     * Move to Moxtra Chat Window
     */
    private void startChatListActivity() {
        Log.i(TAG, "startChatListActivity");
        try {
            String strBIdner_id=PreferenceUtil.getBinderId(AppController.context);
            MXChatManager.getInstance().openChat(PreferenceUtil.getBinderId(AppController.context),  AgentLoginActivity.this);//"BDOVcBOwued9RfCFBetl1VB"
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
            //    showProgress(false);
        }
    }

    @Override
    public void onStart() {
        super.onStart();


        // AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        client.disconnect();
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p/>
     * Stores the registration ID and the app versionCode in the application's
     * shared preferences.
     */
/*
    *
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
                    agentAccessTokenModel = new AccessTokenModel();
                    /**
                     * @params accessTokenModel Object, userName, firstname
                     */

                    userAccessToken = new AccessTokenModel();
                    agentAccessTokenModel = getAccessToken(agentAccessTokenModel, "Rohan_admin1", "Rohan_admin1");

                    if(!agentAccessTokenModel.getAccesstoken().isEmpty())
                    {
                        PreferenceUtil.setTokenId(AppController.context,agentAccessTokenModel.getAccesstoken());

                        for(int i=0;i<nameArray.length;i++)
                            userAccessToken = getAccessToken(userAccessToken, nameArray[i],nameArray[i]);
                        //userAccessToken = getAccessToken(agentAccessTokenModel, ""+prefs.getString("name",""),""+prefs.getString("name",""));

                        //if(!PreferenceUtil.getBinderId(AppController.context).isEmpty()) {
                            createBinderResponse = new CreateBinderResponse();
                            //create Binder method
                            //create Binder for or meet topic
                            createBinder(PreferenceUtil.getTokenId(AppController.context));
                        //}

                        addUserResponse = new AddUserResponse();

                        for(int i=0;i<nameArray.length;i++)
                            addUserResponse = createUserInBinder(addUserResponse,nameArray[i]);
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

                if (msg.getCode().equalsIgnoreCase("RESPONSE_SUCCESS")){
                    setupMoxtraUser();
                }
                else{
                    Log.d("MOXTRA","Hello");
                }
                if (mProgressView != null) {
                    mProgressView.setVisibility(View.GONE);


                }
            }

        }.execute(null, null, null);

    }

    public AccessTokenModel getAccessToken(AccessTokenModel accessTokenModel,String firtName,String usrName)
    {


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
        }
        catch (Exception e)
        {
            return null;
        }

        return accessTokenModel;
    }

    private CreateBinderResponse createBinder(String mAccessToken) {
        String msg = "";


        try {
            createBinderResponse = new CreateBinderResponse();
            CreateBinderRequest createBidnerRequest = new CreateBinderRequest();
            createBidnerRequest.setName(PreferenceUtil.getUser(AppController.context)+" "+"ITravellerSupport");

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
        }
        catch (Exception e)
        {
            Log.e(TAG, "Error when register of binder.", e);
        }
        return  createBinderResponse;
    }

    private AddUserResponse createUserInBinder(AddUserResponse addUserResponse,String email){


        try
        {
            // Simulate network access.
            // Set the Content-Type header
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(new MediaType("application", "json"));

            //User phone number json format
            JSONObject jsonObject = null;
            try {

                jsonObject = new JSONObject("{\"users\":[{\"user\":{\"unique_id\":"+ "\""+email+"\""+"}}]}");
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
            addUserResponse = gson.fromJson(result, AddUserResponse.class);

            return addUserResponse;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return addUserResponse;
    }

    private void setupMoxtraUser() {
        if (MXAccountManager.getInstance().isLinked()) {
            Log.i(TAG, "Moxtra user is already linked.");
            startChatListActivity();
        }
        else
        {
            Log.i(TAG, "Moxtra user is not linked yet.");
            regid = getRegistrationId(this);

            if (TextUtils.isEmpty(regid)) {
                Log.i(TAG, "Register in background.");
                registerInBackground();
                Log.d("Registration TAG", "Not ID");
            }
            else
            {
                Log.d("Registration TAG", "Not ID");

                registerInBackground();
                //            startChatListActivity();
            }
        }
    }

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
//                    if (gcm == null) {
//                        gcm = GoogleCloudMessaging.getInstance(AgentLoginActivity.this);
//                    }
//                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // Save the regid in Moxtra so we can get the GCM notification.
                    try {

                        Bitmap avatar = BitmapFactory.decodeStream(AgentLoginActivity.this.getAssets().open("FA01.png"));
                        final MXSDKConfig.MXUserInfo mxUserInfo = new MXSDKConfig.MXUserInfo(""+prefs.getString("email",""), MXSDKConfig.MXUserIdentityType.IdentityUniqueId);
                        final MXSDKConfig.MXProfileInfo mxProfileInfo = new MXSDKConfig.MXProfileInfo(""+prefs.getString("email",""),""+prefs.getString("email",""), avatar);
                        MXAccountManager.getInstance().setupUser(mxUserInfo, mxProfileInfo, null, regid, AgentLoginActivity.this);
                    } catch (IOException e) {
                        Log.e(TAG, "Can't decode avatar.", e);
                    }

                    // Persist the regID - no need to register again.
                    PreferenceUtil.setUserGcmRegId(AgentLoginActivity.this, regid);
                    PreferenceUtil.setAppVersion(AgentLoginActivity.this, getAppVersion(AgentLoginActivity.this));
                } catch (Exception ex) {
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

    @Override
    public void onUnlinkAccountDone(MXSDKConfig.MXUserInfo mxUserInfo) {

        Log.i(TAG, "Unlinked moxtra account: " + mxUserInfo);
        if (mxUserInfo == null) {
            Log.e(TAG, "Can't logout: the mxUserInfo is null.");
            Toast.makeText(this, "unlink failed as mxUserInfo is null.", Toast.LENGTH_LONG).show();
        }
    }
}