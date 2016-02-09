package com.itraveller.constant;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.itraveller.R;
import com.itraveller.activity.FlightActivity;
import com.itraveller.activity.FlightDomesticActivity;
import com.itraveller.activity.ItinerarySummaryActivity;
import com.itraveller.volley.AppController;

public class CustomLoading {
	static Dialog dialog;

	public static void LoadingScreen(Context context, Boolean check) {
		// create a Dialog component
		dialog = new Dialog(context);
		// tell the Dialog to use the dialog.xml as it's layout description
		dialog.setTitle("Flight Details");
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_dialog_box);
		dialog.findViewById(R.id.skip);
		Button skip_btn = (Button) dialog.findViewById(R.id.skip);

		dialog.setCancelable(false);

		final Context finalContext = context;

		final SharedPreferences prefs=context.getSharedPreferences("Preferences",context.MODE_PRIVATE);


		final SharedPreferences prefss=context.getSharedPreferences("Itinerary",context.MODE_PRIVATE);


		skip_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				SharedPreferences.Editor editor=prefs.edit();
				editor.putInt("Skip_Flight_Bit", 1);
				editor.putInt("No_Flights", 0);
				editor.commit();

				Log.d("Flag test", "" + prefss.getString("Flight_flag", ""));

				if((""+prefss.getString("Flight_flag","")).equals("0")) {
					AppController.getInstance().cancelPendingRequests(FlightDomesticActivity.volley_obj);
					FlightDomesticActivity.volley_obj=null;
				}
				else {
					AppController.getInstance().cancelPendingRequests(FlightActivity.volley_obj);
					FlightActivity.volley_obj=null;
				}

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
