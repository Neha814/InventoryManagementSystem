package com.example.functions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;



import android.util.Base64;
import android.util.Log;


@SuppressWarnings("deprecation")
public class JSONParser {
	static InputStream is = null;
	static String jObj = "";
	static String json = "";
	static int error_code;


	/**
	 * 
	 * @param url
	 * @param method
	 *            (post or get) method will decide which method is to be used.
	 * @param paramList
	 *            contains name value pair which is to be send in http request.
	 * @return String
	 * 
	 * 
	 */
	


	public String makeHttpRequest(String url, String method,
			java.util.List<org.apache.http.NameValuePair> paramList) {
		HttpClient httpclient = new DefaultHttpClient();

		HttpPost httppost = new HttpPost(url);

		String result = null;

		try {
			Log.e("URL===>", "" + url + paramList);
			httppost.setEntity(new UrlEncodedFormEntity(paramList));
			HttpResponse response = httpclient.execute(httppost);
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				Log.i("STATUS OK", "STATUS OK");
				result = out.toString();
			} else {
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}
		} catch (Exception e) {

			Log.i("error encountered", "" + e);
		}
		Log.i("web service result=======", "" + result);
		return result;

	}

	public String makeHttpRequest(String uri, String user, String pass) {
		
		HttpClient httpclient = new DefaultHttpClient();

		String credentials = user + ":" + pass;
		String credBase64 = Base64.encodeToString(credentials.getBytes(),
				Base64.DEFAULT).replace("\n", "");
		HttpGet httpGet = new HttpGet(uri);
		httpGet.setHeader("Authorization", "Basic " + credBase64);
		String result = null;

		try {
			Log.e("URL===>", "" + uri);
			Log.e("name=====>", "" + user+" pass=="+pass);

			HttpResponse response = httpclient.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			error_code = statusLine.getStatusCode();
			Log.i("status code==", "" + statusLine.getStatusCode());
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				Log.i("STATUS OK", "STATUS OK");
				result = out.toString();
			} else {
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}
		} catch (Exception e) {

			Log.i("error encountered", "" + e);
			e.printStackTrace();
			Constants.ERROR_CODE_LOGIN = String.valueOf(error_code);
		}
		Log.i("web service result=======", "" + result);
		return result;
	}

	public String makeHttpRequest1(String uri, String id, String json) {
		HttpClient httpclient = new DefaultHttpClient();

		String credentials = Constants.USERNAME + ":" + Constants.PASSWORD;
		String credBase64 = Base64.encodeToString(credentials.getBytes(),
				Base64.DEFAULT).replace("\n", "");

		HttpGet httpGet = new HttpGet(uri + "/" + id + "/" + json);
		httpGet.setHeader("Authorization", "Basic " + credBase64);

		String result = null;

		try {
			Log.e("URL===>", "" + uri + "/" + id + "/" + json);

			HttpResponse response = httpclient.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			error_code = statusLine.getStatusCode();
			Log.i("status code==", "" + statusLine.getStatusCode());
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				Log.i("STATUS OK", "STATUS OK");
				result = out.toString();
			} else {
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}
		} catch (Exception e) {

			Log.i("error encountered", "" + e);
			e.printStackTrace();

		}
		Log.i("web service result=======", "" + result);
		return result;
	}
	
	
	/**
	 * to find user shops and employee id
	 */

	public String shopHttpRequest(String uri) {
		HttpClient httpclient = new DefaultHttpClient();

		String credentials = Constants.USERNAME + ":" + Constants.PASSWORD;
		String credBase64 = Base64.encodeToString(credentials.getBytes(),
				Base64.DEFAULT).replace("\n", "");

		HttpGet httpGet = new HttpGet(uri);
		httpGet.setHeader("Authorization", "Basic " + credBase64);

		String result = null;

		try {
			Log.e("URL===>", "" + uri);

			HttpResponse response = httpclient.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			error_code = statusLine.getStatusCode();
			Log.i("status code==", "" + statusLine.getStatusCode());
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				Log.i("STATUS OK", "STATUS OK");
				result = out.toString();
			} else {
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}
		} catch (Exception e) {

			Log.i("error encountered", "" + e);
			e.printStackTrace();

		}
		Log.i("web service result=======", "" + result);
		return result;
	}
	

	

	
	/**
	 * to get item detail
	 */
	
	public String makeHttpRequest_item_detail(String uri, String id,
			String customsku,String json) {
		HttpClient httpclient = new DefaultHttpClient();
		
		String credentials = Constants.USERNAME + ":" + Constants.PASSWORD;
		String credBase64 = Base64.encodeToString(credentials.getBytes(),
				Base64.DEFAULT).replace("\n", "");

		HttpGet httpGet = new HttpGet(uri + "/" + id + "/" + json
				+ "?customSku="+customsku);
		httpGet.setHeader("Authorization", "Basic " + credBase64);

		String result = null;

		try {
			Log.e("URL===>", "" + uri + "/" + id + "/" + json
					+ "?customSku="+customsku);

			HttpResponse response = httpclient.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			error_code = statusLine.getStatusCode();
			Log.i("status code==", "" + statusLine.getStatusCode());
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				Log.i("STATUS OK", "STATUS OK");
				result = out.toString();
			} else {
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}
		} catch (Exception e) {

			Log.i("error encountered", "" + e);
			e.printStackTrace();

		}
		Log.i("web service result=======", "" + result);
		return result;
	}
	
	/**
	 * API to post inventory count detail 
	 * @param uri
	 * @param id
	 * @param json
	 * @param inventoryCountItemID
	 * @param qty
	 * @param timeStamp
	 * @param iNVENTORY_COUNT_ID
	 * @param itemID
	 * @param employeeID
	 * @return
	 */


	
	public String makeHttpRequest2(String uri, String id, String json,
			 String qty, String iNVENTORY_COUNT_ID, String itemID, String employeeID) {
		HttpClient httpclient = new DefaultHttpClient();

		String credentials = Constants.USERNAME + ":" + Constants.PASSWORD;
		String credBase64 = Base64.encodeToString(credentials.getBytes(),
				Base64.DEFAULT).replace("\n", "");
		
		HttpPost httpPost = new HttpPost(uri + "/" + id + "/" + json + "?");

		try{
			
			// Build the JSON object to pass parameters
			JSONObject jsonObj = new JSONObject();
			
			jsonObj.put("inventoryCountID", iNVENTORY_COUNT_ID);
			jsonObj.put("itemID", itemID);
			jsonObj.put("employeeID", employeeID);
			jsonObj.put("qty", qty);
			
			Log.e("qty==",""+qty);
			
			Log.e("url==",""+uri + "/" + id + "/" + json + "?"+jsonObj);
			
			StringEntity entity = new StringEntity(jsonObj.toString(), HTTP.UTF_8);
			entity.setContentType("application/json");
			httpPost.setEntity(entity);
			 httpPost.addHeader("Authorization", "Basic " + credBase64);
		
		
		} catch(Exception e){
			e.printStackTrace();
		}
		

		String result = null;

		try {
			HttpResponse response = httpclient.execute(httpPost);
			StatusLine statusLine = response.getStatusLine();
			error_code = statusLine.getStatusCode();
			Log.i("status code==", "" + statusLine.getStatusCode());
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				Log.i("STATUS OK", "STATUS OK");
				result = out.toString();
			} else {
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}
		} catch (Exception e) {

			Log.i("error encountered", "" + e);
			e.printStackTrace();
			Constants.ERROR_CODE_LOGIN = String.valueOf(error_code);

		}
		Log.i("web service result=======", "" + result);
		return result;
	}
	
	/**
	 *  to get inventory count id
	 */
	
	
	public String getinventoryCountID(String uri, String name,String id, String json) {
		HttpClient httpclient = new DefaultHttpClient();

		String credentials = Constants.USERNAME + ":" + Constants.PASSWORD;
		String credBase64 = Base64.encodeToString(credentials.getBytes(),
				Base64.DEFAULT).replace("\n", "");
		
		HttpPost httpPost = new HttpPost(uri + "/" + id + "/" + json + "?");

		try{
			
			// Build the JSON object to pass parameters
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("name", name);
//			jsonObj.put("archived", "true");
			jsonObj.put("shopID", Constants.SHOP_ID);
			
			Log.e("url==",""+uri + "/" + id + "/" + json + "?"+jsonObj);
			
			StringEntity entity = new StringEntity(jsonObj.toString(), HTTP.UTF_8);
			entity.setContentType("application/json");
			
			httpPost.setEntity(entity);
			 httpPost.addHeader("Authorization", "Basic " + credBase64);
		
		
		} catch(Exception e){
			e.printStackTrace();
		}
		

		String result = null;

		try {
			HttpResponse response = httpclient.execute(httpPost);
			StatusLine statusLine = response.getStatusLine();
			error_code = statusLine.getStatusCode();
			Log.i("status code==", "" + statusLine.getStatusCode());
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				Log.i("STATUS OK", "STATUS OK");
				result = out.toString();
			} else {
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}
		} catch (Exception e) {

			Log.i("error encountered", "" + e);
			e.printStackTrace();

		}
		Log.i("web service result=======", "" + result);
		return result;
	}
	
	/**
	 * to get inventory detail for veirification
	 */
	
	
	public String getInventoryDetail(String uri, String systemSKU,String id, String json) {
		HttpClient httpclient = new DefaultHttpClient();

		String credentials = Constants.USERNAME + ":" + Constants.PASSWORD;
		String credBase64 = Base64.encodeToString(credentials.getBytes(),
				Base64.DEFAULT).replace("\n", "");
		
		HttpGet httpGet = new HttpGet(uri + "/" + id + "/" + json
				+ "?systemSku="+systemSKU+"&load_relations=all");
		httpGet.setHeader("Authorization", "Basic " + credBase64);
		
		Log.e("url===",""+uri + "/" + id + "/" + json
				+ "?systemSku="+systemSKU+"&load_relations=all");

		String result = null;

		try {
			HttpResponse response = httpclient.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			error_code = statusLine.getStatusCode();
			Log.i("status code==", "" + statusLine.getStatusCode());
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				Log.i("STATUS OK", "STATUS OK");
				result = out.toString();
			} else {
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}
		} catch (Exception e) {

			Log.i("error encountered", "" + e);
			e.printStackTrace();

		}
		Log.i("web service result=======", "" + result);
		return result;
	}
	
	/**
	 * to get item details from local server
	 */
	
	public String makeHttpRequest_togetItemDetail(String url, String method,
			java.util.List<org.apache.http.NameValuePair> paramList) {

		HttpClient httpclient = new DefaultHttpClient();

		HttpPost httppost = new HttpPost(url);

		String result = null;

		try {
			Log.e("URL===>", "" + url + paramList);
			httppost.setEntity(new UrlEncodedFormEntity(paramList));
			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

			StatusLine statusLine = response.getStatusLine();

			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				Log.i("STATUS OK", "STATUS OK");

				result = out.toString();

			} else {
				// close connection
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}
		} catch (Exception e) {

			Log.i("error encountered", "" + e);
		}
		Log.i("web service result=======", "==" + result);
		return result;

	}
}
