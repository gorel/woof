package edu.purdue.cs.woof;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class CreateContactFragment extends Fragment {
	//UI Elements
	private View rootView;
	private EditText name;
	private EditText smsNumber;
	private EditText email;
	private EditText twitterHandle;
	private EditText redditUsername;
	private Button submitButton;
	
	public CreateContactFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_create_contact, container, false);
        
        //Get the selected contact
        selectContact();
        
         //Set up the UI
        getUIElements();
        setActionListeners();
        
        return rootView;
    }
    
    public void selectContact() {
    	final int REQUEST_SELECT_CONTACT = 1;
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_SELECT_CONTACT);
        }
    }
     
     private void getUIElements() {
    	 name = (EditText) rootView.findViewById(R.id.name_edittext);
    	 smsNumber = (EditText) rootView.findViewById(R.id.sms_number_edittext);
    	 email = (EditText) rootView.findViewById(R.id.email_edittext);
    	 twitterHandle = (EditText) rootView.findViewById(R.id.twitter_handle_edittext);
    	 redditUsername = (EditText) rootView.findViewById(R.id.reddit_username_edittext);
    	 submitButton = (Button) rootView.findViewById(R.id.submit_contact_button);
     }
     
     private void setActionListeners() {
    	 submitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//TODO: Add/update contact and return to previous screen with data
			}
		});
     }
}
