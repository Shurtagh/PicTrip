package DAO;

import com.example.pic_trip.DataBaseHandler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public abstract class DAO {
	
	//Database description
	protected final static int version = 1;
	protected final static String dbName = "dateBase.db";
	
	//Database handler
	protected SQLiteDatabase mDb = null;
	protected DataBaseHandler mHandler = null;
	
	public DAO(Context pContext) {
		this.mHandler = new DataBaseHandler(pContext, dbName, null, version);
		this.open();
	}
	
	public SQLiteDatabase open() {
		this.mDb = mHandler.getWritableDatabase();
		return mDb;
	}
	
	public void close() {
		mDb.close();
	}
}
