package com.example.pic_trip;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

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
	  View popup;
	  TextView tv;
	  
	  if(marker.getTitle() != null) {
		  if(marker.getTitle().toString().contains("VID")) {
			  popup=inflater.inflate(R.layout.popupvideo, null);
			  tv=(TextView)popup.findViewById(R.id.snippet);
			  MediaMetadataRetriever retriever = new MediaMetadataRetriever();
			  retriever.setDataSource(marker.getTitle());
			  Bitmap bitmap=retriever.getFrameAtTime(1000);
			  ImageView image = (ImageView)popup.findViewById(R.id.preview);
			  image.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 200, 320, false));
		  } else {
			  popup=inflater.inflate(R.layout.popup, null);
			  tv=(TextView)popup.findViewById(R.id.snippet);
			  File imgFile = new  File(marker.getTitle());
	    		if(imgFile.exists()){
	    			Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
	    			ImageView image = (ImageView)popup.findViewById(R.id.icon);
	    			image.setImageBitmap(Bitmap.createScaledBitmap(myBitmap, 200, 320, false));
	    		}
		  }
	  } else {
		  popup=inflater.inflate(R.layout.popup, null);
		  tv=(TextView)popup.findViewById(R.id.snippet);
	  }
	  
    tv.setText(marker.getSnippet());
    
    return(popup);
  }
}