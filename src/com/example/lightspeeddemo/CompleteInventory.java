package com.example.lightspeeddemo;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.example.database.ItemDatabaseHandler;
import com.example.database.ItemDetail;
import com.example.functions.Constants;
import com.example.functions.Functions;
import com.example.functions.NetConnection;
import com.example.lightspeeddemo.MainActivity.account;
import com.example.lightspeeddemo.ViewCurrentBatch.Post;

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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CompleteInventory extends Activity {

	public String global_account_id;
	Button confirm;
	SharedPreferences sp;
	Boolean isConnected;
	Boolean IsSuccessfullyPosted;
	TextView complete_text;
	Boolean isSuccessfullyPosted = false;
	int DBSize ;
	int i = 0;
	ArrayList<HashMap<String, String>> temp_itemId_List = new ArrayList<HashMap<String, String>>();
	ArrayList<String> idList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.complete_inventory);
		confirm = (Button) findViewById(R.id.confirm);
		isConnected = NetConnection
				.checkInternetConnectionn(CompleteInventory.this);
		sp = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		global_account_id = sp.getString(Constants.ACCOUNT_ID, "");

		complete_text = (TextView) findViewById(R.id.complete_text);
		
		ItemDatabaseHandler db = new ItemDatabaseHandler(
				CompleteInventory.this);
		
		List<ItemDetail> contacts = db.getAllContacts();
	
		for (ItemDetail cn : contacts) {
		
			String log = "account Id: " + cn.getAccount_id() + " ,qty: "
					+ cn.getQuantity() + " ,inv_count_id: "
					+ cn.getInventory_count_id() + " ,item id: "
					+ cn.getItem_id() + " ,employee id: "
					+ cn.getEmployee_id();
			Log.d("log==",""+log);
			
			
		} 
		DBSize = contacts.size();
		
		String acountID="",Itemquantity="", invCountID="", itemID="", employeeID="";
		for(int j =0 ;j<contacts.size();j++){
			String totalQuantity="0";
			String itemIDToCheck=  contacts.get(j).getItem_id();
			if(!idList.contains(itemIDToCheck)){
				HashMap<String , String> map = new HashMap<String, String>();
				for(int x = 0 ;x<contacts.size();x++){
					idList.add(itemIDToCheck);
					if(itemIDToCheck.equalsIgnoreCase(contacts.get(x).getItem_id())){
						 acountID=  contacts.get(x).getAccount_id();
						 Itemquantity=  contacts.get(x).getQuantity();
						 invCountID=  contacts.get(x).getInventory_count_id();
						 itemID=  contacts.get(x).getItem_id();
						 employeeID=  contacts.get(x).getEmployee_id();
						 totalQuantity = String.valueOf(Integer.parseInt(totalQuantity) +Integer.parseInt(Itemquantity));
			
					}
				}
				map.put("item_id", itemID);
				map.put("account_id", acountID);
				map.put("inventory_count_id", invCountID);
				map.put("employee_id", employeeID);
				map.put("total_quantity", totalQuantity);
				
				temp_itemId_List.add(map);
			}
		
			else {
				continue;
			}

		}
		
		Log.i("items to post==",""+temp_itemId_List);
		int push_item_count = 0;
		for(int z =0;z<temp_itemId_List.size();z++){
			push_item_count = push_item_count+Integer.parseInt(temp_itemId_List.get(z).get("total_quantity"));
		}

		complete_text.setText("You are about to push "
				+ push_item_count + " items to the "
				+ Constants.CREATED_INVENTORY_NAME
				+ " Inventory.Please confirm by clicking CONFIRM button.");
		confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isConnected) {
					new Post(global_account_id).execute(new Void[0]);
				} else {
					showAlertToUser("No Internet Connection.");
				}

			}
		});
	}

	private void showAlertToUser(String paramString) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(
				CompleteInventory.this);
		localBuilder.setMessage(paramString).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(
							DialogInterface paramAnonymousDialogInterface,
							int paramAnonymousInt) {

						paramAnonymousDialogInterface.cancel();
						if(isSuccessfullyPosted){
//							showPrintAlert("Do you want to print a label for the item ?");
							Intent i = new Intent(CompleteInventory.this , HomeScreen.class);
							startActivity(i);
						}

					}
				});
		localBuilder.create().show();
	}

	public class Post extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		JSONObject result = new JSONObject();
		Dialog dialog;
		String id;
		String inv_count_id = Constants.INVENTORY_COUNT_ID ;
		String acountID = "",quantity="",invCountID="",itemID="",employeeID="";
		
		public Post(String account_id) {
			this.id = account_id;
		}

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			dialog = ProgressDialog.show(CompleteInventory.this, "Loading...",
					"Please Wait", true, false);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				
				

					inv_count_id = Constants.INVENTORY_COUNT_ID;
					
					
					
								 acountID=  temp_itemId_List.get(i).get("account_id");
								 quantity=  temp_itemId_List.get(i).get("total_quantity");
								 invCountID=  temp_itemId_List.get(i).get("inventory_count_id");
								 itemID=  temp_itemId_List.get(i).get("item_id");
								 employeeID=  temp_itemId_List.get(i).get("employee_id");
						
					
//					result = function.post(acountID, quantity,
//							invCountID, itemID,employeeID);
		
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
			ItemDatabaseHandler db = new ItemDatabaseHandler(
					CompleteInventory.this);
			try {
				JSONObject attributes = result.getJSONObject("@attributes");
				String count = attributes.getString("count");
				int count_int = Integer.parseInt(count);
				
				if (count_int>0) {
					isSuccessfullyPosted = true;
					db.deleteContact(itemID);
				
					i++;
					Constants.DB_quantity = 0 ;
					if(i<temp_itemId_List.size()){
					new Post(global_account_id).execute(new Void[0]);
					}
					else if(i>=temp_itemId_List.size()){
						
//						db.dropTable();
//						db.CreateTable();
						showAlertToUser("Confirmed....Successfully posted!!!");
						
					}

				} else {
					showAlertToUser("Something went wrong while processing the request.Please try again.");
					Constants.DB_quantity = 0 ;
				}
			} catch (Exception e) {
				e.printStackTrace();
				Constants.DB_quantity = 0 ;
				Log.e("Constants.ERROR_CODE_LOGIN==",""+Constants.ERROR_CODE_LOGIN);
				if(Constants.ERROR_CODE_LOGIN.equals("503")){
					 Thread t = new Thread() {
							public void run() {
								try {
									final Dialog wait = ProgressDialog.show(CompleteInventory.this, "Loading...",
											"Please Wait for 60 seconds", true, false);;
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										// loader
										
										wait.show();
									}
								});
									sleep(60 * 1000);
									wait.dismiss();
									runOnUiThread(new Runnable() {
										@Override
										public void run() {
											new Post(global_account_id).execute(new Void[0]);
										}
									});
									
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						};
						t.start();
				
					
				}
				else if(Constants.ERROR_CODE_LOGIN.equals("500")){
				//	showAlertToUser("Something went wrong while posting item with itemID "+itemID+". Click OK if you want to skip this item and to proceed further.");
					db.deleteContact(itemID);
					new Post(global_account_id).execute(new Void[0]);
				}
			}

		}
	}
	
	private void showPrintAlert(String msg) {
	AlertDialog.Builder localBuilder = new AlertDialog.Builder(CompleteInventory.this);
	
	localBuilder.setMessage(msg);
	localBuilder.setPositiveButton("Yes",
			new DialogInterface.OnClickListener() {
				public void onClick(
						DialogInterface paramAnonymousDialogInterface,
						int paramAnonymousInt) {
					paramAnonymousDialogInterface.dismiss();
					Intent i = new Intent(CompleteInventory.this, PrinterSettings.class);
					startActivity(i);
					
				}
			});
	localBuilder.setNegativeButton("No",
			new DialogInterface.OnClickListener() {
				public void onClick(
						DialogInterface paramAnonymousDialogInterface,
						int paramAnonymousInt) {
					paramAnonymousDialogInterface.dismiss();
					Intent i = new Intent(CompleteInventory.this, HomeScreen.class);
					startActivity(i);
					
				}
			});
	localBuilder.show();
	
}
}
