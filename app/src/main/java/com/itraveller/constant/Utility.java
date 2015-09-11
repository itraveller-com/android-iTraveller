package com.itraveller.constant;

/**
 * Created by VNK on 6/29/2015.
 */
    import android.content.Context;
    import android.net.ConnectivityManager;
    import android.net.NetworkInfo;
    import android.os.StrictMode;
    import android.view.View;
    import android.view.ViewGroup;
    import android.view.View.MeasureSpec;
    import android.widget.ListAdapter;
    import android.widget.ListView;

    import java.net.InetAddress;
    import java.text.DateFormat;
    import java.text.ParseException;
    import java.text.SimpleDateFormat;
    import java.util.Calendar;
    import java.util.Date;

public class Utility {
        public static void setListViewHeightBasedOnChildren(ListView listView) {
            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null) {
                // pre-condition
                return;
            }

            int totalHeight = 0;
            int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            listView.setLayoutParams(params);
            listView.requestLayout();
        }



    ////////////////////////////////////////////////////////////////////////////////
    //////////////////WIFI or Mobile Network Available or Not //////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null) {
            // There are no active networks.
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            if (isInternetAvailable()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("www.google.com");
            // You can replace it with your name
            if (ipAddr.equals("")) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            System.out.println("Exception: " + e);
            return false;
        }

    }



    public static String addDays(String baseDate, int daysToAdd, String dateformatonward , String returnformat) {

        String finalDate=null;
        DateFormat df = new SimpleDateFormat(dateformatonward);
        try {
           Date d = df.parse(baseDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(d);
            calendar.add(Calendar.DAY_OF_YEAR, daysToAdd);
            //Format "yyyy-MM-dd"
            DateFormat date_format = new SimpleDateFormat(returnformat);
            finalDate = date_format.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return finalDate;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////Number of rooms selection using no of members/////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    public static int noRooms(int max_member, int no_of_members){
        float rooms_float = Float.parseFloat(""+no_of_members)/Float.parseFloat(""+max_member);
        int rooms_int = no_of_members/max_member;
        float value = rooms_float - rooms_int;
        if(value>0)
        {
            rooms_int ++;
        }
        return rooms_int;
    }

    }

