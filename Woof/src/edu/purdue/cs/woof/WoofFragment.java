package edu.purdue.cs.woof;

import java.io.IOException;

import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.*;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class WoofFragment extends Fragment {
	//Constants
	private final int MAX_TEXT_LENGTH = 250;
	
	//How to contact them
	private Contact contact;
	private Barker barker;
	
	//UI Elements
	private View rootView;
	private Button chooseContactButton;
	private Button sendWoofButton;
	private EditText woofText;
	private TextView notifyText;
	private TextView characterCountText;
	private Spinner quantitySpinner;
	
	//MediaPlayer stuff
	private MediaPlayer mp;
	
	public WoofFragment() {
		contact = new Contact("someone", "", "", "", "");
		barker = new Barker(contact, "");
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
	    quantitySpinner = (Spinner) rootView.findViewById(R.id.quantity_spinner);
	    
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
	            R.array.woof_quantity_list, android.R.layout.simple_spinner_item);
	    // Specify the layout to use when the list of choices appears
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    // Apply the adapter to the spinner
	    quantitySpinner.setAdapter(adapter);
	}
	 
	private void setActionListeners() {		
		final WoofFragment thisFragment = this;
	    chooseContactButton.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.replace(R.id.container, new CreateContactFragment(thisFragment));
				transaction.addToBackStack(null);
				transaction.commit();
			}
	    });
	
	    sendWoofButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (woofText.getText().length() > 0) {
					int count;
					if (quantitySpinner.getSelectedItem().toString().equals("Yip!")) {
						count = 1;
					}
					else if (quantitySpinner.getSelectedItem().toString().equals("Bark!")) {
						count = 2;
					}
					else {
						count = 3;
					}
					mp = new MediaPlayer();
					mp.stop();
					mp.reset();
					
					try {
						SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(thisFragment.getActivity());
						String soundFile = "woof" + sp.getString("woof_sound_option", "1") + ".mp3";
						AssetFileDescriptor afd = getActivity().getAssets().openFd(soundFile);
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
					
					//Send the message
					barker.setContact(contact);
					barker.setMessage(woofText.getText().toString());
					barker.bark(count);

					woofText.setText("");
					notifyText.setText("Successfully Woof'd " + contact.getName() + "!  Send another?");
					
					Log.i("WOOF", "Sending woof to " + contact.getName() + " with number " + contact.getSmsNumber());
				}
			}
		});
	    
	    woofText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				//Intentionally left blank
			}
		
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				setCharacterCount(s.length());
			}
		
			@Override
			public void afterTextChanged(Editable s) {
				//Intentionally left blank
			}
		});
	}
	
	private void setCharacterCount(int length) {
		characterCountText.setText(length + " / " + MAX_TEXT_LENGTH);
	}
	
	public Contact getContact() {
		return contact;
	}
	
	public void setContact(Contact contact) {
		this.contact = contact;
	}
}