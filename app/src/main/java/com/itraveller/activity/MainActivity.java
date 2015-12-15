package com.itraveller.activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.itraveller.R;
import com.itraveller.constant.Constants;
import com.itraveller.core.LoginScreenActivity;
import com.itraveller.volley.AppController;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import java.net.URL;


import com.google.analytics.tracking.android.EasyTracker;


public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private static String TAG = MainActivity.class.getSimpleName();

    SharedPreferences preferences;


    public static String u_id, at;

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private DrawerLayout mDrawerlayout;
    private SharedPreferences.Editor editor;
    public static final String MY_PREFS = "ScreenHeight";
    //private MaterialMenuDrawable materialMenu;
    public static TextView greeting;
    private ProfileTracker profileTracker;
    public static ImageView profilepic;
    Context context;
    public static String att, str1, str2, str3, str4;
    Fragment fragment;
    String title;
    private CallbackManager callbackManager;


     public void MainActivity(){

     }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        FacebookSdk.sdkInitialize(this.getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

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
        //drawerFragment.setContextValue(this);
        //display the first navigation drawer view on app launch
        //if((att.equals("unregistered") || att.equals("login_from_server") || !str1.equals("unregistered") || !str1.equals("login_from_server") || preferences.getInt("flag",0)==1 || preferences.getInt("temp",0)==1)  && !str1.equals("x") )
        //Log.d("String test test111", "" + preferences.getInt("login_flag", 0) + " " + preferences.getInt("flag", 0) + " " + str1 + " " + str3);
        if(SharedPreferenceRetrive().getString("back","0").equalsIgnoreCase("0")) {
            if (SharedPreferenceRetrive().getString("skipbit", "0").equalsIgnoreCase("0")) {
                displayView(4);
            } else {
                displayView(0);
            }
        } else {
            displayView(0);
            SharedPreferences prefs=getSharedPreferences("Preferences",MODE_PRIVATE);
            SharedPreferences.Editor editor=prefs.edit();
            editor.putString("back", "0").commit();
        }
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Call the 'deactivateApp' method to log an app event for use in analytics and advertising
        // reporting.  Do so in the onPause methods of the primary Activities that an app may be
        // launched into.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            profileTracker.stopTracking();
        } catch (Exception e) {
        }

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
       displayView(position);
    }


    private void displayView(int position) {

        fragment = null;
        title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new MaterialLandingActivity();
                title = getString(R.string.title_home);
                if (SharedPreferenceRetrive().getString("serverCheck","User").equalsIgnoreCase("facebook")) {
                    //profilepic.setImageResource(R.drawable.ic_profile_pic);
                    FacebookImage();
                } else {
                    //profilepic.setImageResource(R.drawable.ic_profile);
                    ServerImage();
                }
                break;

            case 1:
                fragment = new HowItWorksFragment();
                title = "How it works";
                break;

            case 2:
                fragment = new FriendsFragment();
                title = getString(R.string.title_friends);
                break;

            case 3:
                fragment = new MessagesFragment();
                title = getString(R.string.title_messages);
                break;

            case 4:

                LoginScreenActivity fragment1 = new LoginScreenActivity();
                title = getString(R.string.title_login);
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
    public void setActionBarNavDrawer(){
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
        } catch (Exception e){

        }
    }
    public void ServerImage(){
            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_profile);
            profilepic.setImageBitmap(getCroppedBitmap(icon));
            greeting.setText("Hello " + SharedPreferenceRetrive().getString("name", "User"));

    }
}
