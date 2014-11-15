package edu.purdue.cs.woof;

import android.support.v7.app.*;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;

public class Woof extends ActionBarActivity {	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_woof);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new WoofFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.woof, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
        	//Intent intent = new Intent(this, WoofPreferences.class);
        	//startActivity(intent);
        	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=ytc9-wGCHW0"));
        	startActivity(browserIntent);
        	return true;
        }
        return super.onOptionsItemSelected(item);
    }
}