package com.itraveller.chat;

/**
 * Created by rohan bundelkhandi on 17/12/2015.
 */

        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.util.Log;
        import android.widget.Toast;

        import com.itraveller.gcm.chat_files.GCMRegistrar;
        import com.itraveller.materialsearch.SharedPreference;
        import com.itraveller.volley.AppController;

public class GCMRegistrationActivity extends Activity {
    // label to display gcm messages
    AppController aController;

    // Asyntask
    AsyncTask<Void, Void, Void> mRegisterTask;

    public static String name;
    public static String email;
    SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get Global Controller Class object
        aController = (AppController) getApplicationContext();

        preferences=getSharedPreferences("Preferences",Context.MODE_PRIVATE);

        email=""+preferences.getString("email","");
        name=""+preferences.getString("name","");

        // Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(this);

        // Make sure the manifest permissions was properly set
        GCMRegistrar.checkManifest(this);

        // Get GCM registration id
        final String regId = GCMRegistrar.getRegistrationId(this);

        // Check if regid already presents
        if (regId.equals(""))
        {
            Log.i("GCM K", "--- Regid = ''" + regId);
            // Register with GCM
            GCMRegistrar.register(this, Config.GOOGLE_SENDER_ID);
        }
        else
        {
            // Device is already registered on GCM Server
            if (GCMRegistrar.isRegisteredOnServer(this))
            {
                final Context context = this;
                // Skips registration.
                Toast.makeText(getApplicationContext(), "Already registered with GCM Server", Toast.LENGTH_LONG).show();
                Log.i("GCM K", "Already registered with GCM Server");
                //GCMRegistrar.unregister(context);
            }
            else
            {
                Log.i("GCM K", "-- gO for registration--");
                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.
                final Context context = this;

                mRegisterTask = new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {

                        // Register on our server
                        // On server creates a new user
                        aController.register(context, name, email, regId);

                        return null;
                    }
                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;

                        finish();
                    }
                };
                // execute AsyncTask
                mRegisterTask.execute(null, null, null);
            }
        }
    }
    @Override
    protected void onDestroy() {
        // Cancel AsyncTask
        super.onDestroy();
    }
}


