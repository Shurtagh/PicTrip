package com.example.pic_trip;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

class PopupAdapter implements InfoWindowAdapter {
  LayoutInflater inflater=null;

  PopupAdapter(LayoutInflater inflater) {
    this.inflater=inflater;
  }

  @Override
  public View getInfoWindow(Marker marker) {
    return(null);
  }

  @Override
  public View getInfoContents(Marker marker) {
    View popup=inflater.inflate(R.layout.popup, null);

    TextView tv=(TextView)popup.findViewById(R.id.title);

    tv.setText(marker.getTitle());
    tv=(TextView)popup.findViewById(R.id.snippet);
    if(marker.getSnippet() != null) {
    	if(marker.getSnippet().toString().startsWith("/")) {
    		File imgFile = new  File(marker.getSnippet());
    		if(imgFile.exists()){
    			Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
    			ImageView image = (ImageView)popup.findViewById(R.id.icon);
    			image.setImageBitmap(Bitmap.createScaledBitmap(myBitmap, 200, 320, false));
    		}
    	} else {
    		tv.setText(marker.getSnippet());
    	}
    }
    return(popup);
  }
}