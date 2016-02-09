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
    private final static String TOKEN = "TOKEN";
    private final static String BINDERID = "BINDERID";
    private final static String AGENTBINDER = "AGENTBINDER";
    private final static String BINDER_OWNER = "BINDER_OWNER";
    private final static String GCM_REG_ID = "GCM_REG_ID";
    private final static String USER_GCM_REG_ID = "USER_GCM_REG_ID";
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
        ed.remove(BINDERID).commit();
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


    public static void setBinderId(Context context, String unqid) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(BINDERID, unqid).commit();
    }

    public static String getBinderId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sp.getString(BINDERID, "");
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

    public static String getTokenId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sp.getString(TOKEN, "");
    }

    public static void setTokenId(Context context, String token) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(TOKEN, token).commit();
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


    public static String getUserGcmRegId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sp.getString(USER_GCM_REG_ID, null);
    }

    public static void setUserGcmRegId(Context context, String gcmRegId) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(USER_GCM_REG_ID, gcmRegId).commit();
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

