package com.itraveller.moxtraChat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itraveller.R;
import com.itraveller.volley.AppController;
import com.melnykov.fab.FloatingActionButton;
import com.moxtra.sdk.MXAccountManager;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by blade on 5/13/15.
 */
public class MoxtraActivity extends FragmentActivity implements MXChatManager.OnOpenChatForFragmentListener,View.OnClickListener,MXAccountManager.MXAccountLinkListener ,MXAccountManager.MXAccountUnlinkListener, MXChatManager.OnOpenChatListener {

    private FloatingActionButton fab;

    SharedPreferences prefs;


    private static final String Binder_ID="BdVZvNgCvTXHzO7klSsivTC";
    private static final String TAG = "AgentLoginActivity";

    private String regid;
    private AccessTokenModel agentAccessTokenModel=null,userAccessToken;
    private CreateBinderResponse createBinderResponse=null;
    private AddUserResponse addUserResponse=null;

    private String SENDER_ID = "132433516320";
    private GoogleCloudMessaging gcm;
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.moxtra_activity);

        fab=(FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(this);

        fab.setVisibility(View.GONE);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

//        try
//        {
//            MXChatManager.getInstance().openChatForFragment(PreferenceUtil.getBinderId(AppController.context), null, this);
//        }
//        catch (MXException.AccountManagerIsNotValid accountManagerIsNotValid)
//        {
//            accountManagerIsNotValid.printStackTrace();
//        }

        //mProgressView = findViewById(R.id.login_progress);

        prefs=getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        Log.d("Token test", "" + PreferenceUtil.getTokenId(AppController.context));

        if((""+PreferenceUtil.getTokenId(AppController.context)).isEmpty())
            registerForAccessTokenInBackground();
        else
            setupMoxtraUser();

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onOpenChatForFragmentSuccess(String s, android.support.v4.app.Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.chatFragment,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onOpenChatForFragmentFailed(int i, String s) {
            Log.i("onOpenChatForFragment", s);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            new MaterialDialog.Builder(this)
                    .title(R.string.selectUserTitle)
                    .items(getUserList())
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {

                        }
                    })
                    .show();
        }
    }


    private String[] getUserList() {
        List<String> users = new ArrayList<>(3);
        users.add("Ground Chat Support");
        users.add("Travel Documents");
        users.add("Snaps");
        String[] userNames = new String[users.size()];
        users.toArray(userNames);
        return userNames;
    }

    private void registerForAccessTokenInBackground() {
        new AsyncTask<Void, Void, AddUserResponse>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                if (mProgressView != null) {
//                    mProgressView.setVisibility(View.VISIBLE);
//                }
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
                    agentAccessTokenModel = getAccessToken(agentAccessTokenModel, "Rohan_admin", "Rohan_admin");


                    if(!agentAccessTokenModel.getAccesstoken().isEmpty())
                    {
                        PreferenceUtil.setTokenId(AppController.context,agentAccessTokenModel.getAccesstoken());

                        userAccessToken = getAccessToken(agentAccessTokenModel, ""+prefs.getString("name",""),""+prefs.getString("email",""));

                        //if(!PreferenceUtil.getBinderId(AppController.context).isEmpty()) {
                        //createBinderResponse = new CreateBinderResponse();
                        //create Binder method
                        //create Binder for or meet topic
                        //createBinder(PreferenceUtil.getTokenId(AppController.context));
                        //}

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

                if (msg.getCode().equalsIgnoreCase("RESPONSE_SUCCESS")){
                    setupMoxtraUser();
                }
                else{
                    Log.d("MOXTRA","Hello");
                }
//                if (mProgressView != null) {
//                    mProgressView.setVisibility(View.GONE);
//
//
//                }
            }

        }.execute(null, null, null);

    }

    /**
     * Move to Moxtra Chat Window
     */
    private void startChatListActivity() {
        Log.i(TAG, "startChatListActivity");

        if (("" + prefs.getString("email", "")).contains("support@itraveller.com"))
        {
            Intent en = new Intent(MoxtraActivity.this, ChatListActivity.class);
            startActivity(en);
            this.finish();
        }
        else
        {
            try {

                MXChatManager.getInstance().openChat(Binder_ID, MoxtraActivity.this);//"BDOVcBOwued9RfCFBetl1VB"
                //MXChatManager.getInstance().openChatForFragment(Binder_ID, null, this);
                //MXChatManager.getInstance().openChatForFragment(Binder_ID,  MoxtraActivity.this);//"BDOVcBOwued9RfCFBetl1VB"
                this.finish();
            } catch (MXException.AccountManagerIsNotValid accountManagerIsNotValid) {
                Log.e(TAG, "Error when create chat", accountManagerIsNotValid);
            }
        }
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

    private AddUserResponse createUserInBinder(AddUserResponse addUserResponse){


        try
        {
            // Simulate network access.
            // Set the Content-Type header
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(new MediaType("application", "json"));

            //User phone number json format
            JSONObject jsonObject = null;
            try {

                jsonObject = new JSONObject("{\"users\":[{\"user\":{\"unique_id\":"+"\"" +""+prefs.getString("email","")+"\""+"}}]}");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            HttpEntity<String> requestEntity = new HttpEntity<String>(jsonObject.toString(), requestHeaders);


            //add a user to the Binder
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            final String url = "https://apisandbox.moxtra.com/v1/" + Binder_ID + "/addteamuser?access_token=" + PreferenceUtil.getTokenId(AppController.context);

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
//                if (mProgressView != null) {
//                    mProgressView.setVisibility(View.VISIBLE);
//                }
            }

            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(MoxtraActivity.this);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // Save the regid in Moxtra so we can get the GCM notification.
                    try {

                        Bitmap avatar = BitmapFactory.decodeStream(MoxtraActivity.this.getAssets().open("FA01.png"));
                        final MXSDKConfig.MXUserInfo mxUserInfo = new MXSDKConfig.MXUserInfo(""+prefs.getString("email",""), MXSDKConfig.MXUserIdentityType.IdentityUniqueId);
                        final MXSDKConfig.MXProfileInfo mxProfileInfo = new MXSDKConfig.MXProfileInfo(""+prefs.getString("name",""), ""+prefs.getString("name",""), avatar);
                        MXAccountManager.getInstance().setupUser(mxUserInfo, mxProfileInfo, null, regid, MoxtraActivity.this);
                    } catch (IOException e) {
                        Log.e(TAG, "Can't decode avatar.", e);
                    }

                    // Persist the regID - no need to register again.
                    PreferenceUtil.setUserGcmRegId(MoxtraActivity.this, regid);
                    PreferenceUtil.setAppVersion(MoxtraActivity.this, getAppVersion(MoxtraActivity.this));
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
//                if (mProgressView != null) {
//                    mProgressView.setVisibility(View.GONE);
//                }
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
    public void onUnlinkAccountDone(MXSDKConfig.MXUserInfo mxUserInfo) {

        Log.i(TAG, "Unlinked moxtra account: " + mxUserInfo);
        if (mxUserInfo == null) {
            Log.e(TAG, "Can't logout: the mxUserInfo is null.");
            Toast.makeText(this, "unlink failed as mxUserInfo is null.", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onStop() {
        super.onStop();

        client.disconnect();
    }

    @Override
    public void onOpenChatSuccess() {

    }

    @Override
    public void onOpenChatFailed(int i, String s) {

    }
}
