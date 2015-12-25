package com.itraveller.chat;

/**
 * Created by rohan bundelkhandi on 17/12/2015.
 */

        import android.app.Activity;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.util.Log;
        import android.widget.Toast;

        import com.itraveller.materialsearch.SharedPreference;
        import com.itraveller.volley.AppController;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.BufferedReader;
        import java.io.InputStreamReader;
        import java.io.OutputStreamWriter;
        import java.net.URL;
        import java.net.URLConnection;
        import java.net.URLEncoder;

public class ChatMain extends Activity {

    // label to display gcm messages
    AppController aController;
    SharedPreferences preferences;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /******************* Intialize Database *************/
        DBAdapter.init(this);

        // Get Global Controller Class object
        aController = (AppController) getApplicationContext();

        preferences=getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        //Check device contains self information in sqlite database or not.
        int vDevice = DBAdapter.validateUser();

        if(vDevice > 0)
        {
            // Launch Main Activity
            Intent i = new Intent(getApplicationContext(), GridViewScreen.class);
            startActivity(i);
            finish();
        }
        else
        {
            /******* Validate device from server ******/
            // WebServer Request URL
            String serverURL = Config.YOUR_SERVER_URL+"validate_user.php";

            // Use AsyncTask execute Method To Prevent ANR Problem
            LongOperation serverRequest = new LongOperation();

            String email=""+preferences.getString("email","");

            serverRequest.execute(serverURL,email,"","");

        }

    }
    // Class with extends AsyncTask class
    public class LongOperation  extends AsyncTask<String, Void, String> {

        // Required initialization
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(ChatMain.this);
        String data ="";
        int sizeData = 0;

        protected void onPreExecute() {
            // NOTE: You can call UI Element here.

            //Start Progress Dialog (Message)

            Dialog.setMessage("Validating User...");
            Dialog.show();

        }

        // Call after onPreExecute method
        protected String doInBackground(String... params) {

            /************ Make Post Call To Web Server ***********/
            BufferedReader reader=null;
            String Content = "";
            // Send data
            try{

                // Defined URL  where to send data
                URL url = new URL(params[0]);

                // Set Request parameter
                if(!params[1].equals(""))
                    data +="&" + URLEncoder.encode("data1", "UTF-8") + "="+params[1].toString();
                if(!params[2].equals(""))
                    data +="&" + URLEncoder.encode("data2", "UTF-8") + "="+params[2].toString();
                if(!params[3].equals(""))
                    data +="&" + URLEncoder.encode("data3", "UTF-8") + "="+params[3].toString();
                Log.i("GCM", data);

                // Send POST data request
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write( data );
                wr.flush();

                // Get the server response
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null)
                {
                    // Append server response in string
                    sb.append(line + " ");
                }

                // Append Server Response To Content String
                Content = sb.toString();
            }
            catch(Exception ex)
            {
                Error = ex.getMessage();
            }
            finally
            {
                try
                {
                    reader.close();
                }
                catch(Exception ex) {}
            }

            //edited
            int jsonStart = Content.indexOf("{");
            int jsonEnd = Content.lastIndexOf("}");

            if (jsonStart >= 0 && jsonEnd >= 0 && jsonEnd > jsonStart) {
                Content = Content.substring(jsonStart, jsonEnd + 1);
            } else {
                // deal with the absence of JSON content here
            }
            Content = Content.replaceFirst("<font>.*?</font>", "");
            /*****************************************************/
            return Content;
        }

        protected void onPostExecute(String Content) {
            // NOTE: You can call UI Element here.

            // Close progress dialog
            Dialog.dismiss();

            if (Error != null)
            {

            }
            else
            {
                // Show Response Json On Screen (activity)
                /****************** Start Parse Response JSON Data *************/
                aController.clearUserData();

                JSONObject jsonResponse;

                try
                {

                    Log.d("Test Response",""+Content);
                    /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
                    jsonResponse = new JSONObject(Content);

                    /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
                    /*******  Returns null otherwise.  *******/
                    JSONArray jsonMainNode = jsonResponse.optJSONArray("Android");

                    /*********** Process each JSON Node ************/
                    int lengthJsonArr = jsonMainNode.length();

                    for(int i=0; i < lengthJsonArr; i++)
                    {
                        /****** Get Object for each JSON node.***********/
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                        /******* Fetch node values **********/
                        String Status       = jsonChildNode.optString("status").toString();

                        Log.i("GCM","---"+Status);

                        // IF server response status is update
                        if(Status.equals("update")){

                            String RegID      = jsonChildNode.optString("regid").toString();
                            String Name       = jsonChildNode.optString("name").toString();
                            String Email      = jsonChildNode.optString("email").toString();

                            Log.d("Email testing",""+Name);

                            // add device self data in sqlite database
                            DBAdapter.addUserData(Name, Email, RegID);

                            // Launch GridViewScreen Activity
                            Intent i1 = new Intent(getApplicationContext(), GridViewScreen.class);
                            startActivity(i1);
                            finish();

                            Log.i("GCM","---"+Name);
                        }
                        else if(Status.equals("install"))
                        {
                            // Launch RegisterActivity Activity
                            Intent i1 = new Intent(getApplicationContext(), GCMRegistrationActivity.class);
                            startActivity(i1);
                            finish();
                        }
                    }
                    /****************** End Parse Response JSON Data *************/
                } catch (JSONException e)
                {

                    e.printStackTrace();
                }
            }
        }

    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

}


