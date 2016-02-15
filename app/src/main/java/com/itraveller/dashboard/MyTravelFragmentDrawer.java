package com.itraveller.dashboard;

/**
 * Created by rohan bundelkhandi on 24/12/2015.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.itraveller.R;
import com.itraveller.activity.FragmentDrawer;
import com.itraveller.adapter.MyTravelNavigationDrawerAdapter;
import com.itraveller.adapter.NavigationDrawerAdapter;
import com.itraveller.model.NavDrawerItem;

import java.util.ArrayList;
import java.util.List;


public class MyTravelFragmentDrawer extends Fragment {

    SharedPreferences preferences;
    Fragment fragment;
    String att;
    private static String TAG = FragmentDrawer.class.getSimpleName();
    private RecyclerView recyclerView;
    public static ActionBarDrawerToggle mDrawerToggle;
    public static DrawerLayout mDrawerLayout;
    private MyTravelNavigationDrawerAdapter adapter;
    private View containerView;
    public static String[] titles = null;
    private FragmentDrawerListener drawerListener;
    private TypedArray navMenuIcons;
    String str1;
    Button chatBtn;

    public MyTravelFragmentDrawer() {
    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    public FragmentDrawerListener getDrawerListener(){
        return drawerListener;
    }
    public List<NavDrawerItem> getData() {
        List<NavDrawerItem> data = new ArrayList<>();

        SharedPreferences prefs=this.getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        //if user is already logged in then changing "Login" to "Logout"
        Log.d("FragmentDrawer", "Entering in it " + prefs.getString("skipbit", "0"));
        /*if(prefs.getString("skipbit","0").equalsIgnoreCase("0")) {
            titles[4]=titles[4].replace(""+titles[4],"Login");
        }
        else{
            titles[4]=titles[4].replace(""+titles[4],"Logout");
        }*/
        // preparing navigation drawer items
        for (int i = 0; i < titles.length; i++) {
            NavDrawerItem navItem = new NavDrawerItem();
            navItem.setTitle(titles[i]);
            navItem.setIcon(navMenuIcons.getResourceId(i,-1));
            data.add(navItem);
        }

        navMenuIcons.recycle();
        return data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // drawer labels
        titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels_);
        navMenuIcons=getActivity().getResources().obtainTypedArray(R.array.nav_drawer_icons_);
    }

    public void updateDrawer() {
        adapter.notifyDataSetChanged();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        //mDrawerLayout.isDrawerOpen(Gravity.RIGHT);
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        adapter = new MyTravelNavigationDrawerAdapter(getActivity(), getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return layout;
    }

    public SharedPreferences SharedPreferenceRetrive() {
        SharedPreferences preferences = getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        return preferences;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar , R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
                mDrawerLayout.setClickable(true);
                updateDrawer();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
                mDrawerLayout.setClickable(false);
                updateDrawer();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
                updateDrawer();

            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    static abstract class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }
    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }
}

