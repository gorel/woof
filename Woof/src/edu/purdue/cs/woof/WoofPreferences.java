package edu.purdue.cs.woof;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class WoofPreferences extends PreferenceActivity {
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
}