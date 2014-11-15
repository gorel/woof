package edu.purdue.cs.woof;

import java.io.IOException;

import android.content.res.AssetFileDescriptor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.*;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class WoofFragment extends Fragment {
	//Constants
	private final int MAX_TEXT_LENGTH = 250;
	private final String WOOF_TABLE_NAME = "woof_contact_list";
	
	//Contact name
	private CharSequence contactName;
	
	//UI Elements
	View rootView;
	Button chooseContactButton;
	Button sendWoofButton;
	EditText woofText;
	TextView notifyText;
	TextView characterCountText;
	
	//Database stuff
	SQLiteDatabase db;
	
	//MediaPlayer stuff
	MediaPlayer mp;
	
	public WoofFragment() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    rootView = inflater.inflate(R.layout.fragment_woof_main, container, false);
		
	    //Set up the UI
	    getUIElements();
	    setActionListeners();
	    
	    return rootView;
	}
	
	private void getUIElements() {
	    chooseContactButton = (Button) rootView.findViewById(R.id.choose_contact_button);
	    sendWoofButton = (Button) rootView.findViewById(R.id.send_woof_button);
	    woofText = (EditText) rootView.findViewById(R.id.woof_message_edittext);
	    notifyText = (TextView) rootView.findViewById(R.id.notify_text);
	    characterCountText = (TextView) rootView.findViewById(R.id.character_count_text);
	}
	 
	private void setActionListeners() {		
	    chooseContactButton.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				//TODO: Get selected contact
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.replace(R.id.container, new CreateContactFragment());
				transaction.addToBackStack(null);
				transaction.commit();
				
				setContactName("Bob Ross");
				notifyText.setText("Send a Woof to " + getContactName() + "!");
			}
	    });
	
	    sendWoofButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (woofText.getText().length() > 0) {
					mp = new MediaPlayer();
					mp.stop();
					mp.reset();
					
					try {
						AssetFileDescriptor afd = getActivity().getAssets().openFd("woof.mp3");
		
			            mp.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
			            mp.prepare();
			            mp.start();
			        }
					catch (IllegalStateException e) {
			            e.printStackTrace();
			        }
					catch (IOException e) {
			            e.printStackTrace();
			        }

					woofText.setText("");
					notifyText.setText("Successfully Woof'd " + getContactName() + "!");
				}
			}
		});
	    
	    woofText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				
			}
		
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				setCharacterCount(s.length());
			}
		
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
	}
	
	private CharSequence getContactName() {
		return contactName;
	}
	
	private void setContactName(String name) {
		contactName = name;
	}
	
	private void setCharacterCount(int length) {
		characterCountText.setText(length + " / " + MAX_TEXT_LENGTH);
	}
	
	public void addContact(Contact contact) {
		db = getActivity().openOrCreateDatabase(
				"woof.db",
				SQLiteDatabase.CREATE_IF_NECESSARY,
				null);
		final String createTableMaybe = "CREATE TABLE IF NOT EXISTS " +
				WOOF_TABLE_NAME + " (" +
				"id INTEGER PRIMARY KEY AUTOINCREMENT" +
				", name TEXT" +
				", sms_number TEXT" +
				", email TEXT" +
				", twitter_handle TEXT" +
				", reddit_username TEXT" +
				");";
		final String insertIntoTable = "INSERT INTO " + WOOF_TABLE_NAME +
				" ('name', 'sms_number', 'email', 'twitter_handle', 'reddit_username') " +
				"VALUES (" +
				contact.getName() + ", " +
				contact.getEmail() + ", " +
				contact.getTwitterHandle() + ", " +
				contact.getRedditUsername() + ");";
		
		db.execSQL(createTableMaybe);
		db.execSQL(insertIntoTable);
	}
	
	public void updateContact(Contact old, Contact updated) {
		//TODO: Allow contact updates
	}
}