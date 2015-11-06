package com.example.lightspeeddemo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;

import com.example.barcode.barc;

import com.example.functions.Constants;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

public class PrintDetailJewel extends Activity {

	private View mRootView;
	File pdf_file;
	File pdfDir;
	Bitmap screen;
	Document  document;
	String filename;
	ImageView img;
	TextView price_tv , description_tv;

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_printer);
		img = (ImageView) findViewById(R.id.imageView1);
		price_tv = (TextView) findViewById(R.id.price);
		description_tv = (TextView) findViewById(R.id.description);
		
	
		
	
				price_tv.setText("$" + Constants.DB_item_price);
				description_tv.setText(Constants.DB_description);
		
		/**
		 * create image from barcode
		 */
		try {
			Log.e("barcode==",""+Constants.BARCODE_CONTENT);
			BarcodeFormat br_f = BarcodeFormat
					.valueOf(Constants.BARCODE_FORMAT);
			Bitmap barcode_bitmap = barc.encodeAsBitmap(
					Constants.BARCODE_CONTENT, br_f, 200, 80);
			img.setImageBitmap(barcode_bitmap);
		} catch (WriterException e) {
		
			e.printStackTrace();
		}
	
		takeImageOfView();
	}

	private void takeImageOfView() {

		String state = Environment.getExternalStorageState();
		if (!Environment.MEDIA_MOUNTED.equals(state)) {
			Toast.makeText(getApplicationContext(),
					"ext storage is not writable", Toast.LENGTH_SHORT).show();
		}
		

	pdfDir = new File(Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/LightSpeedApp/");
		if (!pdfDir.exists()){
		    pdfDir.mkdir();
		}
	
		LayoutInflater localLayoutInflater = (LayoutInflater) getSystemService("layout_inflater");
		mRootView = new View(PrintDetailJewel.this);
		mRootView = localLayoutInflater.inflate(R.layout.test_printer, null);
		ImageView imageView1;
		TextView price,description;
		imageView1 = (ImageView) mRootView.findViewById(R.id.imageView1);
		price = (TextView) mRootView.findViewById(R.id.price);
		description = (TextView) mRootView.findViewById(R.id.description);
		
		Log.e("price(jewel)==",""+Constants.DB_item_price);
		Log.e("description(jewel)==",""+Constants.DB_description);
		Log.e("barcode format(jewel)==",""+Constants.BARCODE_FORMAT);
		Log.e("barcode content(jewel)==",""+Constants.BARCODE_CONTENT);
		
		price.setText("$" + Constants.DB_item_price);
		description.setText(Constants.DB_description);
		/**
		 * create image from barcode
		 */
		try {
			BarcodeFormat br_f = BarcodeFormat
					.valueOf(Constants.BARCODE_FORMAT);
			Bitmap barcode_bitmap = barc.encodeAsBitmap(
					Constants.BARCODE_CONTENT, br_f, 200, 80);
			imageView1.setImageBitmap(barcode_bitmap);
		} catch (WriterException e) {
			
			e.printStackTrace();
			
		}
		mRootView.measure(800, 480);      // width , height
		mRootView.layout(0, 0, 800, 480); // left , top , right , bottom
		View v1 = mRootView.getRootView();
		v1.setDrawingCacheEnabled(true);
		screen = Bitmap.createBitmap(v1.getDrawingCache());
		v1.setDrawingCacheEnabled(false);

	
		 filename = String.valueOf(System.currentTimeMillis());
		 pdf_file = new File(pdfDir, filename+".pdf");
		
		// STEP 2: Convert the Image to PDF
		covertToPdf();
	}

	private void covertToPdf() {
		try {
             document = new Document();

            PdfWriter.getInstance(document, new FileOutputStream(pdf_file));
            document.open();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            screen.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            addImage(document,byteArray);

            
            // STEP 3: Save, preview, share the image
            
            saveImage();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
	
	private void saveImage() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		 Uri uri = Uri.fromFile(new File(pdfDir,  filename+".pdf"));
		 intent.setDataAndType(uri, "application/pdf");
		 intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		 startActivity(intent);
		 document.close();
	}

	/**
	 * add image method
	 * @param document
	 * @param byteArray
	 */

	private static void addImage(Document document,byte[] byteArray)
    {
        Image image = null;
        try
        {
           image = Image.getInstance(byteArray);
        }
        catch (BadElementException e)
        {
         
            e.printStackTrace();
        }
        catch (MalformedURLException e)
        {
            
            e.printStackTrace();
        }
        catch (IOException e)
        {
       
            e.printStackTrace();
        }

        try
        {
            document.add(image);
        } catch (DocumentException e) {
   
            e.printStackTrace();
        }
    }
	}
