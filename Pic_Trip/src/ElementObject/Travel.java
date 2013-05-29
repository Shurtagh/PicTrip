package ElementObject;

import DAO.TravelDAO;

import com.example.pic_trip.Menu;

public class Travel {
	
	/*
	 * Attributs de la classe
	 */
	private int id = -1;
	private String name;
	private long date_start;
	private long date_stop;
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
	public long getDate_start() {
		return date_start;
	}
	public void setDate_start(long date_start) {
		this.date_start = date_start;
	}
	public long getDate_stop() {
		return date_stop;
	}
	public void setDate_stop(long date_stop) {
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
	
	public Travel (int id, String name, long date_start, long date_stop, String description) {
		this.id = id;
		this.name = name;
		this.date_start = date_start;
		this.date_stop = date_stop;
		this.description = description;
	}
	
	public Travel (String name, long date_start, long date_stop, String description) {
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
		TravelDAO TDAO = new TravelDAO(Menu.getContext());
		if (this.id == -1) {
			TDAO.addTravel(this);
		} else {
			TDAO.updateTravel(this);
		}
	}
	
	public void delete() {
		if (this.id != -1) {
			TravelDAO TDAO = new TravelDAO(Menu.getContext());
			TDAO.deleteTravel(this.id);
		}
	}
}
