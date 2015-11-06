/***********************************************
 * CONFIDENTIAL AND PROPRIETARY 
 * 
 * The source code and other information contained herein is the confidential and the exclusive property of
 * ZIH Corp. and is subject to the terms and conditions in your end user license agreement.
 * This source code, and any other information contained herein, shall not be copied, reproduced, published, 
 * displayed or distributed, in whole or in part, in any medium, by any means, for any purpose except as
 * expressly permitted under such license agreement.
 * 
 * Copyright ZIH Corp. 2012
 * 
 * ALL RIGHTS RESERVED
 ***********************************************/

package com.example.lightspeeddemo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;

import android.os.Looper;
import android.preference.PreferenceManager;

import android.provider.MediaStore.Images.Media;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.example.barcode.barc;
import com.example.functions.Constants;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import com.macrew.imageloader.ImageLoader;
import com.zebra.android.devdemo.util.SettingsHelper;
import com.zebra.android.devdemo.util.UIHelper;

import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.comm.TcpConnection;
import com.zebra.sdk.device.ZebraIllegalArgumentException;
import com.zebra.sdk.graphics.internal.ZebraImageAndroid;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;

public class ImagePrintDemo extends Activity {

	private EditText ipAddressEditText;
	private EditText portNumberEditText;
	private EditText printStoragePath;

	private static final String tcpPortKey = "ZEBRA_DEMO_TCP_PORT";
	private static final String PREFS_NAME = "OurSavedAddress";
	private UIHelper helper = new UIHelper(this);
	private static int TAKE_PICTURE = 1;
	private static int PICTURE_FROM_GALLERY = 2;
	private static File file = null;
	Button print;
	Boolean isSquare = false, isJewel = false;
	View mRootView;
	Bitmap imgToPrint;
	ImageView image;
	SharedPreferences sp;
	ImageView pic;
	Bitmap barcode_bitmap;
	public ImageLoader imageLoader;
	EditText no_of_pages;
	int pages_count = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.image_print_demo);
		sp = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

		ipAddressEditText = (EditText) this.findViewById(R.id.ipAddressInput);
		pic = (ImageView) findViewById(R.id.pic);
		no_of_pages = (EditText) findViewById(R.id.no_of_pages);
		portNumberEditText = (EditText) this.findViewById(R.id.portInput);
		print = (Button) findViewById(R.id.print);
		image = (ImageView) findViewById(R.id.image);

		imageLoader = new ImageLoader(ImagePrintDemo.this);
		imageLoader.DisplayImage(Constants.LOGO, R.drawable.shop_icon, pic);

		// venice-burro
		if (Constants.SHOP_ID.equals("1")) {
			String ip1 = sp.getString(Constants.printer1_ip_venice, "");
			String ip2 = sp.getString(Constants.printer2_ip_venice, "");

			if ((ip1.equals("") || ip1.equals(" "))
					&& (ip2.equals("") || ip2.equals(" "))) {
				ipAddressEditText.setText("");
			} else if (!(ip1.equals("") || ip1.equals(" "))
					&& (ip2.equals("") || ip2.equals(" "))) {
				ipAddressEditText.setText(ip1);
			} else if ((ip1.equals("") || ip1.equals(" "))
					&& !(ip2.equals("") || ip2.equals(" "))) {
				ipAddressEditText.setText(ip2);
			} else if (!(ip1.equals("") || ip1.equals(" "))
					&& !(ip2.equals("") || ip2.equals(" "))) {
				ipAddressEditText.setText(ip1);
			}
		}
		// baby-burro
		else if (Constants.SHOP_ID.equals("2")) {
			String ip1 = sp.getString(Constants.printer1_ip_baby, "");
			String ip2 = sp.getString(Constants.printer2_ip_baby, "");

			if ((ip1.equals("") || ip1.equals(" "))
					&& (ip2.equals("") || ip2.equals(" "))) {
				ipAddressEditText.setText("");
			} else if (!(ip1.equals("") || ip1.equals(" "))
					&& (ip2.equals("") || ip2.equals(" "))) {
				ipAddressEditText.setText(ip1);
			} else if ((ip1.equals("") || ip1.equals(" "))
					&& !(ip2.equals("") || ip2.equals(" "))) {
				ipAddressEditText.setText(ip2);
			} else if (!(ip1.equals("") || ip1.equals(" "))
					&& !(ip2.equals("") || ip2.equals(" "))) {
				ipAddressEditText.setText(ip1);
			}
		}
		// malibu-burro
		else if (Constants.SHOP_ID.equals("4")) {
			String ip1 = sp.getString(Constants.printer1_ip_malibu, "");
			String ip2 = sp.getString(Constants.printer2_ip_malibu, "");

			if ((ip1.equals("") || ip1.equals(" "))
					&& (ip2.equals("") || ip2.equals(" "))) {
				ipAddressEditText.setText("");
			} else if (!(ip1.equals("") || ip1.equals(" "))
					&& (ip2.equals("") || ip2.equals(" "))) {
				ipAddressEditText.setText(ip1);
			} else if ((ip1.equals("") || ip1.equals(" "))
					&& !(ip2.equals("") || ip2.equals(" "))) {
				ipAddressEditText.setText(ip2);
			} else if (!(ip1.equals("") || ip1.equals(" "))
					&& !(ip2.equals("") || ip2.equals(" "))) {
				ipAddressEditText.setText(ip1);
			}
		}
		// westlake-burro
		else if (Constants.SHOP_ID.equals("6")) {
			String ip1 = sp.getString(Constants.printer1_ip_westlake, "");
			String ip2 = sp.getString(Constants.printer2_ip_westlake, "");

			if ((ip1.equals("") || ip1.equals(" "))
					&& (ip2.equals("") || ip2.equals(" "))) {
				ipAddressEditText.setText("");
			} else if (!(ip1.equals("") || ip1.equals(" "))
					&& (ip2.equals("") || ip2.equals(" "))) {
				ipAddressEditText.setText(ip1);
			} else if ((ip1.equals("") || ip1.equals(" "))
					&& !(ip2.equals("") || ip2.equals(" "))) {
				ipAddressEditText.setText(ip2);
			} else if (!(ip1.equals("") || ip1.equals(" "))
					&& !(ip2.equals("") || ip2.equals(" "))) {
				ipAddressEditText.setText(ip1);
			}
		} else {
			ipAddressEditText.setText("192.168.1.220");
			portNumberEditText.setText("1630");
		}

		String port = settings.getString(tcpPortKey, "");
		portNumberEditText.setText(port);

		printStoragePath = (EditText) findViewById(R.id.printerStorePath);

		CheckBox cb = (CheckBox) findViewById(R.id.checkBox);
		cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					printStoragePath.setVisibility(View.VISIBLE);
				} else {
					printStoragePath.setVisibility(View.GONE);
				}
			}
		});

		if (Constants.radioButtonString
				.equalsIgnoreCase("LOCATION Jewelry Printer")) {
			isSquare = false;
			isJewel = true;
			Log.e("isJewel", "isJewel");
		} else if (Constants.radioButtonString
				.equalsIgnoreCase("LOCATION Square Printer")) {
			isSquare = true;
			Log.e("isSquare", "ssSquare");
			isJewel = false;
		}

		ConvertLayoutToBitmap();

		print.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String page_count = no_of_pages.getText().toString();

				if (ipAddressEditText.getText().toString().trim().length() <= 0) {
					ipAddressEditText.setError("Please enter IP address.");
				} else if (portNumberEditText.getText().toString().trim()
						.length() <= 0) {
					portNumberEditText.setError("Please enter port number.");
				}

				else if (page_count.trim().length() <= 0) {
					no_of_pages
							.setError("Enter number of pages to be printed.");
				} else if (page_count.trim().length() > 0) {
					pages_count = Integer.parseInt(no_of_pages.getText()
							.toString());

					printPhotoFromExternal(imgToPrint);

				}

			}

		});

		RadioGroup radioGroup = (RadioGroup) this.findViewById(R.id.radioGroup);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.ipDnsRadio) {
					toggleEditField(portNumberEditText, true);
					toggleEditField(ipAddressEditText, true);

				}
			}
		});
	}

	private void ConvertLayoutToBitmap() {

		ConvertToBitmap();
	}

	private void ConvertToBitmap() {

		if (isSquare) {
			Log.e("isSquare", "isSquare");
			mRootView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.print_detail_square, null);
		} else {
			Log.e("isJewel", "isJewel");
			mRootView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.test_printer, null);
		}

		ImageView imageView1;
		TextView price, description;
		imageView1 = (ImageView) mRootView.findViewById(R.id.imageView1);
		price = (TextView) mRootView.findViewById(R.id.price);
		description = (TextView) mRootView.findViewById(R.id.description);

		Log.e("barcode content(square)==", "" + Constants.BARCODE_CONTENT);

		price.setText("$" + Constants.DB_item_price);
		description.setText(Constants.DB_description);

		// create image from barcode

		try {
		
			barcode_bitmap = barc.encodeAsBitmap(Constants.BARCODE_CONTENT,
					BarcodeFormat.CODE_128, 250, 50);
			Log.d("barcode bitmap==", "" + barcode_bitmap);
			imageView1.setImageBitmap(barcode_bitmap);

		} catch (WriterException e) {
			e.printStackTrace();

		}

		imgToPrint = createDrawableFromView(this, mRootView);
		Log.e("bitmap to print===", "" + imgToPrint);
		try {
			image.setImageBitmap(imgToPrint);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private Bitmap createDrawableFromView(Context context, View view) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetrics);
		view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
		view.layout(0, 0, displayMetrics.widthPixels,
				displayMetrics.heightPixels);

		view.buildDrawingCache();

		Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
				view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmap);
		view.draw(canvas);

		return bitmap;
	}



	private void toggleEditField(EditText editText, boolean set) {

	
		editText.setEnabled(set);
		editText.setFocusable(set);
		editText.setFocusableInTouchMode(set);
	}

	private String getTcpAddress() {
		return ipAddressEditText.getText().toString();
	}

	private String getTcpPortNumber() {
		return portNumberEditText.getText().toString();
	}


	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == TAKE_PICTURE) {
				printPhotoFromExternal(BitmapFactory.decodeFile(file
						.getAbsolutePath()));
			}
			if (requestCode == PICTURE_FROM_GALLERY) {
				Uri imgPath = data.getData();
				Bitmap myBitmap = null;
				try {
					myBitmap = Media.getBitmap(getContentResolver(), imgPath);
				} catch (FileNotFoundException e) {
					helper.showErrorDialog(e.getMessage());
				} catch (IOException e) {
					helper.showErrorDialog(e.getMessage());
				}
				printPhotoFromExternal(myBitmap);
			}
		}
	}

	private void printPhotoFromExternal(final Bitmap bitmap) {
		new Thread(new Runnable() {
			public void run() {
				try {
					getAndSaveSettings();

					Looper.prepare();
					helper.showLoadingDialog("Sending image to printer");
					Connection connection = getZebraPrinterConn();
					connection.open();
					ZebraPrinter printer = ZebraPrinterFactory
							.getInstance(connection);

					if (((CheckBox) findViewById(R.id.checkBox)).isChecked()) {
						printer.storeImage(printStoragePath.getText()
								.toString(), new ZebraImageAndroid(bitmap),
								550, 412);

					} else {
						// give print command as many time as equal to no of
						// pages to be printed.
						for (int i = 0; i < pages_count; i++) {
							Log.e("printing command==", "" + i);
							printer.printImage(new ZebraImageAndroid(bitmap),
									0, 0, 550, 412, false);
						}

					}
					connection.close();

					if (file != null) {
						file.delete();
						file = null;
					}
				} catch (ConnectionException e) {
					helper.showErrorDialogOnGuiThread(e.getMessage());

				} catch (ZebraPrinterLanguageUnknownException e) {
					helper.showErrorDialogOnGuiThread(e.getMessage());
				} catch (ZebraIllegalArgumentException e) {
					helper.showErrorDialogOnGuiThread(e.getMessage());
				} finally {
					if (bitmap != null && !bitmap.isRecycled()) {
						// bitmap.recycle();

					}

					helper.dismissLoadingDialog();
					Looper.myLooper().quit();
					// finish();

				}
			}
		}).start();

	}

	private Connection getZebraPrinterConn() {
		int portNumber;
		try {
			portNumber = Integer.parseInt(getTcpPortNumber());
		} catch (NumberFormatException e) {
			portNumber = 0;
		}

		return new TcpConnection(getTcpAddress(), portNumber);
	}

	private void getAndSaveSettings() {

		SettingsHelper.saveIp(ImagePrintDemo.this, getTcpAddress());
		SettingsHelper.savePort(ImagePrintDemo.this, getTcpPortNumber());
	}

	@Override
	protected void onDestroy() {
		if (barcode_bitmap != null && !barcode_bitmap.isRecycled()) {
			barcode_bitmap.recycle();
			barcode_bitmap = null;

		}

		super.onDestroy();
	}

	@Override
	public void onBackPressed() {

		try {
			if (barcode_bitmap != null && !barcode_bitmap.isRecycled()) {
				barcode_bitmap.recycle();
				barcode_bitmap = null;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onBackPressed();
	}
}
