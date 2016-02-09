package com.itraveller.dashboard;

/**
 * Created by rohan bundelkhandi on 25/12/2015.
 */
public class Config {

    // File upload url (replace the ip with your server address)
    public static final String CAMERA_FILE_UPLOAD_URL = "http://stage.itraveller.com/packages/android_chat_server/fileUploadCameraTravel.php";

    public static final String ATTACH_FILE_UPLOAD_URL = "http://stage.itraveller.com/packages/android_chat_server/fileUploadAttachTravel.php";


    public static final String CAMERA_DATA_URL = "http://stage.itraveller.com/packages/android_chat_server/fileDownloadCamera.php";

    public static final String ATTACH_DATA_URL = "http://stage.itraveller.com/packages/android_chat_server/fileDownloadAttach.php";

    // File url to download
    public static String file_download_url = "http://192.168.0.105/android_chat_server/Attachment_Files/giphy.gif";

    // Directory name to store captured images and videos
    public static final String IMAGE_DIRECTORY_NAME = "TravelAttachmentFiles/camera/";

    // Directory name to store captured images and videos
    public static final String ATTACH_DIRECTORY_NAME = "TravelAttachmentFiles/camera/";
}
