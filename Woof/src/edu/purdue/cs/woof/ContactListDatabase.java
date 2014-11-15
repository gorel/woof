package edu.purdue.cs.woof;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactListDatabase extends SQLiteOpenHelper {
	private final static int DATABASE_VERSION = 2;
	private final static String DATABASE_NAME = "woof_db";
	private final static String CONTACT_LIST_TABLE_NAME = "contact_list";
	private final static String CONTACT_LIST_CREATE = "CREATE TABLE " +
			CONTACT_LIST_TABLE_NAME + " (" +
			"name TEXT" +
			", sms_number TEXT" +
			", email TEXT" +
			", twitter_handle TEXT" +
			", reddit_username TEXT" +
			");";			
	
	public ContactListDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CONTACT_LIST_CREATE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// IDK what to do here
	}
}
