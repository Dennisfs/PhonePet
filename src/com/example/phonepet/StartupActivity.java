package com.example.phonepet;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;

public class StartupActivity extends Activity {
	boolean checkForPreviousPet(){return true;};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_startup);
		
		String fileName = "preferences";
		Intent intent;
		
		// Check if a pet exists on the user's phone
		SharedPreferences sharedPref = getSharedPreferences(fileName, Context.MODE_PRIVATE);
		
		// Clear pet everytime for development purposes.
		SharedPreferences.Editor editor = sharedPref.edit();
		// Theoretically there should be no saved information at this point. Clear for development purposes.
		editor.clear();
		editor.commit();
		
		Boolean petExists = sharedPref.getBoolean("pet_exists", false);
		Log.v("petexists", Boolean.toString(petExists));
		
		if (petExists) {
			// Pet exists, go Home
			intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
			
		}
		else {
			// User does not own a pet, allow them to create one.	
			intent = new Intent(this, CreateActivity.class);
			startActivity(intent);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.startup, menu);
		return true;
	}

}
