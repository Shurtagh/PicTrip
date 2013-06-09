package DAO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import ElementObject.Point;
import ElementObject.PointType;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.pic_trip.Menu;

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
	private String POINT_ORDER = "order";
	
	public PointDAO(Context pContext) {
		super(pContext);
	}
	
	public Point getById(int id) {
		Cursor c = mDb.rawQuery("SELECT * FROM " + POINT_TABLE + " WHERE " + POINT_ID + " = ?", new String[]{String.valueOf(id)});
		c.moveToFirst();
		if (c.getCount() > 1 || c.getCount() == 0) {
			return null;
		} else {
			return new Point(c.getInt(0), c.getInt(1), c.getInt(2), c.getLong(3), c.getFloat(4), c.getFloat(5), c.getString(6), c.getString(7), c.getInt(8));
		}
	}
	
	public ArrayList<Point> getByTravelId(int travel_id) {
		Cursor c = mDb.rawQuery("SELECT * FROM " + POINT_TABLE + " WHERE " + POINT_TRAVEL_ID + " = ? ORDER BY " + POINT_DATE_ADD, new String[]{String.valueOf(travel_id)});
		if (c.getCount() == 0) {
			return null;
		} else {
			ArrayList<Point> list = new ArrayList<Point>();
			while (c.moveToNext()) {
				list.add(new Point(c.getInt(0), c.getInt(1), c.getInt(2), c.getLong(3), c.getFloat(4), c.getFloat(5), c.getString(6), c.getString(7), c.getInt(8)));
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
				list.add(new Point(c.getInt(0), c.getInt(1), c.getInt(2), c.getLong(3), c.getFloat(4), c.getFloat(5), c.getString(6), c.getString(7), c.getInt(8)));
			}
			c.close();
			return list;
		}
	}
	
	public ArrayList<Point> getByDateAdd(int date_add) {
		Cursor c = mDb.rawQuery("SELECT * FROM " + POINT_TABLE + " WHERE " + POINT_DATE_ADD + " = ?", new String[]{String.valueOf(date_add)});
		if (c.getCount() == 0) {
			return null;
		} else {
			ArrayList<Point> list = new ArrayList<Point>();
			while (c.moveToNext()) {
				list.add(new Point(c.getInt(0), c.getInt(1), c.getInt(2), c.getLong(3), c.getFloat(4), c.getFloat(5), c.getString(6), c.getString(7), c.getInt(8)));
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
				list.add(new Point(c.getInt(0), c.getInt(1), c.getInt(2), c.getLong(3), c.getFloat(4), c.getFloat(5), c.getString(6), c.getString(7), c.getInt(8)));
			}
			c.close();
			return list;
		}
	}
	
	public ArrayList<Point> getPointByLatitudeAndLongitude(int latitude, int longitude) {
		Cursor c = mDb.rawQuery("SELECT * FROM " + POINT_TABLE + " WHERE " + POINT_LATITUDE + " = ? AND " + POINT_LONGITUDE + " = ?", new String[]{String.valueOf(latitude), String.valueOf(longitude)});
		if (c.getCount() == 0) {
			return null;
		} else {
			ArrayList<Point> list = new ArrayList<Point>();
			while (c.moveToNext()) {
				list.add(new Point(c.getInt(0), c.getInt(1), c.getInt(2), c.getLong(3), c.getFloat(4), c.getFloat(5), c.getString(6), c.getString(7), c.getInt(8)));
			}
			c.close();
			return list;
		}
	}

	public ArrayList<String> getDistinctDayOfTravel(int travel_id) {
		Cursor c = mDb.rawQuery("SELECT " + POINT_DATE_ADD + " FROM " + POINT_TABLE + " WHERE " + POINT_TRAVEL_ID + " = ? ORDER BY " + POINT_DATE_ADD, new String[]{String.valueOf(travel_id)});
		if (c.getCount() == 0) {
			return null;
		} else {
			ArrayList<String> list = new ArrayList<String>();
			while (c.moveToNext()) {
				long timestamp = c.getLong(0);
				SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd LLL yyyy", Locale.FRANCE);
				String value = sdf.format(new Date(timestamp));
				if (!list.contains(value)) {
					list.add(value);
				}
			}
			c.close();
			return list;
		}
	}
	
	public ArrayList<Point> getAllPhotosOfDay(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd LLL yyyy", Locale.FRANCE);
		try {
			Date dateObject = (Date)sdf.parse(date);
			long timestamp_debut = dateObject.getTime();
			long timestamp_end = timestamp_debut + 86400000;
			Cursor c = mDb.rawQuery("SELECT * FROM " + POINT_TABLE + " WHERE " + POINT_DATE_ADD + " > ? AND " + POINT_DATE_ADD + " < ? ORDER BY " + POINT_DATE_ADD, new String[]{String.valueOf(timestamp_debut), String.valueOf(timestamp_end)});
			if (c.getCount() == 0) {
				return null;
			} else {
				ArrayList<Point> list = new ArrayList<Point>();
				while (c.moveToNext()) {
					list.add(new Point(c.getInt(0), c.getInt(1), c.getInt(2), c.getLong(3), c.getFloat(4), c.getFloat(5), c.getString(6), c.getString(7), c.getInt(8)));
				}
				c.close();
				return list;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<Point> getAllPhotosOfTravel(int travel_id) {
		Cursor c = mDb.rawQuery("SELECT * FROM " + POINT_TABLE + " WHERE " + POINT_TRAVEL_ID + " = ? ORDER BY " + POINT_DATE_ADD, new String[]{String.valueOf(travel_id)});
		if (c.getCount() == 0) {
			return null;
		} else {
			ArrayList<Point> list = new ArrayList<Point>();
			while (c.moveToNext()) {
				list.add(new Point(c.getInt(0), c.getInt(1), c.getInt(2), c.getLong(3), c.getFloat(4), c.getFloat(5), c.getString(6), c.getString(7), c.getInt(8)));
			}
			c.close();
			return list;
		}
	}
	
	public ArrayList<Point> getAllVideosOfTravel(int travel_id) {
		PointTypeDAO PTDAO = new PointTypeDAO(Menu.getContext());
		PointType pt = PTDAO.getByName("Vidéo");
		Cursor c = mDb.rawQuery("SELECT * FROM " + POINT_TABLE + " WHERE " + POINT_TYPE_ID + " = ? AND " + POINT_TRAVEL_ID + " = ? ORDER BY " + POINT_DATE_ADD, new String[]{String.valueOf(pt.getId()), String.valueOf(travel_id)});
		if (c.getCount() == 0) {
			return null;
		} else {
			ArrayList<Point> list = new ArrayList<Point>();
			while (c.moveToNext()) {
				list.add(new Point(c.getInt(0), c.getInt(1), c.getInt(2), c.getLong(3), c.getFloat(4), c.getFloat(5), c.getString(6), c.getString(7), c.getInt(8)));
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
			value.put("`" + POINT_ORDER + "`", p.getOrder());
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
			value.put("`" + POINT_ORDER + "`", p.getOrder());
			mDb.update(POINT_TABLE, value, POINT_ID + " = ?", new String[] {String.valueOf(p.getId())});
		}
	}
	
	public void deletePoint(int id) {
		mDb.delete(POINT_TABLE, POINT_ID + " = ?", new String[] {String.valueOf(id)});
	}
	
	public void deleteAllPointOfTravel(int travel_id) {
		mDb.delete(POINT_TABLE, POINT_TRAVEL_ID + " = ?", new String[] {String.valueOf(travel_id)});
	}
	
	public void deleteAllPointOfTravelExceptTracking(int travel_id) {
		PointTypeDAO PTDAO = new PointTypeDAO(Menu.getContext());
		PointType pt = PTDAO.getByName("Tracking");
		mDb.delete(POINT_TABLE, POINT_TYPE_ID + " <> ?", new String[] {String.valueOf(pt.getId())});
	}
}
