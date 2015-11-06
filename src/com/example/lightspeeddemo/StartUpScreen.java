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
import android.graphics.Bitmap;
import android.net.Uri;
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

public class StartUpScreen extends Activity {

	SharedPreferences sp;
	String account_id;
	ListView listview;
	MyAdapter mAdapter;
	Boolean isConnected;
	public ImageLoader imageLoader;
	Bitmap myBitmap;
	ImageView pic;
	Button logout;
	Button status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startup_screen);

		sp = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		pic = (ImageView) findViewById(R.id.pic);

		imageLoader = new ImageLoader(StartUpScreen.this);
		imageLoader.DisplayImage(Constants.LOGO, R.drawable.shop_icon, pic);

		account_id = sp.getString(Constants.ACCOUNT_ID, "");
		listview = (ListView) findViewById(R.id.listview);
		logout = (Button) findViewById(R.id.logout);
		status = (Button) findViewById(R.id.status);

		isConnected = NetConnection
				.checkInternetConnectionn(StartUpScreen.this);
		if (isConnected) {
			new shop().execute(new Void[0]);
		} else {
			showAlertToUser("No internet connection.");
		}

		status.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String url = "https://cloud.merchantos.com/login.html";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});

		logout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showAlertToUser1("Are you sure you want to logout.");
			}
		});

		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String name = Constants.shop_list.get(position).get("name");
				String shop_id = Constants.shop_list.get(position)
						.get("shopID");
				Intent i = new Intent(StartUpScreen.this, HomeScreen.class);
				i.putExtra("shop_name", name);
				i.putExtra("shop_id", shop_id);
				Constants.SHOP_ID = shop_id;
				Constants.SHOP_NAME = name;
				startActivity(i);

			}
		});
	}

	private void showAlertToUser1(String msg) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(
				StartUpScreen.this);

		localBuilder.setMessage(msg);
		localBuilder.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(
							DialogInterface paramAnonymousDialogInterface,
							int paramAnonymousInt) {
						paramAnonymousDialogInterface.dismiss();
					

						Intent i = new Intent(StartUpScreen.this,
								MainActivity.class);
						i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(i);
						StartUpScreen.this.finish();

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

	public class shop extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		ArrayList result;
		Dialog dialog;
		String user, pass;

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			dialog = ProgressDialog.show(StartUpScreen.this, "Loading...",
					"Please Wait", true, false);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {

				result = function.shop_list(account_id);
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
					Constants.shop_list = result;

					mAdapter = new MyAdapter(Constants.shop_list,
							StartUpScreen.this);
					listview.setAdapter(mAdapter);
				} else {
					showAlertToUser("No Data Found");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void showAlertToUser(String paramString) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(
				StartUpScreen.this);
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

	class MyAdapter extends BaseAdapter {

		LayoutInflater mInflater = null;

		public MyAdapter(ArrayList<HashMap<String, String>> shop_list,
				Activity activity) {
			mInflater = LayoutInflater.from(StartUpScreen.this);
		}

		@Override
		public int getCount() {

			return Constants.shop_list.size();
		}

		@Override
		public Object getItem(int position) {

			return Constants.shop_list.get(position);
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

			holder.name.setText(Constants.shop_list.get(position).get("name"));

			return convertView;
		}

	}

	class ViewHolder {
		TextView name;

	}
}
