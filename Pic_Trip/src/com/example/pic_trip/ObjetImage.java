package com.example.pic_trip;

import java.io.Serializable;


public class ObjetImage implements Serializable, Comparable {

		private float latitude;
		private float longitude;
		private String imagePath;
		private String snippet;
		private long date_taken;
		private int type;
		
		public ObjetImage(float lat, float lon, String path, String s, long d, int t) {
			latitude = lat;
			longitude = lon;
			imagePath = path;
			snippet = s;
			date_taken = d;
			type = t;
		}
		
		public int getType() {
			return type;
		}
		
		public void setSnippet(String s) {
			snippet = s;
		}
		
		public long getDate() {
			return date_taken;
		}
		
		public String getSnippet() {
			return snippet;
		}
		
		public float getLatitude() {
			return latitude;
		}
		
		public float getLongitude() {
			return longitude;
		}
		
		public String getImagePath() {
			return imagePath;
		}

		@Override
		public int compareTo(Object other) {
			 long nombre1 = ((ObjetImage) other).getDate(); 
		      long nombre2 = this.getDate(); 
		      if (nombre1 < nombre2)  return 1; 
		      else if(nombre1 == nombre2) return 0; 
		      else return -1; 
		}
}
