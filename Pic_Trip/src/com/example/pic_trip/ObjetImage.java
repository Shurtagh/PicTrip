package com.example.pic_trip;

import java.io.Serializable;


public class ObjetImage implements Serializable {

		private float latitude;
		private float longitude;
		private String imagePath;
		private String snippet;
		private long date_taken;
		
		public ObjetImage(float lat, float lon, String path, String s, long d) {
			latitude = lat;
			longitude = lon;
			imagePath = path;
			snippet = s;
			date_taken = d;
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
}
