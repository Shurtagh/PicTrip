package DAO;

import java.util.ArrayList;

import ElementObject.Point;
import ElementObject.PointType;
import ElementObject.Tag;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class TagDAO extends DAO{

	/*
	 * Nom de table et de champs
	 */
	private String TAG_TABLE = "point_types";

	private String TAG_ID = "id";
	private String TAG_NAME = "name";
	private String TAG_LEVEL = "level";
	private String TAG_SUPERIOR_ID = "superior_id";
	
	public TagDAO(Context pContext) {
		super(pContext);
	}
	
	public Tag getById(int id) {
		Cursor c = mDb.rawQuery("SELECT * FROM " + TAG_TABLE + " WHERE " + TAG_ID + " = ?", new String[]{String.valueOf(id)});
		c.moveToFirst();
		if (c.getCount() > 1 || c.getCount() == 0) {
			return null;
		} else {
			return new Tag(c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3));
		}
	}
	
	public ArrayList<Tag> getByName(String name) {
		Cursor c = mDb.rawQuery("SELECT * FROM " + TAG_TABLE + " WHERE " + TAG_NAME + " = ?", new String[]{String.valueOf(name)});
		if (c.getCount() == 0) {
			return null;
		} else {
			ArrayList<Tag> list = new ArrayList<Tag>();
			while (c.moveToNext()) {
				list.add(new Tag(c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3)));
			}
			c.close();
			return list;
		}
	}
	
	public ArrayList<Tag> getByLevel(int level) {
		Cursor c = mDb.rawQuery("SELECT * FROM " + TAG_TABLE + " WHERE " + TAG_LEVEL + " = ?", new String[]{String.valueOf(level)});
		if (c.getCount() == 0) {
			return null;
		} else {
			ArrayList<Tag> list = new ArrayList<Tag>();
			while (c.moveToNext()) {
				list.add(new Tag(c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3)));
			}
			c.close();
			return list;
		}
	}
	
	public ArrayList<Tag> getBySuperiorId(int superior_id) {
		Cursor c = mDb.rawQuery("SELECT * FROM " + TAG_TABLE + " WHERE " + TAG_SUPERIOR_ID + " = ?", new String[]{String.valueOf(superior_id)});
		if (c.getCount() == 0) {
			return null;
		} else {
			ArrayList<Tag> list = new ArrayList<Tag>();
			while (c.moveToNext()) {
				list.add(new Tag(c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3)));
			}
			c.close();
			return list;
		}
	}
	
	public ArrayList<Tag> getAll() {
		Cursor c = mDb.rawQuery("SELECT * FROM " + TAG_TABLE, null);
		if (c.getCount() == 0) {
			return null;
		} else {
			ArrayList<Tag> list = new ArrayList<Tag>();
			while (c.moveToNext()) {
				list.add(new Tag(c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3)));
			}
			c.close();
			return list;
		}
	}

	public void deleteAll() {
		mDb.delete(TAG_TABLE, null, null);
	}
	
	public void addTag(Tag t) {
		if (t.getId() == -1) {
			ContentValues value = new ContentValues();
			value.put(TAG_NAME, t.getName());
			value.put(TAG_LEVEL, t.getLevel());
			value.put(TAG_SUPERIOR_ID, t.getSuperior_id());
			mDb.insert(TAG_TABLE, null, value);
		}
	}
	
	public void updateTag(Tag t) {
		if (t.getId() != -1) {
			ContentValues value = new ContentValues();
			value.put(TAG_NAME, t.getName());
			value.put(TAG_LEVEL, t.getLevel());
			value.put(TAG_SUPERIOR_ID, t.getSuperior_id());
			mDb.update(TAG_TABLE, value, TAG_ID + " = ?", new String[] {String.valueOf(t.getId())});
		}
	}
	
	public void deleteTag(int id) {
		mDb.delete(TAG_TABLE, TAG_ID + " = ?", new String[] {String.valueOf(id)});
	}
}
