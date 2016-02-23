package com.itraveller.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.itraveller.R;
import com.itraveller.core.LoginScreenActivity;
import com.itraveller.dashboard.MyTravelActivity;

import java.io.InputStream;
import java.net.URL;

//import com.google.analytics.tracking.android.EasyTracker;


public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    SharedPreferences preferences;
    public static String at;

    public static boolean active;

    public static Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private SharedPreferences.Editor editor;
    public static final String MY_PREFS = "ScreenHeight";
    public static TextView greeting;
    public static ImageView profilepic;
    Fragment fragment;
    String title;

    private enum AttachView {
        HOME(0), MATERIAL_LANDING(1), MY_TRAVEL(2), CONTACT_US(3), LOGIN(4);

        private int position;

        AttachView(int position) {
            this.position = position;
        }

        public int getPosition() {
            return position;
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        FacebookSdk.sdkInitialize(this.getApplicationContext());


        //setting the layout of screen
        setContentView(R.layout.activity_main);

        //setting the orientation of screen
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Textview used as greeting means for displaying "hello user_name"
        greeting = (TextView) findViewById(R.id.greeting);
        //greeting.setText("Hello " + SharedPreferenceRetrive().getString("name","User"));
        //ImageView used for displaying image in navigation bar
        profilepic = (ImageView) findViewById(R.id.image);
        //img1.setImageResource(R.drawable.ic_profile);
        //if user is logged in using facebook


        mToolbar = (Toolbar) findViewById(R.id.toolbar);


        if (Build.VERSION.SDK_INT >= 21) {
            // Set the status bar to dark-semi-transparentish
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // Set paddingTop of toolbar to height of status bar.
            // Fixes statusbar covers toolbar issue
            mToolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        }
        setSupportActionBar(mToolbar);
        getSupportActionBar().show();

        //To save Screen, Actionbar and Statusbar Height
        editor = getSharedPreferences(MY_PREFS, MODE_PRIVATE).edit();
        editor.putInt("Status_Height", getStatusBarHeight());
        editor.putInt("Screen_Height", getScreenHeight());
        editor.putInt("Screen_Width", getScreenWidth());
        editor.commit();

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        if (SharedPreferenceRetrive().getString("back", "0").equalsIgnoreCase("0")) {
            if (SharedPreferenceRetrive().getString("skipbit", "0").equalsIgnoreCase("0")) {
                displayView(AttachView.LOGIN);
            } else {
                displayView(AttachView.HOME);
            }
        } else {
            if (SharedPreferenceRetrive().getString("back", "0").equalsIgnoreCase("1"))
                displayView(AttachView.MATERIAL_LANDING);
            else
                displayView(AttachView.HOME);

            SharedPreferences prefs = getSharedPreferences("Preferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("back", "0").commit();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }



    //this method is used to convert image into circular form
    public static Bitmap getCroppedBitmap(Bitmap bitmap) {
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        active = true;
//        EasyTracker.getInstance(this).activityStart(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Main Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://com.itraveller.activity/http/host/path")
//        );
//        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Main Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://com.itraveller.activity/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
        active = false;
//        EasyTracker.getInstance(this).activityStop(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Call the 'deactivateApp' method to log an app event for use in analytics and advertising
        // reporting.  Do so in the onPause methods of the primary Activities that an app may be
        // launched into.
        AppEventsLogger.deactivateApp(this);
    }


    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public int getScreenHeight() {
        //Screen Height
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        return height;
    }

    public int getScreenWidth() {
        //Screen Width
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return width;
    }

    public int getActionBarHeight() {
        int height;
        height = getSupportActionBar().getHeight();
        return height;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.i("iTraveller", "ActionBar_Height " + getActionBarHeight());
        editor.putInt("ActionBar_Height", getActionBarHeight());
        editor.commit();
        /*getMenuInflater().inflate(R.menu.menu_main, menu);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        getSupportActionBar().show();*/
        return true;
    }


    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.action_search){
            Toast.makeText(getApplicationContext(), "Search action is selected!", Toast.LENGTH_SHORT).show();
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        for (AttachView attachView : AttachView.values()) {
            if (position == attachView.getPosition()) {
                displayView(attachView);
                break;
            }
        }

    }



    private void displayView(AttachView view) {

        fragment = null;
        title = getString(R.string.app_name);
        switch (view) {
            case HOME:
                fragment = new Home_Fragment();
                //fragment = new MaterialLandingActivity();
                title = "Welcome To itraveller";
                if (SharedPreferenceRetrive().getString("serverCheck", "User").equalsIgnoreCase("facebook")) {
                    //profilepic.setImageResource(R.drawable.ic_profile_pic);
                    FacebookImage();
                } else {
                    //profilepic.setImageResource(R.drawable.ic_profile);
                    ServerImage();
                }
                break;

            case MATERIAL_LANDING:
                fragment = new MaterialLandingActivity();
                title = "How it works";
                break;

            case MY_TRAVEL:

                Intent i = new Intent(getApplicationContext(), MyTravelActivity.class);
                startActivity(i);
                finish();
                //    fragment = new MyTravelFragment();
                //    title = getString(R.string.title_friends);
                break;

            case CONTACT_US:
                fragment = new ContactUsFragment();
                title = getString(R.string.title_messages);
                break;

            case LOGIN:

                LoginScreenActivity fragment1 = new LoginScreenActivity();
                title = ""; //getString(R.string.title_login);
                fragment = fragment1;
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            setActionBarNavDrawer();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    public SharedPreferences SharedPreferenceRetrive() {
        SharedPreferences preferences = getSharedPreferences("Preferences", MODE_PRIVATE);
        return preferences;
    }

    public void setActionBarNavDrawer() {
        FragmentDrawer.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        FragmentDrawer.mDrawerToggle.setDrawerIndicatorEnabled(true);
        getSupportActionBar().show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    public void FacebookImage() {
        try {
            Log.d("FacebookImg", "" + "https://graph.facebook.com/" + SharedPreferenceRetrive().getString("id", "") + "/picture");
            URL imgUrl = new URL("https://graph.facebook.com/" + SharedPreferenceRetrive().getString("id", "") + "/picture");
            InputStream in = (InputStream) imgUrl.getContent();
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            profilepic.setImageBitmap(getCroppedBitmap(bitmap));
            greeting.setText("Hello " + SharedPreferenceRetrive().getString("name", "User").toString());
        } catch (Exception e) {

        }
    }

    public void ServerImage() {
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_profile);
        profilepic.setImageBitmap(getCroppedBitmap(icon));
        greeting.setText("Hello " + SharedPreferenceRetrive().getString("name", "User"));

    }
}
