package com.itraveller.searchbar;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itraveller.R;

public class StickyBarActivity extends Activity implements ScrollViewListener {
    private final static String TAG = "StickyBarActivity";
    private ObservableScrollView observableScrollView;
    private RelativeLayout relativeLayout;
    private LinearLayout layoutStick;
    private LinearLayout layoutStick2;

    /**
     * Position of the bar where it must be sticked.
     */
    private int barPosition;
    /**
     * Height of the container view which holds the sticky layout.
     */
    private int height;

    private TextView tvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stickysearchbar);

        observableScrollView = (ObservableScrollView) findViewById(R.id.activity_sticky_bar_scroll_view);

        relativeLayout = (RelativeLayout) findViewById(R.id.activity_sticky_bar_relative_top_layout);
        layoutStick = (LinearLayout) findViewById(R.id.activity_sticky_bar_layout_sticky_view);
        layoutStick2 = (LinearLayout) findViewById(R.id.activity_sticky_bar_layout_sticky_view_2);
        layoutStick2.setVisibility(View.GONE);

        tvTest = (TextView) findViewById(R.id.activity_sticky_bar_tv_test);
        for (int i = 0; i < 30; i++) {
            tvTest.append("\nTest " + i);
        }
        observableScrollView.setScrollViewListener(this);
    }

    @Override
    public void onScrollChanged(ObservableScrollView observableScrollView, int x, int y, int oldx, int oldy) {
        if ( y == barPosition & y < height)
        {
            layoutStick.setVisibility(View.GONE);
            layoutStick2.setVisibility(View.VISIBLE);


        }else if (y < barPosition) {
            layoutStick.setVisibility(View.VISIBLE);
            layoutStick2.setVisibility(View.GONE);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        int stickViewHeight = layoutStick.getHeight();
        Log.i(TAG, "stickViewHeight: " + stickViewHeight);

        height = relativeLayout.getHeight();
        Log.i(TAG, "height: " + height);

        int paddingBottom = relativeLayout.getPaddingBottom();
        Log.i(TAG, "paddingBottom: " + paddingBottom);

        barPosition = height - stickViewHeight - paddingBottom;
    }
}