package com.itraveller.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.itraveller.R;
import com.itraveller.activity.MainActivity;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by rohan bundelkhandi on 24/12/2015.
 */
public class MyTravelActivity extends AppCompatActivity implements MyTravelFragmentDrawer.FragmentDrawerListener {

    public static boolean isActive;
    public static TextView greeting;
    public static ImageView profilepic;
    Toolbar mToolbar;
    ListView upcoming_list, past_list;
    private MyTravelFragmentDrawer drawerFragment;
    Fragment fragment;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setting the layout of screen
        setContentView(R.layout.activity_travel);

        //Textview used as greeting means for displaying "hello user_name"
        greeting = (TextView) findViewById(R.id.greeting);
        profilepic = (ImageView) findViewById(R.id.image);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().show();

        drawerFragment = (MyTravelFragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        displayView(1);

    }


    //this method is used to convert image into circular form
    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public SharedPreferences SharedPreferenceRetrive() {
        SharedPreferences preferences = getSharedPreferences("Preferences", MODE_PRIVATE);
        return preferences;
    }


    public void onBackPressed() {
        Intent i=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        finish();
    }


    private void displayView(int position) {

        fragment = null;
//        title = getString(R.string.app_name);
        switch (position) {
            case 0:

                Intent intent= new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                break;

            case 1:

                fragment = new BookedTripsFragment();
                title = "Your Booked Trips";
                break;

            case 2:

                //Intent i2=new Intent(getApplicationContext(),CameraDownloadActivity.class);
                //startActivity(i2);
                //    fragment = new ProfileFragment();
                title = "Gallery";
                break;


            case 3:

                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                title = "Login";

                    SharedPreferences prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.clear();
                    editor.commit();

                    startActivity(i);
                    finish();

                break;

            default:
                break;

        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        isActive=true;
    }

    @Override
    public void onStop() {
        super.onStop();
//        isActive=false;
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }
}