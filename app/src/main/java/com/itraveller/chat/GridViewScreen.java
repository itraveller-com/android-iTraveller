package com.itraveller.chat;

/**
 * Created by rohan bundelkhandi on 17/12/2015.
 */
        import android.app.Activity;
        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.GridView;

        import com.itraveller.R;
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


public class GridViewScreen extends Activity {

    GridView gridView;
    AppController aController = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_view_android_list);

        //Get gridview object from xml file
        gridView = (GridView) findViewById(R.id.gridView1);

        // Get Global variable instance
        aController = (AppController) getApplicationContext();

        // WebServer Request URL to get All registered devices
        String serverURL = Config.YOUR_SERVER_URL+"userdata.php";

        // Use AsyncTask execute Method To Prevent ANR Problem
        LongOperation serverRequest = new LongOperation();

        serverRequest.execute(serverURL,"rrr","","");

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Intent i = new Intent(getApplicationContext(), ShowMessage.class);
                startActivity(i);
                finish();
            }
        });
    }

    // Class with extends AsyncTask class
    public class LongOperation  extends AsyncTask<String, Void, String> {

        // Required initialization

        //private final HttpClient Client = new DefaultHttpClient();
        // private Controller aController = null;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(GridViewScreen.this);
        String data ="";
        int sizeData = 0;

        protected void onPreExecute() {
            //Start Progress Dialog (Message)
            Dialog.setMessage("Loading ...");
            Dialog.show();
        }

        // Call after onPreExecute method
        protected String doInBackground(String... params) {

            /************ Make Post Call To Web Server ***********/
            BufferedReader reader=null;
            String Content = "";
            // Send data
            try
            {
                // Defined URL  where to send data
                URL url = new URL(params[0]);

                // Set Request parameter
                if(!params[1].equals(""))
                    data +="&" + URLEncoder.encode("data", "UTF-8") + "="+params[1].toString();
                if(!params[2].equals(""))
                    data +="&" + URLEncoder.encode("data2", "UTF-8") + "="+params[2].toString();
                if(!params[3].equals(""))
                    data +="&" + URLEncoder.encode("data3", "UTF-8") + "="+params[3].toString();
                Log.i("GCM",data);

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
            return Content;
        }

        protected void onPostExecute(String Content) {
            // Close progress dialog
            Dialog.dismiss();

            if (Error != null) {

            }
            else
            {
                // Show Response Json On Screen (activity)
                /****************** Start Parse Response JSON Data *************/
                aController.clearUserData();
                JSONObject jsonResponse;

                try
                {
                    // Creates a new JSONObject with name/value mappings from the JSON string.
                    jsonResponse = new JSONObject(Content);
                    // Returns the value mapped by name if it exists and is a JSONArray.
                    // Returns null otherwise.
                    JSONArray jsonMainNode = jsonResponse.optJSONArray("Android");
                    /*********** Process each JSON Node ************/
                    int lengthJsonArr = jsonMainNode.length();

                    for(int i=0; i < lengthJsonArr; i++)
                    {
                        /****** Get Object for each JSON node.***********/
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                        /******* Fetch node values **********/
                        String Name       = jsonChildNode.optString("name").toString();
                        String Email      = jsonChildNode.optString("email").toString();

                        Log.i("User Name",""+ Name);
                        Log.i("User Email", "---" + Email);


                        UserData userdata = new UserData();

                        if(Name.equals("Send To All"))
                        {
                            userdata.setName("Start Chatting");
                            //Add user data to controller class UserDataArr arraylist
                            aController.setUserData(userdata);
                        }
                    }

                    // Set grid adapter to  CustomGridAdapter
                    gridView.setAdapter(new GridAdapter(getBaseContext(), aController));

                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }
    }
}


