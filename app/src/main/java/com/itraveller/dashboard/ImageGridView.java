package com.itraveller.dashboard;

import java.io.File;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.itraveller.R;

public class ImageGridView extends AppCompatActivity {

    // Declare variables
    private String[] FilePathStrings;
    private String[] FileNameStrings;
    private File[] listFile;
    GridView grid;
    File file;
    ImageGridViewAdapter adapter;
    Toolbar mtoolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridview_main);

        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);


//        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Gallery");


        // Check for SD Card
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "Error! No SDCARD Found!", Toast.LENGTH_LONG)
                    .show();
        } else {
            // Locate the image folder in your SD Card
            // External sdcard location
            file = new File(
                    Environment
                            .getExternalStorageDirectory(),"Itraveller/camera/" );

            // Create the storage directory if it does not exist
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    Log.d("Oops! Failed create ", "" + Config.IMAGE_DIRECTORY_NAME + " directory");
                }
            }
        }

        if (file.isDirectory()) {
            listFile = file.listFiles();

            Log.d("List File",""+listFile.length);
            // Create a String array for FilePathStrings
            FilePathStrings = new String[listFile.length];
            // Create a String array for FileNameStrings
            FileNameStrings = new String[listFile.length];

            for (int i = 0; i < listFile.length; i++) {
                // Get the path of the image file
                FilePathStrings[i] = listFile[i].getAbsolutePath();
                // Get the name image file
                FileNameStrings[i] = listFile[i].getName();
            }
        }

        // Locate the GridView in gridview_main.xml
        grid = (GridView) findViewById(R.id.gridview);
        // Pass String arrays to LazyAdapter Class
        adapter = new ImageGridViewAdapter(this, FilePathStrings, FileNameStrings);

        if(adapter.getCount()==0)
        {
            Toast.makeText(getApplicationContext(),"No Data Found",Toast.LENGTH_LONG).show();
        }
        // Set the LazyAdapter to the GridView
        grid.setAdapter(adapter);

        // Capture gridview item click
        grid.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent i = new Intent(ImageGridView.this, ViewImage.class);
                // Pass String arrays FilePathStrings
                i.putExtra("filepath", FilePathStrings);
                // Pass String arrays FileNameStrings
                i.putExtra("filename", FileNameStrings);
                // Pass click position
                i.putExtra("position", position);
                startActivity(i);
            }

        });
    }

}