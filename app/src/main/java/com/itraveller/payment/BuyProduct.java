package com.itraveller.payment;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ebs.android.sdk.Config.Encryption;
import com.ebs.android.sdk.Config.Mode;
import com.ebs.android.sdk.EBSPayment;
import com.ebs.android.sdk.PaymentRequest;
import com.itraveller.R;


public class BuyProduct extends Activity implements OnClickListener {

	Button btn_buy;
	Double amount;
	EditText ed_quantity, ed_totalamount;

	private static String HOST_NAME = "";

	/*For Live*/
//	private static final int ACC_ID = 5128; // Provided by EBS
//	private static final String SECRET_KEY = "ebskey2";
	
	//private static final int ACC_ID = 5087; // Provided by EBS
	//private static final String SECRET_KEY = "fcfdfb899ccf83461ffffcc7ab8fa3bd";
	
	/*For Demo*/
	//Live Data
	private static final int ACC_ID = 13872; // Provided by EBS
	private static final String SECRET_KEY = "601615509525046666e247f18b8ceb51";
	
	private static final int PER_UNIT_PRICE = 1;
	double totalamount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.payment_buy_product);

		HOST_NAME = getResources().getString(R.string.hostname);
		
		initview();
		setOnClickListener();
	}

	protected void initview() {
		btn_buy = (Button) findViewById(R.id.btn_buy);
		ed_quantity = (EditText) findViewById(R.id.ed_quantity);
		ed_totalamount = (EditText) findViewById(R.id.ed_totalamount);
	}

	protected void setOnClickListener() {
		btn_buy.setOnClickListener(this);

		ed_quantity.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {

				calculateTotalAmount();
				ed_totalamount.setText(String.valueOf(totalamount));
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});

	}

	private void calculateTotalAmount() {

		if (ed_quantity.getText().toString().trim().length() > 0) {
			int quantity = Integer.parseInt(ed_quantity.getText().toString());
			totalamount = quantity * PER_UNIT_PRICE;
		} else {
			totalamount = 0.0;
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_buy) {
			if (ed_quantity.getText().toString().trim().length() <= 0
					|| Integer
							.parseInt(ed_quantity.getText().toString().trim()) == 0) {
				Toast.makeText(getApplicationContext(),
						"Please Enter Quantity", Toast.LENGTH_LONG).show();
			} else {
				callEbsKit();
			}
		}
	}

	private void callEbsKit() {

		/** Payment Amount Details */
		
		/** Mandatory */
		// Total Amount
		PaymentRequest.getInstance().setTransactionAmount(totalamount);
		
		/** Mandatory */
		// Reference No
		PaymentRequest.getInstance().setReferenceNo("223");
		//PaymentRequest.getInstance().
		
		/** Initializing the EBS Gateway */
		EBSPayment.getInstance().init(BuyProduct.this, ACC_ID, SECRET_KEY, Mode.ENV_LIVE, Encryption.ALGORITHM_MD5, HOST_NAME);
	}

}