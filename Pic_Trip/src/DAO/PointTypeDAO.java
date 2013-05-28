package DAO;

import java.util.ArrayList;

import ElementObject.Point;
import ElementObject.PointType;
import ElementObject.Tag;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class PointTypeDAO extends DAO {

	/*
	 * Nom de table et de champs
	 */
	private String POINTTYPE_TABLE = "point_types";

	private String POINTTYPE_ID = "id";
	private String POINTTYPE_NAME = "name";
	private String POINTTYPE_IMAGE = "image";
	private String POINTTYPE_SHOW = "show";
	
	
	public PointTypeDAO(Context pContext) {
		super(pContext);
	}
	
	public PointType getById(int id) {
		Cursor c = mDb.rawQuery("SELECT * FROM " + POINTTYPE_TABLE + " WHERE " + POINTTYPE_ID + " = ?", new String[]{String.valueOf(id)});
		c.moveToFirst();
		if (c.getCount() > 1 || c.getCount() == 0) {
			return null;
		} else {
			return new PointType(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3));
		}
	}
	
	public ArrayList<PointType> getByName(String name) {
		Cursor c = mDb.rawQuery("SELECT * FROM " + POINTTYPE_TABLE + " WHERE " + POINTTYPE_NAME + " = ?", new String[]{String.valueOf(name)});
		if (c.getCount() == 0) {
			return null;
		} else {
			ArrayList<PointType> list = new ArrayList<PointType>();
			while (c.moveToNext()) {
				list.add(new PointType(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3)));
			}
			c.close();
			return list;
		}
	}
	
	public ArrayList<PointType> getByImage(String image) {
		Cursor c = mDb.rawQuery("SELECT * FROM " + POINTTYPE_TABLE + " WHERE " + POINTTYPE_IMAGE + " = ?", new String[]{String.valueOf(image)});
		if (c.getCount() == 0) {
			return null;
		} else {
			ArrayList<PointType> list = new ArrayList<PointType>();
			while (c.moveToNext()) {
				list.add(new PointType(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3)));
			}
			c.close();
			return list;
		}
	}
	
	public ArrayList<PointType> getByShow(int show) {
		Cursor c = mDb.rawQuery("SELECT * FROM " + POINTTYPE_TABLE + " WHERE " + POINTTYPE_SHOW + " = ?", new String[]{String.valueOf(show)});
		if (c.getCount() == 0) {
			return null;
		} else {
			ArrayList<PointType> list = new ArrayList<PointType>();
			while (c.moveToNext()) {
				list.add(new PointType(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3)));
			}
			c.close();
			return list;
		}
	}
	
	public ArrayList<PointType> getAll() {
		Cursor c = mDb.rawQuery("SELECT * FROM " + POINTTYPE_TABLE, null);
		if (c.getCount() == 0) {
			return null;
		} else {
			ArrayList<PointType> list = new ArrayList<PointType>();
			while (c.moveToNext()) {
				list.add(new PointType(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3)));
			}
			c.close();
			return list;
		}
	}

	public void deleteAll() {
		mDb.delete(POINTTYPE_TABLE, null, null);
	}
	
	public void addPointType(PointType pt) {
		if (pt.getId() == -1) {
			ContentValues value = new ContentValues();
			value.put(POINTTYPE_NAME, pt.getName());
			value.put(POINTTYPE_IMAGE, pt.getImage());
			value.put(POINTTYPE_SHOW, pt.getShow());
			mDb.insert(POINTTYPE_TABLE, null, value);
		}
	}
	
	public void updatePointType(PointType pt) {
		if (pt.getId() != -1) {
			ContentValues value = new ContentValues();
			value.put(POINTTYPE_NAME, pt.getName());
			value.put(POINTTYPE_IMAGE, pt.getImage());
			value.put(POINTTYPE_SHOW, pt.getShow());
			mDb.update(POINTTYPE_TABLE, value, POINTTYPE_ID + " = ?", new String[] {String.valueOf(pt.getId())});
		}
	}
	
	public void deletePointType(int id) {
		mDb.delete(POINTTYPE_TABLE, POINTTYPE_ID + " = ?", new String[] {String.valueOf(id)});
	}
}
