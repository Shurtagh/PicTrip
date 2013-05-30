package com.example.pic_trip;

import DAO.PointDAO;
import DAO.TravelDAO;
import ElementObject.Point;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class Gps extends Activity {

	private LocationManager lm;
	boolean enable;
	LocationListener locationListener;
	private PointDAO pointDAO;
	private TravelDAO travelDAO;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Menu.context = getApplicationContext();
	    pointDAO = new PointDAO(Menu.getContext());
	    travelDAO = new TravelDAO(Menu.getContext());
        
        Intent int_ = getIntent();
	    enable = int_.getBooleanExtra("enable", true);
	    
	    locationListener = new GPSLocationListener(); 
	    
    	lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    	lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 1, locationListener);
    }
    
    private class GPSLocationListener implements LocationListener 
    {
      @Override
      public void onLocationChanged(Location location) {
    	  //if(!enable) {
    		//  lm.removeUpdates(locationListener);
    	  //}
    	
        float lat = Float.valueOf((float)location.getLatitude());
        float lon = Float.valueOf((float)location.getLongitude());
        Point p = new Point(travelDAO.getCurrentTravel().getId(),3,location.getTime(),lat,lon,null,null,0);
		p.save();
		for(Point g : pointDAO.getAll()) {
			System.out.println(g.getId() + " "+g.getDate_add() + " "+g.getLatitude() + " "+g.getLongitude() + " "+g.getTravel_id() + " " +g.getType_id());
		}
      }

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
      }
}