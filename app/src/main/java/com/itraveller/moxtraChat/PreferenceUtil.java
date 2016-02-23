package com.itraveller.moxtraChat;

/**
 * Created by rohan bundelkhandi on 20/01/2016.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

public class PreferenceUtil {

    private final static String PREF = "PREF";
    private final static String PHONE = "PHONE";

    private final static String SINGLE_TOKEN = "SINGLE_TOKEN";
    private final static String GROUP_TOKEN = "GROUP_TOKEN";
    private final static String TRAVEL_TOKEN = "TRAVEL_TOKEN";

    private final static String SINGLE_BINDERID = "SINGLE_BINDERID";
    private final static String TRAVEL_BINDERID = "TRAVEL_BINDERID";
    private final static String GROUP_BINDERID = "GROUP_BINDERID";

    private final static String AGENTBINDER = "AGENTBINDER";
    private final static String BINDER_OWNER = "BINDER_OWNER";
    private final static String GCM_REG_ID = "GCM_REG_ID";

    private final static String SINGLE_USER_GCM_REG_ID = "SINGLE_USER_GCM_REG_ID";
    private final static String GROUP_USER_GCM_REG_ID = "GROUP_USER_GCM_REG_ID";
    private final static String TRAVEL_USER_GCM_REG_ID = "TRAVEL_USER_GCM_REG_ID";

    private final static String APP_VERSION = "APP_VERSION";
    private static final String TAG = "PreferenceUtil";

    private static String getClientId(Context context) {
        ApplicationInfo appInfo = null;
        try {
            appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Can't get metadata.", e);
        }
        if (appInfo != null && appInfo.metaData != null) {
            String clientId = appInfo.metaData.getString("com.moxtra.sdk.ClientId");
            return clientId;
        }
        return null;
    }

    public static void removeUser(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.remove(PHONE).commit();
        ed.remove(SINGLE_BINDERID).commit();
        ed.remove(TRAVEL_BINDERID).commit();
        ed.remove(GROUP_BINDERID).commit();
        ed.remove(SINGLE_TOKEN).commit();
        ed.remove(TRAVEL_TOKEN).commit();
        ed.remove(GROUP_TOKEN).commit();

    }

    public static void saveUser(Context context, String phone) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(PHONE, phone).commit();
    }

    public static String getUser(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sp.getString(PHONE, "");
    }
    public static String getBinderOwner(Context context) {
        SharedPreferences sp = context.getSharedPreferences(BINDER_OWNER, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(BINDER_OWNER, "agent@instapaisa.com");
        return sp.getString(PHONE, null);
    }


    public static void setSingleBinderId(Context context, String unqid) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(SINGLE_BINDERID, unqid).commit();
    }

    public static String getSingleBinderId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sp.getString(SINGLE_BINDERID, "");
    }


    public static void setTravelBinderId(Context context, String unqid) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(TRAVEL_BINDERID, unqid).commit();
    }

    public static String getTravelBinderId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sp.getString(TRAVEL_BINDERID, "");
    }

    public static void setGroupBinderId(Context context, String unqid) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(GROUP_BINDERID, unqid).commit();
    }

    public static String getGroupBinderId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sp.getString(GROUP_BINDERID, "");
    }


    public static void setAgentBinderId(Context context, String unqid) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(AGENTBINDER, unqid).commit();
    }

    public static String getAgentBinderId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sp.getString(AGENTBINDER, null);
    }

    public static String getSingleTokenId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sp.getString(SINGLE_TOKEN, "");
    }

    public static void setSingleTokenId(Context context, String token) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(SINGLE_TOKEN, token).commit();
    }


    public static String getGroupTokenId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sp.getString(GROUP_TOKEN, "");
    }

    public static void setGroupTokenId(Context context, String token) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(GROUP_TOKEN, token).commit();
    }


    public static String getTravelTokenId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sp.getString(TRAVEL_TOKEN, "");
    }

    public static void setTravelTokenId(Context context, String token) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(TRAVEL_TOKEN, token).commit();
    }

    public static boolean isUserInit(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        String clientId = getClientId(context);
        boolean isInit = sp.getBoolean(clientId, false);
        return isInit;
    }

    public static void setUserInitDone(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(getClientId(context), true).commit();
    }

    public static String getGcmRegId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sp.getString(GCM_REG_ID, null);
    }

    public static void setGcmRegId(Context context, String gcmRegId) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(GCM_REG_ID, gcmRegId).commit();
    }


    public static String getSingleUserGcmRegId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sp.getString(SINGLE_USER_GCM_REG_ID, null);
    }

    public static void setSingleUserGcmRegId(Context context, String gcmRegId) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(SINGLE_USER_GCM_REG_ID, gcmRegId).commit();
    }

    public static String getGroupUserGcmRegId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sp.getString(GROUP_USER_GCM_REG_ID, null);
    }

    public static void setGroupUserGcmRegId(Context context, String gcmRegId) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(GROUP_USER_GCM_REG_ID, gcmRegId).commit();
    }

    public static String getTravelUserGcmRegId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sp.getString(TRAVEL_USER_GCM_REG_ID, null);
    }

    public static void setTravelUserGcmRegId(Context context, String gcmRegId) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(TRAVEL_USER_GCM_REG_ID, gcmRegId).commit();
    }


    public static int getAppVersion(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sp.getInt(APP_VERSION, -1);
    }

    public static void setAppVersion(Context context, int appVersion) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt(APP_VERSION, appVersion).commit();
    }
}

