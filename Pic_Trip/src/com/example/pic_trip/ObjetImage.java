package com.example.pic_trip;

import java.io.Serializable;


public class ObjetImage implements Serializable {

		private float latitude;
		private float longitude;
		private String imagePath;
		
		public ObjetImage(float lat, float lon, String path) {
			latitude = lat;
			longitude = lon;
			imagePath = path;
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
