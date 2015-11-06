package com.example.lightspeeddemo;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.message.BasicNameValuePair;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.Toast;

import com.example.functions.Constants;
import com.example.functions.Functions;
import com.example.functions.NetConnection;

public class MainActivity extends Activity {

	EditText password, username;
	EditText database_url;
	Button submit;
	Boolean isConnected;
	SharedPreferences sp;
	ImageView pic;
	String databaseURL;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		submit = (Button) findViewById(R.id.submit);
		pic = (ImageView) findViewById(R.id.pic);
		database_url = (EditText) findViewById(R.id.database_url);

		sp = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				isConnected = NetConnection
						.checkInternetConnectionn(MainActivity.this);
				if (isConnected) {
					String user = username.getText().toString();
					String pass = password.getText().toString();
					databaseURL = database_url.getText().toString();
					if (user.equals("") || user.equals(" ")) {

						username.setError("Please enter username.");
					} else if (pass.equals("") || pass.equals(" ")) {

						password.setError("Please enter password.");
					} 
					else if(databaseURL.equals("") || databaseURL.equals(" ")){
						database_url.setError("Please enter account id");
					}
					
						else {
					
						/**
						 * call API for database url
						 */
							
						new account(user, pass).execute(new Void[0]);
					}
				} else {
					showAlertToUser("No Internet Connection.");
				}

			}
		});
	}

	/**
	 * method to show alert to the user
	 * 
	 * @param paramString
	 *            message shown to the user
	 */

	private void showAlertToUser(String paramString) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(
				MainActivity.this);
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

	public class account extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();
		ArrayList localArrayList = new ArrayList();
		HashMap result;
		Dialog dialog;
		String user, pass;

		public account(String user, String pass) {
			this.user = user;
			this.pass = pass;
		}

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			dialog = ProgressDialog.show(MainActivity.this, "Loading...",
					"Please Wait", true, false);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {

				result = function.account(this.user, this.pass);
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
				if (result.get("result").toString().equalsIgnoreCase("true")) {
					Editor e = sp.edit();
					e.putString(Constants.ACCOUNT_ID,
							result.get(Constants.ACCOUNT_ID).toString());
					e.putString(Constants.NAME, result.get(Constants.NAME)
							.toString());
					e.commit();
				
					Constants.USERNAME = this.user;
					Constants.PASSWORD = this.pass;
					Constants.USER_NAME = result.get(Constants.NAME).toString();
					
					if(databaseURL.equalsIgnoreCase(sp.getString(Constants.ACCOUNT_ID, ""))){
					new lightspeed(sp.getString(Constants.ACCOUNT_ID, "")).execute(new Void[0]);
					} else {
						showAlertToUser("Account id is not correct.Please sign in again with valid account id.");
					}
					
				}

				else if (result.get("result").toString()
						.equalsIgnoreCase("ERROR")) {
					String error_code = Constants.ERROR_CODE_LOGIN;
					if (error_code.equals("503")) {
						Thread t = new Thread() {
							public void run() {
								try {
									sleep(60 * 1000);
									new account(user, pass)
											.execute(new Void[0]);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						};
						t.start();
					} else if (error_code.equals("401")) {
						showAlertToUser("Invalid username or password");
					} else {
						showAlertToUser("Something went wrong while processing the request.Please try again.");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	
	
	public class getImage extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();
		ArrayList localArrayList = new ArrayList();
		JSONObject result = new JSONObject();
		Dialog dialog;
		String id;

		

		public getImage(String string) {
			this.id = string;
		}

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			dialog = ProgressDialog.show(MainActivity.this, "Loading...",
					"Please Wait", true, false);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				localArrayList.add(new BasicNameValuePair("account_id",
						this.id));
				result = function.getImage(localArrayList);
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
				String response = result.getString("ResponseCode");
				if (response.equals("true")) {
					String logo = result.getString("logo");
					Constants.LOGO = logo;
					Intent i = new Intent(MainActivity.this,
							StartUpScreen.class);
					startActivity(i);
					

				} else if (response.equals("false")) {
					Toast.makeText(getApplicationContext(), "No image url found", Toast.LENGTH_LONG).show();
					Intent i = new Intent(MainActivity.this,
							StartUpScreen.class);
					startActivity(i);
				}
			}

			catch (Exception ae) {

			}

		}

	}
	
	public class lightspeed extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();
		ArrayList localArrayList = new ArrayList();
		JSONObject result = new JSONObject();
		Dialog dialog;
		String id;

		

		public lightspeed(String string) {
		id = string;
		}

		
		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			dialog = ProgressDialog.show(MainActivity.this, "Loading...",
					"Please Wait", true, false);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				localArrayList.add(new BasicNameValuePair("account_id",
						this.id));
				result = function.lightspeed(localArrayList);
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
				String response = result.getString("ResponseCode");
				if (response.equals("true")) {
						new getImage(sp.getString(Constants.ACCOUNT_ID, "")).execute(new Void[0]);
				} else if (response.equals("false")) {
					showAlertToUser("Something went wrong. Please try again.");
				}
			}

			catch (Exception ae) {

			}

		}

	}

}
