package com.example.pic_trip;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class TravelDAO extends DAO {
	
	/*
	 * Nom de table et de champs
	 */
	private String TRAVEL_TABLE = "travels";

	private String TRAVEL_ID = "id";
	private String TRAVEL_NAME = "name";
	private String TRAVEL_DATE_START = "date_start";
	private String TRAVEL_DATE_STOP = "date_stop";
	private String TRAVEL_DESCRIPTION = "description";
	
	public TravelDAO(Context pContext) {
		super(pContext);
	}
	
	public Travel getById(int id) {
		Cursor c = mDb.rawQuery("SELECT * FROM " + TRAVEL_TABLE + " WHERE " + TRAVEL_ID + " = ?", new String[]{String.valueOf(id)});
		if (c.getCount() > 1 || c.getCount() == 0) {
			return null;
		} else {
			return new Travel(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4));
		}
	}
	
	public ArrayList<Travel> getByName(String name) {
		Cursor c = mDb.rawQuery("SELECT * FROM " + TRAVEL_TABLE + " WHERE " + TRAVEL_NAME + " = ?", new String[]{name});
		if (c.getCount() == 0) {
			return null;
		} else {
			ArrayList<Travel> list = new ArrayList<Travel>();
			while (c.moveToNext()) {
				list.add(new Travel(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4)));
			}
			c.close();
			return list;
		}
	}
	
	public ArrayList<Travel> getByDateStart(String dateStart) {
		Cursor c = mDb.rawQuery("SELECT * FROM " + TRAVEL_TABLE + " WHERE " + TRAVEL_DATE_START + " = ?", new String[]{dateStart});
		if (c.getCount() == 0) {
			return null;
		} else {
			ArrayList<Travel> list = new ArrayList<Travel>();
			while (c.moveToNext()) {
				list.add(new Travel(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4)));
			}
			c.close();
			return list;
		}
	}
	
	public ArrayList<Travel> getByDateStop(String dateStop) {
		Cursor c = mDb.rawQuery("SELECT * FROM " + TRAVEL_TABLE + " WHERE " + TRAVEL_DATE_STOP + " = ?", new String[]{dateStop});
		if (c.getCount() == 0) {
			return null;
		} else {
			ArrayList<Travel> list = new ArrayList<Travel>();
			while (c.moveToNext()) {
				list.add(new Travel(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4)));
			}
			c.close();
			return list;
		}
	}
	
	public void addTravel(Travel t) {
		if (p.getId() == -1) {
			ContentValues value = new ContentValues();
			value.put(TRAVEL_NAME, t.getName());
			value.put(TRAVEL_DATE_START, t.getDate_start());
			value.put(TRAVEL_DATE_STOP, t.getDate_stop());
			value.put(TRAVEL_DESCRIPTION, t.getDescription());
			mDb.insert(TRAVEL_TABLE, null, value);
		}
	}
	
	public void updateTravel(Travel t) {
		if (p.getId() != -1) {
			ContentValues value = new ContentValues();
			value.put(TRAVEL_NAME, t.getName());
			value.put(TRAVEL_DATE_START, t.getDate_start());
			value.put(TRAVEL_DATE_STOP, t.getDate_stop());
			value.put(TRAVEL_DESCRIPTION, t.getDescription());
			mDb.update(TRAVEL_TABLE, value, TRAVEL_ID + " = ?", new String[] {String.valueOf(t.getId())});
		}
	}
	
	public void deletePoint(int id) {
		mDb.delete(TRAVEL_TABLE, TRAVEL_ID + " = ?", new String[] {String.valueOf(id)});
	}
}
