package com.example.lightspeeddemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.database.ItemDatabaseHandler;
import com.example.database.ItemDetail;
import com.example.functions.Constants;
import com.example.functions.Functions;
import com.example.lightspeeddemo.ScanFromLocalDB.item_details;
import com.macrew.imageloader.ImageLoader;

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
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class InventoryDetail extends Activity {

	Button scan_next, review_batch;
	EditText barcode_scanned;
	Button print;
	ImageView pic;
	public ImageLoader imageLoader;
	EditText description, item_price, quantity, item_id, manual_quantity;

	SharedPreferences sp;

	ArrayList<HashMap<String, String>> ItemPriceList;
	Integer qnty;
	String global_account_id;
	Boolean wantToScanNextItem = false;
	String hideValue;
	String quantity_text;
	String qty_value;
	EditText code;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.inventory_detail_screen);

		print = (Button) findViewById(R.id.print);
		barcode_scanned = (EditText) findViewById(R.id.barcode_scanned);
		scan_next = (Button) findViewById(R.id.scan_next);
		description = (EditText) findViewById(R.id.description);
		item_price = (EditText) findViewById(R.id.item_price);
		quantity = (EditText) findViewById(R.id.quantity);
		item_id = (EditText) findViewById(R.id.item_id);
		pic = (ImageView) findViewById(R.id.pic);

		imageLoader = new ImageLoader(InventoryDetail.this);
		imageLoader.DisplayImage(Constants.LOGO, R.drawable.shop_icon, pic);

		code = (EditText) findViewById(R.id.code);
		manual_quantity = (EditText) findViewById(R.id.manual_quantity);
		review_batch = (Button) findViewById(R.id.next);
		

		barcode_scanned.setHint(Constants.CODE);
		description.setText(Constants.DB_description);
		code.setText(Constants.BARCODE_CONTENT);
		item_price.setText("$ " + Constants.DB_item_price);
		item_id.setText(Constants.DB_item_id);

		barcode_scanned.addTextChangedListener(mTextEditorWatcher);
		manual_quantity.addTextChangedListener(mTextEditorWatcher1);

		print.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(InventoryDetail.this,
						PrinterSettings.class);
				startActivity(i);
			}
		});

		
		String itemIdFromPreviousScreen = Constants.DB_item_id;
		String totalQuantity = "0";
		ItemDatabaseHandler db = new ItemDatabaseHandler(InventoryDetail.this);
		List<ItemDetail> contacts = db.getAllContacts();
		for (int i = 0; i < contacts.size(); i++) {
			String itemID = contacts.get(i).getItem_id();
			Log.i("itemID==", "" + itemID);
			Log.i("itemIdFromPreviousScreen==", "" + itemIdFromPreviousScreen);
			if (itemIdFromPreviousScreen.equals(itemID)) {

				String quantity_text = contacts.get(i).getQuantity();
				Log.d("quantity==", "" + quantity_text);
				totalQuantity = String.valueOf(Integer.parseInt(totalQuantity)
						+ Integer.parseInt(quantity_text));
			}

		}
		if (totalQuantity.equals("0")) {
			quantity.setText("1");
		} else {
			int qnty_count = Integer.parseInt(quantity.getText().toString()) + 1;

			int qnty_count1 = Integer.parseInt(totalQuantity) + 1;
			quantity.setText("" + qnty_count1);
		}

		sp = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		global_account_id = sp.getString(Constants.ACCOUNT_ID, "");

		hideValue = sp.getString("hide_keyboard_value", "0");
		Log.d("hideValue==", "" + hideValue);

		quantity.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (hideValue.equalsIgnoreCase("1")) {
					Log.d("hide value==1", "hide value ==1");
					quantity.setRawInputType(InputType.TYPE_NULL);
					quantity.setFocusable(true);
				}
				return false;
			}
		});

		manual_quantity.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (hideValue.equalsIgnoreCase("1")) {
					Log.d("hide value==1", "hide value ==1");
					quantity.setRawInputType(InputType.TYPE_NULL);
					quantity.setFocusable(true);
				}
				return false;
			}
		});

		scan_next.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				qnty = Integer.parseInt(quantity.getText().toString());
				int manualQTY;
				if (manual_quantity.getText().toString().equals("")) {
					manualQTY = 0;
				} else {
					manualQTY = Integer.parseInt(manual_quantity.getText()
							.toString());
				}
				qnty = qnty + manualQTY;
				if (qnty.equals("") || qnty.equals(" ")) {
					showAlertToUser("Please enter quantity first");
				} else if (qnty < 1) {
					showAlertToUser("Please enter quantity between 1 to 99");
				} else {

					ItemDatabaseHandler db = new ItemDatabaseHandler(
							InventoryDetail.this);

					List<ItemDetail> contacts = db.getAllContacts();
					ArrayList<String> item_id_list = new ArrayList<String>();
					for (int i = 0; i < contacts.size(); i++) {
						String itemID = contacts.get(i).getItem_id();
						item_id_list.add(itemID);
					}
					if (item_id_list.contains(Constants.DB_item_id)) {
						if (Constants.INVENTORY_COUNT_ID.equals(null)
								|| Constants.INVENTORY_COUNT_ID.equals("")
								|| Constants.INVENTORY_COUNT_ID.equals(" ")
								|| Constants.EMPLOYEE_ID.equals(null)
								|| Constants.EMPLOYEE_ID.equals("")) {
							showAlertToUser("Cannot update data to DB.as employee id or inventory id is null.");
						} else {
							db.updateContact(new ItemDetail(global_account_id,
									String.valueOf(qnty),
									Constants.INVENTORY_COUNT_ID,
									Constants.DB_item_id,
									Constants.EMPLOYEE_ID,
									Constants.DB_description));
						}

					} else {
						if (Constants.INVENTORY_COUNT_ID.equals(null)
								|| Constants.INVENTORY_COUNT_ID.equals("")
								|| Constants.INVENTORY_COUNT_ID.equals(" ")
								|| Constants.EMPLOYEE_ID.equals(null)
								|| Constants.EMPLOYEE_ID.equals("")) {
							showAlertToUser("Cannot add data to DB.as employee id or inventory id is null.");
						} else {
							db.addContact(new ItemDetail(global_account_id,
									String.valueOf(qnty),
									Constants.INVENTORY_COUNT_ID,
									Constants.DB_item_id,
									Constants.EMPLOYEE_ID,
									Constants.DB_description));
							Constants.DB_quantity = Integer.parseInt(quantity
									.getText().toString())
									+ Constants.DB_quantity;
						}
					}

					List<ItemDetail> contacts1 = db.getAllContacts();
					for (ItemDetail cn : contacts1) {

						String log = "account Id: " + cn.getAccount_id()
								+ " ,qty: " + cn.getQuantity()
								+ " ,inv_count_id: "
								+ cn.getInventory_count_id() + " ,item id: "
								+ cn.getItem_id() + " ,employee id: "
								+ cn.getEmployee_id();
						Log.e("log==", "" + log);

					}

					Constants.scan_next_item_code = Constants.BARCODE_CONTENT;
					Log.e("code===============", ""
							+ Constants.scan_next_item_code);
					Constants.DB_quantity = Integer.parseInt(quantity.getText()
							.toString()) + Constants.DB_quantity;
					Intent i = new Intent(InventoryDetail.this,
							OpenInventoryScreen.class);
				
					startActivity(i);
				}

			}
		});

		review_batch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				quantity_text = quantity.getText().toString();
				int qty = Integer.parseInt(quantity_text);
				int manualQTY;
				if (manual_quantity.getText().toString().equals("")) {
					manualQTY = 0;
				} else {
					manualQTY = Integer.parseInt(manual_quantity.getText()
							.toString());
				}
				qty = qty + manualQTY;
				if (quantity_text.equals("") || quantity_text.equals(" ")) {
					showAlertToUser("Please enter quantity");
				} else if (qty < 1) {
					showAlertToUser("Please enter quantity between 1 to 99");
				} else {
					ItemDatabaseHandler db = new ItemDatabaseHandler(
							InventoryDetail.this);

					List<ItemDetail> contacts = db.getAllContacts();
					ArrayList<String> item_id_list = new ArrayList<String>();
					for (int i = 0; i < contacts.size(); i++) {
						String itemID = contacts.get(i).getItem_id();
						item_id_list.add(itemID);

					}
					if (item_id_list.contains(Constants.DB_item_id)) {
						if (Constants.INVENTORY_COUNT_ID.equals(null)
								|| Constants.INVENTORY_COUNT_ID.equals("")
								|| Constants.INVENTORY_COUNT_ID.equals(" ")
								|| Constants.EMPLOYEE_ID.equals(null)
								|| Constants.EMPLOYEE_ID.equals("")) {
							showAlertToUser("Cannot update data to DB.as employee id or inventory id is null.");
						} else {
							db.updateContact(new ItemDetail(global_account_id,
									String.valueOf(qty),
									Constants.INVENTORY_COUNT_ID,
									Constants.DB_item_id,
									Constants.EMPLOYEE_ID,
									Constants.DB_description));
						}

					} else {
						if (Constants.INVENTORY_COUNT_ID.equals(null)
								|| Constants.INVENTORY_COUNT_ID.equals("")
								|| Constants.INVENTORY_COUNT_ID.equals(" ")
								|| Constants.EMPLOYEE_ID.equals(null)
								|| Constants.EMPLOYEE_ID.equals("")) {
							showAlertToUser("Cannot add data to DB.as employee id or inventory id is null.");
						} else {
							db.addContact(new ItemDetail(global_account_id,
									String.valueOf(qty),
									Constants.INVENTORY_COUNT_ID,
									Constants.DB_item_id,
									Constants.EMPLOYEE_ID,
									Constants.DB_description));
						}

						Constants.DB_quantity = Integer.parseInt(quantity
								.getText().toString()) + Constants.DB_quantity;
					}

					List<ItemDetail> contacts1 = db.getAllContacts();
					for (ItemDetail cn : contacts1) {

						String log = "account Id: " + cn.getAccount_id()
								+ " ,qty: " + cn.getQuantity()
								+ " ,inv_count_id: "
								+ cn.getInventory_count_id() + " ,item id: "
								+ cn.getItem_id() + " ,employee id: "
								+ cn.getEmployee_id();
						Log.e("log==", "" + log);

					}
					Constants.DB_quantity = Integer.parseInt(quantity.getText()
							.toString()) + Constants.DB_quantity;
					Intent i = new Intent(InventoryDetail.this,
							ViewCurrentBatch.class);
					startActivity(i);

				}

			}
		});
	
	}

	private void showAlertToUser(String paramString) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(
				InventoryDetail.this);
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

	private final TextWatcher mTextEditorWatcher = new TextWatcher() {

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}

		public void afterTextChanged(Editable s) {

			String abc = s.toString();
			if (abc.endsWith("**") && abc.length() > 2) {
		
				String manual_code = barcode_scanned.getText().toString();

				try {
					manual_code = manual_code.replace("*", "");
				} catch (Exception e) {
					e.printStackTrace();
				}
				Log.e("manula code===", "" + manual_code);
				Log.e("Constants.BARCODE_CONTENT===", ""
						+ Constants.BARCODE_CONTENT);

			

				if (manual_code.equals(Constants.BARCODE_CONTENT)
						|| manual_code.equals(Constants.CUSTOM_SKU)
						|| manual_code.equals(Constants.EAN)
						|| manual_code.equals(Constants.UPC)) {

					int get_qty = Integer.parseInt(quantity.getText()
							.toString()) + 1;

					int manualQTY;
					if (manual_quantity.getText().toString().equals("")) {
						manualQTY = 0;
					} else {
						manualQTY = Integer.parseInt(manual_quantity.getText()
								.toString());
					}
					get_qty = get_qty + manualQTY;
					quantity.setText("" + get_qty);

					barcode_scanned.setHint(barcode_scanned.getText()
							.toString());
					barcode_scanned.setText("");
					manual_quantity.setText("");
					manual_quantity.setHint("Manually add");
				} else {
					Log.i("else", "else");
					ItemDatabaseHandler db = new ItemDatabaseHandler(
							InventoryDetail.this);
					qty_value = quantity.getText().toString();

					int qty = Integer.parseInt(qty_value);
					int manualQTY;
					if (manual_quantity.getText().toString().equals("")) {
						manualQTY = 0;
					} else {
						manualQTY = Integer.parseInt(manual_quantity.getText()
								.toString());
					}
					qty = qty + manualQTY;

					List<ItemDetail> contacts = db.getAllContacts();
					ArrayList<String> item_id_list = new ArrayList<String>();
					for (int i = 0; i < contacts.size(); i++) {
						String itemID = contacts.get(i).getItem_id();
						item_id_list.add(itemID);

					}
					if (item_id_list.contains(Constants.DB_item_id)) {
						if (Constants.INVENTORY_COUNT_ID.equals(null)
								|| Constants.INVENTORY_COUNT_ID.equals("")
								|| Constants.INVENTORY_COUNT_ID.equals(" ")
								|| Constants.EMPLOYEE_ID.equals(null)
								|| Constants.EMPLOYEE_ID.equals("")) {
							showAlertToUser("Cannot update data to DB.as employee id or inventory id is null.");
						} else {
							db.updateContact(new ItemDetail(global_account_id,
									String.valueOf(qty),
									Constants.INVENTORY_COUNT_ID,
									Constants.DB_item_id,
									Constants.EMPLOYEE_ID,
									Constants.DB_description));
						}

					} else {
						if (Constants.INVENTORY_COUNT_ID.equals(null)
								|| Constants.INVENTORY_COUNT_ID.equals("")
								|| Constants.INVENTORY_COUNT_ID.equals(" ")
								|| Constants.EMPLOYEE_ID.equals(null)
								|| Constants.EMPLOYEE_ID.equals("")) {
							showAlertToUser("Cannot add data to DB.as employee id or inventory id is null.");
						} else {
							db.addContact(new ItemDetail(global_account_id,
									String.valueOf(qty),
									Constants.INVENTORY_COUNT_ID,
									Constants.DB_item_id,
									Constants.EMPLOYEE_ID,
									Constants.DB_description));
							Constants.DB_quantity = Integer.parseInt(quantity
									.getText().toString())
									+ Constants.DB_quantity;
						}
					}

				
					getItem(manual_code);
				}
			}

		}
	};

	private final TextWatcher mTextEditorWatcher1 = new TextWatcher() {

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// When No Password Entered
			
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}

		public void afterTextChanged(Editable s) {

			String abc = s.toString();
			if (abc.length() == 2) {
				String manual_qty = manual_quantity.getText().toString();
				String qty_text = quantity.getText().toString();
				int total_qty = Integer.parseInt(qty_text)
						+ Integer.parseInt(manual_qty);
				quantity.setText("" + total_qty);
				manual_quantity.setText("");
				manual_quantity.setHint("Manually add");

			}

		}
	};

	protected void getItem(String code) {
		new item_details(code).execute(new Void[0]);
	}

	public class item_details extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();
		Dialog dialog;

		JSONObject result = new JSONObject();
		ArrayList localArrayList = new ArrayList();
		String sku_code;

		public item_details(String bARCODE_CONTENT) {
			this.sku_code = bARCODE_CONTENT;
			Constants.CODE = bARCODE_CONTENT;
		}

		protected Void doInBackground(Void... paramVarArgs) {
			try {

				localArrayList.add(new BasicNameValuePair("sku", this.sku_code));
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

				
					barcode_scanned.setHint(Constants.CODE);
					barcode_scanned.setText("");
					description.setText(Constants.DB_description);
					code.setText(Constants.BARCODE_CONTENT);
					item_price.setText("$ " + Constants.DB_item_price);
					item_id.setText(Constants.DB_item_id);

				
					String itemIdFromPreviousScreen = Constants.DB_item_id;
					String totalQuantity = "0";
					ItemDatabaseHandler db = new ItemDatabaseHandler(
							InventoryDetail.this);
					List<ItemDetail> contacts = db.getAllContacts();
					for (int i = 0; i < contacts.size(); i++) {
						String itemID = contacts.get(i).getItem_id();
						Log.i("itemID==", "" + itemID);
						Log.i("itemIdFromPreviousScreen==", ""
								+ itemIdFromPreviousScreen);
						if (itemIdFromPreviousScreen.equals(itemID)) {

							String quantity_text = contacts.get(i).getQuantity();
							Log.d("quantity==", "" + quantity_text);
							totalQuantity = String.valueOf(Integer
									.parseInt(totalQuantity)
									+ Integer.parseInt(quantity_text));
						}

					}
					if (totalQuantity.equals("0")) {
						quantity.setText("1");
					} 
					else {
						
						int qnty_count1 = Integer.parseInt(totalQuantity) + 1;
						quantity.setText("" + qnty_count1);
					}

				} else if (response.equals("false")) {
					showAlertToUser("Barcode does not exist in the database");
				}
			}
			catch (Exception ae) {
				ae.printStackTrace();
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			dialog = ProgressDialog.show(InventoryDetail.this, "Loading...",
					"Please Wait", true, false);
			dialog.show();
		}

	}
}
