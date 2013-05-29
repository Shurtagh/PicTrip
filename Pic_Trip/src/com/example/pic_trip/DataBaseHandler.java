package com.example.pic_trip;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHandler extends SQLiteOpenHelper{
	
	/*
	 * Nom des tables
	 */
	private String TRAVEL_TABLE = "travels";
	private String POINT_TABLE = "points";
	private String POINTTYPE_TABLE = "point_types";
	private String TAG_TABLE = "tags";
	private String POINTTOTAG_TABLE = "points_to_tags";
	
	/*
	 * Nom des champs
	 */
	
	//Travels
	private String TRAVEL_ID = "id";
	private String TRAVEL_NAME = "name";
	private String TRAVEL_DATE_START = "date_start";
	private String TRAVEL_DATE_STOP = "date_stop";
	private String TRAVEL_DESCRIPTION = "description";
	
	//Points
	private String POINT_ID = "id";
	private String POINT_TRAVEL_ID = "travel_id";
	private String POINT_TYPE_ID = "type_id";
	private String POINT_DATE_ADD = "date_add";
	private String POINT_LATITUDE = "latitude";
	private String POINT_LONGITUDE = "longitude";
	private String POINT_COMMENT = "comment";
	private String POINT_URI = "uri";
	private String POINT_ORDER = "order";
	
	//Point_types
	private String POINTTYPE_ID = "id";
	private String POINTTYPE_NAME = "name";
	private String POINTTYPE_IMAGE = "image";
	private String POINTTYPE_SHOW = "show";
	
	//Tags
	private String TAG_ID = "id";
	private String TAG_NAME = "name";
	private String TAG_LEVEL = "level";
	private String TAG_SUPERIOR_ID = "superior_id";
	
	//Points_to_tags
	private String POINTTOTAG_TAG_ID = "tag_id";
	private String POINTTOTAG_POINT_ID = "point_id";
	
	/*
	 * Requête de création de table
	 */
	
	private String CREATE_TRAVELS_STATEMENT = "CREATE TABLE " + TRAVEL_TABLE + " (" +
											  TRAVEL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
											  TRAVEL_NAME + " TEXT, " +
											  TRAVEL_DATE_START + " INTEGER, " +
											  TRAVEL_DATE_STOP + " INTEGER, " +
											  TRAVEL_DESCRIPTION + " TEXT);";
	private String CREATE_POINTS_STATEMENT = "CREATE TABLE " + POINT_TABLE + " (" +
											 POINT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
											 POINT_TRAVEL_ID + " INTEGER, " +
										     POINT_TYPE_ID + " INTEGER, " +
										     POINT_DATE_ADD + " INTEGER, " +
										     POINT_LATITUDE + " REAL, " +
										     POINT_LONGITUDE + " REAL, " +
										     POINT_COMMENT + " TEXT, " +
										  	 POINT_URI + " TEXT, " +
										     "`" + POINT_ORDER + "` INTEGER, " +
										     "FOREIGN KEY (" + POINT_TRAVEL_ID + ") REFERENCES " + TRAVEL_TABLE + "(" + TRAVEL_ID + "), " + 
										     "FOREIGN KEY (" + POINT_TYPE_ID + ") REFERENCES " + POINTTYPE_TABLE + "(" + POINTTYPE_ID + "));";
	private String CREATE_POINTTYPES_STATEMENT = "CREATE TABLE " + POINTTYPE_TABLE + " (" +
												 POINTTYPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
												 POINTTYPE_NAME + " TEXT, " +
											     POINTTYPE_IMAGE + " TEXT, " +
											  	 POINTTYPE_SHOW + " INTEGER);";
	private String CREATE_TAGS_STATEMENT = "CREATE TABLE " + TAG_TABLE + " (" +
										    TAG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
									        TAG_NAME + " TEXT, " +
									        TAG_LEVEL + " INTEGER, " +
									  	    TAG_SUPERIOR_ID + " INTEGER);";
	private String CREATE_POINTSTOTAGS_STATEMENT = "CREATE TABLE " + POINTTOTAG_TABLE + " (" +
												   POINTTOTAG_TAG_ID + " INTEGER, " +
											  	   POINTTOTAG_POINT_ID + " INTEGER, " + 
												   "PRIMARY KEY(" + POINTTOTAG_TAG_ID + ", " + POINTTOTAG_POINT_ID + "), " + 
											  	   "FOREIGN KEY (" + POINTTOTAG_TAG_ID + ") REFERENCES " + TAG_TABLE + "(" + TAG_ID + "), " + 
											  	   "FOREIGN KEY (" + POINTTOTAG_POINT_ID + ") REFERENCES " + POINT_TABLE + "(" + POINT_ID + "));";

	/*
	 * Requête de drop de table
	 */
	private String DROP_TRAVELS_STATEMENT = "DROP TABLE IF EXISTS " + TRAVEL_TABLE + ";";
	private String DROP_POINTS_STATEMENT = "DROP TABLE IF EXISTS " + POINT_TABLE + ";";
	private String DROP_TAGS_STATEMENT = "DROP TABLE IF EXISTS " + TAG_TABLE + ";";
	private String DROP_POINTSTOTAGS_STATEMENT = "DROP TABLE IF EXISTS " + POINTTOTAG_TABLE + ";";
	private String DROP_POINTTYPES_STATEMENT = "DROP TABLE IF EXISTS " + POINTTOTAG_TABLE + ";";
	
	public DataBaseHandler (Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TRAVELS_STATEMENT);
		db.execSQL(CREATE_POINTTYPES_STATEMENT);
		db.execSQL(CREATE_TAGS_STATEMENT);
		db.execSQL(CREATE_POINTS_STATEMENT);
		db.execSQL(CREATE_POINTSTOTAGS_STATEMENT);
		addDefaultPointType(db);
	}

	//Ajout des types de points
	public void addDefaultPointType(SQLiteDatabase db) {
		ContentValues value = new ContentValues();
		
		//Vidéo
		value.put(POINTTYPE_IMAGE, "");
		value.put(POINTTYPE_NAME, "Vidéo");
		value.put(POINTTYPE_SHOW, 1);
		db.insert(POINTTYPE_TABLE, null, value);
		
		//Commentaire
		value.put(POINTTYPE_IMAGE, "");
		value.put(POINTTYPE_NAME, "Commentaire");
		value.put(POINTTYPE_SHOW, 1);
		db.insert(POINTTYPE_TABLE, null, value);
		
		//Photo
		value.put(POINTTYPE_IMAGE, "");
		value.put(POINTTYPE_NAME, "Photo");
		value.put(POINTTYPE_SHOW, 1);
		db.insert(POINTTYPE_TABLE, null, value);
		
		//Tracking
		value.put(POINTTYPE_IMAGE, "");
		value.put(POINTTYPE_NAME, "Tracking");
		value.put(POINTTYPE_SHOW, 0);
		db.insert(POINTTYPE_TABLE, null, value);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion <=  1) {
			addDefaultPointType(db);
		}
		if (oldVersion <= 2) {
			String sql = "ALTER TABLE " + POINT_TABLE + " ADD COLUMN `" + POINT_ORDER + "` INTEGER;";
			db.execSQL(sql);
		}
		/*db.execSQL(DROP_TRAVELS_STATEMENT);
		db.execSQL(DROP_POINTTYPES_STATEMENT);
		db.execSQL(DROP_TAGS_STATEMENT);
		db.execSQL(DROP_POINTS_STATEMENT);
		db.execSQL(DROP_POINTSTOTAGS_STATEMENT);
		onCreate(db);*/
	}
	
}
