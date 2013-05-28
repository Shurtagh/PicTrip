package com.example.pic_trip;

import java.io.Serializable;


public class ObjetImage implements Serializable {

		private float latitude;
		private float longitude;
		private String imagePath;
		private String snippet;
		
		public ObjetImage(float lat, float lon, String path, String s) {
			latitude = lat;
			longitude = lon;
			imagePath = path;
			snippet = s;
		}
		
		public void setSnippet(String s) {
			snippet = s;
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
