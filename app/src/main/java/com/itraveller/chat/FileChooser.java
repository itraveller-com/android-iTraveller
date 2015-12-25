package com.itraveller.chat;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.itraveller.R;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileChooser extends ListActivity {

    private TextView m_titleView;
    private TextView m_titleView1;
    public final static String EXTRA_FILE_PATH = "file_path";
    private File currentDir;
    private FileArrayAdapter adapter;
    private FileFilter fileFilter;
    private File fileSelected;
    private ArrayList<String> extensions;
    public static String mime_str;
    SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.getStringArrayList("filterFileExtension") != null) {
                extensions = extras.getStringArrayList("filterFileExtension");
                fileFilter = new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        return ((pathname.isDirectory()) || (pathname.getName().contains(".")?extensions.contains(pathname.getName().substring(pathname.getName().lastIndexOf("."))):false));
                    }
                };
            }
        }

        prefs=getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        currentDir = new File("/sdcard/");
        fill(currentDir);


    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((!currentDir.getName().equals("sdcard")) && (currentDir.getParentFile() != null)) {
                currentDir = currentDir.getParentFile();
                fill(currentDir);
            } else {
                finish();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void fill(File f) {
        File[] dirs = null;
        if (fileFilter != null)
            dirs = f.listFiles(fileFilter);
        else
            dirs = f.listFiles();

        this.setTitle(getString(R.string.currentDir) + ": " + f.getName());
        List<Option> dir = new ArrayList<Option>();
        List<Option> fls = new ArrayList<Option>();
        try {
            for (File ff : dirs) {
                if (ff.isDirectory() && !ff.isHidden())
                    dir.add(new Option(ff.getName(), getString(R.string.folder), ff
                            .getAbsolutePath(), true, false));
                else {
                    if (!ff.isHidden())
                        fls.add(new Option(ff.getName(), getString(R.string.fileSize) + ": "
                                + ff.length(), ff.getAbsolutePath(), false, false));
                }
            }
        } catch (Exception e) {

        }
        Collections.sort(dir);
        Collections.sort(fls);
        dir.addAll(fls);
        if (!f.getName().equalsIgnoreCase("sdcard")) {
            if (f.getParentFile() != null) dir.add(0, new Option("..", getString(R.string.parentDirectory), f.getParent(), false, true));
        }

        adapter = new FileArrayAdapter(FileChooser.this, R.layout.file_view, dir);
        this.setListAdapter(adapter);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        Option o = adapter.getItem(position);
        if (o.isFolder() || o.isParent()) {
            currentDir = new File(o.getPath());
            fill(currentDir);

        }
        else {
            //onFileClick(o);
            fileSelected = new File(o.getPath());
            Intent intent = new Intent();
            intent.putExtra("fileSelected", fileSelected.getAbsolutePath());
            setResult(Activity.RESULT_OK, intent);

            mime_str=""+getMimeType(o.getName());

            if(mime_str.contains("image") || mime_str.contains("video"))
            {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("MIME", mime_str);
                editor.putString("PATH_STR",""+o.getPath());
                editor.commit();

                Toast.makeText(this, "File Selected: " + o.getPath() + "" + getMimeType(o.getName()), Toast.LENGTH_SHORT).show();

                finish();
            }
            else
            {
                Toast.makeText(this, "File format is incorrect select image or video only!!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

}