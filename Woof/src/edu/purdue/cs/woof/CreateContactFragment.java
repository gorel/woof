package edu.purdue.cs.woof;

import android.support.v4.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class CreateContactFragment extends Fragment {
	//Database constants
	private final int NAME_COLUMN = 1;
	private final int SMS_NUMBER_COLUMN = 2;
	private final int EMAIL_COLUMN = 3;
	private final int TWITTER_HANDLE_COLUMN = 4;
	private final int REDDIT_USERNAME_COLUMN = 5;
	
	//Constants
	private final String DATABASE_NAME = "woof.db";
	private final String WOOF_TABLE_NAME = "woof_contact_list";
	private final String createTableMaybe = "CREATE TABLE IF NOT EXISTS " +
		WOOF_TABLE_NAME + " (" +
		"id INTEGER PRIMARY KEY AUTOINCREMENT" +
		", name TEXT" +
		", sms_number TEXT" +
		", email TEXT" +
		", twitter_handle TEXT" +
		", reddit_username TEXT" +
		");";
	
	//UI Elements
	private View rootView;
	private EditText nameField;
	private EditText smsNumberField;
	private EditText emailField;
	private EditText twitterHandleField;
	private EditText redditUsernameField;
	private Button submitButton;
	private Button loadContactButton;

	//Database stuff
	private SQLiteDatabase db;
	
	//Contact list results stuff
	private Contact contact;
	private String contactName;
	private String smsNumber;
	
	//WoofFragment to callback data
	private WoofFragment parent;
	
	public CreateContactFragment() {
		contactName = "";
		smsNumber = "";
    }
	
	public CreateContactFragment(WoofFragment parent) {
		this.parent = parent;
		this.contact = parent.getContact();
		contactName = parent.getContact().getName();
		smsNumber = "";
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	//Set up the UI
        rootView = inflater.inflate(R.layout.fragment_create_contact, container, false);
    	getUIElements();
    	setActionListeners();
        
        return rootView;
    }
    
    public void selectContact() {
    	/*
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_SELECT_CONTACT);
        }
        */
        readContact();
    }
     
     private void getUIElements() {
    	 nameField = (EditText) rootView.findViewById(R.id.name_edittext);
    	 smsNumberField = (EditText) rootView.findViewById(R.id.sms_number_edittext);
    	 emailField = (EditText) rootView.findViewById(R.id.email_edittext);
    	 twitterHandleField = (EditText) rootView.findViewById(R.id.twitter_handle_edittext);
    	 redditUsernameField = (EditText) rootView.findViewById(R.id.reddit_username_edittext);
    	 submitButton = (Button) rootView.findViewById(R.id.submit_contact_button);
    	 loadContactButton = (Button) rootView.findViewById(R.id.load_contact_button);
    	 
    	 if (contact != null) {
    		 nameField.setText(contact.getName());
    		 smsNumberField.setText(contact.getSmsNumber());
    		 emailField.setText(contact.getEmail());
    		 twitterHandleField.setText(contact.getTwitterHandle());
    	 }
     }
     
     private void setActionListeners() {
    	 submitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
		        //Fill in the contact information
		        setContactInformation();
		    	getFragmentManager().popBackStack();
			}
		});
    	 
    	 loadContactButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
		    	//Get the selected contact
		        selectContact();
			}
		});
     }
     
     private void setContactInformation() {
    	 String name = nameField.getText().toString().trim();
    	 String smsNumber = smsNumberField.getText().toString().trim();
    	 String email = emailField.getText().toString().trim();
    	 String twitterHandle = twitterHandleField.getText().toString().trim();
    	 String redditUsername = redditUsernameField.getText().toString().trim();
    	 Contact contact = new Contact(name, smsNumber, email, twitterHandle, redditUsername);
    	 addOrUpdateContact(contact);
    	 parent.setContact(contact);
     }

 	public void addOrUpdateContact(Contact contact) {
 		db = getActivity().openOrCreateDatabase(
 				DATABASE_NAME,
 				SQLiteDatabase.CREATE_IF_NECESSARY,
 				null);
 		final String deleteOldEntryIfExists = "DELETE FROM " + WOOF_TABLE_NAME +
 				" WHERE name LIKE '" + contact.getName() + "'";
 		if (contact.getName().length() > 0) {
	 		final String insertIntoTable = "INSERT INTO " + WOOF_TABLE_NAME +
	 				" ('name', 'sms_number', 'email', 'twitter_handle', 'reddit_username') " +
	 				"VALUES ('" +
	 				contact.getName() + "', '" +
	 				contact.getSmsNumber() + "', '" +
	 				contact.getEmail() + "', '" +
	 				contact.getTwitterHandle() + "', '" +
	 				contact.getRedditUsername() + "');";
	 		
	 		db.execSQL(createTableMaybe);
	 		db.execSQL(deleteOldEntryIfExists);
	 		db.execSQL(insertIntoTable);
	 		Log.i("Database", "Added " + contact.getName() + " to the database");
 		}
 		
 		db.close();
 	}
 	
 	private void readContact() { 		
 		db = getActivity().openOrCreateDatabase(
				DATABASE_NAME,
				SQLiteDatabase.CREATE_IF_NECESSARY,
				null);
 		db.execSQL(createTableMaybe);
 		Cursor cursor = db.rawQuery(
 				"SELECT * FROM " + WOOF_TABLE_NAME +
 				" WHERE name LIKE '%" + contactName.trim() + "%'",
 				null);
		nameField.setText(contactName);
		smsNumberField.setText(smsNumber);
		
 		if (cursor.getCount() > 0) {
 			cursor.moveToFirst();
 			nameField.setText(cursor.getString(NAME_COLUMN));
 			smsNumberField.setText(cursor.getString(SMS_NUMBER_COLUMN));
 			emailField.setText(cursor.getString(EMAIL_COLUMN));
 			twitterHandleField.setText(cursor.getString(TWITTER_HANDLE_COLUMN));
 			redditUsernameField.setText(cursor.getString(REDDIT_USERNAME_COLUMN));
 		}
 	}
}
