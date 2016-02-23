package com.itraveller.adapter;

/**
 * Created by rohan bundelkhandi on 24/12/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itraveller.R;
import com.itraveller.dashboard.BookedTripsFragment;
import com.itraveller.activity.MainActivity;
import com.itraveller.dashboard.MyTravelFragmentDrawer;
import com.itraveller.model.NavDrawerItem;
import com.itraveller.moxtraChat.PreferenceUtil;
import com.moxtra.sdk.MXAccountManager;
import com.moxtra.sdk.MXSDKConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MyTravelNavigationDrawerAdapter extends RecyclerView.Adapter<MyTravelNavigationDrawerAdapter.MyViewHolder> implements MXAccountManager.MXAccountUnlinkListener{

    private List<NavDrawerItem> mData = Collections.emptyList();
    private List<MyViewHolder> mHolders = new ArrayList<MyViewHolder>();
    private LayoutInflater mInflater;
    private Context mContext;
    private static final String TAG = "MyTravelNavigationDrawerAdapter";

    public MyTravelNavigationDrawerAdapter(Context mContext, List<NavDrawerItem> mData) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.nav_drawer_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        mHolders.add(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final NavDrawerItem current = mData.get(position);

        holder.imgIcon.setImageResource(current.getIcon());
        if(position==2){
            if(SharedPreferenceRetrive().getString("skipbit","0").equalsIgnoreCase("0")){
                holder.title.setText("Login");
            } else {
                holder.title.setText("Logout");
            }
        } else {
            holder.title.setText(current.getTitle());
        }


        holder.nav_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment;
                String title;
                fragment = null;
                title = "iTraveller";
                switch (position) {
                    case 0:
                        Intent intent= new Intent(mContext, MainActivity.class);
                        mContext.startActivity(intent);
                        ((Activity)mContext).finish();

                        break;


                    case 1:

                        fragment = new BookedTripsFragment();
                        title = "How it works";
                        MyTravelFragmentDrawer.sDrawerLayout.closeDrawers();
                        break;


                    case 2:


                        title = "Login";
                        if(holder.title.getText().toString().equalsIgnoreCase("Logout")){
                            logout();
                        }
                        break;

                    default:
                        break;
                }

                if (fragment != null) {
                    FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.commit();
                }
            }
        });
    }

    public void delete(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    public SharedPreferences SharedPreferenceRetrive() {
        SharedPreferences preferences = mContext.getSharedPreferences("Preferences", mContext.MODE_PRIVATE);
        return preferences;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView imgIcon;
        RelativeLayout nav_row;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgIcon = (ImageView) itemView.findViewById(R.id.icon);
            title = (TextView) itemView.findViewById(R.id.title);
            nav_row = (RelativeLayout) itemView.findViewById(R.id.nav_drawer_row);
        }
    }

    protected void logout() {
        PreferenceUtil.removeUser(mContext);

        boolean ret = MXAccountManager.getInstance().unlinkAccount(this);
        if (!ret) {

            Log.e(TAG, "Can't logout: the unlinkAccount return false.");
            Toast.makeText(mContext, "unlink failed.", Toast.LENGTH_LONG).show();
        }
        else
        {
            PreferenceUtil.removeUser(mContext);
            SharedPreferences prefs = mContext.getSharedPreferences("Preferences", mContext.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.commit();

            Intent i=new Intent(mContext,MainActivity.class);
            mContext.startActivity(i);
            ((Activity)mContext).finish();
        }
    }
    @Override
    public void onUnlinkAccountDone(MXSDKConfig.MXUserInfo mxUserInfo) {

        Log.i(TAG, "Unlinked moxtra account: " + mxUserInfo);
        if (mxUserInfo == null) {
            Log.e(TAG, "Can't logout: the mxUserInfo is null.");
            Toast.makeText(mContext, "unlink failed as mxUserInfo is null.", Toast.LENGTH_LONG).show();
        }
    }

}

