package com.example.functions;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import android.text.Html;
import android.util.Log;

@SuppressWarnings("unchecked")


public class Functions {

	
	JSONParser json = new JSONParser();
	
	public static String login_uri = "https://api.merchantos.com/API/Account.json";
	public static String shop_uri = "https://api.merchantos.com/API/Account";
	public static String Usershop_uri = "https://api.merchantos.com/API/Login.json";
	
	public static String local_url = "http://barcodes.burrogoods.com/";
//	public static String local_url = "http://phphosting.osvin.net/barcodes/";


	/**
	 * web service for login
	 * 
	 * @param user
	 *            username
	 * @param pass
	 *            password
	 * @return account id , name
	 */


	
	
	public HashMap<String, String> account(String user, String pass) {
	
		HashMap localHashMap = new HashMap();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(this.login_uri, user, pass))
					.toString());
			JSONObject localJSONObject2 = localJSONObject
					.getJSONObject("Account");
			String accountID = localJSONObject2.getString("accountID");
			String name = localJSONObject2.getString("name");

			localHashMap.put("result", "true");
			localHashMap.put(Constants.ACCOUNT_ID, accountID);
			localHashMap.put(Constants.NAME, name);
			return localHashMap;
		} catch (Exception ae) {
			localHashMap.put("result", "ERROR");
			localHashMap.put("error_code", Constants.ERROR_CODE_LOGIN);
			return localHashMap;
		}
	}

	/**
	 * web service to get all shops stores
	 * 
	 * @param id
	 *            account_id
	 * @return list and details of shops
	 */

	public ArrayList<HashMap<String, String>> shop() {
		ArrayList<HashMap<String, String>> localList = new ArrayList<HashMap<String, String>>();
	

		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.shopHttpRequest(this.Usershop_uri))
					.toString());
			
			JSONObject employeeJson= localJSONObject.getJSONObject("Employee");
			HashMap localHashMap = new HashMap();
			localHashMap.put("employeeID", employeeJson.get("employeeID"));
			localHashMap.put("firstName", employeeJson.get("firstName"));
			localHashMap.put("lastName", employeeJson.get("lastName"));
			
			JSONObject shopJson= localJSONObject.getJSONObject("Shop");
			localHashMap.put("shopID", shopJson.get("shopID"));
			localHashMap.put("name", shopJson.get("name"));
			
			localList.add(localHashMap);
			

			return localList;
		} catch (Exception ae) {

			return localList;
		}
	}
	
	/**
	 *  to get shop list
	 */
	
	public ArrayList<HashMap<String, String>> shop_list(String id) {
		ArrayList<HashMap<String, String>> localList = new ArrayList<HashMap<String, String>>();
		String shop_string = "Shop.json";

		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest1(this.shop_uri,id,shop_string))
					.toString());
			
			JSONArray shop= localJSONObject.getJSONArray("Shop");
			for(int i = 0 ; i<shop.length();i++){
			HashMap localHashMap = new HashMap();
			localHashMap.put("shopID", shop.getJSONObject(i).getString("shopID"));
			localHashMap.put("name", shop.getJSONObject(i).getString("name"));
			
			localList.add(localHashMap);
			
			}
			return localList;
		} catch (Exception ae) {

			return localList;
		}
	}
	
	/**
	 * getDataAndUpdateDB
	 * inventoryCountItem.json
	 */
	
	public JSONObject getDataAndUpdate(String id) {
		
	
		String json_string = "Item.json?";
		JSONObject localJSONObject = null;

		try {

			 localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest1(this.shop_uri,id ,json_string ))
					.toString());
					return localJSONObject;
		} catch (Exception ae) {

			return localJSONObject;
		}
	}
	

	/**
	 * web service for InventoryCount
	 * 
	 * @param id
	 *            account_id
	 * @return list of inventory count
	 */

	public ArrayList<HashMap<String, String>> inventoryCount(String id) {
		ArrayList<HashMap<String, String>> localList = new ArrayList<HashMap<String, String>>();
		String inventory_string = "InventoryCount.json";
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest1(this.shop_uri, id,
							inventory_string)).toString());
			JSONArray localJSONArray = localJSONObject
					.getJSONArray("InventoryCount");
			for (int i = 0; i < localJSONArray.length(); i++) {
				HashMap localHashMap = new HashMap();
				JSONObject jObj = localJSONArray.getJSONObject(i);
				localHashMap.put("inventoryCountID",
						jObj.get("inventoryCountID"));
				localHashMap.put("name", jObj.get("name"));
				localHashMap.put("timeStamp", jObj.get("timeStamp"));
				localHashMap.put("archived", jObj.get("archived"));
				localHashMap.put("shopID", jObj.get("shopID"));

				localList.add(localHashMap);
			}

			return localList;
		} catch (Exception ae) {
			return localList;
		}
	}

	
	/**
	 * web service for posting inventory count item to the store
	 * 
	 * @param id
	 *            account_id
	 * @param employeeID
	 * @param itemID
	 * @param iNVENTORY_COUNT_ID
	 * @param timeStamp
	 * @param qty
	 * @param inventoryCountItemID
	 * @return detail of inventory count items.
	 */


	
	public JSONObject post(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap localHashMap = new HashMap();
		JSONObject localJSONObject = null;
		try {

			 localJSONObject = new JSONObject(Html.fromHtml(

					 this.json.makeHttpRequest_togetItemDetail(local_url+"InsertInventoryCount.php", "POST",
								localArrayList)).toString());

			return localJSONObject;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localJSONObject;

		}

	}
	
	
	
	/**
	 * API for getting inventory count ID
	 */
	
	public JSONObject getinventoryID(String inv_name,String id) {

		JSONObject localJSONObject = null;
		String json = "InventoryCount.json";
		try {
			localJSONObject = new JSONObject(Html.fromHtml(

			this.json.getinventoryCountID(this.shop_uri, inv_name ,id,json )).toString());
			return localJSONObject;
		} catch (Exception ae) {
			return localJSONObject;
		}
	}
	
	/**
	 * get inventory detail to show on verification screen
	 */
	
	public JSONObject getInventoryDetail(String systemSKU, String id) {

		JSONObject localJSONObject = null;
		String json = "Item.json";
		try {
			localJSONObject = new JSONObject(Html.fromHtml(

			this.json.getInventoryDetail(this.shop_uri ,systemSKU, id,json)).toString());
			return localJSONObject;
		} catch (Exception ae) {
			return localJSONObject;
		}
	}
	
	/**
	 * to get transaction
	 */

	public JSONObject get_item_details(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap localHashMap = new HashMap();
		JSONObject localJSONObject = null;
		try {

			 localJSONObject = new JSONObject(Html.fromHtml(

					 this.json.makeHttpRequest_togetItemDetail(local_url+"item_details.php?", "POST",
								localArrayList)).toString());

			return localJSONObject;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localJSONObject;

		}

	}
	
	public JSONObject getImage(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
	
		HashMap localHashMap = new HashMap();
		JSONObject localJSONObject = null;
		try {

			 localJSONObject = new JSONObject(Html.fromHtml(

					 this.json.makeHttpRequest_togetItemDetail(local_url+"get_account_logo.php?", "POST",
								localArrayList)).toString());

			return localJSONObject;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localJSONObject;

		}

	}
	
	public JSONObject lightspeed(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
	
		HashMap localHashMap = new HashMap();
		JSONObject localJSONObject = null;
		try {

			 localJSONObject = new JSONObject(Html.fromHtml(

					 this.json.makeHttpRequest_togetItemDetail(local_url+"lightspeed.php?", "POST",
								localArrayList)).toString());

			return localJSONObject;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localJSONObject;

		}

	}

}
