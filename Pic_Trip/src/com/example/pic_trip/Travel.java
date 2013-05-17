package com.example.pic_trip;

public class Travel {
	
	/*
	 * Attributs de la classe
	 */
	private int id = -1;
	private String name;
	private String date_start;
	private String date_stop;
	private String description;
	
	/*
	 * 
	 * GETTERS AND SETTERS
	 * 
	 */
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDate_start() {
		return date_start;
	}
	public void setDate_start(String date_start) {
		this.date_start = date_start;
	}
	public String getDate_stop() {
		return date_stop;
	}
	public void setDate_stop(String date_stop) {
		this.date_stop = date_stop;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	/*
	 * 
	 * CONSTRUCTEURS
	 * 
	 */
	
	public Travel (int id, String name, String date_start, String date_stop, String description) {
		this.id = id;
		this.name = name;
		this.date_start = date_start;
		this.date_stop = date_stop;
		this.description = description;
	}
	
	public Travel () {
		
	}
	
	/*
	 * 
	 * AUTRES
	 * 
	 */
	
	public void save() {
		TravelDAO TDAO = new TravelDAO();
		if (this.id == -1) {
			TDAO.addTravel(this);
		} else {
			TDAO.updateTravel(this);
		}
	}
	
	public void delete() {
		if (this.id != -1) {
			TravelDAO TDAO = new TravelDAO();
			TDAO.deleteTravel(this.id);
		}
	}
}
