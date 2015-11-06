package com.example.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.database.*;

public class ItemDatabaseHandler extends SQLiteOpenHelper {
	// Database Version
	private static final int DATABASE_VERSION = 1;
	// Database Name
	public static final String DATABASE_NAME = "item_db";

	//  table name(table name inside database)
	public static final String SHOP_TABLE = "item_info";

	/**
	 * Column names in SHOP_TABLE
	 * 	
	
	KEY_ACCOUNT_ID 
	KEY_QUANTITY
	KEY_INV_COUNT_ID
	KEY_ITEM_ID
	KEY_EMPLOYEE_ID


	 */
	
	// column names
	public static final String KEY_ACCOUNT_ID = "account_id";
	private static final String KEY_QUANTITY = "quantity";
	private static final String KEY_INV_COUNT_ID = "inv_count_id";
	private static final String KEY_ITEM_ID = "item_id";
	private static final String KEY_EMPLOYEE_ID = "employee_id";
	private static final String KEY_DESCRIPTION = "description";
	

	public ItemDatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * called when database is created.
	 */
	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_FAV_TABLE = "CREATE TABLE " + SHOP_TABLE + "(" + KEY_ACCOUNT_ID
				+ " VARCHAR," + KEY_QUANTITY + " VARCHAR,"
				+ KEY_INV_COUNT_ID + " VARCHAR," + KEY_ITEM_ID + " VARCHAR,"
				+ KEY_EMPLOYEE_ID + " VARCHAR," + KEY_DESCRIPTION + " VARCHAR" + ")";
		db.execSQL(CREATE_FAV_TABLE);

	}
	
	 public void dropTable() {
		    SQLiteDatabase db = getWritableDatabase();
		    String sql = "drop table " + SHOP_TABLE;
		    try {
		      db.execSQL(sql);
		     // setTitle(sql);
		    } catch (SQLException e) {
		      //setTitle("exception");
		    	e.printStackTrace();
		    }
		  }
	 public void CreateTable() {
		    SQLiteDatabase db = getWritableDatabase();
		    String sql = "CREATE TABLE " + SHOP_TABLE + "(" + KEY_ACCOUNT_ID
					+ " VARCHAR," + KEY_QUANTITY + " VARCHAR,"
					+ KEY_INV_COUNT_ID + " VARCHAR," + KEY_ITEM_ID + " VARCHAR,"
					+ KEY_EMPLOYEE_ID + " VARCHAR," + KEY_DESCRIPTION + " VARCHAR" + ")";
		    Log.i("createDB=", sql);

		    try {
		      db.execSQL("DROP TABLE IF EXISTS diary");
		      db.execSQL(sql);
		      //setTitle("drop");
		    } catch (SQLException e) {
		      //setTitle("exception");
		    }
		  }

	/**
	 * This method is called when database is upgraded
	 */
	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + SHOP_TABLE);

		// Create tables again
		onCreate(db);

	}
	public void dropTable(SQLiteDatabase db){
	db.execSQL("DROP TABLE IF EXISTS " + SHOP_TABLE);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	public// Adding new contact
	void addContact(ItemDetail itemDetail) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ACCOUNT_ID, itemDetail.getAccount_id());
		values.put(KEY_QUANTITY, itemDetail.getQuantity());
		values.put(KEY_INV_COUNT_ID, itemDetail.getInventory_count_id());
		values.put(KEY_ITEM_ID, itemDetail.getItem_id());
		values.put(KEY_EMPLOYEE_ID, itemDetail.getEmployee_id());
		values.put(KEY_DESCRIPTION, itemDetail.getDescription());
		
		// Inserting Row
		db.insert(SHOP_TABLE, null, values);
		db.close(); // Closing database connection
	}

	
	// Deleting single contact
	public void deleteContact(String name) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(SHOP_TABLE, KEY_ITEM_ID + " = ?", new String[] { name });
		db.close();
	}
	


	// Getting contact on the basis of shop id
	public Boolean getContact_comp(String subcat_name) {

		Boolean isExist;
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(SHOP_TABLE, new String[] { KEY_ACCOUNT_ID,
				KEY_QUANTITY, KEY_INV_COUNT_ID, KEY_ITEM_ID, KEY_EMPLOYEE_ID , KEY_DESCRIPTION }, KEY_ITEM_ID + "=?",
				new String[] { subcat_name }, null, null, null, null);

		if (cursor.moveToFirst()) {
			isExist = true;
		} else {
			isExist = false;
		}

		return isExist;
	}

	// Getting single contact
	ItemDetail getContact(String name) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(SHOP_TABLE, new String[] { KEY_ACCOUNT_ID,
				KEY_QUANTITY, KEY_INV_COUNT_ID, KEY_ITEM_ID, KEY_EMPLOYEE_ID , KEY_DESCRIPTION }, KEY_ITEM_ID + "=?",
				new String[] { name }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		ItemDetail contact = new ItemDetail(cursor.getString(0),
				cursor.getString(1), cursor.getString(2), cursor.getString(3),
				cursor.getString(4), cursor.getString(5));
		// return contact
		return contact;
	}

	// Getting All Contacts
	public List<ItemDetail> getAllContacts() {
		List<ItemDetail> contactList = new ArrayList<ItemDetail>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + SHOP_TABLE;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
	// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				ItemDetail contact = new ItemDetail();
				contact.setAccount_id(cursor.getString(0));
				contact.setQuantity(cursor.getString(1));
				contact.setInventory_count_id(cursor.getString(2));
				contact.setItem_id(cursor.getString(3));
				contact.setEmployee_id(cursor.getString(4));
				contact.setDescription(cursor.getString(5));
				
				// Adding contact to list
				contactList.add(contact);
			} while (cursor.moveToNext());
		}

		// return contact list
		return contactList;
	}

	// Updating single contact on the basis of item id
	public int updateContact(ItemDetail contact) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ACCOUNT_ID, contact.getAccount_id());
		values.put(KEY_QUANTITY, contact.getQuantity());
		values.put(KEY_INV_COUNT_ID, contact.getInventory_count_id());
		values.put(KEY_ITEM_ID, contact.getItem_id());
		values.put(KEY_EMPLOYEE_ID, contact.getEmployee_id());
		values.put(KEY_DESCRIPTION, contact.getDescription());
		
		// updating row on the basis of item id
		return db.update(SHOP_TABLE, values, KEY_ITEM_ID + " = ?",
				new String[] { String.valueOf(contact.getItem_id()) });
	}

	// Getting contacts Count
	public int getContactsCount() {
		String countQuery = "SELECT  * FROM " + SHOP_TABLE;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

	

}
