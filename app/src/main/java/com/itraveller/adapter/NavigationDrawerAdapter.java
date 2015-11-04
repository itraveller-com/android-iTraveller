package com.itraveller.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.facebook.login.LoginManager;
import com.itraveller.R;
import com.itraveller.activity.FriendsFragment;
import com.itraveller.activity.HowItWorksFragment;
import com.itraveller.activity.LandingActivity;
import com.itraveller.activity.LoginFragment;
import com.itraveller.activity.MainActivity;
import com.itraveller.activity.MaterialLandingActivity;
import com.itraveller.activity.MessagesFragment;
import com.itraveller.model.NavDrawerItem;


/**
 * Created by Ravi Tamada on 12-03-2015.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {
    List<NavDrawerItem> data = Collections.emptyList();
    List<MyViewHolder> holders = new ArrayList<MyViewHolder>();
    private LayoutInflater inflater;
    private Context context;

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
        holder.title.setText(current.getTitle());

        holders.get(0).nav_row.setBackgroundColor(context.getResources().getColor(R.color.background_light));

        Log.d("Nav Item Position Test", "" + position);

        holder.nav_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Notify test", "" + current.isShowNotify());
                Log.d("Nav Item Position Test22", "" + position);

                if (current.isShowNotify()) {
                    current.setShowNotify(false);
                    holder.nav_row.setBackgroundColor(context.getResources().getColor(R.color.white));
                } else {
                    current.setShowNotify(false);
                    holder.nav_row.setBackgroundColor(context.getResources().getColor(R.color.background_light));
//looping through MyViewHolder instances and removing the background color if not selected
                    for (int i = 0; i < holders.size(); i++) {
                        if (i != position) {
                            holders.get(i).nav_row.setBackgroundColor(context.getResources().getColor(R.color.white));
                        }
                    }
                }
            }
        });

        holder.nav_row.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Log.d("Notify test11", "" + current.isShowNotify());
                if (current.isShowNotify()) {
                    current.setShowNotify(false);
                    holder.nav_row.setBackgroundColor(context.getResources().getColor(R.color.white));
                } else
                {
                    current.setShowNotify(false);
                    holder.nav_row.setBackgroundColor(context.getResources().getColor(R.color.background_light));
//looping through MyViewHolder instances and removing the background color if not selected
                    for (int i = 0; i < holders.size(); i++)
                    {
                        if (i != position)
                        {
                            holders.get(i).nav_row.setBackgroundColor(context.getResources().getColor(R.color.white));
                        }
                        else
                        {
                            Fragment fragment;
                            String title;
                            fragment = null;
                            title = "iTraveller";
                            switch (position)
                            {
                                case 0:

                                    fragment = new MaterialLandingActivity();
                                    title = "Choose Destination";
                                    break;


                                case 1:

                                    fragment=new HowItWorksFragment();
                                    title= "How it works";

                                    break;

                                case 2:

                                    fragment = new FriendsFragment();
                                    title = "About Us";

                                    break;

                                case 3:

                                    fragment = new MessagesFragment();
                                    title =
                                    "Contact Us";

                                    break;

                                case 4:

                                    LoginFragment fragment1 = new LoginFragment();
                                    fragment1.setContextValue(context);
                                    fragment = fragment1;


                                    break;


                                default:
                                    break;
                            }

                            if (fragment != null) {
                                FragmentManager fragmentManager = ((FragmentActivity)context).getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.container_body, fragment);
                                fragmentTransaction.commit();

                                // set the toolbar title
                            //    getSupportActionBar().setTitle(title);
                            }
                        }
                    }
                }

                return false;
            }
        });
    }



    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
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
            nav_row=(RelativeLayout) itemView.findViewById(R.id.nav_drawer_row);
        }
    }
}
