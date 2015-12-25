package com.itraveller.dashboard;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.itraveller.R;
import com.itraveller.chat.FileChooser;
import com.itraveller.chat.UploadActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttachFragment extends Fragment {

    private static final int REQUEST_PICK_FILE = 10;
    SharedPreferences prefs;
    private Uri fileUri; // file url to store image/video
    Button attach_btn;

    public AttachFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.attach_view, container, false);

        prefs=getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        attach_btn=(Button) view.findViewById(R.id.attach_file_btn);

        attach_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), FileChooser.class);
                startActivityForResult(intent, REQUEST_PICK_FILE);

            }
        });

        //    textView.setText("Four");
        return view;
    }


    /**
     * Receiving activity result method will be called after closing the camera
     * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_PICK_FILE)
        {
            // successfully captured the image
            // launching upload activity
            fileUri= Uri.parse(prefs.getString("PATH_STR", ""));

            if((""+prefs.getString("MIME","")).contains("image"))
                launchUploadActivity(true);
            else if((""+prefs.getString("MIME","")).contains("video"))
                launchUploadActivity(false);
        }

    }

    private void launchUploadActivity(boolean isImage){
        Intent i = new Intent(getActivity(), MyTravelUploadActivity.class);
        i.putExtra("filePath", fileUri.getPath());
        i.putExtra("isImage", isImage);
        startActivity(i);
    }
}
