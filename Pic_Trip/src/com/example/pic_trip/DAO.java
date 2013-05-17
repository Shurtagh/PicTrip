package com.example.pic_trip;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public abstract class DAO {
	
	//Database description
	protected final static int version = 1;
	protected final static String dbName = "dateBase.db";
	
	//Databse handler
	protected SQLiteDatabase mDb = null;
	protected DataBaseHandler mHandler = null;
	
	public DAO(Context pContext) {
		this.mHandler = new DataBaseHandler(pContext, dbName, null, version);
	}
	
	public SQLiteDatabase open() {
		mDb = mHandler.getWritableDatabase();
		return mDb;
	}
	
	public void close() {
		mDb.close();
	}
}
