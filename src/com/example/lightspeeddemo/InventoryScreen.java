package com.example.lightspeeddemo;

import java.util.ArrayList;
import java.util.HashMap;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.database.ItemDatabaseHandler;
import com.example.functions.Constants;
import com.example.functions.Functions;
import com.example.functions.NetConnection;
import com.macrew.imageloader.ImageLoader;

public class InventoryScreen extends Activity {
	Boolean isConnected;
	SharedPreferences sp;
	String global_account_id;
	ListView listview;
	MyAdapter mAdapter;
	ArrayList<HashMap<String, String>> inventory_list;
	public ImageLoader imageLoader;
	ImageView pic;
	TextView shop_name; 
	Button logout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inventory_screen);
		sp = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		isConnected = NetConnection
				.checkInternetConnectionn(InventoryScreen.this);
		
		listview = (ListView) findViewById(R.id.listview);
		
		shop_name = (TextView) findViewById(R.id.shop_name);

		pic = (ImageView) findViewById(R.id.pic);
		logout = (Button)findViewById(R.id.logout);

		 imageLoader = new ImageLoader(InventoryScreen.this);
		 imageLoader.DisplayImage(Constants.LOGO, R.drawable.shop_icon, pic);

		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Constants.INVENTORY_COUNT_ID = inventory_list.get(position)
						.get("inventoryCountID");
				Constants.TIMESTAMP = inventory_list.get(position)
						.get("timeStamp");
				Intent i = new Intent(InventoryScreen.this,
						OpenInventoryScreen.class);
				Constants.CREATED_INVENTORY_NAME = inventory_list.get(position).get("name"); 
				startActivity(i);

			}
		});
		
		logout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showAlertToUser1("Are you sure you want to logout.");
			}
		});

		if (isConnected) {
			global_account_id = sp.getString(Constants.ACCOUNT_ID, "");
			new InventoryCount(global_account_id).execute(new Void[0]);
		} else {
			showAlertToUser("No internet connection");
		}

	}

	private void showAlertToUser1(String msg) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(InventoryScreen.this);
		
		localBuilder.setMessage(msg);
		localBuilder.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(
							DialogInterface paramAnonymousDialogInterface,
							int paramAnonymousInt) {
						paramAnonymousDialogInterface.dismiss();
						
						Intent i = new Intent(InventoryScreen.this, MainActivity.class);
						startActivity(i);
						
					}
				});
		localBuilder.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					public void onClick(
							DialogInterface paramAnonymousDialogInterface,
							int paramAnonymousInt) {
						paramAnonymousDialogInterface.dismiss();
						
						
					}
				});
		localBuilder.show();
		
	}

	private void showAlertToUser(String paramString) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(
				InventoryScreen.this);
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

	public class InventoryCount extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		Dialog dialog;
		String id;

		public InventoryCount(String account_id) {
			this.id = account_id;
		}

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			dialog = ProgressDialog.show(InventoryScreen.this, "Loading...",
					"Please Wait", true, false);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {

				result = function.inventoryCount(this.id);
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
				if (result.size() > 0) {

					inventory_list = new ArrayList<HashMap<String, String>>();

					for (int i = 0; i < result.size(); i++) {
						if (result.get(i).get("shopID")
								.equalsIgnoreCase(Constants.SHOP_ID)
								&& result.get(i).get("archived")
										.equalsIgnoreCase("false")) {
							inventory_list.add(result.get(i));
							shop_name.setText(Constants.SHOP_NAME);
						}
				}
					if (inventory_list.size() > 0) {
						mAdapter = new MyAdapter(inventory_list,
								InventoryScreen.this);
						listview.setAdapter(mAdapter);
					} else {
						showAlertToUser("No data found.");
					}
				} else {
					showAlertToUser("No data found.");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class MyAdapter extends BaseAdapter {

		LayoutInflater mInflater = null;

		public MyAdapter(ArrayList<HashMap<String, String>> inventory_list,
				Activity activity) {
			mInflater = LayoutInflater.from(InventoryScreen.this);
		}

		@Override
		public int getCount() {

			return inventory_list.size();
		}

		@Override
		public Object getItem(int position) {

			return inventory_list.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {

				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.list_item, null);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				convertView.setTag(holder);

			}

			else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.name.setText(inventory_list.get(position).get("name"));

			return convertView;
		}

	}

	class ViewHolder {
		TextView name;

	}
}
