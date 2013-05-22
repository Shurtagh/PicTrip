package com.example.pic_trip;

import java.util.ArrayList;

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
	private int i=1;
	private float[] images;
	private ArrayList<ObjetImage> result;
	private CancelableCallback MyCancelableCallback = null;
	
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
	  
	  private void traceNormal() {
		  LatLng lastPoint = null;
	    	boolean first = true;
	    	LatLng firstPoint = null;
	    	for(int i=0;i<result.size();i++) {
	    		LatLng currentPoint = new LatLng(result.get(i).getLatitude(), result.get(i).getLongitude());
	    		if(first) {
	    			firstPoint = currentPoint;
	    			first = false;
	    		}
	    		map.addMarker(new MarkerOptions().position(currentPoint).title("Point").snippet(result.get(i).getImagePath()));
	    		if(lastPoint != null) {
	    			map.addPolyline(new PolylineOptions().add(lastPoint, currentPoint).width(5).color(Color.RED));
	    		}
	    		lastPoint = currentPoint;
	    	}
	    	map.moveCamera(CameraUpdateFactory.newLatLngZoom(firstPoint, 10));
	  }
	  
	  private void traceInteractif() {
		  MyCancelableCallback =
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
				  if(i<result.size()) {
					  currentPoint = new LatLng(result.get(i).getLatitude(), result.get(i).getLongitude());
					  map.addMarker(new MarkerOptions().position(currentPoint).title("Point").snippet(result.get(i).getImagePath()));
					  i++;
					  map.getUiSettings().setScrollGesturesEnabled(false);
					  map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPoint, 9.0f),3000,MyCancelableCallback);
				  } else {
					  i=1;
					  lastPoint = null;
				  }
			  }
			  
		  };
		  	
		  	currentPoint = new LatLng(result.get(0).getLatitude(), result.get(0).getLongitude());
	    	map.addMarker(new MarkerOptions().position(currentPoint).title("Point").snippet(result.get(0).getImagePath()));
	    	map.getUiSettings().setScrollGesturesEnabled(false);
	    	map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPoint, 9.0f),3000,MyCancelableCallback);
	    	//map.setOnInfoWindowClickListener(this);
	    	
	    	
	  }
	  
	  protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
		    super.onActivityResult(requestCode, resultCode, imageReturnedIntent); 
		    map.clear();
		    Bundle bundle = imageReturnedIntent.getExtras();
		    if(bundle!=null) {
		    	//images = bundle.getFloatArray("Images");
		    	result = (ArrayList<ObjetImage>)imageReturnedIntent.getSerializableExtra("result");
		    	
		    	// tracé normal
		    	//traceNormal();
		    	
		    	//tracé intéractif
		    	traceInteractif();
	    		
		    	}
		    	map.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
		    }
	  
		  /*
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