package com.example.pic_trip;

import com.google.android.maps.MapActivity;
import android.content.Context;
import android.os.Bundle;
 
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