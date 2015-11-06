package com.example.lightspeeddemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.example.database.ItemDatabaseHandler;
import com.example.database.ItemDetail;

import com.example.functions.Constants;
import com.example.functions.Functions;
import com.example.functions.NetConnection;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ViewCurrentBatch extends Activity {

	Button save_changes, complete_count;
	ListView listview;
	ItemDatabaseHandler db;
	MyAdapter mAdapter;
	String[] holdervalue;
	ImageView pic;
	SharedPreferences sp;
	public ImageLoader imageLoader;
	Boolean isConnected ;
	int i = 0;

	ArrayList<HashMap<String, String>> DbItems = new ArrayList<HashMap<String, String>>();

	private void showAlertToUser(String paramString) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(
				ViewCurrentBatch.this);
		localBuilder.setMessage(paramString).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(
							DialogInterface paramAnonymousDialogInterface,
							int paramAnonymousInt) {
						paramAnonymousDialogInterface.cancel();
						Intent i = new Intent(ViewCurrentBatch.this,
								OpenInventoryScreen.class);
						startActivity(i);

					}
				});
		localBuilder.create().show();
	}

	private void showAlertToUser1(String paramString) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(
				ViewCurrentBatch.this);
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_current_batch);
		
		isConnected = NetConnection
				.checkInternetConnectionn(ViewCurrentBatch.this);

		listview = (ListView) findViewById(R.id.listview);
		save_changes = (Button) findViewById(R.id.save_changes);
		complete_count = (Button) findViewById(R.id.complete_count);
		pic = (ImageView) findViewById(R.id.pic);
		
		try{
		DbItems.clear();
		} catch(Exception e){
		e.printStackTrace();	
		}

		sp = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		imageLoader = new ImageLoader(ViewCurrentBatch.this);
		imageLoader.DisplayImage(Constants.LOGO, R.drawable.shop_icon, pic);

		db = new ItemDatabaseHandler(ViewCurrentBatch.this);
		List<ItemDetail> contacts = db.getAllContacts();
		holdervalue = new String[contacts.size()];
		for (int i = (contacts.size() - 1); i >= 0; i--) {
			HashMap<String, String> map = new HashMap<String, String>();
			String accountID = contacts.get(i).getAccount_id();
			String quantity = contacts.get(i).getQuantity();
			String itemID = contacts.get(i).getItem_id();
			String inventoryID = contacts.get(i).getInventory_count_id();
			String employeeID = contacts.get(i).getEmployee_id();
			String description = contacts.get(i).getDescription();

			map.put("account_id", accountID);
			map.put("quantity", quantity);
			map.put("item_id", itemID);
			map.put("inventory_id", inventoryID);
			map.put("emplyee_id", employeeID);
			map.put("description", description);

			DbItems.add(map);
		}
		
		for(int i=0;i<DbItems.size();i++){
			holdervalue[i] = DbItems.get(i).get("quantity");
		}
		
		Log.d("DbItems==",""+DbItems);

		if (DbItems.size() > 0) {
			mAdapter = new MyAdapter(DbItems, ViewCurrentBatch.this);
			listview.setAdapter(mAdapter);
		} else {
			showAlertToUser("No data found in database");
		}

		save_changes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				for (int i = 0; i < holdervalue.length; i++) {
					Log.i("holdervalue"+"="+i, "" + holdervalue[i]);
					if( holdervalue[i]!=null){
						String inv_id = DbItems.get(i).get("inventory_id");
						String emp_id = DbItems.get(i).get("emplyee_id");
						// check inventory id or employee id for null value
						if(inv_id.equals(null) || inv_id.equals("")
								|| inv_id.equals(" ") || emp_id.equals(null)
								|| emp_id.equals("")){
							showAlertToUser1("Cannot update data to DB, as employee id or inventory id is null.");
						} else {
					db.updateContact(new ItemDetail(DbItems.get(i).get(
							"account_id"), holdervalue[i], DbItems.get(i).get(
							"inventory_id"), DbItems.get(i).get("item_id"),
							DbItems.get(i).get("emplyee_id"), DbItems.get(i)
									.get("description")));
						}
					}
				}

				Intent i = new Intent(ViewCurrentBatch.this,
						OpenInventoryScreen.class);
				startActivity(i);
			}
		});

		complete_count.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showPostAlert("Are you sure you want to post these items to inventory ?");

			}
		});
	}

	class MyAdapter extends BaseAdapter {

		LayoutInflater mInflater = null;

		public MyAdapter(ArrayList<HashMap<String, String>> shop_list,
				Activity activity) {
			mInflater = LayoutInflater.from(ViewCurrentBatch.this);
		}

		@Override
		public int getCount() {

			return DbItems.size();
		}

		@Override
		public Object getItem(int position) {

			return DbItems.get(position);
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
				convertView = mInflater.inflate(R.layout.db_list_item, null);
				holder.description = (TextView) convertView
						.findViewById(R.id.description);
				holder.qnty = (EditText) convertView.findViewById(R.id.qnty);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.qnty.setTag(position);

			holder.qnty.setText(holdervalue[position]);
			holder.description
					.setText(DbItems.get(position).get("description"));


			holder.qnty.setOnFocusChangeListener(new OnFocusChangeListener() {

				@Override
				public void onFocusChange(View v, boolean arg1) {
					if (!arg1) {
						Log.i("on focus chng","on focus chng");
						LinearLayout parent = (LinearLayout) v.getParent();
						EditText qtyTemp = (EditText) parent
								.findViewById(R.id.qnty);
						Log.e("pos==",""+position);
						holdervalue[position] = qtyTemp.getText().toString();
						for(int i=0;i<holdervalue.length;i++){
							Log.e("i=="+i,"value=="+holdervalue[i]);
						}
					}
				}
			});
			if (holdervalue != null) {

				holder.qnty.setText(holdervalue[position]);
				
			} else {

				holdervalue[position] = holder.qnty.getText().toString();
			}

			return convertView;
		}

	}

	class ViewHolder {
		TextView description;
		EditText qnty;
	}

	public class Post extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		JSONObject result = new JSONObject();
		ArrayList localArrayList = new ArrayList();
		Dialog dialog;
		Dialog waitDialog;

		String acountID = "", quantity = "", invCountID = "", itemID = "",
				employeeID = "";

		List<ItemDetail> contacts1 = db.getAllContacts();

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			dialog = ProgressDialog.show(ViewCurrentBatch.this, "Loading...",
					"Please Wait", true, false);
			dialog.show();
		

		}

		@Override
		protected Void doInBackground(Void... params) {
			try {

				acountID = contacts1.get(i).getAccount_id();
				quantity = contacts1.get(i).getQuantity();
				invCountID = contacts1.get(i).getInventory_count_id();
				itemID = contacts1.get(i).getItem_id();
				employeeID = contacts1.get(i).getEmployee_id();

				localArrayList.add(new BasicNameValuePair("account_id", sp
						.getString(Constants.ACCOUNT_ID, "")));
				localArrayList.add(new BasicNameValuePair("qty", quantity));
				localArrayList.add(new BasicNameValuePair("inventoryCountID",
						invCountID));
				localArrayList.add(new BasicNameValuePair("itemID", itemID));
				localArrayList.add(new BasicNameValuePair("employeeID",
						employeeID));

				result = function.post(localArrayList);

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

				String success = result.getString("success");

				if (success.equalsIgnoreCase("true")) {
					db.deleteContact(itemID);

					List<ItemDetail> contacts2 = db.getAllContacts();
					if (contacts2.size() > 0) {
						if(isConnected){
						new Post().execute(new Void[0]);
						} else {
							showAlertToUser1("No internet connection");
						}
					} else {

						showAlertToUser("Confirmed....Successfully posted!!!");
						DbItems.clear();
						db.dropTable();
						db.CreateTable();

					}

				} else {
					showAlertToUser("Something went wrong while processing the request.Please try again.");
					Constants.DB_quantity = 0;
				}
			} catch (Exception e) {
				e.printStackTrace();
				Constants.DB_quantity = 0;


			}

		}
	}

	private void showPostAlert(String msg) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(
				ViewCurrentBatch.this);

		localBuilder.setMessage(msg);
		localBuilder.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(
							DialogInterface paramAnonymousDialogInterface,
							int paramAnonymousInt) {
						paramAnonymousDialogInterface.dismiss();
						if(isConnected){
						new Post().execute(new Void[0]);
						} else {
							showAlertToUser1("No internet connection");
						}

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
}
