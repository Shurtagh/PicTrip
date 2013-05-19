package com.example.pic_trip;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
 
public class MainActivity extends Activity {
	private GoogleMap map;
	static final LatLng HAMBURG = new LatLng(53.558, 9.927);
	static final LatLng PARIS = new LatLng(48.85661, 2.35222);
	
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
	    map.addMarker(new MarkerOptions().position(HAMBURG).title("Hamburg")).showInfoWindow();
	    map.addMarker(new MarkerOptions().position(PARIS).title("Paris")).showInfoWindow();
	    map.addPolyline(new PolylineOptions()
	     .add(HAMBURG, PARIS)
	     .width(5)
	     .color(Color.RED));
	    map.moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, 4));
	  }
}