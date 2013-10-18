package com.example.phonepet;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.MotionEventCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;

public class HomeActivity extends Activity {

	private static final int INVALID_POINTER_ID = 0;

	public HomeActivity() {
		//make a soccer ball!
		//comment made by joey
	}
	
	ImageButton playButton;
	ImageButton accessorizeButton;
	ImageButton poopButton;
	ImageButton feedButton;
	ImageButton cleanButton;
	
	Pet pet; // Pet
	String fileName = "preferences";
	private HomeView hView;
	
	// Window parameters
	int windowWidth;
	int windowHeight;
	static int playgroundHeight;
	static int playgroundWidth;
	int menuHeight;
	
	// The �active pointer� is the one currently moving our object.
	private int mActivePointerId = INVALID_POINTER_ID;
	float mLastTouchX = 0;
	float mLastTouchY = 0;
	float mPosX = 0;
	float mPosY = 0;
	boolean petIsClicked = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		playButton = (ImageButton)findViewById(R.id.Play);
		accessorizeButton = (ImageButton)findViewById(R.id.Accessorize);
		poopButton = (ImageButton)findViewById(R.id.Poop);
		feedButton = (ImageButton)findViewById(R.id.Feed);
		cleanButton = (ImageButton)findViewById(R.id.Sponge);
		
		final Intent playIntent = new Intent(this, PlayActivity.class);
		playButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(playIntent);
				
			}
		});
		
		final Intent accessIntent = new Intent(this, AccessorizeActivity.class);
		accessorizeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(accessIntent);
				
			}
		});
		
		
		pet = new Pet();
		
		// Get metrics of display
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		// Set window metrics
		windowWidth = metrics.widthPixels;
		windowHeight = metrics.heightPixels;
		// Set menu height
		menuHeight = windowHeight / 4;
		// Set playground metrics (where the pet can roam).
		playgroundWidth = windowWidth;
		playgroundHeight = (menuHeight * 3);
		
		// Retrieve home view
		final HomeView homeview = (HomeView)findViewById(R.id.HomeView);
		this.hView = homeview;
		
		// Get pet information.
		SharedPreferences sharedPref = getSharedPreferences(fileName, Context.MODE_PRIVATE);
		// Load pet coordinates from previous app state.
		pet.xCoordinate = sharedPref.getFloat("petX", windowWidth/2);
		pet.yCoordinate = sharedPref.getFloat("petY", windowHeight/2);
		// Set pet resolution.
		pet.width = windowWidth/4;
		pet.height = windowHeight/6;
		
		
		// Draw the pet on the screen.
		this.hView.drawPet(pet.xCoordinate, pet.yCoordinate);
		
		homeview.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				
			final int action = MotionEventCompat.getActionMasked(event); 
				
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					
					final int pointerIndex = MotionEventCompat.getActionIndex(event); 
			        final float userX = MotionEventCompat.getX(event, pointerIndex); 
			        final float userY = MotionEventCompat.getY(event, pointerIndex); 
			            
			        // Check if something is clicked.
			        isPetClicked(userX, userY);
			        Log.v("petIsClicked, down", Boolean.toString(petIsClicked));
			        
			        // Remember where we started (for dragging)
			        mLastTouchX = userX;
			        mLastTouchY = userY;
			        
			        // Save the ID of this pointer (for dragging)
			        mActivePointerId = MotionEventCompat.getPointerId(event, 0);
			        break;
					
				} // End case MotionEvent.ACTION_DOWN
				
				case MotionEvent.ACTION_MOVE: {
			        // Find the index of the active pointer and fetch its position
			        final int pointerIndex = 
			                MotionEventCompat.findPointerIndex(event, mActivePointerId);  
			            
			        final float userX = MotionEventCompat.getX(event, pointerIndex);
			        final float userY = MotionEventCompat.getY(event, pointerIndex);
			            
			        // Calculate the distance moved
			        final float dx = userX - mLastTouchX;
			        final float dy = userY - mLastTouchY;

			        mPosX += dx;
			        mPosY += dy;
			        
			        // Invalidate
			        if (petIsClicked)
			        {
			        	if((userX > 0) && (userX < (windowWidth - pet.width)) 
			        			&& (userY > 0) && (userY < (windowHeight - pet.height - menuHeight))) 
			        	{
			        		homeview.dragPet(userX, userY);
			        		pet.xCoordinate = userX;
			        		pet.yCoordinate = userY;
			        	}
			        }
			        
			        // Remember this touch position for the next move event
			        mLastTouchX = userX;
			        mLastTouchY = userY;

			        break;
			    }
				
				case MotionEvent.ACTION_UP: {
			        mActivePointerId = INVALID_POINTER_ID;
			        break;
			    }
			            
			    case MotionEvent.ACTION_CANCEL: {
			        mActivePointerId = INVALID_POINTER_ID;
			        break;
			    }
			        
			    case MotionEvent.ACTION_POINTER_UP: {
			            
			        final int pointerIndex = MotionEventCompat.getActionIndex(event); 
			        final int pointerId = MotionEventCompat.getPointerId(event, pointerIndex); 

			        if (pointerId == mActivePointerId) {
			            // This was our active pointer going up. Choose a new
			            // active pointer and adjust accordingly.
			            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
			            mLastTouchX = MotionEventCompat.getX(event, newPointerIndex); 
			            mLastTouchY = MotionEventCompat.getY(event, newPointerIndex); 
			            mActivePointerId = MotionEventCompat.getPointerId(event, newPointerIndex);
			        }
			        break;
			    }
					
				} // End switch
				return true;
				
			} // End function onTouch
		
		}); // Set onTouchListener
			
	}

	@Override
	protected void onPause(){
		super.onPause();
		
		// Save data to file
		savePetLocation();
		
	}// End method onPause
	
	
	private void savePetLocation() {
				
		// Get the preferences file and create the editor
		SharedPreferences sharedPref = getSharedPreferences(fileName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		
		// Pet has been created.
		editor.putFloat("petX", pet.xCoordinate);
		editor.putFloat("petY", pet.yCoordinate);
		editor.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	public void isPetClicked(float userX, float userY) {
		
		// Is click within pet image X-range
		if ((userX >= pet.xCoordinate)
				&& (userX <= pet.xCoordinate + pet.width)) 
		{
		    // Is click within pet image Y-range
			if ((userY >= pet.yCoordinate) 
					&& (userY <= pet.yCoordinate + pet.height))
			{
				// Pet has been selected
				petIsClicked = true;
				return;
			}
		}
		
		// Pet not clicked
		petIsClicked = false;
		
	} // End method isPetClicked

} // End class HomeActivity
