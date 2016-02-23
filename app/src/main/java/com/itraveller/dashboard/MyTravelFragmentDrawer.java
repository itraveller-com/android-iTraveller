package com.itraveller.dashboard;

/**
 * Created by rohan bundelkhandi on 24/12/2015.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.itraveller.R;
import com.itraveller.adapter.MyTravelNavigationDrawerAdapter;
import com.itraveller.model.NavDrawerItem;

import java.util.ArrayList;
import java.util.List;

public class MyTravelFragmentDrawer extends Fragment {

    private RecyclerView mRecyclerView;
    public static ActionBarDrawerToggle sDrawerToggle;
    public static DrawerLayout sDrawerLayout;
    private MyTravelNavigationDrawerAdapter myTravelNavigationDrawerAdapter;
    private View mContainer;
    public static String[] sTitles = null;
    private FragmentDrawerListener mDrawerListener;
    private TypedArray mMenuIcons;

    public MyTravelFragmentDrawer() {
    }

    public void setDrawerListener(FragmentDrawerListener mDrawerListener) {
        this.mDrawerListener = mDrawerListener;
    }

    public List<NavDrawerItem> getData() {
        List<NavDrawerItem> data = new ArrayList<>();
        // preparing navigation drawer items
        for (int i = 0; i < sTitles.length; i++) {
            NavDrawerItem navItem = new NavDrawerItem();
            navItem.setTitle(sTitles[i]);
            navItem.setIcon(mMenuIcons.getResourceId(i,-1));
            data.add(navItem);
        }

        mMenuIcons.recycle();
        return data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // drawer labels
        sTitles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels_);
        mMenuIcons=getActivity().getResources().obtainTypedArray(R.array.nav_drawer_icons_);
    }

    public void updateDrawer() {
        myTravelNavigationDrawerAdapter.notifyDataSetChanged();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View mLayout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        //mDrawerLayout.isDrawerOpen(Gravity.RIGHT);
        mRecyclerView = (RecyclerView) mLayout.findViewById(R.id.drawerList);
        myTravelNavigationDrawerAdapter = new MyTravelNavigationDrawerAdapter(getActivity(), getData());
        mRecyclerView.setAdapter(myTravelNavigationDrawerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return mLayout;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        mContainer = getActivity().findViewById(fragmentId);
        sDrawerLayout = drawerLayout;
        sDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar , R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
                sDrawerLayout.setClickable(true);
                updateDrawer();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
                sDrawerLayout.setClickable(false);
                updateDrawer();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
                updateDrawer();

            }
        };

        sDrawerLayout.setDrawerListener(sDrawerToggle);
        sDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                sDrawerToggle.syncState();
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

