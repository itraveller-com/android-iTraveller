package com.itraveller.moxtraChat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.itraveller.R;
import com.itraveller.volley.AppController;
import com.moxtra.sdk.MXAccountManager;
import com.moxtra.sdk.MXSDKConfig;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by rohan bundelkhandi on 28/01/2016.
 */
public class MoxtraMainActivity extends Activity implements  MXAccountManager.MXAccountLinkListener,MXAccountManager.MXAccountUnlinkListener,View.OnClickListener {


    private View mProgressView;
    private String tokenID=null;
    private static final String TAG = "MainActivity";
    private AccessTokenModel agentAccessTokenModel=null;

    @Override
    public void onLinkAccountDone(boolean success) {
        if (success) {
            Log.i(TAG, "Linked to moxtra account successfully.");


        } else {
            Toast.makeText(this, "Failed to setup moxtra user.", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Failed to setup moxtra user.");
            //    showProgress(false);
        }

    }

    @Override
    public void onUnlinkAccountDone(MXSDKConfig.MXUserInfo mxUserInfo) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.moxtra_layout);

        mProgressView = findViewById(R.id.login_progress);


        tokenID =PreferenceUtil.getTokenId(AppController.context);
        if(tokenID==null) {
            registerForAccessTokenInBackground();

            SharedPreferences prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

            if((""+prefs.getString("email","")).contains("@itraveller.com"))
            {
                startActivity(new Intent(MoxtraMainActivity.this, CustomerLoginActivity.class));
            }
            else
            {
                startActivity(new Intent(MoxtraMainActivity.this, AgentLoginActivity.class));
            }
        }
    }

    private void registerForAccessTokenInBackground() {

        new AsyncTask<Void, Void, AccessTokenModel>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (mProgressView != null) {
                    mProgressView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected AccessTokenModel doInBackground(Void... params) {
                String msg = "";
                try
                {

                    agentAccessTokenModel= new AccessTokenModel();
                    agentAccessTokenModel=  getAccessToken(agentAccessTokenModel,"instapaisa_agent01","instapaisa_agent01");

                }
                catch (Exception ex)
                {
                    msg = "Error :" + ex.getMessage();
                    Log.e(TAG, "Error when register on GCM.", ex);
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }

                return agentAccessTokenModel;
            }

            @Override
            protected void onPostExecute(AccessTokenModel msg) {

                if (!msg.getAccesstoken().isEmpty())
                {
                    //Save in preference for reference
                    PreferenceUtil.setTokenId(AppController.context, agentAccessTokenModel.getAccesstoken());

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

    @Override
    public void onClick(View view) {

    }
}
