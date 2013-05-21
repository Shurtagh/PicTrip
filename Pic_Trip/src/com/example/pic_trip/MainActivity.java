package com.example.pic_trip;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
 
public class MainActivity extends Activity {
	private GoogleMap map;
	private static final int SELECT_PHOTO = 1;
	private Intent intent;
	private LatLng currentPoint;
	private LatLng lastPoint = null;
	private int i=2;
	private float[] images;
	
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    Button button = ((Button) findViewById(R.id.addPictures));
	    map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		button.setOnClickListener(new Button.OnClickListener() {  
	        public void onClick(View v) {
	        	intent = new Intent(MainActivity.this, AndroidCustomGalleryActivity.class);
	            startActivityForResult(intent, 100); 
	         }
	    });
	  } 
	  
	  protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
		    super.onActivityResult(requestCode, resultCode, imageReturnedIntent); 
		    map.clear();
		    Bundle bundle = imageReturnedIntent.getExtras();
		    if(bundle!=null) {
		    	images = bundle.getFloatArray("Images");
		    	// tracé normal
		    	/*LatLng lastPoint = null;
		    	boolean first = true;
		    	LatLng firstPoint = null;
		    	for(int i=0;i<images.length-1;i++) {
		    		LatLng currentPoint = new LatLng(images[i], images[++i]);
		    		if(first) {
		    			firstPoint = currentPoint;
		    			first = false;
		    		}
		    		map.addMarker(new MarkerOptions().position(currentPoint).title("Point"));
		    		if(lastPoint != null) {
		    			map.addPolyline(new PolylineOptions().add(lastPoint, currentPoint).width(5).color(Color.RED)).setGeodesic(true);
		    		}
		    		lastPoint = currentPoint;
		    	}
		    	map.moveCamera(CameraUpdateFactory.newLatLngZoom(firstPoint, 10));*/
		    	
		    	//tracé intéractif
		    	currentPoint = new LatLng(images[0], images[1]);
		    	map.addMarker(new MarkerOptions().position(currentPoint).title("Point"));
	    		map.getUiSettings().setScrollGesturesEnabled(false);
	    		map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPoint, 9.0f),3000,MyCancelableCallback);
	    		
		    	}
		    }
	  
		  CancelableCallback MyCancelableCallback =
				   new CancelableCallback(){
	
			  @Override
				public void onCancel() {
					map.getUiSettings().setScrollGesturesEnabled(true);
				}
	
				@Override
				public void onFinish() {
					map.getUiSettings().setAllGesturesEnabled(true);
					if(lastPoint != null) {
						map.addPolyline(new PolylineOptions().add(lastPoint, currentPoint).width(5).color(Color.RED)).setGeodesic(true);
					}
					lastPoint = currentPoint;
					if(i<images.length-1) {
						currentPoint = new LatLng(images[i], images[++i]);
						i++;
				    	map.addMarker(new MarkerOptions().position(currentPoint).title("Point"));
				    	map.getUiSettings().setScrollGesturesEnabled(false);
			    		map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPoint, 9.0f),3000,MyCancelableCallback);
			    	} else {
			    		i=2;
			    		lastPoint = null;
			    	}
				}
				  
			};
		    /*switch(requestCode) { 
		    case SELECT_PHOTO:
		        if(resultCode == RESULT_OK){  
		            Uri selectedImage = imageReturnedIntent.getData();
		            String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.LATITUDE, MediaStore.Images.Media.LONGITUDE};

		            Cursor cursor = getContentResolver().query(
		                               selectedImage, columns, null, null, null);
		            cursor.moveToFirst();

		            int columnIndex = cursor.getColumnIndex(columns[0]);
		            String filePath = cursor.getString(columnIndex);
		            
		            int latitudeIndex = cursor.getColumnIndex(columns[1]);
		            float latitudeToDisplay = cursor.getFloat(latitudeIndex);
		            System.out.println(latitudeToDisplay+"\n");
		            
		            int longitudeIndex = cursor.getColumnIndex(columns[2]);
		            float longitudeToDisplay = cursor.getFloat(longitudeIndex);
		            System.out.println(longitudeToDisplay+"\n");
		            
		            LatLng POINT = new LatLng(latitudeToDisplay, longitudeToDisplay);
		            map.addMarker(new MarkerOptions().position(POINT).title("Point")).showInfoWindow();
		            map.moveCamera(CameraUpdateFactory.newLatLngZoom(POINT, 4));
		            
		            cursor.close();

		            //Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
		        }
		    }*/
}