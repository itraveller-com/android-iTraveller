package com.itraveller.chat;

/**
 * Created by rohan bundelkhandi on 17/12/2015.
 */

        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.DatabaseUtils;
        import android.database.SQLException;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;

        import java.util.ArrayList;
        import java.util.List;


public class DBAdapter {

    /***** if debug is set true then it will show all Logcat message ****/
    public static final boolean DEBUG = true;

    /******************** Logcat TAG ************/
    public static final String LOG_TAG = "DBAdapter";

    /******************** Table Fields ************/
    public static final String KEY_ID = "_id";

    public static final String KEY_USER_CHAT_NAME    = "user_chat_name";

    public static final String KEY_USER_CHAT_MESSAGE = "user_chat_message";

    public static final String KEY_USER_NAME  = "user_name";

    public static final String KEY_USER_EMAIL = "user_email";

    public static final String KEY_USER_CHAT_EMAIL = "user_chat_email";

    public static final String KEY_USER_REGID = "user_regid";

    public static final String KEY_USER_CHAT_TIME="created_at";

    /******************** Database Name ************/
    public static final String DATABASE_NAME = "DB_CHAT";

    /**** Database Version (Increase one if want to also upgrade your database) ****/
    public static final int DATABASE_VERSION = 1;// started at 1

    /** Table names */
    public static final String USER_CHAT_TABLE = "user_chat_details";
    public static final String USER_TABLE = "user_details";

    /*** Set all table with comma seperated like USER_TABLE,ABC_TABLE ***/
    private static final String[] ALL_TABLES = { USER_TABLE,USER_CHAT_TABLE };


    /** Create table syntax */


    private static final String USER_CREATE =
            "create table user_details (_id integer primary key autoincrement,user_name text not null,user_email text not null,user_regid text not null);";

    private static final String USER_CHAT_CREATE =
            "create table user_chat_details (_id integer primary key autoincrement,user_chat_name text not null,user_chat_email text not null,user_chat_message text not null,created_at text not null);";


    /**** Used to open database in syncronized way ****/
    private static DataBaseHelper DBHelper = null;

    public DBAdapter() {
    }

    /******************* Initialize database *************/
    public static void init(Context context) {
        if (DBHelper == null) {
            if (DEBUG)
                Log.i("DBAdapter", context.toString());
            DBHelper = new DataBaseHelper(context);
        }
    }

    /***** Main Database creation INNER class ******/
    private static class DataBaseHelper extends SQLiteOpenHelper {
        public DataBaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            if (DEBUG)
                Log.i(LOG_TAG, "new create");
            try {

                db.execSQL(USER_CREATE);
                db.execSQL(USER_CHAT_CREATE);

            } catch (Exception exception) {
                if (DEBUG)
                    Log.i(LOG_TAG, "Exception onCreate() exception");
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (DEBUG)
                Log.w(LOG_TAG, "Upgrading database from version" + oldVersion
                        + "to" + newVersion + "...");

            for (String table : ALL_TABLES) {
                db.execSQL("DROP TABLE IF EXISTS " + table);
            }
            onCreate(db);
        }

    }

    /**** Open database for insert,update,delete in syncronized manner ****/
    public static synchronized SQLiteDatabase open() throws SQLException {
        return DBHelper.getWritableDatabase();
    }

    // Insert installing device data
    public static void addUserData(String UserName, String UserEmail, String RegID)
    {

        Log.d("Integration","adding");
        try{
            final SQLiteDatabase db = open();

            String name  = sqlEscapeString(UserName);
            String email = sqlEscapeString(UserEmail);
            String regid = sqlEscapeString(RegID);

            ContentValues cVal = new ContentValues();
            cVal.put(KEY_USER_NAME, name);
            cVal.put(KEY_USER_EMAIL, email);
            cVal.put(KEY_USER_REGID, regid);

            db.insert(USER_TABLE, null, cVal);
            //    db.close(); // Closing database connection
        } catch (Throwable t) {
            Log.i("Database", "Exception caught: " + t.getMessage(), t);
        }
    }

    // Adding new user

    public static void addUserChatData(UserData uData) {
        try{
            final SQLiteDatabase db = open();

            String name  = sqlEscapeString(uData.getName());
            String email  = sqlEscapeString(uData.getEmail());
            String message  = sqlEscapeString(uData.getMessage());
            String time=sqlEscapeString(uData.getTime());

            ContentValues cVal = new ContentValues();
            cVal.put(KEY_USER_CHAT_NAME, name);
            cVal.put(KEY_USER_CHAT_EMAIL, email);
            cVal.put(KEY_USER_CHAT_MESSAGE, message);
            cVal.put(KEY_USER_CHAT_TIME,time);

            db.insert(USER_CHAT_TABLE, null, cVal);
            //    db.close(); // Closing database connection
        } catch (Throwable t) {
            Log.i("Database", "Exception caught: " + t.getMessage(), t);
        }
    }

    public static String getUserName(String email)
    {

        final SQLiteDatabase db = open();

        Cursor cursor = db.query(USER_TABLE, new String[] {KEY_USER_NAME }, KEY_USER_EMAIL + "=?",
                new String[] { String.valueOf(email) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        String name="";
        if(cursor.moveToFirst())
        {
            name=cursor.getString(0);
        }

        // return contact
        return name;

    }

    // Getting users Count
    public static int getUserDataCount() {
        String countQuery = "SELECT  * FROM " + USER_CHAT_TABLE;
        final SQLiteDatabase db = open();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public static List<UserData> getUserMessage() {

        List<UserData> contactList = new ArrayList<UserData>();

        final SQLiteDatabase db = open();

        String countQuery = "SELECT  * FROM " + USER_CHAT_TABLE;

        Cursor cursor = db.rawQuery(countQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                UserData data = new UserData();
                data.setID(Integer.parseInt(cursor.getString(0)));
                data.setName(cursor.getString(1));
                data.setEmail(cursor.getString(2));
                data.setMessage(cursor.getString(3));
                data.setTime(cursor.getString(4));

                // Adding contact to list
                contactList.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return contact list
        return contactList;
    }

    // Getting installed device have self data or not
    public static int validateUser() {
        String countQuery = "SELECT  * FROM " + USER_TABLE;
        final SQLiteDatabase db = open();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    // Escape string for single quotes (Insert,Update)
    private static String sqlEscapeString(String aString) {
        String aReturn = "";

        if (null != aString) {
            //aReturn = aString.replace("'", "''");
            aReturn = DatabaseUtils.sqlEscapeString(aString);
            // Remove the enclosing single quotes ...
            aReturn = aReturn.substring(1, aReturn.length() - 1);
        }

        return aReturn;
    }

    // UnEscape string for single quotes (show data)
    private static String sqlUnEscapeString(String aString) {

        String aReturn = "";

        if (null != aString) {
            aReturn = aString.replace("''", "'");
        }

        return aReturn;
    }

}



