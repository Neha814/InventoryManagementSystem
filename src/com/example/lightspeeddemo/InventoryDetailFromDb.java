package com.example.lightspeeddemo;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.functions.Constants;
import com.example.functions.Functions;
import com.example.lightspeeddemo.StartUpScreen.MyAdapter;
import com.example.lightspeeddemo.StartUpScreen.shop;
import com.macrew.imageloader.ImageLoader;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class InventoryDetailFromDb  extends Activity{
	
	TextView barcode_scanned,description,item_price,item_id,quantity ,code;
	TextView total_in_stock,venice,baby,malibu,westlake;
	Button print;
	ImageView pic;
	public ImageLoader imageLoader;

	SharedPreferences sp;
	String account_id;
	ArrayList<HashMap<String, String>> shopListForVerification = new ArrayList<HashMap<String , String>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inventory_detail_from_db);
		
		barcode_scanned = (TextView) findViewById(R.id.barcode_scanned);
		description = (TextView) findViewById(R.id.description);
		item_price = (TextView) findViewById(R.id.item_price);
		item_id = (TextView) findViewById(R.id.item_id);
		quantity = (TextView) findViewById(R.id.quantity);
		print = (Button) findViewById(R.id.print);
		total_in_stock = (TextView) findViewById(R.id.total_in_stock);
		venice = (TextView) findViewById(R.id.venice);
		baby = (TextView) findViewById(R.id.baby);
		malibu = (TextView) findViewById(R.id.malibu);
		westlake = (TextView) findViewById(R.id.westlake);
		code = (TextView) findViewById(R.id.code);
		pic = (ImageView) findViewById(R.id.pic);
		
		imageLoader = new ImageLoader(InventoryDetailFromDb.this);
		 imageLoader.DisplayImage(Constants.LOGO, R.drawable.shop_icon, pic);
		
		
		sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		account_id = sp.getString(Constants.ACCOUNT_ID, "");
		 
		
		barcode_scanned.setText(Constants.BARCODE_CONTENT);
		description.setText(Constants.DB_description);
		item_price.setText("$ "+Constants.DB_item_price);
		item_id.setText(Constants.DB_item_id);
		code.setText(Constants.CODE);
		
		new getInventoryDetail(Constants.BARCODE_CONTENT,account_id).execute(new Void[0]);
		
		
		print.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(InventoryDetailFromDb.this, PrinterSettings.class);
				startActivity(i);
			}
		});
	}

	public class getInventoryDetail extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();
	
		JSONObject result = new JSONObject();
		Dialog dialog;
		String systemsku , id;

		public getInventoryDetail(String bARCODE_CONTENT, String ID) {
			systemsku = bARCODE_CONTENT;
			id = ID;
		}

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			dialog = ProgressDialog.show(InventoryDetailFromDb.this, "Loading...",
					"Please Wait", true, false);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {

				result = function.getInventoryDetail(systemsku,id);
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
				JSONObject Item = result.getJSONObject("Item");
				JSONObject ItemShops = Item.getJSONObject("ItemShops");
				JSONArray ItemShop = ItemShops.getJSONArray("ItemShop");
				
				for(int i = 0 ; i<ItemShop.length();i++){
					HashMap localHashmap = new HashMap();
					
					localHashmap.put("qoh",ItemShop.getJSONObject(i).get("qoh"));
					localHashmap.put("shopID",ItemShop.getJSONObject(i).get("shopID"));
					
					shopListForVerification.add(localHashmap);
				}
				Log.e("shopListForVerification==",""+shopListForVerification);
				
				for(int i = 0;i<shopListForVerification.size();i++){
					String shopID = shopListForVerification.get(i).get("shopID");
					String qoh = shopListForVerification.get(i).get("qoh");
					
					Log.e("shopID==",""+shopID);
					Log.e("qoh==",""+qoh);
			
					if(shopID.equals("0")){
						total_in_stock.setText("In-Stock : "+qoh);
					} else if(shopID.equals("1")){
						venice.setText("Venice-Burro : "+qoh);
					} else if(shopID.equals("2")){
						baby.setText("Baby-Burro : "+qoh);
					} else if(shopID.equals("4")){
						malibu.setText("Malibu-Burro : "+qoh);
					}else if(shopID.equals("6")){
						westlake.setText("WestLake-Burro : "+qoh);
					}
				}
			} catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}