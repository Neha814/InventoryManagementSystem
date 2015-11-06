package com.example.lightspeeddemo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.example.database.ItemDatabaseHandler;
import com.example.functions.Constants;
import com.example.functions.Functions;
import com.example.functions.NetConnection;
import com.macrew.imageloader.ImageLoader;

public class HomeScreen extends Activity {

	TextView inventory_tv, verify_tv, location_settings;

	String shop_id, shop_name;
	ImageView pic;

	Boolean isConnected;
	SharedPreferences sp;
	String account_id;
	int count = 1;
	Button logout;
	public ImageLoader imageLoader;
	Button status;

	ArrayList<HashMap<String, String>> item_list = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> item_list1 = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_screen);
		inventory_tv = (TextView) findViewById(R.id.inventory_tv);
		verify_tv = (TextView) findViewById(R.id.verify_tv);
		logout = (Button) findViewById(R.id.logout);

		location_settings = (TextView) findViewById(R.id.location_settings);
		pic = (ImageView) findViewById(R.id.pic);
		status = (Button) findViewById(R.id.status);

		imageLoader = new ImageLoader(HomeScreen.this);
		imageLoader.DisplayImage(Constants.LOGO, R.drawable.shop_icon, pic);

		isConnected = NetConnection.checkInternetConnectionn(HomeScreen.this);
		sp = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		if (isConnected) {
			new shop().execute(new Void[0]);
		} else {
			showAlertToUser("No internet connection.");
		}

		Intent intent = getIntent();
		shop_id = intent.getStringExtra("shop_id");
		shop_name = intent.getStringExtra("shop_name");

		account_id = sp.getString(Constants.ACCOUNT_ID, "");
		
		status.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// url of website 
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

		inventory_tv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent(HomeScreen.this,
						InventoryCountSelectionScreen.class);

				startActivity(i);

			}
		});

		verify_tv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(HomeScreen.this, ScanFromLocalDB.class);
				startActivity(i);
			}
		});

		location_settings.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(HomeScreen.this, LocationSettings.class);
				startActivity(i);
			}
		});

	}

	private void showAlertToUser(String paramString) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(
				HomeScreen.this);
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

	private void showAlertToUser1(String msg) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(
				HomeScreen.this);

		localBuilder.setMessage(msg);
		localBuilder.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(
							DialogInterface paramAnonymousDialogInterface,
							int paramAnonymousInt) {
						paramAnonymousDialogInterface.dismiss();

							
						Intent i = new Intent(HomeScreen.this,
								MainActivity.class);
						i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(i);
						HomeScreen.this.finish();

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

		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		Dialog dialog;
		String user, pass;

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			dialog = ProgressDialog.show(HomeScreen.this, "Loading...",
					"Please Wait", true, false);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {

				result = function.shop();
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
					// Constants.shop_list = result;
					Constants.EMPLOYEE_ID = result.get(0).get("employeeID");
					Log.e("employee id==", "" + Constants.EMPLOYEE_ID);

				} else {
					showAlertToUser("No Data Found");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent i = new Intent(HomeScreen.this, StartUpScreen.class);
		startActivity(i);
	}
}
