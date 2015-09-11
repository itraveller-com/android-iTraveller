package com.itraveller.core;

/**
 * Created by VNK on 8/16/2015.
 */
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.itraveller.R;
import com.itraveller.activity.MainActivity;

public class SplashScreenActivity extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {

                SharedPreferences prefs=getSharedPreferences("Preferences",MODE_PRIVATE);
                if(String.valueOf(prefs.getInt("flag", 0)).equals(null))
                {
                    SharedPreferences.Editor editor=prefs.edit();
                    editor.putInt("flag",0);
                    editor.commit();
                }

                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                i.putExtra("profile","x");
                i.putExtra("id","x");
                i.putExtra("fname","x");
                i.putExtra("AccessToken","x");
                startActivity(i);



                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}



