package ElementObject;

import DAO.TagDAO;

import com.example.pic_trip.Menu;

public class Tag {

	/*
	 * Attributs de la classe
	 */
	private int id = -1;
	private String name;
	private int level;
	private int superior_id;
	
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
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getSuperior_id() {
		return superior_id;
	}
	public void setSuperior_id(int superior_id) {
		this.superior_id = superior_id;
	}
	
	/*
	 * 
	 * CONSTRUCTEURS
	 * 
	 */
	
	public Tag(int id, String name, int level, int superior_id) {
		this.id = id;
		this.name = name;
		this.level= level;
		this.superior_id = superior_id;
	}
	
	public Tag() {
		
	}
	
	/*
	 * 
	 * AUTRES
	 * 
	 */
	public void save() {
		TagDAO TDAO = new TagDAO(Menu.getContext());
		if (this.id == -1) {
			TDAO.addTag(this);
		} else {
			TDAO.updateTag(this);
		}
	}
	
	public void delete() {
		if (this.id != -1) {
			TagDAO PTDAO = new TagDAO(Menu.getContext());
			PTDAO.deleteTag(this.id);
		}
	}
}
