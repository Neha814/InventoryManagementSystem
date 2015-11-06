package com.example.lightspeeddemo;

import android.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import android.os.Bundle;
import android.preference.PreferenceManager;

import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.example.functions.Constants;
import com.macrew.imageloader.ImageLoader;

public class LocationSettings extends Activity {

	EditText printer1_ip_venice, printer2_ip_venice;
	EditText printer1_ip_baby, printer2_ip_baby;
	EditText printer1_ip_malibu, printer2_ip_malibu;
	EditText printer1_ip_westlake, printer2_ip_westlake;

	Button submit;
	SharedPreferences sp;
	String hide_keyboard_switch_text = "0";
	Switch hide_keyboard_switch;
	ImageView pic;
	public ImageLoader imageLoader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_settings);

		sp = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		Editor e = sp.edit();
		e.putString(Constants.printer1_ip_venice,
				sp.getString(Constants.printer1_ip_venice, ""));
		e.putString(Constants.printer2_ip_venice,
				sp.getString(Constants.printer2_ip_venice, ""));
		e.putString(Constants.printer1_ip_baby,
				sp.getString(Constants.printer1_ip_baby, ""));
		e.putString(Constants.printer2_ip_baby,
				sp.getString(Constants.printer2_ip_baby, ""));
		e.putString(Constants.printer1_ip_malibu,
				sp.getString(Constants.printer1_ip_malibu, ""));
		e.putString(Constants.printer2_ip_malibu,
				sp.getString(Constants.printer2_ip_malibu, ""));
		e.putString(Constants.printer1_ip_westlake,
				sp.getString(Constants.printer1_ip_westlake, ""));
		e.putString(Constants.printer2_ip_westlake,
				sp.getString(Constants.printer2_ip_westlake, ""));

		e.commit();

		printer1_ip_venice = (EditText) findViewById(R.id.printer1_ip_venice);
		printer2_ip_venice = (EditText) findViewById(R.id.printer2_ip_venice);
		printer1_ip_baby = (EditText) findViewById(R.id.printer1_ip_baby);
		printer2_ip_baby = (EditText) findViewById(R.id.printer2_ip_baby);
		printer1_ip_malibu = (EditText) findViewById(R.id.printer1_ip_malibu);
		printer2_ip_malibu = (EditText) findViewById(R.id.printer2_ip_malibu);
		printer1_ip_westlake = (EditText) findViewById(R.id.printer1_ip_westlake);
		printer2_ip_westlake = (EditText) findViewById(R.id.printer2_ip_westlake);
		hide_keyboard_switch = (Switch) findViewById(R.id.hide_keyboard_switch);
		pic = (ImageView) findViewById(R.id.pic);
		submit = (Button) findViewById(R.id.submit);
		
		imageLoader = new ImageLoader(LocationSettings.this);
		 imageLoader.DisplayImage(Constants.LOGO, R.drawable.shop_icon, pic);

		String hideValue = sp.getString("hide_keyboard_value", "0");

		if (hideValue.equalsIgnoreCase("0")) {
			hide_keyboard_switch.setChecked(false);
		} else if (hideValue.equalsIgnoreCase("1")) {
			hide_keyboard_switch.setChecked(true);
		}

		hide_keyboard_switch
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							// on
							hide_keyboard_switch_text = "1";
						} else {
							// off
							hide_keyboard_switch_text = "0";
						}
						Editor e = sp.edit();
						e.putString("hide_keyboard_value",
								hide_keyboard_switch_text);
						e.commit();
						
						Intent i = new Intent(LocationSettings.this , LocationSettings.class);
						startActivity(i);
					}
				});

		/*********************** hide keyboard on edittext touch listener ***************************/

		printer1_ip_venice.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				String hideValue = sp.getString("hide_keyboard_value", "0");
				if (hideValue.equalsIgnoreCase("1")) {

					printer1_ip_venice.setRawInputType(InputType.TYPE_NULL);
					printer1_ip_venice.setFocusable(true);
				}
				return false;
			}
		});
		printer2_ip_venice.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				String hideValue = sp.getString("hide_keyboard_value", "0");
				if (hideValue.equalsIgnoreCase("1")) {

					printer2_ip_venice.setRawInputType(InputType.TYPE_NULL);
					printer2_ip_venice.setFocusable(true);
				}
				return false;
			}
		});

		printer1_ip_baby.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				String hideValue = sp.getString("hide_keyboard_value", "0");
				if (hideValue.equalsIgnoreCase("1")) {

					printer1_ip_baby.setRawInputType(InputType.TYPE_NULL);
					printer1_ip_baby.setFocusable(true);
				}
				return false;
			}
		});
		printer2_ip_baby.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				String hideValue = sp.getString("hide_keyboard_value", "0");
				if (hideValue.equalsIgnoreCase("1")) {

					printer2_ip_baby.setRawInputType(InputType.TYPE_NULL);
					printer2_ip_baby.setFocusable(true);
				}
				return false;
			}
		});

		printer1_ip_malibu.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				String hideValue = sp.getString("hide_keyboard_value", "0");
				if (hideValue.equalsIgnoreCase("1")) {

					printer1_ip_malibu.setRawInputType(InputType.TYPE_NULL);
					printer1_ip_malibu.setFocusable(true);
				}
				return false;
			}
		});
		
		printer2_ip_malibu.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				String hideValue = sp.getString("hide_keyboard_value", "0");
				if (hideValue.equalsIgnoreCase("1")) {

					printer2_ip_malibu.setRawInputType(InputType.TYPE_NULL);
					printer2_ip_malibu.setFocusable(true);
				}
				return false;
			}
		});

		printer1_ip_westlake.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				String hideValue = sp.getString("hide_keyboard_value", "0");
				if (hideValue.equalsIgnoreCase("1")) {

					printer1_ip_westlake.setRawInputType(InputType.TYPE_NULL);
					printer1_ip_westlake.setFocusable(true);
				}
				return false;
			}
		});
		printer2_ip_westlake.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				String hideValue = sp.getString("hide_keyboard_value", "0");
				if (hideValue.equalsIgnoreCase("1")) {

					printer2_ip_westlake.setRawInputType(InputType.TYPE_NULL);
					printer2_ip_westlake.setFocusable(true);
				}
				return false;
			}
		});

		/*********************************************************************************************/
		printer1_ip_venice.setHint(sp.getString(Constants.printer1_ip_venice,
				""));
		printer2_ip_venice.setHint(sp.getString(Constants.printer2_ip_venice,
				""));
		printer1_ip_baby.setHint(sp.getString(Constants.printer1_ip_baby, ""));
		printer2_ip_baby.setHint(sp.getString(Constants.printer2_ip_baby, ""));
		printer1_ip_malibu.setHint(sp.getString(Constants.printer1_ip_malibu,
				""));
		printer2_ip_malibu.setHint(sp.getString(Constants.printer2_ip_malibu,
				""));
		printer1_ip_westlake.setHint(sp.getString(
				Constants.printer1_ip_westlake, ""));
		printer2_ip_westlake.setHint(sp.getString(
				Constants.printer2_ip_westlake, ""));

		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Editor e = sp.edit();
				e.putString(Constants.printer1_ip_venice, printer1_ip_venice
						.getText().toString());
				e.putString(Constants.printer2_ip_venice, printer2_ip_venice
						.getText().toString());
				e.putString(Constants.printer1_ip_baby, printer1_ip_baby
						.getText().toString());
				e.putString(Constants.printer2_ip_baby, printer2_ip_baby
						.getText().toString());
				e.putString(Constants.printer1_ip_malibu, printer1_ip_malibu
						.getText().toString());
				e.putString(Constants.printer2_ip_malibu, printer2_ip_malibu
						.getText().toString());
				e.putString(Constants.printer1_ip_westlake,
						printer1_ip_westlake.getText().toString());
				e.putString(Constants.printer2_ip_westlake,
						printer2_ip_westlake.getText().toString());

				e.commit();

				Intent i = new Intent(LocationSettings.this, HomeScreen.class);
				startActivity(i);
			}
		});
	}

}
