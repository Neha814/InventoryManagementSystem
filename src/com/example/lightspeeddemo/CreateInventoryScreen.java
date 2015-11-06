package com.example.lightspeeddemo;

import java.util.ArrayList;

import org.json.JSONObject;

import com.example.functions.Constants;
import com.example.functions.Functions;
import com.example.lightspeeddemo.StartUpScreen.MyAdapter;
import com.example.lightspeeddemo.StartUpScreen.shop;
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
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class CreateInventoryScreen extends Activity{
	
	EditText inventory_name;
	Button submit;
	SharedPreferences sp;
	String global_account_id;
	String hideValue;
	ImageView pic;
	public ImageLoader imageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_inventory_screen);
		inventory_name = (EditText) findViewById(R.id.inventory_name);
		submit = (Button) findViewById(R.id.submit);
		pic = (ImageView) findViewById(R.id.pic);
		
		 imageLoader = new ImageLoader(CreateInventoryScreen.this);
		 imageLoader.DisplayImage(Constants.LOGO, R.drawable.shop_icon, pic);
		
		sp = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		
		
		 hideValue = sp.getString("hide_keyboard_value", "0");
		 Log.d("hideValue==",""+hideValue);
		 
		 inventory_name.setOnTouchListener(new OnTouchListener(){
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(hideValue.equalsIgnoreCase("1")){
					Log.d("hide value==1","hide value ==1");
					inventory_name.setRawInputType(InputType.TYPE_NULL);
					inventory_name.setFocusable(true);
				} 
				return false;
			}
			 });
		global_account_id = sp.getString(Constants.ACCOUNT_ID, "");
		
		submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			String inventory_name_text = inventory_name.getText().toString();
			if(inventory_name_text.equals("") || inventory_name_text.equals(" ")){
				showAlertToUser("Please enter inventory name.");
			} else {
				
				Constants.CREATED_INVENTORY_NAME = inventory_name_text;
				new getInventoryCountID(inventory_name_text,global_account_id).execute(new Void[0]);
				
			}
				
			}
		});
	}
	private void showAlertToUser(String paramString) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(
				CreateInventoryScreen.this);
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
	
	public class getInventoryCountID extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();
	
		JSONObject result = new JSONObject();
		Dialog dialog;
		String inv_name;
		String id;

		public getInventoryCountID(String inventory_name_text, String id) {
			inv_name = inventory_name_text;
			this.id = id;
		}

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			dialog = ProgressDialog.show(CreateInventoryScreen.this, "Loading...",
					"Please Wait", true, false);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {

				result = function.getinventoryID(inv_name,id);
				Log.i("result==", "" + result);
			} catch (Exception localException) {
				localException.printStackTrace();
				
			}
			return null;

		}

		@Override
		protected void onPostExecute(Void paramVoid) {

			super.onPostExecute(paramVoid);
			dialog.dismiss();
			try {
				JSONObject attribute = result.getJSONObject("@attributes");
				String count = attribute.getString("count");
				if(count.equals("0")){
					showAlertToUser("InventoryCount not Created.Please try again after some time.");
				} else {
					JSONObject InventoryCount = result.getJSONObject("InventoryCount");
					String inv_count_id = InventoryCount.getString("inventoryCountID");
					String name = InventoryCount.getString("name");
					
					Constants.INVENTORY_COUNT_ID = inv_count_id;
					
					Intent i = new Intent(CreateInventoryScreen.this,OpenInventoryScreen.class);
					startActivity(i);
					
				}
			} catch(Exception e){
				e.printStackTrace();
				showAlertToUser("Please enter valid inventory name.");
			}
		}
	}
}
