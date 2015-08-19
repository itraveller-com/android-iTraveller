package com.itraveller.activity;

/**
 * Created by I TRAVELLES on 04-08-2015.
 */

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;



/**
 * Created by I TRAVELLES on 03-08-2015.
 */

import com.itraveller.R;

public class SignupActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set View to register.xml
        setContentView(R.layout.sign_up);

        TextView loginScreen = (TextView) findViewById(R.id.link_to_login);

        // Listening to Login Screen link
        loginScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // Switching to Login Screen/closing register screen
                finish();
            }
        });
    }
}