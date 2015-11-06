package com.example.lightspeeddemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.functions.Constants;
import com.macrew.imageloader.ImageLoader;

public class PrinterSettings extends Activity {

	Button submit;
	RadioGroup radio_group;
	RadioButton radioButton_sq, radioButton_jew;
	ImageView pic;
	public ImageLoader imageLoader;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.printer_setting);
		submit = (Button) findViewById(R.id.submit);
		radio_group = (RadioGroup) findViewById(R.id.radio_group);
		radioButton_jew = (RadioButton) findViewById(R.id.radioButton_jew);
		radioButton_sq = (RadioButton) findViewById(R.id.radioButton_sq);
		pic = (ImageView) findViewById(R.id.pic);
		
		imageLoader = new ImageLoader(PrinterSettings.this);
		 imageLoader.DisplayImage(Constants.LOGO, R.drawable.shop_icon, pic);

		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				int selected = radio_group.getCheckedRadioButtonId();
				RadioButton rb = (RadioButton) findViewById(selected);
				String rb_id = (String) rb.getText();
				
				Constants.radioButtonString = rb_id;
				Log.i("radioButton","=="+Constants.radioButtonString);

				Intent i = new Intent(PrinterSettings.this,ImagePrintDemo.class);
				startActivity(i);
				
			}
		});
	}

}
