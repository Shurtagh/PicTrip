package com.example.pic_trip;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
 
public class MainActivity extends Activity implements OnMapLongClickListener {
	private GoogleMap map;
	private Intent intent;
	private LatLng currentPoint;
	private LatLng lastPoint = null;
	private int i=1;
	private float[] images;
	private ArrayList<ObjetImage> result;
	private CancelableCallback MyCancelableCallback = null;
	private Marker markerClicked;
	private ArrayList<Polyline> polylineOnMaps = new ArrayList<Polyline>();
	private ArrayList<Marker> markerOnMaps = new ArrayList<Marker>();
	private String path[] = new String[200];
	private ArrayList<Marker> comMarker = new ArrayList<Marker>();
	
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    Button button = ((Button) findViewById(R.id.addPictures));
	    map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
	    
	    map.setOnMapLongClickListener(MainActivity.this); 
	    
	    map.setOnInfoWindowClickListener(
		  new OnInfoWindowClickListener(){
		    public void onInfoWindowClick(Marker marker){
		    	markerClicked = marker;
		    	createDialog().show();
		    }
		  }
		);
	    
		button.setOnClickListener(new Button.OnClickListener() {  
	        public void onClick(View v) {
	        	intent = new Intent(MainActivity.this, AndroidCustomGalleryActivity.class);
	        	intent.putExtra("pathes", path);
	            startActivityForResult(intent, 100); 
	         }
	    });
	  } 
	  
	  @Override
	  public void onMapLongClick(LatLng point) {
	           Marker m = map.addMarker(new MarkerOptions().position(point).title("Point").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
	           markerClicked = m;
	           createDialogForCommentaire().show();
	      }
	  
	  private AlertDialog.Builder createDialogForCommentaire() {
		//On instancie notre layout en tant que View
	        LayoutInflater factory = LayoutInflater.from(this);
	        final View alertDialogView = factory.inflate(R.layout.popupcommentaire, null);
	 
	        //Création de l'AlertDialog
	        AlertDialog.Builder adb = new AlertDialog.Builder(this);
	 
	        //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
	        adb.setView(alertDialogView);
	 
	        //On donne un titre à l'AlertDialog
	        adb.setTitle("Commentaire");
	 
	        //On modifie l'icône de l'AlertDialog pour le fun ;)
	        adb.setIcon(android.R.drawable.ic_dialog_alert);
	 
	        //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
	        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            	//Lorsque l'on cliquera sur le bouton "OK", on récupère l'EditText correspondant à notre vue personnalisée (cad à alertDialogView)
	            	EditText et = (EditText)alertDialogView.findViewById(R.id.EditText1);
	            	markerClicked.setSnippet(et.getText().toString());
	            	comMarker.add(markerClicked);
	          } });
	        return adb;
	    }
	  
	  private AlertDialog.Builder createDialog() {
		    //On instancie notre layout en tant que View
	        LayoutInflater factory = LayoutInflater.from(this);
	        final View alertDialogView = factory.inflate(R.layout.alertdialogperso, null);
	 
	        //Création de l'AlertDialog
	    	AlertDialog.Builder adb = new AlertDialog.Builder(this);
	 
	        //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
	        adb.setView(alertDialogView);
	 
	        //On donne un titre à l'AlertDialog
	        adb.setTitle("Information");
	        adb.setCancelable(true);
	        
	        adb.setNegativeButton("Supprimer", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            	for(Polyline line : polylineOnMaps) {
	            		line.remove();
	            	}
	            	polylineOnMaps = new ArrayList<Polyline>();
	            	for(int ind=0;ind<path.length;ind++) {
	            		if(path[ind] != null) {
	            			if(path[ind].equals(markerClicked.getSnippet())) {
	            				path[ind] = null;
	            			}
	            		}
	            	}
	            	markerOnMaps.remove(markerClicked);
	            	markerClicked.remove();
	            	updatePolyline();
	          } });
	 
	        adb.setPositiveButton("Voir", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            	Intent intent = new Intent();
	            	intent.setAction(Intent.ACTION_VIEW);
	            	intent.setDataAndType(Uri.parse("file://" + markerClicked.getSnippet()), "image/*");
	            	startActivity(intent);
	            } });
	        
	        return adb;
	  }
	  
	  private void updatePolyline() {
		  int p=0;
		  do {
			  if(p<markerOnMaps.size()-1) {
				  LatLng firstPoint = new LatLng(markerOnMaps.get(p).getPosition().latitude, markerOnMaps.get(p).getPosition().longitude);
				  p++;
				  LatLng secondPoint = new LatLng(markerOnMaps.get(p).getPosition().latitude, markerOnMaps.get(p).getPosition().longitude);
				  Polyline line = map.addPolyline(new PolylineOptions()
	 		     .add(firstPoint,secondPoint)
	 		     .width(4)
	 		     .color(Color.RED));
	 			polylineOnMaps.add(line);
			  } else {
				  p++;
			  }
		  } while(p<markerOnMaps.size());
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
	    		Marker m = map.addMarker(new MarkerOptions().position(currentPoint).title("Point").snippet(result.get(i).getImagePath()));
	    		path[i] = result.get(i).getImagePath();
	    		markerOnMaps.add(m);
	    		if(lastPoint != null) {
	    			Polyline line = map.addPolyline(new PolylineOptions()
	    		     .add(lastPoint, currentPoint)
	    		     .width(4)
	    		     .color(Color.RED));
	    			polylineOnMaps.add(line);
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
					  Polyline line = map.addPolyline(new PolylineOptions()
		    		     .add(lastPoint, currentPoint)
		    		     .width(4)
		    		     .color(Color.RED));
		    			polylineOnMaps.add(line);
				  }
				  lastPoint = currentPoint;
				  if(i<result.size()) {
					  currentPoint = new LatLng(result.get(i).getLatitude(), result.get(i).getLongitude());
					  Marker m = map.addMarker(new MarkerOptions().position(currentPoint).title("Point").snippet(result.get(i).getImagePath()));
					  path[i] = result.get(i).getImagePath();
					  markerOnMaps.add(m);
					  m.showInfoWindow();
					  i++;
					  map.getUiSettings().setScrollGesturesEnabled(false);
					  map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPoint, 9.0f),4000,MyCancelableCallback);
				  } else {
					  i=1;
					  lastPoint = null;
				  }
			  }
			  
		  };
		  	
		  	currentPoint = new LatLng(result.get(0).getLatitude(), result.get(0).getLongitude());
	    	Marker m = map.addMarker(new MarkerOptions().position(currentPoint).title("Point").snippet(result.get(0).getImagePath()));
	    	markerOnMaps.add(m);
			m.showInfoWindow();
	    	map.getUiSettings().setScrollGesturesEnabled(false);
	    	map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPoint, 9.0f),4000,MyCancelableCallback);
	    	//map.setOnInfoWindowClickListener(this);
	    	
	    	
	  }
	  
	  protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
		    super.onActivityResult(requestCode, resultCode, imageReturnedIntent); 
		    Bundle bundle = imageReturnedIntent.getExtras();
		    if(bundle!=null) {
		    	map.clear();
	    		map.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
		    	result = (ArrayList<ObjetImage>)imageReturnedIntent.getSerializableExtra("result");
		    	polylineOnMaps = new ArrayList<Polyline>();
		    	markerOnMaps = new ArrayList<Marker>();
		    	path = new String[200];
		    	
		    	for(Marker m : comMarker) {
		    		map.addMarker(new MarkerOptions()
		            .position(new LatLng(m.getPosition().latitude, m.getPosition().longitude))
		            .title(m.getTitle())
		            .snippet(m.getSnippet())
		            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
		    	}
		    	
		    	if(result.size()>0) {
		    		if(imageReturnedIntent.getExtras().getBoolean("interactif")) {
		    			traceInteractif();
		    		} else {
		    			traceNormal();
		    		}
		    	}
		    }
      }
}