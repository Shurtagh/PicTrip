package com.example.pic_trip;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
 
public class MainActivity extends Activity {
	private GoogleMap map;
	private static final int SELECT_PHOTO = 1;
	Intent intent;
	
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    Button button = ((Button) findViewById(R.id.addPictures));
	    map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
	    
	    //map.addPolyline(new PolylineOptions().add(HAMBURG, PARIS).width(5).color(Color.RED));
	    
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
		    	float[] images = bundle.getFloatArray("Images");
		    	for(int i=0;i<images.length-1;i++) {
		    		map.addMarker(new MarkerOptions().position(new LatLng(images[i], images[++i])).title("Point"));
		    	}
		    }
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
}