package com.example.pic_trip;

import android.os.Bundle;

import com.google.android.maps.MapActivity;
 
public class MainActivity extends MapActivity {
 
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    //d�fini le vue XML utilis�e
	    setContentView(R.layout.activity_main);
	  }
	 
	  @Override
	  protected boolean isRouteDisplayed() {
	    return true;
	  }
	 
	  @Override
	  protected boolean isLocationDisplayed() {
	    return true;
	  }
}