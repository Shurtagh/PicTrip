package com.example.pic_trip;

import java.util.ArrayList;
import java.util.Collections;

import DAO.PointDAO;
import ElementObject.Point;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
	protected static final int ACTION_GET_VIDEO = 5;
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
	ArrayList<ObjetImage> imagesOnTheMap = new ArrayList<ObjetImage>(); 
	private ArrayList<Marker> comMarker = new ArrayList<Marker>();
	private String snippet;
	private EditText et;
	private int tripId = -1;
	private PointDAO pointDAO;
	private Builder adb;
	private LatLng globalPoint;
	
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    Menu.context = getApplicationContext();
	    pointDAO = new PointDAO(Menu.getContext());
	    
	    result = new ArrayList<ObjetImage>();
	    
	    Intent int_ = getIntent();
	    tripId = int_.getIntExtra("id", -1);
	    
	    setContentView(R.layout.activity_main);
	    Button add_photos = ((Button) findViewById(R.id.addPictures));
	    Button diaporama = ((Button) findViewById(R.id.diaporama));
	    Button enregistrer = ((Button) findViewById(R.id.enregistrer));
	    
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
	    
	    //pointDAO.deleteAll();
	    
	    //ArrayList<Point> test = pointDAO.getAll();
	    //for(Point p : test) {
	    	//System.out.println(p.getTravel_id() + " " + p.getType_id() + " " + p.getLatitude() + " " + p.getLongitude() + " " + p.getDate_add());
	    //}
	    
	    if(tripId != -1) {
	    	ArrayList<Point> points = pointDAO.getByTravelId(tripId);
	    	System.out.println("YYYYYYYY" + points.size());
	    	if(points != null) {
	    		for(Point p : points) {
	    			if(p.getType_id() == 1 || p.getType_id() == 3) {
	    				result.add(new ObjetImage(p.getLatitude(), p.getLongitude(), p.getUri(), p.getComment(), p.getDate_add(), p.getType_id()));
	    			} 
	    			else if(p.getType_id() == 2) {
	    				Marker m = map.addMarker(new MarkerOptions().position(new LatLng(p.getLatitude(),p.getLongitude())).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
	    				if(p.getComment() != null) {
	    					m.setSnippet(p.getComment());
	    				}
	    				comMarker.add(m);
	    			}
	    		}
	    	}
	    	if(result.size() > 0) {
	    		traceNormal();
	    	}
	    }
	    
	    adb = new AlertDialog.Builder(this)
		.setTitle("Information")
		.setMessage("Données enregistrées")
		.setPositiveButton("Ok", null);
	    
	    enregistrer.setOnClickListener(new Button.OnClickListener() {  
	        public void onClick(View v) {
	        	pointDAO.deleteAllPointOfTravelExceptTracking(tripId);
	        	long date = 0;
	        	for(Marker m : markerOnMaps) {
	        		for(ObjetImage image : result) {
	        			if(image.getImagePath() != null) {
	        				if(image.getImagePath().equals(m.getTitle())) {
	        					date = image.getDate();
	        				}
	        			}
	        		}
	        		Point p = new Point(tripId,1,date,(float)m.getPosition().latitude,(float)m.getPosition().longitude,m.getSnippet(),m.getTitle(),0);
	        		p.save();
	        	}
	        	for(Marker m : comMarker) {
	        		Point p = new Point(tripId,2,123,(float)m.getPosition().latitude,(float)m.getPosition().longitude,m.getSnippet(),m.getTitle(),0);
	        		p.save();
	        	}
	    		adb.show();
	        }
	    });
	    
	    diaporama.setOnClickListener(new Button.OnClickListener() {  
	        public void onClick(View v) {
	        	//Intent diapo = new Intent(MainActivity.this, SwipeActivity.class);
	            //startActivityForResult(diapo, 100); 
	        	//stopActivity(diapo);
	         }
	    });
	    
		add_photos.setOnClickListener(new Button.OnClickListener() {  
	        public void onClick(View v) {
	        	intent = new Intent(MainActivity.this, AndroidCustomGalleryActivity.class);
	        	intent.putExtra("pathes", imagesOnTheMap);
	            startActivityForResult(intent, 100); 
	         }
	    });
	  } 
	  
	  @Override
	  public void onMapLongClick(LatLng point) {
		  		globalPoint = point;
		  		choosePointToAdd().show();
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

	        et = (EditText)alertDialogView.findViewById(R.id.EditText1);
	        if(snippet != null) {
	        	et.setText(snippet);
	        	snippet = null;
	        }
	 
	        //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
	        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            	markerClicked.setSnippet(et.getText().toString());
	            	for(int k=0;k<imagesOnTheMap.size();k++) {
                    	if(imagesOnTheMap.get(k).getImagePath() != null) {
                    		if(imagesOnTheMap.get(k).getImagePath().equals(markerClicked.getTitle())) {
                    			imagesOnTheMap.get(k).setSnippet(et.getText().toString());
                			}
                    	}
                    }
        			markerClicked.hideInfoWindow();
        			markerClicked.showInfoWindow();
        			if(!comMarker.contains(markerClicked) && markerClicked.getTitle() == null) {
        				comMarker.add(markerClicked);
        			}
	          } });
	        return adb;
	    }
	  
	  private AlertDialog.Builder choosePointToAdd() {
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
	        
	        adb.setNegativeButton("Ajouter commentaire", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	               Marker m = map.addMarker(new MarkerOptions().position(globalPoint).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
	 	           markerClicked = m;
	 	           createDialogForCommentaire().show();
	            } 
	       });
	        
	        adb.setPositiveButton("Ajouter vidéo", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            	Marker m = map.addMarker(new MarkerOptions().position(globalPoint).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).snippet("Vidéo"));
		 	           markerClicked = m;
		 	          comMarker.add(m);
	            	Intent mediaChooser = new Intent(Intent.ACTION_PICK,
	            			android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
	            	mediaChooser.setType("video/*");
	            	startActivityForResult(mediaChooser, ACTION_GET_VIDEO);
	            } 
	       });
	        
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
	            	
	            	for(int ind=0;ind<imagesOnTheMap.size();ind++) {
	            		if(imagesOnTheMap.get(i).getImagePath() != null) {
	            			if(imagesOnTheMap.get(i).getImagePath().equals(markerClicked.getTitle())) {
	            				imagesOnTheMap.remove(i);
	            			}
	            		}
	            	}
	            	
	            	if(markerOnMaps.contains(markerClicked)) {
	            		markerOnMaps.remove(markerClicked);
	            	} else if (comMarker.contains(markerClicked)) {
	            		comMarker.remove(markerClicked);
	            	}
	            	markerClicked.remove();
	            	updatePolyline();
	            } 
	       });
	        
	        if(markerClicked.getTitle() != null) {
	        	 if(markerClicked.getTitle().toString().startsWith("/")) {
	 	        	adb.setPositiveButton("Voir", new DialogInterface.OnClickListener() {
	 	        		public void onClick(DialogInterface dialog, int which) {
	 	        			Intent intent = new Intent();
	 	        			intent.setAction(Intent.ACTION_VIEW);
	 	        			if(markerClicked.getTitle().toString().contains("VID")) {
	 	        				intent.setDataAndType(Uri.parse("file://" + markerClicked.getTitle()), "video/*");
	 	        			} else {
	 	        				intent.setDataAndType(Uri.parse("file://" + markerClicked.getTitle()), "image/*");
	 	        			}
	 	        			startActivity(intent);
	 	        		} 
	 	        	});
	 	        }
	        }
        	adb.setNeutralButton("Modifier", new DialogInterface.OnClickListener() {
        		public void onClick(DialogInterface dialog, int which) {
        			if(markerClicked.getSnippet() != null) {
        				snippet = markerClicked.getSnippet().toString();
        			}
        			createDialogForCommentaire().show();
        		} 
        	});
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
		  map.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
		  LatLng lastPoint = null;
	    	boolean first = true;
	    	LatLng firstPoint = null;
	    	for(int i=0;i<result.size();i++) {
	    		LatLng currentPoint = new LatLng(result.get(i).getLatitude(), result.get(i).getLongitude());
	    		if(first) {
	    			firstPoint = currentPoint;
	    			first = false;
	    		}
	    		Marker m = null;
	    		if(result.get(i).getType() == 1) {
	    			m = map.addMarker(new MarkerOptions().position(currentPoint).title(result.get(i).getImagePath()).snippet(result.get(i).getSnippet()));
	    		}
	    		else if(result.get(i).getType() == 3) {
	    			m = map.addMarker(new MarkerOptions().position(currentPoint).title(result.get(i).getImagePath()).snippet(result.get(i).getSnippet()));
	    			m.setVisible(false);
	    		}

	    		if(result.get(i).getType() == 1) {
	    			imagesOnTheMap.add(new ObjetImage(result.get(i).getLatitude(), result.get(i).getLongitude(), result.get(i).getImagePath(), result.get(i).getSnippet(), result.get(i).getDate(), result.get(i).getType()));
	    			markerOnMaps.add(m);
	    		}
	    		
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
		  
		  map.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
		  
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
					  Marker m = map.addMarker(new MarkerOptions().position(currentPoint).title(result.get(i).getImagePath()));
					  imagesOnTheMap.add(new ObjetImage(result.get(i).getLatitude(), result.get(i).getLongitude(), result.get(i).getImagePath(), result.get(i).getSnippet(), result.get(i).getDate(), result.get(i).getType()));
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
	    	Marker m = map.addMarker(new MarkerOptions().position(currentPoint).title(result.get(0).getImagePath()));
	    	markerOnMaps.add(m);
			m.showInfoWindow();
	    	map.getUiSettings().setScrollGesturesEnabled(false);
	    	map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPoint, 9.0f),4000,MyCancelableCallback);
	    	//map.setOnInfoWindowClickListener(this);
	  }
	  
	  protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
		    super.onActivityResult(requestCode, resultCode, imageReturnedIntent); 
		    
		    if(requestCode == ACTION_GET_VIDEO) {
		    	Uri _uri = imageReturnedIntent.getData();
		    	String[] filePathColumn = { MediaStore.Images.Media.DATA };
	            Cursor cursor = getContentResolver().query(_uri,filePathColumn, null, null, null);
	            cursor.moveToFirst();
	            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	            String picturePath = cursor.getString(columnIndex);
	            cursor.close();
		    	markerClicked.setTitle(picturePath);
		    } else {
		    	Bundle bundle = imageReturnedIntent.getExtras();
		    	if(bundle!=null) {
		    		map.clear();
		    		map.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
		    		result = (ArrayList<ObjetImage>)imageReturnedIntent.getSerializableExtra("result");
		    		if(tripId != -1) {
		    			ArrayList<Point> points = pointDAO.getByTravelId(tripId);
		    			if(points != null) {
		    				for(Point p : points) {
		    					if(p.getType_id() == 3) {
		    						result.add(new ObjetImage(p.getLatitude(), p.getLongitude(), p.getUri(), p.getComment(), p.getDate_add(), 3));
		    					}
		    				}
		    			}
		    		}
		    		Collections.sort(result);
		    		
		    		for(ObjetImage objet : result) {
		    			System.out.println(objet.getDate() + " " + objet.getType());
		    		}
		    		
		    		polylineOnMaps = new ArrayList<Polyline>();
		    		markerOnMaps = new ArrayList<Marker>();
		    		imagesOnTheMap = new ArrayList<ObjetImage>();
		    		
		    		ArrayList<Marker> tmpMarker = new ArrayList<Marker>();
		    		
		    		for(Marker m : comMarker) {
		    			if(markerClicked.getTitle() != null) {
	    					Marker tmp = map.addMarker(new MarkerOptions()
			    			.position(new LatLng(m.getPosition().latitude, m.getPosition().longitude))
			    			.title(m.getTitle())
			    			.snippet(m.getSnippet())
			    			.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
			    			tmpMarker.add(tmp);
		    			} else {
		    				Marker tmp = map.addMarker(new MarkerOptions()
		    				.position(new LatLng(m.getPosition().latitude, m.getPosition().longitude))
		    				.title(m.getTitle())
		    				.snippet(m.getSnippet())
		    				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
		    				tmpMarker.add(tmp);
		    			}
		    		}
		    		
		    		comMarker = new ArrayList<Marker>();
		    		for(Marker m : tmpMarker) {
		    			comMarker.add(m);
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
}