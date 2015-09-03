package com.itraveller.payment;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.itraveller.R;

public class PaymentSuccessActivity extends Activity {
	String payment_id;
	String PaymentId;
	String AccountId;
	String MerchantRefNo;
	String Amount;
	String DateCreated;
	String Description;
	String Mode;
	String IsFlagged;
	String BillingName;
	String BillingAddress;
	String BillingCity;
	String BillingState;
	String BillingPostalCode;
	String BillingCountry;
	String BillingPhone;
	String BillingEmail;
	String DeliveryName;
	String DeliveryAddress;
	String DeliveryCity;
	String DeliveryState;
	String DeliveryPostalCode;
	String DeliveryCountry;
	String DeliveryPhone;
	String PaymentStatus;
	String PaymentMode;
	String SecureHash;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.payment_success);

		Intent intent = getIntent();

		payment_id = intent.getStringExtra("payment_id");
		System.out.println("payment_id" + " " + payment_id);
		getJsonReport();

		Button btn_payment_success = (Button) findViewById(R.id.btn_payment_success);
		btn_payment_success.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PaymentSuccessActivity.this.finish();

			}
		});

	}

	private void getJsonReport() {
		String response = payment_id;

		JSONObject jObject;
		try {
			jObject = new JSONObject(response.toString());
			PaymentId = jObject.getString("PaymentId");
			AccountId = jObject.getString("AccountId");
			MerchantRefNo = jObject.getString("MerchantRefNo");
			Amount = jObject.getString("Amount");
			DateCreated = jObject.getString("DateCreated");
			Description = jObject.getString("Description");
			Mode = jObject.getString("Mode");
			IsFlagged = jObject.getString("IsFlagged");
			BillingName = jObject.getString("BillingName");
			BillingAddress = jObject
					.getString("BillingAddress");
			BillingCity = jObject.getString("BillingCity");
			BillingState = jObject.getString("BillingState");
			BillingPostalCode = jObject
					.getString("BillingPostalCode");
			BillingCountry = jObject
					.getString("BillingCountry");
			BillingPhone = jObject.getString("BillingPhone");
			BillingEmail = jObject.getString("BillingEmail");
			DeliveryName = jObject.getString("DeliveryName");
			DeliveryAddress = jObject
					.getString("DeliveryAddress");
			DeliveryCity = jObject.getString("DeliveryCity");
			DeliveryState = jObject.getString("DeliveryState");
			DeliveryPostalCode = jObject
					.getString("DeliveryPostalCode");
			DeliveryCountry = jObject
					.getString("DeliveryCountry");
			DeliveryPhone = jObject.getString("DeliveryPhone");
			PaymentStatus = jObject.getString("PaymentStatus");
			PaymentMode = jObject.getString("PaymentMode");
			SecureHash = jObject.getString("SecureHash");
			System.out.println("paymentid_rsp" + PaymentId);
			
			TableLayout table_payment = (TableLayout) findViewById(R.id.table_payment);
			ArrayList<String> arrlist = new ArrayList<String>();
			arrlist.add("PaymentId");
			arrlist.add("AccountId ");
			arrlist.add("MerchantRefNo");
			arrlist.add("Amount");
			arrlist.add("DateCreated");
			arrlist.add("Description");
			arrlist.add("Mode");
			arrlist.add("IsFlagged");
			arrlist.add("BillingName");
			arrlist.add("BillingAddress");
			arrlist.add("BillingCity");
			arrlist.add("BillingState");
			arrlist.add("BillingPostalCode");
			arrlist.add("BillingCountry");
			arrlist.add("BillingPhone");
			arrlist.add("BillingEmail");
			arrlist.add("DeliveryName");
			arrlist.add("DeliveryAddress");
			arrlist.add("DeliveryCity");
			arrlist.add("DeliveryState");
			arrlist.add("DeliveryPostalCode");
			arrlist.add("DeliveryCountry");
			arrlist.add("DeliveryPhone");
			arrlist.add("PaymentStatus");
			arrlist.add("PaymentMode");
			arrlist.add("SecureHash");
			
			ArrayList<String> arrlist1 = new ArrayList<String>();
			arrlist1.add(PaymentId);
			arrlist1.add(AccountId );
			arrlist1.add(MerchantRefNo);
			arrlist1.add(Amount);
			arrlist1.add(DateCreated);
			arrlist1.add(Description);
			arrlist1.add(Mode);
			arrlist1.add(IsFlagged);
			arrlist1.add(BillingName);
			arrlist1.add(BillingAddress);
			arrlist1.add(BillingCity);
			arrlist1.add(BillingState);
			arrlist1.add(BillingPostalCode);
			arrlist1.add(BillingCountry);
			arrlist1.add(BillingPhone);
			arrlist1.add(BillingEmail);
			arrlist1.add(DeliveryName);
			arrlist1.add(DeliveryAddress);
			arrlist1.add(DeliveryCity);
			arrlist1.add(DeliveryState);
			arrlist1.add(DeliveryPostalCode);
			arrlist1.add(DeliveryCountry);
			arrlist1.add(DeliveryPhone);
			arrlist1.add(PaymentStatus);
			arrlist1.add(PaymentMode);
			arrlist1.add(SecureHash);
			
			for(int i=0;i<arrlist.size();i++){
				TableRow row = new TableRow(this);
				
				TextView textH = new TextView(this);
				TextView textC = new TextView(this);
				TextView textV = new TextView(this);
				
				textH.setText(arrlist.get(i));
				textC.setText(":  ");
				textV.setText(arrlist1.get(i));
				textV.setTypeface(null,Typeface.BOLD);
				
				row.addView(textH);
				row.addView(textC);
				row.addView(textV);
				
				table_payment.addView(row);
			}
			
			/*tv_payment_id.setText("PaymentId : " + PaymentId
					+ "\n" + "AccountId : " + AccountId + "\n"
					+ "MerchantRefNo : " + MerchantRefNo + "\n"
					+ "Amount : " + Amount + "\n"
					+ "DateCreated : " + DateCreated + "\n"
					+ "Description : " + Description + "\n"
					+ "Mode : " + Mode + "\n" + "IsFlagged: "
					+ IsFlagged + "\n" + "BillingName : "
					+ BillingName + "\n" + "BillingAddress : "
					+ BillingAddress + "\n" + "BillingCity : "
					+ BillingCity + "\n" + "BillingState : "
					+ BillingState + "\n"
					+ "BillingPostalCode : " + BillingPostalCode
					+ "\n" + "BillingCountry : " + BillingCountry
					+ "\n" + "BillingPhone : " + BillingPhone
					+ "\n" + "BillingEmail : " + BillingEmail
					+ "\n" + "DeliveryName : " + DeliveryName
					+ "\n" + "DeliveryAddress : "
					+ DeliveryAddress + "\n" + "DeliveryCity : "
					+ DeliveryCity + "\n" + "DeliveryState : "
					+ DeliveryState + "\n"
					+ "DeliveryPostalCode : "
					+ DeliveryPostalCode + "\n"
					+ "DeliveryCountry : " + DeliveryCountry
					+ "\n" + "DeliveryPhone : " + DeliveryPhone
					+ "\n" + "PaymentStatus : " + PaymentStatus
					+ "\n" + "PaymentMode : " + PaymentMode
					+ "\n" + "SecureHash : " + SecureHash + "\n");*/
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}