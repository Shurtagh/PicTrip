package com.example.pic_trip;

import java.io.Serializable;
import java.util.ArrayList;

public class MultipleImages implements Serializable {
	private ArrayList<ObjetImage> list;
	
	public MultipleImages() {
		list = new ArrayList<ObjetImage>();
	}
	
	public ArrayList<ObjetImage> getImages() {
		return list;
	}
}
