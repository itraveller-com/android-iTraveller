package com.itraveller.constant;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.itraveller.R;
import com.itraveller.activity.ItinerarySummaryActivity;

public class CustomLoading {
	static Dialog dialog;
	Context context;
	public static void LoadingScreen(Context context, Boolean check) {
		SharedPreferences prefsData = context.getSharedPreferences("Itinerary", context.MODE_PRIVATE);
		String F_bit = ""+prefsData.getString("FlightBit",null);
		int flightBit = Integer.parseInt(""+F_bit);
		context = context;
		// create a Dialog component
		dialog = new Dialog(context);
		// tell the Dialog to use the dialog.xml as it's layout description
		dialog.setTitle("Flight Details");
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_dialog_box);
		dialog.findViewById(R.id.skip);
		Button skip_btn = (Button) dialog.findViewById(R.id.skip);
		final Context finalContext = context;

		final SharedPreferences prefs=context.getSharedPreferences("Preferences",context.MODE_PRIVATE);

		skip_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				SharedPreferences.Editor editor=prefs.edit();
				editor.putInt("Skip_Flight_Bit",1);
				editor.commit();
				dialog.dismiss();
				Intent intent = new Intent(finalContext.getApplicationContext(), ItinerarySummaryActivity.class);
				finalContext.startActivity(intent);
				//finish();
			}
		});
		dialog.setCanceledOnTouchOutside(check);
		dialog.show();
	}
	public static void LoadingHide() {
		// create a Dialog componen
		dialog.dismiss();
	}
}
