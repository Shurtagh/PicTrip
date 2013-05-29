package ElementObject;

import DAO.PointTypeDAO;

import com.example.pic_trip.Menu;

public class PointType {
	
	/*
	 * Attributs de la classe
	 */
	private int id = -1;
	private String name;
	private String image;
	private int show;
	
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
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public int getShow() {
		return show;
	}
	public void setShow(int show) {
		this.show = show;
	}
	
	/*
	 * 
	 * CONSTRUCTEURS
	 * 
	 */
	
	public PointType(int id, String name, String image, int show) {
		this.id = id;
		this.name = name;
		this.image = image;
		this.show = show;
	}
	
	//A ne pas utiliser puisque l'on ne peut pas créer des types de point
	public PointType() {
		
	}
	
	/*
	 * 
	 * AUTRES
	 * 
	 */
	public void save() {
		PointTypeDAO PTDAO = new PointTypeDAO(Menu.getContext());
		if (this.id == -1) {
			PTDAO.addPointType(this);
		} else {
			PTDAO.updatePointType(this);
		}
	}
	
	public void delete() {
		if (this.id != -1) {
			PointTypeDAO PTDAO = new PointTypeDAO(Menu.getContext());
			PTDAO.deletePointType(this.id);
		}
	}
}
