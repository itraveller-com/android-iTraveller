package com.itraveller.activity;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginFragment;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;
import com.itraveller.R;
import com.itraveller.volley.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;



public class MainActivity extends ActionBarActivity implements FragmentDrawer.FragmentDrawerListener {

    private static String TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private DrawerLayout mDrawerlayout;
    private SharedPreferences.Editor editor;
    public static final String MY_PREFS = "ScreenHeight";
    //private MaterialMenuDrawable materialMenu;
    ProfilePictureView profilePictureView;
    private TextView greeting;
    private ProfileTracker profileTracker;
    public ImageView img1,img2;
    Context context;
    public static String  att,str1,str2,str3;
    TextView txt;
    Fragment fragment;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Textview used as greeting means for displaying "hello user_name"
        greeting = (TextView) findViewById(R.id.greeting);

        context= this;

        //code for receiving data from other activities
        Bundle bu=getIntent().getExtras();
        str1= "" +bu.getString("profile");
        str2=""+bu.getString("id");
        str3=""+bu.getString("fname");
        att=""+bu.getString("AccessToken");

        Log.d("AccessToken in Main",""+att);
        Log.d("profile in temp", "" + str1);
        Log.d("fname in temp", "" + str3);

        //ImageView used for displaying image in navigation bar
        img1=(ImageView) findViewById(R.id.image);
        // img2=(ImageView) findViewById(R.id.imageView1);
        //Log.d("IFTHEN",""+(!str1.equals("x") && !str2.equals("x") && !str3.equals("x")));

        //if user is logged in using facebook
        if(!str1.equals("unregistered") && !str2.equals("unregistered") && !str3.equals("unregistered") && !str1.equals("sign_up") && !att.equals("sign_up") && !str1.equals("login_from_server") && !str2.equals("login_from_server")){
            try {
                URL imgUrl = new URL("https://graph.facebook.com/" + str2 + "/picture");
                InputStream in = (InputStream) imgUrl.getContent();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                //getCroppedBitmap() method is called to convert fetched image into circular form
                img1.setImageBitmap(getCroppedBitmap(bitmap));
                Log.d("Fname of user1",""+getString(R.string.hello_user,LoginActivity.profile.getFirstName()));
                greeting.setText(getString(R.string.hello_user, LoginActivity.profile.getFirstName()));

            } catch (Exception e) {
                Log.d("Eception", "Caught");
                e.printStackTrace();
            }

            //used for keeping track of user profile
            profileTracker = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

                    //calling logout1 method if user clicks logout button
                    logout1();

                }
            };
        }
     /*   else if(str1.equals("sign_up") && att.equals("sign_up"))
        {
            Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.ic_profile);
            //display defult image and greetin to unregistered user
            Log.d("Signup user","hi");
            img1.setImageBitmap(getCroppedBitmap(icon));
            greeting.setText("Hello "+str3 );
        } */
        //if user is not logged in using facebook
        else if(str1.equals("login_from_server") && str2.equals("login_from_server"))
        {
            Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.ic_profile);
            //display defult image and greetin to unregistered user
            Log.d("Login through server","hi");
            Log.d("AcesToken", "" + att);
            img1.setImageBitmap(getCroppedBitmap(icon));
            greeting.setText("Hello " + str3);

        }
        else
        {

            Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.ic_profile);
            //display defult image and greetin to unregistered user
            img1.setImageBitmap(getCroppedBitmap(icon));
            greeting.setText("Hello user");
        }



        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black);
        // getSupportActionBar().setTitle("");
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black);
        //getSupportActionBar().show();

        mToolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        getSupportActionBar().show();

        // materialMenu = new MaterialMenuDrawable(this, Color.WHITE, Stroke.THIN);

        //To save Screen, Actionbar and Statusbar Height
        editor = getSharedPreferences(MY_PREFS, MODE_PRIVATE).edit();
        editor.putInt("Status_Height",getStatusBarHeight());
        editor.putInt("Screen_Height",getScreenHeight());
        editor.putInt("Screen_Width", getScreenWidth());
        editor.commit();
        Log.i("iTraveller", "Status_Height " + getStatusBarHeight());
        Log.i("iTraveller", "Height " + getScreenHeight());
        Log.i("iTraveller", "Width " + getScreenWidth());



        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        //drawerFragment.setContextValue(this);


        // display the first navigation drawer view on app launch
        displayView(0);
    }


    //this method is used to convert image into circular form
    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Log.d("Icheck","inside get");
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
        }
        catch(Exception e) {
            Log.d("HelloCC","caught"+e);

        }

    }
    //this method is called if user clicks logot button
    public void logout1()
    {
        //set AccessToken as null
        //    AccessToken.setCurrentAccessToken(null);

        Intent i=new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(i);
        finish();

    }



    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public int getScreenHeight(){
        //Screen Height
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        return height;
    }

    public int getScreenWidth(){
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



    public boolean onPrepareOptionsMenu(Menu menu)
    {
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
    public void logout_from_server(int test)
    {
        String tag_json_obj = "json_obj_req";
        String u_id = null,at = null;

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Signing out...");
        pDialog.show();

        switch(test) {
            case 1:
                u_id = LoginActivity.user_id;
                at = LoginActivity.access_token;
                Log.d("Accesstoken",""+at);
                break;
            case 2:
                u_id = LoginFragmentA.user_id;
                at = LoginFragmentA.access_token;
                break;
            default:
                break;
        }
        String url="http://stage.itraveller.com/backend/api/v1/users/"+u_id+"/logout?token="+at;


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Log.d("You are in try","HIII");
                    JSONObject jobj=new JSONObject(response.toString());
                    String success=jobj.getString("success");
                    Log.d("Success string",""+success.equals("true"));
                    if(success.equals("true"))
                    {
//                                Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                        Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.ic_profile);
                        LoginActivity.access_token=null;
                        att=null;
                        LoginFragmentA fragment1 = new LoginFragmentA();
                        fragment1.setContextValue(context);
                        title = getString(R.string.title_login);
                        fragment = fragment1;
//                                 startActivity(i);
                        //display defult image and greetin to unregistered user
                        img1.setImageBitmap(getCroppedBitmap(icon));
                        greeting.setText("Hello user");
                        finish();

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Logout failed !!",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



                pDialog.hide();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                pDialog.hide();
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);




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
                fragment = new LandingActivity();
                title = getString(R.string.title_home);
                break;
            case 1:
                fragment = new FriendsFragment();
                title = getString(R.string.title_friends);

                break;
            case 2:
                fragment = new MessagesFragment();
                title = getString(R.string.title_messages);
                break;
            case 3:
                Log.d("AccessToken before if",""+att);
                if(att.equals("unregistered")) {
                    Log.d("Inside at","accesstoken is null");
                    LoginFragmentA fragment1 = new LoginFragmentA();
                    fragment1.setContextValue(context);
                    title = getString(R.string.title_login);
                    fragment = fragment1;
                    Log.d("At is","NUll");
                }
                else
                {

                }
                break;
            case 4:
                if(att.equals("login_from_server") )
                {
                    if(LoginActivity.count==1)
                        logout_from_server(1);
                    else
                        logout_from_server(2);
                }
                else
                {
                    LoginManager.getInstance().logOut();
                }
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
}
