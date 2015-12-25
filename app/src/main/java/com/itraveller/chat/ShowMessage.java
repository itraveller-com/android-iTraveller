package com.itraveller.chat;

/**
 * Created by rohan bundelkhandi on 17/12/2015.
 */

        import android.app.NotificationManager;
        import android.app.ProgressDialog;
        import android.content.ClipData;
        import android.content.ClipboardManager;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.content.pm.PackageManager;
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.os.Environment;
        import android.provider.MediaStore;
        import android.support.design.widget.Snackbar;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.util.Log;
        import android.view.ActionMode;
        import android.view.KeyEvent;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewAnimationUtils;
        import android.view.animation.AccelerateDecelerateInterpolator;
        import android.widget.AbsListView;
        import android.widget.AdapterView;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageButton;
        import android.widget.LinearLayout;
        import android.widget.ListView;
        import android.widget.Toast;

        import com.itraveller.R;
        import com.itraveller.gcm.chat_files.GCMIntentService;
        import com.itraveller.volley.AppController;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.BufferedReader;
        import java.io.File;
        import java.io.InputStreamReader;
        import java.io.OutputStreamWriter;
        import java.net.URL;
        import java.net.URLConnection;
        import java.net.URLEncoder;
        import java.text.SimpleDateFormat;
        import java.util.Date;
        import java.util.List;
        import java.util.Locale;

        import io.codetail.animation.SupportAnimator;

public class ShowMessage extends AppCompatActivity implements View.OnClickListener{


    private static final String TAG = ShowMessage.class.getSimpleName();

    SharedPreferences prefs;
    private static final int REQUEST_PICK_FILE = 10;
    private File selectedFile;


    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private Uri fileUri; // file url to store image/video


    LinearLayout mRevealView;
    ImageButton ib_gallery,ib_photo,ib_video;

    Toolbar mtoolbar;
    SharedPreferences preferences;
    GCMIntentService gcmIntentService;
    // UI elements
    static EditText editText;
    // Send Message button
    Button btnSend;
    String copied_data="";
    static ListView msgList;
    static ChatArrayAdapter chatArrayAdapter;

    boolean hidden=true;

    AppController aController;
    String senderEmail;
    String message;
    String senderName;
    ShowMessage activity = null;
    public static boolean isActive,isStop;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_message);

        /******************* Intialize Database *************/
        DBAdapter.init(ShowMessage.this);

        isActive=true;
        isStop=true;

        RemoveAllNotification();
        // Get Global Controller Class object
        aController = (AppController) getApplicationContext();

        preferences=getSharedPreferences("Preferences",Context.MODE_PRIVATE);
        prefs=getSharedPreferences("Preferences", Context.MODE_PRIVATE);


        gcmIntentService=new GCMIntentService();

        mRevealView = (LinearLayout) findViewById(R.id.reveal_items);
        mRevealView.setVisibility(View.INVISIBLE);
        ib_gallery=(ImageButton)findViewById(R.id.gallery);
        ib_photo=(ImageButton)findViewById(R.id.photo);
        ib_video=(ImageButton)findViewById(R.id.video);

        ib_gallery.setOnClickListener(this);
        ib_photo.setOnClickListener(this);
        ib_video.setOnClickListener(this);


        editText = (EditText) findViewById(R.id.chatText);
        btnSend    = (Button) findViewById(R.id.buttonSend);
        msgList=(ListView) findViewById(R.id.msg_list);

        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.activity_chat_singlemessage);
        msgList.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        msgList.setAdapter(chatArrayAdapter);

        msgList.setStackFromBottom(true);

        msgList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        msgList.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            private int nr = 0;

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // TODO Auto-generated method stub
                chatArrayAdapter.clearSelection();
                copied_data = "";
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub

                nr = 0;
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.contextual_menu, menu);
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // TODO Auto-generated method stub
                switch (item.getItemId()) {

                    case R.id.item_copy:
                        nr = 0;

                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("text", "" + copied_data.replace("::", " "));
                        clipboard.setPrimaryClip(clip);

                        copied_data = "";

                        Toast.makeText(getApplicationContext(), "Text Copied", Toast.LENGTH_SHORT).show();

                        chatArrayAdapter.clearSelection();

                        mode.finish();
                }
                return false;
            }

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                  long id, boolean checked) {
                // TODO Auto-generated method stub
                if (checked) {
                    nr++;
                    chatArrayAdapter.setNewSelection(position, checked);
                    copied_data += chatArrayAdapter.getItem(position).message + "::";
                } else {
                    nr--;
                    Log.d("Chat String", "" + copied_data);
                    if (copied_data.contains("::")) {
                        copied_data = copied_data.replace("" + chatArrayAdapter.getItem(position).message + "::", "");
                    }
                    Log.d("Chat String", "" + copied_data);

                    chatArrayAdapter.removeSelection(position);
                }
                mode.setTitle(nr + " selected");

            }
        });

        msgList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                // TODO Auto-generated method stub
                msgList.setItemChecked(position, !chatArrayAdapter.isPositionChecked(position));

                return false;
            }
        });

        senderEmail=""+preferences.getString("email","");


        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);


//        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Itraveller Support Chat");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        senderName=""+DBAdapter.getUserName(senderEmail);

        List<UserData> data = DBAdapter.getUserMessage();

        for (UserData dt : data)
        {
            String date_time_str=dt.getTime();
            String date_time_arr[]=date_time_str.split(" ");
            if((dt.getName()).equals(senderName))
            {
                chatArrayAdapter.add(new ChatMessage(false,""+"You" +" :",dt.getMessage(),""+date_time_arr[0],""+date_time_arr[1]));
            }
            else
            {
                chatArrayAdapter.add(new ChatMessage(true,""+dt.getName()+" :",dt.getMessage(),""+date_time_arr[0],""+date_time_arr[1]));
            }

        }

        int local_row_count=DBAdapter.getUserDataCount();

        Log.d("Number of rows testing",""+local_row_count);

        // WebServer Request URL to get new messages
        String serverURL = Config.YOUR_SERVER_URL+"fetch_data.php";
        new FetchFromServer().execute(serverURL,String.valueOf(local_row_count),"","");

        msgList.setSelection(chatArrayAdapter.getCount() - 1);

        // Register custom Broadcast receiver to show messages on activity
//        registerReceiver(mHandleMessageReceiver, new IntentFilter(Config.DISPLAY_MESSAGE_ACTION));

        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    message = editText.getText().toString();

                    if(message.trim().length() > 0) {
                        // WebServer Request URL to send message to device.
                        String serverURL = Config.YOUR_SERVER_URL + "sendpush.php";

                        new LongOperation().execute(serverURL, "All", message.trim(), senderName,senderEmail);

                        return true;
                    }
                    else
                    {
                        Toast.makeText(ShowMessage.this,"Can't send empty message",Toast.LENGTH_LONG).show();
                        return false;
                    }
                }
                return false;
            }
        });

        activity  = this;

        // Click event on Register button
        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Get data from EditText
                message = editText.getText().toString();

                if(message.trim().length()>0) {
                    // WebServer Request URL to send message to device.
                    String serverURL = Config.YOUR_SERVER_URL + "sendpush.php";

                    new LongOperation().execute(serverURL, "All", message.trim(), senderName,senderEmail);
                }
                else
                {
                    Toast.makeText(ShowMessage.this,"Can't send empty message",Toast.LENGTH_LONG).show();
                }
            }
        });

        // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gallery:

            /*    SimpleFileDialog FileOpenDialog =  new SimpleFileDialog(ShowMessage.this, "FileOpen",
                        new SimpleFileDialog.SimpleFileDialogListener()
                        {
                            @Override
                            public void onChosenDir(String chosenDir)
                            {
                                String m_chosen;
                                // The code in this function will be executed when the dialog OK button is pushed
                                m_chosen = chosenDir;
                                Toast.makeText(ShowMessage.this, "Chosen FileOpenDialog File: " + m_chosen, Toast.LENGTH_LONG).show();
                            }
                        });

                //You can change the default filename using the public variable "Default_File_Name"
                FileOpenDialog.Default_File_Name = "";
                FileOpenDialog.chooseFile_or_Dir(); */
                Intent intent = new Intent(ShowMessage.this, FileChooser.class);
                startActivityForResult(intent, REQUEST_PICK_FILE);

                Snackbar.make(v, "Gallery Clicked", Snackbar.LENGTH_SHORT).show();
                mRevealView.setVisibility(View.INVISIBLE);
                break;
            case R.id.photo:

                //capture image
                captureImage();

                Snackbar.make(v, "Photo Clicked", Snackbar.LENGTH_SHORT).show();
                mRevealView.setVisibility(View.INVISIBLE);
                hidden=true;
                break;
            case R.id.video:

                // record video
                recordVideo();

                Snackbar.make(v, "Video Clicked", Snackbar.LENGTH_SHORT).show();
                mRevealView.setVisibility(View.INVISIBLE);
                hidden=true;
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.attachment_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        /*switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

        }*/
        if(id== R.id.btnMyMenu)
        {
            int cx = (mRevealView.getLeft() + mRevealView.getRight());
            int cy = mRevealView.getTop();
            int endradius = Math.max(mRevealView.getWidth(), mRevealView.getHeight());
            SupportAnimator animator =
                    io.codetail.animation.ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, 0, endradius);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(400);
            SupportAnimator animator_reverse = animator.reverse();
            if(hidden) {

                mRevealView.requestFocus();
                mRevealView.setVisibility(View.VISIBLE);
                animator.start();
                hidden = false;

            } else {
                animator_reverse.addListener(new SupportAnimator.AnimatorListener() {
                    @Override
                    public void onAnimationStart() {
                    }

                    @Override
                    public void onAnimationEnd() {
                        mRevealView.setVisibility(View.INVISIBLE);
                        hidden = true;
                    }

                    @Override
                    public void onAnimationCancel() {
                    }
                    @Override
                    public void onAnimationRepeat() {
                    }
                });
                animator_reverse.start();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public  void addReceiverMessageList(final String sender,final String msg, final String date_time_str)
    {
        msgList.post(new Runnable() {
            public void run() {

                String date_time_arr[]=date_time_str.split(" ");
                chatArrayAdapter.add(new ChatMessage(true,sender+" :",""+ msg,""+date_time_arr[0],""+date_time_arr[1]));
            }
        });
    }

    public void addSenderMessageList(final String msg,final String date_time_str)
    {
        msgList.post(new Runnable() {
            public void run() {

                String date_time_arr[]=date_time_str.split(" ");
        //        chatArrayAdapter.add(new ChatMessage(true,"You"+" :",""+ msg,""+date_time_arr[0],""+date_time_arr[1]));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Log.e("test", "onBackPressed");
    }

/*
    // Create a broadcast receiver to get message and show on screen
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String newMessage = intent.getExtras().getString(Config.EXTRA_MESSAGE);
        //    String newName = intent.getExtras().getString("name");
        //    String newIMEI = intent.getExtras().getString("imei");

            Log.i("GCMBroadcast","Broadcast called.");

            // Waking up mobile if it is sleeping
            aController.acquireWakeLock(getApplicationContext());

            Toast.makeText(getApplicationContext(),
                    "Got Message: " + newMessage,
                    Toast.LENGTH_LONG).show();


            // Releasing wake lock
            aController.releaseWakeLock();
        }
    };


    /**
     * Checking device has camera hardware or not
     * */
private boolean isDeviceSupportCamera() {
    if (getApplicationContext().getPackageManager().hasSystemFeature(
            PackageManager.FEATURE_CAMERA)) {
        // this device has a camera
        return true;
    } else {
        // no camera on this device
        return false;
    }
}

    /**
     * Launching camera app to capture image
     */
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    /**
     * Launching camera app to record video
     */
    private void recordVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

        // set video quality
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
        // name

        // start the video capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
    }

    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);


        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }



    /**
     * Receiving activity result method will be called after closing the camera
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK) {

                // successfully captured the image
                // launching upload activity
                launchUploadActivity(true);


            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }

        }
        else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK) {

                // video successfully recorded
                // launching upload activity
                launchUploadActivity(false);

            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled recording
                Toast.makeText(getApplicationContext(),
                        "User cancelled video recording", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to record video
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to record video", Toast.LENGTH_SHORT)
                        .show();
            }
        }
        else if(requestCode == REQUEST_PICK_FILE)
        {
            Log.d("MIME", prefs.getString("MIME", ""));
            // successfully captured the image
            // launching upload activity
            fileUri= Uri.parse(prefs.getString("PATH_STR", ""));

            if((prefs.getString("MIME","")).contains("image"))
                launchUploadActivity(true);
            else if((prefs.getString("MIME","").contains("video")))
                launchUploadActivity(false);


        }

    }

    private void launchUploadActivity(boolean isImage){
        Intent i = new Intent(ShowMessage.this, UploadActivity.class);
        i.putExtra("filePath", fileUri.getPath());
        i.putExtra("isImage", isImage);
        startActivity(i);
    }

    /**
     * ------------ Helper Methods ----------------------
     * */

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Config.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + Config.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }


        /*********** Send message *****************/
    public class LongOperation  extends AsyncTask<String, Void, String> {

        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(ShowMessage.this);
        String data  = "";
        int sizeData = 0;

        protected void onPreExecute() {
            //Start Progress Dialog (Message)

            Dialog.setMessage("Please wait..");
            Dialog.show();

        }

        // Call after onPreExecute method
        protected String doInBackground(String... params) {

            /************ Make Post Call To Web Server ***ReceiverName********/
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
                    data +="&" + URLEncoder.encode("data3", "UTF-8") + "="+params[3].toString().trim();
                if(!params[4].equals(""))
                    data +="&" + URLEncoder.encode("data4", "UTF-8") + "="+params[4].toString().trim();

                Log.d("Parameter test", "" + params[1] + "" + params[2] + "" + params[3].trim()+""+params[4]);

                // Send POST data request
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write( data );
                wr.flush();

                Log.d("Parameter test", "Request");
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

                Log.d("Parameter test","Response");
                // Append Server Response To Content String
                Content = sb.toString();
            }
            catch(Exception ex)
            {
                Log.d("Parameter test","Exception");
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

            Log.d("Send message","hi");
            /*****************************************************/
            return Content;
        }

        protected void onPostExecute(String Result) {
            // NOTE: You can call UI Element here.

            // Close progress dialog
            Dialog.dismiss();

            if (Error != null)
            {
                Toast.makeText(getBaseContext(), "Error: "+Error, Toast.LENGTH_LONG).show();
            }
            else
            {

                UserData userdata = new UserData(1, senderName, message,""+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                // Show Response Json On Screen (activity)
                DBAdapter.addUserChatData(userdata);

                String date_time_str=""+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                String date_time_arr[]=date_time_str.split(" ");
                chatArrayAdapter.add(new ChatMessage(false, "You" + " :" ,""+ message,""+date_time_arr[0],""+date_time_arr[1]));
                editText.setText("");

                // WebServer Request URL to send message to device.
                String serverURL = Config.YOUR_SERVER_URL+"store_data.php";
                new SendToServer().execute(serverURL,  senderName,senderEmail, message.toString().trim());

                if (editText.getText().length() > 0) {
                    editText.setText(null);
                }

                Toast.makeText(getBaseContext(), "Message sent."+Result, Toast.LENGTH_LONG).show();

            }
        }
    }

    /*********** Upload Message To Server *****************/
    public class SendToServer extends AsyncTask<String, Void, String> {

        private String Error = null;
        String data  = "";

        protected void onPreExecute() {

        }

        // Call after onPreExecute method
        protected String doInBackground(String... params) {

            /************ Make Post Call To Web Server ***ReceiverName********/
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

                Log.d("Parameter test",""+params[1].toString()+""+params[2].toString()+""+params[3].toString());

                // Send POST data request
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write( data );
                wr.flush();

                Log.d("Parameter test", "send request");

                // Get the server response
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                Log.d("Paremeter test","Received Response");

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

            Log.d("Successfully Uploaded","");
            /*****************************************************/
            return Content;
        }

        protected void onPostExecute(String Result) {
            // NOTE: You can call UI Element here.

            if (Error != null)
            {
                //    Toast.makeText(getBaseContext(), "Error: "+Error, Toast.LENGTH_LONG).show();

            } else
            {
                //    Toast.makeText(getBaseContext(), "Uploaded to server."+Result, Toast.LENGTH_LONG).show();
            }
        }

    }


    // Class with extends AsyncTask class
    public class FetchFromServer  extends AsyncTask<String, Void, String> {

        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(ShowMessage.this);
        String data ="";

        protected void onPreExecute() {
            //Start Progress Dialog (Message)
            Dialog.setMessage("Loading new messages...");
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

                Log.d("Parameter test",""+params[1]);
                Log.d("Parameter test",""+params[2]);
                Log.d("Parameter test",""+params[3]);

                // Set Request parameter
                if(!params[1].equals(""))
                    data +="&" + URLEncoder.encode("data1", "UTF-8") + "="+params[1].toString();
                if(!params[2].equals(""))
                    data +="&" + URLEncoder.encode("data2", "UTF-8") + "="+params[2].toString();
                if(!params[3].equals(""))
                    data +="&" + URLEncoder.encode("data3", "UTF-8") + "="+params[3].toString();
                Log.i("GCM", data);

                Log.d("Fetch test","request");
                // Send POST data request
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();

                Log.d("Fetch test", "response");
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

            Log.d("Fetch test", "" + Content);

            return Content;
        }

        protected void onPostExecute(String Content) {
            // NOTE: You can call UI Element here.

            // Close progress dialog
            Dialog.dismiss();

            if (Error != null) {

                Toast.makeText(ShowMessage.this,"Unable to fetch data from server",Toast.LENGTH_LONG).show();
            }
            else
            {

                // Show Response Json On Screen (activity)

                /****************** Start Parse Response JSON Data *************/
                aController.clearUserData();

                JSONObject jsonResponse;

                try {

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
                        String Email       =jsonChildNode.optString("email").toString();
                        String Message       = jsonChildNode.optString("message").toString();
                        String Time_Stamp      = jsonChildNode.optString("time_stamp").toString();

                        Log.i("GCM test", "---" + Name);
                        Log.i("GCM test", "---" + Email);
                        Log.i("GCM test", "---" + Message);
                        Log.i("GCM test", "---" + Time_Stamp);

                        if(!Name.equals("Data not found."))
                        {
                            UserData userData = new UserData(1, Name, Email, Message, Time_Stamp);

                            DBAdapter.addUserChatData(userData);

                            String date_time_arr[]=Time_Stamp.split(" ");

                            if ((userData.getName()).equals(senderName))
                            {
                                chatArrayAdapter.add(new ChatMessage(false, "" + "You" + " :" ,""+ userData.getMessage(),""+date_time_arr[0],""+date_time_arr[1]));
                            }
                            else
                            {
                                chatArrayAdapter.add(new ChatMessage(true, "" + userData.getName() + " :" ,""+ userData.getMessage(),""+date_time_arr[0],""+date_time_arr[1]));
                                //    gcmIntentService.generateNotification(ShowMessage.this,""+userData.getName(),""+userData.getMessage(),""+userData.getIMEI());
                            }
                        }
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void onResume(){
        super.onResume();
        isActive=true;
        isStop=true;
        Log.d("State","Resume");
        RemoveAllNotification();
    }

    protected void onPause() {
        super.onPause();
        Log.d("State","Pause");
        isActive=false;
        isStop=true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("State","Stop");
        isActive=false;
        isStop=true;
    }

    public void RemoveAllNotification() {

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();
    }

    @Override
    protected void onDestroy() {
  /*      try {
            // Unregister Broadcast Receiver
            unregisterReceiver(mHandleMessageReceiver);


        } catch (Exception e) {
            Log.e("UnRegister Receiver Error", "> " + e.getMessage());
        }
    */    super.onDestroy();
        Log.d("State","Destroy");
    }

}

