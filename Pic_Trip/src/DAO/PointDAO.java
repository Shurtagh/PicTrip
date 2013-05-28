package DAO;

import java.util.ArrayList;


import ElementObject.Point;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class PointDAO extends DAO {
	
	/*
	 * Nom de table et de champs
	 */
	private String POINT_TABLE = "points";

	private String POINT_ID = "id";
	private String POINT_TRAVEL_ID = "travel_id";
	private String POINT_TYPE_ID = "type_id";
	private String POINT_DATE_ADD = "date_add";
	private String POINT_LATITUDE = "latitude";
	private String POINT_LONGITUDE = "longitude";
	private String POINT_COMMENT = "comment";
	private String POINT_URI = "uri";
	
	public PointDAO(Context pContext) {
		super(pContext);
	}
	
	public Point getById(int id) {
		Cursor c = mDb.rawQuery("SELECT * FROM " + POINT_TABLE + " WHERE " + POINT_ID + " = ?", new String[]{String.valueOf(id)});
		c.moveToFirst();
		if (c.getCount() > 1 || c.getCount() == 0) {
			return null;
		} else {
			return new Point(c.getInt(0), c.getInt(1), c.getInt(2), c.getString(3), c.getFloat(4), c.getFloat(5), c.getString(6), c.getString(7));
		}
	}
	
	public ArrayList<Point> getByTravelId(int travel_id) {
		Cursor c = mDb.rawQuery("SELECT * FROM " + POINT_TABLE + " WHERE " + POINT_TRAVEL_ID + " = ?", new String[]{String.valueOf(travel_id)});
		if (c.getCount() == 0) {
			return null;
		} else {
			ArrayList<Point> list = new ArrayList<Point>();
			while (c.moveToNext()) {
				list.add(new Point(c.getInt(0), c.getInt(1), c.getInt(2), c.getString(3), c.getFloat(4), c.getFloat(5), c.getString(6), c.getString(7)));
			}
			c.close();
			return list;
		}
	}
	
	public ArrayList<Point> getByTypeId(int type_id) {
		Cursor c = mDb.rawQuery("SELECT * FROM " + POINT_TABLE + " WHERE " + POINT_TYPE_ID + " = ?", new String[]{String.valueOf(type_id)});
		if (c.getCount() == 0) {
			return null;
		} else {
			ArrayList<Point> list = new ArrayList<Point>();
			while (c.moveToNext()) {
				list.add(new Point(c.getInt(0), c.getInt(1), c.getInt(2), c.getString(3), c.getFloat(4), c.getFloat(5), c.getString(6), c.getString(7)));
			}
			c.close();
			return list;
		}
	}
	
	public ArrayList<Point> getByDateAdd(String date_add) {
		Cursor c = mDb.rawQuery("SELECT * FROM " + POINT_TABLE + " WHERE " + POINT_DATE_ADD + " = ?", new String[]{date_add});
		if (c.getCount() == 0) {
			return null;
		} else {
			ArrayList<Point> list = new ArrayList<Point>();
			while (c.moveToNext()) {
				list.add(new Point(c.getInt(0), c.getInt(1), c.getInt(2), c.getString(3), c.getFloat(4), c.getFloat(5), c.getString(6), c.getString(7)));
			}
			c.close();
			return list;
		}
	}
	
	public ArrayList<Point> getAll() {
		Cursor c = mDb.rawQuery("SELECT * FROM " + POINT_TABLE, null);
		if (c.getCount() == 0) {
			return null;
		} else {
			ArrayList<Point> list = new ArrayList<Point>();
			while (c.moveToNext()) {
				list.add(new Point(c.getInt(0), c.getInt(1), c.getInt(2), c.getString(3), c.getFloat(4), c.getFloat(5), c.getString(6), c.getString(7)));
			}
			c.close();
			return list;
		}
	}

	public void deleteAll() {
		mDb.delete(POINT_TABLE, null, null);
	}
	
	public void addPoint(Point p) {
		if (p.getId() == -1) {
			ContentValues value = new ContentValues();
			value.put(POINT_TRAVEL_ID, p.getTravel_id());
			value.put(POINT_TYPE_ID, p.getType_id());
			value.put(POINT_DATE_ADD, p.getDate_add());
			value.put(POINT_LATITUDE, p.getLatitude());
			value.put(POINT_LONGITUDE, p.getLongitude());
			value.put(POINT_COMMENT, p.getComment());
			value.put(POINT_URI, p.getUri());
			mDb.insert(POINT_TABLE, null, value);
		}
	}
	
	public void updatePoint(Point p) {
		if (p.getId() != -1) {
			ContentValues value = new ContentValues();
			value.put(POINT_TRAVEL_ID, p.getTravel_id());
			value.put(POINT_TYPE_ID, p.getType_id());
			value.put(POINT_DATE_ADD, p.getDate_add());
			value.put(POINT_LATITUDE, p.getLatitude());
			value.put(POINT_LONGITUDE, p.getLongitude());
			value.put(POINT_COMMENT, p.getComment());
			value.put(POINT_URI, p.getUri());
			mDb.update(POINT_TABLE, value, POINT_ID + " = ?", new String[] {String.valueOf(p.getId())});
		}
	}
	
	public void deletePoint(int id) {
		mDb.delete(POINT_TABLE, POINT_ID + " = ?", new String[] {String.valueOf(id)});
	}
}
