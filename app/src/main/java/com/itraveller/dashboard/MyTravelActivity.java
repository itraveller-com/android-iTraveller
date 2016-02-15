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


    public static TextView sGreeting;
    public static ImageView sProfilePic;
    Toolbar mToolbar;
    private MyTravelFragmentDrawer mDrawerFragment;
    Fragment mFragment;
    String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setting the layout of screen
        setContentView(R.layout.activity_travel);

        //Textview used as greeting means for displaying "hello user_name"
        sGreeting = (TextView) findViewById(R.id.greeting);
        sProfilePic = (ImageView) findViewById(R.id.image);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().show();

        mDrawerFragment = (MyTravelFragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        mDrawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        mDrawerFragment.setDrawerListener(this);

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

        mFragment = null;
//        title = getString(R.string.app_name);
        switch (position) {
            case 0:

                Intent intent= new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                break;

            case 1:

                mFragment = new BookedTripsFragment();
                mTitle = "Your Booked Trips";
                break;

            default:
                break;

        }

        if (mFragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, mFragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(mTitle);
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