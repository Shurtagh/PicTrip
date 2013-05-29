package ElementObject;

import DAO.PointDAO;

import com.example.pic_trip.Menu;

public class Point {
	
	/*
	 * Attributs de la classe
	 */
	private int id = -1;
	private int travel_id;
	private int type_id;
	private long date_add;
	private float latitude;
	private float longitude;
	private String comment;
	private String uri;
	private int order;
	
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
	public int getTravel_id() {
		return travel_id;
	}
	public void setTravel_id(int travel_id) {
		this.travel_id = travel_id;
	}
	public int getType_id() {
		return type_id;
	}
	public void setType_id(int type_id) {
		this.type_id = type_id;
	}
	public long getDate_add() {
		return date_add;
	}
	public void setDate_add(long date_add) {
		this.date_add = date_add;
	}
	public float getLatitude() {
		return latitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	public float getLongitude() {
		return longitude;
	}
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	
	/*
	 * 
	 * CONSTRUCTEURS
	 * 
	 */
	public Point (int id, int travel_id, int type_id, long date_add, float latitude, float longitude, String comment, String uri, int order) {
		this.id = id;
		this.travel_id = travel_id;
		this.type_id = type_id;
		this.date_add = date_add;
		this.latitude = latitude;
		this.longitude = longitude;
		this.comment = comment;
		this.uri = uri;
		this.order = order;
	}
	
	public Point (int travel_id, int type_id, long date_add, float latitude, float longitude, String comment, String uri, int order) {
		this.travel_id = travel_id;
		this.type_id = type_id;
		this.date_add = date_add;
		this.latitude = latitude;
		this.longitude = longitude;
		this.comment = comment;
		this.uri = uri;
		this.order = order;
	}
	
	public Point () {
		
	}
	
	/*
	 * 
	 * AUTRES
	 * 
	 */
	public void save() {
		PointDAO PDAO = new PointDAO(Menu.getContext());
		if (this.id == -1) {
			PDAO.addPoint(this);
		} else {
			PDAO.updatePoint(this);
		}
	}
	
	public void delete() {
		if (this.id != -1) {
			PointDAO PDAO = new PointDAO(Menu.getContext());
			PDAO.deletePoint(this.id);
		}
	}
}
