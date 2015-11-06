package com.example.lightspeeddemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.barcode.IntentIntegrator;
import com.example.database.ItemDatabaseHandler;
import com.example.database.ItemDetail;
import com.example.functions.Constants;
import com.example.functions.Functions;
import com.macrew.imageloader.ImageLoader;

public class OpenInventoryScreen extends Activity {

	Button scan, ok, reviewBatch;
	EditText enter_manually;
	String hideValue;
	ItemDatabaseHandler db;
	SharedPreferences sp;
	ImageView pic;
	public ImageLoader imageLoader;
	ArrayList<HashMap<String, String>> ItemPriceList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.open_inventory_screen);
		scan = (Button) findViewById(R.id.scan);
		enter_manually = (EditText) findViewById(R.id.enter_manually);
		pic = (ImageView) findViewById(R.id.pic);

		imageLoader = new ImageLoader(OpenInventoryScreen.this);
		imageLoader.DisplayImage(Constants.LOGO, R.drawable.shop_icon, pic);

		enter_manually.addTextChangedListener(mTextEditorWatcher);

		ok = (Button) findViewById(R.id.ok);
		reviewBatch = (Button) findViewById(R.id.completecount);
		sp = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		db = new ItemDatabaseHandler(OpenInventoryScreen.this);
		List<ItemDetail> contacts = db.getAllContacts();
		int size = contacts.size();
		reviewBatch.setText("Review Batch (" + size + ")");
		hideValue = sp.getString("hide_keyboard_value", "0");
		Log.d("hideValue==", "" + hideValue);

		enter_manually.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (hideValue.equalsIgnoreCase("1")) {
					Log.d("hide value==1", "hide value ==1");
					enter_manually.setRawInputType(InputType.TYPE_NULL);
					enter_manually.setFocusable(true);
				}
				return false;
			}
		});

		ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String manual_code = enter_manually.getText().toString();

				Log.e("manula code===", "" + manual_code);
				if (manual_code.equals("") || manual_code.equals(" ")) {

					enter_manually.setError("Please enter barcode manually to proceed further.");
				}

				else {
					Constants.CODE = manual_code;
					Constants.BARCODE_CONTENT = manual_code;
					Constants.BARCODE_FORMAT = "EAN_13";

					new item_details(Constants.BARCODE_CONTENT)
							.execute(new Void[0]);

				}

			}
		});

		scan.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {

					Intent intent = new Intent(
							"com.google.zxing.client.android.SCAN");
					startActivityForResult(intent, 0);

				} catch (Exception e) {
					e.printStackTrace();
					IntentIntegrator integrator = new IntentIntegrator(
							OpenInventoryScreen.this);
					integrator.initiateScan();
				}
			}
		});

		reviewBatch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent(OpenInventoryScreen.this,
						ViewCurrentBatch.class);
				enter_manually.setText("");
				startActivity(i);
			}
		});
	}

	

	private final TextWatcher mTextEditorWatcher = new TextWatcher() {

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// When No Password Entered
			
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}

		public void afterTextChanged(Editable s) {

			String abc = s.toString();
			if (abc.endsWith("**") && abc.length() > 2) {
			
				String manual_code = enter_manually.getText().toString();

				try {
					manual_code = manual_code.replace("*", "");
				} catch (Exception e) {
					e.printStackTrace();
				}
				Log.e("manula code===", "" + manual_code);

				Constants.CODE = manual_code;
				Constants.BARCODE_CONTENT = manual_code;
				Constants.BARCODE_FORMAT = "EAN_13";

				new item_details(Constants.BARCODE_CONTENT)
						.execute(new Void[0]);

				Log.d("string", abc);

			}

		}
	};

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {

				String contents = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

				Constants.CODE = intent.getStringExtra("SCAN_RESULT");

				Constants.BARCODE_CONTENT = contents;
				Constants.BARCODE_FORMAT = format;

				Log.i("***scan result***", "***scan result***");
				Log.i("contents==", "" + contents);
				Log.i("format===", "" + format);

				new item_details(Constants.BARCODE_CONTENT)
						.execute(new Void[0]);

			} else if (resultCode == RESULT_CANCELED) {
				showAlertToUser("Barcode scanning gets cancelled.Please try again.");
			}
		}
	}

	private void showAlertToUser(String paramString) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(
				OpenInventoryScreen.this);
		localBuilder.setMessage(paramString).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(
							DialogInterface paramAnonymousDialogInterface,
							int paramAnonymousInt) {
						paramAnonymousDialogInterface.cancel();

					}
				});
		localBuilder.create().show();
	}

	public class item_details extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();
		Dialog dialog;

		JSONObject result = new JSONObject();
		ArrayList localArrayList = new ArrayList();
		String sku_code;

		public item_details(String bARCODE_CONTENT) {
			this.sku_code = bARCODE_CONTENT;
		}

		protected Void doInBackground(Void... paramVarArgs) {
			try {
				localArrayList.add(new BasicNameValuePair("sku",
						Constants.BARCODE_CONTENT));
				localArrayList.add(new BasicNameValuePair("account_id", sp.getString(Constants.ACCOUNT_ID, "")));

				result = function.get_item_details(localArrayList);

				Log.e("result item lit==", "" + result);

			} catch (Exception localException) {

			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			dialog.dismiss();

			try {
				String response = result.getString("ResponseCode");
				if (response.equals("true")) {
					JSONArray msgArray = result
							.getJSONArray("MessageWhatHappen");
					JSONObject jObj = msgArray.getJSONObject(0);

					Constants.DB_description = jObj.getString("description");
					Constants.DB_item_id = jObj.getString("itemID");
					Constants.DB_item_price = jObj.getString("item_price");
					Constants.CUSTOM_SKU = jObj.getString("customSku");
					Constants.UPC = jObj.getString("UPC");
					Constants.EAN = jObj.getString("EAN");

					Constants.BARCODE_CONTENT = jObj.getString("sku");
					Constants.BARCODE_FORMAT = "EAN_13";

					Intent intent = new Intent(OpenInventoryScreen.this,
							InventoryDetail.class);
					enter_manually.setText("");

					startActivity(intent);
				} else if (response.equals("false")) {
					showAlertToUser("Barcode does not exist in the database");
				}
			}

			catch (Exception ae) {

			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			dialog = ProgressDialog.show(OpenInventoryScreen.this,
					"Loading...", "Please Wait", true, false);
			dialog.show();
		}

	}

}
