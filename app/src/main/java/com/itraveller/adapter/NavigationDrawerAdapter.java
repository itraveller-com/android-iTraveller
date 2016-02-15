package com.itraveller.adapter;

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
import com.itraveller.activity.ContactUsFragment;
import com.itraveller.activity.FragmentDrawer;
import com.itraveller.activity.Home_Fragment;
import com.itraveller.activity.MaterialLandingActivity;
import com.itraveller.dashboard.MyTravelActivity;
import com.itraveller.core.LoginScreenActivity;
import com.itraveller.model.NavDrawerItem;
import com.itraveller.moxtraChat.PreferenceUtil;
import com.moxtra.sdk.MXAccountManager;
import com.moxtra.sdk.MXSDKConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by Ravi Tamada on 12-03-2015.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> implements MXAccountManager.MXAccountUnlinkListener{
    List<NavDrawerItem> data = Collections.emptyList();
    List<MyViewHolder> holders = new ArrayList<MyViewHolder>();
    private LayoutInflater inflater;
    private Context context;
    private static final String TAG = "NavigationDrawerAdapter";


    public NavigationDrawerAdapter(Context context, List<NavDrawerItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        holders.add(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final NavDrawerItem current = data.get(position);

        holder.imgIcon.setImageResource(current.getIcon());
        if(position==4){
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
                        fragment = new Home_Fragment();
                        //    fragment = new MaterialLandingActivity();
                        title = "Choose Destination";
                        FragmentDrawer.mDrawerLayout.closeDrawers();

                        break;


                    case 1:

                        fragment = new MaterialLandingActivity();
                        title = "How it works";
                        FragmentDrawer.mDrawerLayout.closeDrawers();
                        break;

                    case 2:

                        Intent i = new Intent(context, MyTravelActivity.class);
                        context.startActivity(i);
                        ((Activity) context).finish();
                        //      fragment = new MyTravelFragment();
                        title = "About Us";
                        break;

                    case 3:

                        fragment = new ContactUsFragment();
                        title = "Contact Us";
                        FragmentDrawer.mDrawerLayout.closeDrawers();
                        break;

                    case 4:

                        LoginScreenActivity fragment1 = new LoginScreenActivity();
                        title = "Login";
                        if (holder.title.getText().toString().equalsIgnoreCase("Logout")) {

                            logout();

                        }
                        fragment = fragment1;
                        break;

                    default:
                        break;
                }

                if (fragment != null) {
                    FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.commit();
                    // set the toolbar title
                    // getSupportActionBar().setTitle(title);
                }

            }
        });
    }


    protected void logout() {
        PreferenceUtil.removeUser(context);

        boolean ret = MXAccountManager.getInstance().unlinkAccount(this);
        if (!ret) {

            Log.e(TAG, "Can't logout: the unlinkAccount return false.");
            Toast.makeText(context, "unlink failed.", Toast.LENGTH_LONG).show();
        }
        else
        {
            PreferenceUtil.removeUser(context);
            SharedPreferences prefs = context.getSharedPreferences("Preferences", context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.commit();
        }
    }
    @Override
    public void onUnlinkAccountDone(MXSDKConfig.MXUserInfo mxUserInfo) {

        Log.i(TAG, "Unlinked moxtra account: " + mxUserInfo);
        if (mxUserInfo == null) {
            Log.e(TAG, "Can't logout: the mxUserInfo is null.");
            Toast.makeText(context, "unlink failed as mxUserInfo is null.", Toast.LENGTH_LONG).show();
        }
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    public SharedPreferences SharedPreferenceRetrive() {
        SharedPreferences preferences = context.getSharedPreferences("Preferences", context.MODE_PRIVATE);
        return preferences;
    }

    @Override
    public int getItemCount() {
        return data.size();
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
}
