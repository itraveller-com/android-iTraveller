package com.itraveller.chat;

/**
 * Created by rohan bundelkhandi on 17/12/2015.
 */
public class Config {

    // Server Url absolute url where php files are placed.
    public static final String YOUR_SERVER_URL   =  "http://192.168.0.105/android_chat_server/";

    // Google project id
    public static final String GOOGLE_SENDER_ID = "132433516320";


    // File upload url (replace the ip with your server address)
    public static final String FILE_UPLOAD_URL = "http://192.168.0.105/android_chat_server/fileUpload.php";

    // File url to download
    public static String file_download_url = "http://192.168.0.105/android_chat_server/Attachment_Files/giphy.gif";

    // Directory name to store captured images and videos
    public static final String IMAGE_DIRECTORY_NAME = "AttachmentFiles";
    /**
     * Tag used on log messages.
     */
    public static final String TAG = "GCM Chat";

    // Broadcast reciever name to show gcm registration messages on screen
    public static final String DISPLAY_REGISTRATION_MESSAGE_ACTION =
            "com.itraveller.DISPLAY_REGISTRATION_MESSAGE";

    // Broadcast reciever name to show user messages on screen
    public static final String DISPLAY_MESSAGE_ACTION =
            "com.itraveller.DISPLAY_MESSAGE";

    // Parse server message with this name
    public static final String EXTRA_MESSAGE = "message";

}
