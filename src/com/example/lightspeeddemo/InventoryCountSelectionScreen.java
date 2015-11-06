package com.example.lightspeeddemo;

import com.example.functions.Constants;
import com.macrew.imageloader.ImageLoader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class InventoryCountSelectionScreen extends Activity {
	TextView create_new, open_inv;
	ImageView pic;
	public ImageLoader imageLoader;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.inventory_count_selection_screen);
		
		create_new = (TextView) findViewById(R.id.create_new);
		open_inv = (TextView) findViewById(R.id.open_inv);
		pic = (ImageView) findViewById(R.id.pic);
		
		 imageLoader = new ImageLoader(InventoryCountSelectionScreen.this);
		 imageLoader.DisplayImage(Constants.LOGO, R.drawable.shop_icon, pic);
		
		create_new.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(InventoryCountSelectionScreen.this, CreateInventoryScreen.class);
				startActivity(i);
			}
		});
		
		open_inv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(InventoryCountSelectionScreen.this, InventoryScreen.class);
				i.putExtra("shop_id", Constants.SHOP_ID);
				startActivity(i);

			}
		});
	}
}
